package com.happy3w.ideamgr.ctrl;

import com.happy3w.common.model.WebCommonResult;
import com.happy3w.common.util.ErrorCode;
import com.happy3w.common.util.MessageUtil;
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
@RequestMapping("/svc/bigfilespy/status")
public class BigFileSpyStatusCtrl {
    private final Logger logger = LoggerFactory.getLogger(BigFileSpyStatusCtrl.class);
    @Autowired
    private MessageUtil messageUtil;

    @Autowired
    private BigFileSpySvc svc;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public WebCommonResult getStatus(HttpServletRequest request) {
        WebCommonResult result;
        try {
            SpyStatus status = svc.getStatus();
            result = messageUtil.codeToWebResult(ErrorCode.SUCCESS, request);
            result.setData(status);
        }
        catch (Throwable t) {
            result = messageUtil.exceptionToWebResult(t, request);
        }

        return result;
    }

    public static class StartRequest {
        private String path;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
    @ResponseBody
    @RequestMapping(value = "start", method = RequestMethod.POST)
    public WebCommonResult start(HttpServletRequest request,
                               @RequestBody StartRequest startReq) {
        WebCommonResult result;
        try {
            svc.start(startReq.getPath());
            result = messageUtil.codeToWebResult(ErrorCode.SUCCESS, request);
            result.setData(new SpyStatus(true));
        }
        catch (Throwable t) {
            result = messageUtil.exceptionToWebResult(t, request);
        }
        return result;
    }


    @ResponseBody
    @RequestMapping(value = "stop", method = RequestMethod.POST)
    public WebCommonResult stop(HttpServletRequest request) {
        WebCommonResult result;
        try {
            svc.stop();
            result = messageUtil.codeToWebResult(ErrorCode.SUCCESS, request);
        }
        catch (Throwable t) {
            result = messageUtil.exceptionToWebResult(t, request);
        }
        return result;
    }
}
