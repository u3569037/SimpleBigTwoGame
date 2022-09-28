import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
/**
 * This class is used to model a Big Two card game that supports 4 players playing over the internet. 
 * 
 * @author Siu Ka Sing 3035690373
 */

public class BigTwoClient implements CardGame, NetworkGame {
	
	/**
	 * a constructor for creating a Big Two client
	 */
	public BigTwoClient() {
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
		this.handsOnTable = new ArrayList<Hand>();
		this.table = new BigTwoTable(this);
		
		//ask the player to input his/her name
		String pName = JOptionPane.showInputDialog("Please enter your name: ");
		this.playerName = pName;
		makeConnection();
	}
	
	private int numOfPlayers; //an integer specifying the number of players.
	private Deck deck; //a deck of cards.
	private ArrayList<CardGamePlayer> playerList; //a list of players.
	private ArrayList<Hand> handsOnTable; //a list of hands played on the table.
	private int playerID; //an integer specifying the playerID (i.e., index) of the local player.
	private String playerName; //a string specifying the name of the local player.
	private String serverIP; //a string specifying the IP address of the game server.
	private int serverPort; //an integer specifying the TCP port of the game server.
	private Socket sock; //a socket connection to the game server.
	private ObjectOutputStream oos; //an ObjectOutputStream for sending messages to the server.
	private int currentIdx; //an integer specifying the index of the player for the current turn.
	private BigTwoTable table; //a Big Two table which builds the GUI for the game and handles all user actions.
	private CardList input;	//cards inputed by player
	
	
	@Override
	public int getPlayerID() {
		return this.playerID;
	}
	
