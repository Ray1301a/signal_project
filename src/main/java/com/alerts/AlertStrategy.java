package com.alerts;

import java.util.List; 

/**
 * Represents an abstract alert strategy
 * @param <PatientRecord>
 */
public interface AlertStrategy<PatientRecord> {

    public void checkAlert(double measurement,List<PatientRecord> patientRecords);
    
}

