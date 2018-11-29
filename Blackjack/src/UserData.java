/* Juan Leon
 * 8/5/18
 * COP2552.0M1
 * Java SE 1.8
 * 
 * Class representing the data stored for each player.
 * */


public class UserData {
	String key;
	String name;
	int chips;
	
	public UserData(){
		key = "";
		name = "";
		chips = 1000;
	}
	
	public void setName(String s){
		this.name = s;
		this.key = s.toUpperCase();
	}
	
	public void setChips(int c){
		this.chips = c;
	}
	
	public String getKey(){
		return key;
	}
	
	public int getChips(){
		return chips;
	}
	
	public String getName(){
		return name;
	}
	
	//For Testing purposes
	@Override
	public String toString(){
		return (name + " - " + chips + "\n");
	}
}
