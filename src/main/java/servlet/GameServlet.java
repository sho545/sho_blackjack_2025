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


@WebServlet("/GameServlet")
public class GameServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GameServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("formAction") ;
		
		if (action == null) {
	        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "actionパラメータが指定されていません。");
	        return;
		}
		 
		switch (action) {
			case "hit" : 
				hit(request, response) ;
				break ;
			case "stand" :
				stand(request,response) ;
				break ;
			default : 
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効なaction値です: " + action);
	            break;
		}		
	}
	
	//hitの処理
	public void hit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		HttpSession session = request.getSession(false) ;
		GameMaster gameMaster = (GameMaster) session.getAttribute("gameMaster") ;
		String nextPage = "blackjack.jsp" ;
		String message = null ;
		
		if(gameMaster != null) {
			
			Game game = gameMaster.getGame() ;
			gameMaster.playerHits(game);
			gameMaster.checkAndSetBust(game) ;
			boolean playerIsBust = game.getPlayer().isBust() ;
			
			if(playerIsBust) {
				gameMaster.checkGameOver(game);
				message = "ゲーム終了です" ;
			}else {
				message = "hitしますか?standしますか?" ;
			}
			
			request.setAttribute("gameMaster", gameMaster);
			request.setAttribute("message", message);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(nextPage);
			requestDispatcher.forward(request, response);
			
		}else {
			System.err.println("gameMasterを取得できませんでした");
		}
	}
	
	//standの処理
	public void stand(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(false) ;
		GameMaster gameMaster = (GameMaster) session.getAttribute("gameMaster") ;
		String nextPage = "blackjack.jsp" ;
		String message = "ゲーム終了です" ;
		
		if(gameMaster != null) {
			
			Game game = gameMaster.getGame() ;
			gameMaster.makeDealerDrawCards(game) ;
			gameMaster.checkAndSetBust(game) ;
			gameMaster.checkGameOver(game) ;
			
			if(game.getGamePhase() == Game.GamePhase.GAME_OVER) {
			
				request.setAttribute("gameMaster", gameMaster);
				request.setAttribute("message", message);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher(nextPage);
				requestDispatcher.forward(request, response);
			}else {
				System.err.println("ゲームフェーズが終了になっていません");
			}
			
		}else {
			System.err.println("gameMasterを取得できませんでした");
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("formAction") ;
		
		if (action == null) {
	        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "actionパラメータが指定されていません。");
	        return;
		}
		 
		switch (action) {
			case "setGame" : 
				setGame(request, response) ;
				break ;
			case "gameOver" :
				gameOver(request, response) ;
				break ;
			default : 
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効なaction値です: " + action);
	            break;
		}		
	}
	
	//ゲームを始める(user.jsp)から最初の手札を表示(blackjack.jsp)
	public void setGame(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(false) ;
		User loginUser = (User)session.getAttribute("loginUser") ;
		
		String nextPage = "blackjack.jsp" ;
		
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
	public void gameOver(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(false) ;
		User loginUser = (User)session.getAttribute("loginUser") ;
		GameMaster gameMaster = (GameMaster) session.getAttribute("gameMaster") ;
		
		String nextPage = "user.jsp" ;
		
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
				RequestDispatcher requestDispatcher = request.getRequestDispatcher(nextPage);
				requestDispatcher.forward(request, response);
			}else {
				System.err.println("sessionからloginUserを取得できませんでした");
			}
			
		}else {
			System.err.println("sessionからgameMasterを取得できませんでした");
		}
	}


}
