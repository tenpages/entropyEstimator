import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class FastStream {
  private int n, k, m;
  private double epsilon, delta;
  private HashMap<Integer, Integer> frequencyCounter;
  private ArrayList<FastEntropySample> sampleList;
  private Random random;
  private int c;
  private HashMap<Integer, CaItem> caDictionary;
  private PrimeHeap primeHeap;
  private BackupHeap bHeap;
  private int counter; //count the updates from 1 to m

  /**
   *
   * @param n domain size
   * @param epsilon
   * @param delta
   * @param m stream length
   */
  public FastStream(int n, double epsilon, double delta, int m) {
    setDomainSize(n);
    setStreamLength(m);
    setEpsilon(epsilon);
    setDelta(delta);

    setFrequencyEstimator(epsilon, delta);
    setSamplesMaintainer();
  }

  public void setDomainSize(int n) {
    this.n = n;
  }

  public void setStreamLength(int m) {
    this.m = m;
  }

  public void setEpsilon(double epsilon) {
    this.epsilon = epsilon;
  }

  public void setDelta(double delta) {
    this.delta = delta;
  }

  public void setRandom(int seed) {
    random = new Random(seed);
  }

  public void setRandom() {
    random = new Random();
  }

  public void setSamplesMaintainer() {
    c =(int) Math.ceil(16*Math.pow(epsilon,-2)*(Utils.lg(2/delta))*(Utils.lg(m*Math.exp(1))));
    caDictionary = new HashMap<>();
    primeHeap = new PrimeHeap();
    bHeap = new BackupHeap();
    counter = 0;
    sampleList = new ArrayList<>();
    for (int i=0; i<c; i++) {
      FastEntropySample tmp = new FastEntropySample();
      sampleList.add(tmp);
      //primeHeap.push(tmp);
    }
  }

  public void setFrequencyEstimator(double epsilon, double delta) {
    this.k = (int) Math.ceil(7*Math.pow(epsilon, -1));
    this.frequencyCounter = new HashMap<>();
  }

  public void newStreamUpdate(int update) throws Exception {
    this.counter += 1;
    //System.out.println("On update No."+counter+": "+update);
    maintainFrequency(update);
    maintainSamples(update);
  }

  public void maintainSamples(int update) throws Exception {
    /*
    if (caDictionary.size()==0) {
      bHeap.push(new Pair(update, 0));
    }
    */
    if (caDictionary.size()==0) {
      caDictionary.put(update,new CaItem());
      for (FastEntropySample e: sampleList) {
        e.s0=update;
        e.t0=random.nextDouble();
        e.r0=1;
      }
      return;
    }

    if (caDictionary.size()==1 && caDictionary.containsKey(update)) {
      for (FastEntropySample e: sampleList) {
        double t = random.nextDouble();
        if (t < e.t0) {
          e.t0 = t;
          e.r0 = 1;
        }
      }
      return;
    }

    if (caDictionary.size()==1) {
      caDictionary.put(update,new CaItem());
      for (FastEntropySample e: sampleList) {
        double t = random.nextDouble();
        double f0 = random.nextDouble();
        double f1 = random.nextDouble();
        if (t < e.t0) {
          e.setGroup0AsGroup1();
          e.setGroup0(update,t,1);
        }
        else {
          e.setGroup1(update,t,1);
        }

        int u = Utils.Geom(e.t0);
        if (e.t0==0.0) {
          e.u0 = Utils.MAXINT;
        }
        else {
          e.u0 = counter + u;
        }
        if (e.u0<0 || e.u0>Utils.MAXINT) {
          e.u0 = Utils.MAXINT;
        }

        u = Utils.Geom((e.t1-e.t0));
        if (e.t1-e.t0==0.0) {
          e.u1 = Utils.MAXINT;
        }
        else {
          e.u1 = counter + u;
        }
        if (e.u1<0 || e.u1>Utils.MAXINT) {
          e.u1 = Utils.MAXINT;
        }

        primeHeap.push(e);
        int existT = bHeap.find(e.s0);
        if (existT==-1.0) {
          bHeap.push(new Pair(e.s0, e.u0));
        }
        else {
          if (existT<e.u0) {
            bHeap.update(e.s0,e.u0);
          }
        }
        caDictionary.get(e.s0).subheap.push(e);
      }
      //System.out.println(bHeap.peekMin());
      //System.out.println(bHeap.heapBody.get(1));
      //throw new Exception();
      return;
    }

    if (caDictionary.containsKey(update)) {
      caDictionary.get(update).count += 1;
    }

    while (primeHeap.peekMin().u0<=this.counter) {
      FastEntropySample x = primeHeap.pop();
      double t = random.nextDouble()*x.t0 ;//random.nextInt(x.t0-1)+1;
      if (x.s0!=update) {
        if (x.s0!=0) {
          int yIndex = caDictionary.get(x.s0).subheap.find(x);
          if (yIndex != -1) {
            caDictionary.get(x.s0).subheap.delete(yIndex);
          }//9
          x.setGroup0AsGroup1();
          int u = Utils.Geom(x.t0 - t);
          if (u >= Utils.MAXINT - counter) {
            x.u1 = Utils.MAXINT;
          } else {
            x.u1 = counter + u;
          }
        }
        if (!caDictionary.containsKey(update)) {
          caDictionary.put(update, new CaItem());
        } //11
        caDictionary.get(update).subheap.push(x); //10
      }
      x.setGroup0(update,t,caDictionary.get(update).count);
      int u = Utils.Geom(t);
      if (u >= Utils.MAXINT - counter) {
        x.u0 = Utils.MAXINT;
      }
      else {
        x.u0 = counter + u;
      }
      primeHeap.push(x);
    }

    bHeap.delay(update);

    //System.out.println(bHeap.peekMin().getValue());
    while (bHeap.peekMin().getValue()<=this.counter) {
      //System.out.println("check");
      if (!caDictionary.containsKey(update)) {
        caDictionary.put(update, new CaItem());
      }
      Pair y = bHeap.pop();
      FastEntropySample z = caDictionary.get(y.getKey()).subheap.pop();
      double t = random.nextDouble()*(z.t1-z.t0)+z.t0;//nextInt(z.t1-z.t0-1)+z.t0+1;
      z.setGroup1(update,t,caDictionary.get(update).count);
      int u = Utils.Geom(t-z.t0);
      if (u>= Utils.MAXINT - counter) {
        z.u1 = Utils.MAXINT;
      }
      else {
        z.u1 = counter + u;
      }
      caDictionary.get(y.getKey()).subheap.push(z);
      y.setValue(caDictionary.get(y.getKey()).subheap.peekMin().u1);
      bHeap.push(y);
    }
  }

  public void maintainFrequency(int updates) {
    if (frequencyCounter.containsKey(updates)) {
      frequencyCounter.put(updates, frequencyCounter.get(updates)+1);
    }
    else {
      if (frequencyCounter.size()<this.k) {
        frequencyCounter.put(updates,1);
      }
      else {
        frequencyCounter.replaceAll((k,v) -> {
          return v-1;
        });
        ArrayList<Integer> emptyPos = new ArrayList<>();
        frequencyCounter.forEach((k,v) -> {
          if (v==0) {
            emptyPos.add(k);
          }
        });
        for (Integer i: emptyPos) {
          frequencyCounter.remove(i);
        }
      }
    }
  }

  public HashMap.Entry<Integer, Integer> mostFrequentItem() {
    int maxCount = 0;
    HashMap.Entry<Integer, Integer> ans = null;

    Iterator iter = frequencyCounter.entrySet().iterator();
    while (iter.hasNext()) {
      HashMap.Entry<Integer,Integer> entry = (HashMap.Entry) iter.next();
      Integer value = entry.getValue();
      if (value>maxCount) {
        ans = entry;
        maxCount = value;
      }
    }
    return ans;
  }

  public double entropyEstimator() {
    HashMap.Entry<Integer, Integer> mostFrequent = mostFrequentItem();
    int iMax = mostFrequent.getKey();
    int mMax = mostFrequent.getValue();

    double Est = 0;
    if (mMax>m/2) {
      //System.out.println("sampleSize = "+c);
      for (FastEntropySample sample : sampleList) {
        int r;
        if (sample.s0==iMax) {
          r = caDictionary.get(sample.s1).count - sample.r1 + 1;
          //r = sample.r1;
        }
        else {
          r = caDictionary.get(sample.s0).count - sample.r0 + 1;
          //r = sample.r0;
        }
        Est += Utils.lambda(r,m)-Utils.lambda(r-1,m);
      }
      //System.out.println(Est);
      Est = (1-(double)mMax/m)*Est/c + ((double)mMax/m)*Utils.lg((double)m/mMax);
    }
    else {
      for (FastEntropySample sample : sampleList) {
        int r = caDictionary.get(sample.s0).count - sample.r0 + 1;
        //int r = sample.r0;
        Est += Utils.lambda(r,m) - Utils.lambda(r-1,m);
      }
      Est = Est/c;
    }
    return Est;
  }
}
