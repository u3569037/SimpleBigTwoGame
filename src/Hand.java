import java.util.ArrayList;

/**
 * This class is used to model a hand of cards.
 * It has a private instance variable for storing the player who plays this hand. 
 * It also has methods for getting the player of this hand, checking if it is a valid hand, getting the type of this hand, getting the top card of this hand, and checking if it beats a specified hand. 
 * 
 * @author Siu Ka Sing 3035690373
 */

public abstract class Hand extends CardList {
	/**
	 * the player who plays this hand
	 */
	private CardGamePlayer player;  
	
	
	/**
	 * a constructor for building a hand with the specified player and list of cards
	 * 
	 * @param player the player
	 * @param cards the cards of the player
	 */
	public Hand(CardGamePlayer player, CardList cards) {
		this.player = player;
		for (int i=0; i<cards.size();i++) {
			player.addCard(getCard(i)); 
		}	
	}
	
	
	/**
	 * a method for retrieving the player of this hand
	 * @return the player of this hand
	 */
	public CardGamePlayer getPlayer() {
		return player;
	}
	
	
	/**
	 * a method for retrieving the top card of this hand
	 * @return the top card of this hand
	 */
	public Card getTopCard() {
		Card top = this.getCard(0);
		for (int i=0; i < this.size(); i++) {   //comparing cards one by one
			if (this.getCard(i).compareTo(top) > 0){
				top = this.getCard(i);
			}
		}
		return top;
	}
	
	
	/**
	 * a method for checking if this hand beats a specified hand
	 * @param hand
	 * @return whether this hand beats a specified hand
	 */
	public boolean beats(Hand hand) {
		if (this.getTopCard().compareTo(hand.getTopCard()) > 0){
			return true;
		}
		return false;
	}
	
	
	 /**
	  * a method for checking if this is a valid hand
	  * @return whether this is a valid hand
	  */
	 public abstract boolean isValid();
	 
	 
	 /**
	  * a method for returning a string specifying the type of this hand
	  * @return a string specifying the type of this hand
	  */
	 public abstract String getType();
	 
}
