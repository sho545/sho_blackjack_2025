<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="user.User" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ユーザー画面</title>
</head>
<body>
	<%
        // 前の画面から渡されたUserを取得
        User loginUser = (User) session.getAttribute("loginUser") ;
        if(loginUser != null){
    %>
	<p><strong><%= loginUser.getUserName() %>さん</strong></p>
	<%
        }else{
	%>
	<p>ログインしていません<a href="login.jsp">ログインページへ</p>
	<%
		}
	%>


<!--	<p>-->
<!--		<a href="blackjack.jsp?id=ゲームを始める</a>-->
<!--	</p>-->
<!--	<p>-->
<!--		<a href="ManageServlet?id=戦績表示</a>-->
<!--	</p>-->
</body>
</html>