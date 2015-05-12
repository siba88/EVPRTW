package model;

public class Node {
	
	private String id;
	private String type;
	private Double xCoordinate;
	private Double yCoordinate;
	private Double demand;
	private Double readyTime;
	private Double dueDate;
	private Double serviceTime;
	
	public Node(String id, String type, Double xCoordinate, Double yCoordinate,
			Double demand, Double readyTime, Double dueDate, Double serviceTime) {
		super();
		this.id = id;
		this.type = type;
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
		this.demand = demand;
		this.readyTime = readyTime;
		this.dueDate = dueDate;
		this.serviceTime = serviceTime;
	}
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Double getxCoordinate() {
		return xCoordinate;
	}
	public void setxCoordinate(Double xCoordinate) {
		this.xCoordinate = xCoordinate;
	}
	public Double getyCoordinate() {
		return yCoordinate;
	}
	public void setyCoordinate(Double yCoordinate) {
		this.yCoordinate = yCoordinate;
	}
	public Double getDemand() {
		return demand;
	}
	public void setDemand(Double demand) {
		this.demand = demand;
	}
	public Double getReadyTime() {
		return readyTime;
	}
	public void setReadyTime(Double readyTime) {
		this.readyTime = readyTime;
	}
	public Double getDueDate() {
		return dueDate;
	}
	public void setDueDate(Double dueDate) {
		this.dueDate = dueDate;
	}
	public Double getServiceTime() {
		return serviceTime;
	}
	public void setServiceTime(Double serviceTime) {
		this.serviceTime = serviceTime;
	}
	
	public String toString(){
		return id+"     "+type+"     "+xCoordinate+"       "+yCoordinate
 				+"      "+yCoordinate+"        "+ demand+"          "
 				+readyTime+"      "+dueDate+"         "+serviceTime;
	}

}
