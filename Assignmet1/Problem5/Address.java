// Address.java
import java.io.Serializable;

class Address implements Serializable {

    public String city;
    public String state;
    public int pinCode;
    public String country;

    public Address(String city, String state, int pinCode, String country) {
        this.city = city;
        this.state = state;
        this.pinCode = pinCode;
        this.country = country;
    }
}
