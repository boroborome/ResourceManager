package com.happy3w.ideamgr.svc;

import com.happy3w.common.db.DBColumn;
import com.happy3w.common.util.ErrorCode;
import com.happy3w.common.util.ErrorCodeException;
import com.happy3w.common.util.SqlUtil;
import com.happy3w.common.util.convert.TypeConverterManager;
import com.happy3w.ideamgr.model.EnumIdeaStatus;
import com.happy3w.ideamgr.model.Idea;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by ysgao on 3/29/16.
 */
@Component
public class IdeaSvc {

    private static final String EXCEL_END_FLAG = "--File End--";
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
        return queryWithSql(sqlBuf.toString(), lstParam);
    }

    private List<Idea> queryWithSql(String sql, Object ... aryParam) {
        return queryWithSql(sql, Arrays.asList(aryParam));
    }
    private List<Idea> queryWithSql(String sql, List<Object> lstParam) {
        List<Idea> lstRow = jdbcTemplate.execute(sql,
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
        idea.setParentId(rs.getInt("fparentId"));
        idea.setName(rs.getString("fname"));
        idea.setRemark(rs.getString("fremark"));
        idea.setImportant(rs.getInt("fimportant"));
        idea.setUrgency(rs.getInt("furgency"));
        idea.setStatus(EnumIdeaStatus.parse(rs.getInt("fstatus")));
        idea.setProgress(rs.getFloat("fprogress"));
        return idea;
    }

    public Idea add(final Idea idea) {
        jdbcTemplate.execute("insert into t_task(fid,fparentId,fname,fremark,fimportant,furgency,fstatus) values(?,?,?,?,?,?,?)",
                new PreparedStatementCallback(){
                    @Override
                    public List<Idea> doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                        int id = (int) idGeneratorSvc.nextIndex(Idea.class, "t_task", "fid");
                        int status = idea.getStatus() == null ? EnumIdeaStatus.None.getValue() : idea.getStatus().getValue();
                        SqlUtil.fillParams(ps, id, idea.getParentId(), idea.getName(), idea.getRemark(), idea.getId(), idea.getUrgency(), status);
                        ps.execute();
                        idea.setId(id);
                        return null;
                    }

                });

        // update progress
        updateProgress(idea.getParentId());
        return idea;
    }

    private void updateProgress(int id) {
        int curID = id;
        while (curID > 0) {
            Map<String, Object> mapResult = jdbcTemplate.queryForMap("select count(fid) as totalNum,sum(if(fstatus=? or fstatus=?,100,fprogress)) as finishProgress from t_task where fparentId=?",
                    EnumIdeaStatus.Canceled.getValue(), EnumIdeaStatus.Finished.getValue(), curID);
            float totalProgress = getFloat(mapResult, "totalNum");
            float finishProgress = getFloat(mapResult, "finishProgress");
            float progress = totalProgress == 0 ? 0 : finishProgress / totalProgress;

            jdbcTemplate.update("update t_task set fprogress=? where fid=?", progress, curID);
            Idea idea = query(curID);
            curID = idea.getParentId();
        }
    }

    private float getFloat(Map<String, Object> mapResult, String fieldName) {
        if (mapResult == null || fieldName == null || fieldName.isEmpty()) {
            return 0;
        }
        Object objValue = mapResult.get(fieldName);
        if (objValue instanceof Number) {
            return ((Number) objValue).floatValue();
        }
        return 0;
    }


    public void remove(final int id) throws ErrorCodeException {
        Idea idea = queryWithChild(id);
        if (idea == null) {
            return;
        }
        if (idea.getChildren() != null && !idea.getChildren().isEmpty()) {
            throw new ErrorCodeException(ErrorCode.IdeaHasChildren, null, idea.getName(), idea.getId());
        }
        jdbcTemplate.execute("delete from t_task where fid=?",
                new PreparedStatementCallback(){
                    @Override
                    public List<Idea> doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                        ps.setInt(1, id);
                        ps.execute();
                        return null;
                    }

                });
        updateProgress(idea.getParentId());
    }

    public Idea update(Idea idea) {
        jdbcTemplate.execute("update t_task set fname=?,fremark=?,fimportant=?,furgency=?,fstatus=? where fid=?",
                new PreparedStatementCallback(){
                    @Override
                    public List<Idea> doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                        int status = idea.getStatus() == null ? EnumIdeaStatus.None.getValue() : idea.getStatus().getValue();
                        SqlUtil.fillParams(ps, idea.getName(), idea.getRemark(), idea.getId(), idea.getUrgency(), status, idea.getId());
                        ps.execute();
                        return null;
                    }

                });
        updateProgress(idea.getParentId());
        return idea;
    }

    public Idea queryWithChild(int id) {
        Idea idea = query(id);
        List<Idea> lstChild = queryWithSql("select * from t_task where fparentId=?", id);
        if (lstChild != null && !lstChild.isEmpty()) {
            if (idea == null) {
                idea = new Idea();
                idea.setId(id);
            }
            idea.setChildren(lstChild);
        }
        return idea;
    }

    public Idea query(int id) {
        List<Idea> lstIdea = queryWithSql("select * from t_task where fid=?", id);
        return lstIdea == null || lstIdea.isEmpty() ? null : lstIdea.get(0);
    }

    public void saveAllToExcel(OutputStream outputStream) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("data");

