import java.util.ArrayList;

/**
 * This class is used to model a Big Two card game. 
 * 
 * @author Siu Ka Sing 3035690373
 */

public class BigTwo implements CardGame{
	private ArrayList<CardGamePlayer> playerList;    //a list of players
	private ArrayList<Hand> handsOnTable;            //a list of hands played on the table
	private int currentIdx;                          //an integer specifying the index of the current player
	private BigTwoConsole bigTwoConsole;             //a BigTwoConsole object for providing the user interface
	private BigTwoTable table;                       //a Big Two table which builds the GUI for the game and handles all user actions
	private CardList input;							 //cards inputed by player
	
	/**
	 * A constructor for creating a Big Two card game
	 * 
	 * 4 players are created and added to the player list
	 * A 'console' is also created to provide the user interface
	 */
	public BigTwo() {
		ArrayList<CardGamePlayer> pList = new ArrayList<CardGamePlayer>();
		CardGamePlayer player0 = new CardGamePlayer();
		CardGamePlayer player1 = new CardGamePlayer();
		CardGamePlayer player2 = new CardGamePlayer();
		CardGamePlayer player3 = new CardGamePlayer();
		pList.add(player0);
		pList.add(player1);
		pList.add(player2);
		pList.add(player3);
		this.playerList = pList;
		this.currentIdx = 0;
		this.handsOnTable = new ArrayList<Hand>();
		this.bigTwoConsole = new BigTwoConsole(this);
		this.table = new BigTwoTable(this);
	}
	
	
	/**
	 * a method for retrieving the deck of cards being used
	 * @return the deck of cards being used
	 */
	public Deck getDeck() {
		BigTwoDeck deck = new BigTwoDeck();
		deck.initialize();
		return deck;
	}	

	
	/**
	 * a method for retrieving the list of players
	 * @return the list of players
	 */
	public ArrayList<CardGamePlayer> getPlayerList() {
		return playerList;
	}

	
	/**
	 * a method for retrieving the list of hands played on the table
	 * @return the list of hands played on the table
	 */
	public ArrayList<Hand> getHandsOnTable() {
		return handsOnTable;
	}

	
	/**
	 * a method for retrieving the index of the current player
	 * @return the index of the current player
	 */
	public int getCurrentIdx() {
		return currentIdx;
	}

	
	/**
	 * a method for starting the game with a (shuffled) deck of cards supplied as the argument. 
	 * It implements the Big Two game logics.
	 * 
	 * @param deck the deck of cards used in the game
	 */
	public void start(Deck deck) {
		//intialize the deck again by removing all the cards and redistribute
		deck.initialize();
		deck.shuffle();
		//distribute cards
		for (int i=0; i<52; i++) {
			this.playerList.get(i%4).addCard(deck.getCard(i));
			//identify the player who holds the 3 of Diamonds
			if (deck.getCard(i).rank == 2 && deck.getCard(i).suit == 0) {
				this.bigTwoConsole.setActivePlayer(i%4);
				this.currentIdx = i%4;
				this.table.setActivePlayer(i%4);
			}
		}
		//sort the cards for each player
		for (int i=0; i<4; i++) {
			this.playerList.get(i).getCardsInHand().sort();
		}
		
		this.table.printMsg("Player " + this.currentIdx + "'s turn:\n");
	}
	
	
	/**
	 * a method for starting a Big Two card game.
	 * It should create a Big Two card game, create and shuffle a deck of cards, and start the game with the deck of cards
	 * 
	 * @param args null
	 */
	public static void main(String[] args) {
		BigTwo game = new BigTwo();
		BigTwoDeck deck = (BigTwoDeck) game.getDeck();
		game.start(deck);
	}
	

