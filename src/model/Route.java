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
			double deltax = Math.abs(route.get(i).getxCoordinate()
					- route.get(i+1).getxCoordinate());
			double deltay = Math.abs(route.get(i).getyCoordinate()
					- route.get(i+1).getyCoordinate());
			length += Math.sqrt(deltax * deltax + deltay * deltay);
		}
		double deltax = Math.abs(route.get(route.size()-1).getxCoordinate()
				- route.get(route.size()-2).getxCoordinate());
		double deltay = Math.abs(route.get(route.size()-1).getyCoordinate()
				- route.get(route.size()-2).getyCoordinate());
		length += Math.sqrt(deltax * deltax + deltay * deltay);
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

}
