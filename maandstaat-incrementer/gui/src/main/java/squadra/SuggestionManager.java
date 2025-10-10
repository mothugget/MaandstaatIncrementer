package squadra;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SuggestionManager {
    private final Map<String, String[]> customerSuggestionsMap;

    public SuggestionManager() {
        customerSuggestionsMap = new HashMap<>();
    }

    public String getCustomerSuggestionsJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String customerSuggestionJson=gson.toJson(customerSuggestionsMap);
        return customerSuggestionJson;
    }
    public String[] getSuggestions(String customer) {

        return customerSuggestionsMap.get(customer);
    }

    public void setSuggestions(String customer, String[] suggestions) {
        String[] cleanAndOrderedSuggestions = cleanAndOrderSuggestions(suggestions);
        customerSuggestionsMap.put(customer, cleanAndOrderedSuggestions);
    }

    public boolean doesCustomerExist(String customer) {
        return customerSuggestionsMap.containsKey(customer);
    }

    private String[] cleanAndOrderSuggestions(String[] suggestions) {
        ArrayList<String> cleanSuggestions = new ArrayList();
        for (String suggestion : suggestions) {
            if (!suggestion.equals("") && !cleanSuggestions.contains(suggestion)) {
                cleanSuggestions.add(suggestion);
            }
        }
        String[] sortedSuggestions = cleanSuggestions.toArray(new String[0]);
        Arrays.sort(sortedSuggestions);
        return sortedSuggestions;
    }

}
