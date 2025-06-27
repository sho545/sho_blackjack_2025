package model.player;

import java.util.ArrayList;
import java.util.List;

import model.card.Deck;
import model.card.Hand;

public abstract class BasePlayer {
	
	//handsのindex0が通常用の手札でindex1がsplit用の手札
	private List<Hand> hands ;
//	private List<Boolean> Busts ;
//	private List<Integer> sumsOfHands ;
	
	//コンストラクタ
	public BasePlayer() {
        hands = new ArrayList<>();
        //手札0と手札1をあらかじめセット
        	hands.add(new Hand()) ;
    }
//	
//	// コピー用のコンストラクタ
//    public BasePlayer(BasePlayer other) {
//        // 手札は、中身をコピーした新しいリストを作成（ディープコピー）
//        this.hand = new ArrayList<>(other.hand);
//        this.isBust = other.isBust;
//        this.sumOfHand = other.sumOfHand;
//    }

	//deckからカードを引く(abstract)
	public abstract void drawCards(Hand hand, Deck deck) ;
	
	
//	//それぞれの手札の合計を計算
//	public List<Integer> calculateSumsOfHands() {
//		
//		List<Integer> sumsOfHands = new ArrayList<>() ;
//		for(int i=0; i < hands.size(); i++) {
//			int sumOfHand = 0 ;
//			
//			//handのカードナンバーをリストとして取り出す
//			List<Card> handCards = hands.get(i).getHand() ;
//			List<Integer> handCardNumbers = new ArrayList<>() ;
//			for(int j=0; j<handCards.size(); j++) {
//				handCardNumbers.add(handCards.get(j).getNumber()) ;
//			}
//			
//			//handのカードナンバーリストにAが含まれるか含まれないかで計算方法分岐
//			//Aが含まれるときは1つのAを11として扱い、その扱いでバストする場合は1として計算
//			if(!handCardNumbers.contains(1)) {
//				
//				//Aが含まれないときの通常計算
//				for(int j=0; i<handCardNumbers.size(); j++) {
//					if(handCardNumbers.get(j) < 10) {
//						sumOfHand += handCardNumbers.get(j) ;
//					}else {
//						sumOfHand += 10 ;
//					}
//				}
//			}else {
//				//Aが含まれるときの計算
//				//handの1つのAを110(計算では11として扱う)に置換
//				List<Integer> handToCalculate = new ArrayList<>(handCardNumbers) ;
//				handToCalculate.set(handCardNumbers.indexOf(1),110 ) ;
//				
//				for(int j=0; j<handToCalculate.size(); j++) {
//					if(handToCalculate.get(j) < 10) {
//						sumOfHand += handToCalculate.get(j) ;
//					}else if(handToCalculate.get(j) <= 13){
//						sumOfHand += 10 ;
//					}else {
//						sumOfHand += 11 ;}
//					}
//				//1を11として計算してbustした場合、Aを1として計算しなおす
//				if(sumOfHand > 21) {
//					sumOfHand = 0 ;
//					for(int j=0; j<handCardNumbers.size(); j++) {
//						if(handCardNumbers.get(j) < 10) {
//							sumOfHand += handCardNumbers.get(j) ;
//						}else {
//							sumOfHand += 10 ;
//						}
//					}
//				}
//			}
//			 sumsOfHands.add(sumOfHand) ;
//		}
//		return sumsOfHands ;
//	}
	
//	public List<Boolean> checkBusts(){
//		List<Boolean> busts = new ArrayList<>() ;
//		for(int i=0; i<hands.size(); i++) {
//			if(sumsOfHands.get(i) <= 21) {
//				busts.add(false) ;
//			}else {
//				busts.add(true) ;
//			}
//		}
//		return busts ;
//	}
	
	public List<Boolean> checkBusts(){
		List<Boolean> busts = new ArrayList<>() ;
		for(int i=0; i<hands.size(); i++) {
			busts.add(hands.get(i).checkBust()) ;
		}
		return busts ;
	}
	
	public List<Integer> calculateSumsOfHands(){
		List<Integer> sumsOfHands = new ArrayList<>() ;
		for(int i=0; i<hands.size(); i++) {
			sumsOfHands.add(hands.get(i).calculateSumOfHand()) ;
		}
		return sumsOfHands ;
	}

	//getter,setter
	public List<Hand> getHands() {
		return hands;
	}


	public void setHands(List<Hand> hands) {
		this.hands = hands;
	}


//	public List<Boolean> getBusts() {
//		return Busts;
//	}
//
//
//	public void setBusts(List<Boolean> busts) {
//		Busts = busts;
//	}
//
//
//	public List<Integer> getSumsOfHands() {
//		return sumsOfHands;
//	}
//
//
//	public void setSumsOfHands(List<Integer> sumsOfHands) {
//		this.sumsOfHands = sumsOfHands;
//	}
	
}
