import java.util.Random;
import java.util.Stack;

public class SRPN {
    public Stack<Integer> stack; //Stack needed for SRPN calculator.
    public Random random; //Random needed for random numbers.

    public SRPN() {
        stack = new Stack<>();
        random = new Random();
    }

    public void processCommand(String command) {
        StringBuilder input = new StringBuilder();
        boolean isComment = false;

        for (int i = 0; i < command.length(); i++) {//Iterate over input string
            char ch = command.charAt(i); //Return character at each position
            
            switch (ch) {
                case '#': //Start of a comment. Ignore the rest of the line.
                    isComment = true;
                    break;

                case ' ': //Process input if space or newline is entered.
                case '\n':
                    if (input.length() > 0) {
                        stack.push(Integer.parseInt(input.toString())); // Convert input to integer & push to stack.
                        input.setLength(0); // Clear StringBuilder for the next input.
                    }
                    break;

                case '-': //Check if next character is a digit.
                    if (i + 1 < command.length() && Character.isDigit(command.charAt(i + 1))) {
                        input.append(ch); // Append '-' to make a negative number.
                    } else { //Process if there's already input.
                        if (input.length() > 0) {
                            processInput(ch);
                        } else {
                        	processInput(ch); // Process operator '-' case.
                        }
                    }
                    break;

                default:
                    if (isComment) {
                        continue; //Skip the rest of the comment.
                    } else if (Character.isDigit(ch)) {
                        input.append(ch);
                    } else { //Process operators in the input.
                        if (input.length() > 0) {
                            stack.push(Integer.parseInt(input.toString()));
                            input.setLength(0);
                        }
                        processInput(ch);
                    }
                    break;
            }
        }
        if (input.length() > 0) { //Process any remaining numbers.
            stack.push(Integer.parseInt(input.toString()));
        }
    }

    public void processInput(char input) { //Determine what operator has been entered & pass to relevant function.
    	
    	switch (input) {
            case '=':
                printResult();
                break;
            case 'd':
                printStack();
                break;
            case 'r':
                addRandomNumber();
                break;
            case '+':
            case '-':
            case '*':
            case '/':
            case '^':
            case '%':	
                calculateInput(String.valueOf(input));
                break;
            default:
                System.err.println("Unrecognised operator or operand " + input);
                break;
        }
    }

    public void calculateInput(String input) { //Perform the mathematical operation based on which operator was entered.
        if (stack.size() < 2) {
            System.err.println("Stack Underflow.");
            return;
        }
        
        int input_2 = stack.pop();
        int input_1 = stack.pop(); 
        int result; //Store the result of the calculation.
        
        switch (input) {
            case "+":
                result = overflowAdd(input_1, input_2);
                break;
            case "-":
                result = overflowSubtract(input_1, input_2);
                break;
            case "*":
                result = overflowMultiply(input_1, input_2);
                break;
            case "/":
                if (input_2 == 0) { //Catch division by 0.
                    System.err.println("Divide by 0."); 
                    stack.push(input_1);
                    stack.push(input_2);
                    return;
                }
                else{
                	result = input_1 / input_2;
                break;
                }
            case "^":
                if (input_2 < 0 || (input_1 == 0 && input_2 == 0)) { //No minus exponents to keep value as integer.
                    System.err.println("Error: Invalid exponent.");
                    return;
                }
                else{
                	result = (int) Math.pow(input_1, input_2);
                break;
                }
            case "%":
                if (input_2 == 0) {
                    System.err.println("Divide by 0.");
                    stack.push(input_1);
                    return;
                }
                else{
                	result = input_1 % input_2;
                break;
                }
            default:
                System.err.println("Unrecognised operator or operand " + input); //Allow user to re-enter operator.
                stack.push(input_1);
                stack.push(input_2);
                return;
        }
        stack.push(result);
    }

    public int overflowAdd(int input_1, int input_2) { //Add 2 inputs within integer range & prevent roll-over.
    	long result = (long) input_1 + input_2; //Convert to long to check if greater/lower than int max/min range.
    	if (result > Integer.MAX_VALUE) {
    	    return Integer.MAX_VALUE;
    	} 
    	else if (result < Integer.MIN_VALUE) {
    	    return Integer.MIN_VALUE;
    	}
    	    else if (result ==0) {
    	    	return 0;
    	} else {
    	    return (int) result; //Convert back to integer.
    	}
    }

    public int overflowSubtract(int input_1, int input_2) { //Subtracts 2 inputs within integer range & prevent roll-over.
    	    long result = (long) input_1 - input_2; //Convert to long to check if greater/lower than integer max/min range.
    	    if (result > Integer.MAX_VALUE) {
    	        return Integer.MAX_VALUE;
    	    } else if (result < Integer.MIN_VALUE) {
    	        return Integer.MIN_VALUE;
    	    } else {
    	        return (int) result; //Convert back to integer.
    	    }
    }

    public int overflowMultiply(int input_1, int input_2) { //Multiply 2 inputs within integer range & prevent roll-over.
        long result = (long) input_1 * input_2; //Convert to long to check if greater/lower than int max/min range.
        if (result > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        } else if (result < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        } else {
            return (int) result; //Convert back to integer.
        }
    }
    
    
    public void addRandomNumber() { //Add random integer to the stack
        int randomNumber = random.nextInt();
        stack.push(randomNumber);
    }

    public void printResult() { //Print result of calculation unless stack is empty.
        if (!stack.isEmpty()) {
            System.out.println(stack.peek());
            //stack.pop();
        } else {
            System.out.println("Stack is empty.");
        }
    }

    public void printStack() { //Print stack unless stack is empty.
        if (stack.isEmpty()) {
            System.out.println("Stack is empty.");
        } else {
        	System.out.println(stack);
        }
    }

}