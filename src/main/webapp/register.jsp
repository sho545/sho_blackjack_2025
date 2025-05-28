<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>新規登録画面</title>
</head>
<body>
	<h1>新規登録</h1>
	
	<form action="ManageServlet" method="post">
		<input type="hidden" name="formAction" value="register">
		<dl>
			<dt>user name</dt>
				<dd><input type="text" name="userName"></dd>
			<dt>password</dt>
				<dd><input type="text" name="password"></dd>
			<dt><button>新規登録</button></dt>
		</dl>
	</form>
	
	<a href="login.jsp">ログイン画面へ</a>

</body>
</html>