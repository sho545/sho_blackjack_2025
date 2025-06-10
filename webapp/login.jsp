<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>ログイン</title>
<%-- Google Fontsから美しい日本語フォントを読み込み --%>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+JP:wght@400;700&display=swap" rel="stylesheet">

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/common.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/login.css">
</head>
<body>

    <div class="main-container login-container">
        <h1>ログイン</h1>
        
        <c:if test="${not empty message}">
            <p class="message">${message}</p>
        </c:if>
    
        <form action="${pageContext.request.contextPath}/session/login" method="post">
            
            <div class="form-group">
                <label for="userName">ユーザー名</label>
                <input type="text" id="userName" name="userName" placeholder="ユーザー名を入力" required>
            </div>
            
            <div class="form-group">
                <label for="password">パスワード</label>
                <input type="password" id="password" name="password" placeholder="パスワードを入力" required>
            </div>
            
            <button class="certification-btn login-btn">ログイン</button>
            
        </form>
        
        <div class="link">
            <a href="${pageContext.request.contextPath}/register.jsp">新しいアカウントを作成</a>
        </div>
    </div>

</body>
</html>