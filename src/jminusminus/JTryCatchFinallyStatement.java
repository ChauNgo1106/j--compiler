// Copyright 2013 Bill Campbell, Swami Iyer and Bahar Akbal-Delibas

package jminusminus;
import java.util.ArrayList;
import static jminusminus.CLConstants.*;

/**
 * The AST node for an try catch block */

class JTryCatchFinallyStatement extends JStatement {

 
    private JBlock try_block;
    private ArrayList<JFormalParameter> formalP;
    private ArrayList<JBlock> catch_block;
    private ArrayList<JCatchBlock> cblock;
    private JBlock finally_block;
    private int count;
    /**
     * Constructs an AST node
     * 
     * @param line
     *            line in which the expression occurs in the source file.
     * @param expr
     *            the expression.
     */

    public JTryCatchFinallyStatement(int line, JBlock try_block, 
            ArrayList<JFormalParameter> formalP, ArrayList<JBlock> catch_block, 
            JBlock finally_block, int count) {
        super(line);
        this.try_block = try_block;
        this.formalP = formalP;
        this.catch_block = catch_block;
        this.finally_block = finally_block;
        this.count = count;
    }
    public JTryCatchFinallyStatement(int line, JBlock try_block, 
                    ArrayList<JCatchBlock> catch_block, JBlock finally_block, 
                        int count) {
            
        super(line);
        this.try_block = try_block;
        this.cblock = catch_block;
        this.finally_block = finally_block;
        this.count = count;
    }
    /**
     * Analysis involves
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */

    public JStatement analyze(Context context) {
       try_block = (JBlock) try_block.analyze(context);
       for (JFormalParameter p : formalP) {
            p = (JFormalParameter)p.analyze(context);
            //p = p.resolve(context);
        }
       for (JBlock cb : catch_block)
            cb = (JBlock)cb.analyze(context); 
        finally_block = (JBlock) finally_block.analyze(context);    
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
        String startLabel = output.createLabel();
        String endLabel = output.createLabel();
        String handlerLabel = output.createLabel();
        String catchType = formalP.get(0).type().toString();
        ArrayList<JStatement> sts = new ArrayList<JStatement> ();
        
        output.addLabel(startLabel);
        //try block
        
        //try_block = ((JStatement)try_block).codegen(output);
        try_block.codegen(output);
        for (JFormalParameter p : formalP)
           // p = ((JAST)p).codegen(output);
           p.codegen(output);
           
        //catch block (including formal parmaters)
        output.addLabel(handlerLabel);
        for (JBlock cb : catch_block) {
            //cb = ((JStatement)cb).codegen(output);
            cb.codegen(output);
        } 
        //finally block 
        //finally_block = ((JStatement)finally_block).codegen(output);
        finally_block.codegen(output);
        output.addLabel(endLabel);
       
       output.addExceptionHandler(startLabel, endLabel, handlerLabel,catchType); 
    }

    /**
     * {@inheritDoc}
     */

    public void writeToStdOut(PrettyPrinter p) {
        p.printf("<JTryCatchFinallyStatement line=\"%d\">\n", line());
       
            p.println("<TryBlock>");
            p.indentRight();
            try_block.writeToStdOut(p);
            p.indentLeft();
            p.println("</TryBlock>");
        
            p.indentRight();
            int index = 0;
        if (catch_block != null ) {
             do{ 
                p.println("<CatchBlock>");
                formalP.get(index).writeToStdOut(p);
                p.indentLeft();
    
                p.indentRight();
                catch_block.get(index).writeToStdOut(p);
                p.indentLeft();
                p.println("</CatchBlock>");
                ++index;
                --count;
                
            } while (index <= count );
        } 
        if (finally_block != null) {
            p.println("<FinallyBlock>");
            p.indentRight();
            finally_block.writeToStdOut(p);
            p.indentLeft();
            p.println("</FinallyBlock>");
         }  
        p.printf("</JTryCatchFinallyStatement>\n");
 }

}
