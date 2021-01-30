package com.happy3w.ideamgr.svc;

import com.happy3w.ideamgr.model.FileInformation;
import com.happy3w.ideamgr.model.FileStatus;
import com.happy3w.ideamgr.model.SpyStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
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
                } catch (Exception e) {
                    logger.error("Unexpected error", e);
                } finally {
                    workThread = null;
                }
            }
        });
        workThread.start();
    }

    private void traversalPath(String rootPath) throws IOException {
        FileInformation rootInfo = new FileInformation(
                idGeneratorSvc.nextIndex(FileInformation.class),
                0,
                rootPath,
                FILE_TYPE_DIR,
                0,
                FileStatus.INIT
                );
        saveFileInfo(rootInfo);

        Stack<FileInformation> stack = new Stack();
        stack.push(rootInfo);
        while (!stack.isEmpty()) {
            FileInformation fileInfo = stack.pop();

            if (fileInfo.getFstatus() == FileStatus.INIT) {
                processInitStatus(fileInfo, stack);
            } else if (fileInfo.getFstatus() == FileStatus.PROCESSING) {
                Long totalSizeObj = jdbcTemplate.queryForObject("select sum(fsize) from tfile where fparent=?", Long.class, fileInfo.getFid());
                long totalSize = totalSizeObj == null ? 0 : totalSizeObj.longValue();
                fileInfo.setFsize(totalSize);
                fileInfo.setFstatus(FileStatus.FINISHED);
                jdbcTemplate.update("update tfile set fsize=?,fstatus=? where fid=?",
                        totalSize, FileStatus.FINISHED, fileInfo.getFid());
            }
        }
    }

    private void processInitStatus(FileInformation fileInfo, Stack<FileInformation> stack) throws IOException {
        stack.push(fileInfo);
        fileInfo.setFstatus(FileStatus.PROCESSING);

        File file = new File(fileInfo.getFname());
        File[] subFiles = file.listFiles();
        if (subFiles != null) {
            for (File f : subFiles) {
                if (isLink(f)) {
                    continue;
                }

                FileInformation newFileInfo = new FileInformation(
                        idGeneratorSvc.nextIndex(FileInformation.class),
                        fileInfo.getFid(),
                        f.getAbsolutePath(),
                        FILE_TYPE_DIR,
                        0,
                        FileStatus.INIT
                );
                if (f.isFile()) {
                    newFileInfo.setFsize(f.length());
                    newFileInfo.setFstatus(FileStatus.FINISHED);
                    newFileInfo.setFtype(FILE_TYPE_FILE);
                } else {
                    stack.push(newFileInfo);
                }
                saveFileInfo(newFileInfo);
            }
        }
    }

    private boolean isLink(File file) throws IOException {
        return !file.getAbsolutePath().equals(file.getCanonicalPath());
    }

    private void saveFileInfo(FileInformation fileInformation) {
        jdbcTemplate.update("insert into tfile(fid,fparent,fname,ftype,fsize,fstatus) value(?,?,?,?,?,?)",
                fileInformation.getFid(), fileInformation.getFparentid(), fileInformation.getFname(),
                fileInformation.getFtype(), fileInformation.getFsize(), fileInformation.getFstatus());
//        int maxTime = 3;
//        for (int time = 0; time < maxTime; time++) {
//            try {
//                jdbcTemplate.update("REPLACE into tfile(fid,fparent,fname,ftype,fsize,fstatus) value(?,?,?,?,?,?)",
//                        fileInformation.getFid(), fileInformation.getFparentid(), fileInformation.getFname(),
//                        fileInformation.getFtype(), fileInformation.getFsize(), fileInformation.getFstatus());
//                break;
//            } catch (CannotGetJdbcConnectionException e) {
//                logger.info("Database connection lose. the execute time is " + (time + 1));
//                logger.trace("connection lose.", e);
//                if (time == maxTime - 1) {
//                    logger.info("It's the last time,never try.");
//                    throw e;
//                }
//            } catch (Exception e) {
//                logger.error("Unexpect error:", e);
//                break;
//            }
//
//        }
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
}
