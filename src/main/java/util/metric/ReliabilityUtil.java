package util.metric;

import model.Graph;
import model.Node;

import java.util.List;
import java.util.Map;

/**
 * @author yiwenqiu
 * @Description 系统的可靠性
 * @date 10:49 2018/1/8
 */
public class ReliabilityUtil {
    /**
     * 考虑单链路失效下，断连交换机的数量
     * 然后由交换机数量来衡量部署方案的可靠性，断连交换机越少越好
     * 从节点树可以看出跳数与可靠性密切相关，因此我们用跳数来衡量系统的可靠性
     * @param graph
     * @param map
     * @return
     */
    public static int disConnectSwitchesSum(Graph graph, Map<Integer,List<Node>> map){
        int sum = 0;
        int[][] hopMatrix = graph.getShortestEdgesHop();
        for (Integer i:map.keySet()){
            for (Node node:map.get(i)){
                sum += hopMatrix[i][node.getId()];
            }
        }
        return sum;
    }
}
