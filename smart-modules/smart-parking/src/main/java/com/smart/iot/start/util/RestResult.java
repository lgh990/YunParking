package com.smart.iot.start.util;

public class RestResult {

    private int code = 200;
    private String msg;
    private int sendimgflag=0;

    public int getSendimgflag() {
        return sendimgflag;
    }

    public void setSendimgflag(int sendimgflag) {
        this.sendimgflag = sendimgflag;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public  RestResult(int code, String msg) {
        this.code = code;
        this.msg = msg;

    }

    public  RestResult() {

    }



        public RestResult BaseResponse(int code, String msg,int sendimgflag) {
            this.setCode(code);
            this.setMsg(msg);
            this.setSendimgflag(sendimgflag);
            return this;
        }


}
