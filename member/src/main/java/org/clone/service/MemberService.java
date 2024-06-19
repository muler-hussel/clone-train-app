package org.clone.service;

import cn.hutool.core.collection.CollUtil;
import jakarta.annotation.Resource;
import org.clone.domain.Member;
import org.clone.domain.MemberExample;
import org.clone.exception.BusinessException;
import org.clone.exception.BusinessExceptionEnum;
import org.clone.mapper.MemberMapper;
import org.clone.request.MemberRegisterReq;
import org.clone.util.SnowUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    @Resource
    MemberMapper memberMapper;

    public long register(MemberRegisterReq req) {
        String mobile = req.getMobile();
        MemberExample memberExample = new MemberExample();
        memberExample.createCriteria().andMobileEqualTo(mobile);
        List<Member> list = memberMapper.selectByExample(memberExample);

        if (CollUtil.isNotEmpty(list)) {
            //return list.get(0).getId();
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_EXIST);
        }

        Member member = new Member();
        //1毫秒内会产生很多请求；加随机数有几率会重复；自增id不适于分布式场景；uid无序，索引时影响性能。
        //用雪花算法
        member.setId(SnowUtil.getSnowflakeNextId());
        member.setMobile(mobile);

        memberMapper.insert(member);
        return member.getId();
    }
}
