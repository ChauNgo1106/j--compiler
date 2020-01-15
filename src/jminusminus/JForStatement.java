// Copyright 2013 Bill Campbell, Swami Iyer and Bahar Akbal-Delibas

package jminusminus;

import static jminusminus.CLConstants.*;
import java.util.ArrayList;

/**
 *
 */

class JForStatement extends JStatement {

   
    private ArrayList<JStatement> forIn;
    private JExpression condition;
    private ArrayList<JStatement> forUp;
   
    private ArrayList<JStatement> forInitialization = new ArrayList<JStatement>(); 
    private ArrayList<JStatement> forUpdating = new ArrayList<JStatement>() ;
    private JStatement body;
  
    /**
     * Constructs an AST node for a while-statement given its line number, the
     * test expression, and the body.
     * 
     * @param line
     *            line in which the while-statement occurs in the source file.
     * @param condition
     *            test expression.
     * @param body
     *            the body.
     */

    public JForStatement(int line, ArrayList<JStatement> forIn, 
    JExpression condition , ArrayList<JStatement> forUp, JStatement sts) {
        super(line);
        this.condition = condition;
        this.body = sts;
        this.forIn = forIn;
        this.forUp = forUp;
       
    }

    /**
     * Analysis involves analyzing the test, checking its type and analyzing the
     * body statement.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */

    public JStatement analyze(Context context) {
       
        
       for (JStatement forin: forIn) {
            forin = (JStatement) forin.analyze(context);
            forInitialization.add(forin);
        }
         
       condition = (JExpression) condition.analyze(context);
       condition.type().mustMatchExpected(line(), Type.BOOLEAN);
       body = (JStatement) body.analyze(context);
      
      for(JStatement forup: forUp) {
            forup = (JStatement) forup.analyze(context);
            forUpdating.add(forup);
        }
        return this;
    }

    /**
     * Generates code for for loop.
     * 
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */

    public void codegen(CLEmitter output) {
        String startLabel = output.createLabel();
        String endLabel = output.createLabel();

      
       for (JStatement forin : forInitialization){
            forin.codegen(output);
        }
       output.addLabel(startLabel);
       condition.codegen(output, endLabel, false);
       //condition.codegen(output);
       body.codegen(output);
        for (JStatement forup : forUpdating){
            forup.codegen(output);
            }
       output.addBranchInstruction(GOTO,startLabel);
       output.addLabel(endLabel);
    }

    /**
     * {@inheritDoc}
     */

    public void writeToStdOut(PrettyPrinter p) {
        p.printf("<JForStatement line=\"%d\">\n", line());
        p.indentRight();
        
       
       if (forIn != null) {
            p.println("<InitialExpression>");
            for (JStatement forin : forIn) {
                p.indentRight();
                forin.writeToStdOut(p);
                p.indentLeft();
            }
            p.println("</InitialExpression>");
        }
        
        
        p.printf("<TestExpression>\n");
        p.indentRight();
        condition.writeToStdOut(p);
        p.indentLeft();
        p.printf("</TestExpression>\n");
        
        if (forUp != null) {
            p.println("<UpdateExpression>");
            for (JStatement forup : forUp) {
                p.indentRight();
                forup.writeToStdOut(p);
                p.indentLeft();
            }
            p.println("</UpdateExpression>");
        }
        p.printf("<Statement>\n");
        p.indentRight();
        body.writeToStdOut(p);
        p.indentLeft();
        p.printf("</Statement>\n");
        
        p.indentLeft();
        p.printf("</JForStatement>\n");
        
        
    }

}
