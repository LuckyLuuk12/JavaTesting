package main;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Math.pow;

public class FlowNetwork {
  private final int size;
  private ArrayList<Tuple> E = new ArrayList<>();       // List for the edges
  private ArrayList<Integer> V = new ArrayList<>();     // List for the vertices
  private HashMap<Tuple, Integer> C = new HashMap<>();  // Capacity hashmap, every edge has a capacity
  private HashMap<Tuple, Tuple> fC = new HashMap<>();   // filledCapacity hashmap, usefull in algorithms

  /* SETTERS: */
  /**
   *
   * @param size amount of vertices in the flow network
   */
  public FlowNetwork(int size) {
    for(int i = 0; i < size; i++) V.add(i);
    this.size = size;
  }
  /**
   *
   * @param v1 vertex to start the directed edge from
   * @param v2 vertex to go to
   * @param cap capacity if the edge
   * @return True if Tuple(v1, v2) has been added to E, false otherwise
   */
  public boolean addEdge(int v1, int v2, int cap) {
    Tuple edge = new Tuple(v1, v2);         // Create a tuple representing an edge from v1 to v2
    C.put(edge, cap);                       // Put the capacity as a value to the key of the edge in the HashMap
    return E.add(edge);                     // Add the edge to the edges list and return whether it succeeded
  }
  /**
   *
   * @param edge Tuple to add to the edges list, fst is the start vertex, snd the going-to vertex
   * @param cap Capacity of the edge
   * @return True if Tuple(v1, v2) has been added to E, false otherwise
   */
  public boolean addEdge(Tuple edge, int cap) {
    return addEdge(edge.fst(), edge.snd(), cap);
  }
  /**
   *
   * @param v1 starting vertex of the edge to remove
   * @param v2 going-to vertex of the edge to remove
   * @return True if E contained Tuple(v1, v2) and we could remove it, false otherwise
   */
  public boolean removeEdge(int v1, int v2) {
    Tuple edge = new Tuple(v1, v2);
    fC.remove(edge);
    return E.remove(edge) && C.remove(edge, C.get(edge));
  }
  /**
   *
   * @param edge edge to remove with fst as start vertex and snd as going-to vertex
   * @return True if E contained edge, and we could remove it, false otherwise
   */
  public boolean removeEdge(Tuple edge) {
    fC.remove(edge);
    return E.remove(edge) && C.remove(edge, C.get(edge));
  }

  /* GETTERS: */
  public int getSize() { return this.size; }
  public ArrayList<Tuple> edges() { return this.E; }
  public ArrayList<Integer> vertices() { return this.V; }
  public HashMap<Tuple, Integer> capacities() { return this.C; }
  public HashMap<Tuple, Tuple> filledCapacities() { return this.fC; }
  public int largestCap() {
    int[] caps = {0};
    C.forEach((edge, cap)-> {
      if(caps[0] < cap) caps[0] = cap;
    });
    return caps[0];
  }

  /* ALGORITHMS: */
  public void capacityScaling() {
    int[] f = new int[E.size()];
    for(int e = 0; e < E.size(); e++) f[e] = 0;
    int d = 1;
    for(int n = 0; pow(2, n) < largestCap(); n++) d = (int)pow(2, n);

    while(d >= 1) {

      d /= 2;
    }
  }

}
