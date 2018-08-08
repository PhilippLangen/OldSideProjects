import java.io.*;
import java.util.*;
public class Turing {

	private 
	
	static int dimension;
	static boolean doublesided;
	static char[] inputAlphabet;
	static char[] workingAlphabet;
	static String[] states;
	static String start;
	static String[] finals;
	static ArrayList<ArrayList<Order>> orders;
	static ArrayList<Stack<String>> left;
	static ArrayList<Stack<String>> right;
	static String currentState;
	static boolean invalid = false;
	static boolean accepted = false;

	
	
	
	public static void main(String[] args) throws IOException, InterruptedException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Load..");
		String input = sc.next();
		sc.close();
		FileReader fr = new FileReader(input);
		BufferedReader br= new BufferedReader(fr);
		br.skip(10);
		dimension= Integer.parseInt(br.readLine());
		br.skip(12);
		
		if(br.readLine().equals("1")){
			doublesided = true;
		}
		else{
			doublesided = false;
		}
		br.skip(9);
		inputAlphabet = br.readLine().toCharArray();
		br.skip(13);
		workingAlphabet = br.readLine().toCharArray();
		br.skip(7);
		states = br.readLine().split(",");
		br.skip(6);
		start = br.readLine();
		br.skip(6);
		finals = br.readLine().split(",");
		br.skip(7);
		orders = new ArrayList<ArrayList<Order>>();
		String[]buffer = br.readLine().split(">");
			for(String s : buffer){
				s=s.replace("<", "");
				String[] orderBuffer =s.split(",");
				char[] read = new char[dimension];
				for(int i = 0 ; i<dimension; i++)
					read[i]=orderBuffer[i+1].charAt(0);
				char[] write = new char[dimension];
				for(int i = 0 ; i<dimension; i++)
					write[i]=orderBuffer[i+1+dimension].charAt(0);
				char[] move = new char[dimension];
				for(int i = 0; i<dimension; i++)
					move[i]= orderBuffer[i+1+2*dimension].charAt(0);
				
				Order newOrder= new Order(orderBuffer[0],read,write,move,orderBuffer[orderBuffer.length-1]);
				boolean b = false;
				for(ArrayList<Order> l :orders){
					if(l.get(0).getCurrentState().equals(orderBuffer[0])){
						b = l.add(newOrder);
					}
				}
				if(!b){
					ArrayList<Order> newEntry = new ArrayList<Order>();
					newEntry.add(newOrder);
					orders.add(newEntry);
					
				}
				
				
				
			}	
		br.readLine();
		
		right = new ArrayList<Stack<String>>();
		left = new ArrayList<Stack<String>>();
		String[] band = new String[dimension]; 
		for(int i = 0; i < dimension ;i++){
			band[i]= br.readLine();
			right.add(new Stack<String>());
			left.add(new Stack<String>());
			for(int j = 1 ;j<=band[i].length(); j++ ){
		
				right.get(i).push(Character.toString(band[i].charAt(band[i].length()-j)));
			}
		}
		br.close();
		fr.close();
		
		if(!checkTm())
			return;
		
		currentState = start;
		while(!invalid&&!accepted){
		
			for(int flush=0; flush<5; flush ++)
				System.out.println("");
			step();
			Thread.sleep(1000);
		}
		if(invalid)
			System.out.println("invalid");
		else
			System.out.println("accepted");
	
	
	}
	
	
	public static void step(){
		display();
		String stringCurRead ="";
		for(Stack<String> s : right){
			if (s.isEmpty())
			s.push("_");
			stringCurRead += s.pop();
		}
		
		char[] curRead = stringCurRead.toCharArray();
		boolean done = false;
		for(ArrayList<Order> o : orders){
			
			if(o.get(0).getCurrentState().equals(currentState)&&!done){
				done = true;
				for(Order ord : o){
					if(Arrays.equals(ord.getRead(),curRead)){
						
						invalid = false;
						currentState = ord.getNextState();
						System.out.println(ord.toString());
						System.out.println("");
						for(int k = 0; k<dimension; k++){
							switch (ord.getMove()[k]){
							case 'R' : 	left.get(k).push(Character.toString(ord.getWrite()[k]));
										break;
							case 'N' : 	right.get(k).push(Character.toString(ord.getWrite()[k]));
										break;
							case 'L' : 	right.get(k).push(Character.toString(ord.getWrite()[k]));
										if (!left.get(k).isEmpty()){
											right.get(k).push(left.get(k).pop());
										}else if(doublesided){
											right.get(k).push("_");
										}else{
											invalid = true;
											return;
										}
										break;
							default : System.out.println("Invalid direction");
							}
						}
						for(String end: finals){
							if(end.equals(currentState)){
								accepted = true;
								return;
							}
						}
	
					}
				}
				
			}
			
		}
		if(!done){
			invalid = true;
		}
			
		
	}
	
	

public static void display(){
	
	for(int i=0 ; i<dimension ; i++){
		String leftString= "";
		String rightString= "";
		
		for(int j=0; j<left.get(i).size(); j++){
			leftString=  leftString + left.get(i).elementAt(j)   ;
		}
		for(int j=0; j<right.get(i).size(); j++){
			rightString= right.get(i).elementAt(j) + rightString;
		}
		System.out.println(leftString+"#"+rightString);
		
	}
	
	
	
	
}

public static boolean checkTm(){
	
	//check Alphabet
	for(Stack<String> a : right){
		for(String chr : a){
			boolean f =false;
			for(int i= 0; i< inputAlphabet.length ; i++){
				if(chr.equals( Character.toString(inputAlphabet[i])))
					f= true;
			}
			if(f == false){
				System.out.println("character on band not in alphabet");
				return false;
			}
		}
	}
	
	//check WorkAlphabet,Directions,Start and next state
	
	for(ArrayList<Order> list: orders){
		for(Order o : list){
			boolean qa = false;
			boolean qb = false;
			for(String s : states){
				if(o.getCurrentState().equals(s))
					qa=true;
				if(o.getNextState().equals(s))
					qb=true;
			}
			if(!qa||!qb){
				System.out.println("invalid state in order");
				return false;
			}
			
			
			
			for(int i = 0 ; i<dimension ; i++){
				if(!Character.toString(o.move[i]).equals("L")&&!Character.toString(o.move[i]).equals("R")&&!Character.toString(o.move[i]).equals("N")){
				System.out.println("invalid direction in Order, must be R,L or N");
				return false;
				}
				boolean r = false;
				boolean w = false;
				for(int j= 0 ; j < workingAlphabet.length; j++){
					if(o.read[i]==workingAlphabet[j])
						r=true;
					if(o.write[i]==workingAlphabet[j])
						w=true;
				}
				if(!r||!w){
					System.out.println("character in order not in working alphabet");
					return false;	
				}
			}
		}
	}
	
	//check States
	boolean starting = false;
	for(String s: states){
		if(s.equals(start))
			starting = true;
	
	}
	if(!starting){
		System.out.println("starting state not in list of states");
		return false;
	}
	for(String e : finals){
		boolean end = false;
		for(String s : states){
			if(s.equals(e))
				end = true;
		}
		if(!end){
			System.out.println("final state not in states");
		return false;
		}
	}
	
	
	return true;
}


}




