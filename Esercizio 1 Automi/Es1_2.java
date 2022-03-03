public class Es1_2 {

	public static void main (String[] args){
	
		System.out.println(Dfa(args[0]) ? "OK" : "NOPE");
	}
	
	public static boolean Dfa(String s){
			
		int state = 0, i =0;	
			
		while(state >= 0 && i < s.length()){	
		
			final char c = s.charAt(i++);
			
			switch (state){
				case 0:
					if(((int)c >= 65 && (int)c <= 90) || ((int)c >= 97 && (int)c <= 122))
						state = 1;
					else if(c == '_')
							state = 2;
						 else 
							 state = -1;
				break;
				
				case 1:
					if(((int)c >= 65 && (int)c <= 90) || ((int)c >= 97 && (int)c <= 122))
						state = 1;
					else if((int)c >= 48 && (int)c <= 57)
							state = 1;
						 else if(c == '_')
								state = 1;
							  else
								state = -1;
				break;
				
				case 2:
					if(c == '_')
						state = 2;
					else if(((int)c >= 65 && (int)c <= 90) || ((int)c >= 97 && (int)c <= 122))
							state = 1;
						 else if((int)c >= 48 && (int)c <= 57)
								state = 1;
			}
		}
		
		return state == 1;
	}
}