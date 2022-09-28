/**
 * This class is used to model a hand of Straight. 
 * 
 * @author Siu Ka Sing 3035690373
 */

public class Straight extends Hand {
	/**
	 * a constructor for building a hand with the specified player and list of cards
	 * 
	 * @param player the player
	 * @param cards the cards of the player
	 */
	public Straight(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	
	/**
	 * a method for checking if this is a valid hand
	 * @return whether this is a valid hand
	 */
	 public boolean isValid(){
		this.sort();
		if (this.size() == 5) {
			for (int i=0; i < 2; i++) {
				if (this.getCard(i).rank + 1 == this.getCard(i+1).rank && this.getCard(i+1).rank + 1 == this.getCard(i+2).rank && this.getCard(i+2).rank + 1 == this.getCard(i+3).rank && this.getCard(i+3).rank + 1 == this.getCard(i+4).rank) {
					return true;
				}
			}
		}
		return false;
	}
	 
	 
	/**
	  * a method for returning a string specifying the type of this hand
      * @return a string specifying the type of this hand
      */
	public String getType() {
		return "Straight";
	}
}
