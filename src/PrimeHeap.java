import java.util.ArrayList;

public class PrimeHeap {
  private ArrayList<FastEntropySample> heapBody;

  public PrimeHeap() {
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
    while (pt>=1) {
      if (heapBody.get(pt).u0<heapBody.get((pt-1)/2).u0) {
        FastEntropySample tmp = heapBody.get(pt);
        heapBody.set(pt, heapBody.get((pt-1)/2));
        heapBody.set((pt-1)/2, tmp);
        pt = (pt-1)/2;
      }
      else {
        break;
      }
    }
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
        if (heapBody.get(left).u0<heapBody.get(right).u0) {
          if (heapBody.get(left).u0<heapBody.get(pt).u0) {
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
          if (heapBody.get(right).u0<heapBody.get(pt).u0) {
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
        if (left<=maxIndex && heapBody.get(left).u0<heapBody.get(pt).u0) {
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
}
