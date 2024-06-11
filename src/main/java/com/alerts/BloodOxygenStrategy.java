package com.alerts.AlertStrategy;

import com.alerts.AlertGenerator;
import com.alerts.AlertFactory.BloodOxygenAlertConcreteCreator;
import com.data_management.PatientRecord;
import java.util.List;

/**
 * Represents an alert strategy for blood oxygen levels.
 */
public class BloodOxygenStrategy implements AlertStrategy {

    private BloodOxygenAlertConcreteCreator bloodOxygenAlertConcreteCreator;
    private AlertGenerator alertGenerator;

    /**
     * Creates a blood oxygen strategy.
     * @param alertGenerator the alert generator
     */
    public BloodOxygenStrategy(AlertGenerator alertGenerator) {
        this.alertGenerator = alertGenerator;
        this.bloodOxygenAlertConcreteCreator = new BloodOxygenAlertConcreteCreator();
    }

    /**
     * Checks if an alert should be triggered based on the given measurement and patient records.
     * @param measurement the current measurement value
     * @param patientRecords the list of patient records
     */
    @Override
    public void checkAlert(double measurement, List<PatientRecord> patientRecords) {
        evaluateSaturation(measurement, patientRecords);
    }

    /**
     * Evaluates the saturation level of the patient and triggers alerts if necessary.
     * @param measurement the current measurement value
     * @param patientRecords the list of patient records
     */
    private void evaluateSaturation(double measurement, List<PatientRecord> patientRecords) {
        if (patientRecords.isEmpty()) {
            return;
        }

        PatientRecord latestRecord = patientRecords.get(patientRecords.size() - 1);
        String patientId = Integer.toString(latestRecord.getPatientId());
        long latestTimestamp = latestRecord.getTimestamp();

        // Threshold check for low saturation
        if (measurement < 92) {
            checkSystolicPressureAndTriggerAlert(patientRecords, patientId, latestTimestamp, measurement);
        }

        // Check for significant drop in saturation over the last 10 minutes
        checkForDecreasingTrend(patientRecords, patientId, latestTimestamp, measurement);
    }

    /**
     * Checks the last recorded systolic pressure and triggers an appropriate alert if the saturation is too low.
     * @param patientRecords the list of patient records
     * @param patientId the patient ID
     * @param latestTimestamp the timestamp of the latest measurement
     * @param measurement the current measurement value
     */
    private void checkSystolicPressureAndTriggerAlert(List<PatientRecord> patientRecords, String patientId, long latestTimestamp, double measurement) {
        boolean systolicPressureTooLow = false;

        for (int i = patientRecords.size() - 2; i >= 0; i--) {
            PatientRecord record = patientRecords.get(i);
            if (record.getRecordType().equals("SystolicPressure")) {
                double systolicPressure = record.getMeasurementValue();
                if (systolicPressure < 90) {
                    systolicPressureTooLow = true;
                }
                break;
            }
        }

        if (systolicPressureTooLow) {
            alertGenerator.triggerAlert(bloodOxygenAlertConcreteCreator.createAlert(patientId, "Critical Threshold Alert - Hypotensive Hypoxemia Alert", latestTimestamp));
        } else {
            alertGenerator.triggerAlert(bloodOxygenAlertConcreteCreator.createAlert(patientId, "Critical Threshold Alert - Saturation too low", latestTimestamp));
        }
    }

    /**
     * Checks for a significant drop in saturation over a 10-minute window and triggers an alert if detected.
     * @param patientRecords the list of patient records
     * @param patientId the patient ID
     * @param latestTimestamp the timestamp of the latest measurement
     * @param measurement the current measurement value
     */
    private void checkForDecreasingTrend(List<PatientRecord> patientRecords, String patientId, long latestTimestamp, double measurement) {
        for (int i = patientRecords.size() - 2; i >= 0; i--) {
            PatientRecord record = patientRecords.get(i);
            if (record.getRecordType().equals("Saturation") && record.getTimestamp() >= latestTimestamp - (10 * 60 * 1000)) {
                if (record.getMeasurementValue() >= measurement + 5) {
                    alertGenerator.triggerAlert(bloodOxygenAlertConcreteCreator.createAlert(patientId, "Decreasing Trend Alert in Saturation", record.getTimestamp()));
                    break;
                }
            }
        }
    }
}

