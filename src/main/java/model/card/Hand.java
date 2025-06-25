package model.card;

import java.util.ArrayList;
import java.util.List;

public class Hand {
	
	public enum HandResult{
		WIN,
		DRAW,
		LOSE,
		UNDEFINED 
	}
	
	public enum HandPhase{
		NOT_STARTED,
		PLAY,
		OVER
	}
	
	private List<Card> hand ;
	private boolean isBust ;
	private int sumOfHand ;
	private HandResult handResult ;
	private HandPhase handPhase ;
	private int chipsForGame ;
	
	//コンストラクタでhandResultとhandPhaseを初期化
	public Hand() {
		hand = new ArrayList<>() ;
		handResult = HandResult.UNDEFINED ;
		handPhase = HandPhase.NOT_STARTED ;
	}
	//コピーコンストラクタ(split用の手札を用意するときに使用)
	public Hand(Hand hand) {
		//基本データ型は単に参照すればok
		this.isBust = hand.isBust ;
		this.sumOfHand = hand.sumOfHand;
        this.chipsForGame = hand.chipsForGame;
        //enumはimmutableより単に参照すればok
        this.handResult = hand.handResult ;
        this.handPhase = hand.handPhase ;
        //handはディープコピー
        this.hand = new ArrayList<>() ;
        for(int i=0; i<hand.getList().size(); i++) {
        		this.hand.add(new Card(hand.getList().get(i))) ;
        }
	}
	
	public int calculateSumOfHand() {
	
		int sumOfHand = 0 ;
		
		//handのカードナンバーをリストとして取り出す
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

	public boolean checkBust(){
		boolean bust = false ;
		if(sumOfHand > 21) {
			bust = true ;
		}
		return bust ;
	}
	
	//getter,setter
	public List<Card> getList() {
		return hand;
	}

	public void setHand(List<Card> hand) {
		this.hand = hand;
	}

	public void setBust(boolean isBust) {
		this.isBust = isBust;
	}
	public boolean isBust() {
		return isBust ;
	}

	public void setSumOfHand(int sumOfHand) {
		this.sumOfHand = sumOfHand;
	}
	
	public int getSumOfHand() {
		return sumOfHand ;
	}

	public HandResult getHandResult() {
		return handResult;
	}

	public void setHandResult(HandResult handResult) {
		this.handResult = handResult;
	}

	public HandPhase getHandPhase() {
		return handPhase;
	}

	public void setHandPhase(HandPhase handPhase) {
		this.handPhase = handPhase;
	}

	public int getChipsForGame() {
		return chipsForGame;
	}

	public void setChipsForGame(int chipsForGame) {
		this.chipsForGame = chipsForGame;
	}
	

}
