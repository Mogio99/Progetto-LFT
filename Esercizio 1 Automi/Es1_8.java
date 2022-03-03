public class Es1_9{

	public static void main(String[] args){
		System.out.println(Dfa(args[0]) ? "OK" : "NOPE");
	}
	
	public static boolean Dfa(String s){
		
		int state = 0, i = 0;
		
		while(state >= 0 && i < s.length()){
			final char c = s.charAt(i++);
			
			switch(state){
				case 0:
					if(c == 'm' || c == 'M')
						state = 1;
					else if ((c >= 65 && c <= 90) || (c >= 97 && c <= 122))
						state = 6;
						 else
							 state = -1;
				break;
				
				case 1:
					if(c == 'o')
						state = 2;
					else if ((c >= 65 && c <= 90) || (c >= 97 && c <= 122))
						state = 7;
						 else
							 state = -1;
				break;
				
				case 2:
					if(c == 'g')
						state = 3;
					else if ((c >= 65 && c <= 90) || (c >= 97 && c <= 122))
						state = 8;
						 else
							 state = -1;
				break;
				
				case 3:
					if(c == 'i')
						state = 4;
					else if ((c >= 65 && c <= 90) || (c >= 97 && c <= 122))
						state = 9;
						 else
							 state = -1;
				break;
				
				case 4:
					if(c >= 97 && c <= 122)
						state = 5;
					else
						state = -1;
				break;
				
				case 6:
					if(c == 'o')
						state = 7;
					else if ((c >= 65 && c <= 90) || (c >= 97 && c <= 122))
						state = -1;
				break;
				
				case 7:
					if(c == 'g')
						state = 8;
					else if ((c >= 65 && c <= 90) || (c >= 97 && c <= 122))
						state = -1;
				break;
				
				case 8:
					if(c == 'i')
						state = 9;
					else if ((c >= 65 && c <= 90) || (c >= 97 && c <= 122))
						state = -1;
				break;
				
				case 9:
					if(c == 'o')
						state = 5;
					else if ((c >= 65 && c <= 90) || (c >= 97 && c <= 122))
						state = -1;
				break;
			}
		}
		return state == 5;
	}
}