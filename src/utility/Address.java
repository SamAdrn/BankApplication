package utility;

import java.io.Serializable;

/**
 * This class represents an address.<br><br>
 * This class provides methods to simulate a real-life address, complete with the street name, city,
 * state, and a 5-Digit zip code.<br><br>
 * <b>Note that this class only supports U.S addresses</b>
 *
 * @author Samuel A. Kosasih
 *
 * @see bank.Customer
 * @see bank.Branch
 */
public class Address implements Serializable {

    /**
     * This field stores the street address as a <code>String</code>
     */
    private final String STREET;

    /**
     * This field stores the city name as a <code>String</code>
     */
    private final String CITY;

    /**
     * This field stores the state name as a <code>String</code>
     */
    private final String STATE;

    /**
     * This field stores a 5-Digit zip code as a <code>String</code>
     */
    private final String ZIP_CODE;

    /**
     * This field stores a <code>Boolean</code> to indicate that an address is valid.
     */
    private boolean valid = true;

    /**
     * Default Constructor.<br><br>
     * Recommended input for the address:
     * <br>
     * <blockquote>Street,City,State,Zip</blockquote>
     * Use commas (,) to separate each of them, as the constructor will split the <code>address</code> using
     * a comma delimiter.<br><br>
     * An invalid address format will be accepted, but will save the whole address as <em>Invalid Address</em>.
     *
     * @param address the whole address as a <code>String</code>
     */
    public Address(String address) {
        String[] a = address.split(",");
        for (int i = 0; i < a.length; i++) {
            a[i] = a[i].trim();
        }
        if (a.length == 4) {
            this.STREET = a[0];
            if (isValidString(a[1]) && a[1] != null) {
                this.CITY = a[1];
            } else {
                this.CITY = "";
                valid = false;
            }
            if (isValidString(a[2])) {
                this.STATE = a[2];
            } else {
                this.STATE = "";
                valid = false;
            }
            if (checkDigits(a[3])) {
                this.ZIP_CODE = a[3];
            } else {
                this.ZIP_CODE = "";
                valid = false;
            }
        } else {
            this.STREET = "";
            this.CITY = "";
            this.STATE = "";
            this.ZIP_CODE = "";
            valid = false;
        }
    }

    /**
     * Overloaded Constructor.<br><br>
     * Allows every part of the address to be entered individually.
     *
     * @param street  the street as a <code>String</code>
     * @param city    the city as a <code>String</code>
     * @param state   the state as a <code>String</code>
     * @param zipCode the 5-Digit zip code as a <code>String</code>
     */
    public Address(String street, String city, String state, String zipCode) {
        this.STREET = street.trim();
        if (isValidString(city)) {
            this.CITY = city.trim();
        } else {
            this.CITY = "";
            valid = false;
        }
        if (isValidString(state)) {
            this.STATE = state.trim();
        } else {
            this.STATE = "";
            valid = false;
        }
        if (checkDigits(zipCode)) {
            this.ZIP_CODE = zipCode.trim();
        } else {
            this.ZIP_CODE = "";
            valid = false;
        }
    }

    /**
     * Returns the validity of the address.
     *
     * @return <code>true</code> if address is valid, otherwise <code>false</code>
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Retrieves only the street name from the address.<br><br>
     * If address is not <code>valid</code>, then will return an empty <code>String</code>
     *
     * @return the street name as a <code>String</code>
     */
    public String getStreet() {
        return STREET;
    }

    /**
     * Retrieves only the city name from the address.<br><br>
     * If address is not <code>valid</code>, then will return an empty <code>String</code>
     *
     * @return the city name as a <code>String</code>
     */
    public String getCity() {
        return CITY;
    }

    /**
     * Retrieves only the name of the state from the address.<br><br>
     * If address is not <code>valid</code>, then will return an empty <code>String</code>
     *
     * @return the state as a <code>String</code>
     */
    public String getState() {
        return STATE;
    }

    /**
     * Retrieves only the 5-Digit zip code from the address.<br><br>
     * If address is not <code>valid</code>, then will return an empty <code>String</code>
     *
     * @return the zip code as a <code>String</code>
     */
    public String getZipCode() {
        return ZIP_CODE;
    }

    /**
     * Provides a <code>String</code> representation of the address in the form:
     * <br>
     * <blockquote>Street<br>City, State, Zip Code</blockquote>
     * If address is invalid, it will return <em>Invalid Address</em> instead.
     *
     * @return the representation of the address as a <code>String</code>
     */
    @Override
    public String toString() {
        if (isValid()) {
            return STREET + ", " + CITY + ", " + STATE + " " + ZIP_CODE;
        } else {
            return "Invalid Address";
        }
    }

    /**
     * Verifies whether a <code>String</code> is comprised of only 5 characters.
     *
     * @param s the <code>String</code> to be verified
     * @return <code>true</code> if criteria is met, otherwise <code>false</code>
     */
    private boolean checkDigits(String s) {
        if (s == null) {
            return false;
        }
        String[] chars = s.split("");
        return chars.length == 5;
    }

    /**
     * Verifies whether a <code>String</code> does not have any numerical characters at all.
     *
     * @param s the <code>String</code> to be verified
     * @return <code>true</code> if criteria is met, otherwise <code>false</code>
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
