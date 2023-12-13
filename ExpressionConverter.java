import java.util.Scanner;
import java.util.Stack;

public class ExpressionConverter {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Expression Converter!");

        boolean isValidInput;
        String infixExpression;

        do {
            System.out.println("Infix:");
            infixExpression = scanner.nextLine();
            isValidInput = isValidInput(infixExpression);

            if (!isValidInput) {
                System.out.println("Oops! Invalid input. Make sure to use only numbers and valid operators. Try again.");
            }

        } while (!isValidInput);

        String postfixExpression = infixToPostfixConverter(infixExpression);
        System.out.println("Postfix: " + postfixExpression);

        evaluatePostfixExpression(postfixExpression);

        scanner.close();
    }

    private static boolean isValidInput(String infixExpression) {
        for (char c : infixExpression.toCharArray()) {
            if (!Character.isDigit(c) && !isOperator(c) && !isParenthesis(c)) {
                return false;
            }
        }

        Stack<Character> stack = new Stack<>();
        char prevChar = '\0';

        for (char c : infixExpression.toCharArray()) {
            if (isOperator(c)) {
                if (isOperator(prevChar)) {
                    return false; // Consecutive operators detected
                }
            }

            if (isParenthesis(c)) {
                if (c == '(') {
                    stack.push(c);
                } else {
                    if (stack.isEmpty() || stack.pop() != '(') {
                        return false;
                    }
                }
            }

            prevChar = c;
        }

        return stack.isEmpty();
    }

    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private static boolean isParenthesis(char c) {
        return c == '(' || c == ')';
    }

    private static int getPrecedence(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return 0;
        }
    }

    private static String infixToPostfixConverter(String infixExpression) {
        StringBuilder postfixExpression = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        for (char c : infixExpression.toCharArray()) {
            if (Character.isDigit(c)) {
                postfixExpression.append(c);
            } else if (isOperator(c)) {
                while (!stack.isEmpty() && getPrecedence(c) <= getPrecedence(stack.peek())) {
                    postfixExpression.append(stack.pop());
                }
                stack.push(c);
            } else if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    postfixExpression.append(stack.pop());
                }
                stack.pop();
            }
        }

        while (!stack.isEmpty()) {
            postfixExpression.append(stack.pop());
        }

        return postfixExpression.toString();
    }

    private static void evaluatePostfixExpression(String postfixExpression) {
        Stack<Integer> stack = new Stack<>();

        System.out.println("\nEvaluating the postfix expression step by step:");

        for (char c : postfixExpression.toCharArray()) {
            if (Character.isDigit(c)) {
                stack.push(Character.getNumericValue(c));
            } else if (isOperator(c)) {
                int operand2 = stack.pop();
                int operand1 = stack.pop();
                int result = performOperation(operand1, operand2, c);
                stack.push(result);

                System.out.println("Stack: " + stack + " \t Operation: " + operand1 + " " + c + " " + operand2);
            }
        }

        int finalResult = stack.pop();
        System.out.println("\nFinal result: " + finalResult);
    }

    private static int performOperation(int operand1, int operand2, char operator) {
        switch (operator) {
            case '+':
                return operand1 + operand2;
            case '-':
                return operand1 - operand2;
            case '*':
                return operand1 * operand2;
            case '/':
                if (operand2 != 0) {
                    return operand1 / operand2;
                } else {
                    throw new ArithmeticException("Error: Division by zero.");
                }
            default:
                throw new IllegalArgumentException("Error: Invalid operator - " + operator);
        }
    }
}
