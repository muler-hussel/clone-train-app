package org.clone.service;

import cn.hutool.core.collection.CollUtil;
import jakarta.annotation.Resource;
import org.clone.domain.Member;
import org.clone.domain.MemberExample;
import org.clone.mapper.MemberMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    @Resource
    MemberMapper memberMapper;

    public long register(String mobile) {
        MemberExample memberExample = new MemberExample();
        memberExample.createCriteria().andMobileEqualTo(mobile);
        List<Member> list = memberMapper.selectByExample(memberExample);

        if (CollUtil.isNotEmpty(list)) {
            //return list.get(0).getId();
            throw new RuntimeException("Phone is registered");
        }

        Member member = new Member();
        member.setId(System.currentTimeMillis());
        member.setMobile(mobile);

        memberMapper.insert(member);
        return member.getId();
    }
}
