package com.happy3w.ideamgr.svc;

import java.io.File;
import java.util.Arrays;

/**
 * Created by ysgao on 21/12/2016.
 */
public class Test {
    public static void main(String[] args) {
        File file = new File("/Volumes");
        System.out.println(Arrays.asList(file.listFiles()));
    }
}
