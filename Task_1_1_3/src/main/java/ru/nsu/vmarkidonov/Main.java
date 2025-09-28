package ru.nsu.vmarkidonov;

import ru.nsu.vmarkidonov.parser.Parser;
import ru.nsu.vmarkidonov.parser.ParserException;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String prevExpr = null;
        while (true) {
            System.out.print("Enter expression (enter \"-\" to exit or  \"+\" to choose previous result expression): ");
            String exprString = in.next();
            if (exprString.equals("-")) {
                System.out.println("exit");
                break;
            } else if (exprString.equals("+")) {
                if (prevExpr == null) {
                    System.out.println("Previous expression empty");
                    continue;
                } else {
                    exprString = prevExpr;
                }
            }
            Expression expr;
            try {
                expr = Parser.parseExpression(exprString);
            } catch (ParserException exception) {
                System.out.println(exception.getMessage());
                continue;
            }

            try {
                System.out.print("Choose action (0=eval, 1=derivate, 2=simplify): ");
                int action = in.nextInt();
                if (action == 0) {
                    System.out.print("Assignment variable (example: x=1;y=2) (enter \"-\" for empty): ");
                    String varAssignment = in.next();
                    if (varAssignment.equals("-")) {
                        prevExpr = String.valueOf(expr.eval(""));
                        System.out.println(prevExpr);
                    } else {
                        prevExpr = String.valueOf(expr.eval(varAssignment));
                        System.out.println(prevExpr);
                    }
                } else if (action == 1) {
                    System.out.print("Enter variable (example: x) (enter \"-\" for empty): ");
                    String var = in.next();
                    if (var.equals("-")) {
                        prevExpr = expr.derivative("").toString();
                        System.out.println(prevExpr);
                    } else {
                        prevExpr = expr.derivative(var).toString();
                        System.out.println(prevExpr);
                    }
                } else if (action == 2) {
                    prevExpr = expr.simplify().toString();
                    System.out.println(prevExpr);
                }
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        }
    }
}