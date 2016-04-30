package com.happy3w.ideamgr.ctrl;

import com.happy3w.common.model.WebCommonResult;
import com.happy3w.common.util.ErrorCode;
import com.happy3w.common.util.MessageUtil;
import com.happy3w.ideamgr.model.Idea;
import com.happy3w.ideamgr.svc.IdeaSvc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
            result = messageUtil.codeToWebResult(ErrorCode.UKOWN, new Object[]{t.getMessage()}, request);
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
            result = messageUtil.codeToWebResult(ErrorCode.UKOWN, new Object[]{t.getMessage()}, request);
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
            result = messageUtil.codeToWebResult(ErrorCode.UKOWN, new Object[]{t.getMessage()}, request);
        }
        return result;
    }
    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public WebCommonResult update(HttpServletRequest request,
                                  @RequestBody Idea idea) {
        WebCommonResult result;
        try {
            Idea newIdea = ideaSvc.update(idea);
            result = messageUtil.codeToWebResult(ErrorCode.SUCCESS, request);
            result.setData(newIdea);
        }
        catch (Throwable t) {
            result = messageUtil.codeToWebResult(ErrorCode.UKOWN, new Object[]{t.getMessage()}, request);
        }
        return result;
    }
}
