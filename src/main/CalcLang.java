package main;

import java.util.Scanner;

/**
 * Created by Kristoffer.West on 3/30/2017.
 */
public class CalcLang {

    public static void main(String[] args) throws Exception {
        System.out.print("Enter file name: ");
        Scanner keys = new Scanner(System.in);
        String name = keys.nextLine();
        Lexer lex = new Lexer(name);
        Token token;

        Parser parser = new Parser(lex);

        Node root = parser.parseProgram();

        do {
            token = lex.getNext();
            System.out.println(token.toString());
        } while (!token.getKind().equals("eof"));

        //TreeViewer viewer = new TreeViewer("Parse Tree", 0, 0, 1200, 800, root);

        root.execute();

    }

}
