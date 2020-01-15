// Copyright 2013- Bill Campbell, Swami Iyer and Bahar Akbal-Delibas

import jminusminus.CLEmitter;
import static jminusminus.CLConstants.*;
import java.util.ArrayList;

/**
 * This class programatically generates the class file for the 
 * following Java application using CLEmitter:
 * 
 * public class HelloWorld
 * {
 *     private static String message()
 *     {
 *         return "Hello, World!"; 
 *     }
 *     
 *     public static void main(String[] args) 
 *     {
 *         System.out.println(HelloWorld.message());
 *     }
 * }
 */

public class GenHelloWorld {
    public static void main(String[] args) {
        CLEmitter e = new CLEmitter(true);
        ArrayList<String> accessFlags = new ArrayList<String>();

        // Add HelloWorld class
        accessFlags.add("public");
        e.addClass(accessFlags, "HelloWorld", "java/lang/Object", null, true);

        // Add message() method to HelloWorld
        accessFlags.clear();
        accessFlags.add("private");
        accessFlags.add("static");
        e.addMethod(accessFlags, "message", "()Ljava/lang/String;", null, true);

        // Load the constant "Hello, World!" on the stack and return it
        // to the caller
        e.addLDCInstruction("Hello, World!");
        e.addNoArgInstruction(ARETURN);

        // Add main() method to HelloWorld
        accessFlags.clear();
        accessFlags.add("public");
        accessFlags.add("static");
        e.addMethod(accessFlags, "main", "([Ljava/lang/String;)V", null, true);

        // Get System.out on the stack
        e.addMemberAccessInstruction(GETSTATIC, "java/lang/System", "out",
                                                "Ljava/io/PrintStream;");

        // Invoke the static method HelloWorld.message()
        e.addMemberAccessInstruction(INVOKESTATIC, "HelloWorld", "message",
                                                   "()Ljava/lang/String;");

        // Invoke the instance method System.out.println()
        e.addMemberAccessInstruction(INVOKEVIRTUAL, "java/io/PrintStream",
                                                    "println", 
                                                    "(Ljava/lang/String;)V");

        // Return from the method
        e.addNoArgInstruction(RETURN);

        // Write HelloWorld.class to file system
        e.write();
    }
}
