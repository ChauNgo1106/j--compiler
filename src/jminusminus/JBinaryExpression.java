// Copyright 2013 Bill Campbell, Swami Iyer and Bahar Akbal-Delibas

package jminusminus;

import static jminusminus.CLConstants.*;

/**
 * This abstract base class is the AST node for a binary expression. 
 * A binary expression has an operator and two operands: a lhs and a rhs.
 */

abstract class JBinaryExpression extends JExpression {

    /** The binary operator. */
    protected String operator;

    /** The lhs operand. */
    protected JExpression lhs;

    /** The rhs operand. */
    protected JExpression rhs;

    /**
     * Constructs an AST node for a binary expression given its line number, the
     * binary operator, and lhs and rhs operands.
     * 
     * @param line
     *            line in which the binary expression occurs in the source file.
     * @param operator
     *            the binary operator.
     * @param lhs
     *            the lhs operand.
     * @param rhs
     *            the rhs operand.
     */

    protected JBinaryExpression(int line, String operator, JExpression lhs,
            JExpression rhs) {
        super(line);
        this.operator = operator;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    /**
     * {@inheritDoc}
     */

    public void writeToStdOut(PrettyPrinter p) {
        p.printf("<JBinaryExpression line=\"%d\" type=\"%s\" "
                + "operator=\"%s\">\n", line(), ((type == null) ? "" : type
                .toString()), Util.escapeSpecialXMLChars(operator));
        p.indentRight();
        p.printf("<Lhs>\n");
        p.indentRight();
        lhs.writeToStdOut(p);
        p.indentLeft();
        p.printf("</Lhs>\n");
        p.printf("<Rhs>\n");
        p.indentRight();
        rhs.writeToStdOut(p);
        p.indentLeft();
        p.printf("</Rhs>\n");
        p.indentLeft();
        p.printf("</JBinaryExpression>\n");
    }

}

/**
 * The AST node for a plus (+) expression. In j--, as in Java, + is overloaded
 * to denote addition for numbers and concatenation for Strings.
 */

class JPlusOp extends JBinaryExpression {

    /**
     * Constructs an AST node for an addition expression given its line number,
     * and the lhs and rhs operands.
     * 
     * @param line
     *            line in which the addition expression occurs in the source
     *            file.
     * @param lhs
     *            the lhs operand.
     * @param rhs
     *            the rhs operand.
     */

    public JPlusOp(int line, JExpression lhs, JExpression rhs) {
        super(line, "+", lhs, rhs);
    }

    /**
     * Analysis involves first analyzing the operands. If this is a string
     * concatenation, we rewrite the subtree to make that explicit (and analyze
     * that). Otherwise we check the types of the addition operands and compute
     * the result type.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */

    public JExpression analyze(Context context) {
        lhs = (JExpression) lhs.analyze(context);
        rhs = (JExpression) rhs.analyze(context);
        if (lhs.type() == Type.STRING || rhs.type() == Type.STRING) {
            return (new JStringConcatenationOp(line, lhs, rhs))
                    .analyze(context);
        } else if (lhs.type() == Type.INT && rhs.type() == Type.INT) {
            type = Type.INT;
        } else if (lhs.type() == Type.DOUBLE && rhs.type() == Type.DOUBLE) {
            type = Type.DOUBLE;
        } else if (lhs.type() == Type.LONG && rhs.type() == Type.LONG) {
            type = Type.LONG;
        } else {
            type = Type.ANY;
            JAST.compilationUnit.reportSemanticError(line(),
                    "Invalid operand types for +");
        }
        return this;
    }

    /**
     * Any string concatenation has been rewritten as a 
     * {@link JStringConcatenationOp} (in {@code analyze}), so code generation 
     * here involves simply generating code for loading the operands onto the 
     * stack and then generating the appropriate add instruction.
     * 
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */

    public void codegen(CLEmitter output) {
       
        lhs.codegen(output);
        rhs.codegen(output);
         if  (type == Type.DOUBLE) {
            output.addNoArgInstruction(DADD);
        } else if (type == Type.LONG) {
            output.addNoArgInstruction(LADD);
        } else
            output.addNoArgInstruction(IADD);
    }
}

/**
 * The AST node for a subtraction (-) expression.
 */

class JSubtractOp extends JBinaryExpression {

    /**
     * Constructs an AST node for a subtraction expression given its line number,
     * and the lhs and rhs operands.
     * 
     * @param line
     *            line in which the subtraction expression occurs in the source
     *            file.
     * @param lhs
     *            the lhs operand.
     * @param rhs
     *            the rhs operand.
     */

