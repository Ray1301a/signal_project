package com.alerts;

import com.alerts.Alert;

/**
 * Represents a repeated alert decorator
 */
public class RepeatedAlertDecorator extends AlertDecorator {

    private int repeatCheck;
    private long timeInterval;
    private int repeatCount;

    /**
     * Creates a repeated alert decorator.
     * @param alertDecorated the alert to be decorated
     * @param timeInterval the time interval between repeated alerts in milliseconds
     * @param repeatCheck the number of times to repeat the alert
     */
    public RepeatedAlertDecorator(Alert alertDecorated, long timeInterval, int repeatCheck) {
        super(alertDecorated);
        this.repeatCheck = repeatCheck;
        this.timeInterval = timeInterval;
        this.repeatCount = 0;
    }

    /**
     * Repeats the alert for the specified number of times over the given time interval.
     */
    public void repeatTheAlert() {
        for (int i = 0; i < repeatCheck; i++) {
            repeatAlert();
            repeatCount++;
            sleep();
        }
    }

    /**
     * Returns the number of times the alert has been repeated.
     * @return the repeat count
     */
    public int getRepeatCount() {
        return repeatCount;
    }

    /**
     * Repeats the alert by printing the alert details.
     */
    private void repeatAlert() {
        System.out.println("Repeating Alert: " + getCondition() + " for patient " + getPatientId() + " " + (System.currentTimeMillis() - getTimestamp()) + "ms ago");
    }

    /**
     * Sleeps for the specified time interval between repeats.
     */
    private void sleep() {
        try {
            Thread.sleep(timeInterval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

