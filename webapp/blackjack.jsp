<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>ブラックジャック</title>
<%-- 既存のカード用CSSを読み込み --%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/blckjack_card.css">
<%-- Google Fonts --%>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+JP:wght@400;700;900&display=swap" rel="stylesheet">

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/blackjack.css">
</head>
<body>
    
    <c:choose>
        <%-- ログイン,ゲームセッションチェック --%>
        <c:when test="${not empty loginUser and not empty gameMaster}">
        	
        	<%-- JSTLを使って、ページ内で使う変数を最初に準備する --%>
		    <c:set var="loginUser" value="${sessionScope.loginUser}" />
		    <c:set var="gameMaster" value="${sessionScope.gameMaster}" />
		    <c:set var="game" value="${gameMaster.game}" />
		    <c:set var="dealer" value="${game.dealer}" />
		    <c:set var="player" value="${game.players[0]}" />
		    <c:set var="gamePhase" value="${game.gamePhase}" />
		    
            <div class="game-table">

                <%-- =========== ディーラーのエリア =========== --%>
                <div class="hand-area dealer-area">
                    <h2>
                        <span>ディーラーの手札</span>
                        <span class="score">
                            <%-- ゲーム終了時のみ合計点を表示 --%>
                            <c:if test="${gamePhase == 'GAME_OVER'}">
                                ${dealer.hands[0].sumOfHand}
                            </c:if>
                        </span>
                    </h2>
                    <div class="hand">
                        <c:forEach var="card" items="${dealer.hands[0].list}" varStatus="status">
                            <%-- ゲーム中で、かつ2枚目のカードの場合だけ裏返す --%>
                            <c:choose>
                                <c:when test="${gamePhase != 'GAME_OVER' and status.count == 2}">
                                    <div class="card back"></div>
                                </c:when>
                                <c:otherwise>
	                               <div class="card suit-${card.mark} rank-${card.number}">
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
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </div>
                </div>

                <%-- =========== ゲーム情報とアクションエリア =========== --%>
                 <div class="game-info">
                    <p class="message">${message}</p>
                    <div class="action-buttons">
	                    <c:choose>
	                        <%-- 1. ゲーム終了時の表示 --%>
	                        <c:when test="${gamePhase == 'GAME_OVER'}">
	                            <p class="game-result result-win">ゲーム終了！ チップ<fmt:formatNumber value="${gettingChips}" />枚獲得です。</p>
	                            <form action="${pageContext.request.contextPath}/gameSetup/over" method="post" style="margin-top: 20px;"><button class="btn btn-gameover">ユーザー画面へ</button></form>
	                        </c:when>
	
	                        <%-- 2. ベット画面 --%>
	                        <c:when test="${gamePhase == 'NOT_STARTED' }">
	                        	<div class="betting-area">
							        <p class="instruction">ベットするチップの枚数を入力してください(所持チップ数${loginUser.chips }枚)</p>
							        <form id="chips" class="bet-form" action="${pageContext.request.contextPath}/gameSetup/chips" method="post">			                
						                <input class="bet-input" name="chipsForGame" type="number" value="10" min="0" max="${loginUser.chips}" required>		                
						                <button class="btn btn-bet">ベットして開始</button>
						            </form>
								</div>     
	                        </c:when>
	                        
	                        <%-- 3. スプリット選択画面 --%>
	                        <c:when test="${not empty player.split && gamePhase == 'INITIAL_DEAL'}">
	                       		<div class="split-choices">
					                <form action="${pageContext.request.contextPath}/game/split" method="post"><button class="btn btn-split">はい (Split)</button></form>
					                <form action="${pageContext.request.contextPath}/game/notSplit" method="post"><button class="btn btn-not-split">いいえ (続行)</button></form>
	                 			</div>
	                        </c:when>
	                        
	                        <%-- 4. それ以外のゲーム中のアクションボタン (Hit/Stand) --%>
	                        <c:when test="${gamePhase == 'PLAYER_TURN' }">
	                        		<form action="${pageContext.request.contextPath}/game/hit" method="post"><button class="btn btn-hit">Hit</button></form>
                            		<form action="${pageContext.request.contextPath}/game/stand" method="post"><button class="btn btn-stand">Stand</button></form>
	                        </c:when>
	
	                    </c:choose>
                    </div>
                </div>
                

                <%-- =========== プレイヤーのエリア =========== --%>
               	<div class="player-hands-container">
	             	<c:forEach var="hand" items="${player.hands}" varStatus="handStatus">
                        <c:if test="${not empty hand.list }">
                        		<div class="hand-area player-area ${hand.handPhase == 'PLAY' ? 'active-hand' : ''}">
	                            <h2>
	                                <span>${loginUser.userName}さんの手札</span>
	                                <span class="score">${hand.sumOfHand}</span>
	                            </h2>
	                            <div class="hand">
	                                <c:forEach var="card" items="${hand.list}">
	                                    <div class="card suit-${card.mark} rank-${card.number}"><div class="card-inner"><div class="card-topleft"><span class="rank"></span><span class="suit"></span></div><div class="card-center"><span class="suit-big"></span></div><div class="card-bottomright"><span class="rank"></span><span class="suit"></span></div></div></div>
	                                </c:forEach>
	                            </div>
                        		</div>
                        	</c:if>
                    </c:forEach>  
               	</div>
               	
             </div>
          </c:when>

        <%-- ログインしていない、またはゲームが始まっていない場合 --%>
        <c:otherwise>
            <% response.sendRedirect(request.getContextPath() + "/login.jsp"); %>
        </c:otherwise>
    </c:choose>

</body>
</html>