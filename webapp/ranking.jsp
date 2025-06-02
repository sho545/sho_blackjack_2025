<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="user.User" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ランキング</title>
</head>
<body>
	<h1>勝率ベスト5</h1>
	
	<%
        // Servletによって渡されたUserListを取得
        List<User> ranking = (List<User>) request.getAttribute("ranking") ;
        if(ranking != null && !ranking.isEmpty()){
        	for(int i=0; i<ranking.size(); i++ ){
    %>
<!--    勝率ランキング上位5人を表示-->
	<dl>
		<dt><%= i+1 %>位 : <%= ranking.get(i).getUserName() %>さん(ユーザーid : <%= ranking.get(i).getUserId() %>)</dt>
			<dd>試合数 : <%= ranking.get(i).getNumberOfGames() %></dd>
			<dd>勝利数 : <%= ranking.get(i).getVictories() %></dd>
			<dd>勝率 : <%	double victories = ranking.get(i).getVictories();
							double numberOfGames = ranking.get(i).getNumberOfGames() ;
						%>
						<%= (victories/numberOfGames)*100 %> % 
			</dd>
	</dl>
	
	<%		}
        }else{
	%>
	<p>ランキングはありません</p>
	<%
		}
	%>
	<button onclick="history.back()">戻る</button>
</body>
</html>