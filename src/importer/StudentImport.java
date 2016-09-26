package cms.importer;

import cms.core.models.Address;
import cms.core.models.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Leonardo on 9/24/2016.
 */
public class StudentImport extends BaseImport {

    private List<Student> students;

    public StudentImport(String importFilePath) {
        super(importFilePath);
    }

    @Override
    public void process() {
        try {
            List<String> lines = this.readFile();
            this.students = new ArrayList<Student>();

            for (String l : lines) {
                if (l.length() <= 1) {
                    continue;
                }

                String[] values = l.split(",");
                if (values.length != 4) {
                    continue;
                }

                // Extract Student Unique ID.
                String id = values[0];

                // Extract Student Name.
                String name = values[1];
                String firstName;
                String lastName = "";

                if (name.split(" ").length > 1) {
                    String[] splitted = name.split(" ");
                    firstName = splitted[0].trim();
                    lastName = splitted[1].trim();
                } else {
                    firstName = name;
                }

                // Extract Student Address.
                Address address = this.buildAddress( values[2]);

                // Extract Student Phone.
                String phoneNumber = "";
                if (values[3].length() == 10) {
                    phoneNumber = values[3].trim();
                }

                Student newStudent = new Student(firstName, lastName, "", phoneNumber, true, id);
                newStudent.setAddress(address);
                this.students.add(newStudent);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public List<Student> getStudents() {
        return this.students;
    }
}
