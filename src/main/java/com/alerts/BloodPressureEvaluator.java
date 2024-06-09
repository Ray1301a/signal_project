package com.alerts;

import java.util.List;

import com.alerts.Alert;
import com.data_management.PatientRecord;

public class BloodPressureEvaluator {
    
    public static Alert evaluateBloodPressure(List<PatientRecord> records, String type) {
        int consecutiveReadings = 3;
        double thresholdChange = 10.0;
        double highThreshold = 180.0;
        double lowThreshold = 90.0;
        String highAlertMessage = "High Blood Pressure Alert";
        String lowAlertMessage = "Low Blood Pressure Alert";

        double sum = 0;
        for (PatientRecord record : records) {
            if (record.getRecordType().equals(type)) {
                sum += record.getMeasurementValue();
            }
        }

        if (records.size() >= consecutiveReadings) {
            double average = sum / records.size();
            double lastReading = records.get(records.size() - 1).getMeasurementValue();
            double previousReading = records.get(records.size() - 2).getMeasurementValue();
            double secondPreviousReading = records.get(records.size() - 3).getMeasurementValue();

            // Trend Alert
            if (Math.abs(lastReading - previousReading) > thresholdChange &&
                Math.abs(previousReading - secondPreviousReading) > thresholdChange) {
                return new Alert("Trend Alert", "Blood pressure trend detected", records.get(records.size() - 1).getTimestamp());
            }

            // High and Low Blood Pressure Alerts
            if (lastReading > highThreshold) {
                return new Alert("High Blood Pressure Alert", highAlertMessage, records.get(records.size() - 1).getTimestamp());
            } else if (lastReading < lowThreshold) {
                return new Alert("Low Blood Pressure Alert", lowAlertMessage, records.get(records.size() - 1).getTimestamp());
            }
        }
        
        return null;
    }
}
