package model.player;

import model.card.Card;
import model.card.Deck;

public class Dealer extends BasePlayer {
	
	//カードを17を超えるまで引く
	@Override
	public void drawCards(Deck deck){
		
		int sumOfHand = this.getSumOfHand() ;
		
		while(sumOfHand < 17) {
			Card drawnCard = deck.drawCard() ;
			this.getHand().add(drawnCard) ;
			sumOfHand += drawnCard.getNumber() ;
		}
	}

}
