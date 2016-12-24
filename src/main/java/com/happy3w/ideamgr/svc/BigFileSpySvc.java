package com.happy3w.ideamgr.svc;

import com.happy3w.ideamgr.model.FileInformation;
import com.happy3w.ideamgr.model.SpyStatus;
import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static com.happy3w.ideamgr.model.FileInformation.FILE_TYPE_DIR;
import static com.happy3w.ideamgr.model.FileInformation.FILE_TYPE_FILE;

/**
 * Created by ysgao on 3/29/16.
 */
@Component
public class BigFileSpySvc {

    private final Logger logger = LoggerFactory.getLogger(BigFileSpySvc.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private IDGeneratorSvc idGeneratorSvc;

    private Thread workThread;

    public SpyStatus getStatus() {
        return new SpyStatus(workThread != null);
    }

    public void start(final String rootPath) {
        if (workThread != null) {
            return;
        }

        workThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    clearDatabase();
                    traversalPath(rootPath);
                } finally {
                    workThread = null;
                }
            }
        });
        workThread.start();
    }

    private void traversalPath(String rootPath) {
        Stack<DirectoryTask> stack = new Stack();
        stack.push(new DirectoryTask(rootPath, null));
        while (!stack.isEmpty()) {
            DirectoryTask fileTask = stack.pop();
            FileInformation fileInfo = fileTask.getFileInfo();

            if (fileInfo.getFid() < 0) {
                stack.push(fileTask);
                fileInfo.setFid(idGeneratorSvc.nextIndex(FileInformation.class));
                fileInfo.setFtype(FILE_TYPE_DIR);
                fileInfo.setFstatus(0);

                File file = new File(fileInfo.getFname());
                File[] subFiles = file.listFiles();
                if (subFiles != null)
                {
                    for (File f : subFiles) {
                        if (f.isFile()) {
                            FileInformation fi = new FileInformation(f.getAbsolutePath());
                            fi.setFparentid(fileInfo.getFid());
                            fi.setFid(idGeneratorSvc.nextIndex(FileInformation.class));
                            fi.setFsize(f.length());
                            fi.setFtype(FILE_TYPE_FILE);
                            saveFileInfo(fi);

                            fileInfo.addSize(f.length());
                        } else {
                            stack.push(new DirectoryTask(f.getAbsolutePath(), fileInfo));
                        }
                    }
                }

            } else {
                fileInfo.setFstatus(1);
            }
            saveFileInfo(fileInfo);
        }
    }

    private void saveFileInfo(FileInformation fileInformation) {
        int maxTime = 3;
        for (int time = 0; time < maxTime; time++) {
            try {
                jdbcTemplate.update("REPLACE into tfile(fid,fparent,fname,ftype,fsize,fstatus) value(?,?,?,?,?,?)",
                        fileInformation.getFid(), fileInformation.getFparentid(), fileInformation.getFname(),
                        fileInformation.getFtype(), fileInformation.getFsize(), fileInformation.getFstatus());
                break;
            } catch (CannotGetJdbcConnectionException e) {
                logger.info("Database connection lose. the execute time is " + (time + 1));
                logger.trace("connection lose.", e);
                if (time == maxTime - 1) {
                    logger.info("It's the last time,never try.");
                    throw e;
                }
            }

        }
    }

    private void clearDatabase() {
        jdbcTemplate.execute("delete from tfile");
    }

    public void stop() {

    }

    public FileInformation getRootFileInfo() {
        long id = jdbcTemplate.queryForObject("select min(fid) from tfile", Long.class);
        List<FileInformation> fis = jdbcTemplate.query("select * from tfile where fid=?", new Object[]{id}, BeanPropertyRowMapper.newInstance(FileInformation.class));
        return fis.get(0);
    }

    public List<FileInformation> queryFileList(int fid) {
        return jdbcTemplate.query("select * from tfile where fparent=? order by fsize desc", new Object[]{fid},
                BeanPropertyRowMapper.newInstance(FileInformation.class));
    }

    private static class DirectoryTask {
        private FileInformation fileInfo;
        private List<String> subDirectoryTobeCalculate = new ArrayList();

        private FileInformation parent;

        public DirectoryTask(String path, FileInformation parent) {
            fileInfo = new FileInformation(path);
            this.parent = parent;
        }

        public FileInformation getFileInfo() {
            return fileInfo;
        }

        public void setFileInfo(FileInformation fileInfo) {
            this.fileInfo = fileInfo;
        }

        public FileInformation getParent() {
            return parent;
        }

        public void setParent(FileInformation parent) {
            this.parent = parent;
        }

        public List<String> getSubDirectoryTobeCalculate() {
            return subDirectoryTobeCalculate;
        }
    }
}
