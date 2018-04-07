package main.blocks;

import java.util.ArrayList;
import java.util.HashMap;

public class Block_add extends Block {
    /**
     * Constructor
     */
    public Block_add(ArrayList<Integer> portsIn, ArrayList<Integer> portsOut){ super(portsIn,portsOut);    }

    /**
     * Add operation - Sum of all values in data
     */
    @Override
    public Double Operation(HashMap<Integer,Double> data){
        double sum = 0;
        for(Integer i: portsIn){
            sum += data.get(i);
        }
        return sum;
    }
}
