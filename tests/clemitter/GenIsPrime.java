/*
import jminusminus.CLEmitter;
import static jminusminus.CLConstants.*;
import java.util.ArrayList;

public class GenIsPrime {
    public static void main(String[] args) {
        CLEmitter e = new CLEmitter(true);

        ...
            
        e.write();
    }
}
*/


// Copyright 2013- Bill Campbell, Swami Iyer and Bahar Akbal-Delibas

import jminusminus.CLEmitter;
import static jminusminus.CLConstants.*;
import java.util.ArrayList;


public class GenIsPrime {
    public static void main(String[] args) {
        CLEmitter e = new CLEmitter(true);
        ArrayList<String> accessFlags = new ArrayList<String>();

        // Add IsPrime  class
        accessFlags.add("public");
        e.addClass(accessFlags, "IsPrime", "java/lang/Object", null, true);

        // Add the implicit no-arg constructor IsPrime()
        accessFlags.clear();
        accessFlags.add("public");
        e.addMethod(accessFlags, "<init>", "()V", null, true);
        e.addNoArgInstruction(ALOAD_0);
        e.addMemberAccessInstruction(INVOKESPECIAL, "java/lang/Object",
                                                    "<init>", "()V");
        e.addNoArgInstruction(RETURN);

        // Add isPrime() method to IsPrime
        accessFlags.clear();
        accessFlags.add("public");
        accessFlags.add("static");
        
        e.addMethod(accessFlags, "isPrime", "(I)Z", null, true);

        // Load n on the stack
        e.addNoArgInstruction(ILOAD_0);
        // Load the constant 2 on the stack
        e.addNoArgInstruction(ICONST_2);

        // Branch to "A" if n >= 2
        e.addBranchInstruction(IF_ICMPGE, "A"); 

        // load 0 as FALSE value on the stack and return it to the caller
        e.addNoArgInstruction(ICONST_0);
        e.addNoArgInstruction(IRETURN);

        // Emit label "A"
        e.addLabel("A");
        //i = 2
        e.addNoArgInstruction(ICONST_2);
        e.addNoArgInstruction(ISTORE_1);
       
        
        
        //Emit Label "D"
        e.addLabel("D");
        e.addNoArgInstruction(ILOAD_1);
        e.addNoArgInstruction(ILOAD_0); 
        e.addNoArgInstruction(ILOAD_1);
             // Branch to "B" if i > n / i
        e.addNoArgInstruction(IDIV); // n/i
        e.addBranchInstruction(IF_ICMPGT, "B");// i> n/i ?
     
            // Branch to "C" if n % i != 0
        e.addNoArgInstruction(ILOAD_0); //load n
        e.addNoArgInstruction(ILOAD_1); //load i
        e.addNoArgInstruction(IREM);
        e.addBranchInstruction(IFNE, "C");
        //return false
        e.addNoArgInstruction(ICONST_0);
        e.addNoArgInstruction(IRETURN);
        
        //Emit Label "C"
        e.addLabel("C");
        e.addIINCInstruction (1 , 1); //increment i by 1
        e.addBranchInstruction(GOTO, "D");
        //Emit label "B"
        e.addLabel("B");
        e.addNoArgInstruction(ICONST_1);
        e.addNoArgInstruction(IRETURN);


        // Add main() method to IsPrime
        accessFlags.clear();
        accessFlags.add("public");
        accessFlags.add("static");
        e.addMethod(accessFlags, "main", "([Ljava/lang/String;)V", null, true);


       
        // Get command-line argument n, convert it into an integer, and
        // store it away
        e.addNoArgInstruction(ALOAD_0);
        e.addNoArgInstruction(ICONST_0);
        e.addNoArgInstruction(AALOAD);
        e.addMemberAccessInstruction(INVOKESTATIC, "java/lang/Integer",
                                                   "parseInt", 
                                                   "(Ljava/lang/String;)I");
        e.addNoArgInstruction(ISTORE_1);
        e.addNoArgInstruction (ILOAD_1);
        e.addMemberAccessInstruction(INVOKESTATIC, "IsPrime",
                                                   "isPrime", 
                                                   "(I)Z");
    
        
         e.addNoArgInstruction (ISTORE_2);
         e.addNoArgInstruction (ILOAD_2);
         e.addBranchInstruction (IFEQ, "J");
         
         e.addMemberAccessInstruction(GETSTATIC, "java/lang/System", "out",
                                                "Ljava/io/PrintStream;");
        // Create an intance sb of StringBuffer on the stack and duplicate it
        e.addReferenceInstruction(NEW, "java/lang/StringBuffer");
        e.addNoArgInstruction(DUP);
        
        // Invoke the constructor StringBuffer()
        e.addMemberAccessInstruction(INVOKESPECIAL, "java/lang/StringBuffer",
                                                    "<init>", 
                                                    "()V");
        
        e.addNoArgInstruction (ILOAD_1);
        e.addMemberAccessInstruction(INVOKEVIRTUAL, "java/lang/StringBuffer",
                                                    "append", 
                                                "(I)Ljava/lang/StringBuffer;");
         e.addLDCInstruction(" is a prime number");
         e.addMemberAccessInstruction(INVOKEVIRTUAL, "java/lang/StringBuffer",
                                                    "append", 
                                "(Ljava/lang/String;)Ljava/lang/StringBuffer;");
         e.addMemberAccessInstruction(INVOKEVIRTUAL, "java/lang/StringBuffer",
                                                    "toString", 
                                                    "()Ljava/lang/String;");
        e.addMemberAccessInstruction(INVOKEVIRTUAL, "java/io/PrintStream",
                                                    "println", 
                                                    "(Ljava/lang/String;)V");
                                                                     
         e.addBranchInstruction (GOTO, "R");
         e.addLabel ("J");
         e.addMemberAccessInstruction(GETSTATIC, "java/lang/System", "out",
                                                "Ljava/io/PrintStream;");
        // Create an intance sb of StringBuffer on the stack and duplicate it
        e.addReferenceInstruction(NEW, "java/lang/StringBuffer");
        e.addNoArgInstruction(DUP);
        
        // Invoke the constructor StringBuffer()
        e.addMemberAccessInstruction(INVOKESPECIAL, "java/lang/StringBuffer",
                                                    "<init>", 
                                                    "()V");
         e.addNoArgInstruction (ILOAD_1);
         e.addMemberAccessInstruction(INVOKEVIRTUAL, "java/lang/StringBuffer",
                                                    "append", 
                                                "(I)Ljava/lang/StringBuffer;");
         e.addLDCInstruction(" is not a prime number");
         e.addMemberAccessInstruction(INVOKEVIRTUAL, "java/lang/StringBuffer",
                                                    "append", 
                                "(Ljava/lang/String;)Ljava/lang/StringBuffer;");
        e.addMemberAccessInstruction(INVOKEVIRTUAL, "java/lang/StringBuffer",
                                                    "toString", 
                                                    "()Ljava/lang/String;");
        e.addMemberAccessInstruction(INVOKEVIRTUAL, "java/io/PrintStream",
                                                    "println", 
                                                    "(Ljava/lang/String;)V");
         e.addLabel ("R");
        // Return from the method
        e.addNoArgInstruction(RETURN);

        // Write Factorial.class to file system
        e.write();
    }
}
