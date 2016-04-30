package com.happy3w.ideamgr.svc;

import com.happy3w.common.util.SqlUtil;
import com.happy3w.ideamgr.model.Idea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ysgao on 3/29/16.
 */
@Component
public class IdeaSvc {

    private final Logger logger = LoggerFactory.getLogger(IdeaSvc.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private IDGeneratorSvc idGeneratorSvc;


    public List<Idea> queryAll(Integer pageNum, Integer pageSize) {

        StringBuilder sqlBuf = new StringBuilder();
        final List<Object> lstParam = new ArrayList<Object>();

        sqlBuf.append("select * from t_task");

        if (pageNum != null && pageSize != null) {
            sqlBuf.append(" limit ?,?");
            lstParam.add(pageSize * pageNum);
            lstParam.add(pageSize);
        }

        List<Idea> lstRow = jdbcTemplate.execute(sqlBuf.toString(),
                new PreparedStatementCallback<List<Idea>>(){
                    @Override
                    public List<Idea> doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                        SqlUtil.fillParams(ps, lstParam);

                        ResultSet rs = ps.executeQuery();
                        List<Idea> lstRow = new ArrayList<Idea>();
                        while (rs.next()) {
                            lstRow.add(resultSetToIdea(rs));
                        }
                        return lstRow;
                    }

                });
        return lstRow;
    }


    private Idea resultSetToIdea(ResultSet rs) throws SQLException {
        Idea idea = new Idea();
        idea.setId(rs.getInt("fid"));
        idea.setName(rs.getString("fname"));
        idea.setRemark(rs.getString("fremark"));
        idea.setImportant(rs.getInt("fimportant"));
        idea.setUrgency(rs.getInt("furgency"));
        return idea;
    }

    public Idea add(final Idea idea) {
        jdbcTemplate.execute("insert into t_task(fid,fname,fremark,fimportant,furgency) values(?,?,?,?,?)",
                new PreparedStatementCallback(){
                    @Override
                    public List<Idea> doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                        int id = (int) idGeneratorSvc.nextIndex(Idea.class, "t_task", "fid");
                        SqlUtil.fillParams(ps, id, idea.getName(), idea.getRemark(), idea.getId(), idea.getUrgency());
                        ps.execute();
                        idea.setId(id);
                        return null;
                    }

                });
        return idea;
    }


    public void remove(final int id) {
        jdbcTemplate.execute("delete from t_task where fid=?",
                new PreparedStatementCallback(){
                    @Override
                    public List<Idea> doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                        ps.setInt(1, id);
                        ps.execute();
                        return null;
                    }

                });
    }

    public Idea update(Idea idea) {
        jdbcTemplate.execute("update t_task set fname=?,fremark=?,fimportant=?,furgency=? where fid=?",
                new PreparedStatementCallback(){
                    @Override
                    public List<Idea> doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                        SqlUtil.fillParams(ps, idea.getName(), idea.getRemark(), idea.getId(), idea.getUrgency(), idea.getId());
                        ps.execute();
                        return null;
                    }

                });
        return idea;
    }
}
