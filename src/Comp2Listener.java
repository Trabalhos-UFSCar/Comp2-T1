
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.antlr.v4.runtime.tree.TerminalNode;

public class Comp2Listener extends LABaseListener {
    
    SaidaParser out;
    Escopos escopos;
    Map<String, String> tipos;
    VerificadorDeTipos vdt;

    //Essa variavel é usada para contornar a dificuldade que é decidir o tipo de uma variavel
    //enquanto na regra mais_var. 
    String tipoAtual = "";
    
    Boolean dentroDoRegistro;
    List<EntradaTabelaDeSimbolos> simbolosRegistro;
    List<EntradaTabelaDeSimbolos> variaveisRegistroParaAdicionar;
    
    public Comp2Listener(SaidaParser out) {
        this.out = out;
        this.escopos = new Escopos();
        this.tipos = new HashMap<>();
        this.vdt = new VerificadorDeTipos(escopos, tipos);
        this.dentroDoRegistro = false;
        this.simbolosRegistro = new ArrayList<>();
        this.variaveisRegistroParaAdicionar = new ArrayList<>();

        // add os tipos em LA (key) e C (value)
        tipos.put("inteiro", "int");
        tipos.put("literal", "char");
        tipos.put("real", "float");
        tipos.put("logico", "int");  // contorno para bool em C
        tipos.put("nenhum", "void");
        tipos.put("registro", "struct");
        tipos.put("tipo_registro", "typedef struct");
    }
    
    @Override
    public void enterPrograma(LAParser.ProgramaContext ctx) {
        escopos.empilhar("global");
    }
    
    @Override
    public void exitPrograma(LAParser.ProgramaContext ctx) {
        escopos.desempilhar();
    }
    
    @Override
    public void enterCorpo(LAParser.CorpoContext ctx) {
        escopos.empilhar("corpo");
    }
    
    @Override
    public void exitCorpo(LAParser.CorpoContext ctx) {
        escopos.desempilhar();
    }
    
    @Override
    public void enterDeclaracao_global(LAParser.Declaracao_globalContext ctx) {

    }
    
    @Override
    public void exitDeclaracao_global(LAParser.Declaracao_globalContext ctx) {
        escopos.desempilhar();
    }
    
    @Override
    public void enterDeclaracao_local(LAParser.Declaracao_localContext ctx){
        if(ctx.tipo() != null){
            String s = ctx.IDENT().getText();
            tipos.put(s, "registro");
        }
    }
    
    @Override
    public void exitDeclaracao_local(LAParser.Declaracao_localContext ctx){
        if(!variaveisRegistroParaAdicionar.isEmpty()){
            String reg_var_nome;
            String reg_var_tipo;
            int reg_var_dim = 0;

            for (EntradaTabelaDeSimbolos e : variaveisRegistroParaAdicionar) {
                for (EntradaTabelaDeSimbolos f : simbolosRegistro) {
                    reg_var_nome = e.getNome() + "." + f.getNome();
                    reg_var_tipo = f.getTipo();
                    reg_var_dim = f.getDimensao();
                    escopos.adicionarSimbolo(reg_var_nome, reg_var_tipo, reg_var_dim);
                }
            }
            
            variaveisRegistroParaAdicionar = new ArrayList<>();
        }
    }
    
    @Override
    public void enterCmd(LAParser.CmdContext ctx) {
        //checagem se o retorno esta sendo usado em um escopo possivel
        if (ctx.getText().startsWith("retorne")) {
            if (escopos.existeRegistro("procedimento")) {
                out.println("Linha " + ctx.IDENT().getSymbol().getLine() + ": comando retorne nao permitido nesse escopo");
            }
        }
        
        if (!ctx.escopoNome.isEmpty()) {
            escopos.empilhar(ctx.escopoNome);
        }
    }
    
    @Override
    public void exitCmd(LAParser.CmdContext ctx) {
        if (!ctx.escopoNome.isEmpty()) {
            escopos.desempilhar();
        }
    }
    
