public class Calculator {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java Calculator <num1> <operation> <num2>");
            return;
        }
        double num1;
        double num2;
        try {
            num1 = Double.parseDouble(args[0]);
            num2 = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter numeric arguments!");
            return;
        }
        String operator = args[1];
        if (operator.equals("+")) {
            System.out.println("Result: " + (num1 + num2));
        } else if (operator.equals("-")) {
            System.out.println("Result: " + (num1 - num2));
        } else {
            System.out.println("Only '+' and '-' are supported in this calculator.");
        }
    }
}