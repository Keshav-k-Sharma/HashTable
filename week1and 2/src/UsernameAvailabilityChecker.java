import java.util.*;

/**
 * Social Media Username Availability Checker
 * Demonstrates HashMap usage for O(1) lookup
 */
public class UsernameAvailabilityChecker {

    // Existing usernames
    private HashMap<String, Integer> usernameToUserId = new HashMap<>();

    // Track attempt frequency
    private HashMap<String, Integer> usernameAttempts = new HashMap<>();

    public UsernameAvailabilityChecker() {
        // Preloaded users (simulate database)
        usernameToUserId.put("john_doe", 101);
        usernameToUserId.put("admin", 102);
        usernameToUserId.put("alex99", 103);
    }

    // Check username availability
    public boolean checkAvailability(String username) {

        // track attempts
        usernameAttempts.put(username,
                usernameAttempts.getOrDefault(username, 0) + 1);

        return !usernameToUserId.containsKey(username);
    }

    // Suggest alternative usernames
    public List<String> suggestAlternatives(String username) {

        List<String> suggestions = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            String suggestion = username + i;

            if (!usernameToUserId.containsKey(suggestion)) {
                suggestions.add(suggestion);
            }
        }

        suggestions.add(username.replace("_", "."));

        return suggestions;
    }

    // Get most attempted username
    public String getMostAttempted() {

        String mostAttempted = null;
        int maxAttempts = 0;

        for (Map.Entry<String, Integer> entry : usernameAttempts.entrySet()) {

            if (entry.getValue() > maxAttempts) {
                mostAttempted = entry.getKey();
                maxAttempts = entry.getValue();
            }
        }

        return mostAttempted + " (" + maxAttempts + " attempts)";
    }

    // Test the system
    public static void main(String[] args) {

        UsernameAvailabilityChecker checker =
                new UsernameAvailabilityChecker();

        System.out.println("checkAvailability(\"john_doe\") → "
                + checker.checkAvailability("john_doe"));

        System.out.println("checkAvailability(\"jane_smith\") → "
                + checker.checkAvailability("jane_smith"));

        System.out.println("Suggestions for john_doe → "
                + checker.suggestAlternatives("john_doe"));

        // simulate attempts
        checker.checkAvailability("admin");
        checker.checkAvailability("admin");
        checker.checkAvailability("admin");

        System.out.println("Most Attempted → "
                + checker.getMostAttempted());
    }
}
