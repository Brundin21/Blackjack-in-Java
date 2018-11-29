/*	Juan C. Leon
 *  8/5/118
 *  COP2552.0M1
 *  Java SE 1.8
 *  
 *  Blackjack. Single player version of Blackjack.
 * 
 * 	House rounds down on fractions of a dollar.
 * 	House hits until 17 or higher.
 * 	Payout is 1.5 * bet for Natural 21's
 * */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;


public class Blackjack extends Application{

//****************************** Global Variables ******************************
	private Pane table;
	private Pane playersCardPane = new Pane();			//pane for the player's cards	
	private Pane dealersCardPane = new Pane();			//pane for the dealer's cards
	//private Deck deck = new Deck();						//the deck of cards
	private Deck deck;										//the deck of cards
	private ArrayList<Card> playersHand = new ArrayList<Card>();	//the player's current hand
	private ArrayList<Card> dealersHand = new ArrayList<Card>();	//the dealer's current hand
	private int playerTotal = 0;	//total value of the player's hand
	private int dealerTotal = 0;	//total value of the dealer's hand
	
	private String userName;
	private Label nameLabel = new Label();	
	private int userChips = 0;
	private int wager = 0;
	private int payout = 0;
	
	private ScrollPane sP = new ScrollPane();
	private HBox playersButtonPane;
	private FlowPane wagerFlowPane = new FlowPane();
	private TextFlow tf = new TextFlow();
	private Label mainMessage = new Label();
	private Label chipsAmmountLabel = new Label(userChips + "");
	
	private GameData data = new GameData();
	private Stage top10Stage = new Stage();

//****************************** Private Methods ******************************
	private void setUserName(String name){
		userName = name;
		
		nameLabel.setText(userName);
	}
	
	private void setUserChips(int ammount){
		userChips = ammount;
		chipsAmmountLabel.setText(userChips + "");
	}
	
	private void payout(int ammount){
		payout = ammount;
		
		userChips = userChips + payout;
		chipsAmmountLabel.setText(userChips + "");
	}
	
	private void addHistory(String s, String winLose){
		Color c = Color.BLACK;
		Text t = new Text(s +"\n");
		
		if(winLose.equals("WIN")){
			c = Color.BLACK;
		}else if(winLose.equals("EVEN")){
			c = Color.CADETBLUE;
		}else if(winLose.equals("LOSE")){
			c = Color.CRIMSON;
		}
		
		t.setFill(c);
		tf.getChildren().add(t);
		
		sP.setVvalue(1.0);		//Get scroll pane to automatically scroll to the last entry
	}
	
	private void setMessage(String s){
		mainMessage.setStyle("-fx-font-size: 26;"
	//			+ "-fx-text-fill: #FFA500;"
				+ "-fx-font-weight: bold;");
		mainMessage.setText(s);
	}
	
	private void hit(){
		playersHand.add(deck.drawCard());
		displayHand(playersHand, playersCardPane);
		
		playerTotal = getCardTotal(playersHand);
		
		if(playerTotal > 21){
			bust();
			playersButtonPane.setDisable(true);				
		}
	}
	
	private void bust(){
		setMessage("You Bust");
		addHistory("You lose: " + wager, "LOSE");
		payout(-wager);
		wagerFlowPane.setDisable(false);
	}
	
	//************ Dealer's play mechanics ************
	private void dealersTurn(){
		boolean dealersTurn = true;
		
		displayHand(dealersHand, dealersCardPane);
		
		dealerTotal = getCardTotal(dealersHand);
		
		if(dealerTotal == 21){
			setMessage("Dealer Blackjack");
			addHistory("Dealer Blackjack. You lose: " + wager, "LOSE");
			payout(-wager);
			
			dealersTurn = false;			
		}
		
		while(dealersTurn){
			dealerTotal = getCardTotal(dealersHand);			
			//System.out.println("Dealer: " + dealerTotal);  //TODO remove
			
			if(dealerTotal < 17){
				dealersHand.add(deck.drawCard());
				displayHand(dealersHand, dealersCardPane);
				
			}else if(dealerTotal >= 17 && dealerTotal <= 21){
				
				if(dealerTotal == playerTotal){
					setMessage("Stand-off");
					addHistory("Stand-off", "EVEN");
					
				}else if(dealerTotal > playerTotal){
					setMessage("Dealer wins with " + dealerTotal);
					addHistory("Dealer wins. You lose: " + wager, "LOSE");
					payout(-wager);
					
				}else if(dealerTotal < playerTotal){
					setMessage("You win with " + playerTotal);
					addHistory("You win: " + wager, "WIN");
					payout(wager);
				}
				
				
				dealersTurn = false;
				//System.out.println("Dealer's Total: " + dealerTotal);	//TODO remove				
			}
			else if(dealerTotal > 21){
				setMessage("Dealer Busts");
				addHistory("You win: " + wager, "WIN");
				payout(wager);
				
				dealersTurn = false;
				//System.out.println("Dealer Bust:" + dealerTotal);		//TODO remove
			}
		}
		
		wagerFlowPane.setDisable(false);
	}
	
