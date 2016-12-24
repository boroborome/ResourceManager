package com.happy3w.ideamgr.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by ysgao on 21/12/2016.
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class SpyStatus {
    private boolean started;

    public SpyStatus() {
    }

    public SpyStatus(boolean started) {
        this.started = started;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }
}
