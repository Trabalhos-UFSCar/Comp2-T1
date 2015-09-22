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
