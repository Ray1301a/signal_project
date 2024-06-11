package com.cardio_generator.outputs;

import java.net.URI;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import com.alerts.AlertManager;
import com.data_management.DataParser;
import com.data_management.DataStorage;
import com.data_management.Patient;

/**
 * Represents a WebSocket client that receives data from a WebSocket server.
 */
public class WebSocketClient extends WebSocketClient {
    private final DataStorage dataStorage;
    private final AlertManager alertManager;

    /**
     * Creates a WebSocketClient instance.
     * 
     * @param serverUri    the URI of the WebSocket server
     * @param dataStorage  the DataStorage object
     * @param alertManager the AlertManager object
     */
    public WebSocketClient(URI serverUri, DataStorage dataStorage, AlertManager alertManager) {
        super(serverUri);
        this.dataStorage = dataStorage;
        this.alertManager = alertManager;
    }

    /**
     * Called when the connection to the WebSocket server is established.
     */
    @Override
    public void onOpen(ServerHandshake handshakeData) {
        System.out.println("Connected to WebSocket server successfully.");
    }

    /**
     * Called when a message is received from the WebSocket server.
     * Parses the data, stores it, and evaluates it for alerts.
     */
    @Override
    public void onMessage(String message) {
        DataParser parser = new DataParser();
        try {
            System.out.println("Received message: " + message);
            parser.readData(dataStorage, message);
            System.out.println("Data parsed and stored successfully.");

            Patient patient = dataStorage.getPatient(message);
            alertManager.evaluateData(patient, dataStorage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when the connection to the WebSocket server is closed.
     */
    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection to WebSocket server closed: " + reason);
    }

    /**
     * Called when an error occurs during the connection to the WebSocket server.
     */
    @Override
    public void onError(Exception ex) {
        System.out.println("An error occurred: " + ex.getMessage());
    }
}
