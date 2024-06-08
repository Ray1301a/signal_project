package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Strategy that creates files with a patient's data
 * 
 * The class implements the interface OutputStrategy
 */

public class FileOutputStrategy implements OutputStrategy {

    // Changed variable name to camelCase
    private String baseDirectory;
    // Changed variable name to camelCase (file_map to fileMap)
    public final ConcurrentHashMap<String, String> fileMap = new ConcurrentHashMap<>();
    /**
     * Constructs a new FileOutputStrategy with the specified base directory.
     * @param baseDirectory the base directory where files will be created
     */
    public FileOutputStrategy(String baseDirectory) {
    // Changed variable name to camelCase
        this.baseDirectory = baseDirectory;
    }

     /**
     * Outputs the data to the file 
     *
     * @param patientId identifier (integer) of the patient
     * @param timestamp timestamp of the data
     * @param label label of the data
     * @param data data associated with the patient
     */

    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create the directory
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        // Set the FilePath variable
        // Changed variable name to camelCase
        String filePath = fileMap.computeIfAbsent(label, k -> Paths.get(baseDirectory, label + ".txt").toString());

        // Write the data to the file
        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
        } catch (Exception e) {
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }
}
