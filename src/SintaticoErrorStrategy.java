
import org.antlr.v4.runtime.ANTLRErrorStrategy;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author camilo
 */
public class SintaticoErrorStrategy implements ANTLRErrorStrategy {

    SaidaParser out;

    @Override
    public void reset(Parser parser) {

    }

    public SintaticoErrorStrategy(SaidaParser out) {
        this.out = out;
    }

    @Override
    public Token recoverInline(Parser parser) throws RecognitionException {
        String tkName = parser.getCurrentToken().getText();
        if(tkName.equals("<EOF>")) tkName = "EOF";
        
        out.println("Linha " + parser.getCurrentToken().getLine()
                + ": erro sintatico proximo a " + tkName);

        out.println("Fim da compilacao");
        parser.reset();
        return null;
    }

    @Override
    public void recover(Parser parser, RecognitionException re) throws RecognitionException {

    }

    @Override
    public void sync(Parser parser) throws RecognitionException {
        
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
        String tkName = parser.getCurrentToken().getText();
        if(tkName.equals("<EOF>")) tkName = "EOF";
        out.println("Linha " + re.getOffendingToken().getLine()
                + ": erro sintatico proximo a " + tkName);

        out.println("Fim da compilacao");
        parser.reset();
    }

}
