public class Es1_4{

	public static void main(String[] args){
		
		System.out.println(Dfa(args[0]) ? "OK" : "NOPE");
		
	}
	
	public static boolean Dfa(String s){
		
		int state = 0, i = 0;
		
		while(state >= 0 && i < s.length()){
			
			final char c = s.charAt(i++);
			
			switch(state){
				case 0:
					if(c == '0' || c == '2' || c == '4' || c == '6' || c == '8')
						state = 2;
					else if(c == '1' || c == '3' || c == '5' || c == '7' || c == '9')
							state = 1;
						 else if(c == 32)
								state = 0;
							  else
								state = -1;
				break;
				
				case 1:
					if(c == '1' || c == '3' || c == '5' || c == '7' || c == '9')
						state = 1;
					else if(c == '0' || c == '2' || c == '4' || c == '6' || c == '8')
							state = 2;
						 else if(c == 32)
								state = 4;
							  else if(c >= 76 && c <= 90)
									 state = 3;
								   else
									 state = -1;
				break;
				
				case 2: 
					if(c == '1' || c == '3' || c == '5' || c == '7' || c == '9')
						state = 1;
					else if(c == '0' || c == '2' || c == '4' || c == '6' || c == '8')
							state = 2;
						 else if(c == 32)
								state = 5;
							  else if(c >= 65 && c <= 75)
									 state = 6;
								   else
									 state = -1;
				break;
				
				case 3:
					if(c == 32)
						state = 7;
					else if(c >= 97 && c <= 122)
							state = 3;
						 else
							state = -1;
				break;
				
				case 4:
					if(c == 32)
						state = 4;
					else if(c >= 76 && c <= 90)
							state = 3;
						 else 
							state = -1;
				break;
				
				case 5:
					if(c == 32)
						state = 5;
					else if(c >= 65 && c <= 75)
							state = 6;
						 else 
							state = -1;
				break;
				
				case 6:
					if(c == 32)
						state = 7;
					else if(c >= 97 && c <= 122)
							state = 6;
						 else
							state = -1;
				break;
				
				case 7:
					if(c == 32)
						state = 7;
					else if(c >= 65 && c <= 90)
							state = 6;
						 else if(c >= 97 && c <= 122)
								state = -1;
				break;	
			}
		}
		return (state == 3 || state == 6 || state == 7);
	}
}