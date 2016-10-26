package cms.importer;

import cms.core.models.Address;
import cms.core.models.Instructor;
import cms.core.models.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leonardo on 9/24/2016.
 */
public class InstructorImport  extends BaseImport {

    private List<Instructor> instructors;

    public InstructorImport(String importFilePath) {
        super(importFilePath);
    }

    @Override
    public void process() {
        try {
            List<String> lines = this.readFile();
            this.instructors = new ArrayList<>();

            for (String l : lines) {
                String[] values = l.split(",");
                if (values.length != 4) {
                    continue;
                }

                // Extract Instructor Unique ID.
                String id = values[0];

                // Extract Instructor Name.
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

                // Extract Instructor Address.
                Address address = this.buildAddress( values[2]);

                // Extract Instructor Phone.
                String phoneNumber = "";
                if (values[3].length() == 10) {
                    phoneNumber = values[3].trim();
                }

                Instructor instructor = new Instructor(firstName, lastName, "", phoneNumber, id);
                instructor.setAddress(address);
                this.instructors.add(instructor);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public List<Instructor> getInstructors() {
        return this.instructors;
    }
}
