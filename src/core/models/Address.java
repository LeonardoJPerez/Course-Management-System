package cms.core.models;

/**
 * Created by Leonardo on 9/19/2016.
 */
public class Address {

    protected String addressLine1;
    protected String addressLine2;
    protected String city;
    protected String state;
    protected String zipCode;
    protected String country;

    public Address(String addressLine1, String city, String state, String zipCode, String country) {
        this.addressLine1 = addressLine1;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
    }

    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.addressLine1);

        if (this.addressLine2 != null && this.addressLine2.length() > 0) {
            sb.append(" " + this.addressLine2);
        }

        if (this.city != null && this.city.length() > 0) { sb.append(", " + this.city); }
        if (this.state != null && this.state.length() > 0) { sb.append(" " + this.state); }
        if (this.country != null && this.country.length() > 0) { sb.append(" " + this.country); }
        if (this.zipCode != null && this.zipCode.length() > 0) { sb.append(" " + this.zipCode); }

        return sb.toString();
    }

    // Setters - Getters
    public void setAddressLine1(String addressLine) {
        this.addressLine1 = addressLine;
    }

    public String getAddressLine1() {
        return this.addressLine1;
    }

    public void setAddressLine2(String addressLine) {
        this.addressLine2 = addressLine;
    }

    public String getAddressLine2() {
        return this.addressLine2;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return this.city;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return this.country;
    }
}
