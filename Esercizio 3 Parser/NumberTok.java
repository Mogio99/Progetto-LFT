public class NumberTok extends Token {
	
	public int n;
	
	public NumberTok(int s){
		super(Tag.NUM);
		n = s;
	}
	
    public String toString(){
		return "<" + tag + ", " + n + ">";
	}
}