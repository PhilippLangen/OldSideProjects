
public class Order {

	private 
	
	String currentState;
	char[] read;
	char[] write;
	char[] move;
	String nextState;
	
	public Order(String currentState, char[] read, char[] write, char[] move , String nextState){
		
		this.currentState= currentState;
		this.read = read;
		this.write = write;
		this.move = move;
		this.nextState=nextState;
		
		
	}

	public String getCurrentState() {
		return currentState;
	}

	public char[] getRead() {
		return read;
	}

	public char[] getWrite() {
		return write;
	}

	public char[] getMove() {
		return move;
	}

	public String getNextState() {
		return nextState;
	}
	
	public String toString(){
		return getCurrentState() + " " + new String (getRead()) + " " + new String (getWrite()) + " " + new String (getMove()) + " "+ getNextState();
	}
}
