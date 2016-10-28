package cms.core.services;

import cms.core.models.AcademicRecord;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Leonardo on 9/24/2016.
 */
public class AcademicRecordService {

    private static AcademicRecordService _instance;
    private Queue<AcademicRecord> records;

    private AcademicRecordService() {

        this.records = new ArrayDeque<>();
    }

    public static AcademicRecordService getInstance() {
        if (_instance == null) {
            _instance = new AcademicRecordService();
        }

        return _instance;
    }

    public boolean insert(AcademicRecord newRecord) {
        if (newRecord == null) {
            throw new IllegalArgumentException("Record cannot be null.");
        }

        boolean inserted = false;
        if (!this.exists(newRecord)) {
            this.records.add(newRecord);

            inserted = true;
        }

        return inserted;
    }

    public boolean update(AcademicRecord record) {
        if (record == null) {
            throw new IllegalArgumentException("Record cannot be null.");
        }

        boolean updated = false;
        for (AcademicRecord r : this.records) {
            if (r.getId() != record.getId()){
                continue;
            }

            updated = true;
            r = record;
        }
        return updated;
    }

    public List<AcademicRecord> getRecordsByStudent(String studentID) {
        if (studentID == null) {
            throw new IllegalArgumentException("Student ID cannot be null.");
        }

        // Search student records.
        List<AcademicRecord> studentRecords = new ArrayList<>();
        for (AcademicRecord r: this.records) {
            if (r.getStudent().getUUID().equals(studentID)){
                studentRecords.add(r);
            }
        }

        return studentRecords;
    }

    public Queue<AcademicRecord> getRecords(){
        return this.records;
    }

    private boolean exists(AcademicRecord record) {
        boolean exists = false;
        for (AcademicRecord r : this.records) {
            if (!r.equals(record) && r.getId() != record.getId()) {
                continue;
            }

            exists = true;
            break;
        }

        return exists;
    }
}


