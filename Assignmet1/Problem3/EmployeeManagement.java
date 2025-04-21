import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;



class Employee implements Comparable<Employee> {
    private String name;
    private String email;
    private int age;
    private Date dob;
    private int id;  

    private static int nextId = 1; 



    public Employee(String name, String email, int age, Date dob) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.dob = dob;
        this.id = nextId++;

    }



    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public Date getDob() { return dob; }
    public void setDob(Date dob) { this.dob = dob; }
    public int getId() { return id; }



    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return "ID: " + id + ", Name: " + name + ", Email: " + email + ", Age: " + age + ", DOB: " + dateFormat.format(dob);
    }


    @Override
    public int compareTo(Employee other) {
        return this.name.compareTo(other.name); 
    }


    public static void resetIdCounter() { nextId=1;}
}






public class EmployeeManagement {
    private static final String DATA_FILE = "employees.txt";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static List<Employee> employees = new ArrayList<>();

    public static void main(String[] args) {
        loadEmployeesFromFile(); 
        Employee.resetIdCounter();


        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Add Employee");
            System.out.println("2. Delete Employee");
            System.out.println("3. Search Employee");
            System.out.println("4. Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    addEmployee(scanner);
                    break;
                case 2:
                    deleteEmployee(scanner);
                    break;
                case 3:
                    searchEmployees(scanner);
                    break;
                case 4:
                    saveEmployeesToFile();
                    System.out.println("Exiting...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addEmployee(Scanner scanner) {

        try {
            System.out.print("Enter name: ");
            String name = scanner.nextLine();
            System.out.print("Enter email: ");
            String email = scanner.nextLine();
            System.out.print("Enter age: ");
            int age = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter DOB (yyyy-MM-dd): ");
            String dobString = scanner.nextLine();

            Date dob = DATE_FORMAT.parse(dobString);


            Employee employee = new Employee(name, email, age, dob);
            employees.add(employee);
            System.out.println("Employee added: " + employee);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
        }
    }


    private static void deleteEmployee(Scanner scanner) {

        System.out.print("Enter employee ID to delete: ");
        int idToDelete = scanner.nextInt();
        scanner.nextLine();



        employees.removeIf(employee -> employee.getId() == idToDelete);


        System.out.println("Employee with ID " + idToDelete + " deleted (if found).");
    }





    private static void searchEmployees(Scanner scanner) {
        System.out.print("Enter search query: ");
        String query = scanner.nextLine().toLowerCase();
        System.out.print("Sort by (name, email, age, dob): ");
        String sortBy = scanner.nextLine();
        System.out.print("Direction (asc/desc): ");
        String direction = scanner.nextLine();

        Predicate<Employee> searchPredicate = employee ->
                employee.getName().toLowerCase().contains(query) ||
                        employee.getEmail().toLowerCase().contains(query) ||
                        String.valueOf(employee.getAge()).contains(query) ||
                        DATE_FORMAT.format(employee.getDob()).contains(query);

        // Comparator for sorting
        Comparator<Employee> comparator = null;
        switch (sortBy) {
            case "name": comparator = Comparator.comparing(Employee::getName); break;
            case "email": comparator = Comparator.comparing(Employee::getEmail); break;
            case "age": comparator = Comparator.comparingInt(Employee::getAge); break;
            case "dob": comparator = Comparator.comparing(Employee::getDob); break;
            default: System.out.println("Invalid sort field."); return;
        }


        if (direction.equalsIgnoreCase("desc")) {
            comparator = comparator.reversed();
        }

        List<Employee> filteredEmployees = employees.stream()
                .filter(searchPredicate)
                .sorted(comparator)
                .collect(Collectors.toList());

        System.out.println("\nSearch Results:");
        if (filteredEmployees.isEmpty()) {
            System.out.println("No employees found matching the criteria.");
        } else {
            filteredEmployees.forEach(System.out::println);

        }
    }





    private static void loadEmployeesFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            Set<Employee> uniqueEmployees = new HashSet<>(); //Ensures that no duplicate data is added initially

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {

                    try {
                        String name = parts[0].trim();
                        String email = parts[1].trim();
                        int age = Integer.parseInt(parts[2].trim());
                        Date dob = DATE_FORMAT.parse(parts[3].trim());



                        Employee employee = new Employee(name, email, age, dob);
                        uniqueEmployees.add(employee);



                    } catch (NumberFormatException | ParseException e) {

                        System.err.println("Error parsing line: " + line + ". Skipping.");

                    }
                }


            }
            employees.clear(); //Remove any previous data in the employees collection

            employees.addAll(uniqueEmployees);




        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
    }






    private static void saveEmployeesToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE))) {
            for (Employee employee : employees) {
                writer.write(String.format("%s,%s,%d,%s%n", employee.getName(), employee.getEmail(), employee.getAge(), DATE_FORMAT.format(employee.getDob())));
            }

        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());

        }
    }
}