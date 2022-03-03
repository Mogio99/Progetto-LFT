public class Es1_3{

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
						state = 1;
					else if(c == '1' || c == '3' || c == '5' || c == '7' || c == '9')
							state = 2;
						 else if(((int)c >= 65 && (int)c <= 90) || ((int)c >= 97 && (int)c <= 122))
								state = -1;
				break;
				
				case 1:
					if(c == '0' || c == '2' || c == '4' || c == '6' || c == '8')
						state = 1;
					else if(c == '1' || c == '3' || c == '5' || c == '7' || c == '9')
						state = 2;
						 else if (((int)c >= 65 && (int)c <= 75) || ((int)c >= 97 && (int)c <= 107))
							state = 3;
							  else if (((int)c >= 76 && (int)c <= 90) || ((int)c >= 108 && (int)c <= 122))
								state = -1;
				break;
				
				case 2:
					if(c == '0' || c == '2' || c == '4' || c == '6' || c == '8')
						state = 1;
					else if(c == '1' || c == '3' || c == '5' || c == '7' || c == '9')
						state = 2;
						 else if (((int)c >= 76 && (int)c <= 90) || ((int)c >= 108 && (int)c <= 122))
							state = 3;
							  else if (((int)c >= 65 && (int)c <= 75) || ((int)c >= 97 && (int)c <= 107))
								state = -1;
				break;
				
				case 3:
					if(((int)c >= 65 && (int)c <= 90) || ((int)c >= 97 && (int)c <= 122))
						state = 3;
					else if(c == '0' || c == '2' || c == '4' || c == '6' || c == '8' || c == '1' || c == '3' || c == '5' || c == '7' || c == '9')
						state = -1;
				break;
			}		
		}
		return state == 3;
	}
}