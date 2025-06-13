package model.player;

import model.card.Card;
import model.card.Deck;
import user.User;

public class Player extends BasePlayer {
	
	public enum PlayerResult{
		WIN,
		DRAW,
		LOSE,
		UNDEFINED 
	}
	
	private User user ;
	private PlayerResult playerResult ;
	private int chipsForGame ;
	private boolean isSplit ;

	//コンストラクタでloginUserを受け取って初期化
	public Player(User loginUser) {
		this.user = loginUser ;
		this.playerResult = PlayerResult.UNDEFINED;
	}
	
	//コピーコンストラクタ
	//浅いコピー(newされたPlayerオブジェクトはplayerのuserとplayResultと同じ参照先を参照)
	public Player(Player player) {
		super(player) ;
		this.user = player.user ;
		this.playerResult = player.playerResult ;
		this.chipsForGame = player.chipsForGame ;
		this.isSplit = player.isSplit ;
	}
	
	//カードを一枚引く
	@Override
	public void drawCards(Deck deck){
		
		Card drawnCard = deck.drawCard() ;
		this.getHand().add(drawnCard) ;
		
	}
	
	//getter,setter
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public PlayerResult getPlayerResult() {
		return playerResult;
	}

	public void setPlayerResult(PlayerResult playerResult) {
		this.playerResult = playerResult;
	}
	
	public int getChipsForGame() {
		return chipsForGame;
	}

	public void setChipsForGame(int chipsForGame) {
		this.chipsForGame = chipsForGame;
	}

	public boolean isSplit() {
		return isSplit;
	}
	
	public boolean getIsSplit() {
		return isSplit;
	}


	public void setSplit(boolean isSplit) {
		this.isSplit = isSplit;
	}
		
}
