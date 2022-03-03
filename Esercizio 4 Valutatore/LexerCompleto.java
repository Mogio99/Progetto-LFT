import java.io.*; 
import java.util.*;

public class LexerCompleto{

    public static int line = 1;
    private char peek = ' ';
    
    private void readch(BufferedReader br){ //Prendi il carattere succ da br e mettilo in peek
        try{
            peek = (char) br.read();
        }catch (IOException exc){
            peek = (char) -1; // ERROR
		 }
    }

    public Token lexical_scan(BufferedReader br){
        while (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r'){
            if (peek == '\n') 
				line++;
            readch(br);
        }
        
        switch (peek){ //Riconoscitore token "base"
            case '!':
                peek = ' ';
                return Token.not; 
			
			case '(':
                peek = ' ';
                return Token.lpt;
				
			case ')':
                peek = ' ';
                return Token.rpt;
				
			case '{':
                peek = ' ';
                return Token.lpg;
				
			case '}':
                peek = ' ';
                return Token.rpg;
				
			case '+':
                peek = ' ';
                return Token.plus;
				
			case '-':
                peek = ' ';
                return Token.minus;
				
			case '*':
                peek = ' ';
                return Token.mult;
				
			case '/': //Riconoscitore commenti e '/'
				readch(br);
                if (peek == '/'){ //Se trovo //
					peek = ' ';
					do{
						readch(br);
					}while(peek != '\n'); // Se trovo // consumo fino a \n
				
				return lexical_scan(br);
				
				}else if (peek == '*'){ // Se trovo /* 
					peek = ' ';
					boolean trovato = false;
					do{
						readch(br);
						if(peek == '*'){
							while(peek=='*'){
								readch(br);
								if(peek == '/'){
								trovato = true;
								readch(br);
								break;
								}
							}
							//if(peek == '/')
								//trovato = true; // Se trovo */ setto trovato a true per uscire dal loop
						}
						if(peek == ((char)-1)){ // Se arrivo a fine file senza '/' di chiusura commento
							trovato = true;
							System.err.println("Error: comment not close");
						}
					}while(!trovato);
					peek = ' ';
					return lexical_scan(br); //Dopo la chiusura del commento richiami la funz ricorsivamente
				}else{
					return Token.div;
				}
				
			case ';':
                peek = ' ';
                return Token.semicolon;
			
            case '&':
                readch(br);
                if (peek == '&'){
                    peek = ' ';
                    return Word.and;
                }else{
                    System.err.println("Erroneous character"
                            + " after & : "  + peek );
                    return null;
                }											
			
			case '|':
                readch(br);
                if (peek == '|'){
                    peek = ' ';
                    return Word.or;
                }else{
                    System.err.println("Erroneous character"
                            + " after | : "  + peek );
                    return null;
                }
			
			case '<':
                readch(br);
                if (peek == '='){
					peek = ' ';
                    return Word.le;
				}else if(peek == '>'){
						peek = ' ';
						return Word.ne;
					  }else{
							return Word.lt;
					       } 
			case '>':
                readch(br);
                if (peek == '='){
					peek = ' ';
                    return Word.ge;
				}else{
						return Word.gt;
					 }
			
			case '=':
                readch(br);
                if (peek == '='){
					peek = ' ';
                    return Word.eq;
				}else{
						return Token.assign;
					 } 
			
            case (char)-1:
                return new Token(Tag.EOF);

            default:
                if(Character.isLetter(peek) || peek == '_'){
					
					String s = "";
					
					do{									// Consumo fino a quando non sono più lettere, numeri o '_'
						s += peek;
						readch(br);
					}while(Character.isLetter(peek) || Character.isDigit(peek) || peek == '_'); 
					
					switch(s){ // Controllo se quello che ho consumato è una parola chiave
						case "cond":
							return Word.cond;
					
						case "when":
							return Word.when;
					
						case "then":
							return Word.then;
					
						case "else":
							return Word.elsetok;
					
						case "while":
							return Word.whiletok;
					
						case "do":
							return Word.dotok;
					
						case "seq":
							return Word.seq;
					
						case "print":
							return Word.print;
					
						case "read":
							return Word.read;
					
						default: //se voglio riconoscere solo parole chiave inverti commenti sotto e togli dal while num e _
							return new Word(Tag.ID, s); 
							//System.err.println("Erroneous ID: " + s );
							//return null;
					}
					
                }else if(Character.isDigit(peek)){
						
							int num = 0;
					
							do{ 				// Consumo fino a quando trovo dei numeri
								num = num * 10 + (peek-48);
								readch(br);
							}while(Character.isDigit(peek));
						
							return new NumberTok(num);
						
						}else{
								System.err.println("Erroneous character: " + peek );
								return null;
						}
    }
}
		
    public static void main(String[] args){
        LexerCompleto lex = new LexerCompleto();
        String path = "lexer.txt"; // il percorso del file da leggere
		
        try{
            BufferedReader br = new BufferedReader(new FileReader(path));
			
            Token tok;
			
            do{
                tok = lex.lexical_scan(br);
                System.out.println("Scan: " + tok);
            }while(tok.tag != Tag.EOF);
			
            br.close();
			
        }catch(IOException e){
			e.printStackTrace();
		 }    
    }
}