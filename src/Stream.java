import java.util.*;

public class Stream {
  private int n, k, m;
  private double epsilon, delta;
  private HashMap<Integer, Integer> frequencyCounter;
  private ArrayList<EntropySample> sampleList;
  private Random random;
  private int c;

  /**
   *
   * @param n domain size
   * @param epsilon
   * @param delta
   * @param m stream length
   */
  public Stream(int n, double epsilon, double delta, int m) {
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
    c =(int) Math.ceil(16*1/(epsilon*epsilon)*(Utils.lg(2/delta))*(Utils.lg(m*Math.exp(1))));
    //System.out.println("Numbers of Samplers = "+c);
    sampleList = new ArrayList<>();
    for (int i=0; i<c; i++) {
      EntropySample tmp = new EntropySample();
      sampleList.add(tmp);
    }
  }

  public void setFrequencyEstimator(double epsilon, double delta) {
    this.k = (int) Math.ceil(7/epsilon);
    this.frequencyCounter = new HashMap<>();
  }

  public void newStreamUpdate(int update) {
    maintainFrequency(update);
    maintainSamples(update);
  }

  public void maintainSamples(int update) {
    //int t = random.nextInt((int) Math.pow(m,3));
    for (EntropySample sample : sampleList) {
      double t = random.nextDouble();
      if (update == sample.s0) {
        if (t < sample.t0) {
          sample.setGroup0(update, t, 1);
        } else {
          sample.increaseR0();
        }
      } else {
        if (update == sample.s1) {
          sample.increaseR1();
        }
        if (t < sample.t0) {
          sample.setGroup0AsGroup1();
          sample.setGroup0(update, t, 1);
        } else {
          if (t < sample.t1) {
            sample.setGroup1(update, t, 1);
          }
        }
      }
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
      for (EntropySample sample : sampleList) {
        int r;
        if (sample.s0==iMax) {
          r = sample.r1;
        }
        else {
          r = sample.r0;
        }
        Est += Utils.lambda(r,m)-Utils.lambda(r-1,m);
      }
      //System.out.println(Est);
      Est = (1-(double)mMax/m)*Est/c + ((double)mMax/m)*Utils.lg((double)m/mMax);
    }
    else {
      for (EntropySample sample : sampleList) {
        Est += Utils.lambda(sample.r0,m) - Utils.lambda(sample.r0-1,m);
      }
      Est = Est/c;
    }
    return Est;
  }
}
