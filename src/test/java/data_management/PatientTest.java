package data_management;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

public class PatientTest {

    private Patient patient;

    @BeforeEach
    public void setUp() {
        patient = new Patient(1);
        patient.addRecord(120, "BloodPressure", 1609459200000L); // 2021-01-01 00:00:00 UTC
        patient.addRecord(130, "BloodPressure", 1609545600000L); // 2021-01-02 00:00:00 UTC
        patient.addRecord(140, "BloodPressure", 1609632000000L); // 2021-01-03 00:00:00 UTC
    }

    @Test
    public void testGetRecordsWithinTimeRange() {
        List<PatientRecord> records = patient.getRecords(1609459200000L, 1609632000000L); // 2021-01-01 to 2021-01-03
        assertEquals(3, records.size(), "Should return 3 records");
    }

    @Test
    public void testGetRecordsOutsideTimeRange() {
        List<PatientRecord> records = patient.getRecords(1609718400000L, 1609804800000L); // 2021-01-04 to 2021-01-05
        assertTrue(records.isEmpty(), "Should return 0 records");
    }

    @Test
    public void testGetRecordsAtTimeBoundaries() {
        List<PatientRecord> records = patient.getRecords(1609545600000L, 1609632000000L); // 2021-01-02 to 2021-01-03
        assertEquals(2, records.size(), "Should return 2 records");
    }
}
