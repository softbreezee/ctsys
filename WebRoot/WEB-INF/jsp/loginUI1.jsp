<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>登录页</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
  </head>
  
  <body>
    <form>
    	账号:<s:textfield name="user.account"></s:textfield><br/>
    	密码:<s:textfield name="user.password"></s:textfield><br/>
    	<input type="button" value="注册" onclick="doRegister()"/>
    	<input type="button" value="登陆" onclick="doLogin()"/>
    </form>
  </body>
</html>
<script type="text/javascript">
	function doLogin(){
		document.forms[0].action="${pageContext.request.contextPath}/sys/login_login.action";
		document.forms[0].submit();
	};
</script>
