package model;

import java.util.Collections;
import java.util.List;

import model.card.Card;
import model.card.Deck;
import model.player.Dealer;
import model.player.Player;

public class GameMaster {
	
	//コンストラクタで与えられたgameでdeckをシャッフルし、initialカードを2枚ずつ配って
	//ゲームの状態をINITIAL_DEALに
	public GameMaster(Game game) {
		
		Deck deck = game.getDeck() ;
		List<Card> remainingCards = deck.getList() ;
		
		
		//カードシャッフル
		Collections.shuffle(remainingCards) ;
		
		Player player = game.getPlayer() ;
		Dealer dealer = game.getDealer() ;
		
		//カードを2枚ずつ配る
		for(int i=0; i<2; i++) {
			player.getHand().add(deck.drawCard()) ;
		}
		for(int i=0; i<2; i++) {
			dealer.getHand().add(deck.drawCard()) ;
		}
		//状態をinitial dealに
		game.setGamePhase(Game.GamePhase.INITIAL_DEAL) ;
	}
	
	//手札の合計を計算
	//バーストチェック
	//
}
