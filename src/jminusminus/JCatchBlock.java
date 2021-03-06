// Copyright 2013 Bill Campbell, Swami Iyer and Bahar Akbal-Delibas

package jminusminus;

import java.util.ArrayList;

/**
 * The AST node for a block, which delimits a nested level of scope.
 *
 * @see LocalContext
 */

class JCatchBlock extends JStatement {

    private JFormalParameter param;
    private JBlock block;
    /**
     * Constructs an AST node for a block given its line number, and the list of
     * statements forming the block body.
     * 
     * @param line
     *            line in which the block occurs in the source file.
     * @param statements
     *            list of statements forming the block body.
     */

    public JCatchBlock (int line, JFormalParameter param, JBlock block) {
        super(line);
        this.param = param;
        this.block = block;
    }

   

    public JBlock block() {
        return this.block;
    }
    
    public JFormalParameter param() {
        return this.param;
    }
    /**
     * Analyzing a block consists of creating a new nested context for that
     * block and analyzing each of its statements within that context.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */

    public JCatchBlock analyze(Context context) {
        return this;
    }

    /**
     * Generating code for a block consists of generating code for each of its
     * statements.
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
    }
}
