/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comp2;

import comp2.LAParser.ProgramaContext;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

/**
 *
 * @author Jp
 */
public class Comp2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        ANTLRInputStream input = new ANTLRInputStream(
                new FileInputStream(args[0]));
        LALexer lexer = new LALexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LAParser parser = new LAParser(tokens);
        parser.programa();
    }

}
