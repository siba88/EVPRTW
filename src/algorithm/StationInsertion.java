package algorithm;

import java.util.ArrayList;

import model.EVRPTWInstance;
import model.Node;
import model.Route;

public class StationInsertion {

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

	public StationInsertion(EVRPTWInstance instance) {
		this.instance = instance;
		currentTime = instance.getDepot().getReadyTime();
		unvisitedCustomers = instance.getCustomers();
		stations = instance.getStations();
		if(stations.get(0).getxCoordinate() == instance.getDepot().getxCoordinate() && stations.get(0).getyCoordinate() == instance.getDepot().getyCoordinate()) stations.remove(0);
		routes = new ArrayList<Route>();
		fuelTank = instance.getTankCapacity();
		fuelConsumption = instance.getFuelConsumption();
		insertCustomers();
	}

	private void insertCustomers() {
		route = new Route();
		load = instance.getLoad();
		fuelTank = instance.getTankCapacity();
		currentTime = 0;
		boolean finishedRoute = false;
		while (!finishedRoute && !unvisitedCustomers.isEmpty()) {
			if (route.getRoute().isEmpty()) {
				currentNode = instance.getDepot();
				route.addNode(currentNode);
			} else {
				Node neighbour = findBestNeighbour();
				// System.out.println(neighbour);
				if (neighbour != null && load - neighbour.getDemand() > 0) {
					if (fuelTank
							- (calculateDistanceBetweenPoints(neighbour,
									currentNode) * instance
									.getFuelConsumption()) > 0) {
						addNode(neighbour);

					} else {
						Node station = findBestStation(currentNode, route);
						if (station == null) {
							goToDepot(route);
							finishedRoute = true;
							routes.add(route);
							// insertCustomers();
						} else {
							addNode(station);
						}
					}
				} else if (!unvisitedCustomers.isEmpty()) {
					goToDepot(route);
					finishedRoute = true;
				}

			}
		}
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
					checkNode) * instance.getVelocity()) {
				// Do nothing
			} else {
				double currentMetric = distance + readyInterval + timeUrgency;
				if (currentMetric < bestMetric || bestMetric == -1) {
					bestMetric = currentMetric;
					bestNeighbour = checkNode;
				}
			}
		}
		
		if(bestNeighbour!=null && bestNeighbour.getId().equals("C30")){
			System.out.println(currentTime);
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

	public ArrayList<Route> getRoutes() {
		return routes;
	}

}
