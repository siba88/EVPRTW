package algorithm;

import java.util.ArrayList;

import model.EVRPTWInstance;
import model.Route;

public class EVRPTWAlgorithm {
	
	EVRPTWInstance instance;
	
	public EVRPTWAlgorithm(EVRPTWInstance instance){
		this.instance = instance;
		insertCustomers();
	}

	private void insertCustomers() {
		CustomerInsertion cusIns = new CustomerInsertion(instance);
		ArrayList<Route> routes = cusIns.getRoutes();
		double routesLength=0;
		for(int i=0; i<routes.size(); i++){
			routesLength +=routes.get(i).getLength();
		}
		System.out.println(routesLength);
		for(int i=0; i<routes.size(); i++){
			System.out.println(routes.get(i));
			//routes.get(i).validate(instance);
		}
	}
	
	

}
