<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="user.User" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>戦績</title>
</head>
<body>
	<%
        // sessionからloginUserを取得
         User user = (User) request.getAttribute("user") ;
        if(user != null){
        	double numberOfGames = user.getNumberOfGames();
        	double victories = user.getVictories() ;
    %>
	<p><strong><%= user.getUserName() %>さん</strong>の戦績</p>
	
	<p>
		試合数 : <%= numberOfGames %>
	</p>
	<p>
		勝利数 : <%= victories %>
	</p>
	<p>
		勝率 : <% if(numberOfGames != 0){ %>
		       <%= (victories/numberOfGames)*100 %>%
		       <% }else{ %>
		       0%
		       <%} %>
	</p>
	
	<button onclick="history.back()">戻る</button>
	
	<%
        }else{
	%>
	<p>ログインしていません<a href="login.jsp">ログインページへ</a></p>
	<%
		}
	%>

</body>
</html>