public class Calculator2 {
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
        switch (operator) {
            case "+":
                System.out.println("Result: " +  (num1 + num2));
                break;
            case "-":
                System.out.println("Result: " +  (num1 - num2));
                break;
            case "*":
                System.out.println("Result: " +  (num1 * num2));
                break;
            case "/":
                if (num2 != 0) {
                    System.out.println("Result: " + (num1 / num2));
                } else {
                    System.out.println("Error: Cannot divide by zero!");
                }
                break;
            default:
                System.out.println("Supported operations are: +, -, *, /");
        }
    }
}
