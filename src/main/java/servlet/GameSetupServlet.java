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
import model.Game;
import model.GameMaster;
import model.player.Player;
import model.player.Player.PlayerResult;
import user.User;

/**
 * Servlet implementation class GameSetup
 */
@WebServlet("/gameSetup/*")
public class GameSetupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(false) ;
		User loginUser = (User) session.getAttribute("loginUser") ;
		
		if(loginUser != null) {
			String actionPath = request.getPathInfo() ; // ServletPathの後の値
			
			if(actionPath == null) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "アクションパスが指定されていません。");
	            return;
			}else {
				
				switch(actionPath) {
					
					case "/start" :
						start(request, response) ;
						break ;
						
					case "/over" :
						over(request, response) ;
						break ;
						
					default :
						response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効なアクションパスです: " + actionPath);
			            break;
				}
			}
		}else {
			response.sendRedirect(request.getContextPath() + "/login.jsp"); 
	        return; 
		}
	}
	
	//ゲームを始める(user.jsp)から最初の手札を表示(blackjack.jsp)
	public void start(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(false) ;
		User loginUser = (User)session.getAttribute("loginUser") ;
		
		String nextPage = "/blackjack.jsp" ;
		
		if(loginUser != null) {
			Game game = new Game(loginUser) ;
			GameMaster gameMaster = new GameMaster(game) ;
			gameMaster.setGame(game);
			
			session.setAttribute("gameMaster", gameMaster);
			request.setAttribute("message", "hitしますか?standしますか?");
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(nextPage);
			requestDispatcher.forward(request, response);
			
		}else {
			System.err.println("sessionからloginUserを取得できませんでした");
		}
	}
	
	//ゲームを終了して戦績をデータベースに書き込んでユーザー画面へ
	public void over(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(false) ;
		User loginUser = (User)session.getAttribute("loginUser") ;
		GameMaster gameMaster = (GameMaster) session.getAttribute("gameMaster") ;
		
		if(gameMaster != null) {
			if(loginUser != null) {
				Game game = gameMaster.getGame() ;
				PlayerResult playerResult = game.getPlayer().getPlayerResult() ;
				UserDao userDao = new UserDao() ;
				
				if(playerResult == Player.PlayerResult.WIN) {
					try {
						userDao.victoriesPlus1(loginUser.getUserId()) ;
					}catch(SQLException e) {
						System.err.println("データベースアクセス中にエラーが発生しました: " + e.getMessage());
				        e.printStackTrace();

					}
				}else {
					try {
						userDao.numberOfGamesPlus1(loginUser.getUserId()) ;
					}catch(SQLException e) {
						System.err.println("データベースアクセス中にエラーが発生しました: " + e.getMessage());
				        e.printStackTrace();
					}
				}
				session.removeAttribute("gameMaster");
				response.sendRedirect(request.getContextPath() + "/user.jsp");
			}else {
				System.err.println("sessionからloginUserを取得できませんでした");
			}
			
		}else {
			System.err.println("sessionからgameMasterを取得できませんでした");
		}
	}

}
