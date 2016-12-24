package com.happy3w.ideamgr.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by ysgao on 4/10/16.
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class FileInformation {
    public static final int FILE_TYPE_FILE = 0;
    public static final int FILE_TYPE_DIR = 1;

    public static final String TABLE_NAME = "tfile";
    public static final String KEY_NAME = "fid";
    private long fid = -1;
    private long fparentid;
    private String fname;
    private int ftype;
    private long fsize;
    private int fstatus;

    public FileInformation() {

    }

    public FileInformation(String name) {
        this.fname = name;
    }

    public long getFparentid() {
        return fparentid;
    }

    public void setFparentid(long fparentid) {
        this.fparentid = fparentid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public long getFid() {
        return fid;
    }

    public void setFid(long fid) {
        this.fid = fid;
    }

    public int getFtype() {
        return ftype;
    }

    public void setFtype(int ftype) {
        this.ftype = ftype;
    }

    public long getFsize() {
        return fsize;
    }

    public void setFsize(long fsize) {
        this.fsize = fsize;
    }

    public int getFstatus() {
        return fstatus;
    }

    public void setFstatus(int fstatus) {
        this.fstatus = fstatus;
    }

    public void addSize(long size) {
        fsize += size;
    }
}
