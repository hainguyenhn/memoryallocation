import java.util.Random;

public class Process{
	int[] possibleMemory = {5,1,17,31};
	//name of the process
	protected char name;
	//time which process runs
	protected int duration;
	//process's memory capacity
	protected int memory;
	
	//constructor
	public Process(int name){
		Random rand = new Random();
		//generate random name, duration, memory
		this.name = (char) name;
		this.duration = rand.nextInt(6) + 1;
		this.memory = possibleMemory[rand.nextInt(4)];
	}
	
	//return process name
	public char getProcessName(){
		return name;
	}
	
	//set process name
	public void setProcessname(char name){
		this.name = name;
	}
	//return process duration
	public int getDuration(){
		return duration;
	}
	
	//return process memory capacity
	public int getMemory(){
		return memory;
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
