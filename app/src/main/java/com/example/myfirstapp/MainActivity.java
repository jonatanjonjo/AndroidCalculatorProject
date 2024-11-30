package com.example.myfirstapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    TextView result;
    StringBuilder input = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        result = findViewById(R.id.textViewResult);
        result.setText("");
    }

    public void numberFunc(View view) {
        Button button = (Button) view;
        String buttontext = button.getText().toString();

        if (buttontext.equals("=")) {
            calculateResult(input);
        } else if (buttontext.equals("D")) {
            if (input.length() > 0) {
                input.deleteCharAt(input.length() - 1);
                result.setText(input.toString());
            }
        } else if (buttontext.equals("AC")) {
            input.setLength(0);
            result.setText("");
        } else {
            input.append(buttontext);
            result.append(button.getText().toString());
        }
    }

    private void calculateResult(StringBuilder input) {
        try {
            String expression = input.toString();
            double resultValue = evaluateExpression(expression);
            result.setText(String.valueOf(resultValue));
            input.setLength(0);
        } catch (Exception e) {
            result.setText("Error");
        }
    }

    private double evaluateExpression(String expression) {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        int i = 0;
        while (i < expression.length()) {
            char c = expression.charAt(i);


            if (c == '-' && (i == 0 || isOperator(expression.charAt(i - 1)))) {

                StringBuilder number = new StringBuilder();
                number.append(c);
                i++;
                while (i < expression.length() &&
                        (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    number.append(expression.charAt(i));
                    i++;
                }
                numbers.push(Double.parseDouble(number.toString()));
                continue;
            }


            if (Character.isDigit(c) || c == '.') {
                StringBuilder number = new StringBuilder();
                while (i < expression.length() &&
                        (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    number.append(expression.charAt(i));
                    i++;
                }
                numbers.push(Double.parseDouble(number.toString()));
                continue;
            }


            if (isOperator(c)) {
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(c)) {
                    double b = numbers.pop();
                    double a = numbers.pop();
                    char op = operators.pop();
                    numbers.push(applyOperation(op, a, b));
                }
                operators.push(c);
            }

            i++;
        }


        while (!operators.isEmpty()) {
            double b = numbers.pop();
            double a = numbers.pop();
            char op = operators.pop();
            numbers.push(applyOperation(op, a, b));
        }

        return numbers.pop();
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '%';
    }
    //puts weight on operators
    private int precedence(char op) {
        if (op == '+' || op == '-') return 1;
        if (op == '*' || op == '/' || op == '%') return 2;
        return 0;
    }

    private double applyOperation(char op, double a, double b) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                return a / b;
            case '%':
                return a % b;
            default:
                throw new IllegalArgumentException("Invalid operator");
        }
    }
}
