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
        if(ctx.outros_termos_logicos() == null) {
            return tipoExp;
        } else {
            for(LAParser.Termo_logicoContext termo : ctx.outros_termos_logicos().termo_logico()) {
                String tipoOutroTermo = verificaTipo(termo);
                tipoExp = regraTipos(tipoExp, tipoOutroTermo);
            }
        }
        return tipoExp;
    }
    
    public String verificaTipo(LAParser.Termo_logicoContext ctx) {
        String tipoExp = verificaTipo(ctx.fator_logico());
        if(ctx.outros_fatores_logicos() == null) {
            return tipoExp;
        } else {
            for(LAParser.Fator_logicoContext termo : ctx.outros_fatores_logicos().fator_logico()) {
                String tipoOutroTermo = verificaTipo(termo);
                tipoExp = regraTipos(tipoExp, tipoOutroTermo);
            }
        }
        return tipoExp;
    }
     
    public String verificaTipo(LAParser.Fator_logicoContext ctx){
        String tipoExp;
        if(ctx.op_nao() != null){
            tipoExp = new String("logico");
        } else {
            tipoExp = verificaTipo(ctx.parcela_logica());
        }

        return tipoExp;
    }
     
    public String verificaTipo(LAParser.Parcela_logicaContext ctx){
        String tipoExp;
        
        // Verifica se é um nó folha.
        // Caso contrário, continua descendo a árvore
        if(ctx.getChildCount() == 0 ){
            tipoExp = new String("logico");
        } else {
            tipoExp = verificaTipo(ctx.exp_relacional());
        }

        return tipoExp;
    }
    
    public String verificaTipo(LAParser.Exp_relacionalContext ctx){
        String tipoExp;
        
        if(ctx.op_opcional() != null){
            String tipo1 = verificaTipo(ctx.exp_aritmetica());
            String tipo2 = verificaTipo(ctx.op_opcional().exp_aritmetica());
            tipoExp = regraTipos(tipo1, tipo2);
            // Se existe algum operador relacional, entao resultado é logico
            // se os dois lados da expressao podem ser comparados
            if (!tipoExp.equals("tipo_invalido"))
                tipoExp = "logico";
        } else {
            tipoExp = verificaTipo(ctx.exp_aritmetica());
        }
        
        return tipoExp;

    }
    
    public String verificaTipo(LAParser.Exp_aritmeticaContext ctx){
        String tipoExp = verificaTipo(ctx.termo());
        if(ctx.outros_termos() == null) {
            return tipoExp;
        } else {
            for(LAParser.TermoContext termo : ctx.outros_termos().termo()) {
                String tipoOutroTermo = verificaTipo(termo);
                tipoExp = regraTipos(tipoExp, tipoOutroTermo);
            }
        }
        
        return tipoExp;
    }
    
    public String verificaTipo(LAParser.TermoContext ctx){
        String tipoExp = verificaTipo(ctx.fator());
        if(ctx.outros_fatores() == null) {
            return tipoExp;
        } else {
            for(LAParser.FatorContext termo : ctx.outros_fatores().fator()) {
                String tipoOutroTermo = verificaTipo(termo);
                tipoExp = regraTipos(tipoExp, tipoOutroTermo);
            }
        }
        
        return tipoExp;
    }
    
    public String verificaTipo(LAParser.FatorContext ctx){
        String tipoExp = verificaTipo(ctx.parcela());
        if(ctx.outras_parcelas() == null) {
            return tipoExp;
        } else {
            for(LAParser.ParcelaContext termo : ctx.outras_parcelas().parcela()) {
                String tipoOutroTermo = verificaTipo(termo);
                tipoExp = regraTipos(tipoExp, tipoOutroTermo);
            }
        }
        
        return tipoExp;
    }
    
    public String verificaTipo(LAParser.ParcelaContext ctx){
        /* Se chegou até aqui, retorna o tipo do identificador
        associado nas subregras da parcela */
        return null;
    }
     
     public static String regraTipos(String tipo1, String tipo2) {
         if(tipo1.equals(tipo2)) {
             return tipo1;
         } else if(tipo1.equals("real") && tipo2.equals("inteiro") || 
                 tipo2.equals("real") && tipo1.equals("inteiro")) {
             return "real";
             
         } else {
             return "tipo_invalido";
         }
     }
}
