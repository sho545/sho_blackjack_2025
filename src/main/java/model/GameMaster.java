package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.card.Card;
import model.card.Deck;
import model.player.BasePlayer;
import model.player.Dealer;
import model.player.Player;
import model.player.Player.PlayerResult;

//このクラスはディーラーの役割のメソッドを提供するクラスです
public class GameMaster {
	
	private Game game ;
	
	//コンストラクタで与えられたgameでdeckをシャッフルしてフェイズをnot_startedに
	public GameMaster(Game game) {
		game.setGamePhase(Game.GamePhase.NOT_STARTED);
		Deck deck = game.getDeck() ;
		List<Card> remainingCards = deck.getList() ;
		//カードシャッフル
		Collections.shuffle(remainingCards) ;
	}
	
	//手札を2枚ずつ配る
	public void initialDeal(Game game) {
		System.out.println("initialDealメソッドが呼び出されました");
		game.setGamePhase(Game.GamePhase.INITIAL_DEAL) ;
		
		Deck deck = game.getDeck() ;
		Player player = game.getPlayer() ;
		Dealer dealer = game.getDealer() ;
		
		List<Card> playersInitialCards = new ArrayList<>() ;
		List<Card> dealersInitialCards = new ArrayList<>() ;
		
		//カードを2枚ずつ配る		
//		for(int i=0; i<2; i++) {
//			player.getHand().add(deck.drawCard()) ;
//		}
		Card sp4 = new Card(3,4) ;
		Card hart4 = new Card(1,4) ;
		
		player.getHand().add(sp4);
		player.getHand().add(hart4) ;
		
		for(int i=0; i<2; i++) {
			dealer.getHand().add(deck.drawCard()) ;
		}
		player.setHand(playersInitialCards);
		dealer.setHand(dealersInitialCards);
	}
	
	//バーストチェックを行って、バーストの結果をgameにセット
	public  void checkAndSetBust(Game game) {
		Player player = game.getPlayer() ;
		BasePlayer dealer = game.getDealer() ;
		
		int playersSumOfHand = player.calculateSumOfHand() ;
		int dealersSumOfHand = dealer.calculateSumOfHand() ;
		
		player.setSumOfHand(playersSumOfHand);
		dealer.setSumOfHand(dealersSumOfHand);
		
		if(playersSumOfHand > 21) {
			player.setBust(true) ;
			player.setPlayerResult(Player.PlayerResult.LOSE);
			game.setGamePhase(Game.GamePhase.GAME_OVER);
		}else if(dealersSumOfHand > 21) {
			dealer.setBust(true); 
			game.setGamePhase(Game.GamePhase.GAME_OVER);
		}
		//splitPlayerがいればsplitPlayerのバーストも判定
		if(game.getSplitPlayer() != null) {
			int splitPlayersSumOfHand = game.getSplitPlayer().calculateSumOfHand() ;
			if(splitPlayersSumOfHand > 21) {
				game.getSplitPlayer().setBust(true);
				game.setGamePhase(Game.GamePhase.PLAYER_TURN) ;
			}
		}
	}
	
	//splitチェックを行ってsplitならplayerにisSplit=trueをセット
	public void setIsSplit(Game game) {
		List<Card> playersHand = game.getPlayer().getHand() ;
		if(playersHand.size() == 2 && playersHand.get(0).getNumber() == playersHand.get(1).getNumber()) {
			//プレイヤーの手札が二枚でかつ同じ数字のとき
			game.getPlayer().setSplit(true) ;			
		} else {
			game.getPlayer().setSplit(false);
		}
	}
	
	//split用のプレイヤーを準備して手札をスプリット、カードを一枚ずつ配ってGameのsplitPlayerに格納
	//ゲームフェーズをsplitPlayerターンへ
	public void split(Game game) {
		//playerの情報をコピーしてnew。フィールドとして同じアドレスを参照するのはuserとhandとplayerResult
		//enumクラスはimmutableなので注意
		Player player = game.getPlayer() ;
		Player splitPlayer = new Player(player) ;
		//splitPlayerのカードをセット
		splitPlayer.getHand().remove(1) ;
		splitPlayer.drawCards(game.getDeck());
		//playerのカードをセット
		player.getHand().remove(0) ;
		player.drawCards(game.getDeck());
		//fameに新しいplayerとsplitPlayerをセット
		game.setSplitPlayer(splitPlayer);
		game.setPlayer(player);
		//ゲームフェーズをsplitPlayerのターンにセット
		game.setGamePhase(Game.GamePhase.SPLIT_PLAYER_TURN);
	}
	
