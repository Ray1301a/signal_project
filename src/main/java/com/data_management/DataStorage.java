package com.data_management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.alerts.AlertGenerator;

/**
 * Manages storage and retrieval of patient data within a healthcare monitoring
 * system.
 * This class serves as a repository for all patient records, organized by
 * patient IDs.
 */
public class DataStorage {
    private Map<Integer, Patient> patientMap; // Stores patient objects indexed by their unique patient ID.

    /**
     * Constructs a new instance of DataStorage, initializing the underlying storage
     * structure.
     */
    public DataStorage() {
        this.patientMap = new HashMap<>();
        /**
     * Returns the singleton instance of the DataStorage class.
     * 
     * @return the singleton instance of the DataStorage class
     */
    public static DataStorage getInstance(){
        if(instance == null){
            instance = new DataStorage();
        }
        return instance;
    }

    /**
     * Adds or updates patient data in the storage.
     * If the patient does not exist, a new Patient object is created and added to
     * the storage.
     * Otherwise, the new data is added to the existing patient's records.
     *
     * @param patientId        the unique identifier of the patient
     * @param measurementValue the value of the health metric being recorded
     * @param recordType       the type of record, e.g., "HeartRate",
     *                         "BloodPressure"
     * @param timestamp        the time at which the measurement was taken, in
     *                         milliseconds since the Unix epoch
     */
    public void addPatientData(int patientId, double measurementValue, String recordType, long timestamp) {
        Patient patient = patientMap.get(patientId);
        if (patient == null) {
            patient = new Patient(patientId);
            patientMap.put(patientId, patient);
        } else {
            // Check if there exists the record
            List<PatientRecord> records = patient.getRecords(timestamp, timestamp);
            for(int i = records.size()-1; i>=0; i--){
                if(records.get(i).getRecordType().equals(recordType) && records.get(i).getMeasurementValue() == measurementValue){
                    System.out.println("Record already exists");
                    return;
                }
            }
        }    
        patient.addRecord(measurementValue, recordType, timestamp);
    }

    /**
     * Retrieves a list of PatientRecord objects for a specific patient, filtered by
     * a time range.
     *
     * @param patientId the unique identifier of the patient whose records are to be
     *                  retrieved
     * @param startTime the start of the time range, in milliseconds since the Unix
     *                  epoch
     * @param endTime   the end of the time range, in milliseconds since the Unix
     *                  epoch
     * @return a list of PatientRecord objects that fall within the specified time
     *         range
     */
    public List<PatientRecord> getRecords(int patientId, long startTime, long endTime) {
        Patient patient = patientMap.get(patientId);
        if (patient != null) {
            return patient.getRecords(startTime, endTime);
        }
        return new ArrayList<>(); // return an empty list if no patient is found
    }
        public PatientRecord getRecord(int patientId, long timestamp) {
    Patient patient = patientMap.get(patientId);
    if (patient != null) {
        List<PatientRecord> patientRecords = patient.getRecords(timestamp, timestamp);
        if (!patientRecords.isEmpty()) {
            return patientRecords.get(0);
        }
    }
    return null; // return null if no patient or record is found
}

/**
 * Calculate the total number of records stored in the data storage
 * 
 * @return the total number of records
 */
   public int getTotalNumberOfRecords() {
        int totalCount = 0;
        for (Patient patient : patientMap.values()) {
        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE);
        totalCount += records.size();
        }
        return totalCount;
    }


    /**
     * Retrieves a collection of all patients stored in the data storage.
     *
     * @return a list of all patients
     */
    public List<Patient> getAllPatients() {
        return new ArrayList<>(patientMap.values());
    }
        
/**
 * Displays all patients and their records.
 */
public void displayAllPatients() {
    for (Patient patient : patientMap.values()) {
        for (PatientRecord record : patient.getRecords(0, Long.MAX_VALUE)) {
            System.out.printf("Patient ID: %d, Type: %s, Data: %.2f, Timestamp: %d%n", 
                              record.getPatientId(), 
                              record.getRecordType(), 
                              record.getMeasurementValue(), 
                              record.getTimestamp());
        }
    }
}

/**
 * Retrieves a patient object from the patientMap.
 * 
 * @param message the message containing the patient ID
 * @return the patient object
 */
public Patient retrievePatient(String message) {
    String[] parts = message.split(",");
    String[] patientInfo = parts[0].split(":");
    int patientId = Integer.parseInt(patientInfo[1].trim());

    return patientMap.computeIfAbsent(patientId, id -> new Patient(id));
}


    /**
     * The main method for the DataStorage class.
     * Initializes the system, reads data into storage, and continuously monitors
     * and evaluates patient data.
     * 
     * @param args command line arguments
     */
   /** public static void main(String[] args) {
        // DataReader is not defined in this scope, should be initialized appropriately.
        // DataReader reader = new SomeDataReaderImplementation("path/to/data");
        DataStorage storage = new DataStorage();

        // Assuming the reader has been properly initialized and can read data into the
        // storage
        // reader.readData(storage);

        // Example of using DataStorage to retrieve and print records for a patient
        List<PatientRecord> records = storage.getRecords(1, 1700000000000L, 1800000000000L);
        for (PatientRecord record : records) {
            System.out.println("Record for Patient ID: " + record.getPatientId() +
                    ", Type: " + record.getRecordType() +
                    ", Data: " + record.getMeasurementValue() +
                    ", Timestamp: " + record.getTimestamp());
        }

        // Initialize the AlertGenerator with the storage
        AlertGenerator alertGenerator = new AlertGenerator(storage);

        // Evaluate all patients' data to check for conditions that may trigger alerts
        for (Patient patient : storage.getAllPatients()) {
            alertGenerator.evaluateData(patient);
        }
    }*/
}
