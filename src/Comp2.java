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
        SaidaParser sintatico = new SaidaParser(), 
                semantico = new SaidaParser(),
                codigoGerado = new SaidaParser();
        ANTLRInputStream input;
        input = new ANTLRInputStream(
                new FileInputStream(args[0]));

        LALexer lexer = new LALexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        LAParser parser = new LAParser(tokens);

        parser.setErrorHandler(new SintaticoErrorStrategy(sintatico));
        //System.out.println("OUT1:"+ sintatico.toString());
        try {
            LAParser.ProgramaContext tree = parser.programa();
            //System.out.println("OUT2:"+ sintatico.toString());
            
            if(sintatico.toString().isEmpty()){
                LAListener l = new Comp2Listener(semantico, parser.escopos);
                ParseTreeWalker ptw = new ParseTreeWalker();
                ptw.walk(l, tree);
            }

            if (semantico.toString().isEmpty()) {
                LAVisitor v = new Comp2Visitor(sintatico);
                v.visitPrograma(tree);
            }

        } catch (ParseCancellationException pce) {
            sintatico.println(pce.getMessage());
            sintatico.println("Fim da compilacao");
        } catch (Exception e) {
            //System.out.println(e.getMessage());
        }
        
        //System.out.println("OUT3:"+ out.toString());

        PrintWriter pw = new PrintWriter(new File(args[1]));
        pw.print(sintatico.toString() 
                + semantico.toString() 
                + codigoGerado.toString());
        pw.flush();
        pw.close();
        
    }

}
