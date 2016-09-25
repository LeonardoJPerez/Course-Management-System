package cms.core.models;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Leonardo on 9/19/2016.
 */
public class Student
        extends User {

    private Address address;
    private List<WorkSchedule> workHours;
    private boolean isOnline;

    public Student(String firstName, String lastName, String email, String phone, boolean isOnline, String id) {
        super(firstName, lastName, email, phone, id);

        this.isOnline = isOnline;
        this.workHours = new ArrayList<WorkSchedule>();
    }

    @Override
    public String toString() {
        MessageFormat fmt;
        Object[] values;
        if (this.address == null) {
            fmt = new MessageFormat("[{0}] {1} - Phone: {2} - Online: {3}");
            values = new Object[]{this.getUUID(), this.getFullName(), this.getPhone(), this.isOnline()};
        } else {
            fmt = new MessageFormat("[{0}] {1} - Address: {2} - Phone: {3} - Online: {4}");
            values = new Object[]{this.getUUID(), this.getFullName(), this.getAddress().getFullAddress(), this.getPhone(), this.isOnline()};
        }

        return fmt.format(values);
    }


    public void setAddress(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return this.address;
    }

    public boolean isOnline() {
        return this.isOnline;
    }

    public List<WorkSchedule> getWorkHours() {
        return this.workHours;
    }

    public void enroll() {

    }

    public void disenroll() {

    }

    public void addWorkSchedule(WorkSchedule newWorkSchedule) {

        if (newWorkSchedule == null || this.workHours.size() == 7) {
            return;
        }

        for (int i = 0; i < this.workHours.size(); i++) {
            WorkSchedule w = this.workHours.get(i);

            if (w.getDay() == newWorkSchedule.getDay() ||
                    w.getFromHour() == newWorkSchedule.getFromHour() ||
                    w.getToHour() == newWorkSchedule.getToHour()) {
                // If new schedule matches an existing one, grab the index and break.
                w = newWorkSchedule;

                return;
            }
        }

        // Insert if no schedule with similar values are found.
        this.workHours.add(newWorkSchedule);
    }

    public void removeWorkSchedule(WorkSchedule newWorkSchedule) {
        if (newWorkSchedule == null || this.workHours.size() == 0) {
            return;
        }

        int index = -1;
        for (int i = 0; i < this.workHours.size(); i++) {
            WorkSchedule w = this.workHours.get(i);

            if (w.getDay() == newWorkSchedule.getDay() ||
                    w.getFromHour() == newWorkSchedule.getFromHour() ||
                    w.getToHour() == newWorkSchedule.getToHour()) {
                // If new schedule matches an existing one, grab the index and break.
                index = i;
                break;
            }
        }

        if (index == -1) {
            return;
        }
        this.workHours.remove(index);
    }
}
