// Student.java
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
// java.time.LocalDate;
import java.util.Locale;

public class Student implements Serializable {
    private static final long serialVersionUID = 1L; // Important for versioning

    public String firstName;
    public LocalDate dateOfBirth;
    public Address address;



    public Student(String firstName, String dateOfBirth, Address address) {
        this.firstName = firstName;
        //  = new SimpleDateFormat.getDateInstance().format(dateOfBirth); // Use single-parameter Date constructor
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH);
        LocalDate date = LocalDate.parse(dateOfBirth, formatter);
        this.dateOfBirth = date;
        this.address = address;
    }
}











