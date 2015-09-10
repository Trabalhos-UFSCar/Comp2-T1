/*
    Grammar for LA language.
    Students:   Joao Paulo Soares		408034
                Thiago Nogueira			407534
                Camilo Moreira                  359645
                Thales Menato                   407976
*/

grammar LA;

@members{
    Escopos escopos = new Escopos();
    
    private void stop(String msg)
    {
      throw new ParseCancellationException(msg);
    }
}

// Parser

programa  :  
    { escopos.empilhar("global"); }    
    declaracoes 
    'algoritmo' 
    { escopos.empilhar("corpo"); }
    corpo
    { escopos.desempilhar(); }
    'fim_algoritmo'
    { escopos.desempilhar(); }
;

declaracoes  :  
    decl_local_global  declaracoes   |   
    ;

decl_local_global  :  declaracao_local | declaracao_global ;

declaracao_local  :  
    'declare' variavel 
    | 'constante' IDENT ':' tipo_basico '=' valor_constante 
    | 'tipo' IDENT ':' tipo ;


variavel returns [List<String> nomes]
    @init{ $nomes=new ArrayList<>();} :  
    nome=IDENT dimensao  outros=mais_var 
    ':' 
    tp=tipo 
    {
        $nomes.add($nome.text);
        $nomes.addAll($outros.nomes);
        for(String n:$nomes){
            if(!escopos.topo().existeSimbolo(n)){
                escopos.adicionarSimbolo(n,$tp.text);
            } else {
                //reporta erro
            }
        }
    }
;

mais_var returns [List<String> nomes] 
    @init{$nomes=new ArrayList<>();} :
  
    ',' nome=IDENT dimensao outros=mais_var
    {
        $nomes.add($nome.text);
        $nomes.addAll($outros.nomes);
    } 
    |   
;

identificador  :  
    ponteiros_opcionais IDENT dimensao outros_ident 
;

ponteiros_opcionais  :  
    '^' ponteiros_opcionais 
    |   
;

outros_ident  :  
    '.' identificador 
    | 
;

dimensao :
    '[' exp_aritmetica ']' 
    dimensao 
    | 
;

tipo :  
    registro 
    | tipo_estendido 
;

mais_ident  :  
    ',' identificador mais_ident 
    | 
;

mais_variaveis  :  
    variavel mais_variaveis 
    | 
;

tipo_basico  :  
    'literal' 
    | 'inteiro' 
    | 'real' 
    | 'logico' 
;

tipo_basico_ident  :  
    tipo_basico 
    | IDENT 
;

tipo_estendido  :  
    ponteiros_opcionais tipo_basico_ident 
;

valor_constante  :  
    CADEIA | NUM_INT | NUM_REAL | 'verdadeiro' | 'falso' 
;

registro  :  
    'registro' variavel mais_variaveis 'fim_registro' 
;

declaracao_global  :  
    'procedimento' IDENT '(' parametros_opcional ')' declaracoes_locais comandos 'fim_procedimento' 
    | 'funcao' IDENT '(' parametros_opcional ')' ':' tipo_estendido declaracoes_locais comandos 'fim_funcao' 
;

parametros_opcional  :  
    parametro |   
;

parametro  :  
    var_opcional identificador mais_ident ':' tipo_estendido mais_parametros 
;

var_opcional  :  'var' |   ;

mais_parametros  :  ',' parametro |   ;

declaracoes_locais  :  declaracao_local declaracoes_locais |   ;

corpo  :  
    declaracoes_locais comandos
;

comandos  :  cmd comandos |   ;

