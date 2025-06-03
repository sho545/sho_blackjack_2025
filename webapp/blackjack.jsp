<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.GameMaster" %>
<%@ page import="model.Game" %>
<%@ page import="user.User" %>
<%@ page import="model.player.BasePlayer" %>
<%@ page import="model.player.Player" %>
<%@ page import="model.player.Dealer" %>
<%@ page import="model.card.Card" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ブラックジャック</title>
</head>
<body>
	<%
        // sessionからloginUserを取得
         User loginUser = (User) session.getAttribute("loginUser") ;
        if(loginUser != null){
    %>
  		<%
	        // GameServletからgameMasterを取得
	         GameMaster gameMaster = (GameMaster) session.getAttribute("gameMaster") ;
	        if(gameMaster != null){
	        	Game game = gameMaster.getGame() ;
				BasePlayer dealer = game.getDealer() ; 
				Player player = game.getPlayer() ;
	    %>			

			<%  String message = (String) request.getAttribute("message") ;
			 	if(message != null && !message.isEmpty()){
			%>
			<!--		ここにplayerの勝敗を表示したい-->
					<p class="message"><%= message %></p>
					<p>
						<% if(game.getGamePhase()==Game.GamePhase.GAME_OVER && player.getIsWin()){ %>
								<%= loginUser.getUserName() %>さんの勝ちです
						<%
							}else if(game.getGamePhase()==Game.GamePhase.GAME_OVER && !player.getIsWin()) {
						%>
								<%= loginUser.getUserName() %>さんの負けです
						<%
							}
						%>
					</p>
				<%
				 }
			%>
			
				<dl>
					<dt>ディーラーの手札</dt>
						<% 
							List<Card> dealersCards = dealer.getHand() ;
							List<Card> playersCards = player.getHand() ;
							for(int i=0; i<dealersCards.size(); i++){
						%>
							<dd>(<%= dealersCards.get(i).getMark() %>,<%= dealersCards.get(i).getNumber() %>)</dd>
						<% 
							}
						%>
					<dt><%= loginUser.getUserName() %>さんの手札</dt>
						<% 
							for(int i=0; i<playersCards.size(); i++){
						%>
						<dd>(<%= playersCards.get(i).getMark() %>,<%= playersCards.get(i).getNumber() %>)</dd>
						<% 
							}
						%>	
				</dl>
				
				<form action="GameServlet">
					<input type="hidden" name="formAction" value="hit">
					<button>hit</button>
				</form>
				
				<form action="GameServlet">
					<input type="hidden" name="formAction" value="stand">
					<button>stand</button>
				</form>
			
		<%
	        }else{
		%>
			<p>ゲームを取得できませんでした<a href="user.jsp">ユーザー画面へ</a></p>
		<%
	        }
		%>
	<% 	
		}else {
	%>
		<p>ログインしていません<a href="login.jsp">ログインページへ</a></p>
	<%
		}
	%>

</body>
</html>