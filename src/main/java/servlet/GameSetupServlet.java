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
import model.Game;
import model.GameMaster;
import model.card.Hand;
import user.User;

/**
 * Servlet implementation class GameSetup
 */
@WebServlet("/gameSetup/*")

//user,playerが一人のときを想定する
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
						
					case "/chips" :
						chips(request,response) ;
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
	
	//ゲームを始める(user.jsp)から掛け金選択へ(blackjack.jsp)
	public void start(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(false) ;
		User loginUser = (User)session.getAttribute("loginUser") ;
		
		String nextPage = "/blackjack.jsp" ;
		
		if(loginUser != null) {
			//取得するユーザーは一人と想定
			List<User> users = new ArrayList<>() ;
			users.add(loginUser) ;
			Game game = new Game(users) ;
			GameMaster gameMaster = new GameMaster(game) ;
			gameMaster.setGame(game);
			
			session.setAttribute("gameMaster", gameMaster);
			request.setAttribute("message", "チップは何枚賭けますか?");
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(nextPage);
			requestDispatcher.forward(request, response);
			
		}else {
			System.err.println("sessionからloginUserを取得できませんでした");
		}
	}
	
	//掛け金選択(blackjack.jsp)から最初の手札配布(blackjack.jsp)
	public void chips(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(false) ;
		GameMaster gameMaster = (GameMaster)session.getAttribute("gameMaster") ;
		int chipsForGame = Integer.parseInt(request.getParameter("chipsForGame")) ;
		
		String nextPage = "/blackjack.jsp" ;
		String message = null ;
		
		if(gameMaster != null) {
			Game game = gameMaster.getGame() ;
			//今回はプレイヤーが一人として処理を行う
			//ユーザーの所持チップを取得
			int chips = game.getPlayers().get(0).getUser().getChips();
			
			if(chipsForGame <= chips && chipsForGame >= 0) {
				//初期ディールを行ってスプリットチェック
				gameMaster.initialDeal(game) ;
				gameMaster.checkAndSetSplit(game) ;
				//playerのhand0に賭けチップをセット
				game.getPlayers().get(0).getHands().get(0).setChipsForGame(chipsForGame);
				
				if(!game.getPlayers().get(0).isSplit()) {
					//splitでなかったとき
					message = "hitしますか?standしますか?" ;
					game.setGamePhase(Game.GamePhase.PLAYER_TURN);
				} else {
					//splitだったとき
					message = "splitしますか?" ;
				}
			}else {
				message = "チップをを選びなおしてください";
			}
			
			session.setAttribute("gameMaster", gameMaster);
			request.setAttribute("message", message);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(nextPage);
			requestDispatcher.forward(request, response);
			
		}else {
			System.err.println("sessionからloginUserを取得できませんでした");
		}
	}
	
	//ゲームを終了して戦績/chipをデータベースに書き込んでユーザー画面へ
	public void over(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(false) ;
		User loginUser = (User)session.getAttribute("loginUser") ;
		GameMaster gameMaster = (GameMaster) session.getAttribute("gameMaster") ;
		
		if(gameMaster != null) {
			if(loginUser != null) {
				Game game = gameMaster.getGame() ;
				//プレイヤーが一人の時の処理
				//プレイヤーがbetしたチップと獲得したチップを取得
				int bettingChips = 0 ;
				List<Hand> hands = game.getPlayers().get(0).getHands() ;
				for(Hand hand : hands) {
					bettingChips += hand.getChipsForGame() ;
				}
				int gettingChips = game.getPlayers().get(0).getGettingChips() ;
				//チップの+-をloginUserに反映
				int remainChips = loginUser.getChips() - bettingChips + gettingChips ;
				loginUser.setChips(remainChips);
				UserDao userDao = new UserDao() ;
				try {
					userDao.updateChips(loginUser) ;
				}catch(SQLException e) {
					System.err.println("データベースアクセス中にエラーが発生しました: " + e.getMessage());
			        e.printStackTrace();
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
