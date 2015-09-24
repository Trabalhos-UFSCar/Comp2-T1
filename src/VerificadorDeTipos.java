/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jp
 */
public class VerificadorDeTipos {

    Escopos escopos;

    public VerificadorDeTipos(Escopos escopos) {
        this.escopos = escopos;
    }

    public String verificaTipo(LAParser.ExpressaoContext ctx) {
        String tipoExp = verificaTipo(ctx.termo_logico());
        if (ctx.outros_termos_logicos() == null) {
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
        String tipoExp = verificaTipo(ctx.fator_logico());
        if (ctx.outros_fatores_logicos() == null) {
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
        String tipoExp = verificaTipo(ctx.parcela_logica());

        // Se o operador lógico 'nao' está definido
        // Então o resultado é lógico apenas se a expressão
        // após o operador é também lógica
        if (ctx.op_nao() != null) {
            tipoExp = regraTipos(tipoExp, "logico");
        }

        return tipoExp;
    }

    public String verificaTipo(LAParser.Parcela_logicaContext ctx) {
        String tipoExp;

        // Verifica se é um nó folha (verdadeiro ou falso)
        // Caso contrário, continua descendo a árvore
        if (ctx.getChildCount() == 0) {
            tipoExp = new String("logico");
        } else {
            tipoExp = verificaTipo(ctx.exp_relacional());
        }

        return tipoExp;
    }

    public String verificaTipo(LAParser.Exp_relacionalContext ctx) {
        String tipoExp;

        // Se existe algum operador relacional, entao resultado é logico
        // se os dois lados da expressao podem ser comparados
        if (!ctx.op_opcional().getText().isEmpty()) {
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
        String tipoExp = verificaTipo(ctx.termo());
        if (ctx.outros_termos() == null) {
            return tipoExp;
        } else {
            if (ctx.outros_termos().termo() != null) {
                for (LAParser.TermoContext termo : ctx.outros_termos().termo()) {
                    String tipoOutroTermo = verificaTipo(termo);
                    tipoExp = regraTipos(tipoExp, tipoOutroTermo);
                }
            }
        }

        return tipoExp;
    }

    public String verificaTipo(LAParser.TermoContext ctx) {
        String tipoExp = verificaTipo(ctx.fator());
        if (ctx.outros_fatores() == null) {
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
        String tipoExp = verificaTipo(ctx.parcela());
        if (ctx.outras_parcelas() == null) {
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
        String tipoExp;
        if (ctx.parcela_unario() != null) {
            tipoExp = verificaTipo(ctx.parcela_unario());
        } else {
            tipoExp = verificaTipo(ctx.parcela_nao_unario());
        }

        return tipoExp;
    }

    public String verificaTipo(LAParser.Parcela_unarioContext ctx) {
        String tipoExp = null;

        if (ctx.IDENT() != null) {
            tipoExp = escopos.buscaSimbolo(ctx.IDENT().getText()).getTipo();
        } else if (ctx.NUM_INT() != null) {
            tipoExp = "inteiro";
        } else if (ctx.NUM_REAL() != null) {
            tipoExp = "real";
        } else if (ctx.expressao() != null) {
            tipoExp = verificaTipo(ctx.expressao());
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
        String tipo1 = verificaTipo(ctx.expressao());
        if (ctx.outros_ident() == null || ctx.outros_ident().identificador() == null) {
            return tipo1;
        } else {
            String tipo2 = verificaTipo(ctx.outros_ident());

            return regraTipos(tipo1, tipo2);
        }

    }

    public String verificaTipo(LAParser.Outros_identContext ctx) {
        String tipoExp = verificaTipo(ctx.identificador());

        return tipoExp;
    }

    public String verificaTipo(LAParser.IdentificadorContext ctx) {
        String tipoExp = escopos.buscaSimbolo(ctx.IDENT().getText()).getTipo();
        if (ctx.outros_ident() == null || ctx.outros_ident().identificador() == null) {
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
