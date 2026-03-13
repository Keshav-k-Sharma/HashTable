import java.util.*;

/**
 * Problem 9: Two-Sum Problem Variants for Financial Transactions
 *
 * Author: Karthick
 * Version: 1.0
 */
class Transaction {
    int id;
    int amount;
    String merchant;
    String timestamp;

    Transaction(int id, int amount, String merchant, String timestamp) {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        this.timestamp = timestamp;
    }
}

public class TwoSumFinancial {

    // Classic two-sum
    public static List<int[]> findTwoSum(List<Transaction> transactions, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        List<int[]> result = new ArrayList<>();

        for (Transaction t : transactions) {
            int complement = target - t.amount;
            if (map.containsKey(complement)) {
                result.add(new int[]{map.get(complement), t.id});
            }
            map.put(t.amount, t.id);
        }
        return result;
    }

    // Detect duplicates: same amount, same merchant
    public static List<String> detectDuplicates(List<Transaction> transactions) {
        Map<String, List<Integer>> map = new HashMap<>();
        List<String> duplicates = new ArrayList<>();

        for (Transaction t : transactions) {
            String key = t.amount + "|" + t.merchant;
            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(t.id);
        }

        for (Map.Entry<String, List<Integer>> e : map.entrySet()) {
            if (e.getValue().size() > 1) {
                duplicates.add(e.getKey() + " -> " + e.getValue());
            }
        }

        return duplicates;
    }

    public static void main(String[] args) {

        List<Transaction> transactions = Arrays.asList(
                new Transaction(1, 500, "Store A", "10:00"),
                new Transaction(2, 300, "Store B", "10:15"),
                new Transaction(3, 200, "Store C", "10:30"),
                new Transaction(4, 500, "Store A", "11:00")
        );

        System.out.println("Two-Sum for target 500:");
        List<int[]> pairs = findTwoSum(transactions, 500);
        for (int[] pair : pairs) {
            System.out.println(Arrays.toString(pair));
        }

        System.out.println("\nDuplicate transactions:");
        List<String> duplicates = detectDuplicates(transactions);
        for (String d : duplicates) System.out.println(d);
    }
}