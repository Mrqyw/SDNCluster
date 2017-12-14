package util;

import model.Graph;
import model.Node;

import java.util.List;
import java.util.Map;

/**
 * @author yiwenqiu
 * @Description 本研究中负载仅根据所控制的交换机的数量来决定，即控制的交换机数量越多，其负载则为越大。
 * @date 15:24 2017/11/29
 */
public class LoadBalanceUtil {

    /**
     * 每个簇类所拥有的交换机数量的标准差
     * @return
     */
    public static double SDNS(Graph graph,Map<Integer, List<Node>> map){
        int nodeSize = graph.getNodes().length;
        int clusterSize = map.keySet().size();
        double averageSize = nodeSize/clusterSize;
        double sum = 0;
        for (Integer center:map.keySet()){
            sum+=Math.pow(map.get(center).size()-averageSize,2);
        }
        return Math.sqrt(sum/clusterSize);
    }
}
