public class Pair {
  public int key;
  public int value;

  public Pair(int key, int value) {
    setKey(key);
    setValue(value);
  }

  public int getKey() {
    return key;
  }

  public int getValue() {
    return value;
  }

  public void setKey(int key) {
    this.key = key;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public String toString() {
    return "{" + key + ":" + value + "}";
  }
}
