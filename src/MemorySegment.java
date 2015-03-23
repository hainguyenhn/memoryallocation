
public class MemorySegment {
	private Process process;
	private int startTime;
	private int duration;
	
	public MemorySegment(Process process){
		this.process = process;
		this.duration = process.getDuration();
	}
	
	//decrement time remaining on every check.
	public int checkDuration(){
		if(duration > 0){
			duration--;
			return duration;
		}
		else{
			return -1;
		}
	}

	//return process
	public Process getProcess(){
		return process;
	}
	
	//return startTime
	public int getStartTime(){
		return startTime;
	}
	
	//return endTime of Segment
	public int getEndTime(){
		return startTime + process.getMemory();
	}
	
	//return segment length
	public int length(){
		return process.getMemory();
	}

	//segment start time
	public void setStartTime(int i){
		this.startTime = i;
	}
}
