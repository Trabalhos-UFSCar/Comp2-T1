/*
    Grammar for LA language.
    Students:   Joao Paulo Soares		408034
                Thiago Nogueira			407534
                Camilo Moreira                  359645
                Thales Menato                   407976
*/

grammar LA;

@members{    
    private void stop(String msg)
    {
      throw new ParseCancellationException(msg);
    }
}

// Parser

programa  :  
    declaracoes 
    'algoritmo' 
    corpo
    'fim_algoritmo'
;

declaracoes : 
    (decl_local_global)*
;

decl_local_global  :  declaracao_local | declaracao_global ;

declaracao_local :  
    'declare' variavel 
    | 'constante' IDENT ':' tipo_basico '=' valor_constante
    | 'tipo' IDENT ':' tipo
;


variavel :  
    nome=IDENT dimensao mais_var 
    ':' 
    tp=tipo 
;

mais_var :  
    (',' nome=IDENT dimensao)*
;

identificador  :  
    ponteiros_opcionais IDENT dimensao outros_ident 
;

ponteiros_opcionais  :  
    '^' ponteiros_opcionais |   
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

tipo returns[Boolean isRegistro]   
    @init{$isRegistro=true;} :
    registro 
    | tipo_estendido {$isRegistro=false;} 
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

declaracao_global returns [String tipoFuncao] 
    @init{$tipoFuncao="procedimento";} :
    'procedimento' IDENT '(' parametros_opcional ')' declaracoes_locais comandos 'fim_procedimento' 
    | {$tipoFuncao="funcao";} 'funcao' IDENT '(' parametros_opcional ')' ':' tipo_estendido declaracoes_locais comandos 'fim_funcao' 
;

parametros_opcional  :  
    parametro |   
;

parametro  :  
    var_opcional identificador mais_ident ':' tipo_estendido mais_parametros 
;

var_opcional  :  'var' |   ;

mais_parametros  :  ',' parametro |   ;

declaracoes_locais :
    declaracao_local declaracoes_locais |     
;

corpo  :  
    declaracoes_locais comandos
;

comandos  :  cmd comandos |   ;

cmd returns[String escopoNome] 
    @init{$escopoNome="";}:  
    'leia' '(' identificador mais_ident ')' 
    | 'escreva' '(' expressao mais_expressao ')' 
    | 'se' expressao 'entao' { $escopoNome="se"; }comandos senao_opcional 'fim_se' 
    | 'caso' exp_aritmetica 'seja' { $escopoNome="caso"; } selecao senao_opcional 'fim_caso' 
    | 'para' IDENT '<-' exp_aritmetica 'ate' exp_aritmetica 'faca'{ $escopoNome="para"; } comandos 'fim_para' 
    | 'enquanto' expressao 'faca'{ $escopoNome="enquanto"; } comandos 'fim_enquanto' 
    | 'faca' { $escopoNome="ate"; }comandos 'ate' expressao 
    | '^'IDENT chamada_atribuicao 
    | IDENT chamada_atribuicao
    | 'retorne' expressao ;

mais_expressao  :  (',' expressao )*;

senao_opcional returns [String escopoNome] 
    @init{$escopoNome="";}:  
    'senao' { $escopoNome="senao"; }comandos 
    |   ;

chamada_atribuicao  :  
    '(' argumentos_opcional ')' 
    | outros_ident dimensao '<-' expressao ;

argumentos_opcional  :  expressao mais_expressao | ;

selecao  :  (constantes ':' comandos)+ ;

constantes  :  numero_intervalo (',' numero_intervalo)* ;

numero_intervalo  :  op_unario NUM_INT intervalo_opcional ;

intervalo_opcional  :  '..' op_unario NUM_INT |   ;

op_unario  :  '-' |   ;

exp_aritmetica : termo outros_termos ;

op_multiplicacao  :  '*' | '/' ;

op_adicao  :  '+' | '-' ;

termo  :  fator outros_fatores ;

outros_termos  :  (op_adicao termo)* ;

fator  :  parcela outras_parcelas ;

outros_fatores  :  (op_multiplicacao fator)* ;

parcela  :  op_unario parcela_unario | parcela_nao_unario ;

parcela_unario :  
    '^' IDENT outros_ident dimensao 
    | IDENT chamada_partes | NUM_INT | NUM_REAL | '(' expressao ')' ;

parcela_nao_unario  :  '&' IDENT outros_ident dimensao | CADEIA ;

outras_parcelas  :  ('%' parcela )* ;

chamada_partes  :  '(' expressao mais_expressao ')' | outros_ident dimensao |   ;

exp_relacional  :  exp_aritmetica op_opcional ;

op_opcional  :  op_relacional exp_aritmetica |   ;

op_relacional  :  '=' | '<>' | '>=' | '<=' | '>' | '<' ;

expressao  :  termo_logico outros_termos_logicos ;

op_nao  :  'nao' |   ;

termo_logico  :  fator_logico outros_fatores_logicos ;

outros_termos_logicos  :  ('ou' termo_logico)* ;

outros_fatores_logicos  :  ('e' fator_logico)* ;

fator_logico  :  op_nao parcela_logica ;

parcela_logica  :  'verdadeiro' | 'falso' | exp_relacional ;

// Lexer

CADEIA : '"' ( '\\"' | . )~('\n'|'\r')*? '"';

IDENT : [_a-zA-Z][_a-zA-Z0-9]*;

NUM_INT : [0-9]+;

NUM_REAL : [0-9]+'.'[0-9]+;

COMENTARIO : '{' ~[}]* '}' -> skip;

COMENTARIO_ERRADO
    : '{' ~[\r\n}]* '\n' 
      { stop("Linha " + getLine() + ": comentario nao fechado"); }
    ;

ESPACO : [ \t\r\n]+ -> skip ;

ERROR
    : . { stop("Linha "+ getLine() +": "+ getText() +" - simbolo nao identificado"); }
    ;
