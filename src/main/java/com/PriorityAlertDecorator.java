

import com.alerts.Alert;

/**
 * Represents a priority alert decorator.
 */
public class PriorityAlertDecorator extends AlertDecorator {
    
    private int priority;
    
    /**
     * Creates a priority alert decorator.
     * @param alertDecorated the alert to be decorated
     * @param priority the priority of the alert
     */
    public PriorityAlertDecorator(Alert alertDecorated, int priority) {
        super(alertDecorated);
        this.priority = priority;
    }

    /**
     * Returns the priority of the alert.
     * @return the priority of the alert
     */
    public int getPriority() {
        return priority;
    }
    
    /**
     * Sets the priority of the alert.
     * @param priority the new priority of the alert
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }
}

