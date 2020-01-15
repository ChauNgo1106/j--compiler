// Copyright 2013 Bill Campbell, Swami Iyer and Bahar Akbal-Delibas

package jminusminus;

import static jminusminus.CLConstants.*;
import java.util.ArrayList;
import java.util.*;

/**
 * The AST node for a Switch-statement.
 */

class JSwitchStatement extends JStatement {

   //Block statements
    private ArrayList<JStatement> statement;
    //test expression
    private JExpression exp;
    
    //analyzed blocks statement group
    private ArrayList <JStatement> analyzedGroup  = new ArrayList<JStatement>();
    /**
     * Constructs an AST node for a switch statement given its line number, the
     * test expression, and the body.
     * 
     * @param line
     *            line in which the switch statement occurs in the source file.
     * @param condition
     *            test expression.
     * @param body
     *            the body.
     */

    public JSwitchStatement(int line, JExpression exp,
                             ArrayList<JStatement> sts) {
        super(line);
        this.exp = exp;
        this.statement = sts;
    }

    /**
     * Analysis involves analyzing the test, checking its type and analyzing the
     * body statement. of switch
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */

    public JStatement analyze(Context context) {
        exp = (JExpression)exp.analyze(context);
        //analyze block by block. For each block, analyze labels by labels and its body
        for (JStatement sts : statement) {
            ArrayList <JStatement> analyzedBlocks  = new ArrayList<JStatement>();
            ArrayList <JExpression> analyzedLabels = new ArrayList<JExpression>();
            
            ArrayList<JExpression> labels = ((JSwitchBlockStatementGroup) sts).getSwitchLabel();
            ArrayList<JStatement> blocks = ((JSwitchBlockStatementGroup) sts).getBlockPart();
            for (JExpression lb : labels) {
                if (lb != null) { //it is not a default label
                    lb = (JExpression)lb.analyze(context);
                    analyzedLabels.add(lb);
                }
            }
            for (JStatement blk : blocks) {
                blk = (JStatement) blk.analyze(context);
                analyzedBlocks.add(blk);
            }
        JSwitchBlockStatementGroup temp = new JSwitchBlockStatementGroup(line , analyzedLabels, analyzedBlocks);
        analyzedGroup.add((JStatement) temp); 
        }
        return this;
    }

    /**
     * Generates code switch
     * 
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */

    public void codegen(CLEmitter output) {
    
        //codegen for the switch Statement
        exp.codegen(output);
        
        String defaultLabel = output.createLabel();
        String endLabel = output.createLabel();
        
        TreeMap<Integer , String> matchLabelPairs = new TreeMap<Integer, String>();
        ArrayList<Integer> hold = new ArrayList<Integer>();
        ArrayList<String> caseLabels = new ArrayList<String>();
        String caseLabel;
        long hi = 10000;
        long lo = -1;
        long nlabels = 0;
        
       // for (JStatement sts : analyzedGroup) {
       for (JStatement sts : statement) {
            ArrayList<JExpression> labels = ((JSwitchBlockStatementGroup) sts).getSwitchLabel();
            ArrayList<JStatement> blocks = ((JSwitchBlockStatementGroup) sts).getBlockPart();
            for(JExpression lb : labels) {
                if (lb != null) {
                   //lb = (JSwitchLabel)lb.getCaseName();
                    int caseValue =  ((JLiteralInt)lb).getText();
                    hold.add(caseValue);
                } else
                    output.addLabel(defaultLabel);
            }
        }
        //find the total real labels
        nlabels = (long)hold.size();
        //find lowest and highest case value
        for (int caseValue : hold) {
            if ((long)caseValue >= hi )
                hi = (long)caseValue;
            if ((long)caseValue <= lo)
                lo = (long)caseValue;
            //create each label
            caseLabel = output.createLabel();
            caseLabels.add(caseLabel);
            matchLabelPairs.put(caseValue, caseLabel);
        }

        //determine whether we use TABLESWITCH OR LOOKUPSWITCH
        long table_space_cost = 4 + ((long) hi - lo + 1);
        long table_time_cost = 3;
        long lookup_space_cost = 3 + 2 * (long) nlabels;
        long lookup_time_cost = nlabels;
        int opcode = (nlabels > 0 && table_space_cost + 3 * table_time_cost <= 
                    lookup_space_cost + 3 * lookup_time_cost) ? TABLESWITCH : LOOKUPSWITCH;
            
        if (opcode == TABLESWITCH) 
            output.addTABLESWITCHInstruction (defaultLabel, (int)lo, (int)hi, caseLabels);    
        else 
            output.addLOOKUPSWITCHInstruction (defaultLabel, (int)nlabels, matchLabelPairs);
               
        //this index will keep track which case label you are working with. 
        int index = 0;
       // for (JStatement sts : analyzedGroup) {
       for (JStatement sts : statement) {
            ArrayList<JExpression> labels = ((JSwitchBlockStatementGroup) sts).getSwitchLabel();
            ArrayList<JStatement> blocks = ((JSwitchBlockStatementGroup) sts).getBlockPart();
            for (JExpression lb : labels) {
                if (lb != null) {
                    output.addLabel (caseLabels.get(index));
                    index += 1;
                } else
                    output.addLabel(defaultLabel);
            }
           
            for (JStatement block : blocks) {
                //jumb to end label if we encounter break statement. 
                if (block instanceof JBreakStatement){
                    output.addBranchInstruction(GOTO, endLabel);
                } else
                    block.codegen(output);
            }
        }
        
        output.addLabel(endLabel);

}

    /**
     * {@inheritDoc}
     */

    public void writeToStdOut(PrettyPrinter p) {
        p.printf("<JSwitchStatement line=\"%d\">\n", line());
        p.indentRight();
        
        p.printf("<TestExpression>\n");
        p.indentRight();
        exp.writeToStdOut(p);
        p.indentLeft();
        p.printf("</TestExpression>\n");

        
       if (statement != null) {
        // p.printf("<Body>\n");
            for (JStatement sts : statement) {
                p.indentRight();
                sts.writeToStdOut(p);
                p.indentLeft();
            }
        //p.printf("</Body>\n");
        }
      
        p.indentLeft();
        p.printf("</JSwitchStatement>\n");
        
        
    }

}
