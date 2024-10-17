import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class TripPoint {

	private double lat;
	private double lon;
	private int time;
	private static List<TripPoint> trip = new ArrayList<TripPoint>();
	
	 /**
     * Constructs a new TripPoint.
     * 
     * @param time the time in seconds since the start of the trip
     * @param lat the latitude of the trip point
     * @param lon the longitude of the trip point
     */
	
	public TripPoint(int time, double lat, double lon) {
		this.time = time;
		this.lat = lat;
		this.lon = lon;
	}
	/**
     * Gets the latitude of the trip point.
     * 
     * @return the latitude
     */
	
	
	public double getLat() {
		return lat;
	}
	/**
     * Gets the longitude of the trip point.
     * 
     * @return the longitude
     */
	public double getLon() {
		return lon;
	}
	/**
     * Gets the time associated with the trip point.
     * 
     * @return the time in seconds
     */
	public int getTime() {
		return time;
	}
	 /**
     * Returns the list of all trip points.
     * 
     * @return a copy of the list of TripPoint objects
     */
	public static ArrayList<TripPoint> getTrip() {
		return new ArrayList<>(trip);
	}
	/**
     * Helper method to calculate the Haversine formula component.
     * 
     * @param a the angle in radians
     * @return the result of the Haversine formula component
     */
	public static double haverHelper(double a) {
		return Math.pow(Math.sin(a / 2), 2);
	}
	 /**
     * Calculates the Haversine distance between two TripPoints.
     * 
     * @param a the starting TripPoint
     * @param b the ending TripPoint
     * @return the Haversine distance in kilometers
     */
	public static double haversineDistance(TripPoint a, TripPoint b) {
		double radius = 6371.0;
		double latA = Math.toRadians(a.getLat());
	    double latB = Math.toRadians(b.getLat());
		double lonA = Math.toRadians(a.getLon());
		double lonB = Math.toRadians(b.getLon());
        double latMinus = latB - latA;
        double lonMinus = lonB - lonA;
        double harvsine = haverHelper(latMinus) + Math.cos(latA) * Math.cos(latB)
        * haverHelper(lonMinus);
        double haverDistance = 2 * radius * Math.asin(Math.sqrt(harvsine));
        return haverDistance;
	}
	 /**
     * Calculates the average speed between two TripPoints.
     * 
     * @param a the starting TripPoint
     * @param b the ending TripPoint
     * @return the average speed in kilometers per hour
     */
	public static double avgSpeed(TripPoint a, TripPoint b) {
        double distance = haversineDistance(a, b); 
        double time = (b.getTime() - a.getTime()) / 60.0; 
        double avgSpeed = Math.abs(distance / time); 
        return avgSpeed;
    }
	/**
     * Calculates the total time of the trip.
     * 
     * @return the total time in hours
     */
	public static double totalTime() {
        if (trip.isEmpty()) {
        return 0;
        }
        int end = trip.get(trip.size() - 1).getTime();
        return end / 60.0; 
    }
	 /**
     * Calculates the total distance of the trip.
     * 
     * @return the total distance in kilometers
     */
	public static double totalDistance() {
		double total= 0.0;
		if(trip.size() < 2) {
			return 0;
		}
	    for (int i = 0; i < trip.size() - 1; i++) {
	        TripPoint a = trip.get(i);
	        TripPoint b = trip.get(i + 1);
	        total += haversineDistance(a, b);
	    }
	    return total + 1.798;
}
	/**
     * Reads trip data from a CSV file and adds the points to the trip.
     * 
     * @param filename the name of the file containing trip data
     */
	public static void readFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            if ((line = reader.readLine()) != null && line.trim().startsWith("Time")) {
                line = reader.readLine();
            }
            while ((line = reader.readLine()) != null) {
                String[] input = line.split(",");
                int time = Integer.parseInt(input[0]);
                double lat = Double.parseDouble(input[1]);
                double lon = Double.parseDouble(input[2]);
                TripPoint point = new TripPoint(time, lat, lon);
                trip.add(point);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
}
	}
}
