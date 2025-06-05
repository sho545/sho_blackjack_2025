package servlet;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.UserDao;
import user.User;

/**
 * Servlet implementation class Session
 */
@WebServlet("/session/*")
public class SessionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String actionPath = request.getPathInfo() ; // ServletPathの後の値
		
		if(actionPath == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "アクションパスが指定されていません。");
            return;
		}else {
			
			switch(actionPath) {
				
				case "/login" :
					login(request, response) ;
					break ;
					
				case "/logout" :
					logout(request, response) ;
					break ;
					
				default :
					response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効なアクションパスです: " + actionPath);
		            break;
			}
		}
	}
	
	//ログインボタン(login.jsp)からuser.jspへ(loginUserを引き渡し)
	protected void login (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String userName = null ;
		String password = null ;
		User loginUser = null ;
		String nextPage = "login.jsp" ;
		String message = null ;
		HttpSession session = request.getSession() ;
		
		userName = request.getParameter("userName") ;
		password = request.getParameter("password") ;
		
		UserDao userDao = new UserDao() ;
		try {
			
			loginUser = userDao.findUser(userName, password) ;
			
		}catch(SQLException e) {
			System.err.println("データベースアクセス中にエラーが発生しました " + e.getMessage());
		    e.printStackTrace(); 
		    message = "データベースアクセス中にエラーが発生しました" ;
		}
		
		if(loginUser != null) {
			session.setAttribute("loginUser", loginUser);
			message = "ログインしました" ;
			nextPage = "/user.jsp" ;
		} else {
			message = "ユーザーネームかパスワードが無効です" ;
		}
		
		if(message != null) {
			request.setAttribute("message", message) ;
		}
	
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(nextPage);
	    requestDispatcher.forward(request, response);
	   
	}
	
	//ログアウトボタン(user.jsp)からlogin.jspへ
		protected void logout (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
			String nextPage = "/login.jsp" ;
			String message = "ログアウトしました" ;
			HttpSession session = request.getSession(false);
			
			if(session != null) {
				session.invalidate();
				request.setAttribute("message", message) ;
			}
			
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(nextPage);
			requestDispatcher.forward(request, response);
		}

}
