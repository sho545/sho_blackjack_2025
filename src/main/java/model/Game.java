package model;

import model.card.Deck;
import model.player.Dealer;
import model.player.Player;
import user.User ;

public class Game {
	
	public enum GamePhase{
		NOT_STARTED,
		INITIAL_DEAL, //初期カード配布中
		PLAYER_TURN,
		DEALER_TURN,
		GAME_OVER,
		SPLIT_PLAYER_TURN
	}
	
	private Player player ;
	private Dealer dealer ;
	private Deck deck ;
	private GamePhase gamePhase ;
	private Player splitPlayer ;
	

	//コンストラクタでloginUserを受け取って初期化
	public Game(User loginUser) {
		this.player = new Player(loginUser) ;
		this.dealer = new Dealer() ;
		this.deck = new Deck() ;
		this.gamePhase = GamePhase.NOT_STARTED ;
	}
	
	//getter,setter
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	public Dealer getDealer() {
		return dealer;
	}
	public void setDealer(Dealer dealer) {
		this.dealer = dealer;
	}
	public Deck getDeck() {
		return deck;
	}
	public void setDeck(Deck deck) {
		this.deck = deck;
	}
	public GamePhase getGamePhase() {
		return gamePhase;
	}
	public void setGamePhase(GamePhase gamePhase) {
		this.gamePhase = gamePhase;
	}

	public Player getSplitPlayer() {
		return splitPlayer;
	}

	public void setSplitPlayer(Player splitPlayer) {
		this.splitPlayer = splitPlayer;
	}
	
	
	

}
