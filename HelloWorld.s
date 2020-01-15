# ./HelloWorld.s
# Source file: HelloWorld.java
# Compiled: Mon Sep 16 15:27:48 EDT 2019

.text

main:
    subu    $sp,$sp,24 	 # Stack frame is 24 bytes long
    sw      $ra,20($sp) 	 # Save return address
    sw      $fp,16($sp) 	 # Save frame pointer
    sw      $t0,12($sp) 	 # Save register $t0
    sw      $t1,8($sp) 	 # Save register $t1
    sw      $t2,4($sp) 	 # Save register $t2
    sw      $t3,0($sp) 	 # Save register $t3
    addiu   $fp,$sp,20 	 # Save frame pointer

main.0:

main.1:
    jal HelloWorld.message
    move $t0,$v0
    move $t1,$a0
    move $a0,$t0
    jal spim.SPIM.printString
    move $a0,$t1
    la $t2,Constant..String0+12
    move $t3,$a0
    move $a0,$t2
    jal spim.SPIM.printString
    move $a0,$t3
    j main.restore

main.restore:
    lw      $ra,20($sp) 	 # Restore return address
    lw      $fp,16($sp) 	 # Restore frame pointer
    lw      $t0,12($sp) 	 # Restore register $t0
    lw      $t1,8($sp) 	 # Restore register $t1
    lw      $t2,4($sp) 	 # Restore register $t2
    lw      $t3,0($sp) 	 # Restore register $t3
    addiu   $sp,$sp,24 	 # Pop stack
    jr      $ra 	 # Return to caller

.data

Constant..String0:
    .word 2 # Tag 2 indicates a string
    .word 16 # Size of object in bytes
    .word 1 # String length (not including null terminator)
    .asciiz "
" # String terminated by null character 0
    .align 2 # Next object is on a word boundary


.text

HelloWorld.message:
    subu    $sp,$sp,12 	 # Stack frame is 12 bytes long
    sw      $ra,8($sp) 	 # Save return address
    sw      $fp,4($sp) 	 # Save frame pointer
    sw      $t0,0($sp) 	 # Save register $t0
    addiu   $fp,$sp,8 	 # Save frame pointer

HelloWorld.message.0:

HelloWorld.message.1:
    la $t0,Constant..String1+12
    move $v0,$t0
    j HelloWorld.message.restore

HelloWorld.message.restore:
    lw      $ra,8($sp) 	 # Restore return address
    lw      $fp,4($sp) 	 # Restore frame pointer
    lw      $t0,0($sp) 	 # Restore register $t0
    addiu   $sp,$sp,12 	 # Pop stack
    jr      $ra 	 # Return to caller

.data

Constant..String1:
    .word 2 # Tag 2 indicates a string
    .word 28 # Size of object in bytes
    .word 13 # String length (not including null terminator)
    .asciiz "Hello, World!" # String terminated by null character 0
    .align 2 # Next object is on a word boundary


# SPIM Runtime

# Copyright 2013 Bill Campbell, Swami Iyer and Bahar Akbal-Delibas

# SPIM.s 

.text

# Print the integer value passed as parameter.

spim.SPIM.printInt:

    subu $sp,$sp,32     # Stack frame is 32 bytes long
    sw $fp,28($sp)      # Save frame pointer
    addu $fp,$sp,3      # Set up frame pointer

    li $v0,1            # Syscall code to print an integer
    syscall             # Prints the arg value

    lw $fp,28($sp)      # Restore frame pointer
    addiu $sp,$sp,32    # Restore the stack pointer
    jr $ra              # Return to caller

# Print the float value passed as parameter.

spim.SPIM.printFloat:

    subu $sp,$sp,32     # Stack frame is 32 bytes long
    sw $fp,28($sp)      # Save frame pointer
    addu $fp,$sp,32     # Set up frame pointer

    li $v0,2            # Syscall code to print a float
    syscall             # Prints the arg value

    lw $fp,28($sp)      # Restore frame pointer
    addiu $sp,$sp,32    # Restore the stack pointer
    jr $ra              # Return to caller

# Print the double value passed as parameter.

spim.SPIM.printDouble:
    subu $sp,$sp,32     # Stack frame is 32 bytes long
    sw $fp,28($sp)      # Save frame pointer
    addu $fp,$sp,32     # Set up frame pointer

    li $v0,3            # Syscall code to print a double
    syscall             # Prints the arg value

    lw $fp,28($sp)      # Restore frame pointer
    addiu $sp,$sp,32    # restore the stack pointer
    jr $ra              # Return to caller

# Print the string value passed as parameter.

spim.SPIM.printString:
    subu $sp,$sp,32     # Stack frame is 32 bytes long
    sw $fp,28($sp)      # Save frame pointer
    addu $fp,$sp,32     # Set up frame pointer

    li $v0,4            # Syscall code to print a string
    syscall             # Print the string value

    lw $fp,28($sp)      # Restore frame pointer
    addiu $sp,$sp,32    # Restore the stack pointer
    jr $ra              # Return to caller

# Print the char value passed as parameter.

spim.SPIM.printChar:
    subu $sp,$sp,32     # Stack frame is 32 bytes long
    sw $fp,28($sp)      # Save frame pointer
    addu $fp,$sp,32     # Set up frame pointer


    li $v0,11           # Syscall code to print a char
    syscall             # Print the char value

    lw $fp,28($sp)      # Restore frame pointer
    addiu $sp,$sp,32    # Restore the stack pointer
    jr $ra              # Return to caller

