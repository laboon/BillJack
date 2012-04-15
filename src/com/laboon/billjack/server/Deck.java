package com.laboon.billjack.server;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {

	ArrayList<Card> _deck = new ArrayList<Card>();
	
	public Deck() {
		this.reset();
	}
	
	public void shuffle() {
		Collections.shuffle(_deck);
	}
	
	public void reset() {
		_deck.clear();
		
		for (int j=1; j <= 4; j++) {
			for (int k=1; k <= 13; k++) {
				_deck.add(new Card(j, k));
			}
		}
		// System.out.println("Size: " + _deck.size());	
		this.shuffle();
	}
	
	public Card dealCard() {
		
		// TODO: Add check that cards exist in deck!
		
		Card toReturn = _deck.get(0);
		_deck.remove(0);
		return toReturn;
	}
	
	public static void main(String args[]) {
		Deck foo = new Deck();
		Card dealtCard;
		for (int j = 0; j < 52; j++) {
			dealtCard = foo.dealCard();
			System.out.println("Dealt " + dealtCard.toString());
		}
	}
	
}
