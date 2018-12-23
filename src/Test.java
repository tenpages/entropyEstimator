import java.util.HashMap;

public class Test {
  int[] inputStream;
  int n; //domain size
  int m; //stream size

  public Test(int n, int m) {
    this.n = n;
    this.m = m;
  }

  public void generateDataset(double lambda) {
    inputStream = new int[m];
    for (int i=0; i<m; i++) {
      int t = Utils.rand(lambda);
      while (t>n) {
        t = Utils.rand(lambda);
      }
      inputStream[i] = t;
    }
  }

  public double accurateEntropy() {
    int[] frequency = new int[n+1];
    for (int i=0; i<m; i++) {
      frequency[inputStream[i]] += 1;
    }
    double ans = 0;
    //System.out.println("m="+m);
    for (int i=0; i<n; i++) {
      if (frequency[i]!=0) {
        ans += frequency[i] * Utils.lg((double)m / frequency[i]);
        //System.out.println(i+" "+frequency[i]+" "+ frequency[i] * Utils.lg((double)m / frequency[i]));
      }
    }
    return ans/m;
  }

  /**
   * Change this part to run tests on different settings
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    test(0.02,100000,10000, 0.1, 0.4);
  }

  public static void test(double lambda, int size, int n, double epsilon, double delta) throws Exception {
    //int size = 100000;
    //double lambda = 0.1;
    //int n = 10000;
    System.out.println("lambda = "+ lambda + ", size = " + size + ", n = " + n + ", eps = " + epsilon + ", delta = " + delta);
    //double epsilon = 0.1, delta = 0.4;
    Test test = new Test(n,size);

    test.generateDataset(lambda);
    System.out.println("dataset generated");
    /*
    for (int i=0; i<size; i++) {
      System.out.print(test.inputStream[i]+" ");
    }
    System.out.println();
    */
    double sum =0, sumSqr = 0;
    double sum2 =0, sumSqr2 = 0;
    double acc = test.accurateEntropy();
    double upperBound = acc*(1+epsilon);
    double lowerBound = acc*(1-epsilon);
    int counter = 0, counter2 = 0;
    int expCount = 20;
    long startTime = System.currentTimeMillis();
    for (int testTime = 0; testTime<expCount; testTime++) {
      Stream stream = new Stream(n, epsilon, delta, size);
      stream.setRandom(testTime);
      for (int i = 0; i < size; i++) {
        stream.newStreamUpdate(test.inputStream[i]);
      }

      if (testTime==0) {
        HashMap.Entry<Integer, Integer> fq = stream.mostFrequentItem();
        System.out.println("most frequent:" + fq.getKey() + " " + fq.getValue());
      }

      double est = stream.entropyEstimator();
      if (est>upperBound || est<lowerBound) {
        counter += 1;
      }
      else {
        sum += est;
        sumSqr += est * est;
      }
      System.out.print(".");
      //System.out.println(testTime+"-th estimation(slow): " + est);
    }
    long endTime = System.currentTimeMillis();
    System.out.println();
    long startTime2 = System.currentTimeMillis();
    for (int testTime = 0; testTime<expCount; testTime++) {
      FastStream fstream = new FastStream(n, epsilon, delta, size);
      fstream.setRandom(testTime);
      for (int i = 0; i < size; i++) {
        fstream.newStreamUpdate(test.inputStream[i]);
      }
      double est2 = fstream.entropyEstimator();
      if (est2>upperBound || est2<lowerBound) {
        counter2 += 1;
      }
      else {
        sum2 += est2;
        sumSqr2 += est2 * est2;
      }
      System.out.print(".");
      //System.out.println(testTime+"-th estimation(fast): " + est2);
    }
    long endTime2 = System.currentTimeMillis();
    System.out.println();

    System.out.println("accurate: "+acc);
    System.out.println("average estimation: " + sum/(expCount-counter) + ", with stderr = " + Math.sqrt((sumSqr - sum*sum/(expCount-counter))/(expCount-counter-1))+" and failure rate: " + (double)counter/expCount);
    System.out.println("running time: " + (endTime-startTime)/expCount + "ms");
    System.out.println("average estimation: " + sum2/(expCount-counter2) + ", with stderr = " + Math.sqrt((sumSqr2 - sum2*sum2/(expCount-counter2))/(expCount-counter2-1))+" and failure rate: " + (double)counter2/expCount);
    System.out.println("running time: " + (endTime2-startTime2)/expCount + "ms");
  }
}
