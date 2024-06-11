package com.alerts.AlertStrategy;

import com.alerts.AlertGenerator;
import com.alerts.AlertFactory.ECGAlertConcreteCreator;
import com.data_management.PatientRecord;
import java.util.List;

/**
 * Represents an ECG alert strategy
 */
public class ECGStrategy implements AlertStrategy {

    private ECGAlertConcreteCreator ecgAlertConcreteCreator;
    private AlertGenerator alertGenerator;

    /**
     * Creates an ECG strategy
     * @param alertGenerator alert generator
     */
    public ECGStrategy(AlertGenerator alertGenerator) {
        this.alertGenerator = alertGenerator;
        this.ecgAlertConcreteCreator = new ECGAlertConcreteCreator();
    }

    /**
     * Checks if an alert should be triggered
     * @param measurement measurement value of the alert
     * @param patientRecords list of patient records
     */
    @Override
    public void checkAlert(double measurement, List<PatientRecord> patientRecords) {
        evaluateECG(patientRecords);
    }

    /**
     * Evaluates the ECG of the patient and triggers alerts if heart rate is too low or too high,
     * or if there is an abnormal trend in the measurements over five consecutive measurements
     * @param patientRecords list of patient records
     */
    private void evaluateECG(List<PatientRecord> patientRecords) {
        PatientRecord latestRecord = patientRecords.get(patientRecords.size() - 1);
        String patientId = Integer.toString(latestRecord.getPatientId());
        int irregularBpmCount = 0;

        for (int k = patientRecords.size() - 2; k >= 0; k--) {
            PatientRecord previousRecord = patientRecords.get(k);

            if (previousRecord.getRecordType().equals("ECG")) {
                double bpm = calculateBpm(latestRecord.getTimestamp(), previousRecord.getTimestamp());

                // Threshold checks
                if (bpm < 50) {
                    alertGenerator.triggerAlert(ecgAlertConcreteCreator.createAlert(patientId, "Critical Threshold Alert - Heart Rate too low", latestRecord.getTimestamp()));
                } else if (bpm > 100) {
                    alertGenerator.triggerAlert(ecgAlertConcreteCreator.createAlert(patientId, "Critical Threshold Alert - Heart Rate too high", latestRecord.getTimestamp()));
                }

                // Check for abnormal trend over five consecutive measurements
                if (isAbnormalTrend(patientRecords, k, bpm)) {
                    alertGenerator.triggerAlert(ecgAlertConcreteCreator.createAlert(patientId, "Trend Alert - Abnormal Heart Rate", previousRecord.getTimestamp()));
                    return;
                }

                latestRecord = previousRecord;
            }
        }
    }

    /**
     * Calculates the bpm based on the timestamps of two records
     * @param timestamp1 the first timestamp
     * @param timestamp2 the second timestamp
     * @return the calculated bpm
     */
    private double calculateBpm(long timestamp1, long timestamp2) {
        return (60.0 / Math.abs(timestamp1 - timestamp2)) * 1000;
    }

    /**
     * Checks for an abnormal trend in the measurements over five consecutive measurements
     * @param patientRecords list of patient records
     * @param startIndex the index to start checking from
     * @param initialBpm the initial bpm to compare against
     * @return true if there is an abnormal trend, false otherwise
     */
    private boolean isAbnormalTrend(List<PatientRecord> patientRecords, int startIndex, double initialBpm) {
        int irregularBpmCount = 0;
        double bpm = initialBpm;

        for (int i = startIndex - 1; i >= 0; i--) {
            PatientRecord record = patientRecords.get(i);

            if (record.getRecordType().equals("ECG")) {
                double previousBpm = calculateBpm(record.getTimestamp(), patientRecords.get(i + 1).getTimestamp());

                // Check for irregular bpm
                if (Math.abs(bpm - previousBpm) >= 10) {
                    irregularBpmCount++;
                } else {
                    irregularBpmCount = Math.max(0, irregularBpmCount - 1);
                }

                // Trigger alert if there is a trend
                if (irregularBpmCount >= 5) {
                    return true;
                }

                bpm = previousBpm;
            }
        }

        return false;
    }
}
