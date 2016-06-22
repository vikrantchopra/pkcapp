<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 28/5/12
  Time: 3:47 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Welcome to Perceptive Kolkata</title></head>
<%
    String redirectURL = request.getContextPath() + "/home.action";
    response.sendRedirect(redirectURL);
%>
</html>