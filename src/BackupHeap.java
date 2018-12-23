import java.util.ArrayList;

public class BackupHeap {
  protected ArrayList<Pair> heapBody;

  public BackupHeap() {
    heapBody = new ArrayList<>();
  }

  public Pair peekMin() {
    return heapBody.get(0);
  }

  public Pair pop() {
    Pair top = heapBody.get(0);
    heapBody.set(0,heapBody.get(heapBody.size()-1));
    heapBody.remove(heapBody.size()-1);
    this.heapify(0);
    return top;
  }

  public void push(Pair newItem) {
    heapBody.add(newItem);
    int pt = heapBody.size()-1;
    while (pt>=1) {
      if (heapBody.get(pt).getValue()<heapBody.get((pt-1)/2).getValue()) {
        Pair tmp = heapBody.get(pt);
        heapBody.set(pt, heapBody.get((pt-1)/2));
        heapBody.set((pt-1)/2, tmp);
        pt = (pt-1)/2;
      }
      else {
        break;
      }
    }
  }

  public Pair delete(int pos) {
    Pair target = heapBody.get(pos);
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
        if (heapBody.get(left).getValue()<heapBody.get(right).getValue()) {
          if (heapBody.get(left).getValue()<heapBody.get(pt).getValue()) {
            Pair tmp = heapBody.get(left);
            heapBody.set(left, heapBody.get(pt));
            heapBody.set(pt, tmp);
            pt = left;
          }
          else {
            break;
          }
        }
        else {
          if (heapBody.get(right).getValue()<heapBody.get(pt).getValue()) {
            Pair tmp = heapBody.get(right);
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
        if (left<=maxIndex && heapBody.get(left).getValue()<heapBody.get(pt).getValue()) {
          Pair tmp = heapBody.get(left);
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

  public void delay(int token) {
    for (int i=0; i<heapBody.size(); i++) {
      if (heapBody.get(i).getKey()==token) {
        heapBody.get(i).setValue(heapBody.get(i).getValue()+1);
        heapify(i);
        break;
      }
    }
  }

  public int find(int token) {
    for (int i=0; i<heapBody.size(); i++) {
      if (heapBody.get(i).getKey()==token) {
        return heapBody.get(i).getValue();
      }
    }
    return -1;
  }

  public void update(int token, int u) {
    for (int i=0; i<heapBody.size(); i++) {
      if (heapBody.get(i).getKey()==token && heapBody.get(i).getValue()>u) {
        heapBody.get(i).setValue(u);
        heapify(i);
        return;
      }
    }
  }
}
