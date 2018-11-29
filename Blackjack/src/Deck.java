/* Juan Leon
 * 8/5/18
 * COP2552.0M1
 * Java SE 1.8
 * 
 * Class for blackjack.  It represents the deck or decks in the shoe.
 * It also creates an image of the deck displaying the number of cards left in the deck.
 * */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

//Class to represent a deck of playing cards
public class Deck {
	private ArrayList<Card> deck = new ArrayList<Card>();	//the deck of cards
	private ArrayList<Card> discardPile = new ArrayList<Card>();	//list containing discarded cards
	
	private Pane deckImage = new Pane();

//********************** Constructors **********************
	public Deck(int numberOfDecks){
		
		createDeck(numberOfDecks);
		createDeckImage();
		//System.out.println("Deck size: " + deck.size());		//TODO remove
	}
	
	public Deck(){
		this(1);
	}

//********************** Public Methods **********************
	public Card drawCard(){

		if(!deck.isEmpty()){
			Card temp;
		
			temp = deck.get(0);
			//System.out.println("Current card:" + currentCard);	//TODO remove
			deck.remove(0);
			removeCard();		//remove a card from the deck image
			
			//System.out.println(temp.getSuit() + " " + temp.getValue());  //TODO remove
			return temp;
			
		} else if(deck.isEmpty() && !discardPile.isEmpty()){			//if the last card has been drawn, shuffle and deal			
			shuffle();
			
			Card temp;
			
			temp = deck.get(0);
			deck.remove(0);
			//System.out.println("* " + temp.getSuit() + " " + temp.getValue());	//TODO remove
			return temp;
		} else{
			
			return null;		//There is a problem, cards aren't being discarded.
		}
		
	}
	
	public void shuffle(){		//shuffle the deck
		//System.out.println("Shuffling!");	//TODO remove
		
		deck.addAll(discardPile);	//add the cards in the discard pile back into the deck
		discardPile.clear();		//clear the discard pile
		createDeckImage();			//reset the deck image
		
		//System.out.println("Deck size: " + deck.size());	//TODO remove
		
		Collections.shuffle(deck);
		
	}
	
	public void discard(Card discarded){
		/*	Once a card is drawn it is removed from the deck.  A card on the table is neither in the deck nor in the discard pile.
		 *  When the table is cleared, the cards that were in play have to be added to the discard pile.
		 *  
		 *  Main reason for this is to prevent the scenario where a card on the table is redrawn from a re-shuffled deck.
		 *  Once the last card from the deck is drawn, the discard pile is shuffled into the deck.  So no cards that are currently on the table
		 *  can make up the re-shuffled deck.
		 * */
		
		discarded.setLayoutX(0);		//Reset the position of discarded cards.  Has to be done because cards are displayed in a stacked manner
		discarded.setLayoutY(0);
		discardPile.add(discarded);
	}
	
	public Pane getDeckImage(){	
		return deckImage;
	}
//********************** Private Methods **********************
	//creates the deck of cards
	private void createDeck(int numberOfDecks){		
		String[] suit = {"Clubs", "Hearts", "Spades", "Diamonds"};
		String[] value = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
		
		for(int num = 0; num < numberOfDecks; num++){			//loop once for the number of decks desired
			for (int s = 0; s < suit.length; s++){				//loop once for each suit
				for(int v = 0; v < value.length; v++){			//add a card of each value to the deck
					deck.add(new Card(suit[s], value[v]));
				}
			}
		}
		
		Collections.shuffle(deck);
	}
	
	//Create the image of the deck.
	private void createDeckImage(){
		deckImage.getChildren().clear();
		
		Image backImage;
		
		try {
			backImage = new Image(new FileInputStream("images/BACK.png"));
			
			int x = 0;
			
			for(int i = 0; i < deck.size(); i++){
				ImageView tempImage = new ImageView(backImage);
				tempImage.setLayoutX((i/4) * 2);	//Stack 4 cards every 2 pixels
				
				deckImage.getChildren().add(tempImage);
			}
			
			//System.out.println(deckImage.getChildren().size());	//TODO remove
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		}
	}
	
	//remove the last card image in the deck image
	private void removeCard(){
		int lastCardIndex = deckImage.getChildren().size() - 1;
		
		if(lastCardIndex >= 0){
			deckImage.getChildren().remove(lastCardIndex);
		}
	}
	
}