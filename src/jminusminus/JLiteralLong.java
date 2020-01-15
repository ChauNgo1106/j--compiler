// Copyright 2013 Bill Campbell, Swami Iyer and Bahar Akbal-Delibas

package jminusminus;

import static jminusminus.CLConstants.*;


/**
 * The AST node for an {@code long} literal.
 */

class JLiteralLong extends JExpression {

    /** String representation of the long. */
    private String text;

    /**
     * Constructs an AST node for an {@code long} literal given its line number 
     * and string representation.
     * 
     * @param line
     *            line in which the literal occurs in the source file.
     * @param text
     *            string representation of the literal.
     */

    public JLiteralLong(int line, String text) {
        super(line);
        this.text = text;
    }

    /**
     * Analyzing an long literal is trivial.
     * 
     * @param context
     *            context in which names are resolved (ignored here).
     * @return the analyzed (and possibly rewritten) AST subtree.
     */

    public JExpression analyze(Context context) {
        type = Type.LONG;
        return this;
    }

    /**
     * Generating code for an Long literal means generating code to push it onto
     * the stack.
     * 
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */

     public void codegen(CLEmitter output) {
        //cutting off the L or l at the end of the text
        int index = 0;
        String modified = "";
        while (text.charAt(index) != '\0') {
        
            if(text.charAt(index) == 'L' || text.charAt(index) == 'l') {
                break;
            } else 
            index += 1;
        
        }
        //copy trim-off L or l into modified string
        modified += text.substring(0,index);
       
        if (text.equals("0L")) {
            output.addNoArgInstruction(LCONST_0);
        } else if(modified.equals("1L")) {
            output.addNoArgInstruction(LCONST_1);
        } else {
                long i = Long.parseLong(modified);
                output.addLDCInstruction(i);
            }
    }

    /**
     * {@inheritDoc}
     */

    public void writeToStdOut(PrettyPrinter p) {
        p.printf("<JLiteralLong line=\"%d\" type=\"%s\" " + "value=\"%s\"/>\n",
                line(), ((type == null) ? "" : type.toString()), text);
    }

}
