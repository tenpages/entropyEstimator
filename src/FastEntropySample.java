public class FastEntropySample {
  public int s0, s1;
  public double t0, t1;
  public int r0, r1;
  public int u0, u1;

  public FastEntropySample() {
    s0 = 0;
    s1 = 0;
    t0 = 1;
    t1 = 1;
    r0 = 0;
    r1 = 0;
    u0 = 0;
    u1 = 0;
  }

  public void setGroup0(int s, double t, int r) {
    s0 = s; t0 = t; r0 = r;
  }

  public void setGroup1(int s, double t, int r) {
    s1 = s; t1 = t; r1 = r;
  }

  public void increaseR0() {
    r0 += 1;
  }

  public void increaseR1() {
    r1 += 1;
  }

  public void setGroup0AsGroup1() {
    s1 = s0;
    t1 = t0;
    r1 = r0;
  }
}
