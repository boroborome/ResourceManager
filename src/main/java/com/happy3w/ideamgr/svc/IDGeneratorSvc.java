package com.happy3w.ideamgr.svc;

import com.happy3w.ideamgr.model.Idea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Component;

import javax.xml.ws.soap.Addressing;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ysgao on 4/30/16.
 */
@Component
public class IDGeneratorSvc {
    private static Logger logger = LoggerFactory.getLogger(IDGeneratorSvc.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 保存某个类型对应的索引
     */
    private Map<Class<?>, IndexCreator> mapIndex = new HashMap<Class<?>, IndexCreator>();


    public void init(Class<?> type, long startIndex) {
        mapIndex.put(type, new IndexCreator(startIndex));
    }

    public long nextIndex(Class<?> type) {
        IndexCreator creator = mapIndex.get(type);
        if (creator == null) {
            synchronized (mapIndex) {
                creator = new IndexCreator(0);
                mapIndex.put(type, creator);
            }
        }
        return creator.nextIndex();
    }

    public long nextIndex(Class type, String tableName, String fieldName) {
        IndexCreator creator = mapIndex.get(type);
        if (creator == null) {
            init(type, tableName, fieldName);
            creator = mapIndex.get(type);
        }
        return creator.nextIndex();
    }

    private int getMaxID(String tableName, String fieldName) {
        Integer maxId = jdbcTemplate.execute(MessageFormat.format("select max({0}) from {1}", fieldName, tableName),
                new PreparedStatementCallback<Integer>() {
                    @Override
                    public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                        ResultSet rs = ps.executeQuery();
                        return rs.next() ? rs.getInt(1) : 0;
                    }
                });
        return maxId == null ? 0 : maxId.intValue();
    }

    public void init(Class<Idea> type, String tableName, String keyName) {
        synchronized (mapIndex) {
            int maxID = getMaxID(tableName, keyName);
            IndexCreator creator = new IndexCreator(maxID);
            mapIndex.put(type, creator);
        }
    }

    private static class IndexCreator {
        private long index;

        /**
         * 构造方法
         *
         * @param index 起始Index，这个索引不会被nextIndex返回，下一个值才是可用值
         */
        public IndexCreator(long index) {
            super();
            this.index = index;
        }

        /**
         * 获取下一个可用Index
         *
         * @return
         */
        public synchronized long nextIndex() {
            index++;
            return index;
        }
    }
}
