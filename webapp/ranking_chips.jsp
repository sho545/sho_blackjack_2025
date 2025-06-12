<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>ランキング</title>
<%-- Google Fonts --%>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+JP:wght@400;700;900&display=swap" rel="stylesheet">
<%-- アイコン表示ライブラリ (Font Awesome) --%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/ranking.css">

</head>
<body>

    <div class="ranking-container main-container">
        <h1><i class="fa-solid fa-crown"></i>チップランキング</h1>
        
        <c:choose>
            <%-- ランキングリストが空でない場合 --%>
            <c:when test="${not empty ranking}">
                <ol class="ranking-list">
                    <%-- c:forEachでランキングリストをループ処理 --%>
                    <%-- varStatus="status" でループの情報を取得 --%>
                    <c:forEach var="user" items="${ranking}" varStatus="status">
                    
                        <li class="rank-item rank-${status.count}">
                        
                            <%-- ユーザー情報 --%>
                            <div class="user-info">
                                <div class="user-name">
                                    ${user.userName}さん
                                    <%-- 1位から3位までメダルを表示 --%>
                                    <c:if test="${status.count <= 3}">
                                        <i class="fa-solid fa-medal"></i>
                                    </c:if>
                                </div>
                                <div class="user-id">
                                    (ユーザーID: ${user.userId})
                                </div>
                            </div>
                            
                            <%-- 戦績詳細 --%>
                            <div class="user-stats">
                            	<span>チップ数: <fmt:formatNumber value="${user.chips}" /> </span>
                                <span>試合数: <fmt:formatNumber value="${user.numberOfGames}" /></span>
                                <span>勝利数: <fmt:formatNumber value="${user.victories}" /></span>
                                <span>
                                    勝率: 
                                    <c:choose>
                                        <c:when test="${user.numberOfGames > 0}">
                                            <fmt:formatNumber value="${user.victories / user.numberOfGames}" type="percent" minFractionDigits="1" />
                                        </c:when>
                                        <c:otherwise>0.0%</c:otherwise>
                                    </c:choose>
                                </span>
                            </div>
                            
                        </li>
                        
                    </c:forEach>
                </ol>
            </c:when>

            <%-- ランキングが空の場合の表示 --%>
            <c:otherwise>
                <p class="no-ranking">まだ誰もプレイしていません。</p>
            </c:otherwise>
        </c:choose>
        
        <div class="actions">
            <button class="btn btn-back" onclick="history.back()">戻る</button>
        </div>
    </div>

</body>
</html>