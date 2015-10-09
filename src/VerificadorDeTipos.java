
import java.util.Map;

/**
 * Esta classe é responsável pela verificação do tipo de uma expressão ou outros
 * elementos derivados de uma expressão (tais como um identificador).
 * 
 * Ela percorre recursivamente toda a árvore da expressão até as folhas
 * e retorna fazendo uma análise dos nós filhos/irmãos e passando para os nós pais.
 * 
 * A comparação entre tipos é feito por meior da função regraTipos(s1,s2).
 * Outras comparações semânticas são adicionadas durante o algoritmo.
 */
public class VerificadorDeTipos {
    
    //Contém todas as variáveis de todos os escopos criados durante o semântico
    Escopos escopos;
    Map<String, String> tipos;
    
    /**
     * Inicializa a classe como a informação do escopos que precisam verificação de tipo
     * @param escopos escopos que contém informação das variáveis
     */
    public VerificadorDeTipos(Escopos escopos, Map<String, String> tipos) {
        this.escopos = escopos;
        this.tipos = tipos;
    }
    
    /**
     * O método encontra o tipo do primeiro termo lógico
     * da expressão. Se outros termos lógicos estão presentes,
     * compara tipo entre termos lógicos consecutivos 2 a 2 por
     * meio de regraTipos().
     * 
     * @param ctx contexto atual
     * @return tipo encontrado do contexto atual
     */

