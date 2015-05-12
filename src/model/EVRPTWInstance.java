package model;

import java.util.ArrayList;

public class EVRPTWInstance {
	
	private Node depot;
	private ArrayList<Node> stations;
	private ArrayList<Node> customers;
	
	private double tankCapacity;
	private double load;
	private double fuelConsumption;
	private double refueling;
	private double velocity;
	
	private ArrayList<Route> routes;
	
	
	public EVRPTWInstance(){
		stations=new ArrayList<Node>();
		customers=new ArrayList<Node>();
		routes = new ArrayList<Route>();
	}
	
	public void setDepot(Node depot){
		this.depot=depot;
	}
	
	public Node getDepot(){
		return depot;
	}
	
	public void addStation(Node station){
		stations.add(station);
	}
	
	public ArrayList<Node> getStations(){
		return stations;
	}
	
	public void addCustomer(Node customer){
		customers.add(customer);
	}
	
	public ArrayList<Node> getCustomers(){
		return customers;
	}

	public double getTankCapacity() {
		return tankCapacity;
	}

	public void setTankCapacity(double tankCapacity) {
		this.tankCapacity = tankCapacity;
	}

	public double getLoad() {
		return load;
	}

	public void setLoad(double load) {
		this.load = load;
	}

	public double getFuelConsumption() {
		return fuelConsumption;
	}

	public void setFuelConsumption(double fuelConsumption) {
		this.fuelConsumption = fuelConsumption;
	}

	public double getRefueling() {
		return refueling;
	}

	public void setRefueling(double refueling) {
		this.refueling = refueling;
	}

	public double getVelocity() {
		return velocity;
	}

	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

}
