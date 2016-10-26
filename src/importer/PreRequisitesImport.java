package cms.importer;

import cms.core.models.Course;

import java.util.*;

/**
 * Created by Leonardo on 10/25/2016.
 */
public class PreRequisitesImport
        extends BaseImport {

    private Map<String, List<String>> coursePrerequisitesMap;
    private List<Course> coursesCatalog;

    public PreRequisitesImport(String importFilePath, List<Course> courses) {
        super(importFilePath);

        this.coursesCatalog = courses;
    }

    @Override
    public void process() {
        try {
            List<String> lines = this.readFile();
            this.coursePrerequisitesMap = new HashMap<>();

            for (String l : lines) {
                String[] values = l.split(",");
                if (values.length != 2) { continue; }

                // Extract PreRequisite Course Unique ID.
                String preReqId = values[0];
                String courseId = values[1];

                // Verify Courses exist in catalog.
                if (!this.courseExist(preReqId) || !this.courseExist(courseId)){
                    continue;
                }

                // If courseId already added.
                if (this.coursePrerequisitesMap.containsKey(courseId)){
                    boolean exist = false;
                    // Check if prereq already added to avoid dupes.
                    for (String preReq: this.coursePrerequisitesMap.get(courseId)) {
                        if (preReq.equals(preReqId)){
                            exist = true;
                            break;
                        }
                    }

                    // Making sure we have a distinct list of prerequisites.
                    if (!exist){
                        this.coursePrerequisitesMap.get(courseId).add(preReqId);
                    }

                }else{
                    this.coursePrerequisitesMap.put(courseId, new ArrayList<String>());
                    this.coursePrerequisitesMap.get(courseId).add(preReqId);
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public Map<String, List<Course>> getPreRequisitesMapping() {
        Map<String, List<Course>> m = new HashMap<>();

        for (Map.Entry<String, List<String>> kvp: this.coursePrerequisitesMap.entrySet()) {
            m.put(kvp.getKey(), new ArrayList<Course>());

            for (String cId: kvp.getValue()) {
                // Courses here have been already checked against existing.
                // this.getCourse() method should not return null for a given course Id in this step.
                m.get(kvp.getKey()).add(this.getCourse(cId));
            }
        }

        return m;
    }

    // Helper methods.
    private boolean courseExist(String courseId){
        return this.getCourse(courseId) != null;
    }

    private Course getCourse(String courseId){
        for (Course c: this.coursesCatalog) {
            if (c.getCourseId().equals(courseId)){
                return c;
            }
        }

        return null;
    }
}
