package model.player;

import java.util.ArrayList;
import java.util.List;

import model.card.Card;
import model.card.Deck;

public abstract class BasePlayer {
	
	private List<Card> hand ;
	private boolean isBust ;
	private int sumOfHand ;

	//deckからカードを引く(abstract)
	public abstract void drawCards(Deck deck) ;
	
	//途中
	//手札の合計を計算
	public int calculateSumOfHand() {
		int sumOfHand = 0 ;
		
		//handのカードナンバーをリストとして取り出す
		List<Card> hand = this.hand ;
		List<Integer> handsCardNumbers = new ArrayList<>() ;
		for(int i=0; i<hand.size(); i++) {
			handsCardNumbers.add(hand.get(i).getNumber()) ;
		}
		
		//handのカードナンバーリストの1を11に変換
		
		//handのカードナンバーリストにAが含まれるか含まれないかで計算方法分岐
		//Aが含まれるときは11として扱い、その扱いでバストする場合は1賭して計算
		if(!handsCardNumbers.contains(1)) {
			
			for(int i=0; i<this.hand.size(); i++) {
				int number = this.hand.get(i).getNumber() ;
				if(number < 10) {
					sumOfHand += this.hand.get(i).getNumber() ;
				}else {
					sumOfHand += 10 ;
				}
			}
		}else {
			
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
