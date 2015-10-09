
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
        parser.removeErrorListeners();
        try {
            LAParser.ProgramaContext tree = parser.programa();
            
            if(saidaSintatico.toString().isEmpty()){
                LAListener l = new Comp2Listener(saidaSemantico);
                ParseTreeWalker ptw = new ParseTreeWalker();
                ptw.walk(l, tree);
            }

            if (saidaSemantico.toString().isEmpty()) {
                LAVisitor v = new Comp2Visitor(saidaCodigoGerado);
                v.visitPrograma(tree);
            }

        } catch (ParseCancellationException pce) {
            saidaSintatico.println(pce.getMessage());
        } catch (Exception e) {
            //System.out.println("Erro:" + e.getMessage());
            e.printStackTrace();
        }
        
        if(!saidaSintatico.toString().isEmpty() || !saidaSemantico.toString().isEmpty())
            saidaCodigoGerado.println("Fim da compilacao");
        
        System.out.println("Sintatico:\n"+ saidaSintatico.toString() 
                + "\nSemantico:\n"+ saidaSemantico.toString() 
                + "\nCódigo:\n"+ saidaCodigoGerado.toString());

        PrintWriter pw = new PrintWriter(new File(args[1]));
        pw.print(saidaSintatico.toString() 
                + saidaSemantico.toString() 
                + saidaCodigoGerado.toString());
        pw.flush();
        pw.close();
        
    }

}
