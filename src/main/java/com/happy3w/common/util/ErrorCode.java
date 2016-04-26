package com.happy3w.common.util;

/**
 * Created by ysgao on 4/11/16.
 */
public final class ErrorCode {
    /**
     * Success
     */
    public static final int SUCCESS = 0;

    /**
     * Unkown Error
     */
    public static final int UKOWN = 1;

    /**
     * Data is locked.
     */
    public static final int DATA_LOCK = 2;

    /**
     * Excel with bad format.
     */
    public static final int BAD_EXCEL = 3;

    /**
     * No Data
     */
    public static final int NO_DATA = 4;

    /**
     * No File
     */
    public static final int NO_FILE = 5;
    /**
     * No authentication to do something.
     */
    public static final int NO_AUTH = 6;
    public static final int TOO_MANY_FILE = 7;
    public static final int BAD_EXCEL_MT_MTM_SAME_VALUE = 8;
    public static final int BAD_EXCEL_MT_EMPTY = 9;
    public static final int BAD_EXCEL_MT_LEN = 10;
    public static final int BAD_EXCEL_PRESELECT_RANGE = 11;
    public static final int BAD_EXCEL_TOSERVICE_LEN = 12;
    public static final int PAGE_NUM_WRONG = 13;
    public static final int PAGE_SIZE_WRONG = 14;
}
