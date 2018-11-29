/* Juan Leon
 * 8/5/18
 * COP2552.0M1
 * Java SE 1.8
 * 
 * Class for blackjack.  It is responsible for reading and writing player's data from and into file.
 * It also sorts the list by chip amount and can return a list with the top 10 players.
 * */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;



public class GameData {

	private List<UserData> dataList = new ArrayList<UserData>(); 
	
//********************** Constructors **********************
	public GameData(){
		readData();
	}

//********************** Public Methods **********************
	//Checks to see if a player has a record
	public boolean exists(String name){
		for(UserData user: dataList){
			if(user.getKey().equals(name.toUpperCase())){
				return true;
			}
		}
		
		return false;
	}
	
	//Set the chips for a given player
	public void setChips(String name, int chips){		
		for(UserData user: dataList){
			if(user.getKey().equals(name.toUpperCase())){				
				user.setChips(chips);
			}
		}
		
	}
	
	//Look for a player in the database and return how many chips he has.
	//If the player is not found in the database, then create a new user and return the standard 1000 chips given to new players
	public int getChips(String name){
		
		for(UserData user: dataList){		//Look for the player in the dataList and return how many chips he has
			if(user.getKey().equals(name.toUpperCase())){
				return user.getChips();
			}
		}
		
		//If the player is not found in the list
		UserData temp = new UserData();
		
		temp.setName(name);
		dataList.add(temp);
		sortData();
		
		return temp.getChips();
	}
	
	//Write data to file
	public void writeData(){
		if(!dataList.isEmpty()){
			try(PrintWriter output = new PrintWriter("gameData.dat")){
				
				for(int i = 0; i < dataList.size(); i++){
					UserData temp = dataList.get(i);
					
					output.print(temp.name + "%%");
					output.print(temp.chips + "%%\n");
				}
				
			}catch (Exception e) {
				e.printStackTrace();	//TODO figure out what to do if unable to write file
			}
		}
	}

	public List<UserData> getTopTen(){
		List<UserData> topTen = new ArrayList<UserData>();
		
		sortData();
		
		if(dataList.size() < 10){
			for(int i = 0; i < dataList.size(); i++){
				topTen.add(dataList.get(i));
			}
		}else{
			topTen = dataList.subList(0, 10);
		}
				
		return topTen;		
	}
//********************** Private Methods **********************
	//Read data from file
	private void readData(){
		File sourceFile = new File("gameData.dat");
		
		try(Scanner input = new Scanner(sourceFile).useDelimiter("%%\\n*")){
			
			if(input.hasNext()){	//File not empty
								
				while(input.hasNext()){
					UserData temp = new UserData();
					
					String name = input.next();					
					temp.setName(name);
					
					int value = input.nextInt();
					temp.setChips(value);
					
					dataList.add(temp);					
				}
				
			}else{	//File is empty

			}
					
		}catch(FileNotFoundException e) {
			System.out.println("File not found");
		}
		//System.out.println(dataList);		//TODO for testing
		sortData();
	}
	
	//sort the list in descending order of chips
	private void sortData(){
		Collections.sort(dataList, (UserData a, UserData b) -> b.chips-a.chips);
		//System.out.println(dataList);	//TODO for testing
	}
	
}