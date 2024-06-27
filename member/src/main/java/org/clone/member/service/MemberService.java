package org.clone.member.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import jakarta.annotation.Resource;
import org.clone.member.domain.Member;
import org.clone.member.domain.MemberExample;
import org.clone.common.exception.BusinessException;
import org.clone.member.mapper.MemberMapper;
import org.clone.member.request.MemberLoginReq;
import org.clone.member.request.MemberRegisterReq;
import org.clone.member.request.MemberSendCodeReq;
import org.clone.member.response.MemberLoginRes;
import org.clone.common.util.JwtUtil;
import org.clone.common.util.SnowUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.clone.common.exception.BusinessExceptionEnum.*;

@Service
public class MemberService {
    private static final Logger Log = LoggerFactory.getLogger(MemberService.class);

    @Resource
    MemberMapper memberMapper;

    public long register(MemberRegisterReq req) {
        String mobile = req.getMobile();
        Member memberDB = selectByMobile(mobile);

        if (ObjectUtil.isNotNull(memberDB)) {
            throw new BusinessException(MEMBER_MOBILE_EXIST);
        }

        Member member = new Member();
        //1毫秒内会产生很多请求；加随机数有几率会重复；自增id不适于分布式场景；uid无序，索引时影响性能。
        //用雪花算法
        member.setId(SnowUtil.getSnowflakeNextId());
        member.setMobile(mobile);

        memberMapper.insert(member);
        return member.getId();
    }

    public void sendCode(MemberSendCodeReq req) {
        String mobile = req.getMobile();
        Member memberDB = selectByMobile(mobile);

        //手机号不存在，插入记录
        if (ObjectUtil.isNull(memberDB)) {
            Log.info("insert one data");
            Member member = new Member();
            //1毫秒内会产生很多请求；加随机数有几率会重复；自增id不适于分布式场景；uid无序，索引时影响性能。
            //用雪花算法
            member.setId(SnowUtil.getSnowflakeNextId());
            member.setMobile(mobile);
            memberMapper.insert(member);
        } else {
            Log.info("Phone exists");
        }

        //生成验证码
        //String code = RandomUtil.randomString(4);
        String code = "8888";
        Log.info("{}", code);

        //保存短信记录表：手机号，短信验证码，有效期，是否已使用，业务类型，发送时间，使用时间

        //对接短信通道，发送短信
    }

    public MemberLoginRes login (MemberLoginReq req) {
        String mobile = req.getMobile();
        String code = req.getCode();
        Member memberDB = selectByMobile(mobile);

        //手机号不存在
        if (ObjectUtil.isNull(memberDB)) {
            throw new BusinessException(MEMBER_MOBILE_NOT_EXIST);
        }

        //校验短信验证码
        if (!"8888".equals(code)) {
            throw new BusinessException(MEMBER_MOBILE_CODE_ERROR);
        }
        MemberLoginRes memberLoginRes = BeanUtil.copyProperties(memberDB, MemberLoginRes.class);
        String token = JwtUtil.createToken(memberLoginRes.getId(), mobile);
        memberLoginRes.setToken(token);
        return memberLoginRes;
    }

    private Member selectByMobile(String mobile) {
        MemberExample memberExample = new MemberExample();
        memberExample.createCriteria().andMobileEqualTo(mobile);
        List<Member> list = memberMapper.selectByExample(memberExample);
        if (CollUtil.isEmpty(list)) {
            return null;
        } else {
           return list.get(0);
        }
    }

    public int count() {
        return Math.toIntExact(memberMapper.countByExample(null));
    }
}
