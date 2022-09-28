/**
 * This class is used to model a hand of Pair. 
 * 
 * @author Siu Ka Sing 3035690373
 */

public class Pair extends Hand {
	/**
	 * a constructor for building a hand with the specified player and list of cards
	 * 
	 * @param player the player
	 * @param cards the cards of the player
	 */
	public Pair(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	
	/**
	 * a method for checking if this is a valid hand
	 * @return whether this is a valid hand
	 */
	 public boolean isValid(){
		if (this.size() == 2 && this.getCard(0).rank == this.getCard(1).rank) {
			return true;
		}
		return false;
	}
	 
	 
	/**
	  * a method for returning a string specifying the type of this hand
      * @return a string specifying the type of this hand
      */
	public String getType() {
		return "Pair";
	}

}
