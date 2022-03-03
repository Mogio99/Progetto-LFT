public class Es1_11{
	
	public static void main(String[] args){
		System.out.println(Dfa(args[0]) ? "OK" : "NOPE");
	}
	
	public static boolean Dfa(String s){
		
		int i = 0, state = 0;
		
		while(state >= 0 && i < s.length()){
			
			final char c = s.charAt(i++);
			
			switch(state){
				case 0:
					if(c == 'a' || c == '*')
						state = 0;
					else if(c == '/')
						state = 1;
						 else
							 state = -1;
				break;
				
				case 1:
					if(c == 'a')
						state = 0;
					else if(c == '/')
							state = 1;
							else if(c == '*')
									 state = 2;
								 else
									 state = -1;
				break;
				
				case 2:
					if(c == 'a' || c == '/')
						state = 2;
					else if(c == '*')
							  state = 3;
						 else
							 state = -1;
				break;
				
				case 3:
					if(c == '*')
						state = 3;
					else if(c == 'a')
							state = 2;
						 else if(c == '/')
								  state = 4;
							  else
								  state = -1;
				break;
				
				case 4:
					if(c == 'a' || c == '*')
						state = 4;
					else if(c == '/')
							 state = 1;
						 else
							 state = -1;
				break;
			}
		}
		
		return state == 0 || state == 1 || state == 4;
		
	}
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	