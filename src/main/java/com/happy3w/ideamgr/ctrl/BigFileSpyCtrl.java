package com.happy3w.ideamgr.ctrl;

import com.happy3w.common.model.WebCommonResult;
import com.happy3w.common.util.ErrorCode;
import com.happy3w.common.util.MessageUtil;
import com.happy3w.ideamgr.model.FileInformation;
import com.happy3w.ideamgr.model.Idea;
import com.happy3w.ideamgr.model.SpyStatus;
import com.happy3w.ideamgr.svc.BigFileSpySvc;
import com.happy3w.ideamgr.svc.IdeaSvc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by ysgao on 4/25/16.
 */
@RestController
@RequestMapping("/svc/bigfilespy")
public class BigFileSpyCtrl {
    private final Logger logger = LoggerFactory.getLogger(BigFileSpyCtrl.class);
    @Autowired
    private MessageUtil messageUtil;

    @Autowired
    private BigFileSpySvc svc;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public WebCommonResult getRootInfo(HttpServletRequest request) {
        WebCommonResult result;
        try {
            FileInformation fi = svc.getRootFileInfo();
            result = messageUtil.codeToWebResult(ErrorCode.SUCCESS, request);
            result.setData(fi);
        }
        catch (Throwable t) {
            result = messageUtil.exceptionToWebResult(t, request);
        }

        return result;
    }

    public static class QueryFileListRequest {
        private int fid;

        public int getFid() {
            return fid;
        }

        public void setFid(int fid) {
            this.fid = fid;
        }
    }
    @RequestMapping(value = "", method = RequestMethod.POST)
    public WebCommonResult queryFileListInfo(HttpServletRequest request,
                                             @RequestBody QueryFileListRequest queryRequest) {
        WebCommonResult result;
        try {
            List<FileInformation> fis = svc.queryFileList(queryRequest.fid);
            result = messageUtil.codeToWebResult(ErrorCode.SUCCESS, request);
            result.setData(fis);
        }
        catch (Throwable t) {
            result = messageUtil.exceptionToWebResult(t, request);
        }

        return result;
    }


}
