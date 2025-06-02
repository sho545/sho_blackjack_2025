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
			default : 
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効なaction値です: " + action);
	            break;
		}		
	}
	
	//hitを行い、
	public void hit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		HttpSession session = request.getSession(false) ;
		GameMaster gameMaster = (GameMaster) session.getAttribute("gameMaster") ;
		String nextPage = "blackjack.jsp" ;
		String message = null ;
		
		if(gameMaster != null) {
			
			Game game = gameMaster.getGame() ;
			gameMaster.playerHits(game);
			gameMaster.checkAndSetBust(game) ;
			boolean result = gameMaster.isGameOver(game) ;
			request.setAttribute("gameMaster", gameMaster);
			
			if(result == false) {
				message = "hitしますか?standしますか?" ;
			}else {
				message = "ゲーム終了です" ;
			}
			request.setAttribute("message", message);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(nextPage);
			requestDispatcher.forward(request, response);
			
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


}
