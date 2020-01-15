// Copyright 2013 Bill Campbell, Swami Iyer and Bahar Akbal-Delibas

package jminusminus;
import static jminusminus.CLConstants.*;

/**
 * The AST node for an expression that appears as a statement. Only the
 * expressions that have a <em>side-effect</em> are valid statement expressions.
 */

class JThrowStatement extends JStatement {

    /** The expression. */
    JExpression expr;

    /**
     * Constructs an AST node for a statement expression given its line number,
     * and expression.
     * 
     * @param line
     *            line in which the expression occurs in the source file.
     * @param expr
     *            the expression.
     */

    public JThrowStatement(int line, JExpression expr) {
        super(line);
        this.expr = expr;
    }

    /**
     * Analysis involves analyzing the encapsulated expression if indeed it is a
     * statement expression, to be specific, one with a <em>side effect</em>.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */

    public JStatement analyze(Context context) {
        if (expr.isStatementExpression) {
            expr = expr.analyze(context);
        }
        return this;
    }

    /**
     * Generating code for the statement expression involves simply generating
     * code for the encapsulated expression.
     * 
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */

    public void codegen(CLEmitter output) {
        expr.codegen(output);
        output.addNoArgInstruction(ATHROW);
    }

    /**
     * {@inheritDoc}
     */

    public void writeToStdOut(PrettyPrinter p) {
        p.printf("<JThrowStatement line=\"%d\">\n", line());
        p.indentRight();
        expr.writeToStdOut(p);
        p.indentLeft();
        p.printf("</JThrowStatement>\n");
    }

}
