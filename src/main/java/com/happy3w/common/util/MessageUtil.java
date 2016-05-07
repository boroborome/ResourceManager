package com.happy3w.common.util;

import com.happy3w.common.model.WebCommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * Created by ysgao on 4/25/16.
 */
@Component
public class MessageUtil {
    @Autowired
    private ApplicationContext applicationContext;

    public WebCommonResult codeToWebResult(int code, Object[] params, HttpServletRequest request) {
        Locale locale = getLocale(request);
        String message = applicationContext.getMessage("Error."  + code, params, locale);
        return new WebCommonResult(code, message);
    }

    public WebCommonResult codeToWebResult(int code, HttpServletRequest request) {
        return codeToWebResult(code, null, request);
    }

    public WebCommonResult codeMessageToWebResult(ICodeMessage message, HttpServletRequest request) {
        if (message.getLocaledMessage() != null) {
            return new WebCommonResult(message.getCode(), message.getLocaledMessage());
        }
        return codeToWebResult(message.getCode(), message.getParams(), request);
    }

    public Locale getLocale(HttpServletRequest request) {
        Locale locale = null;
        if (request != null) {
            String strLocale = request.getHeader("Accept-Language");
            if (strLocale != null && !strLocale.isEmpty()) {
                locale = new Locale(strLocale);
            } else {
                locale = request.getLocale();
            }
        }
        return locale;
    }

    public WebCommonResult exceptionToWebResult(Throwable t, HttpServletRequest request) {
        if (t instanceof ICodeMessage) {
            return codeMessageToWebResult((ICodeMessage) t, request);
        }
        return codeToWebResult(ErrorCode.UKOWN, new Object[]{t.getMessage()}, request);
    }

    public String getCodeErrorMessage(int code, HttpServletRequest request) {
        Locale locale = getLocale(request);
        return applicationContext.getMessage("Error."  + code, null, locale);
    }
}
