package src;


import java.util.List;

/**
 * Parser Implements a recursive descent lookahead parser for mathematical functions
 * <p>
 * Grammar:
 * <p>
 * The Grammar is LL(1)
 * It is specified by the Productions:
 * <p>
 * expr -> term (('+' | '-') factor)*
 * term -> factor (('*' | '/') factor)*
 * factor -> primary (('^') primary)*
 * primary -> NUM | VAR | FUNC | "(" expr ")"
 */
class Parser {

    List<Lexer.Token> tokens;
    Lexer.Token lookahead;

    /**
     * Constructor for the parser
     *
     * @param toks The tokens from a lexer
     */
    public Parser(List<Lexer.Token> toks) {
        this.tokens = toks;
        this.lookahead = this.tokens.remove(0);
    }

    /**
     * Checks if the lookahead is equal to the given token type
     *
     * @param type The type of the token to check
     * @return
     */
    private boolean lookaheadEqual(Lexer.TokenType type) {
        return this.lookahead.tokenType == type;
    }

    /**
     * Parses the Token stream
     *
     * @return Returns a Syntax tree
     */
    public ASTNode parseTokens() {
        return expr();
    }

    /**
     * Checks if this is the first iteration in the precedence loop
     *
     * @param node The node to check
     * @return True if it is the First iteration
     */
    private static boolean isFirstIteration(ASTNode node) {
        return node != null;
    }

    /**
     * Adds a new Node representing a Add or Subtract node to the tree
     *
     * @param node The to set to the left
     * @param type The token Type
     * @return A new Add/Sub Node
     */
    private ASTNode newAddSubNode(ASTNode node, Lexer.TokenType type) {
        ASTNode opNode = new ASTNode(this.lookahead);
        match(type);
        opNode.setLeft(node);
        opNode.setRight(term());
        return opNode;
    }

    /**
     * Adds a new Node representing a Mult or Div node to the tree
     *
     * @param node The to set to the left
     * @param type The token Type
     * @return A new Mult/Div Node
     */
    private ASTNode newMultDiveNode(ASTNode node, Lexer.TokenType type) {
        ASTNode opNode = new ASTNode(this.lookahead);
        match(type);
        opNode.setLeft(node);
        opNode.setRight(factor());
        return opNode;
    }

    /**
     * Adds a new Node representing a Exp node to the tree
     *
     * @param node The to set to the left
     * @param type The token Type
     * @return A new Exp Node
     */
    private ASTNode newExpNode(ASTNode node, Lexer.TokenType type) {
        ASTNode opNode = new ASTNode(this.lookahead);
        match(type);
        opNode.setLeft(node);
        opNode.setRight(primary());
        return opNode;
    }

    /** Checks if A token Type is a plus or a minus
     * @param type The type of the token
     * @return True if It is a plus or a numus
     */
    private static boolean isPlusOrMinus(Lexer.TokenType type) {
        return type == Lexer.TokenType.PLUS || type == Lexer.TokenType.MINUS;
    }

    /** Checks if a token type is a mult or a div
     * @param type The type of the Token
     * @return True if it is a mult or div
     */
    private static boolean isMultOrDiv(Lexer.TokenType type) {
        return type == Lexer.TokenType.MUL || type == Lexer.TokenType.DIV;
    }

    /** Checks if a Token type is an EXP
     * @param type The token type to check
     * @return True if the Token Type is an exponent
     */
    private boolean isExp(Lexer.TokenType type) {
        return type == Lexer.TokenType.EXP;
    }

    private ASTNode newOpNode(ASTNode node, Lexer.TokenType type) {
        if (isPlusOrMinus(type)) {
            return newAddSubNode(node, type);
        } else if (isMultOrDiv(type)) {
            return newMultDiveNode(node, type);
        } else if (isExp(type)) {
            return newExpNode(node, type);
        } else {
            System.err.printf("Token Type %s is not a valid op token", type.toString());
            System.exit(1);
        }
        return null;
    }


    /** Returns a new operation node
     * @param left The left node
     * @param node The current node
     * @param op The kind of operation
     * @return A new Node
     */
    private ASTNode opNode(ASTNode left, ASTNode node, Lexer.TokenType op) {
        if (isFirstIteration(node)) {
            return newOpNode(node, op);
        } else {
            return newOpNode(left, op);
        }
    }

    /** Iterates over Operators of the same precedence
     * @param left The left node
     * @param firstOp The First operator
     * @param secondOp The second operator
     * @return A new syntax node
     */
    private ASTNode iterateOperators(ASTNode left, Lexer.TokenType firstOp, Lexer.TokenType secondOp) {
        ASTNode node = null;

        while (true) {
            if (lookaheadEqual(firstOp)) {
                node = opNode(left, node, firstOp);
                continue;
            } else if (lookaheadEqual(secondOp)) {
                node = opNode(left, node, secondOp);
                continue;
            }
            break;
        }

        return node;
    }

    /** Production for expr
     * @return An expression tree
     */
    private ASTNode expr() {
        ASTNode left = term();

        ASTNode node = this.iterateOperators(left, Lexer.TokenType.PLUS, Lexer.TokenType.MINUS);

        if (node == null) {
            return left;
        }

        return node;
    }

    /** Production for a term
     * @return A term syntax tree
     */
    private ASTNode term() {
        ASTNode factor = factor();

        ASTNode node = this.iterateOperators(factor, Lexer.TokenType.MUL, Lexer.TokenType.DIV);

        if (node == null) {
            return factor;
        }

        return node;
    }

    /** Production for a factor
     * @return A factor Syntax tree
     */
    private ASTNode factor() {
        ASTNode primary = primary();

        ASTNode node = null;

        while (true) {
            if (lookaheadEqual(Lexer.TokenType.EXP)) {
                node = opNode(primary, node, Lexer.TokenType.EXP);
                continue;
            }
            break;
        }

        if (node == null) {
            return primary;
        }

        return node;
    }

    /** production for primary
     * @return A primary syntax tree
     */
    private ASTNode primary() {
        ASTNode node = null;

        if (lookaheadEqual(Lexer.TokenType.NUM)) {
            node = new ASTNode(this.lookahead);
            match(Lexer.TokenType.NUM);
        } else if (lookaheadEqual(Lexer.TokenType.VAR)) {
            node = new ASTNode(this.lookahead);
            match(Lexer.TokenType.VAR);
        } else if (lookaheadEqual(Lexer.TokenType.FUNC)) {
            node = new ASTNode(this.lookahead);
            match(Lexer.TokenType.FUNC);
            node.setLeft(expr());
        } else {
            match(Lexer.TokenType.LPAREN);
            node = expr();
            match(Lexer.TokenType.RPAREN);
        }

        return node;
    }

    /**
     * Updates the lookahead value
     */
    private void updateLookahead() {
        if (!tokens.isEmpty())
            lookahead = tokens.remove(0);
    }

    /** Matchs the token type and update its value
     * @param type The token type to match
     */
    private void match(Lexer.TokenType type) {
        if (lookaheadEqual(type)) {
            updateLookahead();
        } else {
            System.err.printf("TokenType %s does not match expected TokenType %s\n", lookahead.tokenType.toString(), type.toString());
            System.exit(1);
        }
    }
}
