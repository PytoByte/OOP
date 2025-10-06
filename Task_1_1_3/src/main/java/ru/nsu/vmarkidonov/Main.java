package ru.nsu.vmarkidonov;

import java.util.Scanner;
import ru.nsu.vmarkidonov.exprparts.Number;
import ru.nsu.vmarkidonov.parser.Parser;
import ru.nsu.vmarkidonov.parser.ParserException;


/**
 * User interface.
 */
public class Main {
    /**
     * Run user interface.
     *
     * @param args args from terminal
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Expression prevExpr = null;
        while (true) {
            System.out.print("Enter expression (\"-\" to exit; \"+\" to choose prev res expr): ");
            String exprString = in.next();
            Expression expr;
            if (exprString.equals("-")) {
                System.out.println("exit");
                break;
            } else if (exprString.equals("+")) {
                if (prevExpr == null) {
                    System.out.println("Previous expression empty");
                    continue;
                } else {
                    expr = prevExpr;
                }
            } else {
                try {
                    expr = Parser.parseExpression(exprString);
                } catch (ParserException exception) {
                    System.out.println(exception.getMessage());
                    continue;
                }
            }

            try {
                System.out.print("Choose action (0=eval, 1=derivative, 2=simplify, 3=cancel): ");
                int action = in.nextInt();
                if (action == 0) {
                    System.out.print("Assignment variable (example: x=1;y=2) (\"-\" for empty): ");
                    String varAssignment = in.next();
                    if (varAssignment.equals("-")) {
                        prevExpr = new Number(expr.eval(""));
                        System.out.println(prevExpr);
                    } else {
                        prevExpr = new Number(expr.eval(varAssignment));
                        System.out.println(prevExpr);
                    }
                } else if (action == 1) {
                    System.out.print("Enter variable (example: x) (\"-\" for cancel): ");
                    String var = in.next();
                    if (var.equals("-")) {
                        System.out.println("Cancel");
                    } else {
                        prevExpr = expr.derivative(var);
                        System.out.println(prevExpr);
                    }
                } else if (action == 2) {
                    prevExpr = expr.simplify();
                    System.out.println(prevExpr);
                } else {
                    System.out.println("Cancel");
                }
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        }
    }
}