    public String verificaTipo(LAParser.ExpressaoContext ctx) {
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
    
    /**
     * O método encontra o tipo do primeiro fator lógico
     * do termo lógico. Se outros fatores lógicos estão presentes,
     * compara tipo entre fatores lógicos consecutivos 2 a 2 por
     * meio de regraTipos().
     * 
     * @param ctx contexto atual
     * @return tipo encontrado do contexto atual
     */

    public String verificaTipo(LAParser.Termo_logicoContext ctx) {
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
    
    /**
     * O método encontra o tipo da primeira parcela lógica
     * do fator lógico.
     * 
     * Se o operador nao está presente, retorna tipo lógico se 
     * a parcela lógica tem tipo lógico e tipo_invalido caso contrário.
     * 
     * @param ctx contexto atual
     * @return tipo encontrado do contexto atual
     */

    public String verificaTipo(LAParser.Fator_logicoContext ctx) {
        String tipoExp = verificaTipo(ctx.parcela_logica());

        // Se o operador lógico 'nao' está definido
        // Então o resultado é lógico apenas se a expressão
        // após o operador é também lógica
        if (ctx.op_nao() != null && !ctx.op_nao().getText().isEmpty()) {
            tipoExp = regraTipos(tipoExp, "logico");
        }

        return tipoExp;
    }
    
    /**
     * O método verifica se o nó é uma folha. Se for folha, retorna tipo lógico
     * pois é 'verdadeiro' ou 'falso'. Caso contrário, retorna o tipo
     * da expressão relacional.
     * 
     * @param ctx contexto atual
     * @return tipo encontrado do contexto atual
     */

    public String verificaTipo(LAParser.Parcela_logicaContext ctx) {
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
    
    /**
     * O método verifica se existe um operador relacional entre
     * as expressoes aritmeticas. 
     * Se é encontrado, calcula o tipo dos dois lados da expressão. 
     * Se se relacionam pela regraTipos(), retorna tipo lógico.
     * Se não se relacionam, retorna tipo inválido.
     * Se não tem operadore relacional, retorna o tipo da primeira
     * expressão aritmética.
     * 
     * @param ctx contexto atual
     * @return tipo encontrado do contexto atual
     */

    public String verificaTipo(LAParser.Exp_relacionalContext ctx) {
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
    
    /**
     * O método encontra o tipo do primeiro termo
     * da expressão aritmética. Se outros termos estão presentes,
     * compara tipo entre termos consecutivos 2 a 2 por
     * meio de regraTipos().
     * 
     * @param ctx contexto atual
     * @return tipo encontrado do contexto atual
     */

    public String verificaTipo(LAParser.Exp_aritmeticaContext ctx) {
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
    
    /**
     * O método encontra o tipo do primeiro fator
     * do termo. Se outros fatores estão presentes,
     * compara tipo entre fatores consecutivos 2 a 2 por
     * meio de regraTipos().
     * 
     * @param ctx contexto atual
     * @return tipo encontrado do contexto atual
     */

    public String verificaTipo(LAParser.TermoContext ctx) {
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

    /**
     * O método encontra o tipo da primeira parcela
     * do fator. Se outras parcelas estão presentes,
     * compara tipo entre parcelas consecutivas 2 a 2 por
     * meio de regraTipos().
     * 
     * @param ctx contexto atual
     * @return tipo encontrado do contexto atual
     */
    public String verificaTipo(LAParser.FatorContext ctx) {
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
    
    /**
     * O método retorna o tipo da parcela unária se a mesma está presente.
     * Caso contrário, retorna o tipo da parcela não unária.
     * 
     * @param ctx contexto atual
     * @return tipo encontrado do contexto atual
     */

    public String verificaTipo(LAParser.ParcelaContext ctx) {
        String tipoExp;
        if (ctx.parcela_unario() != null && !ctx.parcela_unario().getText().isEmpty()) {
            tipoExp = verificaTipo(ctx.parcela_unario());
        } else {
            tipoExp = verificaTipo(ctx.parcela_nao_unario());
        }

        return tipoExp;
    }
    
    /**
     * O método retorna o tipo da expressao entre parenteses se a mesma está
     * presente.
     * Retorna 'real' ou 'inteiro' se as respectivas regras léxicas estão presentes.
     * Retorna o tipo da função se chamada_partes() está definido
     * Retorna o tipo do registro se outros_ident() está definido
     * Retorna o tipo da regra léxica IDENT() se nenhuma outra opção foi encontrada.
     * 
     * @param ctx contexto atual
     * @return tipo encontrado do contexto atual
     */

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
    
    /**
     * Retorna o tipo do identificador do endereço se o mesmo está definido
     * Retorna 'literal' se a regra léxica está definida.
     * 
     * @param ctx contexto atual
     * @return tipo encontrado do contexto atual
     */

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
            String nome = ((LAParser.CmdContext) ctx.parent).IDENT().getText();
            nome = nome + ctx.outros_ident().getText();
            Object tipo2 = escopos.buscaSimbolo(nome);
            
            return regraTipos(tipo1, tipo1);
        }

    }
    
    public String verificaTipo(LAParser.Outros_identContext ctx) {
        String text = ctx.getText();
        return escopos.buscaTipoRegistro(text);
    }

    public String verificaTipo(LAParser.IdentificadorContext ctx) {
        String text = ctx.getText();
        EntradaTabelaDeSimbolos entrada = escopos.buscaSimbolo(text);
        String tipoExp;
        System.out.println("Processando: "+text);
        //se não foi encontrada nos escopos, quer dizer que este é um identificador
        //de um registro
        if(entrada!=null){
            tipoExp=entrada.getTipo();
        } else {
            tipoExp=verificaTipo(ctx.outros_ident());
        }
        
        if (ctx.outros_ident() == null || ctx.outros_ident().getText().isEmpty()){//|| ctx.outros_ident().identificador() == null) {
            return tipoExp;
        } else {
            String tipoOutroTermo = verificaTipo(ctx.outros_ident().identificador());
            tipoExp = regraTipos(tipoExp, tipoOutroTermo);

        }
        return tipoExp;
    }
    
    public String verificaTipo(LAParser.TipoContext ctx){
        String tipoExp;
        
        if(ctx.getText().startsWith("registro"))
            tipoExp = "registro";
        else
            tipoExp = verificaTipo(ctx.tipo_estendido());
        
        return tipoExp;
    }
    
    public String verificaTipo(LAParser.Tipo_estendidoContext ctx){
        String tipoExp = verificaTipo(ctx.tipo_basico_ident());
        
        return tipoExp;
    }
    
    public String verificaTipo(LAParser.Tipo_basico_identContext ctx){
        String tipoExp;
        
        if(ctx.tipo_basico() != null ){
            tipoExp = ctx.tipo_basico().getText();
        } else
            tipoExp = tipos.get(ctx.IDENT().getText());
        
        return tipoExp;
    }
    
    /**
     * O método análise dois tipos (em forma de String) e retorna o tipo da combinação.
     * 
     * Retorna tipo1 se tipo1 == tipo2
     * Retorna 'real' se tipo1 = 'real' e tipo2 = 'inteiro' ou vice versa;
     * Retorna tipo_inválido caso contrário.
     * 
     * @param tipo1
     * @param tipo2
     * @return o tipo da combinação de tipo1 e tipo2
     */

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
