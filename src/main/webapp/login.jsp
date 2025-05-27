<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ログイン画面</title>
</head>
<body>
	<h1>ログイン</h1>
	
<!--	ここに※のコメントを表示するようにしたい-->
	
	<form action="ManageServlet" method="post">
		<dl>
			<dt>user name</dt>
				<dd><input type="text" name="userName"></dd>
			<dt>password</dt>
				<dd><input type="text" name="password"></dd>
			<dt><button>ログイン</button></dt>
		</dl>
	</form>
	
	<a href="register.jsp">新規登録画面へ</a>
</body>
</html>