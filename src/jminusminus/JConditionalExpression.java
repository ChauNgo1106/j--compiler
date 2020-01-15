// Copyright 2013 Bill Campbell, Swami Iyer and Bahar Akbal-Delibas

package jminusminus;

import static jminusminus.CLConstants.*;


/**
 * The AST node for an conditional expression.
 */
class JConditionalExpression extends JExpression {

   private JExpression condition;
   private JExpression trueBranch;
   private JExpression falseBranch;
   private String operator;
   

/**
     * Constructs an AST node for an conditional statement given its line number, the 
     * test expression, the consequent, and the alternate.
     * 
     * @param line
     *            line in which the conditional statement occurs in the source file.
     * @param condition
     *            test expression.
     * @param thenPart
     *            then clause.
     * @param elsePart
     *            else clause.
     */
    public JConditionalExpression(int line, JExpression condition, String operator, 
                         JExpression trueBranch, JExpression falseBranch) {
        super(line);
        this.condition = condition;
        this.trueBranch = trueBranch;
        this.falseBranch = falseBranch;
        this.operator = operator;
    }
     /**
     * Analyzing the conditional statement means analyzing its components and checking
     * that the condition is a boolean.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */
    public JExpression analyze(Context context) {
        condition = (JExpression)condition.analyze(context);
        condition.type().mustMatchExpected(line(), Type.BOOLEAN);
            trueBranch = (JExpression)trueBranch.analyze(context);
            falseBranch = (JExpression)falseBranch.analyze(context);
            falseBranch.type().mustMatchExpected(line(), trueBranch.type());
            if (trueBranch.type() == Type.STRING) {
                type = Type.STRING;
            } else if (trueBranch.type() == Type.INT) {
                type = Type.INT;
            } if (trueBranch.type() == Type.DOUBLE) {
                type = Type.DOUBLE;
            } if (trueBranch.type() == Type.LONG) {
                type = Type.LONG;
            } 
        //}
        return this;
    }

    public void codegen(CLEmitter output) {
        String falseLabel = output.createLabel();
        String endLabel = output.createLabel();
        
        condition.codegen(output, falseLabel, false);
        trueBranch.codegen(output);
        output.addBranchInstruction(GOTO, endLabel);
        
        output.addLabel(falseLabel);
        falseBranch.codegen(output);
        output.addLabel(endLabel);
       
    }

    /**
     * {@inheritDoc}
     */

    public void writeToStdOut(PrettyPrinter p) {
    
   
       // p.printf("<JConditionalExpression line=\"%d\">\n", line());
       p.printf("<JConditionalExpression line=\"%d\" type=\"%s\" "
                + "operator=\"%s\">\n", line(), ((type == null) ? "" : type
                .toString()), Util.escapeSpecialXMLChars("?"));
        p.indentRight();
        p.printf("<TestExpression>\n");
        p.indentRight();
        condition.writeToStdOut(p);
        p.indentLeft();
        p.printf("</TestExpression>\n");
        p.printf("<TrueClause>\n");
        p.indentRight();
        trueBranch.writeToStdOut(p);
        p.indentLeft();
        p.printf("</TrueClause>\n");
        p.indentLeft();
        p.printf("<FalseClause>\n");
        p.indentRight();
        falseBranch.writeToStdOut(p);
        p.indentLeft();
        p.printf("</FalseClause>\n");
        p.indentLeft();
        p.printf("</JConditionalExpression>\n");
    }

}





