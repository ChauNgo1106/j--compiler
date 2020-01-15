// Copyright 2013- Bill Campbell, Swami Iyer and Bahar Akbal-Delibas

import jminusminus.CLEmitter;
import static jminusminus.CLConstants.*;
import java.util.ArrayList;

/**
 * This class programatically generates the class file for 
 * the following Java application:
 * 
 * public class Factorial
 * {
 *     public int factorial(int n)
 *     {
 *         if (n &lt;= 1) {
 *             return n;
 *         }
 *         else {
 *             return (n * factorial(n - 1));
 *         }
 *     }
 *     
 *     public static void main(String[] args)
 *     {
 *         Factorial f = new Factorial();
 *         int n = Integer.parseInt(args[0]);
 *         System.out.println("Factorial(" + n + ") = " + f.factorial(n));
 *     }
 * }
 */

public class GenFactorial {
    public static void main(String[] args) {
        CLEmitter e = new CLEmitter(true);
        ArrayList<String> accessFlags = new ArrayList<String>();

        // Add Factorial class
        accessFlags.add("public");
        e.addClass(accessFlags, "Factorial", "java/lang/Object", null, true);

        // Add the implicit no-arg constructor Factorial()
        accessFlags.clear();
        accessFlags.add("public");
        e.addMethod(accessFlags, "<init>", "()V", null, true);
        e.addNoArgInstruction(ALOAD_0);
        e.addMemberAccessInstruction(INVOKESPECIAL, "java/lang/Object",
                                                    "<init>", "()V");
        e.addNoArgInstruction(RETURN);

        // Add factorial() method to Factorial
        accessFlags.clear();
        accessFlags.add("public");
        e.addMethod(accessFlags, "factorial", "(I)I", null, true);

        // Load n on the stack
        e.addNoArgInstruction(ILOAD_1);
        
        // Load the constant 1 on the stack
        e.addNoArgInstruction(ICONST_1);

        // Branch to "Label1" if n > 1
        e.addBranchInstruction(IF_ICMPGT, "Label1");

        // Base case: load n on the stack and return it to the caller
        e.addNoArgInstruction(ILOAD_1);
        e.addNoArgInstruction(IRETURN);

        // Emit label "Label1"
        e.addLabel("Label1");

        // Load n on the stack
        e.addNoArgInstruction(ILOAD_1);
        
        // Load "this" on the stack
        e.addNoArgInstruction(ALOAD_0);
        
        // Load n and the constant 1 on the stack
        e.addNoArgInstruction(ILOAD_1);
        e.addNoArgInstruction(ICONST_1);

        // Comute n - 1 on the stack
        e.addNoArgInstruction(ISUB);

        // Invoke the instance method this.factorial() to compute (n - 1)!
        // on the stack
        e.addMemberAccessInstruction(INVOKEVIRTUAL, "Factorial",
                                                    "factorial", "(I)I");

        // Compute n * (n - 1)! on the stack and return it to the caller
        e.addNoArgInstruction(IMUL);
        e.addNoArgInstruction(IRETURN);

        // Add main() method to Factorial
        accessFlags.clear();
        accessFlags.add("public");
        accessFlags.add("static");
        e.addMethod(accessFlags, "main", "([Ljava/lang/String;)V", null, true);

        // Create an intance f of Factorial on the stack and duplicate it
        e.addReferenceInstruction(NEW, "Factorial");
        e.addNoArgInstruction(DUP);

        // Invoke the constructor Factorial()
        e.addMemberAccessInstruction(INVOKESPECIAL, "Factorial", "<init>",
                                                    "()V");

        // Get command-line argument n, convert it into an integer, and
        // store it away
        e.addNoArgInstruction(ASTORE_1);
        e.addNoArgInstruction(ALOAD_0);
        e.addNoArgInstruction(ICONST_0);
        e.addNoArgInstruction(AALOAD);
        e.addMemberAccessInstruction(INVOKESTATIC, "java/lang/Integer",
                                                   "parseInt", 
                                                   "(Ljava/lang/String;)I");
        e.addNoArgInstruction(ISTORE_2);

        // Get System.out on the stack
        e.addMemberAccessInstruction(GETSTATIC, "java/lang/System", "out",
                                                "Ljava/io/PrintStream;");

        // Create an intance sb of StringBuffer on the stack and duplicate it
        e.addReferenceInstruction(NEW, "java/lang/StringBuffer");
        e.addNoArgInstruction(DUP);

        // Invoke the constructor StringBuffer()
        e.addMemberAccessInstruction(INVOKESPECIAL, "java/lang/StringBuffer",
                                                    "<init>", 
                                                    "()V");

        // Load the constant "Factorial(" on the stack and call instance
        // method StringBuffer.append() to append it to sb
        e.addLDCInstruction("Factorial(");
        e.addMemberAccessInstruction(INVOKEVIRTUAL, "java/lang/StringBuffer",
                                                    "append", 
                                "(Ljava/lang/String;)Ljava/lang/StringBuffer;");

        // Load n on the stack and append it to sb
        e.addNoArgInstruction(ILOAD_2);
        e.addMemberAccessInstruction(INVOKEVIRTUAL, "java/lang/StringBuffer",
                                                    "append", 
                                                "(I)Ljava/lang/StringBuffer;");
        
        // Load the constant ") = " on the stack and append it to sb
        e.addLDCInstruction(") = ");
        e.addMemberAccessInstruction(INVOKEVIRTUAL, "java/lang/StringBuffer",
                                                    "append", 
                                "(Ljava/lang/String;)Ljava/lang/StringBuffer;");

        // Load f and n on the stack
        e.addNoArgInstruction(ALOAD_1);
        e.addNoArgInstruction(ILOAD_2);

        // Invoke the instance method f.factorial() and append n! to sb
        e.addMemberAccessInstruction(INVOKEVIRTUAL, "Factorial",
                                                    "factorial", 
                                                    "(I)I");
        e.addMemberAccessInstruction(INVOKEVIRTUAL, "java/lang/StringBuffer",
                                                    "append", 
                                                "(I)Ljava/lang/StringBuffer;");

        // Invoke instance method sb.toString() to turn it into a string on
        // the stack and invoke the instance method System.out.println()
        // to print the string
        e.addMemberAccessInstruction(INVOKEVIRTUAL, "java/lang/StringBuffer",
                                                    "toString", 
                                                    "()Ljava/lang/String;");
        e.addMemberAccessInstruction(INVOKEVIRTUAL, "java/io/PrintStream",
                                                    "println", 
                                                    "(Ljava/lang/String;)V");

        // Return from the method
        e.addNoArgInstruction(RETURN);

        // Write Factorial.class to file system
        e.write();
    }
}
