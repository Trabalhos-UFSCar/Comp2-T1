package src;

import antlr.*;
import utils.*;
import org.antlr.v4.runtime.ANTLRErrorStrategy;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;

/**
 * Esta classse faz a análise sintática da linguagem LA e é implementada
 * por meio de um ANTLRErrorStrategy.
 * 
 * A classe faz uso de dois métodos da interface:
 * recoverInline() - para erros quando é encontrado um token não esperado
 * com recuperação possível
 * reportError() - para erros já identificados e sem recuperação
 * 
 *  Os outros métodos não são usados nesta implementação e portanto
 * não fazem nada quando são chamados.
 * 
 * Apesar que esta implementação não faz uso de recuperação em
 * caso de erro sintático, optou por um ANTLRErrorStrategy ao 
 * invés de um ANTLRErrorListener pois esta última precisa
 * de uma implementação de um ANTLRErrorStrategy para que seja 
 * possível parar a execução após o primeiro erro.
 * 
 */
public class SintaticoErrorStrategy implements ANTLRErrorStrategy {

    SaidaParser out;

    @Override
    public void reset(Parser parser) {

    }

    public SintaticoErrorStrategy(SaidaParser out) {
        this.out = out;
    }
    
    /**
     * Este método insere uma mensagem de erro para a saída e retorna null
     * para gerar um exceção que vai parar a execução da checagem sintática.
     * 
     * @return null, para parar a execução com uma NullPointerException
     */
    @Override
    public Token recoverInline(Parser parser) throws RecognitionException {
        String tkName = parser.getCurrentToken().getText();
        if(tkName.equals("<EOF>")) tkName = "EOF";
        
        out.println("Linha " + parser.getCurrentToken().getLine()
                + ": erro sintatico proximo a " + tkName);
        parser.reset();
        return null;
    }

    @Override
    public void recover(Parser parser, RecognitionException re) throws RecognitionException {

    }

    @Override
    public void sync(Parser parser) throws RecognitionException {
        
    }
    
    /**
     * Este método retorna falso para que o método reportError() pare
     * a execução após completo (ao invés de tentar recuperar ).
     * 
     * @return false, para indicar que recuperação não é possível.
     */
    @Override
    public boolean inErrorRecoveryMode(Parser parser) {
        return false;
    }

    @Override
    public void reportMatch(Parser parser) {

    }

    /**
     * Este método insere uma mensagem de erro para a saída e termina.
     * Como o método inErrorRecoveryMode() retorna sempre falso, a
     * classe para a execução após o primeiro erro.
     */
    @Override
    public void reportError(Parser parser, RecognitionException re) {
        String tkName = parser.getCurrentToken().getText();
        if(tkName.equals("<EOF>")) tkName = "EOF";
        out.println("Linha " + re.getOffendingToken().getLine()
                + ": erro sintatico proximo a " + tkName);
        parser.reset();
    }

}
