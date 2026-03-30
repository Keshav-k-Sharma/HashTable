import java.util.*;

/**
 * Problem 7: Autocomplete System
 * Suggests queries based on prefix and popularity.
 *
 * Author: Karthick
 * Version: 1.0
 */
public class AutocompleteSystem {

    private HashMap<String, Integer> frequencyMap = new HashMap<>();

    // Add or update a query
    public void updateFrequency(String query) {
        frequencyMap.put(query, frequencyMap.getOrDefault(query, 0) + 1);
    }

    // Get top N suggestions for a prefix
    public List<String> search(String prefix, int topN) {
        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>((a, b) -> a.getValue().equals(b.getValue()) ?
                        b.getKey().compareTo(a.getKey()) :
                        a.getValue() - b.getValue());

        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            if (entry.getKey().startsWith(prefix)) {
                pq.offer(entry);
                if (pq.size() > topN) pq.poll();
            }
        }

        List<String> result = new ArrayList<>();
        while (!pq.isEmpty()) result.add(pq.poll().getKey());
        Collections.reverse(result);
        return result;
    }

    public static void main(String[] args) {
        AutocompleteSystem ac = new AutocompleteSystem();

        // Simulate searches
        ac.updateFrequency("java tutorial");
        ac.updateFrequency("javascript");
        ac.updateFrequency("java tutorial");
        ac.updateFrequency("java 21 features");
        ac.updateFrequency("java 21 features");
        ac.updateFrequency("java 21 features");
        ac.updateFrequency("javascript");
        ac.updateFrequency("java download");

        System.out.println("Top suggestions for 'jav':");
        List<String> suggestions = ac.search("jav", 10);
        for (String s : suggestions) System.out.println(s);
    }
}