    @Override
    public void enterIdentificador(LAParser.IdentificadorContext ctx) {
        
        if (ctx.parent.getText().startsWith(".")) {
            return;
        }
        
        String nome = ctx.IDENT().getText();
        if(ctx.outros_ident() != null && !ctx.outros_ident().getText().isEmpty())
            nome = nome + ctx.outros_ident().getText();
        
        if (!escopos.existeSimbolo(nome)) {
            out.println("Linha " + ctx.IDENT().getSymbol().getLine() + ": identificador " + nome + " nao declarado");
        }
    }
    
    @Override
    public void enterRegistro(LAParser.RegistroContext ctx) {
        dentroDoRegistro = true;
        
        String reg_var_nome = ctx.variavel().n.getText();
        String reg_var_tipo = ctx.variavel().t.getText();
        int reg_var_dim = 0;
        if (!ctx.variavel().d.getText().isEmpty()) {
            reg_var_dim = Integer.parseInt(ctx.variavel().d.getText());
        }
        simbolosRegistro.add(new EntradaTabelaDeSimbolos(reg_var_nome, reg_var_tipo, reg_var_dim));
        
        for (int i = 0; i < ctx.variavel().mais_var().IDENT().size(); i++) {
            reg_var_nome = ctx.variavel().mais_var().IDENT(i).getText();
            reg_var_dim = 0;
            if (!ctx.variavel().mais_var().dimensao(i).getText().isEmpty()) {
                reg_var_dim = Integer.parseInt(ctx.variavel().mais_var().dimensao(i).getText());
            }
            simbolosRegistro.add(new EntradaTabelaDeSimbolos(reg_var_nome, reg_var_tipo, reg_var_dim));
            
        }
        
        for(LAParser.VariavelContext v : ctx.mais_variaveis().variavel()){
            reg_var_nome = v.n.getText();
            reg_var_tipo = v.t.getText();
            reg_var_dim = 0;
            if (!v.d.getText().isEmpty()) {
                reg_var_dim = Integer.parseInt(v.d.getText());
            }
            simbolosRegistro.add(new EntradaTabelaDeSimbolos(reg_var_nome, reg_var_tipo, reg_var_dim));

            for (int i = 0; i < v.mais_var().IDENT().size(); i++) {
                reg_var_nome = v.mais_var().IDENT(i).getText();
                reg_var_dim = 0;
                if (!v.mais_var().dimensao(i).getText().isEmpty()) {
                    reg_var_dim = Integer.parseInt(v.mais_var().dimensao(i).getText());
                }
                simbolosRegistro.add(new EntradaTabelaDeSimbolos(reg_var_nome, reg_var_tipo, reg_var_dim));

            }
        }
        
        for (EntradaTabelaDeSimbolos e : variaveisRegistroParaAdicionar) {
            for (EntradaTabelaDeSimbolos f : simbolosRegistro) {
                reg_var_nome = e.getNome() + "." + f.getNome();
                reg_var_tipo = f.getTipo();
                reg_var_dim = f.getDimensao();
                escopos.adicionarSimbolo(reg_var_nome, reg_var_tipo, reg_var_dim);
            }
        }
        
        variaveisRegistroParaAdicionar = new ArrayList<>();
            
    }
    
    @Override
    public void exitRegistro(LAParser.RegistroContext ctx) {
        dentroDoRegistro = false;
    }
    
    @Override
    public void enterVariavel(LAParser.VariavelContext ctx) {
        EntradaTabelaDeSimbolos ets;
        String nome = ctx.n.getText();
        
        if (dentroDoRegistro) {
            return;
        }
        
        if (ctx.tipo().tipo_estendido() != null) {
            tipoAtual = ctx.tipo().tipo_estendido().tipo_basico_ident().getText();
            ets = new EntradaTabelaDeSimbolos(nome, tipoAtual);
        } else /*if(ctx.tipo().registro()!= null) */ {
            tipoAtual = "registro";
            ets = new EntradaTabelaDeSimbolos(nome, tipoAtual);
        }

        //Checagem para ter certeza de que essa variavel não foi definida anteriormente
        if (!escopos.existeSimbolo(nome) && !tipos.containsKey(nome)) {
            escopos.adicionarSimbolo(nome, tipoAtual);
            // se esta variavel for um registro, serão inseridos os seus elementos
            // em uma lista auxiliar
            if (tipoAtual.equals("registro") || 
                    (tipos.containsKey(tipoAtual) && tipos.get(tipoAtual).equals("registro"))) {
                variaveisRegistroParaAdicionar.add(ets);
            }
            
        } else {
            out.println("Linha " + ctx.IDENT().getSymbol().getLine() + ": identificador " + nome + " ja declarado anteriormente");
        }
        
        if (!tipos.containsKey(tipoAtual)) {
            out.println("Linha " + ctx.IDENT().getSymbol().getLine() + ": tipo " + tipoAtual + " nao declarado");
        }
        
    }
    