    public JSubtractOp(int line, JExpression lhs, JExpression rhs) {
        super(line, "-", lhs, rhs);
    }

    /**
     * Analyzing the - operation involves analyzing its operands, checking
     * types, and determining the result type.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */

    public JExpression analyze(Context context) {
        lhs = (JExpression) lhs.analyze(context);
        rhs = (JExpression) rhs.analyze(context);
        if (lhs.type() == Type.INT && rhs.type() == Type.INT) {
            lhs.type().mustMatchExpected(line(), Type.INT);
            rhs.type().mustMatchExpected(line(), Type.INT);
            type = Type.INT;
        } else if (lhs.type() == Type.DOUBLE && rhs.type() == Type.DOUBLE) {
            lhs.type().mustMatchExpected(line(), Type.DOUBLE);
            rhs.type().mustMatchExpected(line(), Type.DOUBLE);
            type = Type.DOUBLE;
        } else if (lhs.type() == Type.LONG && rhs.type() == Type.LONG) {
            lhs.type().mustMatchExpected(line(), Type.LONG);
            rhs.type().mustMatchExpected(line(), Type.LONG);
            type = Type.LONG;
        } else {
            type = Type.ANY;
            JAST.compilationUnit.reportSemanticError(line(),
                    "Invalid operand types for +");
        }
        
        return this;
    }

    /**
     * Generating code for the - operation involves generating code for the two
     * operands, and then the subtraction instruction.
     * 
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */

    public void codegen(CLEmitter output) {
        lhs.codegen(output);
        rhs.codegen(output);
       if  (type == Type.DOUBLE) {
            output.addNoArgInstruction(DSUB);
        } else if (type == Type.LONG) {
            output.addNoArgInstruction(LSUB);
        } else
            output.addNoArgInstruction(ISUB);
    }
}

/**
 * The AST node for a multiplication (*) expression.
 */

class JMultiplyOp extends JBinaryExpression {

    /**
     * Constructs an AST for a multiplication expression given its line number,
     * and the lhs and rhs operands.
     * 
     * @param line
     *            line in which the multiplication expression occurs in the
     *            source file.
     * @param lhs
     *            the lhs operand.
     * @param rhs
     *            the rhs operand.
     */

    public JMultiplyOp(int line, JExpression lhs, JExpression rhs) {
        super(line, "*", lhs, rhs);
    }

    /**
     * Analyzing the * operation involves analyzing its operands, checking
     * types, and determining the result type.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */

    public JExpression analyze(Context context) {
        lhs = (JExpression) lhs.analyze(context);
        rhs = (JExpression) rhs.analyze(context);
        if (lhs.type() == Type.INT) {
            lhs.type().mustMatchExpected(line(), Type.INT);
            rhs.type().mustMatchExpected(line(), Type.INT);
            type = Type.INT;
        } else if (lhs.type() == Type.DOUBLE) {
            lhs.type().mustMatchExpected(line(), Type.DOUBLE);
            rhs.type().mustMatchExpected(line(), Type.DOUBLE);
            type = Type.DOUBLE;
        } else if (lhs.type() == Type.LONG) {
            lhs.type().mustMatchExpected(line(), Type.LONG);
            rhs.type().mustMatchExpected(line(), Type.LONG);
            type = Type.LONG; 
        }
        return this;
    }

    /**
     * Generating code for the * operation involves generating code for the two
     * operands, and then the multiplication instruction.
     * 
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */

