package network;

import java.util.ArrayList;

public class Mote {
    private ArrayList<Mote> neighbours = new ArrayList<>();
    protected int id;

    public Mote(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public int getID(){
        return id;
    }

    public void addNeighbour(Mote mote){
        neighbours.add(mote);
    }

    public ArrayList<Mote> getNeighbours(){
        return neighbours;
    }

    @Override
    public String toString() {
        return getID() + "";
    }
}
