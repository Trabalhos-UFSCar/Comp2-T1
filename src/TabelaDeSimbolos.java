
import java.util.LinkedList;
import java.util.List;

public class TabelaDeSimbolos {
    private String escopo;
    private LinkedList<EntradaTabelaDeSimbolos> simbolos;
    
    public TabelaDeSimbolos(String escopo) {
        simbolos = new LinkedList<>();
        this.escopo = escopo;
    }
    
    public void adicionarSimbolo(String nome, String tipo) {
        simbolos.add(new EntradaTabelaDeSimbolos(nome,tipo));
    }
    
    public void adicionarSimbolo(EntradaTabelaDeSimbolos entrada) {
        simbolos.add(entrada);
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
    
    public void adicionarParametro(String nome, String tipo) {
       simbolos.peek().adicionarParametro(nome, tipo);
    }
    
    public void adicionarValorRegistro(String nome, String tipo) {
        simbolos.peek().adicionarValorRegistro(nome, tipo);
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
