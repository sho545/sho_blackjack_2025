<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!--<%@ taglib uri="jakarta.tags.core" prefix="c" %> -->
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ログイン画面</title>
</head>
<body>
	<h1>ログイン</h1>
	
<!--	ここに※のコメントを表示するようにしたい-->
	<%  String message = (String) request.getAttribute("message") ;
	 	if(message != null && !message.isEmpty()){
	%>
		<p class="message"><%= message %></p>
	<%
	 	}
	%>
	
	<form action="ManageServlet" method="post">
		<input type="hidden" name="formAction" value="login">
		<dl>
			<dt>user name</dt>
				<dd><input type="text" name="userName" required></dd>
			<dt>password</dt>
				<dd><input type="password" name="password" required></dd>
			<dt><button>ログイン</button></dt>
		</dl>
	</form>
	
	<a href="register.jsp">新規登録画面へ</a>
</body>
</html>