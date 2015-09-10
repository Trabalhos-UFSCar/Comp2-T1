


public class EntradaTabelaDeSimbolos {
    private String nome, tipo;
    private Integer dimensao;//TODO: checar se´será necessário este valor
    
    public EntradaTabelaDeSimbolos(String nome, String tipo) {
        this.nome = nome;
        this.tipo = tipo;
    }
    
    public EntradaTabelaDeSimbolos(String nome, String tipo, Integer dimensao) {
        this.nome = nome;
        this.tipo = tipo;
        this.dimensao = dimensao;
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
}
