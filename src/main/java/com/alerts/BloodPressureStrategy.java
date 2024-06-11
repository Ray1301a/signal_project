package com.alerts;
import com.data_management.PatientRecord;
import java.util.List; 

/**
 * Represents an alert strategy for blood pressure
 * @param <PatientRecord>
 */
public class BloodPressureStrategy<PatientRecord> implements AlertStrategy{

    private AlertGenerator alertGenerator;

    /**
     * Create a blood pressure strategy
     * @param alertGenerator alert generator
     */
    public BloodPressureStrategy(AlertGenerator alertGenerator){
        this.alertGenerator = alertGenerator;
    }
    
    /**
     * Check if an alert should be triggered
     * @param measurement measurement value of the alert
     * @param patientRecord patient record
     */
    @Override
    public void checkAlert(double measurement, List<PatientRecord> patientRecord) {
        evaluateDiastolicPressure(measurement, patientRecord);
        evaluateDiastolicPressure(measurement, patientRecord);
    }

    private void evaluateDiastolicPressure(double measurement, List<com.alerts.PatientRecord> patientRecord) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'evaluateDiastolicPressure'");
    }
    /**
    * Assess the patient's diastolic pressure and generate alerts if the pressure is abnormally low or high,
    * or if there is a consistent decreasing or increasing trend over three successive measurements.
    * @param measurement the measured value triggering the alert
    * @param patientRecord the patient's record containing relevant data
    */
    public void evaluateDiastolicPressure(Double measurement, List<PatientRecord> patientRecord){

        PatientRecord record = patientRecord.get(patientRecord.size()-1);
        String patientId = Integer.toString(record.getPatientId());
        long timeStamp = record.getTimestamp();

        boolean decreaseInDP = false;
        boolean increaseInDP = false;


        //Treshold check
        if (measurement < 60){
            alertGenerator.triggerAlert(new BloodPressureAlert(patientId, "Critical Treshold Alert - Diastolic Pressure too low", timeStamp));
        } else if(measurement > 120){
            alertGenerator.triggerAlert(new BloodPressureAlert(patientId, "Critical Treshold Alert - Diastolic Pressure too high", timeStamp));
        }
        //verify if there is a decrease/increase in the measurements over three consecutive measurements
        //if there already was a decrease/increase reported, then it is considered a trend and an alert is triggered
        for(int i = patientRecord.size()-2; i >= 0; i--){

            if(patientRecord.get(i).getRecordType().equals("DiastolicPressure")){

                PatientRecord previousRecord = patientRecord.get(i);
                double previousmeasurement = previousRecord.getMeasurementValue();
                if(measurement < previousmeasurement + 10){
                    if(increaseInDP){//if there was an increase in the diastolic pressure before the decrease, then there is no decrease/increase trend
                        return;
                    } else if(!decreaseInDP){
                        decreaseInDP = true;
                    } else {
                        alertGenerator.triggerAlert(new BloodPressureAlert(patientId, "Decreasing Trend Alert in Diastolic Pressure", timeStamp));
                        return;
                    }
                }
                else if (measurement > previousmeasurement -10){
                    if(decreaseInDP){//if there was a decrease in the diastolic pressure before the increase, then there is no decrease/increase trend
                        return;
                    }else if(!increaseInDP){
                        increaseInDP = true;
                    } else {
                        alertGenerator.triggerAlert(new BloodPressureAlert(patientId, "Increasing Trend Alert in Diastolic Pressure", timeStamp));
                        return;
                    }
                } else{
                    return;
                }
                measurement = previousmeasurement;
            }
        }
        
    }
