package org.clone.exception;

public class BusinessException extends RuntimeException{
    private BusinessExceptionEnum anEnum;

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
