package org.clone.member.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.clone.member.response.PassengerQueryRes;
import org.clone.common.context.LoginMemberContext;
import org.clone.member.domain.Passenger;
import org.clone.member.domain.PassengerExample;
import org.clone.member.mapper.PassengerMapper;
import org.clone.member.request.PassengerQueryReq;
import org.clone.member.request.PassengerSaveRequest;
import org.clone.common.response.PageRes;
import org.clone.common.util.SnowUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassengerService {

    private static final Logger LOG = LoggerFactory.getLogger(PassengerService.class);
    @Resource
    private PassengerMapper passengerMapper;

    public void save(PassengerSaveRequest req) {
        DateTime now = DateTime.now();
        Passenger passenger = BeanUtil.copyProperties(req, Passenger.class);

        if (ObjectUtil.isNull(passenger.getId())) {
            passenger.setMemberId(LoginMemberContext.getId());
            passenger.setId(SnowUtil.getSnowflakeNextId());
            passenger.setCreateTime(now);
            passenger.setUpdateTime(now);
            passengerMapper.insert(passenger);
        } else {
            passenger.setUpdateTime(now);
            passengerMapper.updateByPrimaryKey(passenger);
        }

    }

    public PageRes<PassengerQueryRes> queryList(PassengerQueryReq req) {
        PassengerExample passengerExample = new PassengerExample();
        passengerExample.setOrderByClause("id desc"); //最新添加的显示在最前面
        PassengerExample.Criteria criteria = passengerExample.createCriteria();
        if (ObjectUtil.isNotNull(req.getMemberId())) {
            criteria.andMemberIdEqualTo(req.getMemberId());
        }
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Passenger> passengerList = passengerMapper.selectByExample(passengerExample);

        PageInfo<Passenger> pageInfo = new PageInfo<>(passengerList);
        LOG.info("Total: {}", pageInfo.getTotal());
        LOG.info("Pages: {}", pageInfo.getPages());

        List<PassengerQueryRes> list = BeanUtil.copyToList(passengerList, PassengerQueryRes.class);
        PageRes<PassengerQueryRes> PageRes = new PageRes<>();
        PageRes.setTotal(pageInfo.getTotal());
        PageRes.setList(list);
        return PageRes;
    }

    public void delete(Long id) {
        passengerMapper.deleteByPrimaryKey(id);
    }

    public List<PassengerQueryRes> queryMine() { //查询我的所有乘客
        PassengerExample passengerExample = new PassengerExample();
        passengerExample.setOrderByClause("name asc");
        PassengerExample.Criteria criteria = passengerExample.createCriteria();
        criteria.andMemberIdEqualTo(LoginMemberContext.getId());
        List<Passenger> list = passengerMapper.selectByExample(passengerExample);
        return BeanUtil.copyToList(list, PassengerQueryRes.class);
    }
}
