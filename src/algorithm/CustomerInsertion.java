package algorithm;

import java.util.ArrayList;

import model.EVRPTWInstance;
import model.Node;
import model.Route;

public class CustomerInsertion {

	EVRPTWInstance instance;
	double currentTime;
	Node currentNode;
	double load;
	ArrayList<Node> unvisitedCustomers;
	ArrayList<Node> stations;
	Route route;
	ArrayList<Route> routes;
	double fuelTank;
	double fuelConsumption;

	public CustomerInsertion(EVRPTWInstance instance) {
		this.instance = instance;
		currentTime = instance.getDepot().getReadyTime();
		unvisitedCustomers = instance.getCustomers();
		stations = instance.getStations();
		if(stations.get(0).getxCoordinate().equals(instance.getDepot().getxCoordinate()) && stations.get(0).getyCoordinate() == instance.getDepot().getyCoordinate()) stations.remove(0);
		routes = new ArrayList<Route>();
		fuelTank = instance.getTankCapacity();
		fuelConsumption = instance.getFuelConsumption();
		insertCustomers();
	}

	public void insertCustomers() {
		route = new Route();
		currentNode = instance.getDepot();
		route.addNode(currentNode);
		load = instance.getLoad();
		fuelTank = instance.getTankCapacity();
		currentTime=0;
		boolean noCapacity = false;
		while (!noCapacity && !unvisitedCustomers.isEmpty()) {
			Node neighbour = findBestNeighbour();
			if (neighbour != null && load - neighbour.getDemand() > 0) {
				if(fuelTank-calculateDistanceBetweenPoints(currentNode,
							neighbour)*instance.getFuelConsumption()>0){
					fuelTank-=calculateDistanceBetweenPoints(currentNode,
							neighbour)*instance.getFuelConsumption();
					load -= neighbour.getDemand();
					currentTime += calculateDistanceBetweenPoints(currentNode,
							neighbour) * instance.getVelocity();
					if (currentTime < neighbour.getReadyTime())
						currentTime = neighbour.getReadyTime();
					currentTime += neighbour.getServiceTime();
					route.addNode(neighbour);
					unvisitedCustomers.remove(neighbour);
					if (unvisitedCustomers.isEmpty()) {
						noCapacity = true;
						goToDepot(route);
					}
					currentNode = neighbour;
				}
				else if(route.getRoute().size()>1){
						Node station = findBestStation(currentNode, route);
						addNode(station);
				}
			} else if(!unvisitedCustomers.isEmpty()){
				//goToDepot(route);
				route.addNode(instance.getDepot());
				routes.add(route);
				noCapacity = true;
			}
		}
		// System.out.println(unvisitedCustomers.size());
		if (!unvisitedCustomers.isEmpty()) {
			insertCustomers();
		}
	}
	
	public void addNode(Node neighbour) {
		// System.out.println(neighbour);
		fuelTank -= calculateDistanceBetweenPoints(neighbour, currentNode)
				* instance.getFuelConsumption();
		//System.out.println("Fuel consuption from node"+currentNode.getId()+"to "+neighbour.getId()+" tank:"+fuelTank);
		currentTime += calculateDistanceBetweenPoints(neighbour, currentNode)
				* instance.getVelocity();
		if (currentTime < neighbour.getReadyTime())
			currentTime = neighbour.getReadyTime();
		currentTime += neighbour.getServiceTime();
		load -= neighbour.getDemand();
		if (neighbour.getType().equals("f")) {
			currentTime += (instance.getTankCapacity() - fuelTank)
					* instance.getRefueling();
			fuelTank = instance.getTankCapacity();
		} else if (neighbour.getType().equals("c")) {
			unvisitedCustomers.remove(neighbour);
		}
		route.addNode(neighbour);
		currentNode = neighbour;

	}

