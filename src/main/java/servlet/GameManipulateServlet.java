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
		if(gamePhase == Game.GamePhase.INITIAL_DEAL || gamePhase == Game.GamePhase.PLAYER_TURN || gamePhase == Game.GamePhase.SPLIT_PLAYER_TURN) {
		
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
						
					case "/split" :
						split(request, response) ;
						break ;
						
					case "/notSplit" :
						notSplit(request, response) ;
						break ;
						
					case "/splitHit" :
						splitHit(request, response) ;
						break ;
						
					case "/splitStand" :
						splitStand(request, response) ;
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
	
	//splitの処理したときの処理
	//掛け金が足りないときは強制敵にnotsplitの状態へ
	public void split(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		HttpSession session = request.getSession(false) ;
		GameMaster gameMaster = (GameMaster) session.getAttribute("gameMaster") ;
		String nextPage = "/blackjack.jsp" ;
		String message = "手札1のターンです。hitしますか?standしますか?" ;
		
		if(gameMaster != null) {
			
			Game game = gameMaster.getGame() ;
			//掛け金の2倍がUserの持ってるチップ以下のときsplitを行う
			if(game.getPlayer().getChipsForGame()*2 <= game.getPlayer().getUser().getChips()) {
				gameMaster.split(game);
			} else {
				message="チップが足りないためsplitできません" ;
			}
			
			request.setAttribute("gameMaster", gameMaster);
			request.setAttribute("message", message);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(nextPage);
			requestDispatcher.forward(request, response);
			
		}else {
			System.err.println("gameMasterを取得できませんでした");
		}
	}
	
	//splitしなかったときの処理
	public void notSplit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		HttpSession session = request.getSession(false) ;
		GameMaster gameMaster = (GameMaster) session.getAttribute("gameMaster") ;
		String nextPage = "/blackjack.jsp" ;
		String message = "hitしますか?standしますか?" ;
		
		if(gameMaster != null) {
			
			Game game = gameMaster.getGame() ;
			game.setGamePhase(Game.GamePhase.PLAYER_TURN);
			
			request.setAttribute("gameMaster", gameMaster);
			request.setAttribute("message", message);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(nextPage);
			requestDispatcher.forward(request, response);
			
		}else {
			System.err.println("gameMasterを取得できませんでした");
		}
	}
	
	//splitHitの処理
	public void splitHit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(false) ;
		GameMaster gameMaster = (GameMaster) session.getAttribute("gameMaster") ;
		String nextPage = "/blackjack.jsp" ;
		String message = null ;
		
		if(gameMaster != null) {	
			Game game = gameMaster.getGame() ;
			gameMaster.splitPlayerHits(game);
			//splitPlayerがbustしていたらgamePhaseはplayer_turnになっている
			gameMaster.checkAndSetBust(game) ;
			boolean splitPlayerIsBust = game.getSplitPlayer().isBust() ;
			
			if(splitPlayerIsBust) {
				message = "手札2のターンです。hitしますか？standしますか？" ;
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
			
	//splitStandの処理
	public void splitStand(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(false) ;
		GameMaster gameMaster = (GameMaster) session.getAttribute("gameMaster") ;
		String nextPage = "/blackjack.jsp" ;
		String message = "手札2のターンです。hitしますか？standしますか？" ;
		
		if(gameMaster != null) {
			Game game = gameMaster.getGame() ;
			game.setGamePhase(Game.GamePhase.PLAYER_TURN);
			
			request.setAttribute("gameMaster", gameMaster);
			request.setAttribute("message", message);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(nextPage);
			requestDispatcher.forward(request, response);
			
		}else {
			System.err.println("gameMasterを取得できませんでした");
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
				gameMaster.calculateChips(game) ;
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
			gameMaster.checkAndSetBust(game) ;
			//splitPlayerかplayerがbustでないときだけディーラーのターンが発生
			if((game.getPlayer().isBust() == false) || (game.getSplitPlayer() != null && game.getSplitPlayer().isBust() == false)) {
				gameMaster.makeDealerDrawCards(game) ;
			}
			gameMaster.checkGameOver(game) ;
			int gettingChips = gameMaster.calculateChips(game) ;
			
			if(game.getGamePhase() == Game.GamePhase.GAME_OVER) {
				
				request.setAttribute("gettingChips", gettingChips);
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
