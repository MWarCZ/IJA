package test;

import static org.junit.Assert.*;

import org.junit.*;

@Ignore
public class TestTest {

  //execute before class
  @BeforeClass
  public static void beforeClass() {
    System.out.println("in before class");
  }

  //execute after class
  @AfterClass
  public static void  afterClass() {
    System.out.println("in after class");
  }

  //execute before test
  @Before
  public void before() {
    System.out.println("in before");
  }

  //execute after test
  @After
  public void after() {
    System.out.println("in after");
  }

  //test case
  @Test
  public void test1() {
    System.out.println("in test1");
  }

  //execute before test
  @Before
  public void before2() {
    System.out.println("in before2");
  }
  //test case
  @Test(timeout = 1000)
  public void test2() {
    System.out.println("in test2");
    //for( ; ; ) { }
  }

  //test case
  @Test(expected = ArithmeticException.class)
  public void test3() {
    System.out.println("in test3");
    throw new ArithmeticException("Test3");
  }

  //test case ignore and will not execute
  @Ignore
  public void ignoreTest() {
    System.out.println("in ignore test");
  }
}
