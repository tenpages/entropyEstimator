import java.util.Random;

public class Utils {
  static int MAXINT = 100000000;
  static Random random = new Random(10);

  public static double lg(double a) {
    return Math.log(a)/Math.log(2);
  }

  public static double lambda(double x, int m) {
    if (x==0) {
      return 0;
    }
    return x * Utils.lg((double)m/x);
  }

  public static void setRandom(int seed) {
    random = new Random(seed);
  }

  public static int rand(double lambda) {
    double u = random.nextDouble();

    int x = 0;
    double cdf = 0;
    while (u>=cdf) {
      x ++;
      cdf = 1 - Math.exp(-1.0 * lambda * x);
    }
    return x;
  }

  public static int Geom(double p) {
    double u = random.nextDouble();
    if (u==0.0) {
      return 1;
    }
    return (int) Math.ceil(Math.log(u)/Math.log(1-p));
  }
  /*
  public static void main(String[] args) {
    System.out.println(0.9940*lg(1/0.9940)+" "+0.006*lg(1/0.006));
    System.out.println(0.9940*lg(1/0.9940)+0.006*lg(1/0.006));
    System.out.println(lg((double)10000/9940));

    System.out.println(0.0436651);
  }
  */
}
