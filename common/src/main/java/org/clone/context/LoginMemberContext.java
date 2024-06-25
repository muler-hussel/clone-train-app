package org.clone.context;

import org.clone.response.MemberLoginRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginMemberContext {
    private static final Logger LOG = LoggerFactory.getLogger(LoginMemberContext.class);

    private static ThreadLocal<MemberLoginRes> member = new ThreadLocal<>();

    public static MemberLoginRes getMember() {
        return member.get();
    }

    public static void setMember(MemberLoginRes member) {
        LoginMemberContext.member.set(member);
    }

    public static Long getId() {
        try {
            return member.get().getId();
        } catch (Exception e) {
            LOG.error("Fail to access to member information", e);
            throw e;
        }
    }

}
