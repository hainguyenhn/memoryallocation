import java.util.LinkedList;


public class MemoryAllocation {
	
	//linkedlist of process
	LinkedList<Process> processQueue = new LinkedList<Process>();
	
	//linkedlist of memory segment
	//total available memory
	final int MainMemeory = 100;
	
	//current memory usage
	int currentUsage = 0;
	
	public MemoryAllocation(Process[] processList){
		for(int i = 0; i < processList.length; i++){
			processQueue.add(processList[i]);
		}
	}
	
	
	
}
