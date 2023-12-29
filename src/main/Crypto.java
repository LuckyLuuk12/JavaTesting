package main;

import java.util.ArrayList;
import java.util.Arrays;
public class Crypto {
  private ArrayList<Integer> stream = new ArrayList<>();
  private int z = 0;

  public ArrayList<Integer> LFSR(ArrayList<Integer> L, int shift) {
    int xorBit = L.get(L.size()-1);
    ArrayList<Integer> tempL = (ArrayList<Integer>) L.clone();
    System.out.println("> "+ L + "\t=>\t z_"+ z + " = " + L.get(1 + L.get(6) + 2*L.get(7)));
    stream.add(L.get(1 + L.get(6) + 2*L.get(7)));
    for(int i = 1; i < L.size(); i++) {
      if(i == 1 || i == 3 || i == 5) { // HARDCODED XORS
        L.set(i, xor(xorBit, tempL.get(i-1)));
      } else {
        L.set(i, (tempL.get(i-1)));
      }
    }
    L.set(0, xorBit);
    z++;
    if(shift > 1) {
      LFSR(L, shift-1);
    }
    return L;
  }

  public String exhaustiveKeySearch(String outputStream) {
    String result = "0000000000";
    String input = "00000000";

    while(!outputStream.equalsIgnoreCase(result)) {
      stream = new ArrayList<>();
      LFSR(stringToList(input), 10);
      StringBuilder sb = new StringBuilder();
      for(int i : stream) {
        sb.append(i);
      }
      result = sb.toString();
      //System.out.println(outputStream + " == " + result + " for input " + input);
      if(outputStream.equalsIgnoreCase(result)) return input;
      input = binaryStringIncreaser(input);
    }
    return "Key not found!";
  }

  public String binaryStringIncreaser(String s) {
    ArrayList<Integer> L = stringToList(s);
    L.set(L.size()-1, L.get(L.size()-1)+1);
    for(int i = L.size()-1; i > 0; i--) {
      if(L.get(i) == 2) {
        L.set(i, 0);
        L.set(i-1, L.get(i-1)+1);
      }
    }
    if(L.get(0) > 1) {
      System.out.println("Overflow Error!");
      return "";
    }
    StringBuilder sb = new StringBuilder();
    for(int i : L) sb.append(i);
    return sb.toString();
  }

  private int xor(int v1, int v2) {
    return (v1+v2)%2;
  }
  public static ArrayList<Integer> stringToList(String s) {
    ArrayList<Integer> l = new ArrayList<>();
    for(int c = 0; c < s.length(); c++) {
      l.add(Integer.parseInt(""+s.charAt(c)));
    }
    return l;
  }

  public static byte[] increment(byte[] array) {
    byte[] r = array.clone();
    for ( int i = array.length - 1; i >= 0; i-- ) {
      byte x = array[ i ];
      if ( x == -1 )
        continue;
      r[ i ] = (byte) (x + 1);
      Arrays.fill( r, i + 1, array.length, (byte) 0 );
      return r;
    }
    throw new IllegalArgumentException( Arrays.toString( array ) );
  }

  /* Modulo calculation stuff: */
  public void pollardsRho() {
    int a = 15; // given by exercise
    int b = 1;  // given by exercise
    int c = 0;  // given by exercise
    int q = 31; // modulo of the group
    int l = 10; // order of the generator / subgroup
    int h = 4;  // the right hand side of g^x = h (mod q)
    int g = 15; // generator
    ArrayList<Integer> aAlreadyFound = new ArrayList<>();
    for(int i = 1; i <= 0.5*q; i++) {
      if(aAlreadyFound.contains(a)) break;
      aAlreadyFound.add(a);
      System.out.println("Iteration "+i+":\\\\");
      if(a%3 == 1) {
        System.out.println("$a_{"+(i-1)+"} \\mod 3 = 1$ so we get $(a\\cdot g, b+1, c) = $\\\\");
        a = (a * g) % q;
        b = (b + 1) % l;
      } else if(a%3 == 2) {
        System.out.println("$a_{"+(i-1)+"} \\mod 3 = 2$ so we get $(a\\cdot h, b, c+1) = $\\\\");
        a = (a * h) % q;
        c = (c + 1) % l;
      } else {
        a = (a * a) % q;
        System.out.println("$a_{"+(i-1)+"} \\mod 3 = 0$ so we get $(a^2, 2b, 2c) = $\\\\");
        b = (2 * b) % l;
        c = (2 * c) % l;
      }
      System.out.println("$(a_{"+i+"}, b_{"+i+"}, c_{"+i+"}) = ("+a+", "+b+", "+c+")$\\\\");
    }
  }
}