	/**
	 * a method for returning a valid hand from the specified list of cards of the player.
	 * Returns null is no valid hand can be composed from the specified list of cards.
	 * 
	 * @param player the player
	 * @param cards the list of cards of the player
	 * @return a valid hand from the specified list of cards of the player, null will be returned if no valid hand is presence
	 */
	public static Hand composeHand(CardGamePlayer player, CardList cards) {
		cards.sort();
		//checking which type the hands might belong to one by one
		if (cards.size() == 1) {
			//Single
			Single testSingle = new Single(player, cards);
			for (int i=0;i < cards.size();i++) {
				testSingle.addCard(cards.getCard(i));
			}
			if (testSingle.isValid()) {
				return (Hand) testSingle;
			}
		}
		if (cards.size() == 2) {
			//Pair
			Pair testPair = new Pair(player, cards);
			for (int i=0;i < cards.size();i++) {
				testPair.addCard(cards.getCard(i));
			}
			if (testPair.isValid()) {
				return (Hand) testPair;
			}
		}
		if (cards.size() == 3) {
			//Triple
			Triple testTriple = new Triple(player, cards);
			for (int i=0;i < cards.size();i++) {
				testTriple.addCard(cards.getCard(i));
			}
			if (testTriple.isValid()) {
				return (Hand) testTriple;
			}
		}
		if (cards.size() == 5) {
			//Quad/
			Quad testQuad = new Quad(player, cards);
			for (int i=0;i < cards.size();i++) {
				testQuad.addCard(cards.getCard(i));
			}
			if (testQuad.isValid()) {
				return (Hand) testQuad;
			}
			
			//StraightFlush
			StraightFlush testStraightFlush = new StraightFlush(player, cards);
			for (int i=0;i < cards.size();i++) {
				testStraightFlush.addCard(cards.getCard(i));
			}
			if (testStraightFlush.isValid()) {
				return (Hand) testStraightFlush;
			}

			//Flush
			Flush testFlush = new Flush(player, cards);
			for (int i=0;i < cards.size();i++) {
				testFlush.addCard(cards.getCard(i));
			}
			if (testFlush.isValid()) {
				return (Hand) testFlush;
			}
			
			//Straight
			Straight testStraight = new Straight(player, cards);
			for (int i=0;i < cards.size();i++) {
				testStraight.addCard(cards.getCard(i));
			}
			if (testStraight.isValid()) {
				return (Hand) testStraight;
			}
			
			//FullHouse
			FullHouse testFullHouse = new FullHouse(player, cards);
			for (int i=0;i < cards.size();i++) {
				testFullHouse.addCard(cards.getCard(i));
			}
			if (testFullHouse.isValid()) {
				return (Hand) testFullHouse;
			}
		}
		return null;
	}

	
	/**
	 * Returns the number of players in this card game.
	 * 
	 * @return the number of players in this card game
	 */
	public int getNumOfPlayers() {
		return this.playerList.size();
	}


	/**
	 * Makes a move by the player.
	 * 
	 * @param playerID
	 *            the playerID of the player who makes the move
	 * @param cardIdx
	 *            the list of the indices of the cards selected by the player
	 */
	public void makeMove(int playerID, int[] cardIdx) {
		checkMove(playerID, cardIdx);
	}


