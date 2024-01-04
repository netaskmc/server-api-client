package com.mlntcandy.netask.ntservapiclient;

public class APIResponseSingular {
    public String action;
    public String target;
    public String payload;

    public APIResponseSingular(String action, String target, String payload) {
        this.action = action;
        this.target = target;
        this.payload = payload;
    }

    public void execute() {
        APIResponseExecutorRegister.execute(this);
    }
}
