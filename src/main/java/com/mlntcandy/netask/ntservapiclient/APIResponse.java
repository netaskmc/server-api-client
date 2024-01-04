package com.mlntcandy.netask.ntservapiclient;

import com.mlntcandy.netask.ntservapi.Ntservapi;

import java.util.Arrays;

public class APIResponse {
    public APIResult result;
    public APIResponseSingular[] actions;

    APIResponse(APIResult result, APIResponseSingular[] actions) {
        this.result = result;
        this.actions = actions;
    }

    public boolean isSuccessful() {
        return result == APIResult.Success && actions != null;
    }

    public boolean execute() {
        if (!isSuccessful()) {
            Ntservapi.LOGGER.error("APIResponse is not successful, skipping execution (" + result + " / " + Arrays.toString(actions) + ")");
            return false;
        }
        for (APIResponseSingular a : actions) {
            a.execute();
        }
        return true;
    }
}
