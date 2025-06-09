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
		
		//handのカードナンバーリストにAが含まれるか含まれないかで計算方法分岐
		//Aが含まれるときは1つのAを11として扱い、その扱いでバストする場合は1として計算
		if(!handsCardNumbers.contains(1)) {
			
			//Aが含まれないときの通常計算
			for(int i=0; i<handsCardNumbers.size(); i++) {
				if(handsCardNumbers.get(i) < 10) {
					sumOfHand += handsCardNumbers.get(i) ;
				}else {
					sumOfHand += 10 ;
				}
			}
		}else {
			//Aが含まれるときの計算
			//handの1つのAを110(計算では11として扱う)に置換
			List<Integer> handToCalculate = new ArrayList<>(handsCardNumbers) ;
			handToCalculate.set(handsCardNumbers.indexOf(1),110 ) ;
			
			for(int i=0; i<handToCalculate.size(); i++) {
				if(handToCalculate.get(i) < 10) {
					sumOfHand += handToCalculate.get(i) ;
				}else if(handToCalculate.get(i) <= 13){
					sumOfHand += 10 ;
				}else {
					sumOfHand += 11 ;}
				}
			//1を11として計算してbustした場合、Aを1として計算しなおす
			if(sumOfHand > 21) {
				sumOfHand = 0 ;
				for(int i=0; i<handsCardNumbers.size(); i++) {
					if(handsCardNumbers.get(i) < 10) {
						sumOfHand += handsCardNumbers.get(i) ;
					}else {
						sumOfHand += 10 ;
					}
				}
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
