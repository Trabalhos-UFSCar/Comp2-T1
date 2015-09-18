import java.util.HashMap;
import java.util.Map;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * This class provides an empty implementation of {@link LAListener}, which can
 * be extended to create a listener which only needs to handle a subset of the
 * available methods.
 */
public class Comp2Listener extends LABaseListener {
    
    SaidaParser out;
    Escopos escopos;
    Map<String, String> tipo;
    
    //TODO: encontrar momentos em que as entradas são refenciadas e checar se elas realmente existem nas tabelas
   
    public Comp2Listener(SaidaParser out, Escopos escopos) {
        this.out = out;
        this.escopos = escopos;
        this.tipo = new HashMap<>();
        
        // add os tipos em LA (key) e C (value)
        tipo.put("inteiro", "int");
        tipo.put("literal", "char");    // TODO: verificar como fazer literal em C
        tipo.put("real", "float");
        tipo.put("logico", "int");  // contorno para bool em C
    }

    @Override
    public void enterPrograma(LAParser.ProgramaContext ctx) {
        escopos.empilhar("global");
    }

    @Override
    public void exitPrograma(LAParser.ProgramaContext ctx) {
        /* out.println("exitPrograma"); */
    }

    @Override
    public void enterVariavel(LAParser.VariavelContext ctx) {
        /* out.println("enterVariavel"); */
                
        if(escopos.existeSimbolo(ctx.IDENT().getText())){
            out.println("Linha "+ctx.IDENT().getSymbol().getLine()+": identificador "+ctx.IDENT()+" ja declarado anteriormente");
        }
        else System.out.println("Variavel adicionada. Linha: " + ctx.IDENT().getSymbol().getLine()+" : "+ctx.IDENT().getSymbol().getText() );
        
        String vars = "";
        
        //TODO: substituir essa definição de tipo pela chamada do mapa()
        if (ctx.tipo().getText().equals("inteiro")) {
            vars = "int ";
        }
    }

    @Override
    public void enterMais_var(LAParser.Mais_varContext ctx) {
        //TODO: corrigir bug: o erro não está sendo adicionada corretamente quando ele ocorre
        if(escopos.existeSimbolo(ctx.IDENT().getText())){
            out.println("Linha "+ctx.IDENT().getSymbol().getLine()+": identificador "+ctx.IDENT()+" ja declarado anteriormente");
        }
       else System.out.println("Variavel adicionada. Linha: " + ctx.IDENT().getSymbol().getLine()+" : "+ctx.IDENT() );
        
    }

    @Override
    public void enterIdentificador(LAParser.IdentificadorContext ctx) {
        /* out.println("enterIdentificador"); */
    }

    @Override
    public void enterCmd(LAParser.CmdContext ctx) {
        /* out.println("enterCmd"); */
//            out.println(ctx.toStringTree());
//        if (ctx.getStart().getText().equals("leia")) {
//            out.print("    scanf(\"%d\",&");
//        } else if (ctx.getStart().getText().equals("escreva")) {
//            out.print("    printf(\"%d\",");
//            ctx.expressao();
//            out.println("x);");
//        }
    }

    @Override
    public void exitCmd(LAParser.CmdContext ctx) {
        /* out.println("exitCmd"); */
//        if (ctx.getStart().getText().equals("leia")) {
//            out.println(");");
//        }
    }

}
