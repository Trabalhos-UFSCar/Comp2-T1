package utils;

import java.util.ArrayList;
import java.util.List;

public class EntradaTabelaDeSimbolos {
    private String nome, tipo;
    private Integer dimensao;
    private List<EntradaTabelaDeSimbolos> parametrosFuncao;
    
    public EntradaTabelaDeSimbolos(String nome, String tipo) {
        this.nome = nome;
        this.tipo = tipo;
        dimensao = 0;
        parametrosFuncao = new ArrayList<>();
    }   
    
    public EntradaTabelaDeSimbolos(String nome, String tipo, Integer dimensao) {
        this.nome = nome;
        this.tipo = tipo;
        this.dimensao = dimensao;
        parametrosFuncao = new ArrayList<>();
    }
    
    public String getNome() {
        return nome;
    }
    
    public String getTipo() {
        return tipo;
    }

    public Integer getDimensao() {
        return dimensao;
    }
    
    @Override
    public String toString() {
        return nome+"("+tipo+")";
    }
    
    public void adicionarParametro(String nome, String tipo) {
        parametrosFuncao.add(new EntradaTabelaDeSimbolos(nome,tipo));
    }
}
