package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    
    //getリクエストのname="actionForm"の値によって分岐
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action = request.getParameter("formAction") ;
		
		if (action == null) {
	        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "actionパラメータが指定されていません。");
	        return;
		}
		 
		switch (action) {
			case "ranking" : 
				ranking(request, response) ;
				break ;
			default : 
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効なaction値です: " + action);
	            break;
		}		
	}
	
	//勝率ランキングurl(user.jsp)から上位ランキングTop5リスト(List<User>)をとってranking.jspへ
	protected void ranking(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		List<User> ranking = new ArrayList<>() ;
		String nextPage = "ranking.jsp" ;
		try {
			UserDao userDao = new UserDao() ;
			ranking = userDao.getRanking5() ;
			
		}catch(SQLException e) {
			System.err.println("データベースアクセス中にエラーが発生しました " + e.getMessage());
		    e.printStackTrace(); 
		}
		if(ranking != null) {
			request.setAttribute("ranking", ranking);
		}else {
			System.err.println("ランキングを取得できませんでした ") ;
		}
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(nextPage);
		requestDispatcher.forward(request, response);
	}

	
	
	//postリクエストのname="actionForm"の値によって分岐
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action = request.getParameter("formAction") ;
		
		if (action == null) {
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
			case "logout" :
				logout(request, response) ;
				break ;
			case "delete" :
				delete(request, response) ;
				break ;
			default : 
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効なaction値です: " + action);
	            break;
		}
		
	}
	
	//ログインボタン(login.jsp)からuser.jspへ(loginUserを引き渡し)
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
	
	//新規登録ボタン(register.jsp)からuserNameとpasswordを受け取って新規登録してlogin.jspへ
	protected void register (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
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
	
	//ログアウトボタン(user.jsp)からlogin.jspへ
	protected void logout (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String nextPage = "login.jsp" ;
		String message = "ログアウトしました" ;
		HttpSession session = request.getSession(false);
		
		if(session != null) {
			session.invalidate();
			request.setAttribute("message", message) ;
		}
		
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(nextPage);
		requestDispatcher.forward(request, response);
	}
	
	//ユーザー削除ボタン(user.jsp)からユーザーを削除してlogin.jspへ
	protected void delete (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String nextPage = "login.jsp" ;
		String message = null ;
		boolean result = false ;
		HttpSession session = request.getSession(false);
		
		if(session != null) {
			User loginUser = (User) session.getAttribute("loginUser") ;
			int userId = loginUser.getUserId() ;
			
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
