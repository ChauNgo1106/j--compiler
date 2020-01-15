// Copyright 2013 Bill Campbell, Swami Iyer and Bahar Akbal-Delibas

package jminusminus;

import java.util.ArrayList;
import static jminusminus.CLConstants.*;

/**
 * A class declaration has a list of modifiers, a name, a super class and a
 * class block; it distinguishes between instance fields and static (class)
 * fields for initialization, and it defines a type. It also introduces its own
 * (class) context.
 *
 * @see ClassContext
 */

class JInterfaceDeclaration extends JAST implements JTypeDecl {

    /**  modifiers. */
    private ArrayList<String> mods;

    /**  Interface qualified name. */
    private String name;

    /** Interface block. */
    private ArrayList<JMember> interfaceBlock;

    //** extends */
    private ArrayList<TypeName> ext;
    
    private Type thisType, superType = null;

    /** Context for this class. */
    private ClassContext context;
    /**
     * Constructs an AST node for a class declaration given the line number, list
     * of class modifiers, name of the class, its super class type, and the
     * class block.
     * 
     * @param line
     *            line in which the class declaration occurs in the source file.
     * @param mods
     *            class modifiers.
     * @param name
     *            class name.
     * @param superType
     *            super class type.
     * @param classBlock
     *            class block.
     */
     
    public JInterfaceDeclaration(int line, ArrayList<String> mods, String name,
        ArrayList<TypeName> ext, ArrayList<JMember> interfaceBlock) {
        super(line);
        this.mods = mods;
        this.name = name;
        this.ext = ext;
        this.interfaceBlock = interfaceBlock;
    }

  /**
     * Returns the class name.
     * 
     * @return the class name.
     */

    public String name() {
        return name;
    }

    /**
     * Returns the class' super class type.
     * 
     * @return the super class type.
     */

    public Type superType() {
        return superType;
    }

    /**
     * Returns the type that this class declaration defines.
     * 
     * @return the defined type.
     */

    public Type thisType() {
        return thisType;
    }

    /**
     * Declares this class in the parent (compilation unit) context.
     * 
     * @param context
     *            the parent (compilation unit) context.
     */

     public void declareThisType(Context context) {
        String qualifiedName = JAST.compilationUnit.packageName() == "" ? name
                : JAST.compilationUnit.packageName() + "/" + name;
        CLEmitter partial = new CLEmitter(false);
        partial.addClass(mods, qualifiedName, Type.OBJECT.jvmName(), null,
                false); // Object for superClass
        thisType = Type.typeFor(partial.toClass());
        context.addType(line, thisType);
    }

    /**
     * Pre-analyzes the members of this declaration in the parent context.
     * Pre-analysis extends to the member headers (including method headers) but
     * not into the bodies.
     * 
     * @param context
     *            the parent (compilation unit) context.
     */

     public void preAnalyze(Context context) {
        // Construct a class context
        this.context = new ClassContext(this, context);

        // Creating a partial class in memory can result in a
        // java.lang.VerifyError if the semantics below are
        // violated, so we can't defer these checks to analyze()
    
        // Create the (partial) class
        CLEmitter partial = new CLEmitter(false);
        
        // Add the class header to the partial class
        String qualifiedName = JAST.compilationUnit.packageName() == "" ? name
                : JAST.compilationUnit.packageName() + "/" + name;
        partial.addClass(mods, qualifiedName, Type.OBJECT.jvmName(), null, false);

        // Pre-analyze the members and add them to the partial
        // class
        for (JMember member : interfaceBlock) {
            member.preAnalyze(this.context, partial);
          }
        // Get the Class rep for the (partial) class and make it the
        // representation for this type
        Type id = this.context.lookupType(name);
        if (id != null && !JAST.compilationUnit.errorHasOccurred()) {
            id.setClassRep(partial.toClass());
        }
    }

    /**
     * Performs semantic analysis on the interface class and all of its members within the
     * given context. Analysis includes field initializations and the method
     * bodies.
     * 
     * @param context
     *            the parent (compilation unit) context. Ignored here.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */
    public JAST analyze(Context context) {
        // Analyze all members
        for (JMember member : interfaceBlock) {
            ((JAST) member).analyze(this.context);
        }
        return this;
    }

    /**
     * Generates code for the class declaration.
     * 
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */

    public void codegen(CLEmitter output) {
        // The class header
        String qualifiedName = JAST.compilationUnit.packageName() == "" ? name
                : JAST.compilationUnit.packageName() + "/" + name;
        output.addClass(mods, qualifiedName, Type.OBJECT.jvmName(), null, false);
        // The implicit empty constructor?
        /*
        if (!hasExplicitConstructor) {
           // codegenImplicitConstructor(output);
        }
        */
        // The members
        for (JMember member : interfaceBlock) {
            ((JAST) member).codegen(output);
        }
    /*
          // Generate a class initialization method?
        if (staticFieldInitializations.size() > 0) {
            //codegenClassInit(output);
        }
        */
    }

    /**
     * {@inheritDoc}
     */
  
    public void writeToStdOut(PrettyPrinter p) {
        p.printf("<JInterfaceDeclaration line=\"%d\" name=\"%s\" \n",
                 line(), name);
        p.indentRight();
        
        if (mods != null) {
            p.printf("<Modifiers>\n");
            p.indentRight();
            for (String mod : mods) {
                p.printf("<Modifier name=\"%s\"/>\n", mod);
            }
            p.indentLeft();
            p.printf("</Modifiers>\n");
        }
       if (ext != null) {
            p.println("<Extends>");
            p.indentRight();
            for (TypeName ls : ext) {
                p.printf("<Extend name=\"%s\"/>\n", ls.toString());
            }
            p.indentLeft();
            p.printf("</Extends>\n");
        }
        
        if (interfaceBlock != null) {
            p.printf("<InterfaceBlock>\n");
            p.indentRight();
            for (JMember member : interfaceBlock) {
                ((JAST) member).writeToStdOut(p);
            }
            p.indentLeft();
            p.printf("</InterfaceBlock>\n");
        }
        p.indentLeft();
        p.printf("</JInterfaceDeclaration>\n");
    }

}    
