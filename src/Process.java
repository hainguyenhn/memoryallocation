import java.util.Random;

public class Process{

	
	int[] possibleMemory = {5,1,17,31};
	//name of the process
	private char name;
	//time which process runs
	private int duration;
	//process's memory capacity
	private int memory;
	
	//constructor
	public Process(){
		Random rand = new Random();
		//generate random name, duration, memory
		int nameTemp = rand.nextInt(91) + 65;
		this.name = String.valueOf(nameTemp).charAt(0);
		this.duration = rand.nextInt(6) + 1;
		this.memory = possibleMemory[rand.nextInt(4)];
	}
	
	//print process
	public String toString(){
		String temp = "";
		for(int i = 0;i < duration;i++){
			temp += name;
		}
		return temp;
	}
}
