<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
  String path     = request.getContextPath();
  String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";

%>
<!--DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"-->
<html>
<head>
    <base href="<%=basePath%>">
    <title>
        login fail page
    </title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">

    <link rel="stylesheet" href="css/bootstrap.min.css">
    <style type="text/css">
        /* should not  */
        body { background: #E7EAEC; }
        .login-box { width: 700px; border: 1px solid #ccc; border-radius: 5px; margin: 60px auto; padding: 60px 3px; background: #fff; }
    </style>
</head>
<body>

  <div class="login-box">
    <div class="alert alert-danger" role="alert" style="text-align:center;">
      <span class="sr-only">Error:</span>
      invalid username or password
    </div>
    <form class="form-horizontal" method="POST" action="j_security_check">
      <fieldset>
        <div class="form-group">
          <label for="j_username" class="col-sm-4 control-label">username</label>
          <div class="col-sm-4">
            <input type="username" name="j_username" class="form-control" id="j_username" placeholder="username" required="required">
          </div>
        </div>
        <div class="form-group">
          <label for="j_password" class="col-sm-4 control-label">Password</label>
          <div class="col-sm-4">
            <input type="password" name="j_password" class="form-control" id="j_password" placeholder="Password" required="required">
          </div>
        </div>
        <div class="form-group">
          <div class="col-sm-offset-4 col-sm-8">
            <button type="submit" class="btn btn-default">Sign in</button>
          </div>
        </div>
      </fieldset>
    </form>
  </div>
</body>
</html>