	/**
	 * Checks the move made by the player.
	 * 
	 * @param playerID
	 *            the playerID of the player who makes the move
	 * @param cardIdx
	 *            the list of the indices of the cards selected by the player
	 */
	public void checkMove(int playerID, int[] cardIdx) {
		boolean valid = false;  //check whether the hand played is valid
		boolean passed = false;
		
		//check if the player passed
		if (cardIdx == null) {
			this.table.printMsg("{pass}\n");
			passed = true;
			
			//proceed to next player
			this.currentIdx = (playerID+1)%4;
			this.bigTwoConsole.setActivePlayer(this.currentIdx);
			this.table.setActivePlayer(this.currentIdx);
			this.table.printMsg("Player "+this.currentIdx+"'s turn:\n");
		}
		
		//store the cards played by player
		input = new CardList();
		if (cardIdx != null) {
			for (int i=0; i < cardIdx.length; i++) {
				input.addCard(this.playerList.get(playerID).getCardsInHand().getCard(cardIdx[i]));
			}
		}
		
		//check whether the player can make his move or not
		if (composeHand(this.playerList.get(this.currentIdx),input) != null){
			//check if there is hands on table and not everyone passed
			if (this.handsOnTable.size()!=0 && this.handsOnTable.get(this.handsOnTable.size()-1).getPlayer() != playerList.get(playerID)) {
				//check if same type as hands on table
				if ((composeHand(this.playerList.get(this.currentIdx),input).getType()==this.handsOnTable.get(this.handsOnTable.size()-1).getType())){
					//check if the hands is larger than hands on table
					if (composeHand(this.playerList.get(this.currentIdx),input).beats(this.handsOnTable.get(this.handsOnTable.size()-1))) {
						valid = true;
					}
				}
			}
			
			//if everyone passed
			if (this.handsOnTable.size() != 0) {
				if (this.handsOnTable.get(this.handsOnTable.size()-1).getPlayer() == playerList.get(playerID)) {
					valid = true;
				}
			}
			
			//first player playing
			else if (handsOnTable.size() == 0){
				//check if first hands contain 3
				for (int i=0; i < cardIdx.length; i++) {
					if (input.getCard(i).rank == 2 && input.getCard(i).suit == 0) {
						valid = true;
					}
				}
			}
		}
		
		//execute moving
		if (valid) {
			if (input != null) {
				this.playerList.get(this.currentIdx).removeCards(input);
				this.handsOnTable.add(composeHand(this.playerList.get(this.currentIdx),input));
				this.table.printMsg(this.getHandsOnTable().get(this.getHandsOnTable().size()-1).getType());
				this.table.printMsg(this.getHandsOnTable().get(this.getHandsOnTable().size()-1).toString()+"\n");
				input.removeAllCards();
				this.table.resetSelected();
			}
			
			//proceed to next player
			this.currentIdx = (this.currentIdx+1)%4;
			this.bigTwoConsole.setActivePlayer(this.currentIdx);
			this.table.setActivePlayer(this.currentIdx);
			this.table.printMsg("Player "+this.currentIdx+"'s turn:\n");
		}
		else if (!passed){
			this.table.printMsg(input.toString()+"   <== Not a legal move!!!\n");
			this.table.printMsg("Player "+this.currentIdx+"'s turn:\n");
			input.removeAllCards();
			this.table.resetSelected();
		}
		
		
		//if end game
		if (endOfGame()) {
			System.out.println("Game ends\n");
			this.table.printMsg("Game ends\n");
			if (playerList.get(0).getNumOfCards() == 0) {
				System.out.println("Player 0 wins the game.\n");
				this.table.printMsg("Player 0 wins the game.\n");
			}
			else {
				System.out.println("Player 0 has " + playerList.get(0).getNumOfCards() + " cards in hand.\n");
				this.table.printMsg("Player 0 has " + playerList.get(0).getNumOfCards() + " cards in hand.\n");
			}
					
					
			if (playerList.get(1).getNumOfCards() == 0) {
				System.out.println("Player 1 wins the game.\n");
				this.table.printMsg("Player 1 wins the game.\n");
			}
			else {
				System.out.println("Player 1 has " + playerList.get(1).getNumOfCards() + " cards in hand.\n");
				this.table.printMsg("Player 1 has " + playerList.get(1).getNumOfCards() + " cards in hand.\n");
			}
					
					
			if (playerList.get(2).getNumOfCards() == 0) {
				System.out.println("Player 2 wins the game.\n");
				this.table.printMsg("Player 2 wins the game.\n");
			}
			else {
				System.out.println("Player 2 has " + playerList.get(2).getNumOfCards() + " cards in hand.\n");
				this.table.printMsg("Player 2 has " + playerList.get(2).getNumOfCards() + " cards in hand.\n");
			}
					
					
			if (playerList.get(3).getNumOfCards() == 0) {
				System.out.println("Player 3 wins the game.\n");
				this.table.printMsg("Player 3 wins the game.\n");
			}
			else {
				System.out.println("Player 3 has " + playerList.get(3).getNumOfCards() + " cards in hand.\n");
				this.table.printMsg("Player 3 has " + playerList.get(3).getNumOfCards() + " cards in hand.\n");
			}
			
			//disable the GUI
			this.table.disable();
		}
	}

	
	/**
	 * Checks for end of game.
	 * 
	 * @return true if the game ends; false otherwise
	 */
	public boolean endOfGame() {
		for (int i=0; i<4; i++) {
			if(this.playerList.get(i).getNumOfCards() == 0) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * a method to remove hands on table
	 */
	public void removeHandsOnTable() {
		this.handsOnTable = new ArrayList<Hand>();
	}

}
