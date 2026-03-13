import java.util.*;

/**
 * Problem 8: Parking Lot Management with Open Addressing
 *
 * Author: Karthick
 * Version: 1.0
 */
public class ParkingLotManagement {

    static class Spot {
        String licensePlate;
        boolean occupied;

        Spot() {
            this.licensePlate = null;
            this.occupied = false;
        }
    }

    private Spot[] parkingSpots;
    private int size;
    private int occupiedCount = 0;
    private int totalProbes = 0;

    public ParkingLotManagement(int capacity) {
        size = capacity;
        parkingSpots = new Spot[capacity];
        for (int i = 0; i < capacity; i++) parkingSpots[i] = new Spot();
    }

    private int hash(String licensePlate) {
        return Math.abs(licensePlate.hashCode()) % size;
    }

    public int parkVehicle(String licensePlate) {
        int index = hash(licensePlate);
        int probes = 0;

        while (parkingSpots[index].occupied) {
            index = (index + 1) % size;
            probes++;
        }

        parkingSpots[index].occupied = true;
        parkingSpots[index].licensePlate = licensePlate;
        occupiedCount++;
        totalProbes += probes;

        System.out.println("Assigned spot #" + index + " (" + probes + " probes)");
        return index;
    }

    public void exitVehicle(String licensePlate) {
        int index = hash(licensePlate);
        while (parkingSpots[index].occupied && !licensePlate.equals(parkingSpots[index].licensePlate)) {
            index = (index + 1) % size;
        }
        if (parkingSpots[index].occupied) {
            parkingSpots[index].occupied = false;
            parkingSpots[index].licensePlate = null;
            occupiedCount--;
            System.out.println("Spot #" + index + " freed for " + licensePlate);
        } else {
            System.out.println("Vehicle not found: " + licensePlate);
        }
    }

    public void getStatistics() {
        double avgProbes = occupiedCount == 0 ? 0 : (double) totalProbes / occupiedCount;
        double occupancy = (double) occupiedCount / size * 100;
        System.out.println("Occupancy: " + String.format("%.2f", occupancy) + "%, Avg Probes: " + String.format("%.2f", avgProbes));
    }

    public static void main(String[] args) {
        ParkingLotManagement lot = new ParkingLotManagement(500);

        lot.parkVehicle("ABC-1234");
        lot.parkVehicle("ABC-1235");
        lot.parkVehicle("XYZ-9999");

        lot.exitVehicle("ABC-1234");

        lot.getStatistics();
    }
}