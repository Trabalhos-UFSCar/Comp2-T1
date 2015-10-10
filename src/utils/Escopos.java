package utils;

import java.util.LinkedList;
import java.util.List;

/*
    Esta classe controla todos os escopos que podem surgir durante a execução
    do código.
*/
public class Escopos {
    
    //Essa é a estrutura básica que molda o funcionamento dos escopos
    private LinkedList<TabelaDeSimbolos> pilha;

    public Escopos() {
        pilha = new LinkedList<>();
    }
    
    //cria um escopo novo
    public void empilhar(String tabelaNome) {
        pilha.push(new TabelaDeSimbolos(tabelaNome));
    }

    //metodo que ajuda trabalhar com os escopos, retornando o escopo ativo
    public TabelaDeSimbolos topo() {
        return pilha.peek();
    }

    //retorna verdadeiro se existe um simbolo com o nome passado em qualquer
    //um dos escopos definidos anteriormente
    public boolean existeSimbolo(String nome) {
        for (TabelaDeSimbolos ts : pilha) {
            if (ts.existeSimbolo(nome)) {
                return true;
            }
        }
        return false;
    }

    //diferente do método anterior que retorna verdadeiro caso o simbolo exista,
    //este retorna o objeto propriamente dito
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

    //adiciona um simbolo no escopo ativo 
    public void adicionarSimbolo(EntradaTabelaDeSimbolos simbolo) {
        topo().adicionarSimbolo(simbolo);
    }

    //adiciona um simbolo no escopo ativo
    public void adicionarSimbolo(String nome, String tipo) {
        topo().adicionarSimbolo(nome, tipo);
    }

    //adiciona um simbolo no escopo ativo
    public void adicionarSimbolo(String nome, String tipo, Integer dimensao) {
        topo().adicionarSimbolo(nome, tipo, dimensao);
    }

    //destroi um escopo
    public TabelaDeSimbolos desempilhar() {
        return pilha.pop();
    }

    //metodo de utilidade para que a pilha pode ser iterada
    public List getTodasTabelas() {
        return pilha;
    }
    
}
