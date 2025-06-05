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
 * Servlet implementation class Register
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String nextPage = "record.jsp" ;
		UserDao userDao = new UserDao() ;
		HttpSession session = request.getSession(false) ;
		
		if(session != null) {
			User loginUser = (User) session.getAttribute("loginUser") ;
			try {
				User user = userDao.getRecord(loginUser.getUserId()) ;
				
				request.setAttribute("user", user);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher(nextPage);
				requestDispatcher.forward(request, response);
				
			}catch(SQLException e) {
				System.err.println("データベースアクセス中にエラーが発生しました " + e.getMessage());
			    e.printStackTrace(); 
			}
		}else {
			System.err.println("sessionを取得できませんでした ") ;
		}
	}

}
