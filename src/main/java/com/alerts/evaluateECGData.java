package com.alerts;

import java.util.List;

import com.data_management.PatientRecord;


// Evaluate data to trigger ECG Data Alerts for abnormal peaks
    public class evaluateECGData {
    
        public static String evaluateECGData(List<PatientRecord> records) {
            double peakThreshold = 150; // Define your threshold for abnormal peaks
    
            for (PatientRecord record : records) {
                if (record.getRecordType().equals("ECGData") && record.getMeasurementValue() > peakThreshold) {
                    return "Abnormal ECG Peak Alert: Peak value exceeds threshold";
                }
            }
    
            return null;
        }
    
        // Evaluate data to trigger Triggered Alert
        public static String evaluateTriggeredAlert(boolean isTriggered) {
            if (isTriggered) {
                return "Triggered Alert: Nurse or patient triggered alert button";
            } else {
                return "Untriggered Alert: Alert button not triggered";
            }
        }
    }
        
    