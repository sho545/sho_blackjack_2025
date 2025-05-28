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


@WebServlet("/ManageServlet")
public class ManageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public ManageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action = request.getParameter("formAction") ;
		
		if (action == null) {
	        // actionパラメータがない場合のエラー処理
	        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "actionパラメータが指定されていません。");
	        return;
		}
		 
		switch (action) {
			case "login" : 
				login(request, response) ;
				break ;
			case "register" :
				register(request, response) ;
				break ;
			default : 
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効なaction値です: " + action);
	            break;
		}
		
	}
	
	//ログイン
	protected void login (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String userName = null ;
		String password = null ;
		User loginUser = null ;
		String nextPage = "login.jsp" ;
		String message = null ;
		try {
			userName = request.getParameter("userName") ;
			password = request.getParameter("password") ;
			UserDao userDao = new UserDao() ;
			loginUser = userDao.findUser(userName, password) ;
			HttpSession session = request.getSession() ;
			
			if(loginUser != null) {
				session.setAttribute("loginUser", loginUser);
				message = "ログインしました" ;
				nextPage = "user.jsp" ;
			}else {
				message = "ユーザーネームかパスワードが無効です" ;
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
	
	//新規登録
	protected void register (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String userName = null ;
		String password = null ;
		boolean result = false ;
		String nextPage = "login.jsp" ;
		String message = null ;
		try {
			userName = request.getParameter("userName") ;
			password = request.getParameter("password") ;
			UserDao userDao = new UserDao() ;
			result = userDao.registerUser(userName, password) ;
			
			if(result) {
				message = "新規登録しました" ;
			}else {
				message = "登録に失敗しました" ;
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
