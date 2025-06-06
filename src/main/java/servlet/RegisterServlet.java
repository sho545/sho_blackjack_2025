package servlet;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.UserDao;
import user.User;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userName = null ;
		String password = null ;
		User registeredUser = null ;
		boolean result = false ;
		String nextPage = "login.jsp" ;
		String message = null ;
		
		userName = request.getParameter("userName") ;
		password = request.getParameter("password") ;
		
		try {
			UserDao userDao = new UserDao() ;
			registeredUser = userDao.findUser(userName, password) ;
			
			if(registeredUser == null) {
				
				result = userDao.registerUser(userName, password) ;
				
				if(result) {
					message = "新規登録しました" ;
				}else {
					message = "登録に失敗しました" ;
				}
			}else {
//				ここは検討
				message = "そのuser nameとpasswordは既に登録されています" ;
				nextPage = "register.jsp" ;
			}
			
		}catch(SQLException e) {
			 System.err.println("データベースアクセス中にエラーが発生しました " + e.getMessage());
		     e.printStackTrace(); 
		     message = "データベースアクセス中にエラーが発生しました" ;
		}catch(Exception e) {
			System.err.println("予期せぬエラーが発生しました: " + e.getMessage());
	        e.printStackTrace();
	        message = "予期せぬエラーが発生しました" ;
		}
		if(message != null) {
			request.setAttribute("message", message) ;
		}
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(nextPage);
		requestDispatcher.forward(request, response);
	}

}
