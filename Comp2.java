//import src.LAParser.ProgramaContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;

public class Comp2 {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        JFileChooser fileChooser = new JFileChooser();
        File folder = null;
        
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(new JPanel());
        if (result == JFileChooser.APPROVE_OPTION) {
            folder = fileChooser.getSelectedFile();
        }
        
        File[] files = folder.listFiles();
        Arrays.sort(files);
        for(File f: files){
        
            System.out.println("Selected file: " + f.getName() 
                    + " " + f.length() + " Bytes");

            ANTLRInputStream input;
            input = new ANTLRInputStream(
                    new FileInputStream(f));

            LALexer lexer = new LALexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);

            LAParser parser = new LAParser(tokens);

            Comp2ErrorStrategy strategy = new Comp2ErrorStrategy();
            parser.setErrorHandler(new Comp2ErrorStrategy());
            try{
                parser.programa();
            } catch (ParseCancellationException pce){
                System.out.println(pce.getMessage());
                System.out.println("Fim da compilacao");
            } catch( Exception e){
                
            }
            
            System.out.println();
        
        }
    }

}
