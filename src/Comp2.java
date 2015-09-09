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
//        JFileChooser fileChooser = new JFileChooser();
//        File folder = null;
//        
//        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
//        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//        int result = fileChooser.showOpenDialog(new JPanel());
//        if (result == JFileChooser.APPROVE_OPTION) {
//            folder = fileChooser.getSelectedFile();
//        }
//        
//        File[] files = folder.listFiles();
//        Arrays.sort(files);
//        for(File f: files){
//        
//            System.out.println("Selected file: " + f.getName() 
//                    + " " + f.length() + " Bytes");

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

            LAListener l = new LABaseListener();
            ParseTreeWalker ptw = new ParseTreeWalker();
            ptw.walk(l, tree);
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
