package utility;

import java.io.Serializable;

/**
 * A class used to store Addresses.
 *
 * @author Samuel Kosasih
 */
public class Address implements Serializable {

    /**
     * The street address
     */
    private final String street;
    /**
     * The city name
     */
    private final String city;
    /**
     * The state name
     */
    private final String state;
    /**
     * A 5-Digit zip code
     */
    private final String zipCode;

    /**
     * Validity of Address
     */
    private boolean valid = true;

    /**
     * Default Constructor.
     *
     * @param address a whole String representing the address
     */
    public Address(String address) {
        String[] a = address.split(",");
        for (int i = 0; i < a.length; i++) {
            a[i] = a[i].trim();
        }
        if (a.length == 4) {
            this.street = a[0];
            if (isValidString(a[1]) && a[1] != null) {
                this.city = a[1];
            } else {
                this.city = "Invalid City";
                valid = false;
            }
            if (isValidString(a[2])) {
                this.state = a[2];
            } else {
                this.state = "Invalid State";
                valid = false;
            }
            if (checkDigits(a[3])) {
                this.zipCode = a[3];
            } else {
                this.zipCode = "Invalid Zip Code";
                valid = false;
            }
        } else {
            this.street = "Invalid Street";
            this.city = "Invalid City";
            this.state = "Invalid State";
            this.zipCode = "00000";
            valid = false;
        }
    }

    /**
     * Overloaded Constructor. Each and every part of the address is entered
     * individually.
     *
     * @param street  the street
     * @param city    the city
     * @param state   the state
     * @param zipCode the 5-Digit zip code
     */
    public Address(String street, String city, String state, String zipCode) {
        this.street = street.trim();
        if (isValidString(city)) {
            this.city = city.trim();
        } else {
            this.city = "Invalid City";
            valid = false;
        }
        if (isValidString(state)) {
            this.state = state.trim();
        } else {
            this.state = "Invalid State";
            valid = false;
        }
        if (checkDigits(zipCode)) {
            this.zipCode = zipCode.trim();
        } else {
            this.zipCode = "00000";
            valid = false;
        }
    }

    /**
     * Returns the validity check of the Address.
     *
     * @return true if Address is valid
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * A String representation of the address in the form:
     * <br>Street<br>City, State, Zip Code<br>
     * If Address is invalid, it will return "Invalid Address" instead.
     *
     * @return a String representation
     */
    @Override
    public String toString() {
        if (isValid()) {
            return street + ", " + city + ", " + state + " " + zipCode;
        } else {
            return "Invalid Address";
        }
    }

    /**
     * A helper method to check whether a given String has 5 digits.
     *
     * @param s the given String
     * @return true if given String has 5 digits
     */
    private boolean checkDigits(String s) {
        if (s == null) {
            return false;
        }
        String[] chars = s.split("");
        return chars.length == 5;
    }

    /**
     * A helper method to check whether a given String is alphabetic only.
     *
     * @param s the given String
     * @return true if the given String has only alphabets
     */
    private boolean isValidString(String s) {
        if (s == null) {
            return false;
        }
        char[] chars = s.toCharArray();
        for (char c : chars) {
            if (Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

}
