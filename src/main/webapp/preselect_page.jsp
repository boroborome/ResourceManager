<%--
  Created by IntelliJ IDEA.
  User: ysgao
  Date: 3/30/16
  Time: 10:31 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*,
org.springframework.web.context.support.WebApplicationContextUtils,
org.springframework.context.ApplicationContext" %>
<%@ page import="javax.sql.DataSource" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.Statement" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Preselect</title>
</head>
<body>
<%
    ServletContext sc = request.getSession().getServletContext();
    ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(sc);
    DataSource datasource = (DataSource) ac.getBean("lescDataSource");
    Statement statement = datasource.getConnection().createStatement();
    ResultSet rs = statement.executeQuery("select * from tbl_preselect");
%>

<form method="post" action="/svc/product/setDefaultService" enctype="multipart/form-data">
    <input type="file" name="fileProduct"/><button type="submit" value="Upload default service config file."/>
</form>
<form method="post" action="/svc/product/publishDefaultService">
    <button type="submit" value="Publish all changes."/>
</form>
    <table border="1" width="100%">
        <tr>
            <td>Product ID</td><td>Product Name</td><td>Service ID</td><td>Service Name</td><td>New Service ID</td><td>New Service Name</td>
        </tr>

        <%
            while (rs.next()) {
        %>
       <tr>
            <td><%=rs.getString("productID")%></td>
            <td><%=rs.getString("productName")%></td>
            <td><%=rs.getString("serviceID")%></td>
            <td><%=rs.getString("servieName")%></td>
            <td><%=rs.getString("newServiceID")%></td>
            <td><%=rs.getString("newServieName")%></td>
        </tr>
        <%
            }
        %>
    </table>

    Total Records:<%=rs.getFetchSize() %><br>
<%
    rs.close();
    statement.close();
%>
</body>
</html>
