package main;

import java.util.LinkedList;
import java.util.List;

public class MyQueue<E> {
  private final List<E> q = new LinkedList<E>();

  public void enq(E el) {
    q.add(el);
  }
  public Object deq() throws NullPointerException {
    if(q.size() > 0) {
      return q.remove(0);
    } else {
      throw new NullPointerException();
    }
  }
  public int size() {
    return q.size();
  }
  public Boolean isEmpty() {
    return q.isEmpty();
  }
  public String toString() {
    StringBuilder s = new StringBuilder();
    s.append("[");
    for(int i = 0; i < q.size(); i++) {
      s.append("'").append(q.get(i)).append("'");
      if(q.size() > 1 && i != q.size()-1) s.append(", ");
    }
    s.append("]");
    return s.toString();
  }
}

/*
  All code runs in constant time so O(1)

 */
