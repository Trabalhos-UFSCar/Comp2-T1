grammar LA;

programa  :  'algoritmo' CORPO 'fim-algoritmo' ;
SPACE : [ \t\r\n]+ -> skip ;
DECLARACOES  :  DECL_LOCAL_GLOBAL DECLARACOES |   ;
DECL_LOCAL_GLOBAL  :  DECLARACAO_LOCAL | DECLARACAO_GLOBAL ;
DECLARACAO_LOCAL  :  'declare' VARIAVEL 
 | 'constante' IDENT ':' TIPO_BASICO '=' VALOR_CONSTANTE
 | 'tipo' IDENT ':' TIPO ;
VARIAVEL  :  IDENT DIMENSAO MAIS_VAR ':' TIPO ;
MAIS_VAR  :  ',' IDENT DIMENSAO MAIS_VAR |   ;
IDENTIFICADOR  :  PONTEIROS_OPCIONAIS IDENT DIMENSAO OUTROS_IDENT ;
PONTEIROS_OPCIONAIS  :  '^' PONTEIROS_OPCIONAIS |   ;
OUTROS_IDENT  :  '.' IDENTIFICADOR |   ;
DIMENSAO  :  [ EXP_ARITMETICA ] DIMENSAO|   ;
TIPO  :  REGISTRO | TIPO_ESTENDIDO ;
MAIS_IDENT  :  ',' IDENTIFICADOR MAIS_IDENT |   ;
MAIS_VARIAVEIS  :  VARIAVEL MAIS_VARIAVEIS |   ;
TIPO_BASICO  :  'literal' | 'inteiro' | 'real' | 'logico' ;
TIPO_BASICO_IDENT  :  TIPO_BASICO | IDENT ;
TIPO_ESTENDIDO  :  PONTEIROS_OPCIONAIS TIPO_BASICO_IDENT ;
VALOR_CONSTANTE  :  CADEIA | NUM_INT | NUM_REAL | 'verdadeiro' | 'falso' ;
REGISTRO  :  'registro' VARIAVEL MAIS_VARIAVEIS 'fim_registro' ;
DECLARACAO_GLOBAL  :  'procedimento' IDENT '(' PARAMETROS_OPCIONAL ')' DECLARACOES_LOCAIS COMANDOS 'fim_procedimento' 
 | 'funcao' IDENT '(' PARAMETROS_OPCIONAL ')' ':' TIPO_ESTENDIDO DECLARACOES_LOCAIS COMANDOS 'fim_funcao' ;
PARAMETROS_OPCIONAL  :  PARAMETRO |   ;
PARAMETRO  :  VAR_OPCIONAL IDENTIFICADOR MAIS_IDENT ':' TIPO_ESTENDIDO MAIS_PARAMETROS ;
VAR_OPCIONAL  :  'var' |   ;
MAIS_PARAMETROS  :  ',' PARAMETRO |   ;
DECLARACOES_LOCAIS  :  DECLARACAO_LOCAL DECLARACOES_LOCAIS |   ;
CORPO  :  DECLARACOES_LOCAIS COMANDOS ;
COMANDOS  :  CMD COMANDOS |   ;
CMD  :  'leia' '(' IDENTIFICADOR MAIS_IDENT ')' 
 | 'escreva' '(' EXPRESSAO MAIS_EXPRESSAO ')' 
 | 'se' EXPRESSAO 'entao' COMANDOS SENAO_OPCIONAL 'fim_se'
 | 'caso' EXP_ARITMETICA 'seja' SELECAO SENAO_OPCIONAL 'fim_caso'
 | 'para' IDENT '<-' EXP_ARITMETICA 'ate' EXP_ARITMETICA 'faca' COMANDOS 'fim_para'
 | 'enquanto' EXPRESSAO 'faca' COMANDOS 'fim_enquanto'
 | 'faca' COMANDOS 'ate' EXPRESSAO
 | '^' IDENT OUTROS_IDENT DIMENSAO '<-' EXPRESSAO
 | IDENT CHAMADA_ATRIBUICAO
 | 'retorne' EXPRESSAO ;
