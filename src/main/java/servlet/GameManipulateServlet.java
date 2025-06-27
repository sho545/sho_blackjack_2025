package servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.Game;
import model.GameMaster;
import model.card.Hand;
import model.card.Hand.HandPhase;
import model.player.Player;


@WebServlet("/game/*")

//playerは一人を想定しています
public class GameManipulateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(false) ;
		GameMaster gameMaster = (GameMaster) session.getAttribute("gameMaster") ;
		Game.GamePhase gamePhase = gameMaster.getGame().getGamePhase() ;
		
		//GamePhaseチェック
		if(gamePhase == Game.GamePhase.INITIAL_DEAL || gamePhase == Game.GamePhase.PLAYER_TURN ) {
		
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
						
//					case "/splitHit" :
//						splitHit(request, response) ;
//						break ;
//						
//					case "/splitStand" :
//						splitStand(request, response) ;
//						break ;
						
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
	
	//splitしたときの処理
	//掛け金が足りないときは強制敵にnotsplitの状態へ
	public void split(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		HttpSession session = request.getSession(false) ;
		GameMaster gameMaster = (GameMaster) session.getAttribute("gameMaster") ;
		String nextPage = "/blackjack.jsp" ;
		String message = "hitしますか?standしますか?" ;
		
		if(gameMaster != null) {
			
			Game game = gameMaster.getGame() ;
			//掛け金の2倍がUserの持ってるチップ以下のときsplitを行う
			if(game.getPlayers().get(0).getHands().get(0).getChipsForGame() *2 <= game.getPlayers().get(0).getUser().getChips()) {
				gameMaster.split(game);
				game.setGamePhase(Game.GamePhase.PLAYER_TURN);
				game.getPlayers().get(0).getHands().get(0).setHandPhase(HandPhase.PLAY);
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
			game.getPlayers().get(0).getHands().get(0).setHandPhase(HandPhase.PLAY);
			
			request.setAttribute("gameMaster", gameMaster);
			request.setAttribute("message", message);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(nextPage);
			requestDispatcher.forward(request, response);
			
		}else {
			System.err.println("gameMasterを取得できませんでした");
		}
	}
	
//	//splitHitの処理
//	public void splitHit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		
//		HttpSession session = request.getSession(false) ;
//		GameMaster gameMaster = (GameMaster) session.getAttribute("gameMaster") ;
//		String nextPage = "/blackjack.jsp" ;
//		String message = null ;
//		
//		if(gameMaster != null) {	
//			Game game = gameMaster.getGame() ;
//			gameMaster.splitPlayerHits(game);
//			gameMaster.checkAndSetBust(game) ;
//			boolean splitPlayerIsBust = game.getSplitPlayer().isBust() ;
//			
//			if(splitPlayerIsBust) {
//				game.setGamePhase(Game.GamePhase.PLAYER_TURN) ;
//				message = "手札2のターンです。hitしますか？standしますか？" ;
//			}else {
//				message = "hitしますか?standしますか?" ;
//			}
//			
//			request.setAttribute("gameMaster", gameMaster);
//			request.setAttribute("message", message);
//			RequestDispatcher requestDispatcher = request.getRequestDispatcher(nextPage);
//			requestDispatcher.forward(request, response);
//			
//		}else {
//			System.err.println("gameMasterを取得できませんでした");
//		}
//	}
//			
//	//splitStandの処理
//	//ゲームフェイズをplayer turnにしてゲーム画面に返還
//	public void splitStand(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		
//		HttpSession session = request.getSession(false) ;
//		GameMaster gameMaster = (GameMaster) session.getAttribute("gameMaster") ;
//		String nextPage = "/blackjack.jsp" ;
//		String message = "手札2のターンです。hitしますか？standしますか？" ;
//		
//		if(gameMaster != null) {
//			Game game = gameMaster.getGame() ;
//			game.setGamePhase(Game.GamePhase.PLAYER_TURN);
//			
//			request.setAttribute("gameMaster", gameMaster);
//			request.setAttribute("message", message);
//			RequestDispatcher requestDispatcher = request.getRequestDispatcher(nextPage);
//			requestDispatcher.forward(request, response);
//			
//		}else {
//			System.err.println("gameMasterを取得できませんでした");
//		}
//	}
	
	
	//hitの処理
	public void hit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		HttpSession session = request.getSession(false) ;
		GameMaster gameMaster = (GameMaster) session.getAttribute("gameMaster") ;
		String nextPage = "/blackjack.jsp" ;
		String message = null ;
		
		if(gameMaster != null) {
			Game game = gameMaster.getGame() ;
			//playerは一人と想定
			Player player = game.getPlayers().get(0) ;
			List<Hand> hands = player.getHands() ;
			Hand playHand = null ;
			int playHandIndex = -1 ;
			//playerのhandsを順に確認していってはじめてOVERでなかったhandを取り出す
			for(int i=0; i<hands.size(); i++) {
				if(hands.get(i).getHandPhase() != HandPhase.OVER) {
					playHand = hands.get(i) ;
					playHandIndex = i ;
					break;
				}			
			}
			playHand.setHandPhase(HandPhase.PLAY) ;
			player.drawCards(playHand, game.getDeck());
			gameMaster.checkAndSetBust(game) ;
			
			if(playHand.checkBust()) {
				//playHandがbustのとき、handsがすべてbustならディーラーのターンは非発生で終了
				//全ての手札がバストならtrueとなるboolean resultを用意
				boolean result = true ;
				for(Hand hand: hands) {
					result &= hand.checkBust() ;
				}
				//全ての手札のhandphaseがoverならtrueを返すboolean handPhaseEndを用意
				boolean handPhaseEnd = true ;
				for(Hand hand: hands) {
					handPhaseEnd &= (hand.getHandPhase() == HandPhase.OVER) ;
				}
				if(result == true) {
					//手札が全てバストのときゲームチェック
					gameMaster.checkAndSetBust(game);
					gameMaster.checkGameOver(game);				
				}else if(handPhaseEnd){
					//バストしていない手札があって全てのhandPhhaseがoverのときディーラーのターンが発生してゲームチェック
					gameMaster.makeDealerDrawCards(game) ;
					gameMaster.checkAndSetBust(game);
					gameMaster.checkGameOver(game);
				}else {
					//バストしていない手札があってhandPhase:overでない手札があるとき次の手札のフェーズをplayにして返す
					System.out.println("次のプレイヤーの手札の番に移ります");
					System.out.println("hand0のゲームフェーズ" + hands.get(0).getHandPhase());
					System.out.println("hand1のゲームフェーズ" + hands.get(1).getHandPhase());
					System.out.println("hand1のbust" + hands.get(1).isBust());
					if(hands.get(playHandIndex+1) != null) {
						hands.get(playHandIndex+1).setHandPhase(HandPhase.PLAY);
						message = "hitしますか?standしますか?" ;
					}else {
						System.err.println("手札の数が適切でありません");
					}
				}
				//ゲーム終了フェーズを確認して獲得チップ取得
				if(game.getGamePhase() == Game.GamePhase.GAME_OVER) {
					gameMaster.calculateChips(game) ;
					int gettingChips = player.getGettingChips() ;
					request.setAttribute("gettingChips", gettingChips);
					message = "ゲーム終了です" ;
				}
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
		int gettingChips = 0 ;
		
		if(gameMaster != null) {
			Game game = gameMaster.getGame() ;
			//playerは一人と想定
			//playerのhandsを順に確認していってはじめてOVERでなかったhandを取り出してoverにする
			List<Hand> hands = game.getPlayers().get(0).getHands() ;
			int playHandIndex = -1 ;
			for(int i=0; i<hands.size(); i++) {
				if(hands.get(i).getHandPhase() != HandPhase.OVER) {
					hands.get(i).setHandPhase(HandPhase.OVER);
					playHandIndex = i ;
					break;
				}			
			}
			//全ての手札がバストならtrueとなるboolean resultを用意
			boolean result = true ;
			for(Hand hand: game.getPlayers().get(0).getHands()) {
				result &= hand.checkBust() ;
			}
			//全ての手札のHandPhaseがoverだったらtrueとなるboolean phaseresultを用意
			boolean phaseResult = true ;
			for(Hand hand: game.getPlayers().get(0).getHands()) {
				phaseResult &= (hand.getHandPhase() == HandPhase.OVER) ;
			}
			if(result) {
				//playerのhandsが全てバストであればディーラーのターンは行われずゲーム終了
				gameMaster.checkGameOver(game) ;
				gameMaster.calculateChips(game) ;
				gettingChips = game.getPlayers().get(0).getGettingChips() ;
			}else if(phaseResult){
				//playerの手札がすべてhanPhase overだったらディーラーのターンが発生してゲーム終了
				gameMaster.makeDealerDrawCards(game) ;
				gameMaster.checkAndSetBust(game);
				gameMaster.checkGameOver(game) ;
				gameMaster.calculateChips(game) ;
				gettingChips = game.getPlayers().get(0).getGettingChips() ;
			}else {
				//それ以外の時は次のhandのターンに
				hands.get(playHandIndex+1).setHandPhase(HandPhase.PLAY);
				message = "hitしますか？standしますか？" ;
				request.setAttribute("gameMaster", gameMaster);
				request.setAttribute("message", message);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher(nextPage);
				requestDispatcher.forward(request, response);
			}
			
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
