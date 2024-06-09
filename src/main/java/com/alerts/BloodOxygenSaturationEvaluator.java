package com.alerts;


import java.util.List;

import com.alerts.Alert;
import com.data_management.PatientRecord;

public class BloodOxygenSaturationEvaluator {
    
    public static Alert evaluateBloodOxygenSaturation(List<PatientRecord> records) {
        double lowThreshold = 92.0;
        double rapidDropThreshold = 5.0;
        String lowAlertMessage = "Low Blood Oxygen Saturation Alert";
        String rapidDropAlertMessage = "Rapid Drop in Blood Oxygen Saturation Alert";

        for (PatientRecord record : records) {
            if (record.getRecordType().equals("BloodSaturation")) {
                double currentSaturation = record.getMeasurementValue();

                // Low Saturation alert
                if (currentSaturation < lowThreshold) {
                    return new Alert("Low Blood Oxygen Saturation Alert", lowAlertMessage, record.getTimestamp());
                }

                // Rapid Drop alert
                int currentIndex = records.indexOf(record);
                if (currentIndex >= 1) {
                    PatientRecord previousRecord = records.get(currentIndex - 1);
                    double previousSaturation = previousRecord.getMeasurementValue();
                    double drop = previousSaturation - currentSaturation;
                    if (drop >= rapidDropThreshold) {
                        return new Alert("Rapid Drop in Blood Oxygen Saturation Alert", rapidDropAlertMessage, record.getTimestamp());
                    }
                }
            }
        }
        
        return null;
    }
}
