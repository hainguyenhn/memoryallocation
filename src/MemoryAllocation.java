import java.util.LinkedList;


public class MemoryAllocation {

	//linkedlist of process
	LinkedList<Process> processQueue = new LinkedList<Process>();

	//linkedlist of memory segment
	LinkedList<MemorySegment> segmentQueue = new LinkedList<MemorySegment>();
	//total available memory
	final int MainMemory = 100;
	//run time
	final int time = 60;

	//current memory usage
	private int currentUsage = 0;

	//constructor
	public MemoryAllocation(Process[] processList){
		//add process to queue.
		for(int i = 0; i < processList.length; i++){
			processQueue.add(processList[i]);
		}

		//add first hole of 100mb.
		MemorySegment firstHole = new MemorySegment(new Hole(100));
		firstHole.setStartTime(0);
		segmentQueue.add(firstHole);
	}

	//decrements memory segment and delete completed process
	public void decrementTime(int time){
		for(int i = 0; i < segmentQueue.size(); i++){
			//if process in segment is done
			if(segmentQueue.get(i).checkDuration() == 0){
				currentUsage -= segmentQueue.get(i).getProcess().getMemory();
				char temp = segmentQueue.get(i).getProcess().getProcessName();
				String tempName = "Process " + temp + " is completed.";

				//get segment starttime and memory space
				int startTime = segmentQueue.get(i).getStartTime();
				int tempSpace = segmentQueue.get(i).getProcess().getMemory();

				//create hole segment
				MemorySegment newHole = new MemorySegment(new Hole(tempSpace));
				newHole.setStartTime(startTime);

				//replace completed process with hole
				segmentQueue.add(i+1, newHole);
				segmentQueue.remove(i);

				joinAdjcent(i);

				print(time, tempName);
				System.out.println("\nnow has " + segmentQueue.size() + " segments.");
			}
		}
	}

	//check and join adjacent holes
	public void joinAdjcent(int index){
		if(index > 0){
			MemorySegment current = segmentQueue.get(index);
			MemorySegment previous = segmentQueue.get(index - 1);

			//join previous if a hole
			if(previous.getProcess().getProcessName() == '*'){
				current = joinHoles(previous,current);
			}
			// join next if a hole
			int pos = segmentQueue.indexOf(current);
			if(pos < segmentQueue.size()-1){
				MemorySegment next = segmentQueue.get(pos+1);
				if(next.getProcess().getProcessName() == '*'){
					current = joinHoles(current,next);
				}
			}
		}
	}

	//join two holes and return memorysegment of new hole
	public MemorySegment joinHoles(MemorySegment a, MemorySegment b){
		int index = segmentQueue.indexOf(a);
		MemorySegment segA = a;
		MemorySegment segB = b;

		//total hole space
		int total = segA.getProcess().getMemory() + segB.getProcess().getMemory();
		//get starttime
		int startTime = segA.getStartTime();
		//create new hole
		Hole tempHole = new Hole(total);
		MemorySegment tempSeg = new MemorySegment(tempHole);
		tempSeg.setStartTime(startTime);
		//remove both holes
		segmentQueue.remove(b);
		segmentQueue.remove(a);
		//add new hole
		segmentQueue.add(index, tempSeg);	
		return segmentQueue.get(index);
	}

	//check memory full
	public boolean memoryFull(){
		return (MainMemory < currentUsage);
	}

	//implement first fit algorithm
	public void firstFit(){
		int currentTime = 0;
		while(currentTime < time){
			if(!processQueue.isEmpty()){
				if(currentTime != 0){
					decrementTime(currentTime);
				}
				//check for space
				int[] result = checkSpace(0,processQueue.peek().getMemory());
				//if there are spaces in memory for required process
				if(result[0] != -1){
					Process temp = processQueue.pop();
					char name = temp.getProcessName();
					String tempName = "Process " + name + " is added.";
					replace(result,temp);
					print(currentTime, tempName);
				}
				else{
					System.out.println("\n No space left");
				}
			}
			currentTime++;
		}
	}

