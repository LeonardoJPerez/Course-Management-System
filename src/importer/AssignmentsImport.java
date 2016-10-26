package cms.importer;

import cms.core.models.SeatAssignment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Leonardo on 10/26/2016.
 */
public class AssignmentsImport
        extends BaseImport{

    Map<String, HashMap<String, Integer>> seatAssigmentsMap;

    public AssignmentsImport(String importFilePath) {
        super(importFilePath);
    }

    @Override
    public void process() {
        try {
            List<String> lines = this.readFile();
            this.seatAssigmentsMap = new HashMap<>();

            for (String l : lines) {
                String[] values = l.split(",");
                if (values.length != 3) { continue; }

                // Extract PreRequisite Course Unique ID.
                String instructorId = values[0];
                String courseId = values[1];
                Integer seatCount = Integer.parseInt(values[2]);

                if (this.seatAssigmentsMap.containsKey(courseId)){
                    boolean exist = false;
                    for (Map.Entry<String, Integer> kvp: this.seatAssigmentsMap.get(courseId).entrySet()) {
                        if (kvp.getKey() == instructorId){
                            kvp.setValue(kvp.getValue() + seatCount);
                            exist = true;
                            break;
                        }
                    }

                    if (!exist){
                        this.seatAssigmentsMap.get(courseId).put(instructorId, seatCount);
                    }
                }else{
                    this.seatAssigmentsMap.put(courseId, new HashMap<String, Integer>());
                    this.seatAssigmentsMap.get(courseId).put(instructorId, seatCount);
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public List<SeatAssignment> getSeatAssignmentMap() {
        List<SeatAssignment> assignments = new ArrayList<>();
        for (Map.Entry<String, HashMap<String, Integer>> courseMap: this.seatAssigmentsMap.entrySet()) {
            for (Map.Entry<String, Integer> kvp: courseMap.getValue().entrySet()) {
                assignments.add(new SeatAssignment(courseMap.getKey(), kvp.getKey(), kvp.getValue()));
            }
        }
        return assignments;
    }
}
