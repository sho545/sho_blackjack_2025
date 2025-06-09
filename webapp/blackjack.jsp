<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
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
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/blckjack_card.css">

<!--	standからのディーラーの二枚目のカードオープン関数-->
<!--	<script type="text/javascript">-->
<!--		document.addEventListener("DOMContentLoaded", function(){-->
<!--			const standButton = document.getElementById("stand");-->
<!--			const hitButton = document.getElementById("hit") ;-->

<!--			if(standButton && hitButton){-->
<!--				standButton.addEventListener("click", function(event){-->
<!--					standButton.disabled = true ;-->
<!--					hitButton.disabled = true ;	-->
<!--				})-->
<!--			}-->
<!--		});-->
<!--	</script>-->

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
						<% if(game.getGamePhase()==Game.GamePhase.GAME_OVER && player.getPlayerResult()==Player.PlayerResult.WIN){ %>
								<%= loginUser.getUserName() %>さんの勝ちです
								<form action="${pageContext.request.contextPath}/gameSetup/over" method="post">
									<button>ゲーム終了</button>
								</form>
						<%
							}else if(game.getGamePhase()==Game.GamePhase.GAME_OVER && player.getPlayerResult()==Player.PlayerResult.LOSE) {
						%>
								<%= loginUser.getUserName() %>さんの負けです
								<form action="${pageContext.request.contextPath}/gameSetup/over" method="post">
									<button>ゲーム終了</button>
								</form>
						<%
							}else if(game.getGamePhase()==Game.GamePhase.GAME_OVER && player.getPlayerResult()==Player.PlayerResult.DRAW){
						%>
								<%= loginUser.getUserName() %>さん引き分けです
								<form action="${pageContext.request.contextPath}/gameSetup/over" method="post">
									<button>ゲーム終了</button>
								</form>
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
						%>	
							<dd>
						<% 	
							for(int i=0; i<dealersCards.size(); i++){
								if(i == 1 && game.getGamePhase() != Game.GamePhase.GAME_OVER){
						%>
								<div class="card suit-<%= dealer.getHand().get(i).getMark()%> 
									rank-<%= dealer.getHand().get(i).getNumber()%> back">
								  <div class="card-inner">
								    <div class="card-topleft">
								      <span class="rank"></span>
								      <span class="suit"></span>
								    </div>
								    <div class="card-center">
								      <span class="suit-big"></span>
								    </div>
								    <div class="card-bottomright">
								      <span class="rank"></span>
								      <span class="suit"></span>
								    </div>
								  </div>
								</div>
							<%
								}else {
							%>
								<div id="dealersSecondCard"
									class="card suit-<%= dealer.getHand().get(i).getMark()%> 
									rank-<%= dealer.getHand().get(i).getNumber()%>">
								  <div class="card-inner">
								    <div class="card-topleft">
								      <span class="rank"></span>
								      <span class="suit"></span>
								    </div>
								    <div class="card-center">
								      <span class="suit-big"></span>
								    </div>
								    <div class="card-bottomright">
								      <span class="rank"></span>
								      <span class="suit"></span>
								    </div>
								  </div>
								</div>	
							<% 
								}
							%> 
						<% 
							}
						%>
							</dd>
					<dt><%= loginUser.getUserName() %>さんの手札</dt>
						<dd>
						<% 
							for(int i=0; i<playersCards.size(); i++){
						%>
							<div class="card suit-<%= player.getHand().get(i).getMark()%> rank-<%= player.getHand().get(i).getNumber()%>">
							  <div class="card-inner">
							    <div class="card-topleft">
							      <span class="rank"></span>
							      <span class="suit"></span>
							    </div>
							    <div class="card-center">
							      <span class="suit-big"></span>
							    </div>
							    <div class="card-bottomright">
							      <span class="rank"></span>
							      <span class="suit"></span>
							    </div>
							  </div>
							</div>
						<% 
							}
						%>
						</dd>	
				</dl>
				
				<% 
				if(game.getGamePhase()!=Game.GamePhase.GAME_OVER) {
				%>
					<form id="hit" action="${pageContext.request.contextPath}/game/hit" method="post">
<!--						<input type="hidden" name="formAction" value="hit">-->
						<button>hit</button>
					</form>
					
					<form id="stand" action="${pageContext.request.contextPath}/game/stand" method="post">
<!--						<input type="hidden" name="formAction" value="stand">-->
						<button>stand</button>
					</form>
				<%
				}
				%>
			
		<%
	        }else{
		%>
			<p>ゲームを取得できませんでした<a href="${pageContext.request.contextPath}/user.jsp">ユーザー画面へ</a></p>
		<%
	        }
		%>
	<% 	
		}else {
	%>
		<p>ログインしていません<a href="${pageContext.request.contextPath}/login.jsp">ログインページへ</a></p>
	<%
		}
	%>

</body>
</html>