    @Override
    public void exitVariavel(LAParser.VariavelContext ctx) {
        tipoAtual = "";
    }
    
    @Override
    public void enterMais_var(LAParser.Mais_varContext ctx) {
        String nome;
        int dim;
        
        if (dentroDoRegistro) {
            return;
        }
        
        if (ctx.IDENT() != null) {
            for (int i = 0; i < ctx.IDENT().size(); i++) {
                nome = ctx.IDENT(i).getText();
                if (!ctx.dimensao(i).getText().isEmpty()) {
                    dim = Integer.parseInt(ctx.dimensao(i).getText());
                } else {
                    dim = 0;
                }
                
                EntradaTabelaDeSimbolos ets;
                if (!escopos.existeSimbolo(nome)){
                    ets = new EntradaTabelaDeSimbolos(nome, tipoAtual, dim);
                    escopos.adicionarSimbolo(ets);
                    if(tipoAtual.equals("registro") || 
                        (tipos.containsKey(tipoAtual) && tipos.get(tipoAtual).equals("registro")))
                        variaveisRegistroParaAdicionar.add(ets);
                } else {
                    out.println("Linha " + ctx.IDENT(i).getSymbol().getLine() + ": identificador " + nome + " ja declarado anteriormente");
                }
                
            }
        }
    }
    
    @Override
    public void enterParametro(LAParser.ParametroContext ctx) {
        String s = ctx.getText();
    }
    
    @Override
    public void enterParcela_unario(LAParser.Parcela_unarioContext ctx) {
        //checagem necessária pois nem todos os tipos de parcela unaria possuem um identificador
        for(int i = 0; i < ctx.getChildCount(); i++)
            System.out.println(i + " " + ctx.getChild(i).getText());
        
        if (ctx.IDENT() != null) {
            String nome = ctx.IDENT().getText();
            if(ctx.chamada_partes() != null && ctx.chamada_partes().outros_ident() != null)
                nome = nome + ctx.chamada_partes().outros_ident().getText();
            if (!escopos.existeSimbolo(nome)) {
                out.println("Linha " + ctx.IDENT().getSymbol().getLine() + ": identificador " + nome + " nao declarado");
            }
        }
    }
    
    @Override
    public void enterChamada_atribuicao(LAParser.Chamada_atribuicaoContext ctx) {
        String t1 = vdt.verificaTipo(ctx);
        String t2 = null;
        String nome = ((LAParser.CmdContext) ctx.parent).IDENT().getText();
        if (ctx.outros_ident() != null && !ctx.outros_ident().getText().isEmpty()) {
            nome = nome + ctx.outros_ident().getText();
        }

        if (!escopos.existeSimbolo(nome)) {
            Integer linha = ((LAParser.CmdContext) ctx.parent).IDENT().getSymbol().getLine();
            out.println("Linha " + linha + ": identificador " + nome + " nao declarado");
        } else {
            t2 = escopos.buscaSimbolo(nome).getTipo();
        }
        
        if (VerificadorDeTipos.regraTipos(t1, t2).equals("tipo_invalido")) {
            nome = ((LAParser.CmdContext) ctx.parent).IDENT().getText();
            if (ctx.outros_ident() != null && !ctx.outros_ident().getText().isEmpty()) {
                nome = nome + ctx.outros_ident().getText();
            }
            if (((LAParser.CmdContext) ctx.parent).getText().contains("^")) {
                nome = "^" + nome;
            }
            Integer linha = ((LAParser.CmdContext) ctx.parent).IDENT().getSymbol().getLine();
            out.println("Linha " + linha + ": atribuicao nao compativel para " + nome);
        }
    }
    
}
