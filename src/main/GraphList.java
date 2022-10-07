package main;

import java.lang.reflect.Array;
import java.util.*;

/*
Graph implemented with a list containing a list for every vertex in which connection are put
 */
public class GraphList {
  private final int size;
  private boolean isWeighted = false;
  private boolean hasNegativeCycle = false;
  private final HashMap<Tuple, Integer> weights = new HashMap<>(); // CREATE HASHMAP with array key and weight as value
  ArrayList<ArrayList<Integer>> graph; // = new ArrayList<>(size);
  ArrayList<Tuple> edges = new ArrayList<>();
  /* for the search algorithms */
  private final int[] explored, discovered, predecessor, dtime, ftime;
  private final int UNDISCOVERED = -1;
  private final int DISCOVERED = 1;
  private final int NULL = -2;
  private final int INF = Integer.MAX_VALUE;

  public GraphList(int size) {
    this.size = size;
    this.graph = new ArrayList<>(size);
    for(int i=0; i < size; i++) {
      graph.add(new ArrayList<>());
    }
    this.explored = new int[size];
    this.discovered = new int[size];
    this.predecessor = new int[size];
    this.dtime = new int[size];
    this.ftime= new int[size];
  }
  /**
   * @param size amount of vertices in the graph
   * @param weighted whether the edges have a weight
   */
  public GraphList(int size, boolean weighted) {
    this.size = size;
    this.isWeighted = weighted;
    this.graph = new ArrayList<>(size);
    for(int i=0; i < size; i++) {
      graph.add(new ArrayList<>());
    }
    this.explored = new int[size];
    this.discovered = new int[size];
    this.predecessor = new int[size];
    this.dtime = new int[size];
    this.ftime= new int[size];
  }
  public int size() {
    return this.size;
  }
  /**
   * @param v1 starting vertex
   * @param v2 ending vertex
   * @return whether setting the edge succeeded
   */
  public boolean setEdge(int v1, int v2) {
    if(this.graph.get(v1).contains(v2)) {
      return false;
    } else {
      return this.graph.get(v1).add(v2) && this.edges.add(new Tuple(v1, v2));
    }
  }
  /**
   * @param v1 starting vertex
   * @param v2 ending vertex
   * @param weight weight of the edfe
   * @return whether setting the edge succeeded
   */
  public boolean setWeightedEdge(int v1, int v2, int weight) {
    if(this.graph.get(v1).contains(v2)) {
      return false;
    } else {
      weights.put(new Tuple(v1, v2), weight);
      return this.graph.get(v1).add(v2) && this.edges.add(new Tuple(v1, v2));
    }
  }

