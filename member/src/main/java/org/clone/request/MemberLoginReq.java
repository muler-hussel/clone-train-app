package org.clone.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class MemberLoginReq {
    @NotBlank(message = "[mobile] cannot be empty")
    @Pattern(regexp = "^1\\d{10}$", message = "Mobile phone pattern is wrong")
    private String mobile;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("MemberLoginReq{");
        sb.append("mobile='").append(mobile).append('\'');
        sb.append(", code='").append(code).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @NotBlank(message = "[code] cannot be empty")
    private String code;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