	//Do this if the player has a blackjack
	private void natural21(){
		if(getCardTotal(dealersHand) != 21){
			setMessage("Blackjack");
			
			int ammount = (int) ((int)wager * 1.5);
			payout(ammount);			
			
			addHistory("Blackjack! You win: " + payout, "WIN");
		}
		else if(getCardTotal(dealersHand) == 21){
			setMessage("Dealer has Blackjack too.");
			payout(wager);
			addHistory("Both you and dealer have Blackjack. Stand-off", "EVEN");	
		}
		
		displayHand(dealersHand, dealersCardPane);
		wagerFlowPane.setDisable(false);
	}
	

	private void discard(){
		/* Process for discarding cards into the discard pile.
		 * Cards on the table are neither in the deck nor in the discard pile.
		 * Only cards in the discard pile get shuffled back into the deck.
		 * Discard pile gets shuffled back when the deck runs out of cards or the user clicks the shuffle button.
		 * */
		for(int i = 0; i < playersHand.size(); i++){	//discard the player's hand
			Card temp = playersHand.get(i);			
			deck.discard(temp);
		}
		
		for(int i = 0; i < dealersHand.size(); i++){	//discard the dealer's hand
			Card temp = dealersHand.get(i);			
			deck.discard(temp);
		}
		
		playersHand.clear();
		dealersHand.clear();
	}
	
	//************ Deal the cards and display them ************
	private void deal(){
		discard();		//discard cards on the table
				
		playersHand.add(deck.drawCard());		//player gets a card
		dealersHand.add(deck.drawCard());		//then dealer gets a card
		playersHand.add(deck.drawCard());		//then player gets a card
		dealersHand.add(deck.drawCard());
		
		displayHand(playersHand, playersCardPane);
		dealerInitDisplay(dealersHand);
		//displayHand(dealersHand, dealersCardPane);
		
		//Determine the players hand value right away
		playerTotal = getCardTotal(playersHand);
		if(playerTotal == 21){					//natural 21
			playersButtonPane.setDisable(true);
			natural21();
		}		
	}
	
	//The dealer's initial hand display, with the second card face down.
	private void dealerInitDisplay(ArrayList<Card> hand){		
		ImageView back = new ImageView();
		
		try {
			Image backImage = new Image(new FileInputStream("images/BACK.png"));
			back = new ImageView(backImage);
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}			
		dealersCardPane.getChildren().clear();
		dealersCardPane.getChildren().add(hand.get(0));
		back.setLayoutX(24);
		back.setLayoutY(4);
		dealersCardPane.getChildren().add(back);		
	}
	
