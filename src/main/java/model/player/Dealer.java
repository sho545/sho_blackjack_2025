package model.player;

import model.card.Card;
import model.card.Deck;
import model.card.Hand;

public class Dealer extends BasePlayer {
	
	//カードを17を超えるまで引く
	@Override
	public void drawCards(Hand hand, Deck deck){
		
		int sumOfHand = hand.calculateSumOfHand() ;
		
		while(sumOfHand < 17) {
			Card drawnCard = deck.drawCard() ;
			hand.getList().add(drawnCard) ;
			sumOfHand += drawnCard.getNumber() ;
		}
	}

}
