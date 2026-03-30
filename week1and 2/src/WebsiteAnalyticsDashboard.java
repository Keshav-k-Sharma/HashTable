import java.util.*;

/**
 * Problem 5: Real-Time Analytics Dashboard
 * Tracks page views, unique visitors, and traffic sources.
 *
 * Concepts:
 * HashMap, HashSet, PriorityQueue
 * Real-time analytics simulation
 *
 * Author: Karthick
 * Version: 1.0
 */
public class WebsiteAnalyticsDashboard {

    private HashMap<String, Integer> pageViews = new HashMap<>();
    private HashMap<String, Set<String>> uniqueVisitors = new HashMap<>();
    private HashMap<String, Integer> trafficSources = new HashMap<>();

    // Process a page view event
    public void processEvent(String url, String userId, String source) {

        // Update page views
        pageViews.put(url, pageViews.getOrDefault(url, 0) + 1);

        // Update unique visitors
        uniqueVisitors.putIfAbsent(url, new HashSet<>());
        uniqueVisitors.get(url).add(userId);

        // Update traffic source
        trafficSources.put(source, trafficSources.getOrDefault(source, 0) + 1);
    }

    // Get top N pages
    public List<String> getTopPages(int N) {

        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>((a, b) -> a.getValue() - b.getValue());

        for (Map.Entry<String, Integer> entry : pageViews.entrySet()) {

            pq.offer(entry);

            if (pq.size() > N) pq.poll();
        }

        List<String> result = new ArrayList<>();

        while (!pq.isEmpty()) {
            Map.Entry<String, Integer> e = pq.poll();
            result.add(e.getKey() + " - " + e.getValue() + " views (" +
                    uniqueVisitors.get(e.getKey()).size() + " unique)");
        }

        Collections.reverse(result);
        return result;
    }

    // Display traffic sources
    public void displayTrafficSources() {

        System.out.println("Traffic Sources:");
        for (Map.Entry<String, Integer> e : trafficSources.entrySet()) {
            System.out.println(e.getKey() + ": " + e.getValue());
        }
    }

    public static void main(String[] args) {

        WebsiteAnalyticsDashboard dashboard = new WebsiteAnalyticsDashboard();

        // simulate events
        dashboard.processEvent("/article/breaking-news", "user_123", "Google");
        dashboard.processEvent("/article/breaking-news", "user_456", "Facebook");
        dashboard.processEvent("/sports/championship", "user_789", "Direct");
        dashboard.processEvent("/article/breaking-news", "user_123", "Google");
        dashboard.processEvent("/sports/championship", "user_101", "Google");

        System.out.println("Top Pages:");
        List<String> topPages = dashboard.getTopPages(2);
        for (String page : topPages) System.out.println(page);

        dashboard.displayTrafficSources();
    }
}