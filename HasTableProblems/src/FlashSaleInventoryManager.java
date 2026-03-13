import java.util.*;

/**
 * Problem 2: E-commerce Flash Sale Inventory Manager
 * Simulates stock handling during high demand flash sales.
 *
 * Concepts:
 * HashMap for instant stock lookup
 * Thread-safe purchase processing
 * Waiting list using LinkedHashMap (FIFO)
 *
 * @author Karthick
 * @version 1.0
 */

public class FlashSaleInventoryManager {

    // productId -> stock
    private HashMap<String, Integer> inventory = new HashMap<>();

    // productId -> waiting list
    private HashMap<String, LinkedList<Integer>> waitingList = new HashMap<>();

    public FlashSaleInventoryManager() {

        // initialize stock
        inventory.put("IPHONE15_256GB", 100);
        waitingList.put("IPHONE15_256GB", new LinkedList<>());
    }

    // Check stock availability
    public int checkStock(String productId) {

        return inventory.getOrDefault(productId, 0);
    }

    // Purchase item (thread-safe)
    public synchronized String purchaseItem(String productId, int userId) {

        int stock = inventory.getOrDefault(productId, 0);

        if (stock > 0) {

            inventory.put(productId, stock - 1);

            return "Success: User " + userId +
                    " purchased item. Remaining stock: " + (stock - 1);

        } else {

            waitingList.get(productId).add(userId);

            int position = waitingList.get(productId).size();

            return "Out of stock. User " + userId +
                    " added to waiting list. Position #" + position;
        }
    }

    // Display waiting list
    public void showWaitingList(String productId) {

        System.out.println("Waiting List: " + waitingList.get(productId));
    }

    public static void main(String[] args) {

        FlashSaleInventoryManager manager =
                new FlashSaleInventoryManager();

        System.out.println("Stock: "
                + manager.checkStock("IPHONE15_256GB") + " units");

        System.out.println(manager.purchaseItem("IPHONE15_256GB", 12345));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 67890));

        // simulate stock exhaustion
        for (int i = 0; i < 100; i++) {
            manager.purchaseItem("IPHONE15_256GB", i);
        }

        System.out.println(manager.purchaseItem("IPHONE15_256GB", 99999));

        manager.showWaitingList("IPHONE15_256GB");
    }
}
