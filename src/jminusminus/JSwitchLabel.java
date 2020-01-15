// Copyright 2013 Bill Campbell, Swami Iyer and Bahar Akbal-Delibas

package jminusminus;

import static jminusminus.CLConstants.*;


class JSwitchLabel extends JExpression {

    private JExpression caseName;
  // private String colon;

    public JSwitchLabel (int line, JExpression caseName) {
        super(line);
        this.caseName = caseName;
    }
    //extract case name
    public JExpression getCaseName(){
        return this.caseName;
    }
    
    //this will be analyzed in JSwitchStatement
      public JExpression analyze(Context context) {
        return this;
    }

    public void codegen(CLEmitter output) {
       
    }

    /**
     * {@inheritDoc}
     */

    public void writeToStdOut(PrettyPrinter p) {
    
        if (caseName!= null) {
            p.printf("<CaseLabel>\n");
            //p.indentRight();
            caseName.writeToStdOut(p);
            p.printf("</CaseLabel>\n");
        } else {
            p.printf("<DefaultLabel/>\n");
        }
    }

}