	//Displays the cards in the hand
	//Used for both the dealer and player, calling the method requires the hand with the cards and the pane to update
	private void displayHand(ArrayList<Card> hand, Pane pane){		
		pane.getChildren().clear();
		
		int x = 0, y = 0;
		
		for(int i = 0; i < hand.size(); i++){
			Card temp;
			
			temp = hand.get(i);
			temp.setLayoutX(x);		//to stagger the cards
			temp.setLayoutY(y);
			
			pane.getChildren().add(temp);
			x+=24;
			y+=4;
		}
	}
	

	
	//This methods just calculates and returns the total value of the cards in the hand.
	//It takes no action based on the total.
	private int getCardTotal(ArrayList<Card> hand){
		/*	So the concept here is:
		 *    Each card has an integer value assigned to it when the card is created (intValue), face cards are 10 and only the Ace is worth 11
		 *    So I get all the intValues into an array and sort it in increasing value.  The order I add the cards doesn't matter because they
		 *       all get added.  I sort them to make sure sure the Ace's get added last.  This bit is crucial to my method.
		 *    So I take the lowest value and add it to the total, if it's an Ace then I add 11.
		 *    Then I see if the next card is an Ace (intValue of 11).
		 *    	If it is an Ace, I try adding 11 to the total.  If the new total is 21 or less, then that Ace is an 11.
		 *    	If the new total is more than 21, count the Ace as 1.
		 *    		Possible scenarios:
		 *    			Ace and Aces: 11 + 11 = 22 so, the first Ace is 11 and following Aces are 1
		 *    			(card/cards totaling <10) and an Ace:  new total is less than 21 so Ace is 11
		 *    			(card/cards totaling 10) and an Ace:  10 + 11 = 21 so the Ace is counted as 11
		 *    			(card/cards totaling 11 or more) and an Ace:  all following Aces will be counted as 1
		 *    	If the next card is not an Ace, then just add it to the running total.
		 *    
		 *    Then return the total. 
		 * 
		 * */
		int total = 0;
		int[] intValueArray = new int[hand.size()];
		
		for (int i = 0; i < hand.size(); i++){				//Get the int values of the cards in the hand
			intValueArray[i] = hand.get(i).getIntValue();
		}
		
		Arrays.sort(intValueArray);							//Sort the values from lowest to highest
		
		total+=intValueArray[0];							//store the value of the lowest card into total	
		
		for (int i = 0; i < hand.size()-1; i++){				//loop through the rest of the cards in the hand
			if(intValueArray[i+1] == 11){					//if the card is an ace (intValue of 11)
				if((total + 11) <= 21){						//add 11 to current total, if the new total is 21 or less 
					total+=11;									//count the ace as 11
				}else{
					total+=1;									//count the ace as 1
				}
			}
			else{
				total+=intValueArray[i+1];
			}
		}
		//System.out.println(Arrays.toString(intValueArray));  
		
		return total;
	}
	

//********************** Methods for creating the visual components **********************
	//Pane containing player's cards and buttons 
	private HBox createCardsButtonPane(){
		HBox pane = new HBox(5);
		
		//Hit and Stand Buttons
		Button hit = new Button("Hit");
		hit.setPrefWidth(60.0);
		Button stand = new Button("Stand");		
		stand.setPrefWidth(60.0);

		pane.getChildren().addAll(hit, stand);

//Hit Button ==>		
		hit.setOnMouseClicked(e-> {		
			hit();						
		});
		
//Stand Button ==>
		stand.setOnMouseClicked(e-> {
			pane.setDisable(true);
			dealersTurn();				
		});
		
		return pane;
		
	}
	
	//Creates the white rectangles where the dealer's and player's cards go
	private Group createRectangle(String s){
		Group g = new Group();
		g.setStyle("-fx-border-color: black;");
		
		Rectangle r = new Rectangle(120, 184);	//cards are 98x153 pixels, 1.56*width
		r.setFill(Color.TRANSPARENT);
		r.setStroke(Color.WHITESMOKE);
		r.setStrokeWidth(5);
		r.setArcWidth(15);
		r.setArcHeight(15);
		
		Label l = new Label(s);
		l.setRotate(-90);
		l.setLayoutX(-75);
		l.setLayoutY(80);
		l.setStyle("-fx-text-fill: #F5F5F5; -fx-font-size: 26; -fx-font-weight: 800;");
		
		g.getChildren().addAll(l,r);
		
		return g;
	}
	
