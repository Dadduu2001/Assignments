// DeserializationTest.java

import java.io.*;
import java.util.List;

public class DeserializationTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        if (args.length != 1) {
            System.err.println("Usage: java DeserializationTest <input_file>");
            System.exit(1);
        }

        String inputFile = args[0];


        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inputFile))) {

            List<Student> students = (List<Student>) ois.readObject();


            System.out.println("Deserialized Students from " + inputFile + ":");
            for (Student student : students) {
                System.out.println(student.firstName + ", " + student.dateOfBirth +
                                   ", " + student.address.city + ", " + student.address.state);
            }
        }




    }
}