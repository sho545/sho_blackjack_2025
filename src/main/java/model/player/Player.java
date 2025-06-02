package model.player;

import model.card.Card;
import model.card.Deck;
import user.User;

public class Player extends BasePlayer {
	
	private User user ;
	private boolean isWin ;
	private int hitCount ;
	
	//コンストラクタでloginUserを受け取って初期化
	public Player(User loginUser) {
		this.user = loginUser ;
		this.hitCount = 0 ;
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

	public boolean getIsWin() {
		return isWin;
	}

	public void setIsWin(boolean isWin) {
		this.isWin = isWin;
	}

	public int getHitCount() {
		return hitCount;
	}

	public void setHitCount(int hitCount) {
		this.hitCount = hitCount;
	}
	
	
	
}
