package cs2321;

import net.datastructures.*;

/**
 * @author Ruihong Zhang
 * Reference textbook R14.16 P14.81 
 * 
 * In this file, you will traverse the city map using DFS, BFS and Dijkstra's algorithm. 
 *
 */



public class Travel {
	AdjacencyGraph<String, Integer> graph = new AdjacencyGraph<String, Integer>();
	static int INFINITE = 999999999;
	
	public Travel(String [][] routes) {
		boolean containsfirst = false;
		boolean containssecond = false;
		boolean edgeexists;
		Vertex<String> first = null;
		Vertex<String> second = null;
		graph.insertVertex(routes[0][0]);
		for(String[] r : routes) {
			for (Vertex<String> vert: graph.vertices()) {
				if ((vert).getElement() == r[0] ) {
					containsfirst = true;
					first = vert;
					continue;
				}
				if ((vert).getElement() == r[1] ) {
					containssecond = true;
					second = vert;
					continue;
				}
			}
			if(containsfirst && containssecond) {
				graph.insertEdge(first, second, Integer.valueOf(r[2]));
			}
			else if(containsfirst) {
				graph.insertEdge(first, graph.insertVertex(r[1]), Integer.valueOf(r[2]));
			}
			else if(containssecond) {
				graph.insertEdge(graph.insertVertex(r[0]), second, Integer.valueOf(r[2]));
			} else {
				graph.insertEdge(graph.insertVertex(r[0]), graph.insertVertex(r[1]), Integer.valueOf(r[2]));
			}
			containsfirst = false;
			containssecond = false;
		}
	}


			}
		}
		if (startVert == null) throw new IllegalArgumentException("Departure Not Found!");
		visitedArr = DFS(startVert, visited, forest, visitedArr);
		int i = 0;
		for(String s:visitedArr) {
			returnArr[i] = s;
			i++;
		}
		return returnArr;
	}
	
	private ArrayList<String> DFS(Vertex<String> v, HashMap<Vertex<String>, Boolean> visited, HashMap<Edge<Integer>, Vertex<String>> forest, ArrayList<String> arr) {
		visited.put(v, true);
		arr.addLast(v.getElement());
		for(Edge<Integer> e: sortedOutgoingEdges(v)) {
			if(forest.get(e) != null) continue;
			if (visited.get(graph.opposite(v, e)) == null){
				forest.put(e, graph.opposite(v, e));
				arr = DFS(graph.opposite(v, e), visited, forest, arr);
			}
		}
		return arr;
	}
	
	public String[] BFS(String departure) {
		String[] returnArr = new String[graph.vertList.size()];
		Vertex<String> startVert = null;
		ArrayList<String> arr = new ArrayList<String>();
		for (Vertex<String> vert: graph.vertices()) {
			if ((vert).getElement() == departure ) {
				startVert = vert;
				break;
			}
		}
		if (startVert == null) throw new IllegalArgumentException("Departure Not Found!");
		arr = BFS(startVert);
		int i = 0;
		for(String s:arr) {
			returnArr[i] = s;
			i++;
		}
		return returnArr;
	}
	
	public ArrayList<String> BFS(Vertex<String> v){
		ArrayList<String> visitedArr = new ArrayList<String>();
 		HashMap<Vertex<String>, Boolean> visited = new HashMap<Vertex<String>, Boolean>();
		HashMap<Edge<Integer>, Vertex<String>> forest = new HashMap< Edge<Integer>, Vertex<String>>();
		CircularArrayQueue<Vertex<String>> q = new CircularArrayQueue<Vertex<String>>(graph.vertList.size());
		Vertex<String> currVert;
		q.enqueue(v);
		while (!q.isEmpty()) {
			currVert = q.dequeue();
			visitedArr.addLast(currVert.getElement());
			visited.put(currVert, true);
			for(Edge<Integer> e: sortedOutgoingEdges(currVert)) {
				if(forest.get(e) != null) continue;
				if(visited.get(graph.opposite(currVert, e)) == null) {
					forest.put(e, graph.opposite(currVert, e));
					visited.put(graph.opposite(currVert, e), true);
					q.enqueue(graph.opposite(currVert, e));
				}
			}
		}
		return visitedArr;
	}
	
	public String[][] Dijkstra(String departure) {
		String[][] returnArr = new String[graph.vertList.size()][2];
		Vertex<String> startVert = null;
		HeapAPQ<Integer, Vertex<String>> pq = new HeapAPQ<Integer, Vertex<String>>();
		HashMap<Vertex<String>, Integer> d = new HashMap<Vertex<String>, Integer>();
		for (Vertex<String> vert: graph.vertices()) {
			if ((vert).getElement() == departure ) {
				startVert = vert;
				break;
			}
		}
		if (startVert == null) throw new IllegalArgumentException("Departure Not Found!");
		d = Dijkstra(startVert);
		for(Entry<Vertex<String>, Integer> e: d.entrySet()) {
			pq.insert(e.getValue(), e.getKey());
		}
		int i = 0;
		while(!pq.isEmpty()) {
			returnArr[i][0] = pq.min().getValue().getElement();
			returnArr[i][1] = pq.min().getKey().toString();
			pq.removeMin();
			i++;
		}
		return returnArr;
	}
	
	public HashMap<Vertex<String>, Integer> Dijkstra(Vertex<String> v){
		HeapAPQ<Integer, Vertex<String>> pq = new HeapAPQ<Integer, Vertex<String>>();
		HashMap<Vertex<String>, Integer> d = new HashMap<Vertex<String>, Integer>();
		HashMap<Vertex<String>, Entry<Integer, Vertex<String>>> pqMap = new HashMap<Vertex<String>, Entry<Integer, Vertex<String>>>();
		HashMap<Vertex<String>, Integer> cloud = new HashMap<Vertex<String>, Integer>();
		HashMap<Vertex<String>, Edge<Integer>> forest = new HashMap<Vertex<String>, Edge<Integer>>();
		Entry<Integer, Vertex<String>> entry;
		for(Vertex<String> u: graph.vertices()) {
			if (u == v) {
				d.put(u, 0);
			} else {
				d.put(u, INFINITE);
			}
			pqMap.put(u, pq.insert(d.get(u), u));
		}
		while (!pq.isEmpty()) {
			entry = pq.removeMin();
			Vertex<String> u = entry.getValue();
			int key = entry.getKey();
			cloud.put(u, key);
			pqMap.remove(u);
			for(Edge<Integer> e: sortedOutgoingEdges(u)) {
				Vertex<String> w = graph.opposite(u, e);
				if(cloud.get(w) == null) {
					int newDist = key + e.getElement();
					if(d.get(w) > newDist) {
						d.put(w, newDist);
						forest.put(w, e);
						pq.replaceKey(pqMap.get(w), newDist);
					}
				}
			}
		}
		return d;
	}
	

	public Iterable<Edge<Integer>> sortedOutgoingEdges(Vertex<String> v)  {
	
		
		
		ArrayList<Edge<Integer>>  A = new ArrayList<Edge<Integer>>();
		HeapAPQ<String, Edge<Integer>> pq = new HeapAPQ<String, Edge<Integer>> ();
		

		for (Edge<Integer> e: graph.outgoingEdges(v)) {
			String city = graph.opposite(v, e).getElement();
			pq.insert(city,e);
		}
		
		while (pq.size()!=0) {
			A.addLast(pq.removeMin().getValue());
		}
		
		return A;
	}
}
