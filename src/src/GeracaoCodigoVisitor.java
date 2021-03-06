package src;

import antlr.*;
import utils.*;
import java.util.HashMap;
import java.util.Map;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * Esta classe é responsável pela geração de código.
 */
public class GeracaoCodigoVisitor<T> extends LABaseVisitor<T> {
    
    public SaidaParser out;
    Escopos escopos;
    Map<String, String> tipos;
    VerificadorDeTipos vdt;
    
    // Estratégia não muito correta, o ideal é na tabela de símbolos ter um 
    //boolean indicando se é ponteiro ou não.
    boolean isPonteiro = false;
    
    //Essa variavel é usada para contornar a dificuldade que é decidir o tipo de uma variavel
    //enquanto na regra mais_var. 
    String tipoAtual = "";
    
    public GeracaoCodigoVisitor(SaidaParser out){
        this.out = out;
        this.escopos = new Escopos();
        this.tipos = new HashMap<>();
        this.vdt = new VerificadorDeTipos(escopos, tipos);

        // add os tipos em LA (key) e C (value)
        tipos.put("inteiro", "int");
        tipos.put("literal", "char");    // TODO: verificar como fazer literal em C
        tipos.put("real", "float");
        tipos.put("logico", "int");  // contorno para bool em C
        tipos.put("registro", ""); // Nao eh usado realmente, soh usado na tabela de simbolo.
    }
    
