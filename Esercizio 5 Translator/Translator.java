import java.io.*;

public class Translator {
    private LexerCompleto lex;
    private BufferedReader pbr;
    private Token look;
    
    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count=0;
	int counter =-1;

    public Translator(LexerCompleto l, BufferedReader br) {
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
		if (look.tag == t) {
	    if (look.tag != Tag.EOF) move();
		} else error("syntax error");
    }

    public void prog(){        
		switch(look.tag){
			case '=': 
			case Tag.PRINT: 
			case Tag.READ: 
			case Tag.COND:  //condizionale cond
			case Tag.WHILE:  
			case '{': {
							int lnext_prog = code.newLabel();
							statlist(lnext_prog);
							code.emitLabel(lnext_prog);
							match(Tag.EOF);
							
							try{
									code.toJasmin();
							}catch(java.io.IOException e){
									System.out.println("IO error\n");
								 };
			break;}
			
			default: error("error in prog");
		}
    }
	
	
	
	private void statlist(int lnext_statlist) {
		switch(look.tag){
		
			case '=': 
			case Tag.PRINT:  
			case Tag.READ:  
			case Tag.COND:  //condizionale cond
			case Tag.WHILE:  
			case '{': {
							int l = code.newLabel();
							stat(l); 
							code.emitLabel(l);
							statlistp(lnext_statlist);
			break;}

			default: error("error in statlist");
		}		
    }
	
	
	private void statlistp(int lnext_statlistp) {
		int stat_next = code.newLabel();
		switch(look.tag){
			
			case ';':  
							//int l = code.newLabel();
						match(';'); 
							//stat(l);
						//	lnext_statlistp = code.newLabel();
							//code.emitLabel(lnext_statlistp);
						stat(stat_next);
						code.emitLabel(stat_next);							
							//statlistp(); 
						statlistp(lnext_statlistp); 
			break;
							
			case Tag.EOF:  /*code.emit(OpCode.GOto, lnext_statlistp); */
			break;
			
			
			case '}':  
			break; 
			
			default: error("error in statlistp");
		}
    }
	
	
	

    public void stat(int S_next) {
        switch(look.tag){
	
			case '=': 
						match('=');
						int read_id_addr = st.lookupAddress(((Word)look).lexeme);
						
						if (read_id_addr==-1){
							read_id_addr = count;
							st.insert(((Word)look).lexeme,count++);
						}
						
						match(Tag.ID);
						expr();
						code.emit(OpCode.istore, read_id_addr);
			break;
							
							
			case Tag.PRINT: 
						match(Tag.PRINT);
						match('(');
						exprlist();
						code.emit(OpCode.invokestatic, 1);
						match(')');
			break;
			
            case Tag.READ:
						match(Tag.READ);
						match('(');
						if (look.tag==Tag.ID){
							int id_addr = st.lookupAddress(((Word)look).lexeme);
							if (id_addr==-1) {
								id_addr = count;
								st.insert(((Word)look).lexeme,count++);
							}                    
							match(Tag.ID);
							match(')');
							code.emit(OpCode.invokestatic,0);
							code.emit(OpCode.istore,id_addr);   
						}
						else
							error("Error in grammar (stat) after read( with " + look);
            break;
				
			case Tag.COND: 
						match(Tag.COND);//condizionale cond
						int B_true1 = code.newLabel();							
						int B_false1 = code.newLabel();	
						whenlist(B_true1,B_false1,S_next);
						code.emit(OpCode.GOto, S_next);/*goto s1.next*/							
						code.emitLabel(B_false1);/*B.false: S2.code*/
						match(Tag.ELSE);
						stat(S_next);/*S2.code*/
						//	code.emit(OpCode.GOto, S_next);/*goto s2.next*/
			break;										
							
/*
if(B) S1 else S2
B.true = newlabel()
B.false = newlabel()
S1.next= S.next
S2.next= S.next
S.oode = B.code | B.true:S1.code | goto S1.next | B.false: S2.code | goto s2.next*/			
							
			case Tag.WHILE: 
						int S1next = code.newLabel();		//S1.next = newlabel
						code.emitLabel(S1next);			 	//S1.next: B.code
						match(Tag.WHILE);
						match('(');
						int B_true = code.newLabel();		//B.true = newlabel()					
						int B_false = S_next;				//B.false = S.next
						//int S1next = code.newLabel();		//S1.next = newlabel
						//code.emitLabel(S1next);			 	//S1.next: B.code
						bexpr(B_true, B_false);
						match(')');
						code.emitLabel(B_true);				//B.true:S1.code
						stat(S_next);
						code.emit(OpCode.GOto, S1next);		//goto S1.next
					//		code.emitLabel(B_false);
			break;
/*
S->while(B)S1
B.true = newlabel()
B.false = S.next
S1.next = newlabel
S.code = S1.next: B.code | B.true:S1.code| goto S1.next
*/							
							
			case '{': 
					match('{');
					statlist(S_next);
					match('}');
			break;
							
			//case Tag.EOF  : break;
			//case '}'      : break; 
			
			
			default: error("error in stat");
        }
     }
	 
