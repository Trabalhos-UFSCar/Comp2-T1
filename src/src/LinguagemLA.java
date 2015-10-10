package src;

import antlr.*;
import utils.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class LinguagemLA {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        //Cria uma saida para cada etapa. O resultado é a some de todas elas.
        SaidaParser saidaSintatico = new SaidaParser(), 
                saidaSemantico = new SaidaParser(),
                saidaCodigoGerado = new SaidaParser();
        
        ANTLRInputStream input;
        input = new ANTLRInputStream(
                new FileInputStream(args[0]));

        LALexer lexer = new LALexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        LAParser parser = new LAParser(tokens);
        
        //Adiciona o ErrorStrategy e remove listeners que não seriam usados.
        parser.setErrorHandler(new SintaticoErrorStrategy(saidaSintatico));
        parser.removeErrorListeners();
        
        try {
            LAParser.ProgramaContext tree = parser.programa();
            // Prossegue se nao tem erros sintaticos
            if(saidaSintatico.toString().isEmpty()){
                LAListener l = new SemanticoListener(saidaSemantico);
                ParseTreeWalker ptw = new ParseTreeWalker();
                ptw.walk(l, tree);
            }
            // Prossegue se nao tem erros semanticos
            if (saidaSemantico.toString().isEmpty()) {
                LAVisitor v = new GeracaoCodigoVisitor(saidaCodigoGerado);
                v.visitPrograma(tree);
            }

        } catch (ParseCancellationException pce) {
            saidaSintatico.println(pce.getMessage());
        } catch (NullPointerException e) {
            // Recebe NullPointer do sintatico para parar depois
            // do primeiro erro. Não precisa de nenhum tratamento.
        }
        
        if(!saidaSintatico.toString().isEmpty() || !saidaSemantico.toString().isEmpty())
            saidaCodigoGerado.println("Fim da compilacao");
        
        // VERBOSE
        /* System.out.println("Sintatico:\n"+ saidaSintatico.toString() 
                + "\nSemantico:\n"+ saidaSemantico.toString() 
                + "\nCódigo:\n"+ saidaCodigoGerado.toString()); */

        PrintWriter pw = new PrintWriter(new File(args[1]));
        pw.print(saidaSintatico.toString() 
                + saidaSemantico.toString() 
                + saidaCodigoGerado.toString());
        pw.flush();
        pw.close();
        
    }

}
