package model.player;

import java.util.List;

import model.card.Card;
import model.card.Deck;
import model.card.Hand;
import user.User;

public class Player extends BasePlayer {
	
	private User user ;
	private boolean isSplit ;
	private int gettingChips ;

	//コンストラクタでloginUserを受け取って初期化
	public Player(User loginUser) {
		user = loginUser ;
	}
	
//	//コピーコンストラクタ
//	//浅いコピー(newされたPlayerオブジェクトはplayerのuserとplayResultと同じ参照先を参照)
//	public Player(Player player) {
//		super(player) ;
//		this.user = player.user ;
//		this.playerResult = player.playerResult ;
//		this.chipsForGame = player.chipsForGame ;
//		this.isSplit = player.isSplit ;
//	}
	
	//山札の上からカードを一枚引く
	@Override
	public void drawCards(Hand hand, Deck deck){
		
		Card drawnCard = deck.drawCard() ;
		hand.getList().add(drawnCard) ;
		
	}
	
	public boolean checkSplit() {
		List<Card> hand0 = this.getHands().get(0).getList() ;
		boolean split = false ;
		if(hand0.size()==2 && hand0.get(0)==hand0.get(1)) {
			split = true ;
		}
		return split ;
	}
	
	//getter,setter
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isSplit() {
		return isSplit;
	}

	public void setSplit(boolean isSplit) {
		this.isSplit = isSplit;
	}

	public int getGettingChips() {
		return gettingChips;
	}

	public void setGettingChips(int gettingChips) {
		this.gettingChips = gettingChips;
	}
	
		
}
