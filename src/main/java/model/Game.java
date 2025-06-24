package model;

import java.util.List;

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
	}
	
	private List<Player> players ;
	private Dealer dealer ;
	private Deck deck ;
	private GamePhase gamePhase ;
	

	//コンストラクタでusersを受け取って初期化
	public Game(List<User> users) {
		for(int i=0; i<users.size(); i++) {
			players.add( new Player(users.get(i))) ;
		}
		this.dealer = new Dealer() ;
		this.deck = new Deck() ;
		this.gamePhase = GamePhase.NOT_STARTED ;
	}
	
	//getter,setter
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

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	
	
	
	

}
