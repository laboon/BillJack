package com.laboon.billjack.server;

import java.util.ArrayList;

public class Hand {
	
	private ArrayList<Card> _cards = new ArrayList<Card>();
	
	public void addCard(Card toAdd) {
		_cards.add(toAdd);
	}
	
	public void clear() {
		_cards.clear();
	}
}
