package src;

//import src.LAParser.ProgramaContext;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

public class Comp2 {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        ANTLRInputStream input = new ANTLRInputStream(
                new FileInputStream(args[0]));

        LALexer lexer = new LALexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LAParser parser = new LAParser(tokens);
        parser.programa();
    }

}
