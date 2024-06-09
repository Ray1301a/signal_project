package com.data_management;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class FileDataReader implements DataReader {
    private String dataDirectory;

    /**
     * Constructs a FileDataReader with the specified data directory.
     * 
     * @param dataDirectory the directory where data files are located
     */
    public FileDataReader(String dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    /**
     * Reads data from files in the specified directory and stores it in the data storage.
     * 
     * @param dataStorage the storage where data will be stored
     * @throws IOException if there is an error reading the data
     */
    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        // List all files in the directory
        List<Path> files = Files.list(Path.of(dataDirectory)).toList();

        // Iterate through each file
        for (Path file : files) {
            // Process each file
            processDataFile(file, dataStorage);
        }
    }

    /**
     * Processes a single data file and adds its contents to the data storage.
     * 
     * @param file the path to the data file
     * @param dataStorage the storage where data will be stored
     * @throws IOException if there is an error reading the file
     */
    private void processDataFile(Path file, DataStorage dataStorage) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
            String line;
            // Read each line of the file
            while ((line = reader.readLine()) != null) {
                // Split the line into components
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    // Extract components
                    int patientId = Integer.parseInt(parts[0]);
                    long timestamp = Long.parseLong(parts[1]);
                    String label = parts[2];
                    String data = parts[3];

                    // Add data to the storage
                    dataStorage.addPatientData(patientId, Double.parseDouble(data), label, timestamp);
                } else {
                    System.err.println("Invalid data format in file: " + file.getFileName());
                }
            }
        }
    }
}
