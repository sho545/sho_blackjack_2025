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
 * Servlet implementation class User
 */
@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	//受け取ったuserIdの戦績を表示
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nextPage = "record.jsp" ;
		UserDao userDao = new UserDao() ;
		HttpSession session = request.getSession(false) ;
		
		//sessionチェック
		if(session != null) {
			
			User loginUser = (User) session.getAttribute("loginUser") ;
			
			//ログインチェック
			if(loginUser != null) {
				try {
					User user = userDao.getRecord(loginUser.getUserId()) ;
					
					request.setAttribute("user", user);
					RequestDispatcher requestDispatcher = request.getRequestDispatcher(nextPage);
					requestDispatcher.forward(request, response);
					
				}catch(SQLException e) {
					System.err.println("データベースアクセス中にエラーが発生しました " + e.getMessage());
				    e.printStackTrace(); 
				}
			} else {
				response.sendRedirect(request.getContextPath() + "/login.jsp"); 
		        return; 
		    }
		}else {
			System.err.println("sessionを取得できませんでした ") ;
			response.sendRedirect(request.getContextPath() + "/login.jsp"); 
	        return; 
		}
	}

	//受け取ったuserIdのユーザーを削除
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String nextPage = "login.jsp" ;
		String message = null ;
		boolean result = false ;
		HttpSession session = request.getSession(false);
		
		//sessionチェック
		if(session != null) {
			User loginUser = (User) session.getAttribute("loginUser") ;
			int userId = loginUser.getUserId() ;
			//ログインチェック
			if(loginUser != null) {
				try {
					UserDao userDao = new UserDao() ;
					result = userDao.deleteUser(userId) ;
					if(result) {
						message = "ユーザーを削除しました" ;
					}else {
						message = "ユーザーを削除できませんでした" ;
					}
				 }catch(SQLException e) {
					System.err.println("データベースアクセス中にエラーが発生しました " + e.getMessage());
				    e.printStackTrace(); 
				    message = "データベースアクセス中にエラーが発生しました" ;
				 }
			} else {
				response.sendRedirect(request.getContextPath() + "/login.jsp"); 
		        return; 
			}
		}
		if(message != null) {
			request.setAttribute("message", message) ;
		}else {
			request.setAttribute("message", "予期せぬエラーが発生しました") ;
		}
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(nextPage);
		requestDispatcher.forward(request, response);
	}

}
