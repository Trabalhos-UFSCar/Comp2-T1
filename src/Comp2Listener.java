import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * This class provides an empty implementation of {@link LAListener},
 * which can be extended to create a listener which only needs to handle a subset
 * of the available methods.
 */
public class Comp2Listener extends LABaseListener {
    
    SaidaParser out;
    public Comp2Listener(SaidaParser out) {
        this.out = out;
    }
    
    
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterPrograma(LAParser.ProgramaContext ctx) {
		out.println("/* Arquivo gerado automaticamente */\n");
		out.println("#include <stdio.h>");
		out.println("#include <stdlib.h>\n");
		
		out.println("int main(){");
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitPrograma(LAParser.ProgramaContext ctx) {
		/* out.println("exitPrograma"); */

		out.println("    return 0;");
		out.println("}");
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterDeclaracoes(LAParser.DeclaracoesContext ctx) {
		/* out.println("enterDeclaracoes"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitDeclaracoes(LAParser.DeclaracoesContext ctx) {
		/* out.println("exit declaracoes"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterDecl_local_global(LAParser.Decl_local_globalContext ctx) {
		/* out.println("enterDecl_local_global"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitDecl_local_global(LAParser.Decl_local_globalContext ctx) {
		/* out.println("exit Decl_local_global"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterDeclaracao_local(LAParser.Declaracao_localContext ctx) {
		/* out.println("enterDeclaracao_local"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitDeclaracao_local(LAParser.Declaracao_localContext ctx) {
		/* out.println("exitDeclaracao_local"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterVariavel(LAParser.VariavelContext ctx) {
		/* out.println("enterVariavel"); */

		String var = "";
		if(ctx.tipo().getText().equals("inteiro")){
			var = "int ";
		}
		var += ctx.IDENT();
		out.println("    " + var + ";");
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitVariavel(LAParser.VariavelContext ctx) {
		/* out.println("exitVariavel"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterMais_var(LAParser.Mais_varContext ctx) {
		/* out.println("enterMais_var"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitMais_var(LAParser.Mais_varContext ctx) {
		/* out.println("exitMais_var"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterIdentificador(LAParser.IdentificadorContext ctx) {
		/* out.println("enterIdentificador"); */
            out.print(ctx.IDENT().getText());
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitIdentificador(LAParser.IdentificadorContext ctx) {
		/* out.println("exitIdentificador"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterPonteiros_opcionais(LAParser.Ponteiros_opcionaisContext ctx) {
		/* out.println("enterPonteiros_opcionais"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitPonteiros_opcionais(LAParser.Ponteiros_opcionaisContext ctx) {
		/* out.println("exitPonteiros_opcionais"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterOutros_ident(LAParser.Outros_identContext ctx) {
		/* out.println("enterOutros_ident"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitOutros_ident(LAParser.Outros_identContext ctx) {
		/* out.println("exitOutros_ident"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterDimensao(LAParser.DimensaoContext ctx) {
		/* out.println("enterDimensao"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitDimensao(LAParser.DimensaoContext ctx) {
		/* out.println("exitDimensao"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterTipo(LAParser.TipoContext ctx) {
		/* out.println("enterTipo"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitTipo(LAParser.TipoContext ctx) {
		/* out.println("exitTipo"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterMais_ident(LAParser.Mais_identContext ctx) {
		/* out.println("enterMais_ident"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitMais_ident(LAParser.Mais_identContext ctx) {
		/* out.println("exitMais_ident"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterMais_variaveis(LAParser.Mais_variaveisContext ctx) {
		/* out.println("enterMais_variaveis"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitMais_variaveis(LAParser.Mais_variaveisContext ctx) {
		/* out.println("exitMais_variaveis"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterTipo_basico(LAParser.Tipo_basicoContext ctx) {
		/* out.println("enterTipo_basico"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitTipo_basico(LAParser.Tipo_basicoContext ctx) {
		/* out.println("exitTipo_basico"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterTipo_basico_ident(LAParser.Tipo_basico_identContext ctx) {
		/* out.println("enterTipo_basico_ident"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitTipo_basico_ident(LAParser.Tipo_basico_identContext ctx) {
		/* out.println("exitTipo_basico_ident"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterTipo_estendido(LAParser.Tipo_estendidoContext ctx) {
		/* out.println("enterTipo_estendido"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitTipo_estendido(LAParser.Tipo_estendidoContext ctx) {
		/* out.println("exitTipo_estendido"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterValor_constante(LAParser.Valor_constanteContext ctx) {
		/* out.println("enterValor_constante"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitValor_constante(LAParser.Valor_constanteContext ctx) {
		/* out.println("exitValor_constante"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRegistro(LAParser.RegistroContext ctx) {
		/* out.println("enterRegistro"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRegistro(LAParser.RegistroContext ctx) {
		/* out.println("exitRegistro"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterDeclaracao_global(LAParser.Declaracao_globalContext ctx) {
		/* out.println("enterDeclaracao_global"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitDeclaracao_global(LAParser.Declaracao_globalContext ctx) {
		/* out.println("exitDeclaracao_global"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterParametros_opcional(LAParser.Parametros_opcionalContext ctx) {
		/* out.println("enterParametros_opcional"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitParametros_opcional(LAParser.Parametros_opcionalContext ctx) {
		/* out.println("exitParametros_opcional"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterParametro(LAParser.ParametroContext ctx) {
		/* out.println("enterParametro"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitParametro(LAParser.ParametroContext ctx) {
		/* out.println("exitParametro"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterVar_opcional(LAParser.Var_opcionalContext ctx) {
		/* out.println("enterVar_opcional"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitVar_opcional(LAParser.Var_opcionalContext ctx) {
		/* out.println("exitVar_opcional"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterMais_parametros(LAParser.Mais_parametrosContext ctx) {
		/* out.println("enterMais_parametros"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitMais_parametros(LAParser.Mais_parametrosContext ctx) {
		/* out.println("exitMais_parametros"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterDeclaracoes_locais(LAParser.Declaracoes_locaisContext ctx) {
		/* out.println("enterDeclaracoes_locais"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitDeclaracoes_locais(LAParser.Declaracoes_locaisContext ctx) {
		/* out.println("exitDeclaracoes_locais"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterCorpo(LAParser.CorpoContext ctx) {
	/* out.println("enterCorpo"); */
        }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitCorpo(LAParser.CorpoContext ctx) {
	/* out.println("exitCorpo"); */
        }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterComandos(LAParser.ComandosContext ctx) {
		/* out.println("enterComandos"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitComandos(LAParser.ComandosContext ctx) {
		/* out.println("exitComandos"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterCmd(LAParser.CmdContext ctx) {
		/* out.println("enterCmd"); */
//            out.println(ctx.toStringTree());
            if(ctx.getStart().getText().equals("leia")){
                out.print("    scanf(\"%d\",&");
            }else if(ctx.getStart().getText().equals("escreva")){
                out.print("    printf(\"%d\",");
                ctx.expressao();
                out.println(");");
            }
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitCmd(LAParser.CmdContext ctx) {
		/* out.println("exitCmd"); */
            if(ctx.getStart().getText().equals("leia")){
                out.println(");");
            }
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterMais_expressao(LAParser.Mais_expressaoContext ctx) {
		/* out.println("enterMais_expressao"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitMais_expressao(LAParser.Mais_expressaoContext ctx) {
		/* out.println("exitMais_expressao"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterSenao_opcional(LAParser.Senao_opcionalContext ctx) {
		/* out.println("enterSenao_opcional"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitSenao_opcional(LAParser.Senao_opcionalContext ctx) {
		/* out.println("exitSenao_opcional"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterChamada_atribuicao(LAParser.Chamada_atribuicaoContext ctx) {
		/* out.println("enterChamada_atribuicao"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitChamada_atribuicao(LAParser.Chamada_atribuicaoContext ctx) {
		/* out.println("exitChamada_atribuicao"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterArgumentos_opcional(LAParser.Argumentos_opcionalContext ctx) {
		/* out.println("enterArgumentos_opcional"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitArgumentos_opcional(LAParser.Argumentos_opcionalContext ctx) {
		/* out.println("exitArgumentos_opcional"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterSelecao(LAParser.SelecaoContext ctx) {
		/* out.println("enterSelecao"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitSelecao(LAParser.SelecaoContext ctx) {
		/* out.println("exitSelecao"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterMais_selecao(LAParser.Mais_selecaoContext ctx) {
		/* out.println("enterMais_selecao"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitMais_selecao(LAParser.Mais_selecaoContext ctx) {
		/* out.println("exitMais_selecao"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterConstantes(LAParser.ConstantesContext ctx) {
		/* out.println("enterConstantes"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitConstantes(LAParser.ConstantesContext ctx) {
		/* out.println("exitConstantes"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterMais_constantes(LAParser.Mais_constantesContext ctx) {
		/* out.println("enterMais_constantes"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitMais_constantes(LAParser.Mais_constantesContext ctx) {
		/* out.println("exitMais_constantes"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNumero_intervalo(LAParser.Numero_intervaloContext ctx) {
		/* out.println("enterNumero_intervalo"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNumero_intervalo(LAParser.Numero_intervaloContext ctx) {
		/* out.println("exitNumero_intervalo"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterIntervalo_opcional(LAParser.Intervalo_opcionalContext ctx) {
		/* out.println("enterIntervalo_opcional"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitIntervalo_opcional(LAParser.Intervalo_opcionalContext ctx) {
		/* out.println("exitIntervalo_opcional"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterOp_unario(LAParser.Op_unarioContext ctx) {
		/* out.println("enterOp_unario"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitOp_unario(LAParser.Op_unarioContext ctx) {
		/* out.println("exitOp_unario"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterExp_aritmetica(LAParser.Exp_aritmeticaContext ctx) {
		/* out.println("enterExp_aritmetica"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitExp_aritmetica(LAParser.Exp_aritmeticaContext ctx) {
		/* out.println("exitExp_aritmetica"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterOp_multiplicacao(LAParser.Op_multiplicacaoContext ctx) {
		/* out.println("enterOp_multiplicacao"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitOp_multiplicacao(LAParser.Op_multiplicacaoContext ctx) {
		/* out.println("exitOp_multiplicacao"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterOp_adicao(LAParser.Op_adicaoContext ctx) {
		/* out.println("enterOp_adicao"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitOp_adicao(LAParser.Op_adicaoContext ctx) {
		/* out.println("exitOp_adicao"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterTermo(LAParser.TermoContext ctx) {
		/* out.println("enterTermo"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitTermo(LAParser.TermoContext ctx) {
		/* out.println("exitTermo"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterOutros_termos(LAParser.Outros_termosContext ctx) {
		/* out.println("enterOutros_termos"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitOutros_termos(LAParser.Outros_termosContext ctx) {
		/* out.println("exitOutros_termos"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterFator(LAParser.FatorContext ctx) {
		/* out.println("enterFator"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitFator(LAParser.FatorContext ctx) {
		/* out.println("exitFator"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterOutros_fatores(LAParser.Outros_fatoresContext ctx) {
		/* out.println("enterOutros_fatores"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitOutros_fatores(LAParser.Outros_fatoresContext ctx) {
		/* out.println("exitOutros_fatores"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterParcela(LAParser.ParcelaContext ctx) {
		/* out.println("enterParcela"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitParcela(LAParser.ParcelaContext ctx) {
		/* out.println("exitParcela"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterParcela_unario(LAParser.Parcela_unarioContext ctx) {
		/* out.println("enterParcela_unario"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitParcela_unario(LAParser.Parcela_unarioContext ctx) {
		/* out.println("exitParcela_unario"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterParcela_nao_unario(LAParser.Parcela_nao_unarioContext ctx) {
		/* out.println("enterParcela_nao_unario"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitParcela_nao_unario(LAParser.Parcela_nao_unarioContext ctx) {
		/* out.println("exitParcela_nao_unario"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterOutras_parcelas(LAParser.Outras_parcelasContext ctx) {
		/* out.println("enterOutras_parcelas"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitOutras_parcelas(LAParser.Outras_parcelasContext ctx) {
		/* out.println("exitOutras_parcelas"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterChamada_partes(LAParser.Chamada_partesContext ctx) {
		/* out.println("enterChamada_partes"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitChamada_partes(LAParser.Chamada_partesContext ctx) {
		/* out.println("exitChamada_partes"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterExp_relacional(LAParser.Exp_relacionalContext ctx) {
		/* out.println("enterExp_relacional"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitExp_relacional(LAParser.Exp_relacionalContext ctx) {
		/* out.println("exitExp_relacional"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterOp_opcional(LAParser.Op_opcionalContext ctx) {
		/* out.println("enterOp_opcional"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitOp_opcional(LAParser.Op_opcionalContext ctx) {
		/* out.println("exitOp_opcional"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterOp_relacional(LAParser.Op_relacionalContext ctx) {
		/* out.println("enterOp_relacional"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitOp_relacional(LAParser.Op_relacionalContext ctx) {
		/* out.println("exitOp_relacional"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterExpressao(LAParser.ExpressaoContext ctx) {
		/* out.println("enterExpressao"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitExpressao(LAParser.ExpressaoContext ctx) {
		/* out.println("exitExpressao"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterOp_nao(LAParser.Op_naoContext ctx) {
		/* out.println("enterOp_nao"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitOp_nao(LAParser.Op_naoContext ctx) {
		/* out.println("exitOp_nao"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterTermo_logico(LAParser.Termo_logicoContext ctx) {
		/* out.println("enterTermo_logico"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitTermo_logico(LAParser.Termo_logicoContext ctx) {
		/* out.println("exitTermo_logico"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterOutros_termos_logicos(LAParser.Outros_termos_logicosContext ctx) {
		/* out.println("enterOutros_termos_logicos"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitOutros_termos_logicos(LAParser.Outros_termos_logicosContext ctx) {
		/* out.println("exitOutros_termos_logicos"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterOutros_fatores_logicos(LAParser.Outros_fatores_logicosContext ctx) {
		/* out.println("enterOutros_fatores_logicos"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitOutros_fatores_logicos(LAParser.Outros_fatores_logicosContext ctx) {
		/* out.println("exitOutros_fatores_logicos"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterFator_logico(LAParser.Fator_logicoContext ctx) {
		/* out.println("enterFator_logico"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitFator_logico(LAParser.Fator_logicoContext ctx) {
		/* out.println("exitFator_logico"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterParcela_logica(LAParser.Parcela_logicaContext ctx) {
		/* out.println("enterParcela_logica"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitParcela_logica(LAParser.Parcela_logicaContext ctx) {
		/* out.println("exitParcela_logica"); */
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterEveryRule(ParserRuleContext ctx) {
		/* out.println("enterEveryRule"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitEveryRule(ParserRuleContext ctx) {
		/* out.println("exitEveryRule"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void visitTerminal(TerminalNode node) {
		/* out.println("visitTerminal"); */
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void visitErrorNode(ErrorNode node) {
		/* out.println("visitErrorNode"); */
	}
}
