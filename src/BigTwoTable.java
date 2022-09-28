import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.*;
import java.net.Socket;

/**
 * A class for a BigTwo card game table (GUI)
 * 
 * @author Siu Ka Sing 3035690373
 * 
 */

public class BigTwoTable implements CardGameTable {

	private BigTwoClient game;     //client of the game
	private boolean[] selected;    //a boolean array indicating which cards are being selected.
	private int activePlayer;    //an integer specifying the index of the active player.
	private JFrame frame;      //the main window of the application.
	private JPanel bigTwoPanel;   //a panel for showing the cards of each player and the cards played on the table.
	private JButton playButton;   //a ¡§Play¡¨ button for the active player to play the selected cards.
	private JButton passButton;   //a ¡§Pass¡¨ button for the active player to pass his/her turn to the next player.
	private JTextArea msgArea;    //a text area for showing the current game status as well as end of game messages.
	public JTextArea chatArea;    //a text area for chatting
	private Image[][] cardImages;    //a 2D array storing the images for the faces of the cards.
	private Image cardBackImage;     //an image for the backs of the cards.
	private Image[] avatars;     //an array storing the images for the avatars.
	private JLabel lastHand;     //label of the last hands played
	private JLabel[] playerTitles;   //label of the titles of players
	private JTextField sendMsg;     //box for inputting chat message
	
	
	/**
	 *  A constructor for creating a BigTwoTable 
	 *  The parameter game is a reference to a card game associates with this table
	 * @param border 
	 */
	public BigTwoTable(BigTwoClient game){
		this.game = game;
		//intiate the selected array
		selected = new boolean[13];
		for (int i=0; i < 13; i++) {
			selected[i] = false;
		}
		
		//intiate the JFrame
		frame = new JFrame("BigTwo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000,700);
		frame.setResizable(false);
		
		//intiate the game panel
		BigTwoPanel bigTwoPanel = new BigTwoPanel();
		frame.add(bigTwoPanel);
		
		//set the menu on the top
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		JMenuItem connect = new JMenuItem("Connect");
		JMenuItem quit = new JMenuItem("Quit");
		menuBar.add(connect);
		menuBar.add(quit);
		connect.addActionListener(new ConnectMenuItemListener());
		quit.addActionListener(new QuitMenuItemListener());
		
		//set the text area
		JPanel textArea= new JPanel();
		textArea.setLayout(new BoxLayout(textArea, BoxLayout.Y_AXIS));
		textArea.setSize(new Dimension(380,450));
		frame.add(textArea,BorderLayout.EAST);
		
 		//set the message area
		msgArea = new JTextArea();
		JScrollPane sp = new JScrollPane(msgArea);
		sp.setPreferredSize(new Dimension(380, 250));
		msgArea.setEditable(false);
		msgArea.append(" Welcome to the BigTwo Game!!!   >3< \n");
		textArea.add(sp);
		
		//set the chat area
		chatArea = new JTextArea();
		JScrollPane cp = new JScrollPane(chatArea);
		cp.setPreferredSize(new Dimension(380, 200));
		chatArea.setEditable(false);
		chatArea.append("---------------------------------------Chat Room---------------------------------------\n");
		textArea.add(cp);
		
		//set the send message box
		JPanel typeMsg = new JPanel();
		typeMsg.setLayout(new BoxLayout(typeMsg, BoxLayout.X_AXIS));
		typeMsg.setSize(new Dimension(380,50));
		textArea.add(typeMsg);
		JLabel Msg = new JLabel();
		Msg.setText("Message: ");
		typeMsg.add(Msg);
		sendMsg = new JTextField();
		sendMsg.setMaximumSize(new Dimension(380,50));
		typeMsg.add(sendMsg);
		// Register a listener with the textfield
	    sendMsg.addActionListener(new TextFieldListener());
			
		//set the play and pass buttons
		playButton = new JButton("Play");
		passButton = new JButton("Pass");
		playButton.addActionListener(new PlayButtonListener());
		passButton.addActionListener(new PassButtonListener());
		bigTwoPanel.setLayout(new GridBagLayout()); 
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; 
		c.gridy = 0; 
		c.gridwidth = 2; 
		c.weighty = 1.0; 
		c.anchor = GridBagConstraints.SOUTH;
		c.insets = new Insets(0, 0, 5, 200);
		c.ipady = 0;
		bigTwoPanel.add(playButton,c);
		c.insets = new Insets(0, 0, 5, 0);
		bigTwoPanel.add(passButton,c);
		
		//set the player icons
		avatars = new Image[4];
		avatars[0] = new ImageIcon("src/player0.png").getImage();
		avatars[1] = new ImageIcon("src/player1.png").getImage();
		avatars[2] = new ImageIcon("src/player2.png").getImage();
		avatars[3] = new ImageIcon("src/player3.png").getImage();
		
		
		//set the card images
		cardImages =  new Image[4][13];  
		cardImages[0][0] = new ImageIcon("bin/ad.gif").getImage();
		cardImages[1][0] = new ImageIcon("bin/ac.gif").getImage();
		cardImages[2][0] = new ImageIcon("bin/ah.gif").getImage();
		cardImages[3][0] = new ImageIcon("bin/as.gif").getImage();
		cardImages[0][1] = new ImageIcon("bin/2d.gif").getImage();
		cardImages[1][1] = new ImageIcon("bin/2c.gif").getImage();
		cardImages[2][1] = new ImageIcon("bin/2h.gif").getImage();
		cardImages[3][1] = new ImageIcon("bin/2s.gif").getImage();
		cardImages[0][2] = new ImageIcon("bin/3d.gif").getImage();
		cardImages[1][2] = new ImageIcon("bin/3c.gif").getImage();
		cardImages[2][2] = new ImageIcon("bin/3h.gif").getImage();
		cardImages[3][2] = new ImageIcon("bin/3s.gif").getImage();
		cardImages[0][3] = new ImageIcon("bin/4d.gif").getImage();
		cardImages[1][3] = new ImageIcon("bin/4c.gif").getImage();
		cardImages[2][3] = new ImageIcon("bin/4h.gif").getImage();
		cardImages[3][3] = new ImageIcon("bin/4s.gif").getImage();
		cardImages[0][4] = new ImageIcon("bin/5d.gif").getImage();
		cardImages[1][4] = new ImageIcon("bin/5c.gif").getImage();
		cardImages[2][4] = new ImageIcon("bin/5h.gif").getImage();
		cardImages[3][4] = new ImageIcon("bin/5s.gif").getImage();
		cardImages[0][5] = new ImageIcon("bin/6d.gif").getImage();
		cardImages[1][5] = new ImageIcon("bin/6c.gif").getImage();
		cardImages[2][5] = new ImageIcon("bin/6h.gif").getImage();
		cardImages[3][5] = new ImageIcon("bin/6s.gif").getImage();
		cardImages[0][6] = new ImageIcon("bin/7d.gif").getImage();
		cardImages[1][6] = new ImageIcon("bin/7c.gif").getImage();
		cardImages[2][6] = new ImageIcon("bin/7h.gif").getImage();
		cardImages[3][6] = new ImageIcon("bin/7s.gif").getImage();
		cardImages[0][7] = new ImageIcon("bin/8d.gif").getImage();
		cardImages[1][7] = new ImageIcon("bin/8c.gif").getImage();
		cardImages[2][7] = new ImageIcon("bin/8h.gif").getImage();
		cardImages[3][7] = new ImageIcon("bin/8s.gif").getImage();
		cardImages[0][8] = new ImageIcon("bin/9d.gif").getImage();
		cardImages[1][8] = new ImageIcon("bin/9c.gif").getImage();
		cardImages[2][8] = new ImageIcon("bin/9h.gif").getImage();
		cardImages[3][8] = new ImageIcon("bin/9s.gif").getImage();
		cardImages[0][9] = new ImageIcon("bin/td.gif").getImage();
		cardImages[1][9] = new ImageIcon("bin/tc.gif").getImage();
		cardImages[2][9] = new ImageIcon("bin/th.gif").getImage();
		cardImages[3][9] = new ImageIcon("bin/ts.gif").getImage();
		cardImages[0][10] = new ImageIcon("bin/jd.gif").getImage();
		cardImages[1][10] = new ImageIcon("bin/jc.gif").getImage();
		cardImages[2][10] = new ImageIcon("bin/jh.gif").getImage();
		cardImages[3][10] = new ImageIcon("bin/js.gif").getImage();
		cardImages[0][11] = new ImageIcon("bin/qd.gif").getImage();
		cardImages[1][11] = new ImageIcon("bin/qc.gif").getImage();
		cardImages[2][11] = new ImageIcon("bin/qh.gif").getImage();
		cardImages[3][11] = new ImageIcon("bin/qs.gif").getImage();
		cardImages[0][12] = new ImageIcon("bin/kd.gif").getImage();
		cardImages[1][12] = new ImageIcon("bin/kc.gif").getImage();
		cardImages[2][12] = new ImageIcon("bin/kh.gif").getImage();
		cardImages[3][12] = new ImageIcon("bin/ks.gif").getImage();
		cardBackImage = new ImageIcon("bin/b.gif").getImage();
		
		
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 0;
		c.weighty = 0;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.ipady = 0;
		playerTitles = new JLabel[4];
		
		//print the name of the players
		for (int i=0; i< game.getPlayerList().size(); i++){
			c.insets = new Insets(i*120, -150, 0, 0);
			playerTitles[i] = new JLabel();
			playerTitles[i].setBackground(Color.cyan);
			playerTitles[i].setText(this.game.getPlayerList().get(i).getName());
		    bigTwoPanel.add(playerTitles[i], c);
		}
		
		c.insets = new Insets(480, -150, 0, 0);
		lastHand = new JLabel();
		lastHand.setBackground(Color.cyan);
	    bigTwoPanel.add(lastHand, c);
		
    	
    	this.bigTwoPanel = bigTwoPanel;
    	bigTwoPanel.addMouseListener(new BigTwoPanel());
		frame.setVisible(true);
	}

	
	/**
	 * an inner class that extends the JPanel class and implements the MouseListener interface. 
	 * Overrides the paintComponent() method inherited from the JPanel class to draw the card game table. 
	 * Implements the mouseClicked() method from the MouseListener interface to handle mouse click events. 
	 * @author SIU
	 */
	class BigTwoPanel extends JPanel implements MouseListener{
		public void paintComponent(Graphics g) {
			Graphics2D g2D = (Graphics2D) g;
			g.setColor(Color.BLACK);
			g.fillRect (0, 0, 600, 500);
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect (0, 1000, 600, 50);
			g.setColor(Color.cyan);
			g.fillRect (0, 0, 600, 119);
			g.fillRect (0, 120, 600, 119);
			g.fillRect (0, 240, 600, 119);
			g.fillRect (0, 360, 600, 119);
			g.fillRect (0, 480, 600, 119);
			
			
			//draw the player icons
			g.drawImage(avatars[0], 5, 40, this);
			g.drawImage(avatars[1], 5, 150, this);
			g.drawImage(avatars[2], 5, 270, this);
			g.drawImage(avatars[3], 5, 390, this);
			
			
			//print the cards
	    	for (int i=0; i< game.getPlayerList().size(); i++){
				CardGamePlayer current = game.getPlayerList().get(i);
			    if (game.getPlayerID() == i) {
			    	for (int j=0; j < current.getNumOfCards(); j++) {
			    		if (selected[j] == true) {
			    			g2D.drawImage(cardImages[current.getCardsInHand().getCard(j).getSuit()][current.getCardsInHand().getCard(j).getRank()], 130+j*30, 10+i*120, this);
			    		}
			    		else {
			    			g2D.drawImage(cardImages[current.getCardsInHand().getCard(j).getSuit()][current.getCardsInHand().getCard(j).getRank()], 130+j*30, 20+i*120, this);
			    		}
			    	}
			    }
			    else {
			    	for (int j=0; j < current.getNumOfCards(); j++) {
			    		g2D.drawImage(cardBackImage, 130+j*30, 20+i*120, this);
			    	}
			    }
			}
	    	
	    	//print last hands on table
	    	if (game.getHandsOnTable().size() != 0) {
	    		for (int i=0; i < game.getHandsOnTable().get(game.getHandsOnTable().size()-1).size(); i++) {
	    			g2D.drawImage(cardImages[game.getHandsOnTable().get(game.getHandsOnTable().size()-1).getCard(i).suit][game.getHandsOnTable().get(game.getHandsOnTable().size()-1).getCard(i).rank], 130+i*30, 500, this);
	    		}
	    	}
		}

		public void mouseClicked(MouseEvent e) {
			CardGamePlayer current = game.getPlayerList().get(activePlayer);
			if (activePlayer == game.getPlayerID()) {
				Rectangle bounds = new Rectangle(130, 20 + activePlayer*120, 30, 97);
	            for (int i=0; i < current.getNumOfCards(); i++) {
	            	//if the card is not selected before
	            	if (selected[i] == false) {
	            		if (i == current.getNumOfCards()-1) {  //rightmost card
	            			bounds = new Rectangle(130 + 30*i, 20 + activePlayer*120, 73, 97);
	            		}
	            		else {
	            			bounds = new Rectangle(130 + 30*i, 20 + activePlayer*120, 30, 97);
	            		}
	            		//check if clicked on the cards
						if (bounds.contains(e.getX(),e.getY())) {
							selected[i] = true;
			                frame.repaint();
						}
	            	}
	            	//if the card is selected before
					else {
						if (i == current.getNumOfCards()-1) {  //rightmost card
	            			bounds = new Rectangle(130 + 30*i, 10 + activePlayer*120, 73, 97);
	            		}
	            		else {
	            			bounds = new Rectangle(130 + 30*i, 10 + activePlayer*120, 30, 97);
	            		}
						//check if clicked on the cards
						if (bounds.contains(e.getX(),e.getY())) {
							selected[i] = false;
			                frame.repaint();
						}
						
						
						//check the small rectangle part the card that is above its right card
						if (i != current.getNumOfCards()-1) {  //not rightmost card
							if (selected[i+1]==false) {  //if its right card is not selected
								bounds = new Rectangle(130 + 30*(i+1), 10 + activePlayer*120, 73, 10);
								//check if clicked on the cards
								if (bounds.contains(e.getX(),e.getY())) {
									selected[i] = false;
					                frame.repaint();
								}
							}
	            		}
						
		            }
	            }
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
	
	
	/**
	 * an inner class that implements the ActionListener interface. 
	 * Implements the actionPerformed() method from the ActionListener interface to handle button-click events for the ¡§Play¡¨ button.
	 * When the ¡§Play¡¨ button is clicked, makeMove() method of the CardGame object is called to make a move
	 * @author SIU
	 *
	 */
	class PlayButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (getSelected().length == 0) {
				msgArea.append("{empty}  <== Not a legal move!!!!\n");
				frame.repaint();
			}
			else {
				game.makeMove(activePlayer, getSelected());
				//update the hands on table shown
				if (game.getHandsOnTable().size() != 0) {
					lastHand.setText("Played by " + game.getHandsOnTable().get(game.getHandsOnTable().size()-1).getPlayer().getName());
				}
				//change the background of the current active player
				for (int i=0; i<4; i++) {
					playerTitles[i].setForeground(Color.black);
					if (activePlayer == i) {
						playerTitles[i].setForeground(Color.red);
					}
				}
				resetSelected();
				disable();
				frame.repaint();
			}
		}	
	}
	
	
	/**
	 * an inner class that implements the ActionListener interface. 
	 * Implements the actionPerformed() method from the ActionListener interface to handle button-click events for the ¡§Pass¡¨ button. 
	 * When the ¡§Pass¡¨ button is clicked, makeMove() method of the CardGame object is called to make a move
	 * @author SIU
	 *
	 */
	class PassButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (game.getHandsOnTable().size() == 0) {
				msgArea.append("{pass}  <== Not a legal move!!!!\n");
				resetSelected();
				frame.repaint();
			}
			else {
				game.makeMove(activePlayer, null);
				//update the hands on table shown
				if (game.getHandsOnTable().size() != 0) {
					lastHand.setText("Played by " + game.getHandsOnTable().get(game.getHandsOnTable().size()-1).getPlayer().getName());
				}
				//change the background of the current active player
				for (int i=0; i<4; i++) {
					playerTitles[i].setForeground(Color.black);
				}
				for (int i=0; i<4; i++) {
					if (activePlayer == i) {
						playerTitles[i].setForeground(Color.red);
					}
				}
				resetSelected();
				disable();
				frame.repaint();
			}
		}	
	}
	
	
	/**
	 * an inner class that implements the ActionListener interface. 
	 * Implements the actionPerformed() method from the ActionListener interface to handle menu-item-click events for the ¡§Connect¡¨ menu item. 
	 * When the ¡§Connect¡¨ menu item is selected, we will establish a connection to the game server.
	 * @author SIU
	 *
	 */
	class ConnectMenuItemListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			game.makeConnection();
		}		
	}
	
	
	/**
	 * an inner class that implements the ActionListener interface. 
	 * Implements the actionPerformed() method from the ActionListener interface to handle menu-item-click events for the ¡§Quit¡¨ menu item. 
	 * When the ¡§Quit¡¨ menu item is selected, terminate the application.
	 * @author SIU
	 *
	 */
	class QuitMenuItemListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}	
	}


	/**
	 * Sets the index of the active player (i.e., the current player).
	 * 
	 * @param activePlayer
	 *            an int value representing the index of the active player
	 */
	public void setActivePlayer(int activePlayer) {
		if (activePlayer < 0 || activePlayer >= game.getPlayerList().size()) {
			this.activePlayer = -1;
		} else {
			this.activePlayer = activePlayer;
		}
		for (int i=0; i<4; i++) {
			playerTitles[i].setForeground(Color.black);
			playerTitles[i].setText(this.game.getPlayerList().get(i).getName());
			if (activePlayer == i) {
				playerTitles[i].setForeground(Color.red);
			}
		}
		if (game.getHandsOnTable().size() != 0) {
			lastHand.setText("Played by " + game.getHandsOnTable().get(game.getHandsOnTable().size()-1).getPlayer().getName());
		}
		if (activePlayer == game.getPlayerID()) {
			enable();
		}
		else {
			disable();
		}
		frame.repaint();
	}

	
	/**
	 * Returns an array of indices of the cards selected.
	 * 
	 * @return an array of indices of the cards selected
	 */
	public int[] getSelected() {
		//count the number of selected cards
		int numOfSelected = 0;
		for (int i=0; i < game.getPlayerList().get(activePlayer).getNumOfCards(); i++) {
			if (selected[i] == true) {
				numOfSelected++;
			}
		}
		
		//intiate the output array
		int[] selectedIdx = new int[numOfSelected];
		int currentIdx = 0; 
		for (int i=0; i < game.getPlayerList().get(activePlayer).getNumOfCards(); i++) {
			if (selected[i] == true) {
				selectedIdx[currentIdx] = i;
				currentIdx++;
			}
		}
		
		return selectedIdx;
		
	}

	
	/**
	 * Resets the list of selected cards to an empty list.
	 */
	public void resetSelected() {
		this.selected = new boolean[13];
	}

	
	/**
	 * Repaints the GUI.
	 */
	public void repaint() {
		frame.repaint();
	}

	
	/**
	 * Prints the specified string to the message area of the card game table.
	 * 
	 * @param msg
	 *            the string to be printed to the	 message area of the card game
	 *            table
	 */
	public void printMsg(String msg) {
		msgArea.append(msg);
	}

	
	/**
	 * Clears the message area of the card game table.
	 */
	public void clearMsgArea() {
		msgArea.setText(null);
		
		//print the welcoming 
		msgArea.append(" Welcome to the BigTwo Game!!!   >3< \n");
	}

	/**
	 * Resets the GUI.
	 */
	public void reset() {
		//reset the list of selectedcards
		resetSelected();
		
		//clear the message area 
		clearMsgArea();
		
		//enable user interactions
		enable();
	}

	/**
	 * Enables user interactions.
	 */
	public void enable() {
		//set the buttons clickable
		playButton.setEnabled(true);
		passButton.setEnabled(true);
		
		//enable the BigTwoPanel for selection of cards
		bigTwoPanel.setEnabled(true);
	}

	/**
	 * Disables user interactions.
	 */
	public void disable() {
		//set the buttons unclickable
		playButton.setEnabled(false);
		passButton.setEnabled(false);
		
		//disable the BigTwoPanel for selection of cards
		bigTwoPanel.setEnabled(false);
	}
	
	/**
	 * an inner class for handling the message send to the chat box
	 * @author SIU
	 *
	 */
	class TextFieldListener implements ActionListener{  
    	public void actionPerformed(ActionEvent evt){  
    		String msg = sendMsg.getText();
    		game.sendMessage(new CardGameMessage(7, -1, msg));
    		sendMsg.setText("");
       }
    }
}
