/* Juan Leon
 * 8/5/18
 * COP2552.0M1
 * Java SE 1.8
 * 
 * Class for blackjack.  It represents a card. It holds the suit, card value, and value used in blackjack.
 * Each card is an ImageView with the appropriate image.
 * */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

//Class to represent a playing card
public class Card extends ImageView{
	
	String suit;		//the suit of the card
	String value;		//String value of the card
	int intValue;		//integer value of the card used in Blackjack, aces count as 11

//********************** Constructor **********************
	public Card(String suit, String value) {
		this.suit = suit;
		this.value = value;
		setIntValue();
		try {
			setCardImage();
		} catch (FileNotFoundException e) {
			
		}
		
		//TODO need to handle improper values
	}

//********************** Public Methods **********************
	public int getIntValue(){
		return this.intValue;
		
	}
	
	public String getSuit(){
		return this.suit;
		
	}
	
	public String getValue(){
		return this.value;
		
	}

//********************** Private Methods **********************	
	private void setIntValue(){
		try{	//If value is a number, then intValue is that number
			this.intValue = Integer.parseInt(value);
		}
		catch(NumberFormatException e){		//if it's an ace or a face card, the intValue is 11 or 10 accordingly
			if(value.equals("A")){
				this.intValue = 11;
				
			}else if(value.equals("J")){
				this.intValue = 10;
				
			}else if(value.equals("Q")){
				this.intValue = 10;
				
			}else if(value.equals("K")){
				this.intValue = 10;
			}
		}
	}
	
	private void setCardImage() throws FileNotFoundException{		//loads the appropriate image for this object 
		String[] clubs = { "images/CA.png",
				"images/C2.png",
				"images/C3.png",
				"images/C4.png",
				"images/C5.png",
				"images/C6.png",
				"images/C7.png",
				"images/C8.png",
				"images/C9.png",
				"images/C10.png",
				"images/CJ.png",
				"images/CQ.png",
				"images/CK.png"};
		
		String[] hearts = { "images/HA.png",
				"images/H2.png",
				"images/H3.png",
				"images/H4.png",
				"images/H5.png",
				"images/H6.png",
				"images/H7.png",
				"images/H8.png",
				"images/H9.png",
				"images/H10.png",
				"images/HJ.png",
				"images/HQ.png",
				"images/HK.png"};
		
		String[] spades = { "images/SA.png", 
				"images/S2.png",
				"images/S3.png",
				"images/S4.png",
				"images/S5.png",
				"images/S6.png",
				"images/S7.png",
				"images/S8.png",
				"images/S9.png",
				"images/S10.png",
				"images/SJ.png",
				"images/SQ.png",
				"images/SK.png"};
		
		String[] diamonds = { "images/DA.png", 
				"images/D2.png",
				"images/D3.png",
				"images/D4.png",
				"images/D5.png",
				"images/D6.png",
				"images/D7.png",
				"images/D8.png",
				"images/D9.png",
				"images/D10.png",
				"images/DJ.png",
				"images/DQ.png",
				"images/DK.png"};
		
		String imageToLoad = "";
		
		if(suit.equals("Clubs")){		//card is a clubs						
			if(intValue < 10){			//is a number card 1 - 9
				imageToLoad = clubs[intValue - 1];
				
			}else if(intValue == 11){	//is an ace
				imageToLoad = clubs[0];
				
			}else if(intValue == 10){	
				if(value.equals("10")){			//is a 10
					imageToLoad = clubs[9];
					
				}else if(value.equals("J")){	//is a Jack
					imageToLoad = clubs[10];
					
				}else if (value.equals("Q")){	//is a Queen
					imageToLoad = clubs[11];
					
				}else if (value.equals("K")){	//is a King
					imageToLoad = clubs[12];
					
				}
			}
			
		}else if(suit.equals("Hearts")){						
			if(intValue < 10){
				imageToLoad = hearts[intValue - 1];
				
			}else if(intValue == 11){
				imageToLoad = hearts[0];
				
			}else if(intValue == 10){
				if(value.equals("10")){
					imageToLoad = hearts[9];
					
				}else if(value.equals("J")){
					imageToLoad = hearts[10];
					
				}else if (value.equals("Q")){
					imageToLoad = hearts[11];
					
				}else if (value.equals("K")){
					imageToLoad = hearts[12];
					
				}
			}
			
		}else if(suit.equals("Spades")){			
			if(intValue < 10){
				imageToLoad = spades[intValue - 1];
				
			}else if(intValue == 11){
				imageToLoad = spades[0];
				
			}else if(intValue == 10){
				if(value.equals("10")){
					imageToLoad = spades[9];
					
				}else if(value.equals("J")){
					imageToLoad = spades[10];
					
				}else if (value.equals("Q")){
					imageToLoad = spades[11];
					
				}else if (value.equals("K")){
					imageToLoad = spades[12];
					
				}
			}
			
		}else if(suit.equals("Diamonds")){			
			if(intValue < 10){
				imageToLoad = diamonds[intValue - 1];
				
			}else if(intValue == 11){
				imageToLoad = diamonds[0];
				
			}else if(intValue == 10){
				if(value.equals("10")){
					imageToLoad = diamonds[9];
					
				}else if(value.equals("J")){
					imageToLoad = diamonds[10];
					
				}else if (value.equals("Q")){
					imageToLoad = diamonds[11];
					
				}else if (value.equals("K")){
					imageToLoad = diamonds[12];
					
				}
			}
		}
		
		this.setImage(new Image(new FileInputStream(imageToLoad)));
		
	}
	

}
