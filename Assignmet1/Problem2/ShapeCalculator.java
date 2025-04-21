import java.util.InputMismatchException;
import java.util.Scanner;

// --------------- Custom Exception Classes ---------------

class InvalidShapeParameterException extends Exception {
    public InvalidShapeParameterException(String message) {
        super(message);
    }
}

class InvalidDimensionException extends InvalidShapeParameterException {
    public InvalidDimensionException(String dimensionName, double value) {
        super("Invalid dimension: " + dimensionName + " cannot be " + value + ". It must be positive.");
    }
}


class InvalidTriangleException extends InvalidShapeParameterException {
    public InvalidTriangleException(String message) {
        super(message);
    }
}


// --------------- Shape Interface ---------------

interface Shape2D {
    double calculateArea();
    double calculatePerimeter(); 
    String getShapeName(); 
}

// --------------- Concrete Shape Classes ---------------

class Circle implements Shape2D {
    private final double radius;
    private final String SHAPE_NAME = "Circle";

    public Circle(double radius) throws InvalidDimensionException {
        if (radius <= 0) {
            throw new InvalidDimensionException("Radius", radius);
        }
        this.radius = radius;
    }

    @Override
    public double calculateArea() {
        return Math.PI * radius * radius;
    }

    @Override
    public double calculatePerimeter() {
        return 2 * Math.PI * radius;
    }

    @Override
    public String getShapeName() {
        return SHAPE_NAME;
    }
}

/**
 * Represents a Rectangle.
 */
class Rectangle implements Shape2D {
    private final double length;
    private final double width;
    private final String SHAPE_NAME = "Rectangle";

    public Rectangle(double length, double width) throws InvalidDimensionException {
        if (length <= 0) {
            throw new InvalidDimensionException("Length", length);
        }
        if (width <= 0) {
            throw new InvalidDimensionException("Width", width);
        }
        this.length = length;
        this.width = width;
    }

    @Override
    public double calculateArea() {
        return length * width;
    }

    @Override
    public double calculatePerimeter() {
        return 2 * (length + width);
    }

     @Override
    public String getShapeName() {
        return SHAPE_NAME;
    }
}

class Triangle implements Shape2D {
    private final double base;
    private final double height;
    private final double side1;
    private final double side2;
    private final double side3;
    private final String SHAPE_NAME = "Triangle";

    public Triangle(double base, double height, double side1, double side2, double side3) throws InvalidShapeParameterException {
        
        if (base <= 0) {
            throw new InvalidDimensionException("Base", base);
        }
        if (height <= 0) {
            throw new InvalidDimensionException("Height", height);
        }
     
        if (side1 <= 0) {
             throw new InvalidDimensionException("Side 1", side1);
        }
        if (side2 <= 0) {
            throw new InvalidDimensionException("Side 2", side2);
        }
        if (side3 <= 0) {
            throw new InvalidDimensionException("Side 3", side3);
        }

        if (!isValidTriangle(side1, side2, side3)) {
            throw new InvalidTriangleException(
                String.format("Invalid sides for a triangle: %.2f, %.2f, %.2f. The sum of any two sides must be greater than the third.", side1, side2, side3)
            );
        }

        this.base = base;
        this.height = height;
        this.side1 = side1;
        this.side2 = side2;
        this.side3 = side3;
    }

  
    private boolean isValidTriangle(double a, double b, double c) {
        return (a + b > c) && (a + c > b) && (b + c > a);
    }

    @Override
    public double calculateArea() {
        
        return 0.5 * base * height;
    }

    @Override
    public double calculatePerimeter() {
        
        return side1 + side2 + side3;
    }

    @Override
    public String getShapeName() {
        return SHAPE_NAME;
    }
}


// --------------- Main Application Class ---------------

public class ShapeCalculator {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            displayMenu();
            int choice = getUserChoice();

            try {
                Shape2D shape = createShape(choice);
                if (shape != null) {
                    displayResults(shape);
                } else if (choice != 0) { 
                     System.out.println("Invalid shape choice. Please try again.");
                }
                // If choice was 0 or invalid, the loop continues anyway

            } catch (InvalidShapeParameterException e) {
                System.err.println("Error creating shape: " + e.getMessage());
            } catch (InputMismatchException e) {
                System.err.println("Invalid input. Please enter a number.");
                scanner.nextLine(); 
            } catch (Exception e) { 
                System.err.println("An unexpected error occurred: " + e.getMessage());
                e.printStackTrace(); 
                 scanner.nextLine(); 
            }

            System.out.println("\n----------------------------------------\n"); 
        }
       
    }

    private static void displayMenu() {
        System.out.println("Select a shape to calculate its Area and Perimeter:");
        System.out.println("1. Circle");
        System.out.println("2. Rectangle");
        System.out.println("3. Triangle");
        System.out.print("Enter your choice (number): ");
    }


    private static int getUserChoice() {
        int choice = -1; 
        try {
            choice = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.err.println("Invalid input. Please enter a number.");
            scanner.nextLine(); 
            
        } finally {
             if (scanner.hasNextLine()) {
                scanner.nextLine();
             }
        }
        return choice;
    }

    private static double getDoubleInput(String prompt) {
        double value = -1;
        while (true) { 
            System.out.print(prompt);
            try {
                value = scanner.nextDouble();
                scanner.nextLine(); 
                return value;
            } catch (InputMismatchException e) {
                System.err.println("Invalid input. Please enter a number (e.g., 10.5).");
                scanner.nextLine(); 
            }
        }
    }



    private static Shape2D createShape(int choice) throws InvalidShapeParameterException, InputMismatchException {
        switch (choice) {
            case 1: // Circle
                System.out.println("--- Circle ---");
                double radius = getDoubleInput("Enter the radius: ");
                return new Circle(radius);

            case 2: // Rectangle
                System.out.println("--- Rectangle ---");
                double length = getDoubleInput("Enter the length: ");
                double width = getDoubleInput("Enter the width: ");
                return new Rectangle(length, width);

            case 3: // Triangle
                System.out.println("--- Triangle ---");
                System.out.println("Enter dimensions for Area calculation:");
                double base = getDoubleInput("Enter the base: ");
                double height = getDoubleInput("Enter the height: ");
                System.out.println("Enter dimensions for Perimeter calculation:");
                double side1 = getDoubleInput("Enter length of side 1: ");
                double side2 = getDoubleInput("Enter length of side 2: ");
                double side3 = getDoubleInput("Enter length of side 3: ");
                return new Triangle(base, height, side1, side2, side3);

            default:
                return null; // Invalid choice handled in main loop
        }
    }


    private static void displayResults(Shape2D shape) {
        System.out.println("\n--- Results for " + shape.getShapeName() + " ---");
        System.out.printf("Area: %.2f\n", shape.calculateArea());
        if (shape instanceof Circle) {
             System.out.printf("Circumference: %.2f\n", shape.calculatePerimeter());
        } else {
             System.out.printf("Perimeter: %.2f\n", shape.calculatePerimeter());
        }
    }
}