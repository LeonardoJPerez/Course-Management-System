package cms.core.models;

import java.text.MessageFormat;
import java.util.UUID;

/**
 * Created by Leonardo on 9/23/2016.
 */
public class Instructor
        extends User {

    private Address address;

    public Instructor(String firstName, String lastName, String email, String phone, String id) {
        super(firstName, lastName, email, phone, id);
    }

    @Override
    public String toString() {
        MessageFormat fmt;
        Object[] values;
        if (this.address == null) {
            fmt = new MessageFormat("[{0}] {1} - Phone: {2} - Online: {3}");
            values = new Object[]{this.getUUID(), this.getFullName(), this.getPhone()};
        } else {
            fmt = new MessageFormat("[{0}] {1} - Address: {2} - Phone: {3} - Online: {4}");
            values = new Object[]{this.getUUID(), this.getFullName(), this.getAddress().getFullAddress(), this.getPhone()};
        }

        return fmt.format(values);
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return this.address;
    }
}
