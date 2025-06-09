package servlet;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.Game;
import model.GameMaster;


@WebServlet("/game/*")
public class GameManipulateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(false) ;
		GameMaster gameMaster = (GameMaster) session.getAttribute("gameMaster") ;
		Game.GamePhase gamePhase = gameMaster.getGame().getGamePhase() ;
		
		//GamePhaseチェック
		if(gamePhase == Game.GamePhase.PLAYER_TURN) {
		
			String actionPath = request.getPathInfo() ; // ServletPathの後の値
			
			if(actionPath == null) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "アクションパスが指定されていません。");
	            return;
			}else {
				
				switch(actionPath) {
					
					case "/hit" :
						hit(request, response) ;
						break ;
						
					case "/stand" :
						stand(request, response) ;
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
	
	//hitの処理
	public void hit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		HttpSession session = request.getSession(false) ;
		GameMaster gameMaster = (GameMaster) session.getAttribute("gameMaster") ;
		String nextPage = "/blackjack.jsp" ;
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
		String nextPage = "/blackjack.jsp" ;
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

}