MAIS_EXPRESSAO  :  ',' EXPRESSAO MAIS_EXPRESSAO |   ;
SENAO_OPCIONAL  :  'senao' COMANDOS |   ;
CHAMADA_ATRIBUICAO  :  '(' ARGUMENTOS_OPCIONAL ')' | OUTROS_IDENT DIMENSAO '<-' EXPRESSAO ;
ARGUMENTOS_OPCIONAL  :  EXPRESSAO MAIS_EXPRESSAO |   ;
SELECAO  :  CONSTANTES ':' COMANDOS MAIS_SELECAO ;
MAIS_SELECAO  :  SELECAO |   ;
CONSTANTES  :  NUMERO_INTERVALO MAIS_CONSTANTES ;
MAIS_CONSTANTES  :  ',' CONSTANTES |   ;
NUMERO_INTERVALO  :  OP_UNARIO NUM_INT INTERVALO_OPCIONAL ;
INTERVALO_OPCIONAL  :  '..' OP_UNARIO NUM_INT |   ;
OP_UNARIO  :  '-' |   ;
EXP_ARITMETICA  :  TERMO OUTROS_TERMOS ;
OP_MULTIPLICACAO  :  '*' | '/' ;
OP_ADICAO  :  '+' | '-' ;
TERMO  :  FATOR OUTROS_FATORES ;
OUTROS_TERMOS  :  OP_ADICAO TERMO OUTROS_TERMOS |   ;
FATOR  :  PARCELA OUTRAS_PARCELAS ;
OUTROS_FATORES  :  OP_MULTIPLICACAO FATOR OUTROS_FATORES |   ;
PARCELA  :  OP_UNARIO PARCELA_UNARIO | PARCELA_NAO_UNARIO ;
PARCELA_UNARIO  :  '^' IDENT OUTROS_IDENT DIMENSAO | IDENT CHAMADA_PARTES | NUM_INT | NUM_REAL | '(' EXPRESSAO ')' ;
PARCELA_NAO_UNARIO  :  '&' IDENT OUTROS_IDENT DIMENSAO | CADEIA ;
OUTRAS_PARCELAS  :  '%' PARCELA OUTRAS_PARCELAS |   ;
CHAMADA_PARTES  :  '(' EXPRESSAO MAIS_EXPRESSAO ')' | OUTROS_IDENT DIMENSAO |   ;
EXP_RELACIONAL  :  EXP_ARITMETICA OP_OPCIONAL ;
OP_OPCIONAL  :  OP_RELACIONAL EXP_ARITMETICA |   ;
OP_RELACIONAL  :  '=' | '<>' | '>=' | '<=' | '>' | '<' ;
EXPRESSAO  :  TERMO_LOGICO OUTROS_TERMOS_LOGICOS ;
OP_NAO  :  'nao' |   ;
TERMO_LOGICO  :  FATOR_LOGICO OUTROS_FATORES_LOGICOS ;
OUTROS_TERMOS_LOGICOS  :  'ou' TERMO_LOGICO OUTROS_TERMOS_LOGICOS |   ;
OUTROS_FATORES_LOGICOS  :  'e' FATOR_LOGICO OUTROS_FATORES_LOGICOS |   ;
FATOR_LOGICO  :  OP_NAO PARCELA_LOGICA ;
PARCELA_LOGICA  :  'verdadeiro' | 'falso' | EXP_RELACIONAL ;

CADEIA : ('a'..'z' | 'A'..'Z'|'0'..'9'|'_')+;
IDENT : ('a'..'z' | 'A'..'Z'|'0'..'9'|'_')+;
NUM_REAL : ('a'..'z' | 'A'..'Z'|'0'..'9'|'_')+;
NUM_INT : ('a'..'z' | 'A'..'Z'|'0'..'9'|'_')+;
