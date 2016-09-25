package cms.core.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leonardo on 9/19/2016.
 */
public class Student
        extends User {

    private Address address;
    private List<WorkSchedule> workHours;
    private boolean isOnline;

    public Student(String firstName, String lastName, String email, String phone, boolean isOnline) {
        super(firstName, lastName, email, phone);

        this.isOnline = isOnline;
        this.workHours = new ArrayList<WorkSchedule>();
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

        if (index == -1) { return; }
        this.workHours.remove(index);
    }
}
