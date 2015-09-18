//import src.LAParser.ProgramaContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

public class Comp2 {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        SaidaParser out = new SaidaParser();
        ANTLRInputStream input;
        input = new ANTLRInputStream(
                new FileInputStream(args[0]));

        LALexer lexer = new LALexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        LAParser parser = new LAParser(tokens);

        parser.setErrorHandler(new Comp2ErrorStrategy(out));
        try {
            LAParser.ProgramaContext tree = parser.programa();

            LAListener l = new Comp2Listener(out, parser.escopos);
            ParseTreeWalker ptw = new ParseTreeWalker();
            ptw.walk(l, tree);
            
            LAVisitor v = new Comp2Visitor(out);
            v.visitPrograma(tree);
        } catch (ParseCancellationException pce) {
            out.println(pce.getMessage());
            out.println("Fim da compilacao");
        } catch (Exception e) {

        }

        PrintWriter pw = new PrintWriter(new File(args[1]));
        pw.print(out.toString());
        pw.flush();
        pw.close();
//            System.out.println();
//        
//        }
    }

}
