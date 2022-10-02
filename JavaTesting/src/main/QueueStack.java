package main;

public class QueueStack {
  private final MyQueue<Object> q1 = new MyQueue<>(); // the main queue
  private final MyQueue<Object> q2 = new MyQueue<>(); // our helper queue
  public void push(Object el) {
    if(q1.isEmpty()) {                  // if q1 is empty then
      q1.enq(el);                       // we simply add the element to q1
    } else {                            // otherwise
      int size = q1.size();             // we set the size q1 has before adding
      for(int i = 0; i < size; i++) {   // loop over all items in q1
        q2.enq(q1.deq());               // and enqueue them into q2
      }
      q1.enq(el);                       // now add the element to the currently empty q1
      for(int i = 0; i < size; i++) {   // loop over everything in q2
        q1.enq(q2.deq());               // and add them to q1
      }
    }
  }
  public Object pop() {                 // because we made a "reversed" queue it basically is a stack
    return q1.deq();                    // so we return the dequeued element of the main queue
  }
  public void show() {
    System.out.println(q1);
  }
  public boolean isEmpty() {
    return (q1.isEmpty());
  }
}

/*
  Worst-case analysis of push:
    in the worst case we have to loop 2 times over the size of the queue so linear with the queue size O(n)
  Worst-case analysis of pop:
    we always can immediately dequeue from the main queue and return so 1 step / constant time O(1)
 */