
import java.util.ArrayList;
import java.util.List;




public class EntradaTabelaDeSimbolos {
    private String nome, tipo;
    private Integer dimensao;//TODO: checar se´será necessário este valor 
    private List<EntradaTabelaDeSimbolos> valoresRegistro;
    private List<EntradaTabelaDeSimbolos> parametrosFuncao;
    
    public EntradaTabelaDeSimbolos(String nome, String tipo) {
        this.nome = nome;
        this.tipo = tipo;
        dimensao = 0;
        valoresRegistro = new ArrayList<>();
        parametrosFuncao = new ArrayList<>();
    }   
    
    public EntradaTabelaDeSimbolos(String nome, String tipo, Integer dimensao) {
        this.nome = nome;
        this.tipo = tipo;
        this.dimensao = dimensao;
        valoresRegistro = new ArrayList<>();
        parametrosFuncao = new ArrayList<>();
    }
    
    public String buscaTipoRegistro(String nome){
        for(EntradaTabelaDeSimbolos entrada:valoresRegistro){
            if(entrada.nome.equals(nome)){
                return entrada.tipo;
            }
        }
        
        return "";
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
    
    public void adicionarValorRegistro(String nome, String tipo) {
        valoresRegistro.add(new EntradaTabelaDeSimbolos(nome,tipo));
    }
}
