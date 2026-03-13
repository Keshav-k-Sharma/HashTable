import java.util.*;

/**
 * Problem 10: Multi-Level Cache System with Hash Tables
 *
 * Author: Karthick
 * Version: 1.0
 */
class VideoData {
    String videoId;
    String content;

    VideoData(String videoId, String content) {
        this.videoId = videoId;
        this.content = content;
    }
}

public class MultiLevelCache {

    private LinkedHashMap<String, VideoData> L1;
    private HashMap<String, VideoData> L2;
    private HashMap<String, VideoData> L3;

    private int L1Capacity = 10000;
    private int L2Capacity = 100000;

    private int L1Hits = 0, L2Hits = 0, L3Hits = 0;

    public MultiLevelCache() {
        L1 = new LinkedHashMap<>(L1Capacity, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, VideoData> eldest) {
                return size() > L1Capacity;
            }
        };
        L2 = new HashMap<>();
        L3 = new HashMap<>();
    }

    // Add video to L3 (database)
    public void addToDatabase(VideoData video) {
        L3.put(video.videoId, video);
    }

    public VideoData getVideo(String videoId) {

        // L1 check
        if (L1.containsKey(videoId)) {
            L1Hits++;
            return L1.get(videoId);
        }

        // L2 check
        if (L2.containsKey(videoId)) {
            L2Hits++;
            VideoData video = L2.get(videoId);
            promoteToL1(video);
            return video;
        }

        // L3 check
        if (L3.containsKey(videoId)) {
            L3Hits++;
            VideoData video = L3.get(videoId);
            addToL2(video);
            promoteToL1(video);
            return video;
        }

        return null; // video not found
    }

    private void promoteToL1(VideoData video) {
        L1.put(video.videoId, video);
    }

    private void addToL2(VideoData video) {
        if (L2.size() >= L2Capacity) {
            Iterator<String> it = L2.keySet().iterator();
            if (it.hasNext()) L2.remove(it.next());
        }
        L2.put(video.videoId, video);
    }

    public void getStatistics() {
        int total = L1Hits + L2Hits + L3Hits;
        double hitRate = total == 0 ? 0 : (double) (L1Hits + L2Hits + L3Hits) / total * 100;
        System.out.println("Cache Hits - L1: " + L1Hits + ", L2: " + L2Hits + ", L3: " + L3Hits);
        System.out.println("Overall Hit Rate: " + String.format("%.2f", hitRate) + "%");
    }

    public static void main(String[] args) {

        MultiLevelCache cache = new MultiLevelCache();

        // Populate L3 database
        cache.addToDatabase(new VideoData("video_123", "Content A"));
        cache.addToDatabase(new VideoData("video_999", "Content B"));

        System.out.println("Access video_123 first time:");
        cache.getVideo("video_123"); // L3 hit → promote to L1 & L2

        System.out.println("Access video_123 second time:");
        cache.getVideo("video_123"); // L1 hit

        System.out.println("Access video_999 first time:");
        cache.getVideo("video_999"); // L3 hit → promote

        cache.getStatistics();
    }
}