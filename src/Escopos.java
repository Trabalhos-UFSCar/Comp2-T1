
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

    public void adicionarSimbolo(String nome, String tipo) {
        topo().adicionarSimbolo(nome, tipo);
    }

    public void adicionarSimbolo(String nome, String tipo, Integer dimensao) {
        topo().adicionarSimbolo(nome, tipo, dimensao);
    }

    public void desempilhar() {
        TabelaDeSimbolos ts = pilha.pop();
        System.out.println(ts.toString());
    }

    public List getTodasTabelas() {
        return pilha;
    }
}
