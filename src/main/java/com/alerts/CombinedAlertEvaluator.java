package com.alerts;

import java.util.List;

import com.data_management.PatientRecord;

public class CombinedAlertEvaluator {

    // Evaluate data to trigger Hypotensive Hypoxemia Alert
    public static String evaluateHypotensiveHypoxemia(List<PatientRecord> records) {
        boolean lowBloodPressure = false;
        boolean lowBloodOxygenSaturation = false;

        for (PatientRecord record : records) {
            if (record.getRecordType().equals("SystolicPressure") && record.getMeasurementValue() < 90) {
                lowBloodPressure = true;
            } else if (record.getRecordType().equals("BloodSaturation") && record.getMeasurementValue() < 92) {
                lowBloodOxygenSaturation = true;
            }
        }

        if (lowBloodPressure && lowBloodOxygenSaturation) {
            return "Hypotensive Hypoxemia Alert: Low blood pressure and low blood oxygen saturation";
        }

        return null;
    }
}