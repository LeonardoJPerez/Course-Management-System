package cms.importer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Leonardo on 10/25/2016.
 */
public class PreRequisitesImport
        extends BaseImport {

    Map<String, List<String>> courseAndPrerequisitesMap;

    public PreRequisitesImport(String importFilePath) {
        super(importFilePath);
    }

    @Override
    public void process() {
        try {
            List<String> lines = this.readFile();
            this.courseAndPrerequisitesMap = new HashMap<>();

            for (String l : lines) {
                String[] values = l.split(",");
                if (values.length != 2) { continue; }

                // Extract PreRequisite Course Unique ID.
                String preReqId = values[0];
                String courseId = values[1];

                if (this.courseAndPrerequisitesMap.containsKey(courseId)){
                    boolean exist = false;
                    for (String preReq: this.courseAndPrerequisitesMap.get(courseId)) {
                        if (preReq == preReqId){
                            exist = true;
                            break;
                        }
                    }

                    // Making sure we have a distinct list of prerequisites.
                    if (!exist){
                        this.courseAndPrerequisitesMap.get(courseId).add(preReqId);
                    }

                }else{
                    this.courseAndPrerequisitesMap.put(courseId, new ArrayList<String>());
                    this.courseAndPrerequisitesMap.get(courseId).add(preReqId);
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public Map<String, List<String>> getPreRequisitesMapping() {
        return this.courseAndPrerequisitesMap;
    }
}
