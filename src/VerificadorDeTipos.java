
public class VerificadorDeTipos {

    Escopos escopos;

    public VerificadorDeTipos(Escopos escopos) {
        this.escopos = escopos;
    }

    public String verificaTipo(LAParser.ExpressaoContext ctx) {
        String text = ctx.getText();
        String tipoExp = verificaTipo(ctx.termo_logico());
        if (ctx.outros_termos_logicos() == null || ctx.outros_termos_logicos().getText().isEmpty()) {
            return tipoExp;
        } else {
            for (LAParser.Termo_logicoContext termo : ctx.outros_termos_logicos().termo_logico()) {
                String tipoOutroTermo = verificaTipo(termo);
                tipoExp = regraTipos(tipoExp, tipoOutroTermo);
            }
        }
        return tipoExp;
    }

    public String verificaTipo(LAParser.Termo_logicoContext ctx) {
        String text = ctx.getText();
        String tipoExp = verificaTipo(ctx.fator_logico());
        if (ctx.outros_fatores_logicos() == null || ctx.outros_fatores_logicos().getText().isEmpty()) {
            return tipoExp;
        } else {
            for (LAParser.Fator_logicoContext termo : ctx.outros_fatores_logicos().fator_logico()) {
                String tipoOutroTermo = verificaTipo(termo);
                tipoExp = regraTipos(tipoExp, tipoOutroTermo);
            }
        }
        return tipoExp;
    }

    public String verificaTipo(LAParser.Fator_logicoContext ctx) {
        String text = ctx.getText();
        String tipoExp = verificaTipo(ctx.parcela_logica());

        // Se o operador lógico 'nao' está definido
        // Então o resultado é lógico apenas se a expressão
        // após o operador é também lógica
        if (ctx.op_nao() != null && !ctx.op_nao().getText().isEmpty()) {
            tipoExp = regraTipos(tipoExp, "logico");
        }

        return tipoExp;
    }

    public String verificaTipo(LAParser.Parcela_logicaContext ctx) {
        String text = ctx.getText();
        String tipoExp;

        // Verifica se é um nó folha (verdadeiro ou falso)
        // Caso contrário, continua descendo a árvore
        if (ctx.getChildCount() == 0 || ctx.exp_relacional()==null) {
            tipoExp = "logico";
        } else {
            tipoExp = verificaTipo(ctx.exp_relacional());
        }

        return tipoExp;
    }

    public String verificaTipo(LAParser.Exp_relacionalContext ctx) {
        String text = ctx.getText();
        String tipoExp;

        // Se existe algum operador relacional, entao resultado é logico
        // se os dois lados da expressao podem ser comparados
        if (ctx.op_opcional() != null && !ctx.op_opcional().getText().isEmpty()) {
            String tipo1 = verificaTipo(ctx.exp_aritmetica());
            String tipo2 = verificaTipo(ctx.op_opcional().exp_aritmetica());
            tipoExp = regraTipos(tipo1, tipo2);
            if (!tipoExp.equals("tipo_invalido")) {
                tipoExp = "logico";
            }
        } else {
            tipoExp = verificaTipo(ctx.exp_aritmetica());
        }

        return tipoExp;

    }

    public String verificaTipo(LAParser.Exp_aritmeticaContext ctx) {
        String text = ctx.getText();
        String tipoExp = verificaTipo(ctx.termo());
        if (ctx.outros_termos() == null || ctx.outros_termos().getText().isEmpty()) {
            return tipoExp;
        } else {
            for (LAParser.TermoContext termo : ctx.outros_termos().termo()) {
                String tipoOutroTermo = verificaTipo(termo);
                tipoExp = regraTipos(tipoExp, tipoOutroTermo);
            }
        }

        return tipoExp;
    }

    public String verificaTipo(LAParser.TermoContext ctx) {
        String text = ctx.getText();
        String tipoExp = verificaTipo(ctx.fator());
        if (ctx.outros_fatores() == null && ctx.outros_fatores().getText().isEmpty()) {
            return tipoExp;
        } else {
            for (LAParser.FatorContext termo : ctx.outros_fatores().fator()) {
                String tipoOutroTermo = verificaTipo(termo);
                tipoExp = regraTipos(tipoExp, tipoOutroTermo);
            }
        }

        return tipoExp;
    }