	//Creates the pane where a history of the current game will be displayed
	private Group createBottomLeftPane(){
		Group g2 = new Group();		//group containing bottom left pane
		double g2Height = 180;		//height of the group
		double sPWidth = 300;
		
		//scroll pane to display a running history of the game
				
		sP.setPrefSize(sPWidth, g2Height);
		sP.setMaxSize(sPWidth, g2Height);
		sP.setFitToHeight(true);
		sP.setFitToWidth(true);
		sP.setStyle("-fx-border-color: black;"
						+ "-fx-border-width: 2;"
						+ "-fx-border-radius: 3;");
		
		tf.setPadding(new Insets(10));
		tf.setStyle("-fx-background-color: #F5F5F5;");
		
		sP.setContent(tf);
		
		double wagerPaneWidth = 200;		//width of the pane with the wager slider
		
		//Pane holding all the wagering elements
		wagerFlowPane.setDisable(true);			//is disabled until player starts the game
		wagerFlowPane.setLayoutX(sPWidth-1);
		wagerFlowPane.setPrefSize(wagerPaneWidth, g2Height);
		wagerFlowPane.setStyle("-fx-border-color: black;"
				+ "-fx-border-width: 2;"
				+ "-fx-border-radius: 3;"
				+ "-fx-background-color: #F5F5F5;");
		
		//Display the name of the person playing		
		nameLabel.setPrefWidth(wagerPaneWidth);
		nameLabel.setAlignment(Pos.CENTER);
		nameLabel.setPadding(new Insets(10));
		nameLabel.setStyle("-fx-font-size: 16;");
		
		//Labels to display how many chips a player has
		Label chipsLabel = new Label("Chips:");
			chipsLabel.setPrefWidth((wagerPaneWidth/3)*2);
			chipsLabel.setAlignment(Pos.CENTER_RIGHT);
			chipsLabel.setStyle("-fx-font-size: 16");
		
		chipsAmmountLabel.setPrefWidth((wagerPaneWidth/3) - 1);
		chipsAmmountLabel.setAlignment(Pos.CENTER);
		chipsAmmountLabel.setStyle("-fx-font-size: 16");
		
		//Labels to display how much is being wagered
		Label wagerLabel = new Label("Wager:");
			wagerLabel.setPrefWidth((wagerPaneWidth/3)*2);
			wagerLabel.setAlignment(Pos.CENTER_RIGHT);
			wagerLabel.setStyle("-fx-font-size: 16");
		Label wagerField = new Label("5");
			wagerField.setPrefWidth((wagerPaneWidth/3) - 1);
			wagerField.setAlignment(Pos.CENTER);
			wagerField.setStyle("-fx-background-color: #F5F5F5;"
					+ "-fx-font-size: 16");
		
		//Slider to set the wager
		Slider wagerSlider = new Slider(5,100,1);
			wagerSlider.setPrefWidth(wagerPaneWidth);
			wagerSlider.setPadding(new Insets(10));
			wagerSlider.setShowTickLabels(true);
			wagerSlider.setShowTickMarks(true);
			wagerSlider.setMajorTickUnit(25);
			wagerSlider.setMinorTickCount(5);
		
		wagerSlider.valueProperty().addListener((ov, oldVal, newVal) -> {		//So the slider only deals with int values
			int value = (int)Math.round(newVal.doubleValue());
			wagerSlider.setValue(value);
			wagerField.setText(value + "");		//display the amount to be wagered
		});
		
		//Bet button and stackpane to center the button
		Button betButton = new Button("Bet");		
			betButton.setPrefWidth(100);
		StackPane betPane = new StackPane();
			betPane.setAlignment(Pos.CENTER);
			betPane.setPrefWidth(wagerPaneWidth);
			betPane.getChildren().add(betButton);

//Bet Button==>			
		betButton.setOnAction(e -> {
			wagerFlowPane.setDisable(true);
			playersButtonPane.setDisable(false);
			wager = (int) wagerSlider.getValue();
			setMessage("");
			deal();
		});
		
		
		wagerFlowPane.getChildren().addAll(nameLabel, chipsLabel, chipsAmmountLabel, wagerLabel, wagerField, wagerSlider,betPane);
		
		g2.getChildren().add(sP);
		g2.getChildren().add(wagerFlowPane);
		
		return g2;
	}
	
