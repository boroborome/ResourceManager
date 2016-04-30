package com.happy3w.ideamgr.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by ysgao on 4/10/16.
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Idea {
    private int id;
    private String name;
    private String remark;
    private int important;
    private int urgency;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getImportant() {
        return important;
    }

    public void setImportant(int important) {
        this.important = important;
    }

    public int getUrgency() {
        return urgency;
    }

    public void setUrgency(int urgency) {
        this.urgency = urgency;
    }
}
