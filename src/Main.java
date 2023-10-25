import java.util.Scanner;
import java.util.Stack;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean keepCalculating = true;

        while (keepCalculating) {
            System.out.println("Enter the Expression you want to evaluate");
            String exp = sc.next();
            double ans = 0.0;
            try {
                ans = evaluate(exp);
            } catch (UnsupportedOperationException e) {
                System.out.println("Divide by zero is not possible " + e.getMessage());
                continue;
            }

            System.out.println("Result: " + ans);
            System.out.print("Do you want to continue (yes/no)? ");
            String choice = sc.next();
            if (choice.equals("no") || choice.equals("No")) {
                keepCalculating = false;
            }
        }
    }

    public static double evaluate(String expression) {
        char[] tokens = expression.toCharArray();
        Stack<Double> values = new Stack<>();
        Stack<Character> ops = new Stack<>();

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] == ' ')
                continue;
            if (tokens[i] >= '0' && tokens[i] <= '9') {
                // Read and store numeric values
                StringBuilder sbuf = new StringBuilder();
                while (i < tokens.length && tokens[i] >= '0' && tokens[i] <= '9') {
                    sbuf.append(tokens[i++]);
                }
                values.push(Double.parseDouble(sbuf.toString()));
                i--;
            } else if (tokens[i] == '(') {
                ops.push(tokens[i]);
            } else if (tokens[i] == ')') {
                // Calculate values inside parentheses
                while (ops.peek() != '(') {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                ops.pop();  // Pop the '('
            } else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/') {
                // Handle operators and their precedence
                while (!ops.empty() && hasPrecedence(tokens[i], ops.peek())) {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                ops.push(tokens[i]);
            }
        }
        while (!ops.empty()) {
            // Calculate remaining operations
            values.push(applyOp(ops.pop(), values.pop(), values.pop()));
        }
        return values.pop();  // The final result
    }

    public static boolean hasPrecedence(char op1, char op2) {
        // Check operator precedence
        if (op2 == '(' || op2 == ')')
            return false;
        return (op1 != '*' && op1 != '/') || (op2 != '+' && op2 != '-');
    }

    public static double applyOp(char op, double b, double a) {
        // Apply the operator to two operands
        switch (op) {
            case '+' -> {
                return a + b;
            }
            case '-' -> {
                return a - b;
            }
            case '*' -> {
                return a * b;
            }
            case '/' -> {
                if (b == 0)
                    throw new UnsupportedOperationException("Cannot divide by zero");
                return a / b;
            }
        }
        return 0;
    }
}
