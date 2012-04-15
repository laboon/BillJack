package com.laboon.billjack.server;

import java.util.ArrayList;

import com.laboon.billjack.client.BillJackService;
import com.laboon.billjack.shared.FieldVerifier;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;


/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class BillJackServiceImpl extends RemoteServiceServlet implements
		BillJackService {

	public final static int BASE_BET = 10;
	
	Deck _deck = new Deck();
	String _name;
	ArrayList<Card> _dealerCards = new ArrayList<Card>();
	ArrayList<Card> _playerCards = new ArrayList<Card>();
	int _money = 0;
	
	private String createReturnString(boolean done) {
		StringBuilder toReturn = new StringBuilder();
		if (done) {
			toReturn.append("Dealer (" + calculateBestValue(_dealerCards) +")<br>");
		} else {
			toReturn.append("Dealer:<br>");
		}
		if (done) {
			for (Card c : _dealerCards) {
				toReturn.append("<img src=\"" + c.getImageName() + "\"> ");
			}
		} else {
			toReturn.append("<img src=\"" + _dealerCards.get(0).getImageName() + "\"> ");
			toReturn.append("<img src=\"images/back-blue-150-1.png\"> ");
		}
		toReturn.append("<br>" + _name  
				+ " (" + calculateBestValue(_playerCards) +")"
				+ "$" + _money + "<br>");
		for (Card c : _playerCards) {
			toReturn.append("<img src=\"" + c.getImageName() + "\"> ");
		}

		return toReturn.toString();
	}
	
	private int calculateBestValue(ArrayList<Card> hand) {
		int toReturn = 0;
		int numAces = 0;
		
		for (Card c : hand) {
			switch (c.getVal()) {
				case Card.JACK:
				case Card.QUEEN:
				case Card.KING:
					toReturn += 10;
					break;
				case Card.ACE:
					toReturn += 11;
					numAces++;
					break;
				default:
					toReturn += c.getVal();
			}
		}
		for (int j=0; j < numAces; j++) {
			if (toReturn > 21) {
				toReturn -= 10;
			}
		}
		return toReturn;
	}
	
	private void dealerPlay() {
		while (calculateBestValue(_dealerCards) < 17) {
			_dealerCards.add(_deck.dealCard());
		}
	}
	
	private void putPlayerData() {
		
	}
	
	private int getPlayerData() {
		return 50;
		
		/*
		int toReturn = 0;
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		boolean playerExists = false;
		Entity player;
		
		// If player exists, get how much he/she has
		// If player does not exist, add him/her
		
		try {
			player = datastore.get(KeyFactory.createKey("Player", _name));
			toReturn = ((Integer) player.getProperty("money")).intValue();
			playerExists = true;
		} catch (EntityNotFoundException enfex) {
			playerExists = false;
		}
		
		if (!playerExists) {
			toReturn = 200;
			player = new Entity("Player");
			player.setProperty("name", _name);
			player.setProperty("money", _money);
			datastore.put(player);
		}
		
		return toReturn;
		*/
	}
	
	public Integer getPlayerVal() {
		return new Integer(calculateBestValue(_playerCards));
	}
	
	private String playHand() {
		
		_dealerCards.clear();
		_playerCards.clear();
		_deck.reset();
		
		_money -= BASE_BET;
		
		_dealerCards.add(_deck.dealCard());
		_dealerCards.add(_deck.dealCard());
		_playerCards.add(_deck.dealCard());
		_playerCards.add(_deck.dealCard());
		return createReturnString(false);
		
	}
	
	public String startGame(String input) throws IllegalArgumentException {
		
		// Verify that the input is valid. 
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException(
					"Name must be at least one character long");
		}
		
		
		_money = getPlayerData();
		input = escapeHtml(input);
		_name = input;
		
		// String serverInfo = getServletContext().getServerInfo();
		// String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		// Escape data from the client to avoid cross-site script vulnerabilities.
		
		// userAgent = escapeHtml(userAgent);
		
		return playHand();
	}

	public String newHand() {
		return playHand();
	}
	
	public String hit() {
		String toReturn;
		_playerCards.add(_deck.dealCard());
		int handValue = calculateBestValue(_playerCards);
		if (handValue > 21) {
			toReturn = createReturnString(false) + "<br>BUSTED!";
		} else {
			toReturn = createReturnString(false);
		}
		
		if (_money < 10 && _money > 0) {
			toReturn = toReturn + "You don't have enough money to keep playing.<br>";
		} else if (_money <= 0) {
			toReturn = toReturn + "You're out of money.<br>";
		}
		
		return toReturn;
	}
	
	public String stay() {
		
		String toReturn;
		
		dealerPlay();
		
		int dealerVal = calculateBestValue(_dealerCards);
		int playerVal = calculateBestValue(_playerCards);
		int amountWon = 0;
		
		if (playerVal == 21 && _playerCards.size() == 2) {
			// That's a blackjack, special rules apply
			if (dealerVal != 21) {
				amountWon = (int) ((int) BASE_BET * 2.5);
				_money += amountWon; 
				toReturn = _name + " GOT BLACKJACK, WON " + amountWon 
					+ "!<br>" + createReturnString(true);
			} else {
				if (_dealerCards.size() > 2) {
					amountWon = (int) ((int) BASE_BET * 2.5);
					_money += amountWon;
					toReturn = _name + " GOT BLACKJACK, WON " + amountWon 
						+ "!<br>" + createReturnString(true);
				} else {
					amountWon = BASE_BET;
					_money += amountWon;
					toReturn = _name + " AND DEALER BOTH GOT BLACKJACK, PUSH!<br>" 
						+ createReturnString(true);
				}
			}
			
		} else if (dealerVal > 21) {
			// Dealer busted, player gets his money back + more
			amountWon = BASE_BET * 2;
			_money += amountWon;
			toReturn = "DEALER BUSTED, " + _name + " wins " + BASE_BET 
				+ "!<br>" + createReturnString(true);
		} else if (dealerVal > playerVal) {
			// Money bet is already lost
			toReturn = "DEALER WINS!<br>" + createReturnString(true);
		} else if (dealerVal < playerVal) {
			amountWon = BASE_BET * 2;
			_money += amountWon;
			toReturn = _name + " WINS" + BASE_BET +"!<br>" + createReturnString(true);
		} else {
			amountWon = BASE_BET;
			_money += amountWon;
			toReturn = "IT'S A PUSH!<br>" + createReturnString(true);
		}
		
		if (_money < 10 && _money > 0) {
			toReturn = toReturn + "You don't have enough money to keep playing.<br>";
		} else if (_money <= 0) {
			toReturn = toReturn + "You're out of money.<br>";
		}
		
		return toReturn;
	}
	
	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}
}