  /**
   * @param v starting vertex for iterative Depth First Search
   */
  public void iterativeDFS(int v) {
    // Initialize Depth First Search
    LinkedList<Integer> traversed = new LinkedList<>();
    for(int u = 0; u < this.size; u++) {
      if(u != v) {                                            // if u is not the node where we start
        discovered[u] = UNDISCOVERED;                         // u is undiscovered
        predecessor[u] = NULL;                                // u has no predecessor
      }
    }
    discovered[v] = DISCOVERED;                               // v is discovered
    predecessor[v] = NULL;                                    // v has no predecessor
    Stack<Integer> S = new Stack<>();
    S.push(v);
    // Main loop
    while(!S.isEmpty()) {
      int u = S.pop();
      discovered[u] = DISCOVERED;                             // we popped it thus explored it
      if(!traversed.contains(u)) traversed.add(u);
      for(int w = this.graph.get(u).size()-1; w >= 0; w--) {  // loop over the list of neighbours of u in reversed order
        int y = this.graph.get(u).get(w);                     // set y to be the vertex at index w in the neighbour list of u
        if(discovered[y] == UNDISCOVERED) {                   // if w is not discovered
          predecessor[y] = u;                                 // set its predecessor to u
          S.push(y);                                          // we found something so push it to the stack
        }
      }
    }
  }
  /**
   * @param v starting vertex for recursive initiation phase Depth First Search
   */
  public void recursiveDFS(int v) {
    for(int u = 0; u < this.size; u++) {
      if(u != v) {
        this.explored[u] = UNDISCOVERED;
        this.predecessor[u] = NULL;
      }
    }
    this.explored[v] = DISCOVERED;
    this.predecessor[v] = NULL;
    rDFS(v);
  }
  /**
   * @param v starting vertex for recursive looping phase Depth First Search
   */
  private void rDFS(int v) {
    for(int w = 0; w < this.graph.get(v).size(); w++) {  // loop over the list of neighbours of u in reversed order
      int u = this.graph.get(v).get(w);                     // set y to be the vertex at index w in the neighbour list of u
      if(this.explored[u] == UNDISCOVERED) {
        this.explored[u] = DISCOVERED;
        this.predecessor[u] = v;
        rDFS(u);
      }
    }
  }
  /**
   * @param v starting vertex for Dijkstra's algorithm
   */
  public void dijkstra(int v) {
    if(!isWeighted) return;
    int[] d = new int[size];
    d[v] = 0;
    PriorityQueue<Tuple> q = new PriorityQueue<>();             // queue of tuples, fst = vertex, snd = priority
    q.add(new Tuple(v, 0));                                     // at the starting vertex with priority 0
    predecessor[v] = NULL;
    for(int u = 0; u < this.size; u++) {                        // for all vertices in the graph
      if(u != v) {                                              // if u is not the node where we start
        d[u] = INF;                                             // distance to u is INFINITE
        predecessor[u] = NULL;                                  // u has no predecessor
        q.add(new Tuple(u, INF));
      }
    }
    while(!q.isEmpty()) {
      Tuple popped = q.poll();
      int u = popped.fst();
      for(int y = this.graph.get(u).size()-1; y >= 0; y--) {  // loop over the list of neighbours of u in reversed order
        int w = this.graph.get(u).get(y);                     // set y to be the vertex at index w in the neighbour list of u
        int newV = d[u] + weights.get(new Tuple(u, w));
        if(newV < d[w]) {
          d[w] = newV;
          predecessor[w] = u;
          q.removeIf(e -> e.equals(new Tuple(w, weights.get(new Tuple(u, w))))); q.add(new Tuple(w, newV)); // q.decreasePriority(w, newV);
        }
      }
    }
  }
  /**
   * @param v starting vertex for the Bellman-Ford algorithm to detect negative cycles
   */
  public void bellmanFord(int v) {
    if(!isWeighted) return;
    int[] d = new int[this.size];
    for(int u = 0; u < this.size; u++) {
      d[u] = INF;
    }
    d[v] = 0;
    for(int i = 1; i < this.size-1; i++) {
      for(Tuple t : edges) {
        int u = t.fst(); int w = t.snd();
        if(d[u] + weights.get(new Tuple(u, w)) < d[w]) {
          d[w] = d[u] + weights.get(new Tuple(u, w));
        }
      }
    }
    for(Tuple t : edges) {
      int u = t.fst(); int w = t.snd();
      if(d[u] + weights.get(new Tuple(u, w)) < d[w]) this.hasNegativeCycle = true;
    }
  }
  public void showPredecessors() {
    for(int i = 0; i < this.predecessor.length; i++) {
      System.out.println("Predecessor of "+i+" is "+this.predecessor[i]);
    }
  }
  /**
   * @return -1 if no universal sink was found, otherwise the number of the vertex
   */
  public int detectSink() {                     // list of vertex itself is empty && goesTo([0..size]\i], i)
    for(int i = 0; i < this.size; i++) {
      int edgeCount = this.graph.get(i).size(); // count how many outgoing edges node i has
      if(edgeCount != 0) continue;              // if it is not 0 we can skip because a sink has no outgoing edges
      boolean hasAllOutgoing = true;
      for(int j = 0; j < this.size; j++) {      // loop over all
        if(!goesTo(j, i) && j != i) {           // if node j does not go to node i and j is not i
          hasAllOutgoing = false;               // then i can't have an incoming edge from all other nodes
          break;                                // thus can't be a sink and we can break
        }
      }

      if(hasAllOutgoing) return i;// if it has no outgoing edges but all vertices have an edge going to it
    }
    return -1;
  }
  /**
   * @return -1 if no mothervertex has been found, otherwise the number of the vertex
   */
  public int hasMotherVertex() { // Return -1 if not found and any int higher for the mothervertex
    int v = -1;
    for(int i = 0; i < this.size; i++) explored[i] = UNDISCOVERED;
    for(int u = 0; u < this.size; u++) {
      if(explored[u] == UNDISCOVERED) {
        recursiveDFS(u);
        v = u;
      }
    }
    for(int i = 0; i < this.size; i++) explored[i] = UNDISCOVERED;
    recursiveDFS(v);
    for(int w = 0; w < this.size; w++) {
      if(explored[w] == UNDISCOVERED) return -1;
    }
    return v;
  }
  /**
   * @param from vertex to start at
   * @param to vertex to look at
   * @return whether vertex  `from`  has a direct connection to vertex  `to`
   */
  private boolean goesTo(int from, int to) {
    return edges.contains(new Tuple(from, to));
  }
  public boolean hasNegativeCycle() {
    return this.hasNegativeCycle;
  }
  public void show() {
    for (int i = 0; i < size; i++) {
      int edgeCount = graph.get(i).size();
      System.out.print(i+": ");
      for (int j = 0; j < edgeCount; j++) { System.out.print(graph.get(i).get(j)+" "); }
      System.out.print("\n");
    }
  }
}
