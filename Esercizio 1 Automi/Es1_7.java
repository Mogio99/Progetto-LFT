public class Es1_8{

	public static void main(String[] args){
		
		System.out.println(Dfa(args[0]) ? "OK" : "NOPE");
		
	}
	
	public static boolean Dfa(String s){
		
		int state = 0, i = 0;
		
		while(state >= 0 && i < s.length()){
			
			final char c = s.charAt(i++); 
			
			switch(state){
				case 0:
					if(c == 'b')
						state = 4;
					else if(c == 'a')
							state = 1;
						 else
							 state = -1;
				break;
				
				case 1:
					if(c == 'b')
						state = 2;
					else if(c == 'a')
							state = 1;
						 else
							 state = -1;
				break;
				
				case 2:
					if(c == 'b')
						state = 3;
					else if(c == 'a')
							state = 1;
						 else
							state = -1;
				break;
				
				case 3:
					if(c == 'a')
						state = 1;
					else if(c == 'b')
							state = 4;
						 else
							state = -1;
				break;
				
				case 4:
					if(c == 'a')
						state = 1;
					else if(c == 'b')
							state = 4;
						 else
							 state = -1;
				break;
			}
		}
		return (state == 1 || state == 2 || state == 3);
	}
}