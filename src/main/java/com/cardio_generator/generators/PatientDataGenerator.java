package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;
/**
* declares method to generate a data
*/    


public interface PatientDataGenerator {
    /**
    * it generates data for the patient that gave her using the strategy that you chose
    * @param patientId patient identifier (integer)
    * @param outputStrategy strategy of the output data
    */
    void generate(int patientId, OutputStrategy outputStrategy);
}
