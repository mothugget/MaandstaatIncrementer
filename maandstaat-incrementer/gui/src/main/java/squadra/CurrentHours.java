package squadra;

import java.util.HashMap;
import java.util.Map;

public class CurrentHours {

    // Inner class to store the data for each customer
    public static class CustomerData {
        private String description;
        private int hours;

        public CustomerData(String description, int hours) {
            this.description = description;
            this.hours = hours;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getHours() {
            return hours;
        }

        public void setHours(int hours) {
            this.hours = hours;
        }

        @Override
        public String toString() {
            return "Description: " + description + ", Hours: " + hours;
        }
    }

    // Map to store data per customer
    private Map<String, CustomerData> customerMap;

    public CurrentHours() {
        customerMap = new HashMap<>();
    }

    // Retrieve CustomerData; create new if not exists
    public CustomerData getCustomerData(String customer) {
        return customerMap.computeIfAbsent(customer, k -> new CustomerData("", 0));
    }

    // Optional: set data directly
    public void setCustomerData(String customer, String description, int hours) {
        customerMap.put(customer, new CustomerData(description, hours));
    }

    // Optional: check if customer exists
    public boolean hasCustomer(String customer) {
        return customerMap.containsKey(customer);
    }
}
