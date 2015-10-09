
import java.util.LinkedList;
import java.util.List;

public class Escopos {

    private LinkedList<TabelaDeSimbolos> pilha;

    public Escopos() {
        pilha = new LinkedList<>();
    }

    public void empilhar(String tabelaNome) {
        pilha.push(new TabelaDeSimbolos(tabelaNome));
    }

    public TabelaDeSimbolos topo() {
        return pilha.peek();
    }

    public boolean existeSimbolo(String nome) {
        for (TabelaDeSimbolos ts : pilha) {
            if (ts.existeSimbolo(nome)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean existeRegistro(String nome) {
        for (TabelaDeSimbolos ts : pilha) {
            for(EntradaTabelaDeSimbolos entrada:ts.todosSimbolos()){
                return !entrada.buscaTipoRegistro(nome).equals("");
            }
        }
        return false;
    }

    public EntradaTabelaDeSimbolos buscaSimbolo(String nome) {
        for (TabelaDeSimbolos ts : pilha) {
            for (EntradaTabelaDeSimbolos entrada : ts.todosSimbolos()) {
                if (entrada.getNome().equals(nome)) {
                    return entrada;
                }
            }
        }
        return null;
    }

    public void adicionarSimbolo(EntradaTabelaDeSimbolos simbolo) {
        topo().adicionarSimbolo(simbolo);
    }

    public void adicionarSimbolo(String nome, String tipo) {
        topo().adicionarSimbolo(nome, tipo);
    }

    public void adicionarSimbolo(String nome, String tipo, Integer dimensao) {
        topo().adicionarSimbolo(nome, tipo, dimensao);
    }

    public TabelaDeSimbolos desempilhar() {
        return pilha.pop();
    }

    public List getTodasTabelas() {
        return pilha;
    }

    public void adicionarParametro(String nome, String tipo) {
        topo().adicionarParametro(nome, tipo);
    }

    public void adicionarValorRegistro(String nome, String tipo) {
        topo().adicionarValorRegistro(nome, tipo);
    }

    public String buscaTipoRegistro(String nome) {
        for (TabelaDeSimbolos tabela : pilha) {
            for (EntradaTabelaDeSimbolos entrada : tabela.todosSimbolos()) {
                    return entrada.buscaTipoRegistro(nome);
                
            }
        }

        return "";
    }
}
