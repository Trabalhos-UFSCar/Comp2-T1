
import java.util.HashMap;
import java.util.Map;

public class Comp2Listener extends LABaseListener {

    SaidaParser out;
    Escopos escopos;
    Map<String, String> tipo;

    public Comp2Listener(SaidaParser out) {
        this.out = out;
        this.escopos = new Escopos();
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
        if(!ctx.escopoNome.isEmpty()){
            escopos.empilhar(ctx.escopoNome);
        }
    }

    
    @Override
    public void exitCmd(LAParser.CmdContext ctx) {
        if(!ctx.escopoNome.isEmpty()){
            escopos.desempilhar();
        }
    }

    @Override
    public void enterIdentificador(LAParser.IdentificadorContext ctx) {
        System.out.println("identificador=" + ctx.IDENT().getText() + " Existe: " + escopos.existeSimbolo(ctx.IDENT().getText()));
        if (!escopos.existeSimbolo(ctx.IDENT().getText())) {
            out.println("Linha " + ctx.IDENT().getSymbol().getLine() + ": identificador " + ctx.IDENT() + " nao declarado");
        }
    }

    @Override
    public void enterVariavel(LAParser.VariavelContext ctx) {
        for (String n : ctx.nomes) {
            escopos.adicionarSimbolo(n, ctx.tp.getText());
        }

        if (tipo.get(ctx.tipo().getText()) == null) {
            out.println("Linha " + ctx.IDENT().getSymbol().getLine() + ": tipo " + ctx.tipo().getText() + " nao declarado");
        }

//        if (escopos.existeSimbolo(ctx.IDENT().getSymbol().getText())) {
//            out.println("Linha " + ctx.IDENT().getSymbol().getLine() + ": identificador " + ctx.IDENT() + " ja declarado anteriormente");
//        } else {
//            System.out.println("Variavel adicionada. Linha: " + ctx.IDENT().getSymbol().getLine() + " : " + ctx.IDENT().getSymbol().getText());
//        }
    }

    @Override
    public void enterMais_var(LAParser.Mais_varContext ctx) {
        //TODO: corrigir bug: o erro não está sendo adicionada corretamente quando ele ocorre
//        if (escopos.existeSimbolo(ctx.IDENT().getText())) {
//            out.println("Linha " + ctx.IDENT().getSymbol().getLine() + ": identificador " + ctx.IDENT() + " ja declarado anteriormente");
//        } else {
//            System.out.println("Variavel adicionada. Linha: " + ctx.IDENT().getSymbol().getLine() + " : " + ctx.IDENT());
//        }

    }

}
