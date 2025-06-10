<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %> <%-- 勝率フォーマット用にfmtライブラリを追加 --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>戦績</title>
<%-- Google Fonts --%>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+JP:wght@400;700&display=swap" rel="stylesheet">
<%-- アイコン表示ライブラリ (Font Awesome) --%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/record.css">
</head>
<body>

    <%-- requestスコープからuserオブジェクトを取得 --%>
    <c:set var="user" value="${requestScope.user}" />

    <c:choose>
        <%-- userオブジェクトが正しく渡された場合 --%>
        <c:when test="${not empty user}">
            <div class="stats-container main-container">
                <h1><strong>${user.userName}</strong>さんの戦績</h1>

                <div class="stats-grid">
                    <%-- 試合数カード --%>
                    <div class="stat-card">
                        <i class="fa-solid fa-gamepad"></i>
                        <span class="stat-value"><fmt:formatNumber value="${user.numberOfGames}" /></span>
                        <span class="stat-label">試合数</span>
                    </div>

                    <%-- 勝利数カード --%>
                    <div class="stat-card">
                        <i class="fa-brands fa-web-awesome"></i>
                        <span class="stat-value"><fmt:formatNumber value="${user.victories}" /></span>
                        <span class="stat-label">勝利数</span>
                    </div>

                    <%-- 勝率カード --%>
                    <div class="stat-card">
                        <i class="fa-solid fa-percent"></i>
                        <span class="stat-value">
                            <c:choose>
                                <c:when test="${user.numberOfGames > 0}">
                                    <%-- fmt:formatNumberでパーセント表示と小数点以下の桁数調整を自動化 --%>
                                    <fmt:formatNumber value="${user.victories / user.numberOfGames}" type="percent" minFractionDigits="1" />
                                </c:when>
                                <c:otherwise>
                                    0<small>%</small>
                                </c:otherwise>
                            </c:choose>
                        </span>
                        <span class="stat-label">勝率</span>
                    </div>
                </div>

                <button class="btn btn-back" onclick="history.back()">戻る</button>
            </div>
        </c:when>

        <%-- userオブジェクトが渡されなかった、またはログインしていない場合 --%>
        <c:otherwise>
            <% response.sendRedirect(request.getContextPath() + "/login.jsp"); %>
        </c:otherwise>
    </c:choose>

</body>
</html>