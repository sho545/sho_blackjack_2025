<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%-- Google Fontsから美しい日本語フォントを読み込み --%>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+JP:wght@400;700&display=swap" rel="stylesheet">

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
<title>新規登録画面</title>
</head>
<body>

	<div class="register-container">
		<h1>新規登録</h1>
		
		 	<c:if test="${not empty message}">	
				<p class="message">${message}</p>
			</c:if>
		
		<form action="${pageContext.request.contextPath }/RegisterServlet" method="post">
			
			<div class="form-group">
				<label for="userName" >ユーザー名</label>
					<input id="userName" type="text" name="userName" placeholder="ユーザー名を入力" required>
			</div>
			
			<div class="form-group">
				<label for="password">パスワード</label>
					<input id="password" name="password" type="password" placeholder="パスワードを入力" required>
			</div>
			
				<button class="register-button">新規登録</button>
				
		</form>
	
		<div class="link">
			<a href="${pageContext.request.contextPath }/login.jsp">ログイン画面へ</a>
		</div>
	</div>

</body>
</html>