package com.happy3w.ideamgr.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by ysgao on 4/10/16.
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    private int fstatus = FileStatus.INIT;

    public void addSize(long size) {
        fsize += size;
    }
}
