<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ユーザー画面</title>
</head>
<body>
	<%
        // 前の画面から渡されたIDを取得 (例)
        String receivedId = request.getParameter("userId");
        if (receivedId == null) {
            receivedId = "defaultId"; 
        }
        String receivedName = request.getParameter("userName");
        if (receivedName == null) {
            receivedName = "defaultName"; 
        }
    %>

	<p><strong><%= receivedName %>さん</strong></p>

	<p>
		<a href="blackjack.jsp?id=<%= receivedId %>ゲームを始める</a>
	</p>
	<p>
		<a href="ManageServlet?id=<%= receivedId %>戦績表示</a>
	</p>
</body>
</html>