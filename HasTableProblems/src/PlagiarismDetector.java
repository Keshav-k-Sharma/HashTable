import java.util.*;

/**
 * Problem 4: Plagiarism Detection System
 * Uses n-gram hashing to detect document similarity.
 *
 * Concepts:
 * HashMap<String, Set<String>>
 * n-gram extraction
 * similarity calculation
 *
 * @author Karthick
 * @version 1.0
 */

public class PlagiarismDetector {

    // n-gram -> set of document IDs
    private HashMap<String, Set<String>> ngramIndex = new HashMap<>();

    private int N = 5; // 5-word n-grams

    // Add document to index
    public void indexDocument(String documentId, String text) {

        List<String> ngrams = generateNgrams(text);

        for (String gram : ngrams) {

            ngramIndex.putIfAbsent(gram, new HashSet<>());
            ngramIndex.get(gram).add(documentId);
        }
    }

    // Generate n-grams
    private List<String> generateNgrams(String text) {

        List<String> result = new ArrayList<>();

        String[] words = text.toLowerCase().split("\\s+");

        for (int i = 0; i <= words.length - N; i++) {

            StringBuilder gram = new StringBuilder();

            for (int j = 0; j < N; j++) {
                gram.append(words[i + j]).append(" ");
            }

            result.add(gram.toString().trim());
        }

        return result;
    }

    // Analyze document similarity
    public void analyzeDocument(String documentId, String text) {

        List<String> ngrams = generateNgrams(text);

        HashMap<String, Integer> matchCounts = new HashMap<>();

        for (String gram : ngrams) {

            if (ngramIndex.containsKey(gram)) {

                for (String doc : ngramIndex.get(gram)) {

                    matchCounts.put(doc,
                            matchCounts.getOrDefault(doc, 0) + 1);
                }
            }
        }

        System.out.println("Total n-grams: " + ngrams.size());

        for (Map.Entry<String, Integer> entry : matchCounts.entrySet()) {

            double similarity =
                    (entry.getValue() * 100.0) / ngrams.size();

            System.out.println(
                    "Match with " + entry.getKey()
                            + " → " + entry.getValue()
                            + " matches → Similarity: "
                            + String.format("%.2f", similarity) + "%"
            );
        }
    }

    public static void main(String[] args) {

        PlagiarismDetector detector = new PlagiarismDetector();

        String doc1 = "Artificial intelligence is transforming the world of technology and innovation";
        String doc2 = "Artificial intelligence is transforming modern technology and innovation industries";

        detector.indexDocument("essay_001", doc1);

        System.out.println("Analyzing new document...");
        detector.analyzeDocument("essay_002", doc2);
    }
}