	//playerにhitさせる
	public void playerHits(Game game) {
		BasePlayer player = game.getPlayer() ;
		player.drawCards(game.getDeck());
	}
	//splitPlayerにhitさせる
	public void splitPlayerHits(Game game) {
		BasePlayer splitPlayer = game.getSplitPlayer() ;
		splitPlayer.drawCards(game.getDeck());
	}
	
	
	//プレイヤーが初期カードで21になっていたときを除いてdealerにカードを引かせる
	public void makeDealerDrawCards(Game game) {
		game.setGamePhase(Game.GamePhase.DEALER_TURN);
		//playerの手札を計算してセット
		game.getPlayer().setSumOfHand(game.getPlayer().calculateSumOfHand());
		if(game.getPlayer().getSumOfHand() == 21 && game.getPlayer().getHand().size() == 2) {
			//初期カードでプレイヤーが21になっていたとき何もしない
		} else {
			BasePlayer dealer = game.getDealer() ;
			int dealersSumOfHand = dealer.calculateSumOfHand() ;
			dealer.setSumOfHand(dealersSumOfHand);
			dealer.drawCards(game.getDeck());
		}
	}
	
	//勝敗チェックして終了フェーズをセット
	public void checkGameOver(Game game) {
		
		Player player = game.getPlayer() ;
		Dealer dealer = game.getDealer() ;
		
		//splitPlayerがいるときはsplitPlayerがバストのときLOSEをセット
		if(game.getSplitPlayer() != null) {
			Player splitPlayer = game.getSplitPlayer() ;
			if(splitPlayer.isBust() == true) {
				splitPlayer.setPlayerResult(PlayerResult.LOSE);
			}
		}
		
		if(player.isBust() == true) {
			player.setPlayerResult(PlayerResult.LOSE) ;
			game.setGamePhase(Game.GamePhase.GAME_OVER);
		}else if(dealer.isBust() == true) {
			player.setPlayerResult(PlayerResult.WIN) ;
			game.setGamePhase(Game.GamePhase.GAME_OVER);
		}else {
			player.setSumOfHand(player.calculateSumOfHand());
			dealer.setSumOfHand(dealer.calculateSumOfHand());
			int playersSumOfHand = player.getSumOfHand() ;
			int dealersSumOfHand = dealer.getSumOfHand() ;
			
			//splitPlayerがいるときはsplitPlayerの結果をセット
			if(game.getSplitPlayer() != null && game.getSplitPlayer().isBust() == false) {
				Player splitPlayer = game.getSplitPlayer() ;
				splitPlayer.setSumOfHand(splitPlayer.calculateSumOfHand());
				int splitPlayersSumOfHand = splitPlayer.getSumOfHand() ;
				if(splitPlayersSumOfHand > dealersSumOfHand) {
					splitPlayer.setPlayerResult(PlayerResult.WIN);
				} else if(splitPlayersSumOfHand < dealersSumOfHand) {
					splitPlayer.setPlayerResult(PlayerResult.LOSE);
				} else {
					splitPlayer.setPlayerResult(PlayerResult.DRAW);
				}
			}
			
			if(playersSumOfHand > dealersSumOfHand) {
				player.setPlayerResult(PlayerResult.WIN) ;
				game.setGamePhase(Game.GamePhase.GAME_OVER);
			}else if (playersSumOfHand < dealersSumOfHand) {
				player.setPlayerResult(PlayerResult.LOSE) ;
				game.setGamePhase(Game.GamePhase.GAME_OVER);
			}else {
				
				player.setPlayerResult(PlayerResult.DRAW) ;
				game.setGamePhase(Game.GamePhase.GAME_OVER);
			}
		}
		
	}
	
	//もらったgameのチップの増減を計算してユーザー情報のchipを書き換えして獲得したチップを返す
	//checkGameOverの後に呼び出す
	public int calculateChips(Game game) {
		//残りのチップを計算
		int chipsForGame = game.getPlayer().getChipsForGame() ;
		int remainChips = game.getPlayer().getUser().getChips() - chipsForGame ;
		//プラスされるチップ用の変数を用意
		int gettingChips = 0 ;
		//倍率決定のためにplayerの手札の合計と手札の枚数,勝敗を準備しておく
		int playersPoint = game.getPlayer().calculateSumOfHand() ;
		int numberOfPlayersCards = game.getPlayer().getHand().size() ;
		Player.PlayerResult playersResult = game.getPlayer().getPlayerResult() ;

		if(playersPoint == 21 && numberOfPlayersCards == 2 && playersResult == Player.PlayerResult.WIN) {
			//最初の段階でブラックジャックで勝ったとき
			gettingChips = (int) (chipsForGame * 1.5) ;
		} else if (playersPoint == 21 && playersResult == Player.PlayerResult.WIN) {
			//ブラックジャックになって勝ったとき
			gettingChips = (int) (chipsForGame * 2.5) ;
		} else if(playersResult == Player.PlayerResult.WIN) {
			//ブラックジャック以外で勝ったとき
			gettingChips = chipsForGame * 2 ;
		}else if (playersResult == Player.PlayerResult.DRAW) {
			//引き分けのとき
			gettingChips = chipsForGame ;
		} else if (playersResult == Player.PlayerResult.LOSE) {
			//負けのとき
			gettingChips = 0 ;
		}
		remainChips += gettingChips ;
		//残りチップをuserに再セット
		game.getPlayer().getUser().setChips(remainChips);
		
		return gettingChips ;
	}
	
	//setter,getter
	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}
	
	
	

}