# Read the integer value from the user through console.

spim.SPIM.readInt:
    subu $sp,$sp,32     # Stack frame is 32 bytes long
    sw $fp,28($sp)      # Save frame pointer
    addu $fp,$sp,32     # Set up frame pointer

    li $v0,5            # Syscall code to read an integer
    syscall             # Load the integer value read from console into $v0

    lw $fp,28($sp)      # Restore frame pointer
    addiu $sp,$sp,32    # Restore the stack pointer
    jr $ra              # Return to caller

# Read the float value from the user through console.

spim.SPIM.readFloat:
    subu $sp,$sp,32     # Stack frame is 32 bytes long
    sw $fp,28($sp)      # Save frame pointer
    addu $fp,$sp,32     # Set up frame pointer

    li $v0,6            # Syscall code to read a float
    syscall             # Load the float value read from console into $f0

    lw $fp,28($sp)      # Restore frame pointer
    addiu $sp,$sp,32    # Restore the stack pointer
    jr $ra              # Return to caller

# Read the double value from the user through console.

spim.SPIM.readDouble:
    subu $sp,$sp,32     # Stack frame is 32 bytes long
    sw $fp,28($sp)      # Save frame pointer
    addu $fp,$sp,32     # Set up frame pointer

    li $v0,7            # Syscall code to read a double
    syscall             # Load the float value read from console into $f0

    lw $fp,28($sp)      # Restore frame pointer
    addiu $sp,$sp,32    # Restore the stack pointer
    jr $ra              # Return to caller

# Read the string value from the user through console.

spim.SPIM.readString:
    subu $sp,$sp,32     # Stack frame is 32 bytes long
    sw $fp,28($sp)      # Save frame pointer
    addu $fp,$sp,32     # Set up frame pointer

    li $v0,8            # Syscall code to read a string
    syscall             # Load the string value; $a0 = buffer, $a1 = length

    lw $fp,28($sp)      # Restore frame pointer
    addiu $sp,$sp,32    # Restore the stack pointer
    jr $ra              # Return to caller

# Read the char value from the user through console.

spim.SPIM.readChar:
    subu $sp,$sp,32     # Stack frame is 32 bytes long
    sw $fp,28($sp)      # Save frame pointer
    addu $fp,$sp,32     # Set up frame pointer

    li $v0,12           # Syscall code to read a char
    syscall             # Load the char value read from console into $a0

    lw $fp,28($sp)      # Restore frame pointer
    addiu $sp,$sp,32    # Restore the stack pointer
    jr $ra              # Return to caller

# Opens a file. This operation uses two arguments:
# $a0 = the address of the file name to open
# $a1 = flags  (0: read only, 1: write only, 2: read and write, 
#               100: create file, 8: append data)
# These registers are assumed to be set by the caller before calling this procedure

spim.SPIM.open:
    subu $sp,$sp,32     # Stack frame is 32 bytes long
    sw $fp,28($sp)      # Save frame pointer
    addu $fp,$sp,32     # Set up frame pointer

    li $v0,13           # System call code for open file
    syscall             # Open a file (file descriptor returned in $v0)

    lw $fp,28($sp)      # Restore frame pointer
    addiu $sp,$sp,32    # Restore the stack pointer
    jr $ra              # Return to caller

# Reads from a file. This operation uses three arguments:
# $a0 = file descriptor
# $a1 = the address of input buffer
# $a2 = the length of bytes to read
# These registers are assumed to be set by the caller before calling this procedure

spim.SPIM.read:
    subu $sp,$sp,32     # Stack frame is 32 bytes long
    sw $fp,28($sp)      # Save frame pointer
    addu $fp,$sp,32     # Set up frame pointer

    li $v0,14           # System call code for read file
    syscall             # Read from file ($a0 contains file descriptor, $v0 contains 
                        # number of character read)

    lw $fp,28($sp)      # Restore frame pointer
    addiu $sp,$sp,32    # Restore the stack pointer
    jr $ra              # Return to caller

# Writes to a file. This operation uses three arguments:
# $a0 = file descriptor
# $a1 = the address of output buffer
# $a2 = the length of bytes to write
# These registers are assumed to be set by the caller before calling this procedure

spim.SPIM.write:
    subu $sp,$sp,32     # Stack frame is 32 bytes long
    sw $fp,28($sp)      # Save frame pointer
    addu $fp,$sp,32     # Set up frame pointer

    li $v0,15           # System call for write to file
    syscall             # Write to file  ($a0 contains file descriptor)

    lw $fp,28($sp)      # Restore frame pointer
    addiu $sp,$sp,32    # Restore the stack pointer
    jr $ra              # Return to caller

# Close a file ($a0 contains file descriptor).

spim.SPIM.close:
    subu $sp,$sp,32     # Stack frame is 32 bytes long
    sw $fp,28($sp)      # Save frame pointer
    addu $fp,$sp,32     # Set up frame pointer

    li $v0,16           # System call code for close
    syscall             # Close file

    lw $fp,28($sp)      # Restore frame pointer
    addiu $sp,$sp,32    # Restore the stack pointer
    jr $ra              # Return to caller

# Exit SPIM.

spim.SPIM.exit:
    li $v0,10           # Syscall code to exit
    syscall

# Exit SPIM with a specified code (in $a0).

spim.SPIM.exit2:
    li $v0,17           # Syscall code to exit2
    syscall

