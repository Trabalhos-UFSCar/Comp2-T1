// Generated from .\src\LA.g4 by ANTLR 4.5.1
import java.util.HashMap;
import java.util.Map;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

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
        return ctx == null || ctx.getText().equals("");
    } 
    
    public boolean regraVazia(TerminalNode node){
        return node == null || node.getText().equals("");
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
//        out.print();
        return null; 
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitDecl_local_global(LAParser.Decl_local_globalContext ctx) { 
        return null; 
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
        return null;
    }
    
    @Override public T visitVariavel(LAParser.VariavelContext ctx) { 
        String nome = ctx.nome.getText();
        tipoAtual = ctx.tipo().getText();
        escopos.adicionarSimbolo(nome, tipoAtual);
        System.out.println(ctx.getText());
        
        String variavel = "";
        if(tipoAtual.equals("literal")){
            variavel =
             tipo.get(tipoAtual) +
             " " +
             nome +
             "[80]" +
             visitMais_var(ctx.mais_var()) +
            ";"
           ;
        }else{
            variavel = 
             tipo.get(tipoAtual) +
             " " +
             nome + 
             visitDimensao(ctx.dimensao()) +
             visitMais_var(ctx.mais_var()) +
             ";"
            ;
        }
        
        out.println(variavel);
        
        tipoAtual = "";
        
        return null; 
    }
    
    @Override public T visitDimensao(LAParser.DimensaoContext ctx) {
        if(regraVazia(ctx)){
            return (T) "";
        }
        
        System.out.println(ctx.getText());
        
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
        
        return null; 
    }
    
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitDeclaracao_global(LAParser.Declaracao_globalContext ctx) { 
//        out.println("global");
        return null; 
    }
    
    @Override public T visitCorpo(LAParser.CorpoContext ctx) {
        escopos.empilhar("corpo");
        
        visitDeclaracoes_locais(ctx.declaracoes_locais());
        
        visitComandos(ctx.comandos());
        
        escopos.desempilhar();
        
        return null; 
    }
    
    @Override public T visitDeclaracoes_locais(LAParser.Declaracoes_locaisContext ctx){
        if(regraVazia(ctx)){
            return null;
        }
        
        visitDeclaracao_local(ctx.declaracao_local());
        
        visitDeclaracoes_locais(ctx.declaracoes_locais());
        
        return null;
    }
    
    @Override public T visitComandos(LAParser.ComandosContext ctx) { 
        if(regraVazia(ctx)){
            return null;
        }
        
        visitCmd(ctx.cmd());
        
        visitComandos(ctx.comandos());

        return null; 
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
            }else if(tipo.equals("literal")){
                cmd += "gets(" + nome + ");";
            }
        }else if(ctx.getStart().getText().equals("escreva")){
            cmd += escrevaCmd(ctx.expressao());
            for(LAParser.ExpressaoContext exp : ctx.mais_expressao().expressao()){
                out.println(cmd);
                cmd = escrevaCmd(exp);
            }
        }
        
        out.println(cmd);
        
        return null; 
    }
    
    public String escrevaCmd(LAParser.ExpressaoContext ctx){
        String cmd = "";
        
        if(vdt.verificaTipo(ctx).equals("inteiro")){
            cmd += "printf(\"%d"+"\", " + visitExpressao(ctx) + ");";
        }else if(vdt.verificaTipo(ctx).equals("real")){
            cmd += "printf(\"%f"+"\", " + visitExpressao(ctx) + ");";
        }else if(vdt.verificaTipo(ctx).equals("literal")){
            cmd += "printf(\"%s\", " + visitExpressao(ctx) + ");";
        }
        
        return cmd;
    }
    
    @Override public T visitExpressao(LAParser.ExpressaoContext ctx) { 
        String expressao = "";
        
        expressao = (String) visitExp_aritmetica(ctx.termo_logico().fator_logico().parcela_logica().exp_relacional().exp_aritmetica());
        
        return (T) expressao; 
    }
    
    @Override public T visitExp_aritmetica(LAParser.Exp_aritmeticaContext ctx) { 
        System.out.println(ctx.getText());
        
        if(!regraVazia(ctx.outros_termos())){
            return (T) ((String) visitTermo(ctx.termo()) + (String) visitOutros_termos(ctx.outros_termos()));
        }else{
            return (T) (String) visitTermo(ctx.termo());
        }
    }
    
    @Override public T visitOutros_termos(LAParser.Outros_termosContext ctx) { 
        String outros_termos = "";
        
        for(int i=0; i<ctx.op_adicao().size(); i++){
            outros_termos += ctx.op_adicao(i).getText() + (String) visitTermo(ctx.termo(i));
        }
        return (T)outros_termos; 
    }
    
    @Override public T visitTermo(LAParser.TermoContext ctx) { 
        if(!regraVazia(ctx.outros_fatores())){
            return (T) ((String) visitFator(ctx.fator()) + (String) visitOutros_fatores(ctx.outros_fatores()));
        }else{
            return (T) (String) visitFator(ctx.fator());
        }
    }
    
    @Override public T visitOutros_fatores(LAParser.Outros_fatoresContext ctx) { 
        String outros_fatores = "";
        
        for(int i=0; i<ctx.op_multiplicacao().size(); i++){
            outros_fatores += ctx.op_multiplicacao(i).getText() + visitFator(ctx.fator(i));
        }
        return (T)outros_fatores;
    }
    
    @Override public T visitFator(LAParser.FatorContext ctx) { 
        if(!regraVazia(ctx.outras_parcelas())){
            return (T) ((String) visitParcela(ctx.parcela()) + (String) visitOutras_parcelas(ctx.outras_parcelas()));
        }else{
            return (T) (String) visitParcela(ctx.parcela());
        }
    }
    
    @Override public T visitParcela(LAParser.ParcelaContext ctx) { 
        String parcela = "";
        
        if(!regraVazia(ctx.parcela_unario())){
            parcela += (String) visitOp_unario(ctx.op_unario()) + (String) visitParcela_unario(ctx.parcela_unario());
        }else{ //Regra é parcela_nao_unario
            parcela += (String) visitParcela_nao_unario(ctx.parcela_nao_unario());
        }
        
        return (T) parcela; 
    }
    
    @Override public T visitOp_unario(LAParser.Op_unarioContext ctx) { return (T) ctx.getText(); }
    
    @Override public T visitParcela_unario(LAParser.Parcela_unarioContext ctx) { 
        String parcela_unario = "";
        
        if(!regraVazia(ctx.IDENT())){
            parcela_unario += ctx.IDENT().getText();
        }else if(!regraVazia(ctx.NUM_INT())){
            parcela_unario += ctx.NUM_INT().getText();
        }else if(!regraVazia(ctx.NUM_REAL())){
            parcela_unario += ctx.NUM_REAL().getText();
        }
        
        return (T) parcela_unario; 
    }
    
    @Override public T visitParcela_nao_unario(LAParser.Parcela_nao_unarioContext ctx) { 
        String parcela_nao_unario = "";
        
        if(!regraVazia(ctx.IDENT())){
            parcela_nao_unario += "&" + ctx.IDENT().getText() + 
             visitOutros_ident(ctx.outros_ident()) +
             visitDimensao(ctx.dimensao());
        }else{ //Regra é CADEIA
            parcela_nao_unario += ctx.CADEIA().getText();
        }
        
        return (T) parcela_nao_unario; 
    }
    
    @Override public T visitIdentificador(LAParser.IdentificadorContext ctx) {
        return (T) ctx.IDENT().getText(); 
    }
}