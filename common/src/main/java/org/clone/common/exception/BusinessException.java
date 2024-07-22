package org.clone.common.exception;

public class BusinessException extends RuntimeException{
    private BusinessExceptionEnum anEnum;

    private BusinessExceptionEnum e;

    public BusinessExceptionEnum getE() {
        return e;
    }

    public void setE(BusinessExceptionEnum e) {
        this.e = e;
    }

    public BusinessException(BusinessExceptionEnum anEnum) {
        this.anEnum = anEnum;
    }


    public BusinessExceptionEnum getAnEnum() {
        return anEnum;
    }

    public void setAnEnum(BusinessExceptionEnum anEnum) {
        this.anEnum = anEnum;
    }

    @Override
    public Throwable fillInStackTrace() {return this;}
}
