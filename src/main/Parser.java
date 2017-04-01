package main;

/*
    This class provides a recursive descent parser of Blunt,
    creating a parse tree (which can later be translated to
    VPL code)
*/

public class Parser {

    private Lexer lex;

    public Parser(Lexer lexer) {
        lex = lexer;
    }

    public Node parseProgram() {
        Token token = lex.getToken();

        Node statements = null;

        // optional global list
        if (token.isKind("id") || token.isKind("message") || token.isKind("print") || token.isKind("newline") || token.isKind("input")) {// have statements
            lex.putBack(token);
            statements = parseStatements();
        } else if(token.isKind("eof")){
            return new Node(token);
        } else {
            lex.putBack(token);
        }

        Node root = new Node("program", statements, null, null, null);
        return root;
    }

    private Node parseStatements() {
        System.out.println("-----> parsing statements:");
        Node first = parseStatement();
        Node second = null;

        Token token = lex.getToken();

        if (token.isKind("eof")) {// there are no more
            second = new Node(token);
            return new Node("statements", first, second, null, null);
        } else {// there are more statements
            lex.putBack(token);
            second = parseStatements();
        }

        return new Node("statements", first, second, null, null);
    }

    private Node parseStatement() {
        System.out.println("-----> parsing statement:");
        Token token = lex.getToken();

        if (token.isKind("id")) {
            Token token2 = lex.getToken();
            if(token2.matches("single", "=")){
                Node expr = parseExpression();
                return new Node("statement", new Node(token) , new Node(token2), expr, null);
            }else{
                lex.putBack(token);
                Node expr = parseExpression();
                return new Node("statement", expr , null, null, null);
            }

        } else if (token.isKind("message")){
            Token token1 = lex.getToken();
            errorCheck(token1, "string");
            lex.putBack(token1);
            Node first = parseStatement();
            return new Node("statement", new Node(token), first, null, null );

        } else if (token.isKind("print")){

            Token token1 = lex.getToken();

            if(token1.isKind("string")){
                Node expr = parseExpression();
                return new Node("statement", new Node(token), new Node(token1), expr, null);
            } else if(token1.isKind("id")){
                lex.putBack(token1);
                Node expr = parseExpression();
                return new Node("statement", new Node(token), expr, null, null);
            }

        } else if (token.isKind("newline")) {

            return new Node(token);

        } else if (token.isKind("input")){

            Token token1 = lex.getToken();
            errorCheck(token1, "string");
            Node second = new Node(token1);
            Token token2 = lex.getToken();
            errorCheck(token2, "id");
            Node third = new Node(token2);
            return new Node("statement", new Node(token), second, third, null);

        }

        Node node1 = new Node(token);

        return node1;
    }

    private Node parseExpression() {
        System.out.println("-----> parsing expression:");
        Token token1 = lex.getToken();

        if(token1.isKind("num") || token1.isKind("id")){
            Token token2 = lex.getToken();
            if(!token2.isKind("single")){
                lex.putBack(token2);
                lex.putBack(token1);
                Node first = parseTerm();
                return new Node("expression",first, null, null, null );
            }else{
                if(token2.matches("single", "*") || token2.matches("single", "/") || token2.matches("single", ")")){
                    lex.putBack(token2);
                    lex.putBack(token1);
                    Node first = parseTerm();
                    return new Node("expression",first, null, null, null );
                }else{
                    lex.putBack(token1);
                    Node first = parseTerm();
                    Node second = parseExpression();
                    return new Node("expression",first, new Node(token2), second, null );
                }

            }
        }else if(token1.isKind("single")){
            lex.putBack(token1);
            Node first = parseTerm();
            return new Node("expression",first, null, null, null );
        } else if(token1.isKind("bif")){
            lex.putBack(token1);
            Node first = parseTerm();
            return new Node("expression",first, null, null, null );
        }

        return new Node(token1);
    }

    private Node parseTerm(){
        System.out.println("-----> parsing terms:");
        Token token1 = lex.getToken();

        if(token1.isKind("num") || token1.isKind("id")){
            Token token2 = lex.getToken();
            if(!token2.isKind("single")){
                lex.putBack(token2);
                lex.putBack(token1);
                Node first = parseFactor();
                return new Node("term",first, null, null, null );
            }else if(token2.getDetails().equals(")")){
                lex.putBack(token2);
                lex.putBack(token1);
                Node first = parseFactor();
                return new Node("term",first, null, null, null );
            } else{
                lex.putBack(token1);
                Node first = parseFactor();
                Node second = parseTerm();
                return new Node("term",first, new Node(token2), second, null );
            }
        } else if(token1.isKind("single")){
            lex.putBack(token1);
            Node first = parseFactor();
            return new Node("term",first, null, null, null );
        } else if(token1.isKind("bif")){
            lex.putBack(token1);
            Node first = parseFactor();
            return new Node("term",first, null, null, null );
        }

        return new Node(token1);
    }

    private Node parseFactor(){
        System.out.println("-----> parsing factors:");

        Token token1 = lex.getToken();

        if(token1.isKind("id")){

            Token token2 = lex.getToken();

            if(!token2.matches("single", "(")){
                lex.putBack(token2);
                return new Node(token1);
            }else{
                errorCheck(token2, "single", "(");
                Node first = parseExpression();
                Token token3 = lex.getToken();
                errorCheck(token3, "single", ")");
                return new Node("factor", first, null, null, null);
            }

        } else if(token1.isKind("num")){

            return new Node("factor", new Node(token1), null, null, null);

        } else if(token1.matches("single", "(")){

            Node first = new Node(token1);
            Node second = parseExpression();
            Token token2 = lex.getToken();
            errorCheck(token2, "single", ")");
            Node third = new Node(token2);
            return new Node("factor", first, second, third, null);

        } else if(token1.matches("single", "-")){

            Node first = new Node(token1);
            Node second = parseFactor();
            return new Node("factor", first, second, null, null);

        } else if(token1.isKind("bif")){

            Node first = new Node(token1);
            Node second = parseExpression();
            return new Node("factor", first, second, null, null);

        }

        return new Node(token1);
    }

    // check whether token is correct kind
    private void errorCheck(Token token, String kind) {
        if (!token.isKind(kind)) {
            System.out.println("Error:  expected " + token + " to be of kind " + kind);
            System.exit(1);
        }
    }

    // check whether token is correct kind and details
    private void errorCheck(Token token, String kind, String details) {
        if (!token.isKind(kind) || !token.getDetails().equals(details)) {
            System.out.println("Error:  expected " + token + " to be kind=" + kind + " and details=" + details);
            System.exit(1);
        }
    }


}
