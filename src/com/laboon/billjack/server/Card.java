package com.laboon.billjack.server;

public class Card {
	
	public static final int HEARTS = 1;
	public static final int DIAMONDS = 2;
	public static final int CLUBS = 3;
	public static final int SPADES = 4;
	
	public static final int ACE = 1;
	public static final int JACK = 11;
	public static final int QUEEN = 12;
	public static final int KING = 13;
	
	int _suit = 0;
	int _val = 0;
	
	public Card(int suit, int val) {
		
		// TODO: add some error checking here?
		
		_suit = suit;
		_val = val;
	}
	
	public int getSuit() {
		return _suit;
	}
	
	public int getVal() {
		return _val;
	}
	
	public String getImageName() {
		
		String toReturn = "images/";

		switch (_suit) {
		case HEARTS:
			toReturn += "hearts-";
			break;
		case DIAMONDS:
			toReturn += "diamonds-";
			break;
		case SPADES:
			toReturn += "spades-";
			break;
		case CLUBS:
			toReturn += "clubs-";
			break;
		}
		
		switch (_val) {
		case ACE:
			toReturn += "a";
			break;
		case JACK:
			toReturn += "j";
			break;
		case QUEEN:
			toReturn += "q";
			break;
		case KING:
			toReturn += "k";
			break;
		default:
			toReturn += _val;
		}
		
		toReturn += "-150.png";
		return toReturn;

	}
	
	public String toString() {
		
		String toReturn = "";
		
		switch (_val) {
		case ACE:
			toReturn += "A";
			break;
		case JACK:
			toReturn += "J";
			break;
		case QUEEN:
			toReturn += "Q";
			break;
		case KING:
			toReturn += "K";
			break;
		case 10:
			toReturn += "T";
			break;
		default:
			toReturn += _val;
		}
		
		switch (_suit) {
		case HEARTS:
			toReturn += "h";
			break;
		case DIAMONDS:
			toReturn += "d";
			break;
		case SPADES:
			toReturn += "s";
			break;
		case CLUBS:
			toReturn += "c";
			break;
		}
			
		return toReturn;
	}
}
