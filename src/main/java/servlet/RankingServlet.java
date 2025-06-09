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

import dao.UserDao;
import user.User;

/**
 * Servlet implementation class RankingServlet
 */
@WebServlet("/RankingServlet")
public class RankingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

	
}