	/*private void findNearestStation() {
		Node nearestStation = null;
		boolean stationFound = false;
		double minimalLength = 0;
		while (!stationFound) {
			for (int i = 0; i < stations.size(); i++) {
				double length = calculateDistanceBetweenPoints(currentNode,
						stations.get(i));
				if (nearestStation == null || length < minimalLength) {
					nearestStation = stations.get(i);
					minimalLength = length;
				}
			}
		}
			System.out.println(fuelTank);
			if (fuelTank - (minimalLength * fuelConsumption) > 0) {
				currentTime += (instance.getTankCapacity() - fuelTank)
						* instance.getRefueling();
				fuelTank = instance.getTankCapacity();
				currentNode = nearestStation;
				route.addNode(currentNode);
				stationFound = true;
			} else {
				System.out.println(currentNode);
				Node nodeBefore = route.getRoute().get(
						route.getRoute().size() - 2);
				System.out.println(nodeBefore);
				fuelTank += calculateDistanceBetweenPoints(currentNode,
						nodeBefore)*instance.getFuelConsumption();
				System.out.println(fuelTank);
				currentTime-=calculateDistanceBetweenPoints(currentNode,
						nodeBefore)*instance.getVelocity()+currentNode.getServiceTime();
				route.getRoute().remove(currentNode);
				unvisitedCustomers.add(currentNode);
				if(nodeBefore.getType().equals("f")){
					goToDepot();
				}
				else{
					currentNode=nodeBefore;
				}
			}
		
	}*/
	
	public void removeLastNode(Route route) {
		Node nodeBefore = route.getRoute().get(route.getRoute().size() - 2);
		if (currentNode.getType().equals("c")) {
			unvisitedCustomers.add(currentNode);
		}
		fuelTank += calculateDistanceBetweenPoints(nodeBefore, currentNode)
				* instance.getFuelConsumption();
		route.getRoute().remove(currentNode);
		currentTime = route.getTime(instance);
		currentNode = nodeBefore;
	}
	
	public Node findBestStation(Node node, Route route) {
		Node nearestStation = null;
		double minimalLength = 0;
		for (int i = 0; i < stations.size(); i++) {
			double length = calculateDistanceBetweenPoints(node,
					stations.get(i));
			if (nearestStation == null || length < minimalLength) {
				nearestStation = stations.get(i);
				minimalLength = length;
			}
		}
		if (fuelTank - minimalLength * instance.getFuelConsumption() > 0) {
			/*System.out.println(currentNode);
			System.out.println(nearestStation);
			System.out.println(fuelTank);
			System.out.println(minimalLength * instance.getFuelConsumption());*/
			return nearestStation;

		} else {
			removeLastNode(route);
			return findBestStation(currentNode, route);
		}
	}

	private Node findBestNeighbour() {
		double bestMetric = -1;
		Node bestNeighbour = null;
		for (int i = 0; i < unvisitedCustomers.size(); i++) {
			Node checkNode = unvisitedCustomers.get(i);
			double distance = calculateDistanceBetweenPoints(currentNode,
					checkNode);
			double readyInterval;
			if (checkNode.getReadyTime() > currentTime) {
				readyInterval = checkNode.getReadyTime() - currentTime;
			} else {
				readyInterval = 0;
			}
			double timeUrgency = checkNode.getDueDate() - currentTime;
			if (timeUrgency < calculateDistanceBetweenPoints(currentNode,
					checkNode)*instance.getVelocity()) {
				// unvisitedCustomers.remove(checkNode);
				// unfeasibleNodes.add(checkNode);
				/*
				 * ArrayList<Node> oldRoute = route.getRoute(); int index =
				 * oldRoute.lastIndexOf(currentNode); Node nodeBeforeCurrent =
				 * oldRoute.get(index-1); double oldTime =
				 * currentTime-calculateDistanceBetweenPoints(nodeBeforeCurrent,
				 * currentNode
				 * )*instance.getVelocity()-currentNode.getServiceTime(); double
				 * newDistance =
				 * calculateDistanceBetweenPoints(nodeBeforeCurrent, checkNode);
				 * double checkTime = oldTime +
				 * newDistance*instance.getVelocity();
				 * if(checkTime<checkNode.getReadyTime()||
				 * checkTime>checkNode.getDueDate()){
				 * System.out.println("Not feasible"); } else{
				 * 
				 * 
				 * Route route = new Route();
				 * route.addNode(instance.getDepot()); route.addNode(checkNode);
				 * route.addNode(instance.getDepot()); routes.add(route);
				 * unvisitedCustomers.remove(checkNode); }
				 * unfeasibleNodes.add(checkNode);
				 * unvisitedCustomers.remove(checkNode);
				 * System.out.println("Unfeasible node");
				 */
			} else {
				double currentMetric = distance + readyInterval + timeUrgency;
				if (currentMetric < bestMetric || bestMetric == -1) {
					bestMetric = currentMetric;
					bestNeighbour = checkNode;
				}
			}
		}

		return bestNeighbour;
	}