    public String verificaTipo(LAParser.FatorContext ctx) {
        String text = ctx.getText();
        String tipoExp = verificaTipo(ctx.parcela());
        if (ctx.outras_parcelas() == null || ctx.outras_parcelas().getText().isEmpty()) {
            return tipoExp;
        } else {
            for (LAParser.ParcelaContext termo : ctx.outras_parcelas().parcela()) {
                String tipoOutroTermo = verificaTipo(termo);
                tipoExp = regraTipos(tipoExp, tipoOutroTermo);
            }
        }

        return tipoExp;
    }

    public String verificaTipo(LAParser.ParcelaContext ctx) {
        String text = ctx.getText();
        String tipoExp;
        if (ctx.parcela_unario() != null && !ctx.parcela_unario().getText().isEmpty()) {
            tipoExp = verificaTipo(ctx.parcela_unario());
        } else {
            tipoExp = verificaTipo(ctx.parcela_nao_unario());
        }

        return tipoExp;
    }

    public String verificaTipo(LAParser.Parcela_unarioContext ctx) {
        String tipoExp = null;
        
        if(ctx.expressao() != null){
            tipoExp = verificaTipo(ctx.expressao());
        }else if (ctx.NUM_REAL() != null) {
            tipoExp = "real";
        }else if (ctx.NUM_INT() != null) {
            tipoExp = "inteiro";
        }else if(ctx.chamada_partes() != null && !(ctx.chamada_partes().
         getText().equals("")) && ctx.chamada_partes().outros_ident() != null
         && !ctx.chamada_partes().outros_ident().getStart().getText().equals("[")){
            tipoExp = verificaTipo(ctx.chamada_partes().outros_ident());
        }else if(ctx.outros_ident() != null){
            tipoExp = verificaTipo(ctx.outros_ident());
        }else{
            tipoExp = escopos.buscaSimbolo(ctx.IDENT().getText()).getTipo();
        }

        return tipoExp;
    }

    public String verificaTipo(LAParser.Parcela_nao_unarioContext ctx) {
        String tipoExp;

        if (ctx.IDENT() != null) {
            tipoExp = escopos.buscaSimbolo(ctx.IDENT().getText()).getTipo();
        } else {
            tipoExp = "literal";
        }
        return tipoExp;
    }

    public String verificaTipo(LAParser.Chamada_atribuicaoContext ctx) {
        String text = ctx.getText();
        String tipo1 = verificaTipo(ctx.expressao());
        if (ctx.outros_ident() == null || ctx.outros_ident().getText().isEmpty()){//|| ctx.outros_ident().identificador() == null) {
            return tipo1;
        } else {
            String tipo2 = verificaTipo(ctx.outros_ident());
            
            return regraTipos(tipo1, tipo2);
        }

    }

    public String verificaTipo(LAParser.Outros_identContext ctx) {
        String text = ctx.getText();
        String tipoExp = verificaTipo(ctx.identificador());

        return tipoExp;
    }

    public String verificaTipo(LAParser.IdentificadorContext ctx) {
        String text = ctx.getText();
        String tipoExp = escopos.buscaSimbolo(text).getTipo();
        if (ctx.outros_ident() == null || ctx.outros_ident().getText().isEmpty()){//|| ctx.outros_ident().identificador() == null) {
            if(tipoExp.equals("registro")){
                tipoExp=escopos.buscaSimbolo(text).buscaTipoRegistro(text);
            }
            
            return tipoExp;
        } else {
            String tipoOutroTermo = verificaTipo(ctx.outros_ident().identificador());
            tipoExp = regraTipos(tipoExp, tipoOutroTermo);

        }
        return tipoExp;
    }

    public static String regraTipos(String tipo1, String tipo2) {
        if (tipo1.equals(tipo2)) {
            return tipo1;
        } else if (tipo1.equals("real") && tipo2.equals("inteiro")
                || tipo2.equals("real") && tipo1.equals("inteiro")) {
            return "real";
        } else {
            return "tipo_invalido";
        }
    }
}
