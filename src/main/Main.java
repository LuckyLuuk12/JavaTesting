package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
  public static void main(String[] args) throws IOException {
    String input = "";
    QueueStack qs = new QueueStack();
    GraphList dg = new GraphList(8);
    Crypto c = new Crypto();

    while(!input.equalsIgnoreCase("exit")) {
      System.out.print("$ ");
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); // Enter data using BufferReader
      input = reader.readLine(); // Reading data using readLine
      String[] pars = input.split(" "); // get parameters by spaces
      // Execute code after input:
      if(input.toLowerCase().contains("help")) {
        System.out.print(
          """
            \u001B[31m~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~ Help - page 1 ~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~
            \u001B[33m 1. fac <number>               \u001B[37m| returns the factorial of <number>
            \u001B[33m 2. qs-push <object>           \u001B[37m| converts object to string and pushes it to the QueueStack
            \u001B[33m 3. qs-pop                     \u001B[37m| pops from the QueueStack and shows what it popped
            \u001B[33m 4. qs-show                    \u001B[37m| prints the stack
            \u001B[33m 4. qs-sizeandweight <s> <b>   \u001B[37m| resets the grapg with new size and boolean whether it is weighted
            \u001B[33m 5. dg-set <v1> <v2>           \u001B[37m| sets an edge from <v1> to <v2>
            \u001B[33m 6. dg-wset <v1> <v2> <weight> \u001B[37m| sets an edge from <v1> to <v2> with a certain weight
            \u001B[33m 7. dg-setlist <array>         \u001B[37m| sets edges from an array formatted [[0,1],[1,2],..]
            \u001B[33m 8. dg-show                    \u001B[37m| shows all specified data of the Directed Graph
            \u001B[33m 9. dg-size <number>           \u001B[37m| sets the amount of vertices AND resets the Graph
            \u001B[33m10. dg-sink                    \u001B[37m| returns a vertex which is an Universal Sink
            \u001B[33m11. dg-idfs <number>           \u001B[37m| performs iterative DFS starting at <number>, starts at 0 if <number> is not given
            \u001B[33m12. dg-rdfs <number>           \u001B[37m| performs recursive DFS starting at <number>, starts at 0 if <number> is not given
            \u001B[33m13. dg-mother                  \u001B[37m| returns a mothervertex, if there is none it returns -1
            \u001B[33m14. dg-dijkstra                \u001B[37m| performs dijkstra if the graph is weighted
            \u001B[33m15. dg-bf                      \u001B[37m| performs Bellman-Ford if the graph is weighted
            \u001B[33m16. lfsr <bitstring> <number>  \u001B[37m| returns an LFSR'ed bitstring shifted <number> times
            \u001B[33m17. inc <bitstring>            \u001B[37m| increments the given <bitstring> by 1
            \u001B[33m18. findkey <outputStream>     \u001B[37m| performs exhaustive key search to find the original input of the <outputStream>
            \u001B[0m
            """);
      }
      else if(input.toLowerCase().contains("fac") && pars.length > 1) { System.out.println("> fac "+input+" = "+fac(Long.parseLong(pars[1]))); }
      else if(input.toLowerCase().contains("qs-push ") && pars.length > 1) {
        qs.push(pars[1]);
        System.out.println("> Pushed "+input+" to the QueueStack");
      }
      else if(input.toLowerCase().contains("qs-pop")) { System.out.println("> Popped "+qs.pop()+" from the QueueStack"); }
      else if(input.toLowerCase().contains("qs-show") || input.toLowerCase().contains("print stack")) { qs.show(); }
      else if(input.toLowerCase().contains("dg-sizeandweight") && pars.length > 2) {
        dg = new GraphList(Integer.parseInt(pars[1]), Boolean.parseBoolean(pars[2]));
      }
      else if(input.toLowerCase().contains("dg-set") && pars.length > 2) {
        boolean done = dg.setEdge(Integer.parseInt(pars[1]), Integer.parseInt(pars[2]));
        if(done) System.out.println("> Set an edge from "+pars[1]+" to "+pars[2]);
      }
      else if(input.toLowerCase().contains("dg-wset") && pars.length > 3) {
        boolean done = dg.setWeightedEdge(Integer.parseInt(pars[1]), Integer.parseInt(pars[2]), Integer.parseInt(pars[3]));
        if(done) System.out.println("> Set an edge from "+pars[1]+" to "+pars[2]+" weighted "+pars[3]);
      }
      else if(input.toLowerCase().contains("dg-setlist") && pars.length > 1) {
        for(String[] s : to2dArray(pars[1])) dg.setEdge(Integer.parseInt(s[0]), Integer.parseInt(s[1]));
      }
      else if(input.toLowerCase().contains("dg-show")) { dg.show(); dg.showPredecessors(); }
      else if(input.toLowerCase().contains("dg-size") && pars.length > 1) { dg = new GraphList(Integer.parseInt(pars[1])); }
      else if(input.toLowerCase().contains("dg-sink")) { System.out.println(dg.detectSink()); }
      else if(input.toLowerCase().contains("dg-idfs")) {
        if(pars.length > 1) dg.iterativeDFS(Integer.parseInt(pars[1])); else dg.iterativeDFS(0);
      }
      else if(input.toLowerCase().contains("dg-rdfs")) {
        if(pars.length > 1) dg.recursiveDFS(Integer.parseInt(pars[1])); else dg.recursiveDFS(0);
      }
      else if(input.toLowerCase().contains("dg-mother")) { System.out.println("Mothervertex: "+dg.hasMotherVertex()); }
      else if(input.toLowerCase().contains("dg-dijkstra")) {
        if(pars.length > 1) dg.dijkstra(Integer.parseInt(pars[1])); else dg.dijkstra(0);
      }
      else if(input.toLowerCase().contains("dg-bf")) {
        if(pars.length > 1) dg.bellmanFord(Integer.parseInt(pars[1])); else dg.bellmanFord(0);
        System.out.println("> "+dg.hasNegativeCycle());
      }
      else if(input.toLowerCase().contains("lfsr ") && pars.length > 2) {
        c.LFSR(stringToList(pars[1]), Integer.parseInt(pars[2]));
      }
      else if(input.toLowerCase().contains("inc ") && pars.length > 1) {
        System.out.println(c.binaryStringIncreaser(pars[1]));
      }
      else if(input.toLowerCase().contains("findkey ") && pars.length > 1) {
        System.out.println(c.exhaustiveKeySearch(pars[1]));
      }
      else { System.out.println("Invalid command.."); }
    }
  }
  // dg-setlist [[0,1],[0,5],[1,2],[1,4],[2,3],[3,1],[3,7],[4,3],[4,6],[5,4],[5,6],[6,5],[7,6]]
  public static long fac(long n) {
    if(n == 0) return 1;
    return n * fac(n-1);
  }
  public static String[][] to2dArray(String s) {
    return Arrays.stream(s.substring(2, s.length() - 2)
        .split("\\],\\["))
        .map(e -> Arrays.stream(e.split("\\s*,\\s*"))
        .toArray(String[]::new)).toArray(String[][]::new);
  }
  public static ArrayList<Integer> stringToList(String s) {
    ArrayList<Integer> l = new ArrayList<>();
    for(int c = 0; c < s.length(); c++) {
      l.add(Integer.parseInt(""+s.charAt(c)));
    }
    return l;
  }

}