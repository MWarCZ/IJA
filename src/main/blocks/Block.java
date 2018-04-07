package main.blocks;
import main.manipulator.IOperation;

import java.util.ArrayList;
import java.util.Objects;

public abstract class Block implements IOperation{
    protected ArrayList<Integer> portsIn;
    protected ArrayList<Integer> portsOut;
    /**
     *Constructor
     */
    public Block(ArrayList<Integer> portsIn, ArrayList<Integer> portsOut){
        this.portsIn = portsIn;
        this.portsOut = portsOut;
    }
    public Block(){
        portsIn = new ArrayList<Integer>();
        portsOut = new ArrayList<Integer>();
    }
    @Override
    public ArrayList<Integer> GetPortsIn() {    return this.portsIn;    }
    @Override
    public ArrayList<Integer> GetPortsOut() {   return this.portsOut;   }
}

