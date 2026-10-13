package test;

import org.junit.jupiter.api.Test;

public class P1CompleteTest extends CompleteTest {

  public P1CompleteTest() {
    super("test/resources");
  }

  @Test
  public void test0() {
    testN(0);
  }

  @Test
  public void test1() {
    testN(1);
  }

  @Test
  public void test2() {
    testN(2);
  }

  @Test
  public void test3() {
    testN(3);
  }

  @Test
  public void test4() {
    testN(4);
  }
}
