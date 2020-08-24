package src;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Lexer implements an expression lexer for mathematical expression
 */
class Lexer implements Serializable {

	private static final long serialVersionUID = 1L;    // UID for serializable
    private static String[] nonDigitTerminals = { "+", "-", "*", "/", "^", "(", ")" };
    private static String[] variables = {"x", "t"};

    /**
     * Enumerates the possible tokens in the language
     */
    enum TokenType {
        NUM, PLUS, MINUS, MUL, DIV, EXP, LPAREN, RPAREN, FUNC, VAR;
    }

    /**
     * Represents a Token in the language
     */
    class Token implements Serializable {
    	
    	private static final long serialVersionUID = 1L;    // UID for serializable
        TokenType tokenType;
        String tokenVal;

        /** Constructor for the Token
         * @param tokenType The type of the token
         * @param tokenVal The String value of the Token
         */
        public Token(TokenType tokenType, String tokenVal) {
            this.tokenType = tokenType;
            this.tokenVal = tokenVal;
        }

        /** Return a String of the token
         * @return The string version of the token
         */
        public String toString() {
            return "|" + this.tokenType + ": " + this.tokenVal + "|";
        }

        /** Equality for Token
         * @param obj The obj to compare
         * @return True if it is the same token
         */
        @Override
        public boolean equals(Object obj) {
            Token o = (Token) obj;
            return o.tokenVal.equals(this.tokenVal) && o.tokenType.equals(this.tokenType);
        }
    }

    transient Scanner scanner;

    /** Produces a list of Tokens
     * @param arg The string to tokenize
     * @return A list of Tokens
     */
    public List<Token> tokenize(String arg) {
        List<Token> result = new ArrayList<>();
        arg = addWhiteSpaceAroundParensOrCaret(arg);
        this.scanner = new Scanner(arg);
        while (this.scanner.hasNext()) {
            if (this.hasNextNumber()) {
                addToken(this.scanner.next(), TokenType.NUM, result);
            } else {
                String next = this.scanner.next();
                if (isNonDigitTerminal(next)) {
                    TokenType type = matchNonDigitTerminalType(next);
                    addToken(next, type, result);
                } else if (isVariable(next)) {
                    addToken(next, TokenType.VAR, result);
                } else {
                    addToken(next, TokenType.FUNC, result);
                }
            }
        }
        return result;
    }

    /** Checks if the exponent char is part of 'e^', 'sin^-1', 'tan^-1'
     * @param arg the argument for the transformation
     * @param index the index to check at
     * @return true if the exponent char is part of a function name
     */
    private boolean isSpecialString(String arg, int index) {
        if (index != 0) {
            char prev = arg.charAt(index - 1);
            return prev == 'n' || prev == 's' || prev == 'e';
        }
        return false;
    }

    /** Adds a whitespace around All the Parens and exponents
     * @param arg The expression to Change
     * @return A cleaned string
     */
    private String addWhiteSpaceAroundParensOrCaret(String arg) {
        StringBuilder sb = new StringBuilder();
        char curr;
        for (int i = 0; i < arg.length(); i++) {
            curr = arg.charAt(i);
            if (curr == '^') {
                if (isSpecialString(arg, i)) {
                    sb.append(curr);
                    if (arg.charAt(i - 1) == 'e') {
                        sb.append(' ');
                    }
                } else {
                    sb.append(' ');
                    sb.append(curr);
                    sb.append(' ');
                }
            } else if (curr == '(' || curr == ')') {
                sb.append(' ');
                sb.append(curr);
                sb.append(' ');
            } else {
                sb.append(curr);
            }
        }
        return sb.toString();
    }

    /**
     * @param next Checks if the strng should be interpreted as a variable
     * @return True if the string is a variable
     */
    private boolean isVariable(String next) {
        for (String var : variables) {
            if (next.equals(var))
                return true;
        }
        return false;
    }

    /** Matches a non-difit terminal to the correct type
     * @param next The string to match
     * @return The correct TokenType
     */
    private TokenType matchNonDigitTerminalType(String next) {
        switch (next) {
            case "+":
                return TokenType.PLUS;
            case "-":
                return TokenType.MINUS;
            case "*":
                return TokenType.MUL;
            case "/":
                return TokenType.DIV;
            case "^":
                return TokenType.EXP;
            case "(":
                return TokenType.LPAREN;
            case ")":
                return TokenType.RPAREN;
        }
        return null;
    }

    /** Checks if the string is a non-digit terminal
     * @param next The string to check
     * @return True if the string is a non-digit terminal
     */
    private boolean isNonDigitTerminal(String next) {
        for (String terminal: nonDigitTerminals) {
            if (next.equals(terminal))
                return true;
        }
        return false;
    }

    /** Adds a token to the results list
     * @param val The value of the token
     * @param type They type of the Token
     * @param result The result list to append to
     */
    private void addToken(String val, TokenType type, List<Token> result) {
       Token tok = new Token(type, val);
       result.add(tok);
    }

    /** Checks if the next value is a number
     * @return True if the next value is a number
     */
    private boolean hasNextNumber() {
       return this.scanner.hasNextInt() || this.scanner.hasNextFloat();
    }
}
