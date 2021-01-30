<%@ page language="java" import="java.util.*,
org.springframework.web.context.support.WebApplicationContextUtils,
org.springframework.context.ApplicationContext" %>
<%@ page import="javax.sql.DataSource" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.Statement" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Plan</title>
</head>
<body>
<%
    ServletContext sc = request.getSession().getServletContext();
    ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(sc);
    DataSource datasource = (DataSource) ac.getBean("dataSource");
    Statement statement = datasource.getConnection().createStatement();
%>

<form method="post" action="/svc/product/setDefaultService" enctype="multipart/form-data">
    <input type="file" name="fileProduct"/><input type="submit" value="Upload default service config file."/>
</form>
<form method="post" action="/svc/product/publishDefaultService">
    <input type="submit" value="Publish all changes."/>
</form>
<hr>
    <table width="100%">
        <tr>
            <td>
                Plans:
                <table border="1" width="100%">
                    <tr>
                        <td>ID</td><td>Start Time</td><td>End Time</td><td>Task Name</td>
                    </tr>

                    <%
                        ResultSet rs = statement.executeQuery("select * from t_plan p left join t_task t on p.ftaskid=t.fid order by p.fstart");
                        while (rs.next()) {
                    %>
                    <tr>
                        <td><%=rs.getInt("p.fid")%></td>
                        <td><%=rs.getTimestamp("p.fstart")%></td>
                        <td><%=rs.getTimestamp("p.fend")%></td>
                        <td><%=rs.getString("t.fname")%></td>
                    </tr>
                    <%
                        }
                    %>
                </table>

                Total Records:<%=rs.getFetchSize() %><br>

            </td>

            <td>
                Tasks:<a href="/svc/"
                <table border="1" width="100%">
                    <tr>
                        <td>ID</td><td>Name</td><td>Remark</td><td>Important</td><td>Urgency</td>
                    </tr>

                    <%
                        rs.close();
                        rs = statement.executeQuery("select * from t_task order by fimportant");
                        while (rs.next()) {
                    %>
                    <tr>
                        <td><%=rs.getInt("fid")%></td>
                        <td><%=rs.getString("fname")%></td>
                        <td><%=rs.getString("fremark")%></td>
                        <td><%=rs.getInt("fimportant")%></td>
                        <td><%=rs.getInt("furgency")%></td>
                    </tr>
                    <%
                        }
                    %>
                </table>

                Total Records:<%=rs.getFetchSize() %><br>
            </td>
        </tr>
    </table>

<%
    if (rs != null) {
        rs.close();
    }
    if (statement != null) {
        statement.close();
    }
%>
</body>
</html>
