import java.util.Scanner;
import java.util.LinkedList;

public class Main {
	public static void main(String[] args) {
		LinkedList<int[]> GraphInfo=new LinkedList<>();

		// Input graph
		// (N, E) = (# of nodes, # of edges)
		Scanner scanner = new Scanner(System.in);
		int numOfNodes = scanner.nextInt();
		int numOfEdges = scanner.nextInt();

		// (src, dst, distance)
		for (int i = 0; i < numOfEdges; i++) {
			int[] list = new int[3];
			list[0] = scanner.nextInt(); // src
			list[1] = scanner.nextInt(); // dst
			list[2] = scanner.nextInt(); // distance
			GraphInfo.add(list);
		}

		// D = reachable city distance threshold
		int D = scanner.nextInt();

	
		LinkedList<shortestPaths> spp = new LinkedList<shortestPaths>();
		for(int i = 0; i < numOfNodes; i++) {
			shortestPaths temp = new shortestPaths(i + 1, GraphInfo, numOfNodes);
			spp.add(temp);
		}
		
		
		int bestCity = 0;
		int accessibility = 0;
		int totalDist = 0;
		int criteria = D;
		
		
		while(true) {
			for(shortestPaths paths : spp) {
				int[] calcResult = paths.calcAccess(criteria);
				
				if (accessibility < calcResult[0] || (accessibility == calcResult[0] && totalDist > calcResult[1]) || (accessibility == calcResult[0] && totalDist == calcResult[1] && paths.startCity < bestCity)) {
					bestCity = paths.startCity;
					accessibility = calcResult[0];
					totalDist = calcResult[1];
				}
			}
			
			if (accessibility != 0) break;
			else criteria++;
		}
		
		System.out.print("Best city ");
		System.out.println(bestCity);
		System.out.print("Accessibility ");
		System.out.println(accessibility);
		System.out.print("Total distance ");
		System.out.println(totalDist);
		
		for(shortestPaths paths : spp) {
			if(paths.startCity == bestCity) {
				for(cityNode cloud : paths.cloudCity) {
					if(cloud.cityLabel == bestCity) continue;
					if(cloud.distValue <= criteria) {
						System.out.print("Path");
						for(int i : cloud.path) {
							System.out.print(" ");
							System.out.print(i);
						}
						System.out.println();
						System.out.print("Distance ");
						System.out.println(cloud.distValue);
					}
				}
				
				break;
			}
		}
		
		
	}
}

class cityNode {
	int cityLabel;
	int distValue;
	LinkedList<int[]> adjacentCity = new LinkedList<int[]>();
	LinkedList<Integer> path = new LinkedList<>();
	
	public cityNode() {}
	public cityNode(int label, LinkedList<int[]> graphIn) {
		cityLabel = label;
		distValue = 0;
		getAdjCity(graphIn);
	}
	
	public void getAdjCity(LinkedList<int[]> graph) {
		for(int[] graphElement : graph) {
			if(graphElement[0] == cityLabel) {
				int[] temp = new int[2];
				temp[0] = graphElement[1];
				temp[1] = graphElement[2];
				adjacentCity.add(temp);
			}
		}
	}

	
}

class shortestPaths {
	int startCity;
	LinkedList<int[]> graph;
	LinkedList<cityNode> cloudCity = new LinkedList<cityNode>();
	int numOfNodes;
	
	public shortestPaths() {}
	public shortestPaths(int startLabel, LinkedList<int[]> graphIn, int num) {
		startCity = startLabel;
		graph = graphIn;
		numOfNodes = num;
		dijkstraAlgo();
		cloudSort();
	}

	
	public void dijkstraAlgo() {
		
		LinkedList<cityNode> outCity = new LinkedList<cityNode>();
		cityNode evalCity = new cityNode(startCity, graph);
		
		evalCity.path.add(startCity);
		
		LinkedList<Integer> cities = new LinkedList<Integer>();
		for(int[] cityData : graph) {
			int tempCity = cityData[0];
			if(!cities.contains(tempCity)) cities.add(tempCity);
		}
		
		
		
		for(int i = 0; i < numOfNodes; i++) {
			int cityLabel = i + 1;
			if(cityLabel != startCity) {
				cityNode newCity = new cityNode(cityLabel, graph);
				newCity.distValue = 99999999;	
				outCity.add(newCity);
			}
		}

		
		
		while(!outCity.isEmpty()) {	
			for(int[] adj : evalCity.adjacentCity) {
				for(cityNode out : outCity) {
					if (out.cityLabel == adj[0]) {
						int evalDist = evalCity.distValue + adj[1];
						if(evalDist < out.distValue) {
							out.distValue = evalDist;
							out.path.clear();
							for(int i : evalCity.path) {
								out.path.add(i);
							}
							out.path.add(out.cityLabel);	
						}
					}
				}	
			}
			
			cloudCity.add(evalCity);
			
			if(outCity.size() == 1) {
				cloudCity.add(outCity.pop());
				break;
			}
			
			
			int minLabel = outCity.get(0).cityLabel;
			int minDist = outCity.get(0).distValue;
			for(int i = 0; i < outCity.size(); i++) {
				int tempLabel = outCity.get(i).cityLabel;
				int tempDist = outCity.get(i).distValue;
				if(tempDist < minDist) {
					minLabel = tempLabel;
					minDist = tempDist;
				}
			}
			
			for(int i = 0; i < outCity.size(); i++) {
				if(outCity.get(i).cityLabel == minLabel) {
					evalCity = outCity.get(i);
					outCity.remove(i);
					break;
				}
			}	
		}
	}
	
	public int[] calcAccess(int length) {
		int access = 0;
		int totalDist = 0;
		int[] result = new int[2];
		
		for(cityNode cloud : cloudCity) {
			if(cloud.cityLabel == startCity) continue;
			if(cloud.distValue <= length) {
				access++;
				totalDist += cloud.distValue;
			}
		}
		
		result[0] = access;
		result[1] = totalDist;
		
		return result;
	}
	
	public void cloudSort() {
		
		LinkedList<cityNode> tempCloud = new LinkedList<cityNode>();
		
		while(!cloudCity.isEmpty()) {
			tempCloud.add(cloudCity.pop());
		}
		
		while(!tempCloud.isEmpty()) {
			int minLabel = tempCloud.get(0).cityLabel;
			for(cityNode cloud : tempCloud) {
				if(cloud.cityLabel < minLabel) minLabel = cloud.cityLabel;
			}
			
			for(int i = 0; i < tempCloud.size(); i++) {
				if(minLabel == tempCloud.get(i).cityLabel) {
					cloudCity.add(tempCloud.get(i));
					tempCloud.remove(i);
					break;
				}
			}
			
		}
	}
	
}