package com.happy3w.ideamgr.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

/**
 * Created by ysgao on 4/10/16.
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Idea {
    public static final String TABLE_NAME = "t_task";
    public static final String KEY_NAME = "fid";
    private int id;
    private int parentId;
    private String name;
    private String remark;
    private int important;
    private int urgency;
    private float progress;
    private EnumIdeaStatus status;

    private List<Idea> children;

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

    public List<Idea> getChildren() {
        return children;
    }

    public void setChildren(List<Idea> children) {
        this.children = children;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public EnumIdeaStatus getStatus() {
        return status;
    }

    public void setStatus(EnumIdeaStatus status) {
        this.status = status;
    }

    public float getProgress() {
        return (status == EnumIdeaStatus.Canceled || status == EnumIdeaStatus.Finished) ? (float) 100 : progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }
}