	//Creates the login pane where the player types his name and selects how many decks to use;
	private FlowPane createLoginPane(){
		FlowPane pane = new FlowPane();
			pane.setPrefSize(533, 400);
			pane.setAlignment(Pos.TOP_CENTER);
			pane.setStyle("-fx-background-color: #F5F5F5;"
					+ "-fx-border-color: black;"
					+ "-fx-border-width: 2;");
		
		Label welcomeLabel = new Label("Welcome to Blackjack!");
			welcomeLabel.setStyle("-fx-font-size: 25");
			welcomeLabel.setPrefWidth(533);
			welcomeLabel.setAlignment(Pos.CENTER);
			welcomeLabel.setPadding(new Insets(50,0,20,0));
		pane.getChildren().add(welcomeLabel);
		
		Label nameLabel = new Label("What is your name?");
			nameLabel.setStyle("-fx-font-size: 16;");
			nameLabel.setPrefWidth(533);
			nameLabel.setPadding(new Insets(20));
			nameLabel.setAlignment(Pos.CENTER);
		pane.getChildren().add(nameLabel);
				
		TextField nameField = new TextField();
			nameField.setMaxWidth(300);
			nameField.setStyle("-fx-font-size: 15;");
		StackPane nfStackPane = new StackPane();
			nfStackPane.setAlignment(Pos.CENTER);
			nfStackPane.getChildren().add(nameField);
		pane.getChildren().add(nfStackPane);
		
		Label decksCBLabel = new Label("How many decks would you like to play with?");
			decksCBLabel.setStyle("-fx-font-size: 16;");
			decksCBLabel.setPadding(new Insets(20));
		pane.getChildren().add(decksCBLabel);
		
		ComboBox<Integer> decksComboBox = new ComboBox<Integer>();
			decksComboBox.getItems().addAll(1, 2, 3, 4, 5, 6);
			decksComboBox.getSelectionModel().selectFirst();
		pane.getChildren().add(decksComboBox);
		
		Button startButton = new Button("Start");
			startButton.setPrefWidth(150);
			startButton.setStyle("-fx-font-size: 16;");
		pane.getChildren().add(startButton);

//Start Button ==>
		startButton.setOnAction(e->{
			String name = nameField.getText();
			
			if(!name.isEmpty()){
				if(data.exists(name)){
					setMessage("Welcome back " + name);
				}else{
					setMessage("Hello, " + name + ".\nAs a new player you have been given 1000 chips.");
				}
				setUserName(name);
				setUserChips(data.getChips(name));		
				
				deck = new Deck(decksComboBox.getValue());
				createDeckImage();
				wagerFlowPane.setDisable(false);
				
				pane.setManaged(false);
				pane.setVisible(false);
			}
		});
		
		return pane;
	}
	
	//Create the image of the deck and the shuffle button
	private void createDeckImage(){		
		Group deckImageGroup = new Group();
			deckImageGroup.setLayoutX(50);
			deckImageGroup.setLayoutY(50);		
		Pane deckImage = deck.getDeckImage();
			deckImageGroup.getChildren().add(deckImage);

//Shuffle Button ==>		
		Button shuffleButton = new Button("Shuffle");
			shuffleButton.prefWidth(150);
			shuffleButton.setLayoutX(20);
			shuffleButton.setLayoutY(170);
		deckImageGroup.getChildren().add(shuffleButton);
		table.getChildren().add(deckImageGroup);
		
		shuffleButton.setOnAction(e -> {
			deck.shuffle();
		});
	}
	
	//Create a window for the Top Ten List
	private void createTopTen(){
		List<UserData> topTen = new ArrayList<UserData>();
		
		topTen = data.getTopTen();	//get the top players
		try{
			FlowPane root = new FlowPane();
				root.setPrefSize(380,380);
			
			Label top10Label = new Label("Top 10");
				top10Label.setStyle("-fx-font-size: 22;"
						+ "-fx-font-weight: bold;");
				top10Label.setPrefWidth(root.getPrefWidth());
				top10Label.setAlignment(Pos.CENTER);
				top10Label.setPadding(new Insets(20));
			root.getChildren().add(top10Label);
			
			Label nameLabel = new Label("Name");					
				nameLabel.setStyle("-fx-font-size: 16;"
						+ "-fx-font-weight: bold;");
				nameLabel.setPrefWidth((root.getPrefWidth()/4) * 3);
				nameLabel.setPadding(new Insets(5,0,0,40));
			Label chipsLabel = new Label("Chips");
				chipsLabel.setStyle("-fx-font-size: 16;"
						+ "-fx-font-weight: bold;");
				chipsLabel.setPrefWidth((root.getPrefWidth()/4)-1);
			root.getChildren().addAll(nameLabel, chipsLabel);
			
			//Add the names of the top players
			for(UserData user: topTen){
				Label tempName = new Label(user.getName());					
					tempName.setStyle("-fx-font-size: 16;");
					tempName.setPrefWidth((root.getPrefWidth()/4) * 3);
					tempName.setPadding(new Insets(5,0,0,40));
				Label tempChips = new Label(user.getChips()+"");
					tempChips.setStyle("-fx-font-size: 16;");
					tempChips.setPrefWidth((root.getPrefWidth()/4)-1);
				
				root.getChildren().addAll(tempName, tempChips);
			}
			
			top10Stage.setTitle("Top 10");
			top10Stage.setResizable(false);
			top10Stage.setScene(new Scene(root));
			top10Stage.show();
				
		}catch(Exception e){
			e.printStackTrace();
		}
			
	}
	
