<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="user.User" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>ユーザー画面</title>
	<script>
		function confirmSubmit(formId){
			if(confirm("本当にアカウントを削除しますか?")){
				document.getElementById(formId).submit() ;
			}else{
				return false ;
			}
		}
	</script>
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
	<p><strong><%= loginUser.getUserName() %>さん</strong>(ユーザーid : <%= loginUser.getUserId() %>)</p>
	
	<p>
		<form action="${pageContext.request.contextPath}/gameSetup/start" method="post">
<!--			<input type="hidden" name="formAction" value="setGame">-->
			<button>ゲームを始める</button>
		</form>
	</p>
	<p>
		<a href="${pageContext.request.contextPath}/RecordServlet">戦績表示</a>
	</p>
	<p>
		<a href="${pageContext.request.contextPath}/RankingServlet">勝率ランキング</a>
	</p>
	<p>
		<form id="deleteForm<%=loginUser.getUserId() %>" action="${pageContext.request.contextPath}/UserServlet" method="post">
			<button type="button" onclick="confirmSubmit('deleteForm<%= loginUser.getUserId() %>')">ユーザー削除</button>
		</form>
	</p>
	<p>
		<form action="${pageContext.request.contextPath}/session/logout" method="post">
<!--			<input type="hidden" name="formAction" value="logout">-->
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