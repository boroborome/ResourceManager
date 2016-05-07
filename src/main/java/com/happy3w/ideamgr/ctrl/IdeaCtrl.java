package com.happy3w.ideamgr.ctrl;

import com.happy3w.common.model.WebCommonResult;
import com.happy3w.common.util.ErrorCode;
import com.happy3w.common.util.ICodeMessage;
import com.happy3w.common.util.MessageUtil;
import com.happy3w.ideamgr.model.Idea;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ysgao on 4/25/16.
 */
@RestController
@RequestMapping("/svc/idea")
public class IdeaCtrl {
    private final Logger logger = LoggerFactory.getLogger(IdeaCtrl.class);
    @Autowired
    private MessageUtil messageUtil;

    @Autowired
    private IdeaSvc ideaSvc;

    @RequestMapping(method = RequestMethod.GET)
    public WebCommonResult queryAll(HttpServletRequest request,
                                    @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                    @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        WebCommonResult result;
        try {
            List<Idea> lstIdea = ideaSvc.queryAll(pageNum, pageSize);
            result = messageUtil.codeToWebResult(ErrorCode.SUCCESS, request);
            result.setData(lstIdea);
        }
//        catch (ErrorCodeException e) {
//            result = messageUtil.codeMessageToWebResult(e, request);
//        }
        catch (Throwable t) {
            result = messageUtil.exceptionToWebResult(t, request);
        }

        return result;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public WebCommonResult add(HttpServletRequest request,
                               @RequestBody Idea idea) {
        WebCommonResult result;
        try {
            Idea newIdea = ideaSvc.add(idea);
            result = messageUtil.codeToWebResult(ErrorCode.SUCCESS, request);
            result.setData(newIdea);
        }
        catch (Throwable t) {
            result = messageUtil.exceptionToWebResult(t, request);
        }
        return result;
    }
    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public WebCommonResult delete(HttpServletRequest request,
                                  @PathVariable("id") int id) {
        WebCommonResult result;
        try {
            ideaSvc.remove(id);
            result = messageUtil.codeToWebResult(ErrorCode.SUCCESS, request);
        }
        catch (Throwable t) {
            result = messageUtil.exceptionToWebResult(t, request);
        }
        return result;
    }
    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public WebCommonResult update(HttpServletRequest request,
                                  @RequestBody Idea idea, @PathVariable int id) {
        WebCommonResult result;
        try {
            idea.setId(id);
            Idea newIdea = ideaSvc.update(idea);
            result = messageUtil.codeToWebResult(ErrorCode.SUCCESS, request);
            result.setData(newIdea);
        }
        catch (Throwable t) {
            result = messageUtil.exceptionToWebResult(t, request);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public WebCommonResult querySingle(HttpServletRequest request,
                                       @PathVariable int id,
                                       @RequestParam(value = "withChild", required = false) Boolean withChild) {
        WebCommonResult result;
        try {
            Idea newIdea = Boolean.TRUE.equals(withChild) ? ideaSvc.queryWithChild(id) : ideaSvc.query(id);
            result = messageUtil.codeToWebResult(ErrorCode.SUCCESS, request);
            result.setData(newIdea);
        }
        catch (Throwable t) {
            result = messageUtil.exceptionToWebResult(t, request);
        }
        return result;
    }


    /**
     * Download all preselect data.
     * @param request
     * @param response
     */
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void download(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.addHeader("mime", "application/msword");
            response.addHeader("Content-Disposition", "attachment; filename=idea.xlsx");
            ideaSvc.saveAllToExcel(response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            try {
                response.getWriter().write(messageUtil.getCodeErrorMessage(ErrorCode.UKOWN, request));
            } catch (IOException e1) {
                // Ignore this exception.
            }
            response.setStatus(404);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public WebCommonResult upload(HttpServletRequest request) {
        List<MultipartFile> lstFile = ((MultipartHttpServletRequest) request).getFiles("file");
        if (lstFile == null || lstFile.isEmpty()) {
            return messageUtil.codeToWebResult(ErrorCode.FILE_CAN_ONLY_ONE, null, request);
        }

        if (lstFile.size() != 1) {
            return messageUtil.codeToWebResult(ErrorCode.FILE_CAN_ONLY_ONE, null, request);
        }

        MultipartFile fileProduct = lstFile.get(0);

        if (!fileProduct.getOriginalFilename().toLowerCase().endsWith(".xlsx")) {
            return messageUtil.codeToWebResult(ErrorCode.FILE_CAN_ONLY_ONE, null, request);
        }

        WebCommonResult result;
        try {
            ideaSvc.upload(fileProduct.getInputStream());
            result = messageUtil.codeToWebResult(ErrorCode.SUCCESS, request);
        } catch (Throwable t) {
            result = messageUtil.exceptionToWebResult(t, request);
        }

        return result;
    }

}
