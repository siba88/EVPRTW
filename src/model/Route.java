package model;

import java.util.ArrayList;

public class Route {
	
	ArrayList<Node> route;
	
	public Route(){
		route = new ArrayList<Node>();
	}
	
	public void addNode(Node node){
		route.add(node);
	}
	
	public ArrayList<Node> getRoute(){
		return route;
	}
	
	public double getLength(){
		double length = 0;
		for(int i=0; i<route.size()-1; i++){
			length += calculateDistanceBetweenPoints(route.get(i), route.get(i+1));
		}
		return length;
	}
	
	public String toString(){
		String returnString ="";
		for(int i=0; i<route.size()-1; i++){
			returnString+=route.get(i).getId();
			returnString+=", ";
		}
		returnString+=route.get(route.size()-1).getId();
		return returnString;
	}
	
	public double getTime(EVRPTWInstance instance){
		double currentTime=0;
		double currentEnergy = instance.getTankCapacity();
		for(int i=0; i<route.size()-1; i++){
			currentTime+=calculateDistanceBetweenPoints(route.get(i), route.get(i+1))*instance.getVelocity();
			if(currentTime<route.get(i).getReadyTime()){
				currentTime=route.get(i).getReadyTime();
			}
			currentEnergy -= calculateDistanceBetweenPoints(route.get(i), route.get(i+1))*instance.getFuelConsumption();
			if(route.get(i).getType().equals("c")){
				currentTime += route.get(i).getServiceTime();
			}
			else if(route.get(i).getType().equals("f")){
				currentTime+=(instance.getTankCapacity()-currentEnergy)*instance.getRefueling();
				currentEnergy=instance.getTankCapacity();
			}
		}
		return currentTime;
	}
	
	public void validate(EVRPTWInstance instance){
		double currentTime=0;
		double currentEnergy = instance.getTankCapacity();
		for(int i=0; i<route.size()-2; i++){
			currentTime+=calculateDistanceBetweenPoints(route.get(i), route.get(i+1))*instance.getVelocity();
			//System.out.println(instance.getVelocity());
			if(currentTime<route.get(i).getReadyTime()){
				currentTime=route.get(i).getReadyTime();
			}
			if(route.get(i).getType().equals("f")){
				currentTime+=(instance.getTankCapacity()-currentEnergy)*instance.getRefueling();
				currentEnergy=instance.getTankCapacity();
			}
			//System.out.println("route: "+route.get(i).getId()+" "+route.get(i+1).getId()+" time:"+currentTime+" dueDate "+route.get(i+1).getDueDate());
			if(currentTime>route.get(i+1).getDueDate()){
				System.out.println("Time violation at node:"+route.get(i+1).getId());
			}
			//System.out.println("route: "+route.get(i).getId()+" "+route.get(i+1).getId()+"tank "+currentEnergy+" after "+(currentEnergy - calculateDistanceBetweenPoints(route.get(i), route.get(i+1))*instance.getFuelConsumption()));
			currentEnergy -= calculateDistanceBetweenPoints(route.get(i), route.get(i+1))*instance.getFuelConsumption();
			if(currentTime>route.get(i+1).getDueDate()){
				System.out.println("Time violation at node:"+route.get(i+1).getId());
			}
			if(route.get(i).getType().equals("c")){
				currentTime += route.get(i+1).getServiceTime();
			}
			if(currentEnergy<0){
				//System.out.println(currentEnergy);
				//System.out.println(calculateDistanceBetweenPoints(route.get(i), route.get(i+1)));
				System.out.println("Energy violation at node:"+route.get(i+1).getId());
			}
		}
		
	}
	
	public double calculateDistanceBetweenPoints(Node x, Node y){
		double deltax = Math.abs(x.getxCoordinate()
				- y.getxCoordinate());
		double deltay = Math.abs(x.getyCoordinate()
				- y.getyCoordinate());
		return Math.sqrt(deltax * deltax + deltay * deltay);
	}

}
