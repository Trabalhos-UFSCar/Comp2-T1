
import java.util.ArrayList;
import java.util.List;

public class TabelaDeSimbolos {
    private String escopo;
    private List<EntradaTabelaDeSimbolos> simbolos;
    private List<EntradaTabelaDeSimbolos> valoresRegistro;
    private List<EntradaTabelaDeSimbolos> parametrosFuncao;
    
    public TabelaDeSimbolos(String escopo) {
        simbolos = new ArrayList<EntradaTabelaDeSimbolos>();
        valoresRegistro = new ArrayList<EntradaTabelaDeSimbolos>();
        parametrosFuncao = new ArrayList<EntradaTabelaDeSimbolos>();
        this.escopo = escopo;
    }
    
    public void adicionarSimbolo(String nome, String tipo) {
        simbolos.add(new EntradaTabelaDeSimbolos(nome,tipo));
    }
    
    public void adicionarParametro(String nome, String tipo) {
        parametrosFuncao.add(new EntradaTabelaDeSimbolos(nome,tipo));
    }
    
    public void adicionarValorRegistro(String nome, String tipo) {
        valoresRegistro.add(new EntradaTabelaDeSimbolos(nome,tipo));
    }
    
    public void adicionarSimbolo(String nome, String tipo, Integer dimensao) {
        simbolos.add(new EntradaTabelaDeSimbolos(nome,tipo, dimensao));
    }
    
    public void adicionarSimbolos(List<String> nomes, String tipo) {
        for(String s:nomes) {
            simbolos.add(new EntradaTabelaDeSimbolos(s, tipo));
        }
    }
    
    public boolean existeSimbolo(String nome) {
        for(EntradaTabelaDeSimbolos etds:simbolos) {
            if(etds.getNome().equals(nome)) {
                return true;
            }
        }
        return false;
    }
    
    public List<EntradaTabelaDeSimbolos> todosSimbolos(){
        return simbolos;
    }
    
    @Override
    public String toString() {
        String ret = "Escopo: "+escopo;
        for(EntradaTabelaDeSimbolos etds:simbolos) {
            ret += "\n   "+etds;
        }
        return ret;
    }
}
