package main;

import java.util.Objects;

public class Tuple implements Comparable {
  private int v1;
  private int v2;
  private int hashCode;

  public Tuple(int v1, int v2) {
    this.v1 = v1;
    this.v2 = v2;
    this.hashCode = Objects.hash(v1, v2);
  }

  public int fst() {
    return v1;
  }
  public int snd() {
    return v2;
  }
  @Override
  public int compareTo(Object o) {
    if(o instanceof Tuple) {
      return Integer.compare(snd(), ((Tuple) o).snd());
    } else {
      return 1;
    }
  }
  @Override
  public String toString() {
    return "("+v1+", "+v2+")";
  }
  @Override
  public boolean equals(Object o) {
    return o instanceof Tuple && fst() == ((Tuple) o).fst() && snd() == ((Tuple) o).snd();
  }
  @Override
  public int hashCode() {
    return this.hashCode;
  }
}
