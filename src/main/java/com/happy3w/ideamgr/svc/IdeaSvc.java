package com.happy3w.ideamgr.svc;

import com.happy3w.ideamgr.model.Idea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by ysgao on 3/29/16.
 */
@Component
public class IdeaSvc {

    private final Logger logger = LoggerFactory.getLogger(IdeaSvc.class);

    public List<Idea> queryAll(Integer pageNum, Integer pageSize) {
        return null;
    }
}
