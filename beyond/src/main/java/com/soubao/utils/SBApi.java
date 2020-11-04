package com.soubao.utils;

import java.util.ArrayList;

public class SBApi {
    private int status = 1;
    private String msg = "成功";
    private Object result;

    public Object getResult() {
        return this.result == null ? new ArrayList() : this.result;
    }

    public static SBApi.SBApiBuilder builder() {
        return new SBApi.SBApiBuilder();
    }

    public int getStatus() {
        return this.status;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public SBApi(int status, String msg, Object result) {
        this.status = status;
        this.msg = msg;
        this.result = result;
    }

    public SBApi() {
    }

    public static class SBApiBuilder {
        private int status;
        private String msg;
        private Object result;

        SBApiBuilder() {
        }

        public SBApi.SBApiBuilder status(int status) {
            this.status = status;
            return this;
        }

        public SBApi.SBApiBuilder msg(String msg) {
            this.msg = msg;
            return this;
        }

        public SBApi.SBApiBuilder result(Object result) {
            this.result = result;
            return this;
        }

        public SBApi build() {
            return new SBApi(this.status, this.msg, this.result);
        }

        public String toString() {
            return "SBApi.SBApiBuilder(status=" + this.status + ", msg=" + this.msg + ", result=" + this.result + ")";
        }
    }
}
