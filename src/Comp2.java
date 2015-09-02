//import src.LAParser.ProgramaContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

public class Comp2 {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        JFileChooser fileChooser = new JFileChooser();
        File selectedFile = null;
        
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(new JPanel());
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
        }
        
        System.out.println("Selected file: " + selectedFile.getName() 
                + " " + selectedFile.length() + " Bytes");
        
        ANTLRInputStream input;
        input = new ANTLRInputStream(
                new FileInputStream(selectedFile));

        LALexer lexer = new LALexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        LAParser parser = new LAParser(tokens);
        parser.programa();
    }

}
