package Calculator;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class CalcController {
    @FXML
    private Text output;
    @FXML
    private Text calculations;
    @FXML
    private Text m;

    private Model model = new Model();

    private double number1 = 0;
    private double number2 = 0;
    private String operator = "";
    private String calculationsStr="";
    private double storage=0;

    /*
    Три возможных состояния:
    1 - все переменные пустые
    2 - number1 содержит значение и operator содержит значение
    3 - все переменные содержат значения
     */
    private int stateOfVariables = 1;

    /*
    Два возможных состояния:
    1 - начало ввода числа
    2 - продолжение ввода числа
     */
    private int stateForNumber = 1;

    /*
    Два возможных состояния:
    1 - число введенное пользователем
    2 - число выведенное программой
     */
    private int stateForOutputNumber = 1;

    @FXML
    private void numberPressed(Event event) {
        try{
            Double.parseDouble(output.getText());

            if(stateForNumber==1){
                setOutput("0");
                stateForNumber = 2;
            }
            String value;

            if(event.getEventType().getName().equals("KEY_PRESSED")){
                value = ((KeyEvent)event).getText();
            }else{
                value = ((Button)event.getSource()).getText();
            }

            if(!value.equals(".")) {
                if (output.getText().equals("0")) {
                    setOutput("");
                }
            }

            if(!(value.equals(".")&&output.getText().contains("."))) {
                if (output.getText().length() < 16) {
                    setOutput(output.getText() + value);
                }
            }
            stateForOutputNumber = 1;
        }
        catch(NumberFormatException ex){
            return;
        }
    }

    @FXML
    private void operation1Pressed(Event event) {

        stateForNumber=1;
        try {
            double temp = Double.parseDouble(output.getText());
            output.setText(myFormat(temp));

            String value;
            if(event.getEventType().getName().equals("KEY_PRESSED")){
                value = ((KeyEvent)event).getText();
            }else{
                value = ((Button)event.getSource()).getText();
            }

            switch(stateOfVariables){
                case 1:
                    number1 = Double.parseDouble(output.getText());
                    operator = value;
                    if(calculationsStr.contains("sqrt")||calculationsStr.contains("reciproc")){
                        calculationsStr = calculationsStr+" "+operator+" ";
                    }else{
                        calculationsStr = calculationsStr + output.getText()+" "+operator+" ";
                    }

                    setCalculations(calculationsStr);
                    stateOfVariables = 2;
                    stateForOutputNumber = 2;
                    return;
                case 2:
                    if(stateForOutputNumber == 1) {

                        number1 = model.calculate(number1, Double.parseDouble(output.getText()), operator);

                        operator = value;

                        String temp1 = new StringBuilder(calculationsStr).reverse().toString();
                        if(temp1.startsWith(")")){
                            calculationsStr = calculationsStr+" "+operator+" ";
                        }else{
                            if(Double.parseDouble(output.getText())<0){
                                calculationsStr = calculationsStr + "("+output.getText() +")"+ " " + operator + " ";
                            }
                            else {
                                calculationsStr = calculationsStr + output.getText() + " " + operator + " ";
                            }
                        }

                        setCalculations(calculationsStr);
                        setOutput(myFormat(number1));
                    }else{
                        operator = value;
                        calculationsStr = calculationsStr.substring(0, calculationsStr.length()-2);
                        calculationsStr = calculationsStr + operator + " ";
                        setCalculations(calculationsStr);
                    }
                    return;
                case 3:
                    number1 = Double.parseDouble(output.getText());
                    number2 = 0;
                    operator = value;

                    String temp1 = new StringBuilder(calculationsStr).reverse().toString();
                    if(temp1.startsWith(")")){
                        calculationsStr = calculationsStr+" "+operator+" ";
                    }else{
                        calculationsStr = calculationsStr + output.getText() + " " + operator + " ";
                    }

                    setCalculations(calculationsStr);
                    stateOfVariables = 2;
                    stateForOutputNumber = 2;
                    return;

            }
        } catch(NumberFormatException ex){
            return;
        }
    }

    @FXML
    private void sqrtPressed(ActionEvent event) {
        stateForNumber=1;
        try {
            Double.parseDouble(output.getText());

            String value = ((Button)event.getSource()).getText();

            switch(stateOfVariables){
                case 3:
                    calculationsStr = "";
                case 1:
                case 2:
                    try {
                        calculationsStr = calculationsStr+"sqrt(" + output.getText() + ")";
                        setCalculations(calculationsStr);
                        setOutput(myFormat(model.calculate2(Double.parseDouble(output.getText()), value)));

                    }catch(ArithmeticException ex){
                        inCaseOfArException(ex);
                    }
                    return;
            }
        } catch(NumberFormatException ex){
            return;
        }
    }

    @FXML
    private void reciprocPressed(ActionEvent event) {
        stateForNumber = 1;
        try {
            Double.parseDouble(output.getText());

            String value = ((Button) event.getSource()).getText();

            switch (stateOfVariables) {
                case 3:
                    calculationsStr = "";
                case 1:
                case 2:
                    try {
                        calculationsStr = calculationsStr+"reciproc(" + output.getText() + ")";
                        setCalculations(calculationsStr);
                        setOutput(myFormat(model.calculate2(Double.parseDouble(output.getText()), value)));

                    }catch(ArithmeticException ex){
                        inCaseOfArException(ex);
                    }
                    return;
            }
        } catch (NumberFormatException ex) {
            return;
        }
    }

    @FXML
    private void plusMinusPressed(ActionEvent event) {
            if(!output.getText().equals("0")){
                if(output.getText().startsWith("-")){
                    setOutput(output.getText().substring(1, output.getText().length()));
                }else{
                    setOutput("-"+output.getText());
                }
                stateForOutputNumber = 1;
            }
    }

    @FXML
    private void percentPressed(ActionEvent event) {
        stateForNumber=1;
        try {
            Double.parseDouble(output.getText());

            String value = ((Button)event.getSource()).getText();

            switch(stateOfVariables) {
                case 1:
                    setOutput("0");
                    setStateOfVariablesToOne();
                    return;
                case 2:
                    if(!operator.equals("*"))
                        return;

                    setOutput(myFormat(model.calculate(number1, Double.parseDouble(output.getText()), value)));

                    if(calculationsStr.endsWith("* ")){
                        calculationsStr = calculationsStr + output.getText();
                    }
                    else{
                        calculationsStr = calculationsStr.substring(0, calculationsStr.indexOf("*")+2);
                        calculationsStr = calculationsStr + output.getText();
                    }
                    setCalculations(calculationsStr);
                    return;
                case 3:
                    return;
            }
        } catch(NumberFormatException ex){
            return;
        }
    }

    @FXML
    private void equalPressed() {
        stateForNumber=1;
        try {
            Double.parseDouble(output.getText());

            switch(stateOfVariables){
                case 1:
                    try {
                        if (Double.parseDouble(output.getText()) == 0) {
                            setOutput("0");
                        }
                    }catch (NumberFormatException ex) {
                        return;
                    }
                    calculationsStr="";
                    setCalculations(calculationsStr);
                    return;
                case 2:
                    try {
                        number2 = Double.parseDouble(output.getText());
                        number1 = model.calculate(number1, number2, operator);
                        calculationsStr = " ";
                        setCalculations(calculationsStr);
                        setOutput(myFormat(number1));
                        stateOfVariables = 3;
                    }catch(ArithmeticException ex){
                        inCaseOfArException(ex);
                    }
                    return;
                case 3:
                    try {
                        number1 = Double.parseDouble(output.getText());
                        number1 = model.calculate(number1, number2, operator);
                        setOutput(myFormat(number1));
                    }catch(ArithmeticException ex){
                        inCaseOfArException(ex);
                    }
                    return;
            }
        }catch(NumberFormatException ex){
            return;
        }

    }

    @FXML
    private void keyPressed(KeyEvent event) {
        switch (event.getCode()){
            case EQUALS:
                if(event.isShiftDown()){
                    KeyEvent evt = new KeyEvent(KeyEvent.KEY_PRESSED, "+", "+", KeyCode.PLUS, true, false, false, false);
                    this.operation1Pressed(evt);
                    return;
                }else{
                    this.equalPressed();
                    return;
                }
            case ENTER:
                this.equalPressed();
                return;
            case NUMPAD1:
            case NUMPAD2:
            case NUMPAD3:
            case NUMPAD4:
            case NUMPAD5:
            case NUMPAD6:
            case NUMPAD7:
            case NUMPAD8:
            case NUMPAD9:
            case NUMPAD0:
            case DIGIT1:
            case DIGIT2:
            case DIGIT3:
            case DIGIT4:
            case DIGIT5:
            case DIGIT6:
            case DIGIT7:
            case DIGIT8:
            case DIGIT9:
            case DIGIT0:
            case PERIOD:
                this.numberPressed(event);
                return;
            case MINUS:
                if(!event.isShiftDown()){
                    this.operation1Pressed(event);
                    return;
                }
                else{
                    return;
                }
            case MULTIPLY:
            case DIVIDE:
                this.operation1Pressed(event);
                return;
            case BACK_SPACE:
                this.backspacePressed();
                return;

        }
    }

    @FXML
    private void backspacePressed() {
        try {
            Double.parseDouble(output.getText());

            if(stateForNumber==2) {
                    String temp = output.getText();
                    if(temp.length()==1 || (temp.length()==2 && temp.startsWith("-"))){
                        setOutput("0");
                    }else {
                        setOutput(temp.substring(0, temp.length() - 1));
                    }
            }
        } catch(NumberFormatException ex){
            return;
        }
    }

    @FXML
    private void clearLastInputPressed(ActionEvent event) {
        setOutput("0");
    }

    @FXML
    private void clearCalculationsPressed(ActionEvent event) {
        setOutput("0");
        setStateOfVariablesToOne();
    }

    @FXML
    private void storagePressed(ActionEvent event) {
        try {
            Double.parseDouble(output.getText());

            String value = ((Button) event.getSource()).getText();
            switch (value){
                case "MC":
                    storage=0;
                    m.setText("");
                    return;
                case "MR":
                    setOutput(myFormat(storage));
                    stateForNumber=1;
                    return;
                case "MS":
                    storage=Double.parseDouble(output.getText());
                    stateForNumber = 1;
                    m.setText("M");
                    return;
                case "M+":
                    storage += Double.parseDouble(output.getText());
                    if(m.getText().equals("")){
                        m.setText("M");
                    }
                    stateForNumber=1;
                    return;
                case "M-":
                    storage-=Double.parseDouble(output.getText());
                    if(m.getText().equals("")){
                        m.setText("M");
                    }
                    stateForNumber=1;
                    return;
            }
        } catch(NumberFormatException ex){
            return;
        }
    }

    private String myFormat(double number){
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.GERMAN);
        otherSymbols.setDecimalSeparator('.');
        DecimalFormat format;

        if(number<-9E15||(number>-1E-15&&number<1E-15&&number!=0)||number>9E15){
            BigDecimal bd = BigDecimal.valueOf(number);
            bd = bd.round(MathContext.DECIMAL32);
            format = new DecimalFormat("0.###############E0", otherSymbols);
            return format.format(bd);
        }else{
            format = new DecimalFormat("0.###############", otherSymbols);
            return format.format(number);
        }
    }

    private void setCalculations(String calcStr){
        if(calcStr.length()>24){
            int index = calcStr.length() - 23;
            String temp = calcStr.substring(index, calcStr.length());
            temp = "« "+temp;
            calculations.setText(temp);
        }
        else
            calculations.setText(calcStr);
    }

    private void setStateOfVariablesToOne(){
        calculationsStr="";
        setCalculations(calculationsStr);
        number1=0;
        number2=0;
        operator="";
        stateForNumber=1;
        stateOfVariables=1;
    }

    private void setOutput(String str){
            if (str.length() > 10) {
                output.setFont(new Font("Consolas", 16));
            } else
                output.setFont(new Font("Consolas", 20));
            output.setText(str);
    }

    private void inCaseOfArException(ArithmeticException ex){
        setStateOfVariablesToOne();
        output.setFont(new Font(11));
        output.setText(ex.getLocalizedMessage());
    }
}