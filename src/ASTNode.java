package src;

import java.io.Serializable;

/**
 * ASTNode implements a Syntax tree for the expression evaluation
 */
public class ASTNode implements Serializable {
	
	private static final long serialVersionUID = 1L;    // UID for serializable
    private Lexer.Token token;
    private ASTNode left;
    private ASTNode right;

    /** Constructor for ASTNode
     * @param tok The Token for this node
     */
    public ASTNode(Lexer.Token tok) {
        this.token = tok;
        this.left = null;
        this.right = null;
    }

    /** Returns the type of the token in the node
     * @return The tokens TokenType
     */
    public Lexer.TokenType getTokenType() {
        return this.token.tokenType;
    }

    /** Returns the value stored in the token
     * @return The value of the token
     */
    public String getTokenVal() {
        return this.token.tokenVal;
    }


    /** Returns the left node
     * @return The left ASTNode
     */
    public ASTNode getLeft() {
        return left;
    }

    /** Sets the left ASTNode
     * @param left The Node to set to
     */
    public void setLeft(ASTNode left) {
        this.left = left;
    }

    /** Returns the right Node
     * @return The Right node
     */
    public ASTNode getRight() {
        return right;
    }

    /** Sets the right Node
     * @param right The node to set to
     */
    public void setRight(ASTNode right) {
        this.right = right;
    }
}