    /*
        Métodos auxiliares para verificarem se um regra de contexto
        ou se um nó é/está vazio. Em alguns casos, ctx != null
        mas tem comprimento vazio.
    
    */
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
    @Override 
    public T visitPrograma(LAParser.ProgramaContext ctx) { 
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
    @Override 
    public T visitDeclaracoes(LAParser.DeclaracoesContext ctx) { 
        if(regraVazia(ctx)){
            return null;
        }
        
        for(LAParser.Decl_local_globalContext dlg : ctx.decl_local_global()){
            visitDecl_local_global(dlg);
        }
        
        return null;
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override 
    public T visitDecl_local_global(LAParser.Decl_local_globalContext ctx) { 
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
    @Override 
    public T visitDeclaracao_local(LAParser.Declaracao_localContext ctx) {
        if(!regraVazia(ctx.variavel())){
            visitVariavel(ctx.variavel());
        }else if(ctx.getStart().getText().equals("constante")){
            // Trata declaracao constante como variavel normal.
            tipoAtual = ctx.tipo_basico().getText();
            String variavel = variavel(ctx.IDENT().getText(), null);
            out.println(variavel);
            out.println(ctx.IDENT().getText()+" = "+ctx.valor_constante().getText()+";");
            tipoAtual = "";
        }else if(ctx.getStart().getText().equals("tipo")){
            String ident = ctx.IDENT().getText();
            tipos.put(ident, ident);
            out.println("typedef ");
            visitTipo(ctx.tipo());
            out.println(ident+";");
        }
        return null;
    }
    
    @Override 
    public T visitTipo(LAParser.TipoContext ctx) {
        String tipo = "";
        
        if(!regraVazia(ctx.registro())){
            visitRegistro(ctx.registro());
            return (T)"registro";
        }else{
            tipo += ctx.tipo_estendido().tipo_basico_ident().getText();
        }
        
        return (T)tipo;
    }
    
    @Override 
    public T visitRegistro(LAParser.RegistroContext ctx) { 
        String registro = "";
        
        registro += "struct{";
        out.println(registro);
        out.identationLevel++;
        if(!regraVazia(ctx.variavel())){
            visitVariavel(ctx.variavel());
        }
        if(!regraVazia(ctx.mais_variaveis())){
            visitMais_variaveis(ctx.mais_variaveis());
        }
        out.identationLevel--;
        registro = "}";
        
        out.println(registro);
        
        return null; 
    }
    
    @Override 
    public T visitVariavel(LAParser.VariavelContext ctx) {
        if(ctx.tipo().getStart().getText().equals("^")){
            isPonteiro = true;
        }
        tipoAtual = (String)visitTipo(ctx.tipo());
        
        String variavel = "";
        variavel = variavel(ctx.n.getText(), ctx.dimensao());
        for(int i=0; i<ctx.mais_var().IDENT().size(); i++){
            out.println(variavel);
            variavel = variavel(ctx.mais_var().IDENT(i).getText(), ctx.mais_var().dimensao(i));
        }
        
        tipoAtual = "";
        isPonteiro = false;
        
        out.println(variavel);
        
        return null; 
    }
    
    public String variavel(String nome, LAParser.DimensaoContext dimCtx){
        escopos.adicionarSimbolo(nome, tipoAtual);
        String ponteiro_opcional = "";
        if(isPonteiro){
            ponteiro_opcional = "*";
        }
        
        String variavel = "";
        if(tipoAtual.equals("literal")){
            variavel =
             tipos.get(tipoAtual) +
             ponteiro_opcional +
             " " +
             nome +
             "[80]" +
            ";"
           ;
        }else{
            variavel = 
             tipos.get(tipoAtual) +
             ponteiro_opcional +
             " " +
             nome + 
             visitDimensao(dimCtx) +
             ";"
            ;
        }
        
        return variavel;
    }
    
    @Override 
    public T visitDimensao(LAParser.DimensaoContext ctx) {
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
    @Override 
    public T visitDeclaracao_global(LAParser.Declaracao_globalContext ctx) { 
        if(ctx.getStart().getText().equals("procedimento")){
            escopos.empilhar(ctx.IDENT().getText());
            out.println("void " + ctx.IDENT().getText()+"("+visitParametros_opcional(ctx.parametros_opcional())+"){");
            out.identationLevel++;
            visitDeclaracoes_locais(ctx.declaracoes_locais());
            visitComandos(ctx.comandos());
            escopos.desempilhar();
            out.identationLevel--;
            out.println("}");
        }else{ //Função
            String tipoRetorno = tipos.get(ctx.tipo_estendido().getText());
            escopos.adicionarSimbolo(ctx.IDENT().getText(), ctx.tipo_estendido().getText());
            escopos.empilhar(ctx.IDENT().getText());
            out.println(tipoRetorno + " "
             + ctx.IDENT().getText() + "(" + visitParametros_opcional(ctx.parametros_opcional())
             + "){");
            out.identationLevel++;
            visitDeclaracoes_locais(ctx.declaracoes_locais());
            visitComandos(ctx.comandos());
            escopos.desempilhar();
            out.identationLevel--;
            out.println("}");
        }
        
        return null; 
    }
    
    @Override 
    public T visitParametros_opcional(LAParser.Parametros_opcionalContext ctx) { 
        if(regraVazia(ctx.parametro())){
            return (T)"";
        }
        String parametros_opcional = "";
        
        escopos.adicionarSimbolo((String)visitIdentificador(ctx.parametro().identificador()), 
         ctx.parametro().tipo_estendido().getText());
        
        String tipoParam = "";
        if(ctx.parametro().tipo_estendido().getText().equals("literal")){
            tipoParam = "char* ";
        }else{
            tipoParam = tipos.get(ctx.parametro().tipo_estendido().getText()) + " ";
        }
        
        parametros_opcional += tipoParam;
        
        if(!regraVazia(ctx.parametro().var_opcional())){
            parametros_opcional += (String)visitVar_opcional(ctx.parametro().var_opcional());
        }
        
        parametros_opcional += (String)visitIdentificador(ctx.parametro().identificador());
        
        if(!regraVazia(ctx.parametro().mais_ident())){
            parametros_opcional += (String)visitMais_ident(ctx.parametro().mais_ident());
        }
        
        if(!regraVazia(ctx.parametro().mais_parametros())){
            parametros_opcional += ", " + visitParametro(ctx.parametro().mais_parametros().parametro());
        }
        
        return (T)parametros_opcional; 
    }
    
    @Override 
    public T visitCorpo(LAParser.CorpoContext ctx) {
        escopos.empilhar("corpo");
        
        visitDeclaracoes_locais(ctx.declaracoes_locais());
        
        visitComandos(ctx.comandos());
        
        escopos.desempilhar();
        
        return null; 
    }
    
    @Override 
    public T visitDeclaracoes_locais(LAParser.Declaracoes_locaisContext ctx){
        if(regraVazia(ctx)){
            return null;
        }
        
        visitDeclaracao_local(ctx.declaracao_local());
        
        visitDeclaracoes_locais(ctx.declaracoes_locais());
        
        return null;
    }
    
    @Override 
    public T visitComandos(LAParser.ComandosContext ctx) { 
        if(regraVazia(ctx)){
            return null;
        }
        
        visitCmd(ctx.cmd());
        
        visitComandos(ctx.comandos());

        return null; 
    }
    
    @Override 
    public T visitCmd(LAParser.CmdContext ctx) {
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
            cmd += (String)visitChamada_atribuicao(ctx.chamada_atribuicao());
            isPonteiro = false;
        }else if(ctx.getStart().getText().equals("se")){
            cmd += "if(" + visitExpressao(ctx.expressao()) +"){";
            
            entraComandosEscopo(cmd, ctx);
            
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
            
            entraComandosEscopo(cmd, ctx);
            
            cmd = "}";
        }else if(ctx.getStart().getText().equals("enquanto")){
            cmd += "while ("+(String)visitExpressao(ctx.expressao())+"){";
            
            entraComandosEscopo(cmd, ctx);
            
            cmd = "}";
        }else if(ctx.getStart().getText().equals("faca")){
            cmd += "do{";
            
            entraComandosEscopo(cmd, ctx);
            
            cmd = "}while ("+visitExpressao(ctx.expressao())+");";
        }else if(ctx.getStart().getText().equals("retorne")){
            cmd += "return " + visitExpressao(ctx.expressao()) + ";";
        }
        
        out.println(cmd);
        
        return null; 
    }
    
    public void entraComandosEscopo(String cmd, LAParser.CmdContext ctx){
        out.println(cmd);
            
        out.identationLevel++;
        this.escopos.empilhar(ctx.escopoNome);
        visitComandos(ctx.comandos());
        this.escopos.desempilhar();
        out.identationLevel--;
    }
    
    @Override 
    public T visitSelecao(LAParser.SelecaoContext ctx) {        
        for(int i=0; i<ctx.constantes().size(); i++){
            visitConstantes(ctx.constantes(i));
            out.identationLevel++;
            visitComandos(ctx.comandos(i));
            out.println("break;");
            out.identationLevel--;
        }
        
        return null; 
    }
    
    @Override 
    public T visitConstantes(LAParser.ConstantesContext ctx) {
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
    
    @Override 
    public T visitSenao_opcional(LAParser.Senao_opcionalContext ctx) { 
        String senao_opcional = "";
        
        out.identationLevel++;
        this.escopos.empilhar(ctx.escopoNome);
        visitComandos(ctx.comandos());
        this.escopos.desempilhar();
        out.identationLevel--;
        
        return null;
    }
    
    @Override 
    public T visitChamada_atribuicao(LAParser.Chamada_atribuicaoContext ctx) { 
        String chamada_atribuicao = "";
        String parentIdent = ((LAParser.CmdContext)ctx.getParent()).IDENT().getText();
        
        if(!regraVazia(ctx.argumentos_opcional())){ // É função
            chamada_atribuicao += parentIdent + "(" + visitExpressao(ctx.argumentos_opcional().expressao());
            for(LAParser.ExpressaoContext eCtx : ctx.argumentos_opcional().mais_expressao().expressao()){
                chamada_atribuicao += ", " + visitExpressao(eCtx);
            }
            chamada_atribuicao += ");";
        }else{ //Não é função
            if(vdt.verificaTipo(ctx.expressao()).equals("literal")){
                chamada_atribuicao += "strcpy(" + parentIdent + visitOutros_ident(ctx.outros_ident())
                 + ", " + visitExpressao(ctx.expressao()) + ");";
            }
            else{
                chamada_atribuicao += parentIdent + (String)visitOutros_ident(ctx.outros_ident()) + 
                 (String)visitDimensao(ctx.dimensao()) +
                 "=" +
                 (String)visitExpressao(ctx.expressao()) +
                 ";";
            }
        }
        
        return (T)chamada_atribuicao; 
    }
    
    @Override 
    public T visitOutros_ident(LAParser.Outros_identContext ctx) { 
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
    
    @Override 
    public T visitIdentificador(LAParser.IdentificadorContext ctx) { 
        String identificador = "";
        
        identificador += visitPonteiros_opcionais(ctx.ponteiros_opcionais()) +
         ctx.IDENT().getText() +
         (String)visitDimensao(ctx.dimensao()) +
         visitOutros_ident(ctx.outros_ident())
        ;
        
        return (T)identificador; 
    }
    
    @Override 
    public T visitPonteiros_opcionais(LAParser.Ponteiros_opcionaisContext ctx) { 
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
    
    @Override 
    public T visitExpressao(LAParser.ExpressaoContext ctx) { 
        String expressao = "";
        
        expressao += (String)visitTermo_logico(ctx.termo_logico());
        for(int i=0; i<ctx.outros_termos_logicos().termo_logico().size(); i++){
            expressao += " || " + (String)visitTermo_logico(ctx.outros_termos_logicos().termo_logico(i));
        }
        
        return (T) expressao; 
    }
    
    @Override 
    public T visitTermo_logico(LAParser.Termo_logicoContext ctx) { 
        String termo_logico = "";
        
        termo_logico += (String)visitFator_logico(ctx.fator_logico());
        for(int i=0; i<ctx.outros_fatores_logicos().fator_logico().size(); i++){
            termo_logico += " && " + (String)visitFator_logico(ctx.outros_fatores_logicos().fator_logico(i));
        }
        
        return (T)termo_logico; 
    }
    
    @Override 
    public T visitFator_logico(LAParser.Fator_logicoContext ctx) { 
        String fator_logico = "";
        
        if(!regraVazia(ctx.op_nao())){
            fator_logico += "!";
        }
        
        fator_logico += visitParcela_logica(ctx.parcela_logica());
        
        return (T)fator_logico; 
    }
    
    @Override 
    public T visitParcela_logica(LAParser.Parcela_logicaContext ctx) { 
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
    
    @Override 
    public T visitExp_relacional(LAParser.Exp_relacionalContext ctx) { 
        String exp_relacional = "";
        
        exp_relacional += (String)visitExp_aritmetica(ctx.exp_aritmetica());
        exp_relacional += (String)visitOp_opcional(ctx.op_opcional());
        
        return (T)exp_relacional;
    }
    
    @Override 
    public T visitOp_opcional(LAParser.Op_opcionalContext ctx) { 
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
    
    @Override 
    public T visitExp_aritmetica(LAParser.Exp_aritmeticaContext ctx) { 
        if(!regraVazia(ctx.outros_termos())){
            return (T) ((String) visitTermo(ctx.termo()) + (String) visitOutros_termos(ctx.outros_termos()));
        }else{
            return (T) (String) visitTermo(ctx.termo());
        }
    }
    
    @Override 
    public T visitOutros_termos(LAParser.Outros_termosContext ctx) { 
        String outros_termos = "";
        
        for(int i=0; i<ctx.op_adicao().size(); i++){
            outros_termos += ctx.op_adicao(i).getText() + (String) visitTermo(ctx.termo(i));
        }
        return (T)outros_termos; 
    }
    
    @Override 
    public T visitTermo(LAParser.TermoContext ctx) { 
        if(!regraVazia(ctx.outros_fatores())){
            return (T) ((String) visitFator(ctx.fator()) + (String) visitOutros_fatores(ctx.outros_fatores()));
        }else{
            return (T) (String) visitFator(ctx.fator());
        }
    }
    
    @Override 
    public T visitOutros_fatores(LAParser.Outros_fatoresContext ctx) { 
        String outros_fatores = "";
        
        for(int i=0; i<ctx.op_multiplicacao().size(); i++){
            outros_fatores += ctx.op_multiplicacao(i).getText() + visitFator(ctx.fator(i));
        }
        return (T)outros_fatores;
    }
    
    @Override 
    public T visitFator(LAParser.FatorContext ctx) { 
        if(!regraVazia(ctx.outras_parcelas())){
            return (T) ((String) visitParcela(ctx.parcela()) + (String) visitOutras_parcelas(ctx.outras_parcelas()));
        }else{
            return (T) (String) visitParcela(ctx.parcela());
        }
    }
    
    @Override 
    public T visitParcela(LAParser.ParcelaContext ctx) { 
        String parcela = "";
        
        if(!regraVazia(ctx.parcela_unario())){
            parcela += (String) visitOp_unario(ctx.op_unario()) + (String) visitParcela_unario(ctx.parcela_unario());
        }else{ //Regra é parcela_nao_unario
            parcela += (String) visitParcela_nao_unario(ctx.parcela_nao_unario());
        }
        
        return (T) parcela; 
    }
    
    @Override 
    public T visitOp_unario(LAParser.Op_unarioContext ctx) { return (T) ctx.getText(); }
    
    @Override 
    public T visitParcela_unario(LAParser.Parcela_unarioContext ctx) { 
        String parcela_unario = "";
        
//        if(!regraVazia(ctx.chamada_partes())){
//            parcela_unario += ctx.IDENT().getText() + (String)visitChamada_partes(ctx.chamada_partes());
//            return (T)parcela_unario;
//        }
        
        if(ctx.getStart().getText().equals("^")){
            isPonteiro = true;
            parcela_unario += "*" + ctx.IDENT().getText()
             + visitOutros_ident(ctx.outros_ident()) + visitDimensao(ctx.dimensao());
            isPonteiro = false;
        }else if(!regraVazia(ctx.IDENT())){
            parcela_unario += ctx.IDENT().getText() + (String)visitChamada_partes(ctx.chamada_partes());
        }else if(!regraVazia(ctx.NUM_INT())){
            parcela_unario += ctx.NUM_INT().getText();
        }else if(!regraVazia(ctx.NUM_REAL())){
            parcela_unario += ctx.NUM_REAL().getText();
        }else if(!regraVazia(ctx.expressao())){
            parcela_unario += "("+ (String)visitExpressao(ctx.expressao())+ ")";
        }
        
        return (T) parcela_unario; 
    }
    
    @Override 
    public T visitChamada_partes(LAParser.Chamada_partesContext ctx) { 
        if(regraVazia(ctx)){
            return (T)"";
        }
        String chamada_partes = "";
        
        if(!regraVazia(ctx.expressao())){
            chamada_partes += "(" + visitExpressao(ctx.expressao());
            if(!regraVazia(ctx.mais_expressao())){
                chamada_partes += visitMais_expressao(ctx.mais_expressao());
            }
            chamada_partes += ")";
        }else if(!regraVazia(ctx.outros_ident()) || !regraVazia(ctx.dimensao())){
            chamada_partes += (String)visitOutros_ident(ctx.outros_ident()) + (String)visitDimensao(ctx.dimensao());
        }
        
        return (T)chamada_partes; 
    }
    
    @Override 
    public T visitParcela_nao_unario(LAParser.Parcela_nao_unarioContext ctx) { 
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