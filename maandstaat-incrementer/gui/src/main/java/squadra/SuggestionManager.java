package squadra;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class SuggestionManager {
    private Map<String, String[]> customerSuggestionsMap;


    public Map<String, String[]>  getCustomerSuggestions() {
        return customerSuggestionsMap;
    }

    public String[] getSuggestions(String customer){
        return customerSuggestionsMap.get(customer);
    }

    public void setSuggestions(String customer,String[] suggestions) {
        String[] cleanAndOrderedSuggestions=cleanAndOrderSuggestions(suggestions);
        customerSuggestionsMap.put(customer,cleanAndOrderedSuggestions);
    }

    private String[] cleanAndOrderSuggestions(String[] suggestions){
        ArrayList<String> cleanSuggestions= new ArrayList(); 
        for (String suggestion:suggestions){
            if (!suggestion.equals("")){
                cleanSuggestions.add(suggestion);
            }
        }
        String[] sortedSuggestions=cleanSuggestions.toArray(new String[0]);
         Arrays.sort(sortedSuggestions);
        return sortedSuggestions;
    }

}
