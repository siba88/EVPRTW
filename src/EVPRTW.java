import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import algorithm.EVRPTWAlgorithm;
import model.EVRPTWInstance;
import model.Node;


public class EVPRTW {

	public static void main(String[] args) {
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("evrptw_otl_instances/r101.txt"));
			String line = br.readLine();
			
			EVRPTWInstance instance = new EVRPTWInstance();
			Node depot;
			
			line = br.readLine();
			
			 while (!line.isEmpty()) {
				 String id = line.substring(0, 10).trim();
				 String type = line.substring(11, 21).trim();
				 Double xCoordinate = Double.valueOf(line.substring(21, 31).trim());
				 Double yCoordinate = Double.valueOf(line.substring(31, 41).trim());
				 Double demand = Double.valueOf(line.substring(41, 51).trim());
				 Double readyTime = Double.valueOf(line.substring(51, 61).trim());
				 Double dueDate = Double.valueOf(line.substring(61, 71).trim());
				 Double serviceTime = Double.valueOf(line.substring(72, 81).trim());
				 Node node=new Node(id, type, xCoordinate, yCoordinate, demand, readyTime, dueDate, serviceTime);
				 line = br.readLine();
				 if(node.getType().equals("d")){
					 instance.setDepot(node);
				 }
				 if(node.getType().equals("f")){
					 if(!(node.getxCoordinate().equals(instance.getDepot().getxCoordinate()) && node.getyCoordinate().equals(instance.getDepot().getyCoordinate()))){
						 instance.addStation(node);
					 }
				 }
				 if(node.getType().equals("c")){
					 instance.addCustomer(node);
				 }
			 }
			 line = br.readLine();
			 double tankCapacity = Double.valueOf(line.substring(30, line.length()-1));
			 instance.setTankCapacity(tankCapacity);
			 line = br.readLine();
			 double load= Double.valueOf(line.substring(25, line.length()-1));
			 instance.setLoad(load);
			 line = br.readLine();
			 double fuelConsumption = Double.valueOf(line.substring(25, line.length()-1));
			 instance.setFuelConsumption(fuelConsumption);
			 line = br.readLine();
			 double refueling = Double.valueOf(line.substring(26, line.length()-1));
			 instance.setRefueling(refueling);
			 line = br.readLine();
			 double velocity = Double.valueOf(line.substring(20, line.length()-1));
			 instance.setVelocity(velocity);
			 
			 EVRPTWAlgorithm algorithm = new EVRPTWAlgorithm(instance);
			 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
