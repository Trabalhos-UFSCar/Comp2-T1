package utils;

/**
 * A classe é um StringBuffer usado nas diversas etapas do processo
 */
public class SaidaParser {
    private StringBuffer buffer;
    public int identationLevel = 0;

    public SaidaParser() {
        this.buffer = new StringBuffer();
    }
    
    public void println(String texto) {
        for(int i=0; i<identationLevel; i++){
            buffer.append("    ");
        }
        buffer.append(texto+"\n");
    }

    @Override
    public String toString() {
        return buffer.toString();
    }
    
    
}
