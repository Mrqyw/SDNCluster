package model;

import java.util.List;
import java.util.Map;

/**
 * @author yiwenqiu
 * @Description 聚类结果
 * @date 21:09 2017/11/15
 */
public class ClusterResult {

    public ClusterResult() {
    }

    public ClusterResult(Map<Integer, List<Node>> nodeMap, double maxDistance) {
        this.nodeMap = nodeMap;
        this.maxDistance = maxDistance;
    }

    /**
     * 每个聚类的节点集
     * key:聚类中心的id
     * value：对应的节点
     */
    private Map<Integer,List<Node>> nodeMap;
    /**
     * 距聚类中心的最大距离
     */
    private double maxDistance;

    public Map<Integer, List<Node>> getNodeMap() {
        return nodeMap;
    }

    public void setNodeMap(Map<Integer, List<Node>> nodeMap) {
        this.nodeMap = nodeMap;
    }

    public double getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
    }
}
