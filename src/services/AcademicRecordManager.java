package services;

import models.AcademicRecord;
import models.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leonardo on 9/24/2016.
 */
public class AcademicRecordManager {

    private static AcademicRecordManager _instance;
    private List<AcademicRecord> records;

    private AcademicRecordManager() {
        this.records = new ArrayList<AcademicRecord>();
    }

    public static AcademicRecordManager getInstance() {
        if (_instance == null) {
            _instance = new AcademicRecordManager();
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

    public List<AcademicRecord> getRecordsByStudents(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null.");
        }

        // Search student records.
        List<AcademicRecord> studentRecords = new ArrayList<AcademicRecord>();
        for (AcademicRecord r: this.records) {
            if (r.getStudent().getUUID() == student.getUUID()){
                studentRecords.add(r);
            }
        }

        return studentRecords;
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


