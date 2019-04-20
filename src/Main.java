import Ctrl.apps.spaningTree.SpanningTreeService;
import network.Mote;
import network.Network;
import org.contikios.cooja.sdnwise.AbstractCoojaMote;
import org.contikios.cooja.sdnwise.CoojaMote;
import org.contikios.cooja.sdnwise.CoojaSink;
import org.graphstream.graph.Edge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

public class Main {

    static HashMap<Integer, int[]> tree = new HashMap<>();
    public static void main(String[] args) {
        ArrayList<int[]>nodes = new ArrayList<>();
        double[][]positions = new double[][]{
                {0,0},
                {-66.38433730530808,-97.23120631771903},
                {64.9314359634095,8.273703876099507},
                {90.61933411611739,39.00741853695243},
                {11.340255670051143,-70.65952721232344},
                {-76.18012111625647,-5.803524476285844},
                {-20.736920576834024,2.1201417915090275},
                {-74.38051075065806,98.77829800577479},
                {-54.66382855274592,22.89014974328107},
                {-29.433598706082464,-18.728731877536163},
                {4.223184405957991,76.80171604062814},
                {-21.807157421554592,-82.24358149021964},
                {-75.66764339621825,-41.097078324490745},
                {-75.51305093308457,34.58534847887191},
                {-61.6310656580044,63.1502706675229},
                {-53.37120990400885,-15.996863452368856},
                {-55.12583163031621,-73.58511658286777},
                {-17.880308838433635,40.051474360556085},
                {52.11770341809515,32.72220649861406},
                {47.48624088335269,-64.16577930100821},
                {28.992045578132817,4.83105296313213},
        };

        nodes.add(new int[]{18, 21, 7, 10});
        nodes.add(new int[]{17, 12});
        nodes.add(new int[]{19, 4, 21});
        nodes.add(new int[]{3, 19});
        nodes.add(new int[]{20, 12});
        nodes.add(new int[]{16, 9, 10, 13, 14});
        nodes.add(new int[]{16, 1, 18, 21, 9});
        nodes.add(new int[]{15});
        nodes.add(new int[]{16, 18, 6, 7, 14, 15, 10});
        nodes.add(new int[]{16, 1, 6, 7, 9});
        nodes.add(new int[]{18});
        nodes.add(new int[]{17, 2, 5});
        nodes.add(new int[]{16, 17, 6});
        nodes.add(new int[]{6, 9, 15});
        nodes.add(new int[]{18, 8, 9, 14});
        nodes.add(new int[]{6, 7, 9, 10, 13});
        nodes.add(new int[]{2, 12, 13});
        nodes.add(new int[]{1, 7, 9, 11, 15});
        nodes.add(new int[]{3, 4, 21});
        nodes.add(new int[]{5});
        nodes.add(new int[]{1, 3, 19, 7});





        tree.put(1,new int[]{18,21,7,10});
        tree.put(3,new int[]{4});
        tree.put(5,new int[]{20});
        tree.put(10,new int[]{6,16});
        tree.put(12,new int[]{5});
        tree.put(13,new int[]{17});
        tree.put(15,new int[]{14,8});
        tree.put(16,new int[]{13});
        tree.put(17,new int[]{12, 2});
        tree.put(18,new int[]{9,11,15});
        tree.put(21,new int[]{3,19});


        Network network = new Network();
        network.display();
        Scanner scanner = new Scanner(System.in);

        for (int i = 1; i <= nodes.size() ; i++) {
            AbstractCoojaMote m;
            if (i == 1) {
                m = new CoojaSink(i);
            }
            else {
                m = new CoojaMote(i);
            }
            network.addNode(m, positions[i-1][0], positions[i-1][1]);
        }

        for (int i = 1; i <= nodes.size() ; i++) {
            int[] links = nodes.get(i-1);
            for (int dst : links)
                network.addLink(network.getMote(i),network.getMote(dst));
        }


        for (int i = 2; i <= nodes.size() ; i++) {
            AbstractCoojaMote m = (AbstractCoojaMote) network.getMote(i);
            m.init();
        }
        AbstractCoojaMote m = (AbstractCoojaMote) network.getMote(1);
        m.init();


        ArrayList<Integer> send = new ArrayList<>();
        send.add(1);
        for (int i = 0; i < send.size(); i++) {
            int index = send.get(i)-1;
            for (int node : nodes.get(index)) {
                if (!send.contains(node))
                    send.add(node);
            }

        }
        System.out.println(send);

        send.remove(0);

        for (int i : send) {
            CoojaMote mote = (CoojaMote) network.getMote(i);
            mote.sendReport();
        }
        System.out.println("fuuuuuuuuuuuuuuuuuuuuuuuuuuuuuck");
        SpanningTreeService.printTun();
        HashMap<Integer, Set<Integer>> tree = SpanningTreeService.getSpanningTree();

        drawTee(network, tree);
        scanner.nextInt();
        CoojaMote newMote = new CoojaMote(22);
        int[] neighbours = new int[]{5, 20};
        network.addNode(newMote, 47.48624088335269,-84.16577930100821);
        for (int dst : neighbours)
            network.addLink(newMote,network.getMote(dst));
        CoojaMote mote = (CoojaMote) network.getMote(5);
        newMote.init();
        mote.sendBeacon();
        newMote.sendReport();
        SpanningTreeService.printTun();
        drawTee(network, tree);
    }

    private static void drawTee(Network network, HashMap<Integer, Set<Integer>> tree) {
        for (int src: tree.keySet()){
            Set<Integer> dests = tree.get(src);
            for (int dst: dests){
                Edge edge = network.getEdge(src+"-"+dst);
                if (edge == null)
                    edge = network.getEdge(dst+"-"+src);
                edge.addAttribute("ui.style", "fill-color: red;size: 3px;");
            }
        }
    }

    private static int numberOfRules(int node){
        int num = 0;
        int[] dests = tree.get(node);
        if (dests == null)
            return 1;
        for (int dst: dests)
            num += numberOfRules(dst);
        return num+1;
    }
}
