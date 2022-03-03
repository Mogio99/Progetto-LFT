import java.io.*;

public class Parser_1{
    private LexerCompleto lex;
    private BufferedReader pbr;
    private Token look;

    public Parser_1(LexerCompleto l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    void move() {
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    void error(String s) {
	throw new Error("near line " + lex.line + ": " + s);
    }

    void match(int t) {
	if (look.tag == t) { //look.tag legge i simboli, quindi devo ciclare questo
	    if (look.tag != Tag.EOF)
          move();
	} else error("syntax error");
    }

    public void start() {	
		switch (look.tag) {
			case '(':
			case Tag.NUM:
				expr();
				match(Tag.EOF);
			break;
			
			default:
				error("error in start");
			break;
		}
    }

    private void expr() {
		switch (look.tag) {
			case '(':
			case Tag.NUM:
				term();
				exprp();
			break;
			
			default:
				error("error in expr");
			break;
		}
	}

    private void exprp() {
		switch (look.tag) {
			case '+':
				match('+');
				term();
				exprp();
			break;
			
			case '-':
				match('-');
				term();
				exprp();
			break;
			
			case ')':
			case Tag.EOF:
			break;
			
			default:
				error("error in exprp");
			break;
		}
    }

    private void term() {
		switch (look.tag) {
			case '(':
			case Tag.NUM:
				fact();
				termp();
			break;
			
			default:
				error("error in term");
			break;
		}
	}

    private void termp() {
        switch (look.tag) {
			case '*':
				match('*');
				fact();
				termp();
			break;
			
			case '/':
				match('/');
				fact();
				termp();
			break;
			
			case '+':
			case '-':
			case ')':
			case Tag.EOF:
			break;
			
			default:
				error("error in termp");
			break;
		}
    }

    private void fact() {
        switch (look.tag) {
			case '(':
				match('(');
				expr();
				match(')');
			break;

			case Tag.NUM:
				match(Tag.NUM);
			break;
			
			default:
				error("error in fact");
			break;
				
		}
	}

    public static void main(String[] args) {
        LexerCompleto lex = new LexerCompleto();
        String path = "parser.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser_1 parser = new Parser_1(lex, br);
            parser.start();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}