
import java.util.ArrayList;
import java.util.List;




public class EntradaTabelaDeSimbolos {
    private String nome, tipo;
    private Integer dimensao;//TODO: checar se´será necessário este valor 
    private Boolean isPonteiro, isRegistro, isProcedimento, isFuncao;
    private List<EntradaTabelaDeSimbolos> valoresRegistro;
    private List<EntradaTabelaDeSimbolos> parametrosFuncao;
    
    public EntradaTabelaDeSimbolos(String nome, String tipo) {
        this.nome = nome;
        this.tipo = tipo;
        dimensao = 0;
        isPonteiro = isRegistro = isProcedimento = isFuncao = false;
        valoresRegistro = new ArrayList<>();
        parametrosFuncao = new ArrayList<>();
    }   
    
    public EntradaTabelaDeSimbolos(String nome, String tipo, Integer dimensao) {
        this.nome = nome;
        this.tipo = tipo;
        this.dimensao = dimensao;
        isPonteiro = isRegistro = isProcedimento = isFuncao = false;
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

    public void setAsPonteiro() {
        this.isPonteiro = true;
    }

    public void setAsRegistro() {
        this.isRegistro = true;
    }

    public void setAsProcedimento() {
        this.isProcedimento = true;
    }

    public void setAsFuncao() {
        this.isFuncao = true;
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
