
import java.util.BitSet;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ANTLRErrorStrategy;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author camilo
 */
public class Comp2ErrorStrategy implements ANTLRErrorStrategy {

    @Override
    public void reset(Parser parser) {

    }

    @Override
    public Token recoverInline(Parser parser) throws RecognitionException {
        System.out.println("Linha " + parser.getCurrentToken().getLine() +
                ": erro sintatico proximo a " + parser.getCurrentToken().getText());
        
        System.out.println("Fim de Compilacao");
        parser.reset();
        return null;
    }

    @Override
    public void recover(Parser parser, RecognitionException re) throws RecognitionException {
        
    }

    @Override
    public void sync(Parser parser) throws RecognitionException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean inErrorRecoveryMode(Parser parser) {
        return false;
    }

    @Override
    public void reportMatch(Parser parser) {
        
    }

    @Override
    public void reportError(Parser parser, RecognitionException re) {
        System.out.println("Linha " + re.getOffendingToken().getLine() +
                ": erro sintatico proximo a " + re.getOffendingToken().getText());
        
        System.out.println("Fim de Compilacao");
        parser.reset();
    }
    
}