	@Override
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}
	
	@Override
	public String getPlayerName() {
		return this.playerName;
	}
	
	@Override
	public void setPlayerName(String playerName) {
		this.playerName = playerName;		
	}
	
	@Override
	public String getServerIP() {
		return this.serverIP;
	}
	
	@Override
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}
	
	@Override
	public int getServerPort() {
		return this.serverPort;
	}
	
	@Override
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	
	@Override
	public void makeConnection() {
		try {
			serverIP = "127.0.0.1";
			serverPort = 2396;
			sock = new Socket(serverIP, serverPort);
			oos = new ObjectOutputStream(sock.getOutputStream()); 
			
			//set up a thread for receiving messages from the game server	
			ServerHandler threadJob = new ServerHandler();
			Thread myThread = new Thread(threadJob);
			myThread.start();
			
			//send a message of join
			CardGameMessage connect = new CardGameMessage(1, -1, playerName);
			sendMessage(connect);
			
			//send a message of ready
			CardGameMessage ready = new CardGameMessage(4, -1, null);
			sendMessage(ready);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void parseMessage(GameMessage message) {
		//player list
		if (message.getType() == 0) {
			playerID = message.getPlayerID();
			String[] playerNames = (String[]) message.getData();
			for (int i=0; i < 4; i++) {
				playerList.get(i).setName(playerNames[i]);
			}
		}
		//join
		if (message.getType() == 1) {
			table.printMsg(message.getData()+" joins the game.\n");
			playerList.get(message.getPlayerID()).setName((String) message.getData());
		}
		//full
		if (message.getType() == 2) {
			table.printMsg("The game is fulled!\n");
		}
		//quit
		if (message.getType() == 3) {
			table.printMsg(playerList.get(message.getPlayerID()).getName()+" quited.\n");
			playerList.get(message.getPlayerID()).setName(null);
			if (!endOfGame()) {
				//ready
				sendMessage(new CardGameMessage(4, playerID, null));
			}
		}
		//ready
		if (message.getType() == 4) {
			table.printMsg(playerList.get(message.getPlayerID()).getName()+" is ready!\n");
		}
		//start
		if (message.getType() == 5) {
			table.printMsg("All players are ready. Game starts!\n");
			start((Deck) message.getData());
		}
		//move
		if (message.getType() == 6) {
			checkMove(message.getPlayerID(), (int[]) message.getData());
		}
		//chat?
		if (message.getType() == 7) {
			//table.chatArea.append(playerList.get(message.getPlayerID()).getName()+"  (/127.0.0.1:2396): ");
			table.chatArea.append((String) message.getData()+"\n");
		}
	}
	
	@Override
	public void sendMessage(GameMessage message) {
		try {
			oos.writeObject(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public int getNumOfPlayers() {
		return this.numOfPlayers;
	}
	
	@Override
	public Deck getDeck() {
		return this.deck;
	}
	
	@Override
	public ArrayList<CardGamePlayer> getPlayerList() {
		return this.playerList;
	}
	
	@Override
	public ArrayList<Hand> getHandsOnTable() {
		return this.handsOnTable;
	}
	
	@Override
	public int getCurrentIdx() {
		return this.currentIdx;
	}
	
	@Override
	public void start(Deck deck) {
		//distribute cards
		for (int i=0; i<52; i++) {
			this.playerList.get(i%4).addCard(deck.getCard(i));
			//identify the player who holds the 3 of Diamonds
			if (deck.getCard(i).rank == 2 && deck.getCard(i).suit == 0) {
				this.currentIdx = i%4;
				this.table.setActivePlayer(i%4);
			}
		}
		//sort the cards for each player
		for (int i=0; i<4; i++) {
			this.playerList.get(i).getCardsInHand().sort();
		}
				
		this.table.printMsg(playerList.get(this.currentIdx).getName() + "'s turn:\n");
		
	}
	
	@Override
	public void makeMove(int playerID, int[] cardIdx) {
		CardGameMessage move = new CardGameMessage(6, -1, cardIdx);
		sendMessage(move);
	}
	
	@Override
	public void checkMove(int playerID, int[] cardIdx) {
		boolean valid = false;  //check whether the hand played is valid
		boolean passed = false;
		
		//check if the player passed
		if (cardIdx == null) {
			this.table.printMsg("{pass}\n");
			passed = true;
			
			//proceed to next player
			this.currentIdx = (playerID+1)%4;
			this.table.setActivePlayer(this.currentIdx);
			this.table.printMsg(playerList.get(this.currentIdx).getName()+"'s turn:\n");
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
			this.table.setActivePlayer(this.currentIdx);
			this.table.printMsg(playerList.get(this.currentIdx).getName()+"'s turn:\n");
		}
		else if (!passed){
			this.table.printMsg(input.toString()+"   <== Not a legal move!!!\n");
			this.table.printMsg(playerList.get(this.currentIdx).getName()+"'s turn:\n");
			input.removeAllCards();
			this.table.setActivePlayer(this.currentIdx);
			this.table.resetSelected();
		}
		
		
		//if end game
		if (endOfGame()) {
			String result = ""; //game result
			result += "Game ends\n";
					
					
			System.out.println("Game ends\n");
			this.table.printMsg("Game ends\n");
			if (playerList.get(0).getNumOfCards() == 0) {
				System.out.println(playerList.get(0).getName()+" wins the game.\n");
				this.table.printMsg(playerList.get(0).getName()+" wins the game.\n");
				result += playerList.get(0).getName()+" wins the game.\n";
			}
			else {
				System.out.println(playerList.get(0).getName()+" has " + playerList.get(0).getNumOfCards() + " cards in hand.\n");
				this.table.printMsg(playerList.get(0).getName()+" has " + playerList.get(0).getNumOfCards() + " cards in hand.\n");
				result += playerList.get(0).getName()+" has " + playerList.get(0).getNumOfCards() + " cards in hand.\n";
			}
					
					
			if (playerList.get(1).getNumOfCards() == 0) {
				System.out.println(playerList.get(1).getName()+" wins the game.\n");
				this.table.printMsg(playerList.get(1).getName()+" wins the game.\n");
				result += playerList.get(1).getName()+" wins the game.\n";
			}
			else {
				System.out.println(playerList.get(1).getName()+" has " + playerList.get(1).getNumOfCards() + " cards in hand.\n");
				this.table.printMsg(playerList.get(1).getName()+" has " + playerList.get(1).getNumOfCards() + " cards in hand.\n");
				result += playerList.get(1).getName()+" has " + playerList.get(1).getNumOfCards() + " cards in hand.\n";
			}
					
					
			if (playerList.get(2).getNumOfCards() == 0) {
				System.out.println(playerList.get(2).getName()+" wins the game.\n");
				this.table.printMsg(playerList.get(2).getName()+" wins the game.\n");
				result += playerList.get(2).getName()+" wins the game.\n";
			}
			else {
				System.out.println(playerList.get(2).getName()+" has " + playerList.get(2).getNumOfCards() + " cards in hand.\n");
				this.table.printMsg(playerList.get(2).getName()+" has " + playerList.get(2).getNumOfCards() + " cards in hand.\n");
				result += playerList.get(2).getName()+" has " + playerList.get(2).getNumOfCards() + " cards in hand.\n";
			}
					
					
			if (playerList.get(3).getNumOfCards() == 0) {
				System.out.println(playerList.get(3).getName()+" wins the game.\n");
				this.table.printMsg(playerList.get(3).getName()+" wins the game.\n");
				result += playerList.get(3).getName()+" wins the game.\n";
			}
			else {
				System.out.println(playerList.get(3).getName()+" has " + playerList.get(3).getNumOfCards() + " cards in hand.\n");
				this.table.printMsg(playerList.get(3).getName()+" has " + playerList.get(3).getNumOfCards() + " cards in hand.\n");
				result += playerList.get(3).getName()+" has " + playerList.get(3).getNumOfCards() + " cards in hand.\n";
			}
			
			JOptionPane.showMessageDialog(null,result);
			//send a message of ready
			sendMessage(new CardGameMessage(4, -1, null));
		}
		
	}
	
	@Override
	public boolean endOfGame() {
		for (int i=0; i<4; i++) {
			if(this.playerList.get(i).getNumOfCards() == 0) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * an inner class that implements the Runnable interface and handle messages from the server
	 * @author SIU
	 *
	 */
	class ServerHandler implements Runnable{

		@Override
		public void run() {
			try {
				ObjectInputStream streamReader = new ObjectInputStream(sock.getInputStream());

				// read messages from the server
				while (streamReader != null) {
					//handle the incoming message
					parseMessage((GameMessage) streamReader.readObject());
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}			
		}
		
	}
	
	
	/**
	 * a method for creating an instance of BigTwoClient
	 * @param args null
	 */
	public static void main(String[] args) {
		BigTwoClient client = new BigTwoClient();
	}
	
	
	/**
	 * a method for returning a valid hand from the specified list of cards of the player. 
	 * Returns null if no valid hand can be composed from the specified list of cards.
	 * @param player the player who owns the cards
	 * @param cards the cards that we need to check
	 * @return
	 */
	public Hand composeHand(CardGamePlayer player, CardList cards) {
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
}
