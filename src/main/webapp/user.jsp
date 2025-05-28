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
	<%  String message = (String) request.getAttribute("message") ;
	 	if(message != null && !message.isEmpty()){
	%>
		<p class="message"><%= message %></p>
	<%
	 	}
	%>
	<%
        // 前の画面から渡されたUserを取得
        User loginUser = (User) session.getAttribute("loginUser") ;
        if(loginUser != null){
    %>
	<p><strong><%= loginUser.getUserName() %>さん</strong></p>
	
	<p>
		<a href="blackjack.jsp?id= <%= loginUser.getUserId()%> ">ゲームを始める</a>
	</p>
	<p>
		<a href="ManageServlet?formAction=record&user=<%= loginUser%> ">戦績表示</a>
	</p>
	<p>
		<a href="ManageServlet?formAction=ranking">勝率ランキング</a>
	</p>
	<p>
		<a href="ManageServlet?user=<%= loginUser%> ">ユーザー削除</a>
	</p>
	<p>
		<form action="ManageServlet" method="post">
			<input type="hidden" name="formAction" value="logout">
			<button>ログアウト</button>
		</form>
	</p>
	
	<%
        }else{
	%>
	<p>ログインしていません<a href="login.jsp">ログインページへ</a></p>
	<%
		}
	%>

</body>
</html>