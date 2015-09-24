
import java.util.HashMap;
import java.util.Map;

public class Comp2Listener extends LABaseListener {

    SaidaParser out;
    Escopos escopos;
    Map<String, String> tipo;
    VerificadorDeTipos vdt;

    //Essa variavel é usada para contornar a dificuldade que é decidir o tipo de uma variavel
    //enquanto na regra mais_var. 
    String tipoAtual = "";

    public Comp2Listener(SaidaParser out) {
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
    public void enterCmd(LAParser.CmdContext ctx) {
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
        if (!escopos.existeSimbolo(ctx.IDENT().getText())) {
            out.println("Linha " + ctx.IDENT().getSymbol().getLine() + ": identificador " + ctx.IDENT() + " nao declarado");
        }
    }

    @Override
    public void enterVariavel(LAParser.VariavelContext ctx) {
        String nome = ctx.nome.getText();
        tipoAtual = ctx.tipo().getText();
        if (!escopos.existeSimbolo(nome)) {
            escopos.adicionarSimbolo(nome, tipoAtual);
        } else {
            out.println("Linha " + ctx.IDENT().getSymbol().getLine() + ": identificador " + nome + " ja declarado anteriormente");
        }

        if (tipo.get(ctx.tipo().getText()) == null) {
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
        //essa checagem é necessário pois na ultima iteração do mais_var ele tera um IDENT nulo
        //pois não tem nada nela
        if (ctx.nome != null) {
            nome = ctx.nome.getText();

            if (!escopos.existeSimbolo(nome)) {
                escopos.adicionarSimbolo(nome, tipoAtual);
            } else {
                out.println("Linha " + ctx.nome.getLine() + ": identificador " + nome + " ja declarado anteriormente");
            }
        }
    }

    @Override
    public void enterParcela_unario(LAParser.Parcela_unarioContext ctx) {
        //checagem necessária pois nem todos os tipos de parcela unaria possuem um identificador
        if (ctx.IDENT() != null) {
            if (!escopos.existeSimbolo(ctx.IDENT().getText())) {
                out.println("Linha " + ctx.IDENT().getSymbol().getLine() + ": identificador " + ctx.IDENT().getText() + " nao declarado");
            }
        }
    }

    @Override
    public void enterChamada_atribuicao(LAParser.Chamada_atribuicaoContext ctx) {
       if(ctx.expressao()!=null){
           System.out.println(vdt.verificaTipo(ctx.expressao()));
       }
    }

}
