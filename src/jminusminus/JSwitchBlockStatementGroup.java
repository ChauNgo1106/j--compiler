// Copyright 2013 Bill Campbell, Swami Iyer and Bahar Akbal-Delibas

package jminusminus;

import static jminusminus.CLConstants.*;
import java.util.ArrayList;

/**
 * The AST node for an if-statement.
 */

class JSwitchBlockStatementGroup extends JStatement {

  
    
    private ArrayList<JExpression> switchLabel;

   
    private ArrayList<JStatement> blockPart;

    /**
     * Constructs an AST node for an if-statement given its line number, the 
     * test expression, the consequent, and the alternate.
     * 
     * @param line
     *            line in which the if-statement occurs in the source file.
     * @param condition
     *            test expression.
     * @param thenPart
     *            then clause.
     * @param elsePart
     *            else clause.
     */

    public JSwitchBlockStatementGroup(int line, ArrayList<JExpression> SL, 
                        ArrayList<JStatement> BP) {
        super(line);
        this.switchLabel = SL;
        this.blockPart = BP;
        
    }
    public ArrayList<JExpression> getSwitchLabel(){
        return this.switchLabel;
    }
    
    public ArrayList<JStatement> getBlockPart(){
        return this.blockPart;
    } 
    /**
     * Analyzing the if-statement means analyzing its components and checking
     * that the test is a boolean.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */

    public JStatement analyze(Context context) { 
        return this;
    }

    /**
     * Code generation for an if-statement. We generate code to branch over the
     * consequent if !test; the consequent is followed by an unconditonal branch
     * over (any) alternate.
     * 
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */
     
   
    public void codegen(CLEmitter output) {
    }

    /**
     * {@inheritDoc}
     */

    public void writeToStdOut(PrettyPrinter p) {
        p.printf("<SwitchBlockStatementGroup>\n");
        p.indentRight();
       if (switchLabel != null) {
            for (JExpression switchlabel : switchLabel) {
                p.indentRight();
                switchlabel.writeToStdOut(p);
                p.indentLeft();
            }
           
        }
        
        if (blockPart != null) {
            p.printf("<Body>\n");
            for (JStatement blockpart : blockPart) {
                p.indentRight();
                blockpart.writeToStdOut(p);
                p.indentLeft();
            }
            p.printf("</Body>\n");
           
        }
        
        p.indentLeft();
        p.printf("</SwitchBlockStatementGroup>\n");
    }

}