cmd  :  
    'leia' '(' identificador mais_ident ')' 
    | 'escreva' '(' expressao mais_expressao ')' 
    | 'se' expressao 'entao' { escopos.empilhar("se"); }comandos senao_opcional 'fim_se'{ escopos.desempilhar(); } 
    | 'caso' exp_aritmetica 'seja' { escopos.empilhar("caso"); } selecao senao_opcional 'fim_caso' { escopos.desempilhar(); } 
    | 'para' IDENT '<-' exp_aritmetica 'ate' exp_aritmetica 'faca'{ escopos.empilhar("para"); } comandos 'fim_para' { escopos.desempilhar(); } 
    | 'enquanto' expressao 'faca'{ escopos.empilhar("enquanto"); } comandos 'fim_enquanto' { escopos.desempilhar(); } 
    | 'faca' { escopos.empilhar("ate"); }comandos  { escopos.desempilhar(); }'ate' expressao 
    | '^' nome=IDENT outros_ident dimensao '<-' expressao 
    {if(!escopos.existeSimbolo($nome.text)){
        //reportar erro
     }
    } 
    | nome=IDENT chamada_atribuicao
    {if(!escopos.existeSimbolo($nome.text)){
        //reportar erro
     }
    } 
    | 'retorne' expressao ;

mais_expressao  :  ',' expressao mais_expressao |   ;

senao_opcional  :  
    'senao' { escopos.empilhar("senao"); }comandos{ escopos.desempilhar(); } 
    |   ;

chamada_atribuicao  :  
    '(' argumentos_opcional ')' 
    | outros_ident dimensao '<-' expressao ;

argumentos_opcional  :  expressao mais_expressao |   ;

selecao  :  constantes ':' comandos mais_selecao ;

mais_selecao  :  selecao |   ;

constantes  :  numero_intervalo mais_constantes ;

mais_constantes  :  ',' constantes |   ;

numero_intervalo  :  op_unario NUM_INT intervalo_opcional ;

intervalo_opcional  :  '..' op_unario NUM_INT |   ;

op_unario  :  '-' |   ;

exp_aritmetica : termo outros_termos ;

op_multiplicacao  :  '*' | '/' ;

op_adicao  :  '+' | '-' ;

termo  :  fator outros_fatores ;

outros_termos  :  op_adicao termo outros_termos |   ;

fator  :  parcela outras_parcelas ;

outros_fatores  :  op_multiplicacao fator outros_fatores |   ;

parcela  :  op_unario parcela_unario | parcela_nao_unario ;

parcela_unario  :  
    '^' IDENT outros_ident dimensao 
    | IDENT chamada_partes | NUM_INT | NUM_REAL | '(' expressao ')' ;

parcela_nao_unario  :  '&' IDENT outros_ident dimensao | CADEIA ;

outras_parcelas  :  '%' parcela outras_parcelas |   ;

chamada_partes  :  '(' expressao mais_expressao ')' | outros_ident dimensao |   ;

exp_relacional  :  exp_aritmetica op_opcional ;

op_opcional  :  op_relacional exp_aritmetica |   ;

op_relacional  :  '=' | '<>' | '>=' | '<=' | '>' | '<' ;

expressao  :  termo_logico outros_termos_logicos ;

op_nao  :  'nao' |   ;

termo_logico  :  fator_logico outros_fatores_logicos ;

outros_termos_logicos  :  'ou' termo_logico outros_termos_logicos |   ;

outros_fatores_logicos  :  'e' fator_logico outros_fatores_logicos |   ;

fator_logico  :  op_nao parcela_logica ;

parcela_logica  :  'verdadeiro' | 'falso' | exp_relacional ;

// Lexer

CADEIA : '"' ~[\r\n]* '"';

IDENT : [_a-zA-Z][_a-zA-Z0-9]*;

NUM_INT : [0-9]+;

NUM_REAL : [0-9]+'.'[0-9]+;

COMENTARIO : '{' .*? '}' -> skip;

COMENTARIO_ERRADO
    : '{' ~('\r'|'\n'|'}')* '\n' 
      { stop("Linha " + getLine() + ": comentario nao fechado"); }
    ;

ESPACO : [ \t\r\n]+ -> skip ;

ERROR
    : . { stop("Linha "+ getLine() +": "+ getText() +" - simbolo nao identificado"); }
    ;
