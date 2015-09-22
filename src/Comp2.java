//import src.LAParser.ProgramaContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class Comp2 {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        SaidaParser saidaSintatico = new SaidaParser(), 
                saidaSemantico = new SaidaParser(),
                saidaCodigoGerado = new SaidaParser();
        ANTLRInputStream input;
        input = new ANTLRInputStream(
                new FileInputStream(args[0]));

        LALexer lexer = new LALexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        LAParser parser = new LAParser(tokens);

        parser.setErrorHandler(new SintaticoErrorStrategy(saidaSintatico));
        //System.out.println("OUT1:"+ sintatico.toString());
        try {
            LAParser.ProgramaContext tree = parser.programa();
            //System.out.println("OUT2:"+ sintatico.toString());
            
            if(saidaSintatico.toString().isEmpty()){
                LAListener l = new Comp2Listener(saidaSemantico, parser.escopos);
                ParseTreeWalker ptw = new ParseTreeWalker();
                ptw.walk(l, tree);
            }

            if (saidaSemantico.toString().isEmpty()) {
                LAVisitor v = new Comp2Visitor(saidaSintatico);
                v.visitPrograma(tree);
            }

        } catch (ParseCancellationException pce) {
            saidaSintatico.println(pce.getMessage());
            saidaSintatico.println("Fim da compilacao");
        } catch (Exception e) {
            //System.out.println(e.getMessage());
        }
        
        //System.out.println("OUT3:"+ out.toString());

        PrintWriter pw = new PrintWriter(new File(args[1]));
        pw.print(saidaSintatico.toString() 
                + saidaSemantico.toString() 
                + saidaCodigoGerado.toString());
        pw.flush();
        pw.close();
        
    }

}
