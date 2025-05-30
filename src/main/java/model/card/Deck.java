package model.card;

import java.util.ArrayList;
import java.util.List;

public class Deck {
	
	private List<Card> deck ;
	
	//コンストラクタでトランプの山札を初期化
	public Deck() {
		this.deck = new ArrayList<>() ;
		for(int mark = 1; mark <= 4; mark++) {
			for(int number = 1; number <=13; number++ ) {
				Card card = new Card(mark, number) ;
				this.deck.add(card) ;
			}
		}
	}
	
	//山札から一番目を取り出す
	public Card drawCard() {
		Card drawnCard = this.deck.remove(0) ;
		return drawnCard ;
	}
	
	//getter
	public List<Card> getList() {
		return deck;
	}

	
	

}
