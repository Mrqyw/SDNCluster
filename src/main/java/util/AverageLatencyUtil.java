package util;

import model.Graph;
import model.Node;

import java.util.List;
import java.util.Map;

/**
 * @author yiwenqiu
 * @Description 平均时延，设每条链路的传输速度都一致
 * @date 15:29 2017/11/29
 */
public class AverageLatencyUtil {
    public static double averageDistance(Graph graph, Map<Integer,List<Node>> clusterResult){
        double sum = 0;
        for (Integer controller:clusterResult.keySet()){
            sum+=distance(controller,clusterResult.get(controller),graph.getShortestEdges());
        }
        return sum/graph.getNodes().length;
    }

    private static double distance(int controller, List<Node> nodes,double[][] shortestPathLength){
        double sum = 0;
        for (Node node:nodes){
            sum+=shortestPathLength[controller][node.getId()];
        }
        return sum;
    }
}
