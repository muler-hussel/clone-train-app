package org.clone.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.clone.business.domain.SkToken;
import org.clone.business.domain.SkTokenExample;
import org.clone.business.mapper.SkTokenMapper;
import org.clone.business.mapper.cust.SkTokenMapperCust;
import org.clone.business.req.SkTokenQueryReq;
import org.clone.business.req.SkTokenSaveReq;
import org.clone.business.resp.SkTokenQueryResp;
import org.clone.common.response.PageRes;
import org.clone.common.util.SnowUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.clone.business.enums.RedisKeyPreEnum.SK_TOKEN;
import static org.clone.business.enums.RedisKeyPreEnum.SK_TOKEN_COUNT;

@Service
public class SkTokenService {

    private static final Logger LOG = LoggerFactory.getLogger(SkTokenService.class);

    @Resource
    private SkTokenMapper skTokenMapper;

    @Resource
    private DailyTrainSeatService dailyTrainSeatService;

    @Resource
    private DailyTrainStationService dailyTrainStationService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Resource
    private SkTokenMapperCust skTokenMapperCust;

    @Value("${spring.profiles.active}")
    private String env;

    public void save(SkTokenSaveReq req) {
        DateTime now = DateTime.now();
        SkToken skToken = BeanUtil.copyProperties(req, SkToken.class);
        if (ObjectUtil.isNull(skToken.getId())) {
            skToken.setId(SnowUtil.getSnowflakeNextId());
            skToken.setCreateTime(now);
            skToken.setUpdateTime(now);
            skTokenMapper.insert(skToken);
        } else {
            skToken.setUpdateTime(now);
            skTokenMapper.updateByPrimaryKey(skToken);
        }
    }

    public PageRes<SkTokenQueryResp> queryList(SkTokenQueryReq req) {
        SkTokenExample skTokenExample = new SkTokenExample();
        skTokenExample.setOrderByClause("id desc");
        SkTokenExample.Criteria criteria = skTokenExample.createCriteria();

        LOG.info("Pages: {}", req.getPage());
        LOG.info("Total: {}", req.getSize());
        PageHelper.startPage(req.getPage(), req.getSize());
        List<SkToken> skTokenList = skTokenMapper.selectByExample(skTokenExample);

        PageInfo<SkToken> pageInfo = new PageInfo<>(skTokenList);
        LOG.info("Rows: {}", pageInfo.getTotal());
        LOG.info("Pages: {}", pageInfo.getPages());

        List<SkTokenQueryResp> list = BeanUtil.copyToList(skTokenList, SkTokenQueryResp.class);

        PageRes<SkTokenQueryResp> pageResp = new PageRes<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public void delete(Long id) {
        skTokenMapper.deleteByPrimaryKey(id);
    }

    public void genDaily (Date date, String trainCode) {
        LOG.info("Delete token records of {} on {}", trainCode, DateUtil.formatDate(date));
        SkTokenExample skTokenExample = new SkTokenExample();
        skTokenExample.createCriteria().andDateEqualTo(date).andTrainCodeEqualTo(trainCode);
        skTokenMapper.deleteByExample(skTokenExample);

        DateTime now = DateTime.now();
        SkToken skToken = new SkToken();
        skToken.setDate(date);
        skToken.setTrainCode(trainCode);
        skToken.setId(SnowUtil.getSnowflakeNextId());
        skToken.setCreateTime(now);
        skToken.setUpdateTime(now);

        int countSeat = dailyTrainSeatService.countSeat(date, trainCode);
        long stationCount = dailyTrainStationService.countByTrainCode(date, trainCode);
        //根据实际过往买票情况确定比例，如一般火车可以买总座位的3/4
        int count = (int) (countSeat * stationCount * 3/4);
        LOG.info("{} generated {} tokens", trainCode, count);
        skToken.setCount(count);
        skTokenMapper.insert(skToken);
    }


    public boolean validSkToken(Date date, String trainCode, Long memberId) {
        LOG.info("Start to get {}'s token of {} on {}", memberId, trainCode, DateUtil.formatDate(date));

        if (!env.equals("dev")) {
            //防止机器人刷票，先获取令牌锁
            String lockKey = SK_TOKEN + DateUtil.formatDate(date) + "-" + trainCode + memberId;
            Boolean setIfAbsent = redisTemplate.opsForValue().setIfAbsent(lockKey, lockKey, 5, TimeUnit.SECONDS);
            if (Boolean.TRUE.equals(setIfAbsent)) {
                LOG.info("Get token lock");
            } else {
                LOG.info("Fail to get token lock");
                return false;
            }
        }

        //从缓存中获取token数量
        String skTokenCountKey = SK_TOKEN_COUNT + "-" + DateUtil.formatDate(date) + trainCode;
        Object skTokenCount = redisTemplate.opsForValue().get(skTokenCountKey);
        if (skTokenCount != null) {
            Long count = redisTemplate.opsForValue().decrement(skTokenCountKey, 1);
            if (count < 0L) {
                LOG.info("Fail to get token lock");
                return false;
            } else {
                LOG.info("After getting token lock, remaining {} tokens", count);
                //重置缓存，使之长期存在
                redisTemplate.expire(skTokenCountKey, 60, TimeUnit.SECONDS);
                //每获取5个更新数据库
                if (count % 5 == 0) skTokenMapperCust.decrease(date, trainCode, 5);
                return true;
            }
        } else {
            LOG.info("No tokens in cache");
            //检查数据库中是否有令牌
            SkTokenExample skTokenExample = new SkTokenExample();
            skTokenExample.createCriteria().andDateEqualTo(date).andTrainCodeEqualTo(trainCode);
            List<SkToken> tokenCountList = skTokenMapper.selectByExample(skTokenExample);
            if (CollUtil.isEmpty(tokenCountList)) {
                LOG.info("No token record");
                return false;
            }

            SkToken skToken = tokenCountList.get(0);
            if (skToken.getCount() <= 0) {
                LOG.info("No tokens");
                return false;
            }
            //还有令牌
            Integer count = skToken.getCount() - 1;
            skToken.setCount(count);
            LOG.info("Put token records into cache");
            redisTemplate.opsForValue().set(skTokenCountKey, String.valueOf(count), 60, TimeUnit.SECONDS);
            //skTokenMapper.updateByPrimaryKey(skToken);
            return true;
        }
//        int updateCount = skTokenMapperCust.decrease(date, trainCode); //返回的是update sql语句改变了几条数据
//        if (updateCount > 0) {
//            return true;
//        } else {
//            return false;
//        }
    }
}
