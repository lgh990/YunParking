package com.yuncitys.smart.parking.common.util;

/**
 * 公共异常类
 * 备注：与原异常没有区别，只是多了一个errormsg字段，保存开发人员提供的异常提示信息
 * @author suny
 * @date 2017-7-4
 * <pre>
 *  desc:
 * </pre>
 */
public class CommonException extends Exception {

    // 原始异常
    private Throwable target;

    // 开发提供异常提示内容
    private String errormsg = "";

    public Throwable getTargetException() {
        return target;
    }

    public Throwable getCause() {
        return target;
    }

    protected CommonException() {
        super((Throwable) null);
    }

    public CommonException(Throwable target, String s) {
        super(s, null);
        this.target = target;
        this.errormsg = s;
    }

    public CommonException(Throwable target) {
        super((Throwable) null);
        this.target = target;
    }

    public String getErrormsg() {
        return errormsg;
    }

}
