package model.card;

public class Card {
	
	//markは(1,ハート),(2,ダイア),(3,スペード),(4,クローバー)で対応
	private final int mark ;
	private final int number ;
	
	//コンストラクタ
	public Card(int mark, int number) {
		if(mark >= 1 && mark <= 4 && number >= 1 && number <= 13) {
			this.mark = mark ;
			this.number = number ;
		}else {
			throw new IllegalArgumentException("0<mark<5かつ0<number<14でなければいけません") ;
		}
	}
	
	
	//getter
	public int getMark() {
		return mark;
	}
	
	public int getNumber() {
		return number;
	}
	
}