//        workbook.
        writeDate(sheet);

        workbook.write(outputStream);
    }


    private void writeDate(final XSSFSheet sheet) {
        jdbcTemplate.query("select * from t_task", new RowCallbackHandler() {
            private int rowIndex = 0;
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                if (rowIndex == 0) {
                    writeHead(sheet, rs);
                    rowIndex += 2;
                }
                XSSFRow row = sheet.createRow(rowIndex);
                for (int index =0, count = rs.getMetaData().getColumnCount(); index < count; ++index) {
                    String value = rs.getString(index + 1);
                    row.createCell(index).setCellValue(value);
                }

                rowIndex++;
            }
        });
        XSSFRow endRow = sheet.createRow(sheet.getLastRowNum() + 1);
        endRow.createCell(0).setCellValue(EXCEL_END_FLAG);
    }

    private void writeHead(XSSFSheet sheet, ResultSet rs) throws SQLException {
        XSSFRow headRow = sheet.createRow(0);
        XSSFRow typeRow = sheet.createRow(1);
        for (int index =0, count = rs.getMetaData().getColumnCount(); index < count; ++index) {
            int columnIndex = index + 1;
            String name = rs.getMetaData().getColumnName(columnIndex);
            String type = rs.getMetaData().getColumnClassName(columnIndex);
            headRow.createCell(index).setCellValue(name);
            typeRow.createCell(index).setCellValue(type);
        }
    }

    public void upload(InputStream inputStream) throws ErrorCodeException {
        jdbcTemplate.execute("delete from t_task");
        // fill excel into database
        XSSFWorkbook xssfWorkbook;
        try {
            xssfWorkbook = new XSSFWorkbook(inputStream);
        } catch (Exception e) {
            logger.trace(e.getMessage(), e);
            throw new ErrorCodeException(ErrorCode.BAD_EXCEL_FORMAT, e, new Object[]{e.getMessage()});
        }
        XSSFSheet serviceSheet = xssfWorkbook.getSheetAt(0);

        DBColumn[] aryColumns = getColumns(serviceSheet);
        String sql = createInsertSql("t_task", aryColumns);
        jdbcTemplate.execute(sql, new PreparedStatementCallback() {
                @Override
                public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {

                    for (int rowIndex = 2, maxRowIndex = serviceSheet.getLastRowNum(); rowIndex < maxRowIndex; ++rowIndex) {
                        XSSFRow row = serviceSheet.getRow(rowIndex);
                        String flag = getCellValue(row, 0);
                        if (EXCEL_END_FLAG.equalsIgnoreCase(flag)) {
                            break;
                        }
                        for (int columnIndex = 0; columnIndex < aryColumns.length; ++columnIndex) {
                            String value = getCellValue(row, columnIndex);
                            ps.setObject(columnIndex + 1, aryColumns[columnIndex].getConverter().parse(value));
                        }
                        ps.execute();
                    }

                    return null;
                }
            });
        idGeneratorSvc.init(Idea.class, Idea.TABLE_NAME, Idea.KEY_NAME);
    }

    private String createInsertSql(String tableName, DBColumn[] aryColumns) {
        StringBuilder buf = new StringBuilder("replace into ");
        buf.append(tableName).append('(');
        for (DBColumn column : aryColumns) {
            buf.append(column.getName()).append(',');
        }
        buf.setLength(buf.length() - 1);
        buf.append(") values(");
        for (int index = 0; index < aryColumns.length; ++index) {
            buf.append("?,");
        }
        buf.setLength(buf.length() - 1);
        buf.append(")");
        return buf.toString();
    }

    private String getCellValue(XSSFRow row, int column) {
        if (row == null) {
            return null;
        }
        XSSFCell cell = row.getCell(column);
        return cell == null ? null : cell.toString();
    }
    private DBColumn[] getColumns(XSSFSheet serviceSheet) throws ErrorCodeException {
        XSSFRow headRow = serviceSheet.getRow(0);
        XSSFRow typeRow = serviceSheet.getRow(1);
        List<DBColumn> lstColumn = new ArrayList<DBColumn>();
        for (int columnIndex = 0;; ++columnIndex) {
            String head = getCellValue(headRow, columnIndex);
            if (head == null || head.isEmpty()) {
                break;
            }
            String typeName = getCellValue(typeRow, columnIndex);
            try {
                Class type = Class.forName(typeName);
                DBColumn column = new DBColumn();
                column.setName(head);
                column.setType(type);
                column.setConverter(TypeConverterManager.getInstance().getConverter(type));
                lstColumn.add(column);
            } catch (ClassNotFoundException e) {
                throw new ErrorCodeException(ErrorCode.UKOWN, e, e.getMessage());
            }
        }
        return lstColumn.toArray(new DBColumn[lstColumn.size()]);
    }
}