	//check space
	public int[] checkSpace(int start, int space){
		//store index, space and number of process need
		int empty[] = {-1,0,0};
		for(int i = start; i < segmentQueue.size(); i++){
			//if segment is a hole, then check space
			if(segmentQueue.get(i).getProcess().getProcessName() == '*'){
				//return the first hole if it's satisfied
				if(segmentQueue.get(i).getProcess().getMemory() >= space){
					if(empty[0] == -1){
						empty[0] = i;
					}
					empty[1] = segmentQueue.get(i).getProcess().getMemory();
					empty[2]++;
					System.out.println("\nFound segment " + empty[0] + "space found "  + empty[1] + " and required " + empty[2]);
					return empty;
				}
				else{
					//only set for the first time.
					if(empty[0] == -1){
						empty[0] = i;
					}
					empty[1] += segmentQueue.get(i).getProcess().getMemory();
					empty[2]++;
					if(empty[1] >= space){
						System.out.println("\nFound segment " + empty[0] + "space found "  + empty[1] + " and required " + empty[2]);
						return empty;
					}
				}
			}
			//reset empty
			else{
				empty[0] = -1;
				empty[1] = 0;
				empty[2] = 0;
			}
		}
		System.out.println("\nFound segment " + empty[0] + "space found "  + empty[1] + " and required " + empty[2]);
		//if not enough space
		if(empty[1] < space){
			empty[0] = -1;
			empty[1] = 0;
			empty[2] = 0;
		}
		return empty;
	}

	//replace hole with process
	public void replace(int[] location, Process process){
		//number of segments need to be removed
		int numSegment = location[2];

		int memoryRequired = process.getMemory();
		int memoryLocated = location[1];
		int start = location[0];

		//prepare new segment with process
		MemorySegment newProcessSeg = new MemorySegment(process);
		int startTime = segmentQueue.get(start).getStartTime();
		newProcessSeg.setStartTime(startTime);

		//add new segment
		segmentQueue.add(start++,newProcessSeg);

		//create new hole if necessary(there are remain space left)
		if(memoryLocated > memoryRequired){
			Hole hole = new Hole(memoryLocated - memoryRequired);
			//add hole after hole needs to be removed
			MemorySegment holeSeg = new MemorySegment(hole);
			holeSeg.setStartTime(newProcessSeg.getEndTime());
			segmentQueue.add(start++, holeSeg);
		}

		//remove number of segment
		while(numSegment > 0){
			segmentQueue.remove(start);
			numSegment--;
		}

		System.out.println(process.getProcessName() + " is added, now has " + segmentQueue.size() + " segments.");
	}

	//print method
	public void print(int time,String event){
		char[] memSlice = new char[MainMemory];
		for(int i = 0; i < MainMemory; i++){
			memSlice[i] = '*';
		}

		System.out.println("\nTime: " +time +" \n"+  event);
		for(int i = 0; i < segmentQueue.size(); i++){
			for(int j = segmentQueue.get(i).getStartTime(); j < segmentQueue.get(i).getEndTime(); j++){
				memSlice[j] = segmentQueue.get(i).getProcess().getProcessName();
			}
		}

		for(int i = 0; i < MainMemory; i++){
			System.out.print(memSlice[i]);
		}
	}

	public static void main(String[] args){
		int numProcess = 100;
		Process[] list = new Process[numProcess];
		for(int i = 0; i < numProcess; i++){
			list[i] = new Process(i+65);
			System.out.print(list[i].getProcessName()+"-");
			System.out.print(list[i].getMemory() +"-");
			System.out.print(list[i].getDuration());
			System.out.println();
		}

		MemoryAllocation mem = new MemoryAllocation(list);
		mem.firstFit();
	}
}
