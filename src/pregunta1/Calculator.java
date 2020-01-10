/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pregunta1;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Sebastian Calderon
 */
public class Calculator {
    
    public String evaluate(String exp){
        //System.out.println(exp);
        String newString = null;
        //There is (), [] or {} in the exp
        boolean exists = true;
        //While there are sub expressions
        while (exists) {
            for (int i = 0; i < exp.length(); i++) {
                char c = exp.charAt (i);
                int end = 0;
                //The first open parenthesis is searched
                switch(c){
                    case '(':
                        for (int j = i+1; j < exp.length(); j++) {
                            char d = exp.charAt (j);
                            //If there is another open parenthesis in the sub expression, it is ruled out to resolve it
                            if (d=='{' || d=='[' || d=='(') {
                                break;
                            //Else, the term index of the sub expression is recorded to solve the sub expression
                            }else if (d==')'){
                                end = j+1;
                                break;
                            }
                        }
                        break;
                    case '{':
                        for (int j = i+1; j < exp.length(); j++) {
                            char d = exp.charAt (j);
                            //If there is another open parenthesis in the sub expression, it is ruled out to resolve it
                            if (d=='(' || d=='[' || d=='{') {
                                break;
                            //Else, the term index of the sub expression is recorded to solve the sub expression
                            }else if(d=='}'){
                                end = j+1;
                                break;
                            }
                        }
                        break;
                    case '[':
                        for (int j = i+1; j < exp.length(); j++) {
                            char d = exp.charAt (j);
                            //If there is another open parenthesis in the sub expression, it is ruled out to resolve it
                            if (d=='{' || d=='['|| d=='[') {
                                break;
                            //Else, the term index of the sub expression is recorded to solve the sub expression
                            }else if(d==']'){
                                end = j+1;
                                break;
                            }
                        }
                        break;
                    default:
                        break;
                }
                //If there is a sub expression, it is resolved
                if (end!=0) {
                    //Sub expression without parentheses
                    String subString = exp.substring(i+1, end-1);
                    //System.out.println(subString);
                    String result = resolve(subString);
                    //System.out.println(result);
                    //The sub expression is replaced by the result in the full expression
                    newString = exp.substring(0, i).concat(result).concat(exp.substring(end,exp.length()));
                    exp = newString;
                    //System.out.println(exp);
                }
            }
            //If a sub expression was resolved
            if (newString!=null) {
                for (int i = 0; i < newString.length(); i++) {
                    char c = newString.charAt(i);
                    //Check if there are more sub expressions
                    if (c=='(' || c=='{' || c=='[') {
                        exists = true;
                        break;
                    }else{
                        exists = false;
                    }
                }
            //Else, the analyzed expression does not contain sub expressions
            }else{
                exists = false;
            }
        }
        //If the expression no longer contains sub expressions, it is resolved
        return resolve(exp);
    }

    private String resolve(String substring) {
        //ArrayList to save each expression number
        List<Double> numbers = new ArrayList<>();
        //Pattern to obtain expression numbers
        Matcher finder = Pattern.compile("\\d+(\\.\\d+)?").matcher(substring);
        while (finder.find()) {            
            numbers.add(Double.parseDouble(finder.group()));
        }
        String operators = substring;
        //Format to eliminate expression numbers and obtain only operators
        //Remove .0 and set to 4 decimal places maximum
        DecimalFormat df = new DecimalFormat("###.####");
        for (Double number : numbers) {
            operators=operators.replaceFirst(df.format(number), "");
        }
        //If expression starts with -, the first number is negative
        if (substring.charAt(0)=='-') {
            numbers.set(0, numbers.get(0)*-1);
            //The operator is removed
            operators=operators.replaceFirst("-","");
        }
        
        //If there are negative numbers
        if (numbers.size()<=operators.length()) {
            for (int i = 0; i < substring.length()-1; i++) {
                char c = substring.charAt(i);
                //there is a negative number when an operator is followed by a -
                if (c=='+' || c=='-' || c=='*' || c=='/') {
                    if (substring.charAt(i+1)=='-') {
                        String varIndex = String.valueOf(c)+"-";
                        int index = operators.indexOf(varIndex)+1;
                        StringBuilder str = new StringBuilder(operators);
                        //- is removed from the operators and the value is passed to the number in the array numbers
                        operators = str.deleteCharAt(index).toString();
                        numbers.set(index, numbers.get(index)*-1);
                        //System.out.println(operators)
                    }
                }
            }
        }
        //Expression is resolved
        double result = 0;
        String varOp;
        char c;
        for (int i = 0; i < operators.length(); i++) {
            c = operators.charAt(i);
            //First * or / from left to right
            if (c=='*' || c=='/') {
                switch(c){
                    case '*':
                        result = numbers.get(i)*numbers.get(i+1);
                        break;
                    case '/':
                        result = numbers.get(i)/numbers.get(i+1);
                        //If it is divided, maximum 4 decimals are left
                        BigDecimal bigDecimal = new BigDecimal(result).setScale(4, RoundingMode.UP);
                        result = bigDecimal.doubleValue();
                        break;
                }
                //The first factor is replaced by the result and the second one is eliminated
                varOp = "["+String.valueOf(c)+"]";
                operators=operators.replaceFirst(varOp, "");
                numbers.set(i, result);
                numbers.remove(i+1);
                i--;
            }
        }
        for (int i = 0; i < operators.length(); i++) {
            c = operators.charAt(i);
            //Then, - or + from left to right
            if (c=='-' || c=='+') {
                switch(c){
                    case '-':
                        result = numbers.get(i)-numbers.get(i+1);
                        break;
                    case '+':
                        result = numbers.get(i)+numbers.get(i+1);
                        break;
                }
                //The first factor is replaced by the result and the second one is eliminated
                varOp = "["+String.valueOf(c)+"]";
                operators=operators.replaceFirst(varOp, "");
                numbers.set(i, result);
                numbers.remove(i+1);
                i--;
            }
        }
        //The result is set to 4 decimals or the .0 is eliminated
        return String.valueOf(df.format(numbers.get(0)));
        
    }
}
