package main.blocks;

import java.util.ArrayList;
import java.util.HashMap;

public class Block_sub extends Block {
    /**
     * Constructor
     */
    public Block_sub(ArrayList<Integer> portsIn, ArrayList<Integer> portsOut){  super(portsIn,portsOut);    }

    /**
     * Subtraction
     */
    @Override
    public Double Operation(HashMap<Integer,Double> data){
        double sum = 0;
        boolean firstIter = true;
        for(Integer i: portsIn){
            if(firstIter == true){
                sum = data.get(i);
                firstIter = false;
            }
            else{
                sum -= data.get(i);
            }
        }
        return sum;
    }
}
