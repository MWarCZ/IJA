package main.test;
import static org.junit.Assert.*;
import main.blocks.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BlockTest {
    private ArrayList<Integer> TestInput = new ArrayList<Integer>(Arrays.asList(1,2,3));
    private ArrayList<Integer> TestOutput = new ArrayList<Integer>(Arrays.asList(2,5,7));
    private HashMap<Integer,Double> InValue = new HashMap<Integer,Double>();

    private Block test01;
    private Block test02;
    private Block test03;

    public void TestSet(){
        test01 = new Block_add(TestInput, TestOutput);
        test02 = new Block_mul(TestInput, TestOutput);
        test03 = new Block_sub(new ArrayList<Integer>(Arrays.asList(1,2)), new ArrayList<Integer>(Arrays.asList(2,3)));

        ArrayList<Integer> Test01Input = test01.GetPortsIn();
        ArrayList<Integer> Test01Output = test01.GetPortsOut();

        assertEquals(Test01Input,TestInput);
        assertEquals(Test01Output,TestOutput);
        assertEquals(new ArrayList<Integer>(Arrays.asList(1,2)), test03.GetPortsIn());
        assertEquals(new ArrayList<Integer>(Arrays.asList(2,3)), test03.GetPortsOut());
    }

    public void TestResult(){
        double result;
        double result01 = 6.0;
        double resultSub = -1.0;

        InValue.put(1,1.0);
        InValue.put(2,2.0);
        InValue.put(3,3.0);

        result = test01.Operation(InValue);
        assertEquals(result01, result, 0);

        result = test02.Operation(InValue);
        assertEquals(result01, result, 0);

        InValue.remove(3);
        result = test03.Operation(InValue);
        assertEquals(resultSub, result, 0);
    }
}
