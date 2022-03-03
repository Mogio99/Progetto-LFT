import java.io.*;

public class Parser_2{
	private LexerCompleto lex;
	private BufferedReader pbr;
	private Token look;
	
	public Parser_2(LexerCompleto l, BufferedReader br){
		lex = l;
		pbr = br;
		move();
	}
	
	void move(){
		look = lex.lexical_scan(pbr);
		System.out.println("token = " + look);
	}
	
	void error(String s){
		throw new Error("near line " + lex.line + ": " + s);
	}
	
	void match(int t){
		if(look.tag == t){
			if(look.tag != Tag.EOF)
				move();
		}else error("syntax error");
	}
	
	public void prog(){
		switch (look.tag){
			case '=':
			case '{':
			case Tag.PRINT:
			case Tag.READ:
			case Tag.COND:
			case Tag.WHILE:
				statlist();
				match(Tag.EOF);
			break;
			
			default:
				error("error in prog");
			break;
		}	
	}
	
	private void statlist(){
		switch (look.tag){
			case '=':
			case '{':
			case Tag.PRINT:
			case Tag.READ:
			case Tag.COND:
			case Tag.WHILE:
				stat();
				statlistp();
			break;
			
			default:
				error("error in statlist");
			break;
		}
	}
	
	private void statlistp(){
		switch(look.tag){
			case ';':
				move();
				stat();
				statlistp();
			break;

			case Tag.EOF: //produzione epsilon
			case '}':
			break;

			default:
				error("error in statlistp");
			break;
		}
	}
	
	private void stat(){
		switch (look.tag){
			case '=':
				match('=');
				match(Tag.ID);
				expr();
			break;
			
			case Tag.PRINT:
				match(Tag.PRINT);
				match('(');
				exprlist();
				match(')');
			break;
			
			case Tag.READ:
				match(Tag.READ);
				match('(');
				match(Tag.ID);
				match(')');
			break;
			
			case Tag.COND:
				match(Tag.COND);
				whenlist();
				match(Tag.ELSE);
				stat();
			break;
			
			case Tag.WHILE:
				match(Tag.WHILE);
				match('(');
				bexpr();
				match(')');
				stat();
			break;
			
			case '{':
				match('{');
				statlist();
				match('}');
			break;
			
			default:
				error("error in stat");
			break;
		}
	}
	
	private void whenlist(){
		switch (look.tag){
			case Tag.WHEN:
				whenitem();
				whenlistp();
			break;
			
			default:
				error("error in whenlist");
			break;
		}
	}
	
	private void whenlistp(){
		switch (look.tag){
			case Tag.WHEN:
				whenitem();
				whenlistp();
			break;
			
			case Tag.ELSE:
			break;
			
			default:
				error("error in whenlistp");
			break;
		}
	}
	
	private void whenitem(){
		switch (look.tag){
			case Tag.WHEN:
				match(Tag.WHEN);
				match('(');
				bexpr();
				match(')');
				match(Tag.DO);
				stat();
			break;
			
			default:
				error("error in whenitem");
			break;
		}
	}
	
	private void bexpr(){
		switch (look.tag){
			case Tag.RELOP:
				match(Tag.RELOP);
				expr();
				expr();
			break;
			
			default:
				error("error in bexpr");
			break;
		}
	}
	
	private void expr(){
		switch(look.tag){
			case '+':
				match('+');
				match('(');
				exprlist();
				match(')');
			break;

			case '-':
				match('-');
				expr();
				expr();
			break;

			case '*':
				match('*');
				match('(');
				exprlist();
				match(')');
			break;

			case '/':
				match('/');
				expr();
				expr();
			break;

			case Tag.NUM:
				match(Tag.NUM);
			break;

			case Tag.ID:
				match(Tag.ID);
			break;
		  
			default:
				error("error in expr");
			break;
		}
	}
	
	private void exprlist(){
		switch (look.tag){
			case '+':
			case '-':
			case '*':
			case '/':
			case Tag.NUM:
			case Tag.ID:
				expr();
				exprlistp();
			break;
			
			default:
				error("error in exprlist");
			break;
		}
	}
	
	private void exprlistp(){
		switch (look.tag){
			case '+':
			case '-':
			case '*':
			case '/':
			case Tag.NUM:
			case Tag.ID:
				expr();
				exprlistp();
			break;
			
			case ')':
			break;
			
			default:
				error("error in exprlistp");
			break;
		}
	}
	
	public static void main(String[] args){
		LexerCompleto lex = new LexerCompleto();
		String path = "parser.txt"; // il percorso del file da leggere
		
		try{
			BufferedReader br = new BufferedReader(new FileReader(path));
			Parser_2 parser = new Parser_2(lex, br);
			parser.prog();
			System.out.println("Input OK");
			br.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}