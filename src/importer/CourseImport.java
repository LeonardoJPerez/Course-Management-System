package cms.importer;

import cms.core.enumerations.CourseType;
import cms.core.enumerations.SemesterName;
import cms.core.models.Address;
import cms.core.models.Course;
import cms.core.models.Semester;
import cms.core.models.Student;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Leonardo on 9/24/2016.
 */
public class CourseImport extends BaseImport {

    private List<Semester> semesters;

    public CourseImport(String importFilePath) {
        super(importFilePath);

        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);

        semesters = new ArrayList<Semester>();
        semesters.add(new Semester(SemesterName.Fall, year));
        semesters.add(new Semester(SemesterName.Winter, year));
        semesters.add(new Semester(SemesterName.Spring, year));
        semesters.add(new Semester(SemesterName.Summer, year));
        semesters.add(new Semester(SemesterName.NoSemester, year));
    }

    @Override
    public void process() {
        try {
            List<String> lines = this.readFile();
            for (String l : lines) {
                if (l.length() < 1) {
                    continue;
                }

                String[] values = l.split(",");
                if (values.length < 2){
                    // Minimum amount of data required not met.
                    continue;
                }

                // Extract Course Unique ID.
                String id = values[0];

                // Extract Course name
                String name = values[1];

                Course newCourse = new Course(name, CourseType.Online, null, id);
                // Extract semesters
                // Check for no semester
                if (values.length == 2){
                    InsertSemester(SemesterName.NoSemester, newCourse);
                }else {
                    for ( int i = 2; i < values.length; i++){
                        switch (values[i].toLowerCase()){
                            case "fall":
                                InsertSemester(SemesterName.Fall, newCourse);
                                break;
                            case "winter":
                                InsertSemester(SemesterName.Winter, newCourse);
                                break;
                            case "spring":
                                InsertSemester(SemesterName.Spring, newCourse);
                                break;
                            case "summer":
                                InsertSemester(SemesterName.Summer, newCourse);
                                break;
                            default:
                                InsertSemester(SemesterName.NoSemester, newCourse);
                                break;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void InsertSemester(SemesterName semesterName, Course newCourse) {
        int length = this.semesters.size();
        for (int i = 0; i < length; i++)
        {
            if (this.semesters.get(i).getSemesterName() != semesterName){ continue; }

            Semester s = this.semesters.get(i);
            s.addCourse(newCourse);
            this.semesters.set(i, s);
        }
    }

    public List<Semester> getCourses() {
        return this.semesters;
    }
}
