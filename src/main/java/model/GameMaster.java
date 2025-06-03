package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.card.Card;
import model.card.Deck;
import model.player.BasePlayer;
import model.player.Dealer;
import model.player.Player;

public class GameMaster {
	
	private Game game ;
	
	//コンストラクタで与えられたgameでdeckをシャッフルし、initialカードを2枚ずつ配って
	//ゲームの状態をINITIAL_DEALに
	public GameMaster(Game game) {
		
		Deck deck = game.getDeck() ;
		List<Card> remainingCards = deck.getList() ;
		
		
		//カードシャッフル
		Collections.shuffle(remainingCards) ;
		
		Player player = game.getPlayer() ;
		Dealer dealer = game.getDealer() ;
		
		List<Card> playersInitialCards = new ArrayList<>() ;
		List<Card> dealersInitialCards = new ArrayList<>() ;
		player.setHand(playersInitialCards);
		dealer.setHand(dealersInitialCards);
		
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
	
	//バーストチェックを行って、バーストの結果をgameにセット
	public  void checkAndSetBust(Game game) {
		BasePlayer player = game.getPlayer() ;
		BasePlayer dealer = game.getDealer() ;
		
		int playersSumOfHand = player.calculateSumOfHand() ;
		int dealersSumOfHand = dealer.calculateSumOfHand() ;
		
		player.setSumOfHand(playersSumOfHand);
		dealer.setSumOfHand(dealersSumOfHand);
		
		if(playersSumOfHand > 21) {
			game.setGamePhase(Game.GamePhase.GAME_OVER);
			player.setBust(true) ;
		}else if(dealersSumOfHand > 21) {
			game.setGamePhase(Game.GamePhase.GAME_OVER);
			dealer.setBust(true); 
		}
	}
	
	//playerにhitさせる
	public void playerHits(Game game) {
		game.setGamePhase(Game.GamePhase.PLAYER_TURN);
		BasePlayer player = game.getPlayer() ;
		player.drawCards(game.getDeck());
	}
	
	//dealerにカードを引かせる
	public void makeDealerDrawCards(Game game) {
		game.setGamePhase(Game.GamePhase.DEALER_TURN);
		BasePlayer dealer = game.getDealer() ;
		int dealersSumOfHand = dealer.calculateSumOfHand() ;
		dealer.setSumOfHand(dealersSumOfHand);
		dealer.drawCards(game.getDeck());
	}
	
	//勝敗チェックして終了フェーズをセット
	public void checkGameOver(Game game) {
		Player player = game.getPlayer() ;
		Dealer dealer = game.getDealer() ;
		if(player.isBust() == true) {
			player.setIsWin(false);
			game.setGamePhase(Game.GamePhase.GAME_OVER);
		}else if(dealer.isBust() == true) {
			player.setIsWin(true);
			game.setGamePhase(Game.GamePhase.GAME_OVER);
		}else {
			player.setSumOfHand(player.calculateSumOfHand());
			dealer.setSumOfHand(dealer.calculateSumOfHand());
			int playersSumOfHand = player.getSumOfHand() ;
			int dealersSumOfHand = dealer.getSumOfHand() ;
			
			if(playersSumOfHand > dealersSumOfHand) {
				player.setIsWin(true);
				game.setGamePhase(Game.GamePhase.GAME_OVER);
			}else if (playersSumOfHand < dealersSumOfHand) {
				player.setIsWin(false);
				game.setGamePhase(Game.GamePhase.GAME_OVER);
			}else {
				//引き分けの時は一旦負けとしておく
				player.setIsWin(false);
				game.setGamePhase(Game.GamePhase.GAME_OVER);
			}
		}
		
	}
	
	//setter,getter
	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}
	
	

}
