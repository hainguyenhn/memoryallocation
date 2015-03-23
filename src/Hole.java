
public class Hole extends Process{
	public Hole(int length){
		super('*');
		this.name = '*';
		this.duration = 99999;
		this.memory  = length;
	}
}