	//******* Main pane representing the playing table ******* 
	private Pane createTable(){
		double playerX = 709;	//X coordinate of the player's play area
		double playerY = 470;	//Y coordinate of the player's play area
		
		//Create the playing table
		Pane tablePane = new AnchorPane();
		
		//Create the dealer's card rectangle on the table
		Group dealersR = createRectangle("DEALER");
			dealersR.setLayoutX(454);
			dealersR.setLayoutY(40);
		tablePane.getChildren().add(dealersR);
		
		//Place the pane where the dealer's cards are shown
			dealersCardPane.setLayoutX(466);	//start of rectangle + 16
			dealersCardPane.setLayoutY(56);
		tablePane.getChildren().add(dealersCardPane);
		
		//Create the player's cards rectangle on the table 
		Group playersR = createRectangle("PLAYER");
			playersR.setLayoutX(playerX);
			playersR.setLayoutY(playerY);
		tablePane.getChildren().addAll(playersR);
		
		//Place the Pane where the player's cards are shown
			playersCardPane.setLayoutX(playerX + 12);
			playersCardPane.setLayoutY(playerY + 16);
		tablePane.getChildren().add(playersCardPane);
		
		//Create the pane with the hit and stand buttons
		playersButtonPane = createCardsButtonPane();
			playersButtonPane.setLayoutX(playerX - 1);
			playersButtonPane.setLayoutY(playerY + 200);
			playersButtonPane.setDisable(true);
		tablePane.getChildren().add(playersButtonPane);
		
		//Creates the bottom left pane with game history
		Group bottomLeftPane = createBottomLeftPane();
			bottomLeftPane.setLayoutX(50);
			bottomLeftPane.setLayoutY(480);
		tablePane.getChildren().add(bottomLeftPane);
		
		//Label for displaying messages
		StackPane messagePane = new StackPane();
			messagePane.setAlignment(Pos.CENTER);
			messagePane.setLayoutX(0);
			messagePane.setLayoutY(320);
			messagePane.setPrefWidth(1024);
			messagePane.getChildren().add(mainMessage);
		tablePane.getChildren().add(messagePane);
				
		//Create the login pane
		FlowPane loginPane = createLoginPane();
			loginPane.setLayoutX(245);
			loginPane.setLayoutY(184);
		tablePane.getChildren().add(loginPane);
		
		//Button for Top 10
		Button top10Button = new Button("Top 10");
			top10Button.setPrefWidth(150);
			top10Button.setStyle("-fx-font-size: 16");
			top10Button.setLayoutX(800);
			top10Button.setLayoutY(100);
		tablePane.getChildren().add(top10Button);

//Top10 Button ==>
		top10Button.setOnAction(e -> {
			if(!top10Stage.isShowing()){
				if(userName != null){
					data.setChips(userName, userChips);
				}
				createTopTen();
			}
		});
		
		//The table's properties
		tablePane.setPrefSize(1024.0, 768.0);
		tablePane.setStyle("-fx-background-color: #2EB82E");
		
		
		return tablePane;
	}

//************ Blackjack main components ************
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		//******** Set the table *******
		top10Stage.initOwner(primaryStage);		//so that the top 10 window closes when the parent window closes
												//it's here because it can only be set before the stage is shown
		
		table = createTable();
		
		Scene scene = new Scene(table, 1024, 768);

		primaryStage.setTitle("Blackjack");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();	
			
	}
	
	@Override
	public void stop(){		//write data when the game closes
		if(userName != null){
			data.setChips(userName, userChips);
			data.writeData();
		}
	}
	
	public static void main(String[] args) {
		launch(args);

	}
}
