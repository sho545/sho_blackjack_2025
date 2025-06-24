package model;

import java.util.Collections;
import java.util.List;

import model.Game.GamePhase;
import model.card.Card;
import model.card.Deck;
import model.card.Hand;
import model.card.Hand.HandPhase;
import model.card.Hand.HandResult;
import model.player.BasePlayer;
import model.player.Dealer;
import model.player.Player;

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
		game.setGamePhase(Game.GamePhase.INITIAL_DEAL) ;
		
		Deck deck = game.getDeck() ;
		List<Player> players = game.getPlayers() ;
		Dealer dealer = game.getDealer() ;
		
		//player/dealerの手札0にカードを2枚ずつ配る		
		for(int i=0; i<players.size(); i++) {
			for(int j=0; j<2; j++) {
				players.get(i).getHands().get(0).getList().add(deck.drawCard()) ;
			}
		}
//		Card sp1 = new Card(3,1) ;
//		Card hart1 = new Card(1,1) ;
//		
//		player.getHand().add(sp1);
//		player.getHand().add(hart1) ;
		
		for(int i=0; i<2; i++) {
			dealer.getHands().get(0).getList().add(deck.drawCard()) ;
		}
		//player毎に手札0の合計をセット
		for(int i=0; i<players.size(); i++) {
			Player player = players.get(i) ;
			//i番目のplayerの手札0
			Hand hand = player.getHands().get(0) ;
			hand.setSumOfHand(hand.calculateSumOfHand()) ;
		}
		dealer.getHands().get(0).calculateSumOfHand() ;
	}
	
	//バーストチェックを行って、バーストの結果をgameにセット
	//バースト発生していたplayeのhandにはhand result　loseをセット
	public  void checkAndSetBust(Game game) {
		List<Player> players = game.getPlayers() ;
		BasePlayer dealer = game.getDealer() ;
		
		//playerのhand毎にbustチェック
		for(int i=0; i<players.size(); i++) {
			//i番目のplayerについての操作
			Player player = players.get(i) ;	
			for(int j=0; j<player.getHands().size(); j++) {
				//i番目のplayerのj番目の手札のバストチェックの結果とバストならoverフェーズをセット
				Hand hand = player.getHands().get(j);
				hand.setBust(hand.checkBust());
				if(hand.checkBust()) {
					hand.setHandResult(HandResult.LOSE);
					hand.setHandPhase(HandPhase.OVER);
				}
			}
		}
		if(dealer.getHands().get(0).calculateSumOfHand() > 21) {
			dealer.getHands().get(0).setBust(true); 
		}	
	}
	
	//splitチェックを行ってsplitならplayerにisSplit=trueをセット
	public void checkAndSetSplit(Game game) {
		List<Player> players = game.getPlayers() ;
		for(int i=0; i<players.size(); i++) {
			Player player = players.get(i) ;
			player.setSplit(player.checkSplit());
		}
	}
	
	//isSplit=trueのplayerにsplitの操作(hand0とhand1にsplit用の手札をそれぞれ用意)
	//ゲームフェーズをplayer turnに
	public void split(Game game) {
		List<Player> players = game.getPlayers() ;
		Deck deck = game.getDeck() ;
		for(int i=0; i<players.size(); i++) {
			Player player = players.get(i) ;
			if(player.isSplit()) {
				//isSplit=trueのプレイヤーに対してsplitの操作
				List<Hand> hands = player.getHands() ;
				//split用の手札1に手札0をディープコピー
				hands.add(new Hand(hands.get(0))) ;
				//hand0の2枚目のカードを消去してデッキから一枚ドロー
				hands.get(0).getList().remove(1) ;
				hands.get(0).getList().add(deck.drawCard()) ;
				//hand1の1枚目のカードを消去してデッキから一枚ドロー
				hands.get(1).getList().remove(0) ;
				hands.get(1).getList().add(deck.drawCard()) ;
				//手札0と1の手札合計をそれぞれセット
				hands.get(0).setSumOfHand(hands.get(0).calculateSumOfHand()) ;
				hands.get(1).setSumOfHand(hands.get(1).calculateSumOfHand()) ;
			}
		}
		game.setGamePhase(GamePhase.PLAYER_TURN) ;
	}
	
	//dealerにカードを引かせる
	//プレイヤーのバストを確認して全てのプレイヤーがバストしていればなにもしない
	public void makeDealerDrawCards(Game game) {
		game.setGamePhase(Game.GamePhase.DEALER_TURN);
		//プレイヤーの手札がすべてバストしているかをチェックするboolean bustを用意する
		//全てバストの時はbust=true
		boolean bust = true ;
		List<Player> players = game.getPlayers() ;
		for(int i=0; i<players.size(); i++) {
			Player player = players.get(i);
			for(int j=0; j<player.getHands().size(); j++) {
				bust &= player.getHands().get(j).checkBust() ;
			}		
		}
		Hand dealersHand = game.getDealer().getHands().get(0) ;
		dealersHand.setSumOfHand(dealersHand.calculateSumOfHand()) ;
		if(!bust) {
			game.getDealer().drawCards(dealersHand, game.getDeck());
		}
	}
	
	//勝敗チェックしてplayer resultをセットしてGame Overフェーズをセット
	//bustチェック後呼び出します
	public void checkGameOver(Game game) {
		
		List<Player> players = game.getPlayers() ;
		boolean dealerIsBust = game.getDealer().getHands().get(0).checkBust() ;
		int dealerSumOfHand = game.getDealer().getHands().get(0).calculateSumOfHand() ;
		
		//各playerの手札ごとに勝敗決定
		for(Player player: players) {
			List<Hand> playerHands = player.getHands() ;
			for(Hand hand: playerHands) {
				//i番目のplayerの手札jの勝敗セット
				if(hand.checkBust()) {
					hand.setHandResult(HandResult.LOSE) ;
					hand.setHandPhase(HandPhase.OVER);
				}else if(dealerIsBust) {
					hand.setHandResult(HandResult.WIN);
					hand.setHandPhase(HandPhase.OVER);
				}else {
					if(hand.calculateSumOfHand() > dealerSumOfHand) {
						hand.setHandResult(HandResult.WIN) ;
					}else if (hand.calculateSumOfHand() < dealerSumOfHand) {
						hand.setHandResult(HandResult.LOSE) ;
					}else {
						hand.setHandResult(HandResult.DRAW) ;
					}
					hand.setHandPhase(HandPhase.OVER);
				}
			}
		}
		//全ての手札で勝敗がセットされたかを確認するためのboolean result(=trueなら全てセット済み)を用意
		boolean result = true ;
		for(Player player: players) {
			List<Hand> playerHands = player.getHands() ;
			for(Hand hand: playerHands) {
				result &= (hand.getHandResult() != HandResult.UNDEFINED) ;
			}
		}
		//全ての手札で勝敗がセットされたらゲームフェーズをoverに
		if(result) {
			game.setGamePhase(GamePhase.GAME_OVER);
		}else {
			System.err.println("全ての手札に結果がセットされていません");
		}
	}
	
	//ここから
	//plyer毎にgettingChipsを計算してセット
	//checkGameOverの後に呼び出す
	public void calculateChips(Game game) {
		List<Player> players = game.getPlayers() ;
		for(Player player : players) {
			int gettingChips = 0 ;
			List<Hand> playerHands = player.getHands() ;
			//ここから
		}
		//残りのチップを計算
		int chipsForGame = game.getPlayer().getChipsForGame() ;
		int remainChips = game.getPlayer().getUser().getChips() - chipsForGame ;
		if(game.getSplitPlayer() != null) {
			remainChips -= chipsForGame ;
		}
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
		//splitPlayerがいるときはsplitPlayerの分のチップの増減も追加
		if(game.getSplitPlayer() != null) {
			int splitPlayersPoint = game.getSplitPlayer().calculateSumOfHand() ;
			int numberOfSplitPlayersCards = game.getSplitPlayer().getHand().size() ;
			Player.PlayerResult splitPlayersResult = game.getSplitPlayer().getPlayerResult() ;

			if(splitPlayersPoint == 21 && numberOfSplitPlayersCards == 2 && splitPlayersResult == Player.PlayerResult.WIN) {
				//最初の段階でブラックジャックで勝ったとき
				gettingChips += (int) (chipsForGame * 1.5) ;
			} else if (splitPlayersPoint == 21 && splitPlayersResult == Player.PlayerResult.WIN) {
				//ブラックジャックになって勝ったとき
				gettingChips += (int) (chipsForGame * 2.5) ;
			} else if(splitPlayersResult == Player.PlayerResult.WIN) {
				//ブラックジャック以外で勝ったとき
				gettingChips += chipsForGame * 2 ;
			}else if (splitPlayersResult == Player.PlayerResult.DRAW) {
				//引き分けのとき
				gettingChips += chipsForGame ;
			} else if (splitPlayersResult == Player.PlayerResult.LOSE) {
				//負けのとき
				gettingChips += 0 ;
			}
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
