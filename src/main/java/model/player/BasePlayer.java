package model.player;

import java.util.List;

import model.card.Card;
import model.card.Deck;

public abstract class BasePlayer {
	
	private List<Card> hand ;
	private boolean isBust ;
	private int sumOfHand ;

	//deckからカードを引く(abstract)
	public abstract void drawCards(Deck deck) ;
	
	//手札の合計を計算
	public int calculateSumOfHand() {
		int sumOfHand = 0 ;
		for(int i=0; i<this.hand.size(); i++) {
			int number = this.hand.get(i).getNumber() ;
			if(number < 10) {
				sumOfHand += this.hand.get(i).getNumber() ;
			}else {
				sumOfHand += 10 ;
			}
		}
		return sumOfHand ;
	}
	
	//getter,setter
	public List<Card> getHand() {
		return hand;
	}

	public void setHand(List<Card> hand) {
		this.hand = hand;
	}

	public boolean isBust() {
		return isBust;
	}

	public void setBust(boolean isBust) {
		this.isBust = isBust;
	}
	
	public int getSumOfHand() {
		return sumOfHand;
	}

	public void setSumOfHand(int sumOfHand) {
		this.sumOfHand = sumOfHand;
	}

}
