// Copyright 2013 Bill Campbell, Swami Iyer and Bahar Akbal-Delibas

package jminusminus;

import static jminusminus.CLConstants.*;

/**
 * The AST node for an {@code double} literal.
 */

class JLiteralDouble extends JExpression {

    /** String representation of the double. */
    private String text;

    /**
     * Constructs an AST node for an {@code double} literal given its line number 
     * and string representation.
     * 
     * @param line
     *            line in which the literal occurs in the source file.
     * @param text
     *            string representation of the literal.
     */

    public JLiteralDouble(int line, String text) {
        super(line);
        this.text = text;
    }

    /**
     * Analyzing an double literal is trivial.
     * 
     * @param context
     *            context in which names are resolved (ignored here).
     * @return the analyzed (and possibly rewritten) AST subtree.
     */

    public JExpression analyze(Context context) {
        type = Type.DOUBLE;
        return this;
    }

    /**
     * Generating code for an double literal means generating code to push it onto
     * the stack.
     * 
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */

    public void codegen(CLEmitter output) {
       // double i = Double.parseDouble(text);
        if (text.equals("0.0D")) {
            output.addNoArgInstruction(DCONST_0);
         } else if (text.equals("1.0D")) {
            output.addNoArgInstruction(DCONST_1);
        } else {
                double i = Double.parseDouble(text);
                output.addLDCInstruction(i);
            }
    }
    /**
     * {@inheritDoc}
     */

    public void writeToStdOut(PrettyPrinter p) {
        p.printf("<JLiteralDouble line=\"%d\" type=\"%s\" " + "value=\"%s\"/>\n",
                line(), ((type == null) ? "" : type.toString()), text);
    }

}
