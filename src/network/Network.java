package network;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import java.util.HashMap;

public class Network  {
    private HashMap<Integer, Mote> moteHashMap = new HashMap<>();
    private Graph graph;

    public Network(){
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        graph = new SingleGraph("Tutorial 1");
    }

    public Edge getEdge(String id){
        return graph.getEdge(id);
    }

    public void addNode(Mote mote, double x, double y){
        if (moteHashMap.keySet().contains(mote.getID()))
            return;
        moteHashMap.put(mote.getID(), mote);
        graph.addNode(mote.getID() + "");
        Node e1=graph.getNode(mote.getID() + "");
        e1.setAttribute("xyz", x, -y,0);
        e1.addAttribute("ui.style", "shape:circle;fill-color: yellow;size: 30px; text-alignment: center;");
        e1.addAttribute("ui.label", "" + mote.getID());
    }

    public void display(){
        Viewer viewer = graph.display();
        viewer.disableAutoLayout();
    }

    public void addLink(Mote src, Mote dst){
        if (src == null)
            return;
        if (dst == null)
            return;
        if (!src.getNeighbours().contains(dst))
            src.addNeighbour(dst);
        if (!dst.getNeighbours().contains(src))
            dst.addNeighbour(src);
        if (graph.getEdge(src.toString()+"-"+dst.toString()) == null && graph.getEdge(dst.toString()+"-"+src.toString()) == null)
            graph.addEdge(src.toString()+"-"+dst.toString(), src.toString(), dst.toString());
    }

    public Mote getMote(int id){
        return moteHashMap.get(id);
    }
}