    public void codegen(CLEmitter output) {
        lhs.codegen(output);
        rhs.codegen(output);
        if  (type == Type.DOUBLE) {
            output.addNoArgInstruction(DMUL);
        } else if (type == Type.LONG) {
            output.addNoArgInstruction(LMUL);
        } else
            output.addNoArgInstruction(IMUL);
    }
}
//Division Operator
//
//
//////////////////////

class JDivideOp extends JBinaryExpression {
    /**
             * Constructs an AST for a division expression given its line number,
             * and the lhs and rhs operands.
             * 
             * @param line
             *            line in which the division expression occurs in the
             *            source file.
             * @param lhs
             *            the lhs operand.
             * @param rhs
             *            the rhs operand.
             */    
    public JDivideOp ( int line , JExpression lhs , JExpression rhs ) {
        super ( line , "/" , lhs , rhs );
    }
    /**
     * Analyzing the / operation involves analyzing its operands, checking
     * types, and determining the result type.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */
    public JExpression analyze ( Context context ) {
        lhs = (JExpression) lhs.analyze (context);
        rhs = (JExpression) rhs.analyze (context);
        if (lhs.type() == Type.INT) {
            lhs.type().mustMatchExpected(line(), Type.INT);
            rhs.type().mustMatchExpected(line(), Type.INT);
            type = Type.INT;
        } else if (lhs.type() == Type.DOUBLE) {
            lhs.type().mustMatchExpected(line(), Type.DOUBLE);
            rhs.type().mustMatchExpected(line(), Type.DOUBLE);
            type = Type.DOUBLE;
        } else if (lhs.type() == Type.LONG) {
            lhs.type().mustMatchExpected(line(), Type.LONG);
            rhs.type().mustMatchExpected(line(), Type.LONG);
            type = Type.LONG; 
        }
        return this ;
    }
     /**
     * Generating code for the / operation involves generating code for the two
     * operands, and then the division instruction.
     * 
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */
    public void codegen ( CLEmitter output ) {
        lhs.codegen (output);
        rhs.codegen (output);
        if  (type == Type.DOUBLE) {
            output.addNoArgInstruction(DDIV);
        } else if (type == Type.LONG) {
            output.addNoArgInstruction(LDIV);
        } else
            output.addNoArgInstruction(IDIV);
    }
}

//Remainder Operator
//
//
//////////////////////

class JRemainOp extends JBinaryExpression {
    /**
             * Constructs an AST for a modulus expression given its line number,
             * and the lhs and rhs operands.
             * 
             * @param line
             *            line in which the modulus expression occurs in the
             *            source file.
             * @param lhs
             *            the lhs operand.
             * @param rhs
             *            the rhs operand.
             */    
    public JRemainOp ( int line , JExpression lhs , JExpression rhs ) {
        super ( line , "%" , lhs , rhs );
    }
    /**
     * Analyzing the % operation involves analyzing its operands, checking
     * types, and determining the result type.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */
    public JExpression analyze ( Context context ) {
        lhs = (JExpression) lhs.analyze (context);
        rhs = (JExpression) rhs.analyze (context);
        if (lhs.type() == Type.INT) {
            lhs.type().mustMatchExpected(line(), Type.INT);
            rhs.type().mustMatchExpected(line(), Type.INT);
            type = Type.INT;
        } else if (lhs.type() == Type.DOUBLE) {
            lhs.type().mustMatchExpected(line(), Type.DOUBLE);
            rhs.type().mustMatchExpected(line(), Type.DOUBLE);
            type = Type.DOUBLE;
        } else if (lhs.type() == Type.LONG) {
            lhs.type().mustMatchExpected(line(), Type.LONG);
            rhs.type().mustMatchExpected(line(), Type.LONG);
            type = Type.LONG; 
        }
        return this ;
    }
     /**
     * Generating code for the % operation involves generating code for the two
     * operands, and then the remainder instruction.
     * 
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */
    public void codegen ( CLEmitter output ) {
        lhs.codegen (output);
        rhs.codegen (output);
        if  (type == Type.DOUBLE) {
            output.addNoArgInstruction(DREM);
        } else if (type == Type.LONG) {
            output.addNoArgInstruction(LREM);
        } else
            output.addNoArgInstruction(IREM);
    }
}

//LeftShift Operator
//
//
//////////////////////

class JLeftShiftOp extends JBinaryExpression {
    /**
             * Constructs an AST for a left shift expression given its line number,
             * and the lhs and rhs operands.
             * 
             * @param line
             *            line in which the left shift expression occurs in the
             *            source file.
             * @param lhs
             *            the lhs operand.
             * @param rhs
             *            the rhs operand.
             */    
    public JLeftShiftOp( int line , JExpression lhs , JExpression rhs ) {
        super ( line , "<<" , lhs , rhs );
    }
    /**
     * Analyzing the << operation involves analyzing its operands, checking
     * types, and determining the result type.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */
    public JExpression analyze ( Context context ) {
        lhs = (JExpression) lhs.analyze (context);
        rhs = (JExpression) rhs.analyze (context);
        if (lhs.type() == Type.INT) {
            lhs.type().mustMatchExpected(line(), Type.INT);
            rhs.type().mustMatchExpected(line(), Type.INT);
            type = Type.INT;
        } else if (lhs.type() == Type.LONG) {
            lhs.type().mustMatchExpected(line(), Type.LONG);
            rhs.type().mustMatchExpected(line(), Type.LONG);
            type = Type.LONG; 
        }
        return this ;
    }
     /**
     * Generating code for the shift left operation involves generating code for the two
     * operands, and then the shift left instruction.
     * 
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */
    public void codegen ( CLEmitter output ) {
        lhs.codegen (output);
        rhs.codegen (output);
        if  (type == Type.LONG) {
            output.addNoArgInstruction(LSHL);
        } else
            output.addNoArgInstruction(ISHL);
    }
}

//RightShift Operator
//
//
//////////////////////
class JRightShiftOp extends JBinaryExpression {
    /**
             * Constructs an AST for a right shift expression given its line number,
             * and the lhs and rhs operands.
             * 
             * @param line
             *            line in which the right shift expression occurs in the
             *            source file.
             * @param lhs
             *            the lhs operand.
             * @param rhs
             *            the rhs operand.
             */    
    public JRightShiftOp( int line , JExpression lhs , JExpression rhs ) {
        super ( line , ">>" , lhs , rhs );
    }
    /**
     * Analyzing the >> operation involves analyzing its operands, checking
     * types, and determining the result type.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */
    public JExpression analyze ( Context context ) {
        lhs = (JExpression) lhs.analyze (context);
        rhs = (JExpression) rhs.analyze (context);
        if (lhs.type() == Type.INT) {
            lhs.type().mustMatchExpected(line(), Type.INT);
            rhs.type().mustMatchExpected(line(), Type.INT);
            type = Type.INT;
        } else if (lhs.type() == Type.LONG) {
            lhs.type().mustMatchExpected(line(), Type.LONG);
            rhs.type().mustMatchExpected(line(), Type.LONG);
            type = Type.LONG; 
        }
        return this ;
    }
     /**
     * Generating code for the right shift operation involves generating code for the two
     * operands, and then the right shift  instruction.
     * 
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */
    public void codegen ( CLEmitter output ) {
        lhs.codegen (output);
        rhs.codegen (output);
        if  (type == Type.LONG) {
            output.addNoArgInstruction(LSHR);
        } else
            output.addNoArgInstruction(ISHR);
    }
}
//Logical Right Shift Operator
//
//
//////////////////////

class JLogicalRightShiftOp extends JBinaryExpression {
    /**
             * Constructs an AST for a logical right shift expression given its line number,
             * and the lhs and rhs operands.
             * 
             * @param line
             *            line in which the logical right shift expression occurs in the
             *            source file.
             * @param lhs
             *            the lhs operand.
             * @param rhs
             *            the rhs operand.
             */    
    public JLogicalRightShiftOp( int line , JExpression lhs , JExpression rhs ) {
        super ( line , ">>>" , lhs , rhs );
    }
    /**
     * Analyzing the >>> operation involves analyzing its operands, checking
     * types, and determining the result type.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */
    public JExpression analyze ( Context context ) {
        lhs = (JExpression) lhs.analyze (context);
        rhs = (JExpression) rhs.analyze (context);
        if (lhs.type() == Type.INT) {
            lhs.type().mustMatchExpected(line(), Type.INT);
            rhs.type().mustMatchExpected(line(), Type.INT);
            type = Type.INT;
        } else if (lhs.type() == Type.LONG) {
            lhs.type().mustMatchExpected(line(), Type.LONG);
            rhs.type().mustMatchExpected(line(), Type.LONG);
            type = Type.LONG; 
        }
        return this ;
    }
     /**
     * Generating code for the logical right shift operation involves generating code for the two
     * operands, and then the logical right shift instruction.
     * 
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */
    public void codegen ( CLEmitter output ) {
        lhs.codegen (output);
        rhs.codegen (output);
        if  (type == Type.LONG) {
            output.addNoArgInstruction(LUSHR);
        } else
            output.addNoArgInstruction(IUSHR);
    }
}

//Inclusive Or Operator
//
//
//////////////////////

class JInclusiveOrOp extends JBinaryExpression {
    /**
             * Constructs an AST for a inclusive Or expression given its line number,
             * and the lhs and rhs operands.
             * 
             * @param line
             *            line in which the inclusive or expression occurs in the
             *            source file.
             * @param lhs
             *            the lhs operand.
             * @param rhs
             *            the rhs operand.
             */    
    public JInclusiveOrOp( int line , JExpression lhs , JExpression rhs ) {
        super ( line , "|" , lhs , rhs );
    }
    /**
     * Analyzing the | operation involves analyzing its operands, checking
     * types, and determining the result type.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */
    public JExpression analyze ( Context context ) {
        lhs = (JExpression) lhs.analyze (context);
        rhs = (JExpression) rhs.analyze (context);
        if (lhs.type() == Type.INT) {
            lhs.type().mustMatchExpected(line(), Type.INT);
            rhs.type().mustMatchExpected(line(), Type.INT);
            type = Type.INT;
        } else if (lhs.type() == Type.LONG) {
            lhs.type().mustMatchExpected(line(), Type.LONG);
            rhs.type().mustMatchExpected(line(), Type.LONG);
            type = Type.LONG; 
        }
        return this ;
    }
     /**
     * Generating code for the inclusive Or operation involves generating code for the two
     * operands, and then the inclusive or instruction.
     * 
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */
    public void codegen ( CLEmitter output ) {
        lhs.codegen (output);
        rhs.codegen (output);
        if  (type == Type.LONG) {
            output.addNoArgInstruction(LOR);
        } else
            output.addNoArgInstruction(IOR);
    }
}

//Exclusive Or Operator
//
//
//////////////////////

class JExclusiveOrOp extends JBinaryExpression {
    /**
             * Constructs an AST for a exclusive Or expression given its line number,
             * and the lhs and rhs operands.
             * 
             * @param line
             *            line in which the exclusive or expression occurs in the
             *            source file.
             * @param lhs
             *            the lhs operand.
             * @param rhs
             *            the rhs operand.
             */    
    public JExclusiveOrOp( int line , JExpression lhs , JExpression rhs ) {
        super ( line , "^" , lhs , rhs );
    }
    /**
     * Analyzing the ^ operation involves analyzing its operands, checking
     * types, and determining the result type.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */
    public JExpression analyze ( Context context ) {
        lhs = (JExpression) lhs.analyze (context);
        rhs = (JExpression) rhs.analyze (context);
        if (lhs.type() == Type.INT) {
            lhs.type().mustMatchExpected(line(), Type.INT);
            rhs.type().mustMatchExpected(line(), Type.INT);
            type = Type.INT;
        } else if (lhs.type() == Type.LONG) {
            lhs.type().mustMatchExpected(line(), Type.LONG);
            rhs.type().mustMatchExpected(line(), Type.LONG);
            type = Type.LONG; 
        }
        return this ;
    }
     /**
     * Generating code for the exclusive Or operation involves generating code for the two
     * operands, and then the exclusive or instruction.
     * 
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */
    public void codegen ( CLEmitter output ) {
        lhs.codegen (output);
        rhs.codegen (output);
        if  (type == Type.LONG) {
            output.addNoArgInstruction(LXOR);
        } else
            output.addNoArgInstruction(IXOR);
    }
}


//And Operator
//
//
//////////////////////


class JAndOp extends JBinaryExpression {
    /**
             * Constructs an AST for an and expression given its line number,
             * and the lhs and rhs operands.
             * 
             * @param line
             *            line in which the and expression occurs in the
             *            source file.
             * @param lhs
             *            the lhs operand.
             * @param rhs
             *            the rhs operand.
             */    
    public JAndOp( int line , JExpression lhs , JExpression rhs ) {
        super ( line , "&" , lhs , rhs );
    }
    /**
     * Analyzing the & operation involves analyzing its operands, checking
     * types, and determining the result type.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */
    public JExpression analyze ( Context context ) {
        lhs = (JExpression) lhs.analyze (context);
        rhs = (JExpression) rhs.analyze (context);
        if (lhs.type() == Type.INT) {
            lhs.type().mustMatchExpected(line(), Type.INT);
            rhs.type().mustMatchExpected(line(), Type.INT);
            type = Type.INT;
        } else if (lhs.type() == Type.LONG) {
            lhs.type().mustMatchExpected(line(), Type.LONG);
            rhs.type().mustMatchExpected(line(), Type.LONG);
            type = Type.LONG; 
        }
        return this ;
    }
     /**
     * Generating code for the and operation involves generating code for the two
     * operands, and then the and instruction.
     * 
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */
    public void codegen ( CLEmitter output ) {
        lhs.codegen (output);
        rhs.codegen (output);
        if  (type == Type.LONG) {
            output.addNoArgInstruction(LAND);
        } else
            output.addNoArgInstruction(IAND);
    }
}



