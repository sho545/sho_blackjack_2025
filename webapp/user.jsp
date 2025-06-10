<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page import="user.User" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>ユーザー画面</title>
<%-- Google Fontsから美しい日本語フォントを読み込み --%>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+JP:wght@400;700&display=swap" rel="stylesheet">

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/user.css">

<script>
    function confirmSubmit(formId){
        if(confirm("本当にアカウントを削除しますか?")){
            document.getElementById(formId).submit();
        }else{
            return false;
        }
    }
</script>

</head>
<body>

    <c:choose>
        <%-- ログインしている場合の表示 --%>
        <c:when test="${not empty loginUser}">
            <div class="main-container dashboard-container">
            
                <div class="user-header">
                    <h1>${loginUser.userName}さん</h1>
                    <p>ようこそ！</p>
                </div>
                
                <c:if test="${not empty message }">
                    <p class="message"> ${message} </p>
                </c:if>

                <div class="action-menu">
            
                    <form action="${pageContext.request.contextPath}/gameSetup/start" method="post">
                        <button class="btn btn-primary">ゲームを始める</button>
                    </form>
                    
                    <%-- リンクもボタンのように見せる --%>
                    <a href="${pageContext.request.contextPath}/RecordServlet" class="btn btn-secondary">戦績表示</a>
                    <a href="${pageContext.request.contextPath}/RankingServlet" class="btn btn-secondary">勝率ランキング</a>
                </div>
                
                <div class="account-actions">
                    <form id="deleteForm-${loginUser.userId}" action="${pageContext.request.contextPath}/UserServlet" method="post">
                        <button type="button" class="btn btn-danger" onclick="confirmSubmit('deleteForm-${loginUser.userId}')">アカウント削除</button>
                    </form>
                    
                    <form action="${pageContext.request.contextPath}/session/logout" method="post">
                        <button class="btn btn-logout">ログアウト</button>
                    </form>
                </div>
            </div>
        </c:when>
        
        <%-- ログインしていない場合の表示（ログイン画面へのリダイレクト） --%>
        <c:otherwise>
            <% response.sendRedirect(request.getContextPath() + "/login.jsp"); %>
        </c:otherwise>
    </c:choose>

</body>
</html>