	public double calculateDistanceBetweenPoints(Node currentNode,
			Node checkNode) {
		double deltax = Math.abs(currentNode.getxCoordinate()
				- checkNode.getxCoordinate());
		double deltay = Math.abs(currentNode.getyCoordinate()
				- checkNode.getyCoordinate());
		double distance = Math.sqrt(deltax * deltax + deltay * deltay);
		return distance;
	}

	/*
	 * private void assignRestOfTheNodes() { route = new Route(); currentNode =
	 * instance.getDepot(); route.addNode(currentNode); load =
	 * instance.getLoad(); boolean noCapacity = false; while(!noCapacity &&
	 * !unfeasibleNodes.isEmpty()){ Node neighbour = findBestNeighbour();
	 * if(neighbour!=null && load-neighbour.getDemand()>0){ load -=
	 * neighbour.getDemand(); currentTime +=
	 * calculateDistanceBetweenPoints(currentNode,
	 * neighbour)*instance.getVelocity()+neighbour.getServiceTime();
	 * route.addNode(neighbour); unfeasibleNodes.remove(neighbour); currentNode
	 * = neighbour; } else{ route.addNode(instance.getDepot());
	 * routes.add(route); noCapacity=true; } } if(!unfeasibleNodes.isEmpty()){
	 * System.out.println(unfeasibleNodes.size()); //assignRestOfTheNodes(); }
	 * 
	 * }
	 * 
	 * private Node bestUnfeasibleNeighbour(){ double bestMetric = -1; Node
	 * bestNeighbour = null; for(int i =0; i <unfeasibleNodes.size(); i++){ Node
	 * checkNode = unfeasibleNodes.get(i); double distance =
	 * calculateDistanceBetweenPoints(currentNode, checkNode); double
	 * readyInterval; if(checkNode.getReadyTime()>currentTime){ readyInterval =
	 * checkNode.getReadyTime()-currentTime; } else { readyInterval=0; } double
	 * timeUrgency = checkNode.getDueDate()-currentTime; double currentMetric =
	 * distance + readyInterval + timeUrgency; if(currentMetric < bestMetric ||
	 * bestMetric == -1){ bestMetric = currentMetric; bestNeighbour = checkNode;
	 * } } return bestNeighbour; }
	 */

	public ArrayList<Route> getRoutes() {
		return routes;
	}
	
	public void goToDepot(Route route) {
		if (currentTime
				+ calculateDistanceBetweenPoints(currentNode,
						instance.getDepot()) * instance.getVelocity() > instance
				.getDepot().getDueDate()) {
			removeLastNode(route);
			goToDepot(route);
		}
		else if(fuelTank<calculateDistanceBetweenPoints(currentNode,
				instance.getDepot())*instance.getFuelConsumption()){
			Node station = findBestStation(currentNode, route);
			addNode(station);
			goToDepot(route);
		}
		else {
			route.addNode(instance.getDepot());
			routes.add(route);
		}
	}

}
