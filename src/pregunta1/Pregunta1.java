/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pregunta1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian Calderon
 */
public class Pregunta1 {
    
    public static void main(String[] args) {
        //args[0] = file path
        //args size != 1 = error
        if (args.length!=1) {
            System.out.println("Wrong arguments number");
        }else{
            String file = args[0];
            try {
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                String line;
                Calculator calculator = new Calculator();
                while((line = br.readLine()) != null){
                    // ( number, { number, [ number
                    int var1=0, var2=0, var3=0; 
                    //Check number of (, {, [, ), }, ] , for syxtax error
                    boolean error = false;
                    for (int i = 0; i < line.length(); i++) {
                        char c = line.charAt (i);
                            switch(c){
                            case '(':
                                var1+=1;
                                break;
                            case '{':
                                var2+=1;
                                break;
                            case '[':
                                var3+=1;
                                break;
                            case ')':
                                var1-=1;
                                break;
                            case '}':
                                var2-=1;
                                break;
                            case ']':
                                var3-=1;
                                break;
                            //If there is *, / or +, the next character must be a number or a -, else there is a sintax error
                            case '*':
                            case '/':
                            case '+':
                                //If it starts or ends with *, +, /, there is a sintax error 
                                if (i==0 || i==line.length()-1) {
                                    error = true;
                                }else{
                                    char d = line.charAt (i+1);
                                    //If after one *, / or + follow another of these operations, there is a syntax error
                                    if (d=='*' || d=='/' || d=='+') {
                                        error = true;
                                    }
                                }
                                break;
                            case '-':
                                //If it ends with -, there is a sintax error 
                                if (i==line.length()-1) {
                                    error = true;
                                }
                                break;
                            default:
                                break;
                        }
                        if (error) {
                            break;
                        }
                    }
                    //If there is no equal amount of (), [], or {} or there is another error
                    if (var1!=0 || var2!=0 || var3!= 0 || error) {
                        System.out.println("Sintax Error");
                    }else{
                        System.out.println(line+" -> "+ calculator.evaluate(line));
                    }    
                }
                fr.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Pregunta1.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Pregunta1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
