/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jp
 */
public class SaidaParser {
    private StringBuffer buffer;
    public int identationLevel = 0;

    public SaidaParser() {
        this.buffer = new StringBuffer();
    }
    
//    public void print(String texto){
//        buffer.append(texto);
//    }
    
    public void println(String texto) {
        for(int i=0; i<identationLevel; i++){
            buffer.append("    ");
        }
        buffer.append(texto+"\n");
    }

    @Override
    public String toString() {
        return buffer.toString(); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
