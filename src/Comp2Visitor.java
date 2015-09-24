// Generated from .\src\LA.g4 by ANTLR 4.5.1
import java.util.HashMap;
import java.util.Map;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

/**
 * This class provides an empty implementation of {@link LAVisitor},
 * which can be extended to create a visitor which only needs to handle a subset
 * of the available methods.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public class Comp2Visitor<T> extends LABaseVisitor<T> {
    
    public SaidaParser out;
    Escopos escopos;
    Map<String, String> tipo;
    VerificadorDeTipos vdt;
    //Essa variavel é usada para contornar a dificuldade que é decidir o tipo de uma variavel
    //enquanto na regra mais_var. 
    String tipoAtual = "";
    
    public Comp2Visitor(SaidaParser out){
        this.out = out;
        this.escopos = new Escopos();
        this.tipo = new HashMap<>();
        this.vdt = new VerificadorDeTipos(escopos);

        // add os tipos em LA (key) e C (value)
        tipo.put("inteiro", "int");
        tipo.put("literal", "char");    // TODO: verificar como fazer literal em C
        tipo.put("real", "float");
        tipo.put("logico", "int");  // contorno para bool em C
    }
    
    public boolean regraVazia(ParserRuleContext ctx){
        return ctx.getText().equals("");
    } 

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitPrograma(LAParser.ProgramaContext ctx) { 
        escopos.empilhar("global");
        
        out.println("/* Arquivo gerado automaticamente */\n");
        out.println("#include <stdio.h>");
        out.println("#include <stdlib.h>\n");
        
        out.println("int main(){");
        out.identationLevel++;
        
        visitDeclaracoes(ctx.declaracoes());
        
        visitCorpo(ctx.corpo());
        
        out.println("return 0;");
        out.identationLevel--;
        out.println("}");
        
        escopos.desempilhar();
        
        return null; 
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitDeclaracoes(LAParser.DeclaracoesContext ctx) { 
        System.out.println("Passei delcaracoes");
//        out.print();
        return visitChildren(ctx); 
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitDecl_local_global(LAParser.Decl_local_globalContext ctx) { 
        return visitChildren(ctx); 
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitDeclaracao_local(LAParser.Declaracao_localContext ctx) {
        if(!regraVazia(ctx.variavel())){
            visitVariavel(ctx.variavel());
        }
        return visitChildren(ctx);
    }
    
    @Override public T visitVariavel(LAParser.VariavelContext ctx) { 
        String nome = ctx.nome.getText();
        tipoAtual = ctx.tipo().getText();
        escopos.adicionarSimbolo(nome, tipoAtual);
        
        String variavel = 
         tipo.get(tipoAtual) +
         " " +
         nome + 
         visitDimensao(ctx.dimensao()) +
         visitMais_var(ctx.mais_var()) +
         ";"
        ;
        
        out.println(variavel);
        
        tipoAtual = "";
        
        return visitChildren(ctx); 
    }
    
    @Override public T visitDimensao(LAParser.DimensaoContext ctx) {
        if(regraVazia(ctx)){
            return (T) "";
        }
        
        String dimensao = 
         "[" +
         visitExp_aritmetica(ctx.exp_aritmetica()) +
         "]"
        ;
        
        return (T) dimensao; 
    }
    
    @Override public T visitMais_var(LAParser.Mais_varContext ctx) { 
        if(regraVazia(ctx)){
            return (T) "";
        }
        
        return visitChildren(ctx); 
    }
    
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitDeclaracao_global(LAParser.Declaracao_globalContext ctx) { 
        System.out.println("passei global");
//        out.println("global");
        return visitChildren(ctx); 
    }
    
    @Override public T visitCorpo(LAParser.CorpoContext ctx) {
        escopos.empilhar("corpo");
        
        visitDeclaracoes_locais(ctx.declaracoes_locais());
        
        visitComandos(ctx.comandos());
        
        escopos.desempilhar();
        
        return visitChildren(ctx); 
    }
    
    @Override public T visitDeclaracoes_locais(LAParser.Declaracoes_locaisContext ctx){
        if(regraVazia(ctx)){
            return null;
        }
        
        visitDeclaracao_local(ctx.declaracao_local());
        
        visitDeclaracoes_locais(ctx.declaracoes_locais());
        
        return visitChildren(ctx);
    }
    
    @Override public T visitComandos(LAParser.ComandosContext ctx) { 
        if(regraVazia(ctx)){
            return null;
        }
        
        visitCmd(ctx.cmd());
        
        visitComandos(ctx.comandos());

        return visitChildren(ctx); 
    }
    
    @Override public T visitCmd(LAParser.CmdContext ctx) {
        String cmd = "";
        
        if(ctx.getStart().getText().equals("leia")){
            String nome = (String) visitIdentificador(ctx.identificador());
            String tipo = escopos.buscaSimbolo(nome).getTipo();
        
            if(tipo.equals("inteiro")){
                cmd += "scanf(\"%d\",&" + nome + ");";
            }else if(tipo.equals("real")){
                cmd += "scanf(\"%f\",&" + nome + ");";
            }
        }else if(ctx.getStart().getText().equals("escreva")){
            
            System.out.println("Passei cmd");
            if(vdt.verificaTipo(ctx.expressao()).equals("inteiro")){
                cmd += "printf(\"%d"+"\", " + visitExpressao(ctx.expressao()) + ");";
            }else if(vdt.verificaTipo(ctx.expressao()).equals("real")){
                cmd += "printf(\"%f"+"\", " + visitExpressao(ctx.expressao()) + ");";
            }
        }
        
        System.out.println(ctx.getText());
        
        out.println(cmd);
        
        return visitChildren(ctx); 
    }
    
    @Override public T visitExpressao(LAParser.ExpressaoContext ctx) { 
        String expressao = "";
        System.out.println(ctx.getText());
        
        expressao = (String) visitExp_aritmetica(ctx.termo_logico().fator_logico().parcela_logica().exp_relacional().exp_aritmetica());
        
        return (T) expressao; 
    }
    
    @Override public T visitExp_aritmetica(LAParser.Exp_aritmeticaContext ctx) { 
        String exp_aritmetica = "";
        System.out.println(ctx.getText());
        
        exp_aritmetica += (String) visitParcela_unario(ctx.termo().fator().parcela().parcela_unario());
        
        return (T) exp_aritmetica; 
    }
    
    @Override public T visitParcela_unario(LAParser.Parcela_unarioContext ctx) { 
        String parcela_unario = "";
        
        if(!ctx.IDENT().getText().equals("")){
            parcela_unario += ctx.IDENT().getText();
        }else if(!ctx.NUM_INT().getText().equals("")){
            parcela_unario += ctx.NUM_INT().getText();
        }else if(!ctx.NUM_REAL().getText().equals("")){
            parcela_unario += ctx.NUM_REAL().getText();
        }
        
        return (T) parcela_unario; 
    }
    
    @Override public T visitIdentificador(LAParser.IdentificadorContext ctx) {
        return (T) ctx.IDENT().getText(); 
    }
}