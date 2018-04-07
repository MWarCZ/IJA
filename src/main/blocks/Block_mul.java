package main.blocks;

import java.util.ArrayList;
import java.util.HashMap;

public class Block_mul extends Block{
    /**
     * Constructor
     */
    public Block_mul(ArrayList<Integer> portsIn, ArrayList<Integer> portsOut){  super(portsIn,portsOut);    }

    /**
     * Multiplication
     */
    @Override
    public Double Operation(HashMap<Integer,Double> data){
        double sum = 1.0;
        for(Integer i: portsIn){
            sum *= data.get(i);
        }
        return sum;
    }
}