	private void whenlist(int b_true, int b_false, int S_next) {
		switch (look.tag) {
			case Tag.WHEN: 
							whenitem(b_true,b_false,S_next) ;
							whenlistp(b_true,b_false,S_next);
			break;
			
		default: error("error in whenlist");
		}
		
		
    }

    private void whenlistp(int b_true, int b_false,int S_next) {
		switch (look.tag){
			case Tag.WHEN: 
							whenitem(b_true,b_false,S_next) ;
							whenlistp(b_true,b_false,S_next);
			break;
		
			case Tag.ELSE: 
			break; 
			
			default: error("error in whenlistp");
		}
    }

    private void whenitem(int b_true,int b_false,int S_next) {
		switch (look.tag){
			case Tag.WHEN: 
							match(Tag.WHEN);
							match('(');
							bexpr(b_true,b_false);
							match(')');
							match(Tag.DO);
							code.emitLabel(b_true); /*B.true:S1.code*/
							stat(S_next);/*S1.code*/
						//	code.emit(OpCode.GOto, S_next);/* goto S1.next */
			break;
/*
if(B) S1 else S2
B.true = newlabel()
B.false = newlabel()
S1.next= S.next
S2.next= S.next
S.oode = B.code | B.true:S1.code | goto S1.next | B.false: S2.code | goto s2.next*/			
			default: error("error in whenitem");
		}	
    }
	 
	private void bexpr(int B_true_bexpr, int B_false_bexpr) {
		
		int i ;
		
		switch (look.tag){
			case Tag.RELOP:	
							if(look == Word.lt){
								match(Tag.RELOP);
								expr();
								expr();
								code.emit(OpCode.if_icmplt, B_true_bexpr);	 
								code.emit(OpCode.GOto,B_false_bexpr);
								
							}else if(look == Word.gt){
										match(Tag.RELOP);
										expr();
										expr();
										code.emit(OpCode.if_icmpgt, B_true_bexpr);  
										code.emit(OpCode.GOto,B_false_bexpr);
								
							}else if(look == Word.eq){
										match(Tag.RELOP);
										expr();
										expr();
										code.emit(OpCode.if_icmpeq, B_true_bexpr);  
										code.emit(OpCode.GOto,B_false_bexpr);
			
							}else if(look == Word.le){
										match(Tag.RELOP);
										expr();
										expr();
										code.emit(OpCode.if_icmple, B_true_bexpr);	 
										code.emit(OpCode.GOto,B_false_bexpr);
										
							}else if(look == Word.ne){ 
										match(Tag.RELOP);
										expr();
										expr();
										code.emit(OpCode.if_icmpne, B_true_bexpr);	
										code.emit(OpCode.GOto,B_false_bexpr);
										
							}else if(look == Word.ge){
										match(Tag.RELOP);
										expr();
										expr();
										code.emit(OpCode.if_icmpge, B_true_bexpr); 
										code.emit(OpCode.GOto,B_false_bexpr);
								
							}
			break;
							
			default: error("error in bexpr");
		}
    }
	
	

    private void expr() {
        switch(look.tag) {
            case '-':
						match('-');
						expr();
						expr();
						code.emit(OpCode.isub);
						counter--;
			break;
	
			case '+': 
						match('+');
						match('(');
						exprlist();
						match(')');
						
						while(counter>0){
							code.emit(OpCode.iadd);
							counter--;
						}
			break;
							  
			case '*': 
						match('*');
						match('(');
						exprlist();
						match(')');
						
						while(counter>0){
							code.emit(OpCode.imul);
							counter--;
						}
			break;
			
			case '/': 
						match('/');
						expr();
						expr();
						code.emit(OpCode.idiv);
						counter--;
			break;
			
			case Tag.NUM: 
						counter++;
						int v = ((NumberTok)look).n;
						code.emit(OpCode.ldc, v);
						match(Tag.NUM);
			break;
			
			case Tag.ID: 
						int read_id_addr = st.lookupAddress(((Word)look).lexeme);
						match(Tag.ID);
						
						if (read_id_addr==-1) error("Errore in caricamento");
							   
						code.emit(OpCode.iload, read_id_addr);
			break;
							  
			default:  
					 error("error in expr");
        }
    }

	private void exprlist() {
		switch (look.tag){
			
			case '+': 
			case '-': 
			case '*': 
			case '/': 
			case Tag.NUM:  
			case Tag.ID: {
							expr();
							exprlistp();
			break;}
			
			default: error("error in exprlist");
		}
    }
	
	private void exprlistp() {
		switch (look.tag){
			
			case '+': 
			case '-': 
			case '*': 
			case '/': 
			case Tag.NUM: 
			case Tag.ID: {
							expr();
							exprlistp();
			break;}
			
			case ')': 
			break;

			default: error("error in exprlistp");
		}		
    }

	public static void main(String[] args) {
        LexerCompleto lex = new LexerCompleto();
        String path = "prova.lft"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Translator translator = new Translator(lex, br);
            translator.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
//java -jar jasmin.jar Output.j
//java Output
}