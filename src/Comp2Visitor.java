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
    boolean isPonteiro = false;
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
        
        visitDeclaracoes(ctx.declaracoes());
        
        out.println("int main(){");
        out.identationLevel++;
        
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
        if(regraVazia(ctx)){
            return null;
        }else{
            visitDecl_local_global(ctx.decl_local_global());
            visitDeclaracoes(ctx.declaracoes());
        }
        
        return null;
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitDecl_local_global(LAParser.Decl_local_globalContext ctx) { 
        if(regraVazia(ctx)){
            return null; 
        }else if(!regraVazia(ctx.declaracao_local())){
            visitDeclaracao_local(ctx.declaracao_local());
        }else if(!regraVazia(ctx.declaracao_global())){
            visitDeclaracao_global(ctx.declaracao_global());
        }
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
        }else if(ctx.getStart().getText().equals("constante")){
            // Trata declaracao constante como variavel normal.
            tipoAtual = ctx.tipo_basico().getText();
            String variavel = variavel(ctx.IDENT().getText(), null);
            out.println(variavel);
            out.println(ctx.IDENT().getText()+" = "+ctx.valor_constante().getText()+";");
            tipoAtual = "";
        }
        return null;
    }
    
    @Override public T visitVariavel(LAParser.VariavelContext ctx) {
        tipoAtual = ctx.tipo().getText();
        
        String variavel = "";
        variavel = variavel(ctx.nome.getText(), ctx.dimensao());
        for(int i=0; i<ctx.mais_var().IDENT().size(); i++){
            out.println(variavel);
            variavel = variavel(ctx.mais_var().IDENT(i).getText(), ctx.mais_var().dimensao(i));
        }
        
        tipoAtual = "";
        
        out.println(variavel);
        
        return null; 
    }
    
    public String variavel(String nome, LAParser.DimensaoContext dimCtx){
        escopos.adicionarSimbolo(nome, tipoAtual);
        
        String variavel = "";
        if(tipoAtual.equals("literal")){
            variavel =
             tipo.get(tipoAtual) +
             " " +
             nome +
             "[80]" +
            ";"
           ;
        }else{
            variavel = 
             tipo.get(tipoAtual) +
             " " +
             nome + 
             visitDimensao(dimCtx) +
             ";"
            ;
        }
        
        return variavel;
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
        }else if(!regraVazia(ctx.chamada_atribuicao())){
            if(ctx.getStart().getText().equals("^")){
                cmd += "*";
                isPonteiro = true;
            }
            cmd += ctx.IDENT().getText() + (String)visitChamada_atribuicao(ctx.chamada_atribuicao());
            isPonteiro = false;
        }else if(ctx.getStart().getText().equals("se")){
            cmd += "if(" + visitExpressao(ctx.expressao()) +"){";
            out.println(cmd);
            
            out.identationLevel++;
            this.escopos.empilhar(ctx.escopoNome);
            visitComandos(ctx.comandos());
            this.escopos.desempilhar();
            out.identationLevel--;
            
            cmd = "}";
            out.println(cmd);
            
            if(!regraVazia(ctx.senao_opcional())){
                out.println("else{");
                visitSenao_opcional(ctx.senao_opcional());
                out.println("}");
            }
            cmd = "";
        }else if(ctx.getStart().getText().equals("caso")){
            cmd += "switch (" + (String)visitExp_aritmetica(ctx.exp_aritmetica(0)) + "){";
            out.println(cmd);
            
            out.identationLevel++;
            this.escopos.empilhar(ctx.escopoNome);
            visitSelecao(ctx.selecao());
            if(!regraVazia(ctx.senao_opcional())){
                cmd = "default:";
                out.println(cmd);
                visitSenao_opcional(ctx.senao_opcional());
            }
            this.escopos.desempilhar();
            out.identationLevel--;
            cmd = "}";
        }else if(ctx.getStart().getText().equals("para")){
            cmd += "for("+ctx.IDENT().getText()+" = "+(String)visitExp_aritmetica(ctx.exp_aritmetica(0))
             + "; "+ctx.IDENT().getText()+" <= "+(String)visitExp_aritmetica(ctx.exp_aritmetica(1))
             + "; "+ctx.IDENT().getText()+"++){";
            out.println(cmd);
            
            out.identationLevel++;
            this.escopos.empilhar(ctx.escopoNome);
            visitComandos(ctx.comandos());
            this.escopos.desempilhar();
            out.identationLevel--;
            cmd = "}";
        }
        
        out.println(cmd);
        
        return null; 
    }
    
    @Override public T visitSelecao(LAParser.SelecaoContext ctx) {        
        for(int i=0; i<ctx.constantes().size(); i++){
            visitConstantes(ctx.constantes(i));
            out.identationLevel++;
            visitComandos(ctx.comandos(i));
            out.println("break;");
            out.identationLevel--;
        }
        
        return null; 
    }
    
    @Override public T visitConstantes(LAParser.ConstantesContext ctx) {
        for(int i=0; i<ctx.numero_intervalo().size(); i++){
            String valorInicialString = "";
            if(!regraVazia(ctx.numero_intervalo(i).op_unario())){
                valorInicialString += "-";
            }
            valorInicialString += ctx.numero_intervalo(i).NUM_INT().getText();
            int valorInicial = Integer.parseInt(valorInicialString);
            
            int valorFinal=valorInicial;
            if(!regraVazia(ctx.numero_intervalo(i).intervalo_opcional())){
                String valorFinalString = "";
                if(!regraVazia(ctx.numero_intervalo(i).intervalo_opcional().op_unario())){
                    valorFinalString += "-";
                }
                valorFinalString += ctx.numero_intervalo(i).intervalo_opcional().NUM_INT().getText();
                valorFinal = Integer.parseInt(valorFinalString);
            }
            
            for(int j=valorInicial; j<=valorFinal; j++){
                out.println("case "+j+":");
            }
        }
        
        return null; 
    }
    
    @Override public T visitSenao_opcional(LAParser.Senao_opcionalContext ctx) { 
        String senao_opcional = "";
        
        out.identationLevel++;
        this.escopos.empilhar(ctx.escopoNome);
        visitComandos(ctx.comandos());
        this.escopos.desempilhar();
        out.identationLevel--;
        
        return null;
    }
    
    @Override public T visitChamada_atribuicao(LAParser.Chamada_atribuicaoContext ctx) { 
        String chamada_atribuicao = "";
        
        if(!regraVazia(ctx.argumentos_opcional())){ // É função
            chamada_atribuicao += "(" + visitExpressao(ctx.argumentos_opcional().expressao());
            for(LAParser.ExpressaoContext eCtx : ctx.argumentos_opcional().mais_expressao().expressao()){
                chamada_atribuicao += ", " + visitExpressao(eCtx);
            }
            chamada_atribuicao += ");";
        }else{ //Não é função
            chamada_atribuicao += (String)visitOutros_ident(ctx.outros_ident()) + 
             (String)visitDimensao(ctx.dimensao()) +
             "=" +
             (String)visitExpressao(ctx.expressao()) +
             ";";
        }
        
        return (T)chamada_atribuicao; 
    }
    
    @Override public T visitOutros_ident(LAParser.Outros_identContext ctx) { 
        if(regraVazia(ctx)){
            return (T)"";
        }
        String outros_ident = "";
        
        if(isPonteiro){
            outros_ident += "->";
        }else{
            outros_ident += ".";
        }
        
        outros_ident += visitIdentificador(ctx.identificador());
        
        return (T)outros_ident; 
    }
    
    @Override public T visitIdentificador(LAParser.IdentificadorContext ctx) { 
        String identificador = "";
        
        identificador += visitPonteiros_opcionais(ctx.ponteiros_opcionais()) +
         ctx.IDENT().getText() +
         (String)visitDimensao(ctx.dimensao()) +
         visitOutros_ident(ctx.outros_ident())
        ;
        
        return (T)identificador; 
    }
    
    @Override public T visitPonteiros_opcionais(LAParser.Ponteiros_opcionaisContext ctx) { 
        if(regraVazia(ctx)){
            return (T)"";
        }
        String ponteiros_opcionais = "";
        
        ponteiros_opcionais += "*" + visitPonteiros_opcionais(ctx.ponteiros_opcionais());
        
        return (T)ponteiros_opcionais; 
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
        
        expressao += (String)visitTermo_logico(ctx.termo_logico());
        for(int i=0; i<ctx.outros_termos_logicos().termo_logico().size(); i++){
            expressao += " || " + (String)visitTermo_logico(ctx.outros_termos_logicos().termo_logico(i));
        }
        
        return (T) expressao; 
    }
    
    @Override public T visitTermo_logico(LAParser.Termo_logicoContext ctx) { 
        String termo_logico = "";
        
        termo_logico += (String)visitFator_logico(ctx.fator_logico());
        for(int i=0; i<ctx.outros_fatores_logicos().fator_logico().size(); i++){
            termo_logico += " && " + (String)visitFator_logico(ctx.outros_fatores_logicos().fator_logico(i));
        }
        
        return (T)termo_logico; 
    }
    
    @Override public T visitFator_logico(LAParser.Fator_logicoContext ctx) { 
        String fator_logico = "";
        
        if(!regraVazia(ctx.op_nao())){
            fator_logico += "!";
        }
        
        fator_logico += visitParcela_logica(ctx.parcela_logica());
        
        return (T)fator_logico; 
    }
    
    @Override public T visitParcela_logica(LAParser.Parcela_logicaContext ctx) { 
        String parcela_logica = "";
        
        if(ctx.getStart().getText().equals("verdadeiro")){
            parcela_logica += "1";
        }else if(ctx.getStart().getText().equals("falso")){
            parcela_logica += "0";
        }else{
            parcela_logica += (String)visitExp_relacional(ctx.exp_relacional());
        }
        
        return (T)parcela_logica; 
    }
    
    @Override public T visitExp_relacional(LAParser.Exp_relacionalContext ctx) { 
        String exp_relacional = "";
        
        exp_relacional += (String)visitExp_aritmetica(ctx.exp_aritmetica());
        exp_relacional += (String)visitOp_opcional(ctx.op_opcional());
        
        return (T)exp_relacional;
    }
    
    @Override public T visitOp_opcional(LAParser.Op_opcionalContext ctx) { 
        if(regraVazia(ctx)){
            return (T)"";
        }
        String op_opcional = "";
        
        if(ctx.op_relacional().getText().equals("=")){
            op_opcional += "==";
        }else if(ctx.op_relacional().getText().equals("<>")){
            op_opcional += "!=";
        }else{
            op_opcional += ctx.op_relacional().getText();
        }
        
        op_opcional += (String)visitExp_aritmetica(ctx.exp_aritmetica());
        
        return (T)op_opcional; 
    }
    
    @Override public T visitExp_aritmetica(LAParser.Exp_aritmeticaContext ctx) { 
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
}