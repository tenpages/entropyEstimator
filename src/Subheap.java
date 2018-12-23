import java.util.ArrayList;

public class Subheap {
  protected ArrayList<FastEntropySample> heapBody;

  public Subheap() {
    heapBody = new ArrayList<>();
  }

  public FastEntropySample peekMin() {
    return heapBody.get(0);
  }

  public FastEntropySample pop() {
    FastEntropySample top = heapBody.get(0);
    heapBody.set(0,heapBody.get(heapBody.size()-1));
    heapBody.remove(heapBody.size()-1);
    this.heapify(0);
    return top;
  }

  public void push(FastEntropySample newItem) {
    heapBody.add(newItem);
    int pt = heapBody.size()-1;
    //System.out.print("Pushing in "+newItem.u1+" "+pt);
    while (pt>0) {
      if (heapBody.get(pt).u1<heapBody.get((pt-1)/2).u1) {
        FastEntropySample tmp = heapBody.get(pt);
        heapBody.set(pt, heapBody.get((pt-1)/2));
        heapBody.set((pt-1)/2, tmp);
        pt = (pt-1)/2;
      }
      else {
        break;
      }
    }
    //System.out.println(" "+pt);
  }

  public FastEntropySample delete(int pos) {
    FastEntropySample target = heapBody.get(pos);
    heapBody.set(pos, heapBody.get(heapBody.size()-1));
    heapBody.remove(heapBody.size()-1);
    this.heapify(pos);
    return target;
  }

  public void heapify(int pos) {
    int pt = pos;
    int maxIndex = heapBody.size()-1;

    while (pt<=maxIndex) {
      int left = pt*2+1;
      int right = pt*2+2;

      if (right<=maxIndex) {
        if (heapBody.get(left).u1<heapBody.get(right).u1) {
          if (heapBody.get(left).u1<heapBody.get(pt).u1) {
            FastEntropySample tmp = heapBody.get(left);
            heapBody.set(left, heapBody.get(pt));
            heapBody.set(pt, tmp);
            pt = left;
          }
          else {
            break;
          }
        }
        else {
          if (heapBody.get(right).u1<heapBody.get(pt).u1) {
            FastEntropySample tmp = heapBody.get(right);
            heapBody.set(right, heapBody.get(pt));
            heapBody.set(pt, tmp);
            pt = right;
          }
          else {
            break;
          }
        }
      }
      else {
        if (left<=maxIndex && heapBody.get(left).u1<heapBody.get(pt).u1) {
          FastEntropySample tmp = heapBody.get(left);
          heapBody.set(left, heapBody.get(pt));
          heapBody.set(pt, tmp);
          pt = left;
        }
        else {
          break;
        }
      }
    }
  }

  public int find(FastEntropySample x) {
    if (heapBody.contains(x)) {
      return heapBody.indexOf(x);
    }
    else {
      return -1;
    }
  }

  /*
  public static void main(String[] args) {
    Subheap sheap = new Subheap();
    FastEntropySample a = new FastEntropySample();
    a.u1 = 20;
    FastEntropySample b = new FastEntropySample();
    b.u1 = 30;
    FastEntropySample c = new FastEntropySample();
    c.u1 = 40;
    FastEntropySample d = new FastEntropySample();
    d.u1 = 50;
    sheap.push(c);
    System.out.println(sheap.heapBody.get(0).u1);
    System.out.println();
    sheap.push(d);
    System.out.println(sheap.heapBody.get(0).u1);
    System.out.println(sheap.heapBody.get(1).u1);
    System.out.println();
    sheap.push(b);
    System.out.println(sheap.heapBody.get(0).u1);
    System.out.println(sheap.heapBody.get(1).u1);
    System.out.println(sheap.heapBody.get(2).u1);
    System.out.println();
    sheap.push(a);
    System.out.println(sheap.heapBody.get(0).u1);
    System.out.println(sheap.heapBody.get(1).u1);
    System.out.println(sheap.heapBody.get(2).u1);
    System.out.println(sheap.heapBody.get(3).u1);
    System.out.println();
    a.u1=45;
    sheap.heapify(0);
    System.out.println(sheap.heapBody.get(0).u1);
    System.out.println(sheap.heapBody.get(1).u1);
    System.out.println(sheap.heapBody.get(2).u1);
    System.out.println(sheap.heapBody.get(3).u1);
  }
  */
}
