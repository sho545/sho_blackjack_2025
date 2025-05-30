package model.player;

import java.util.List;

import model.card.Card;
import model.card.Deck;

public abstract class BasePlayer {
	
	private List<Card> hand ;
	private boolean isBurst ;
	private int sumOfHand ;

	//deckからカードを引く(abstract)
	public abstract void drawCards(Deck deck) ;
	
	//getter,setter
	public List<Card> getHand() {
		return hand;
	}

	public void setHand(List<Card> hand) {
		this.hand = hand;
	}

	public boolean isBurst() {
		return isBurst;
	}

	public void setBurst(boolean isBurst) {
		this.isBurst = isBurst;
	}
	
	public int getSumOfHand() {
		return sumOfHand;
	}

	public void setSumOfHand(int sumOfHand) {
		this.sumOfHand = sumOfHand;
	}

}
