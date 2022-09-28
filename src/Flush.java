/**
 * This class is used to model a hand of Flush. 
 * 
 * @author Siu Ka Sing 3035690373
 */

public class Flush extends Hand {
	/**
	 * a constructor for building a hand with the specified player and list of cards
	 * 
	 * @param player the player
	 * @param cards the cards of the player
	 */
	public Flush(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * a method for checking if this is a valid hand
	 * @return whether this is a valid hand
	 */
	 public boolean isValid(){
		if (this.size() == 5) {
			if (this.getCard(0).suit == this.getCard(1).suit && this.getCard(1).suit == this.getCard(2).suit && this.getCard(2).suit == this.getCard(3).suit && this.getCard(3).suit == this.getCard(4).suit) {
				return true;
			}
		}
		return false;
	}
	 
	 
	/**
	  * a method for returning a string specifying the type of this hand
      * @return a string specifying the type of this hand
      */
	public String getType() {
		return "Flush";
	}

}
