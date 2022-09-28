/**
 * This class is used to model a hand of FullHouse. 
 * 
 * @author Siu Ka Sing 3035690373
 */

public class FullHouse extends Hand {
	/**
	 * a constructor for building a hand with the specified player and list of cards
	 * 
	 * @param player the player
	 * @param cards the cards of the player
	 */
	public FullHouse(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	
	/**
	 * a method for checking if this is a valid hand
	 * @return whether this is a valid hand
	 */
	 public boolean isValid(){
		this.sort();
		if (this.size() == 5) {
			for (int i=0; i<3; i++) {
				if (this.getCard(i).rank == this.getCard(i+1).rank && this.getCard(i+1).rank == this.getCard(i+2).rank) {
					if (this.getCard((i+3)%5).rank == this.getCard((i+4)%5).rank) {
						return true;
					}
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
		return "FullHouse";
	}

	
	/**
	 * a method for retrieving the top card of this hand
	 * @return the top card of this hand
	 */
	public Card getTopCard() {
		this.sort();
		CardList topList = new CardList();
		if (this.size() == 5) {
			for (int i=0; i<3; i++) {
				if (this.getCard(i).rank == this.getCard(i+1).rank && this.getCard(i+1).rank == this.getCard(i+2).rank) {
					topList.addCard(this.getCard(i));
					topList.addCard(this.getCard(i+1));
					topList.addCard(this.getCard(i+2));
				}
			}
		}
		Card top = topList.getCard(0);
		for (int i=0; i<topList.size(); i++) {
			if (topList.getCard(i).compareTo(top) > 0){
				top = topList.getCard(i);
			}
		}
		return top;
	}
}
