package src;

/**
 * Esta classe é responsável pela análise semântica da linguagem LA.
 */

import antlr.*;
import utils.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SemanticoListener extends LABaseListener {
    
    // StringBuffer para saida
    SaidaParser out;
    // Pilha de escopos
    Escopos escopos;
    // Tipos disponíveis
    Map<String, String> tipos;
    VerificadorDeTipos vdt;

    // Essa variavel é usada para salvar
    // o último tipo de variável instanciado
    // para registros ou lista de variáveis de
    // mesmo tipo tais como em mais_var();
    String tipoAtual = "";
    
    /* Variáveis utilizadas para registros */
    Boolean dentroDoRegistro;
    List<EntradaTabelaDeSimbolos> simbolosDoRegistro;
    List<EntradaTabelaDeSimbolos> variaveisRegistroParaAdicionar;
    
    public SemanticoListener(SaidaParser out) {
        this.out = out;
        this.escopos = new Escopos();
        this.tipos = new HashMap<>();
        this.vdt = new VerificadorDeTipos(escopos, tipos);
        this.dentroDoRegistro = false;
        this.simbolosDoRegistro = new ArrayList<>();
        this.variaveisRegistroParaAdicionar = new ArrayList<>();

        // add os tipos em LA (key) e C (value)
        tipos.put("inteiro", "int");
        tipos.put("literal", "char");
        tipos.put("real", "float");
        tipos.put("logico", "int");  // contorno para bool em C
        tipos.put("registro","struct");
    }
    
    /*
        O método empilha o escopo global do programa.
    */
    @Override
    public void enterPrograma(LAParser.ProgramaContext ctx) {
        escopos.empilhar("global");
    }
    
    /*
        O método desempilha o escopo global do programa.
    */
    @Override
    public void exitPrograma(LAParser.ProgramaContext ctx) {
        escopos.desempilhar();
    }
    
    /*
        O método empilha o escopo do corpo do programa.
    */
    @Override
    public void enterCorpo(LAParser.CorpoContext ctx) {
        escopos.empilhar("corpo");
    }
    
    /*
        O método desempilha o escopo do corpo do programa.
    */
    @Override
    public void exitCorpo(LAParser.CorpoContext ctx) {
        escopos.desempilhar();
    }
    
    /*
        O método tem duas funções principais:
        Adicionar novos tipos a lista de tipos quando são definidos
        Adicionar constante a tabela de símbolos
    */
    @Override
    public void enterDeclaracao_local(LAParser.Declaracao_localContext ctx){
        if(ctx.tipo() != null){
            String s = ctx.IDENT().getText();
            tipos.put(s, "registro");
        } else if(ctx.valor_constante() != null){
            String s = ctx.IDENT().getText();
            String t = ctx.tipo_basico().getText();
            escopos.adicionarSimbolo(s, t);
        }
    }
    
    /*
        O método tem como objetivo adicionar ao escopo o
        elementos dos registros. Como o registro é definido após
        a lista de variáveis daquele registro, elas são inseridas
        na lista variaveisRegistroParaAdicionar. Quando a declarações
        locais terminam, os registros e seus elementos já foram lidos 
        pela árvore e adicionados a lista simbolosDoRegistro.
    
        Este método vai adicionar no escopo as combinações de
        elementos definidos como registro e seus sub-elementos.
        Exemplo, x é um registro e y e z são seus elementos.
        Este método adiciona x.y e x.z no escopo.
    */
    @Override
    public void exitDeclaracao_local(LAParser.Declaracao_localContext ctx){
        if(!variaveisRegistroParaAdicionar.isEmpty()){
            String reg_var_nome;
            String reg_var_tipo;
            int reg_var_dim = 0;

            for (EntradaTabelaDeSimbolos e : variaveisRegistroParaAdicionar) {
                for (EntradaTabelaDeSimbolos f : simbolosDoRegistro) {
                    //combinacao do nome x.y
                    reg_var_nome = e.getNome() + "." + f.getNome();
                    //tipo de y
                    reg_var_tipo = f.getTipo();
                    //dim de y
                    reg_var_dim = f.getDimensao();
                    escopos.adicionarSimbolo(reg_var_nome, reg_var_tipo, reg_var_dim);
                }
            }
            //Zera a lista
            variaveisRegistroParaAdicionar = new ArrayList<>();
        }
    }
    /*
        No caso de ser um comando que cria um escopo especidico, ele terá um 
        nome de escopodefinido na gramatica diretamente, aqui é chegado se ele 
        tem esse nome e cria um escopo com ele
    */
    @Override
    public void enterCmd(LAParser.CmdContext ctx) {
        if (!ctx.escopoNome.isEmpty()) {
            escopos.empilhar(ctx.escopoNome);
        }
    }
    
    /*
        Aqui o escopo de comando é destruido 
    */
    @Override
    public void exitCmd(LAParser.CmdContext ctx) {
        if (!ctx.escopoNome.isEmpty()) {
            escopos.desempilhar();
        }
    }
    
    /*
        O metodo tem como objetivo checar se todos os elementos
        foram previamente inseridos na tabela de simbolos.
        Por exemplo, os elementos sao inseridos na tabela por meio 
        da regra variavel(), mais_var() ou mais_variavel(). Se um 
        identificador nao foi inserido corretamente, este metodo 
        vai adicionar a mensagem de erro.
    
        Existe apenas um caso especial. Se o identificador esta dentro
        de um mais_indent(), nao e feita a verificacao, pois se y e um
        registro, y esta na tabela, y.x esta na tabela mas x nao esta.
    */
    
    @Override
    public void enterIdentificador(LAParser.IdentificadorContext ctx) {
        
        //Se o pai comeca com '.', entao eh mais_ident() e nao verifica
        if (ctx.parent.getText().startsWith(".")) {
            return;
        }
        
        //Encontra o nome completo do identificador.
        String nome = ctx.IDENT().getText();
        if(ctx.outros_ident() != null && !ctx.outros_ident().getText().isEmpty())
            nome = nome + ctx.outros_ident().getText();
        
        if (!escopos.existeSimbolo(nome)) {
            out.println("Linha " + ctx.IDENT().getSymbol().getLine() + ": identificador " + nome + " nao declarado");
        }
    }
    
    /*
        O metodo adiciona os elementos definidos dentro de um registro
        dentro da lista simbolosDoRegistro.
    
        Como os simbolos do registro nao sao adicionados ao escopo
        que nem outras variaveis, o booleano dentroDoRegistro bloqueia que
        variaveis de registro sejam adicionadas no escopo.
    
        Note que variavel() e mais_var() sao do mesmo tipo. E mais_variaveis()
        e uma lista dos anteriores.
    */
    
    @Override
    public void enterRegistro(LAParser.RegistroContext ctx) {
        dentroDoRegistro = true;
        
        String reg_var_nome = ctx.variavel().n.getText();
        String reg_var_tipo = ctx.variavel().t.getText();
        int reg_var_dim = 0;
        if (!ctx.variavel().d.getText().isEmpty()) {
            reg_var_dim = Integer.parseInt(ctx.variavel().d.getText());
        }
        simbolosDoRegistro.add(new EntradaTabelaDeSimbolos(reg_var_nome, reg_var_tipo, reg_var_dim));
        
        for (int i = 0; i < ctx.variavel().mais_var().IDENT().size(); i++) {
            reg_var_nome = ctx.variavel().mais_var().IDENT(i).getText();
            reg_var_dim = 0;
            if (!ctx.variavel().mais_var().dimensao(i).getText().isEmpty()) {
                reg_var_dim = Integer.parseInt(ctx.variavel().mais_var().dimensao(i).getText());
            }
            simbolosDoRegistro.add(new EntradaTabelaDeSimbolos(reg_var_nome, reg_var_tipo, reg_var_dim));
            
        }
        
        for(LAParser.VariavelContext v : ctx.mais_variaveis().variavel()){
            reg_var_nome = v.n.getText();
            reg_var_tipo = v.t.getText();
            reg_var_dim = 0;
            if (!v.d.getText().isEmpty()) {
                reg_var_dim = Integer.parseInt(v.d.getText());
            }
            simbolosDoRegistro.add(new EntradaTabelaDeSimbolos(reg_var_nome, reg_var_tipo, reg_var_dim));

            for (int i = 0; i < v.mais_var().IDENT().size(); i++) {
                reg_var_nome = v.mais_var().IDENT(i).getText();
                reg_var_dim = 0;
                if (!v.mais_var().dimensao(i).getText().isEmpty()) {
                    reg_var_dim = Integer.parseInt(v.mais_var().dimensao(i).getText());
                }
                simbolosDoRegistro.add(new EntradaTabelaDeSimbolos(reg_var_nome, reg_var_tipo, reg_var_dim));

            }
        }
            
    }
    
    /*
        O metodo retira o bloqueio de variaveis no escopo tradicional.
    */
    
    @Override
    public void exitRegistro(LAParser.RegistroContext ctx) {
        dentroDoRegistro = false;
    }
    
    /*
        O metodo adiciona uma variavel a tabela de simbolos sendo usada
        no momento, verifica se duas ou mais variaveis tem o mesmo nome,
        se o tipo de uma variavel nao foi definido.
    
        O tipo de uma variavel vem de tipo_estendido..tipo_basico_ident()
        ou é um registro caso contrário.
    |   O registro entra na tabela de simbolos com o tipo registro.
        Os elementos do registro são inseridos depois das declaracoes locais.
    */
    
    @Override
    public void enterVariavel(LAParser.VariavelContext ctx) {
        EntradaTabelaDeSimbolos ets;
        String nome = ctx.n.getText();
        // bloqueia insercao
        if (dentroDoRegistro) {
            return;
        }
        // encontra tipo da variavel
        if (ctx.tipo().tipo_estendido() != null) {
            tipoAtual = ctx.tipo().tipo_estendido().tipo_basico_ident().getText();
            ets = new EntradaTabelaDeSimbolos(nome, tipoAtual);
        } else /*if(ctx.tipo().registro()!= null) */ {
            tipoAtual = "registro";
            ets = new EntradaTabelaDeSimbolos(nome, tipoAtual);
        }

        //Checagem para ter certeza de que essa variavel não foi definida anteriormente
        if (!escopos.existeSimbolo(nome)) {
            escopos.adicionarSimbolo(nome, tipoAtual);
            // se esta variavel for um registro, serão inseridos os seus elementos
            // em uma lista auxiliar depois que registro passou na arvore
            if (tipoAtual.equals("registro") || 
                    (tipos.containsKey(tipoAtual) && tipos.get(tipoAtual).equals("registro"))) {
                variaveisRegistroParaAdicionar.add(ets);
            }
        // simbolo ja existe
        } else {
            out.println("Linha " + ctx.IDENT().getSymbol().getLine() + ": identificador " + nome + " ja declarado anteriormente");
        }
        // tipo nao existe
        if (!tipos.containsKey(tipoAtual)) {
            out.println("Linha " + ctx.IDENT().getSymbol().getLine() + ": tipo " + tipoAtual + " nao declarado");
        }
        
    }
    /*
        Se outra variavel for instanciada, elas podem ser de tipos diferentes
        O método reseta tipoAtual.
    */
    @Override
    public void exitVariavel(LAParser.VariavelContext ctx) {
        tipoAtual = "";
    }
    
    /*
        O método funciona similar a variavel(), exceto que os
        elementos variaveis() está em uma lista.
    
        Não verifica se um tipo não existe, isso é papel de variavel()
   
    */
    @Override
    public void enterMais_var(LAParser.Mais_varContext ctx) {
        String nome;
        int dim;
        // bloqueia insercao
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
    
    /*
        O método faz checagem do identificador da parcela unaria, 
        similar a identificador().
    */
    @Override
    public void enterParcela_unario(LAParser.Parcela_unarioContext ctx) {
        //checagem necessária pois nem todos os tipos de parcela unaria possuem um identificador        
        if (ctx.IDENT() != null) {
            String nome = ctx.IDENT().getText();
            if(ctx.chamada_partes() != null && ctx.chamada_partes().outros_ident() != null)
                nome = nome + ctx.chamada_partes().outros_ident().getText();
            if (!escopos.existeSimbolo(nome)) {
                out.println("Linha " + ctx.IDENT().getSymbol().getLine() + ": identificador " + nome + " nao declarado");
            }
        }
    }
    
    /*
        O método verifica se uma atribuição é válida.
        Ele encontra o tipo da expressao() e do ident() e compara-os
        por meio da regraDeTipos()
        Também faz checagem se a variavel a ser atribuida ja foi declarada.
    */
    
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
