// SerializationTest.java
import java.io.*;
import java.util.ArrayList;
import java.util.List;
// import Problem5.Student;

public class SerializationTest {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: java SerializationTest <output_file>");
            System.exit(1);
        }

        String outputFile = args[0];

        List<Student> students = createStudents();

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outputFile))) {
            oos.writeObject(students);
            System.out.println("Students serialized to " + outputFile);
        }
    }



    private static List<Student> createStudents() {
        List<Student> students = new ArrayList<>();
        students.add(new Student("Alice", "Jan 01, 1990", new Address("New York", "NY", 10001, "USA")));
        students.add(new Student("Bob", "Feb 15, 1992", new Address("Los Angeles", "CA", 90001, "USA")));
        students.add(new Student("Charlie", "Mar 20, 1994", new Address("Chicago", "IL", 60601, "USA")));
        students.add(new Student("David", "Apr 25, 1996", new Address("Houston", "TX", 77001, "USA")));


        return students;
    }
}