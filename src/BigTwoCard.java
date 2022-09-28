import java.util.ArrayList;

/**
 * This class is used to model a card used in a Big Two card game 
 * 
 * @author Siu Ka Sing 3035690373
 */

public class BigTwoCard extends Card {
   	 /**
	  * a constructor for building a card with the specified suit and rank. 
	  * 	 
	  * @param suit an integer between 0 and 3
	  * @param rank an integer between 0 and 12
	  */
	 public BigTwoCard(int suit, int rank) {
		 super(suit, rank);
	 }
	 

	 /**
	  * a method for comparing the order of this card with the specified card. 
	  * @param card cards to be compared
	  * @return a negative integer, zero, or a positive integer as this card is less than, equal to, or greater than the specified card
	  */
	 public int compareTo(Card card) {
		int rank1 = this.rank;
		int rank2 = card.rank;
		rank1 = (rank1+11)%13;
		rank2 = (rank2+11)%13;
		if (rank1 > rank2) {
			return 1;
		} else if (rank1 < rank2) {
			return -1;
		} else if (this.suit > card.suit) {
			return 1;
		} else if (this.suit < card.suit) {
			return -1;
		} else {
			return 0;
		}
	 }
}
