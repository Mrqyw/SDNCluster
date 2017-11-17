package util;

import model.ClusterResult;
import model.Graph;
import model.Node;
import java.util.*;

/**
 * @author yiwenqiu
 * @Description 聚类工具类
 * @date 18:59 2017/11/15
 */
public class ClusterUtil {
    /**
     * K-means
     * 1.随机选取节点，作为节点中心
     * 2.分配节点到不同的簇类
     * 3.更新簇类中心
     * 4.重新分配更新中心，直到簇类中心不变
     *
     * @return 簇类结果
     */
    public static ClusterResult KMeansCluster(List<Integer> centers,Graph graph, int k) {
        List<Integer> oldCenters;
        if(centers==null){
            oldCenters = getRandomList(k, graph.getNodes().length);
        }else{
            oldCenters = centers;
        }

        Map<Integer, List<Node>> map = distributeNodes(oldCenters, graph.getNodes(), graph.getShortestEdges());
        List<Integer> newCenters = null;
        do {
            oldCenters = newCenters;
            newCenters = reCalCenter(map, graph.getShortestEdges());
            map = distributeNodes(newCenters, graph.getNodes(), graph.getShortestEdges());
        } while (centersEqual(oldCenters, newCenters));
        double maxDistance = getMaxDistance(map, graph.getShortestEdges());
        return new ClusterResult(map, maxDistance);
    }

    /**
     * Optimized K-means
     * 1.找出整体聚类中心
     * 2.找到离现有的聚类中心最远的点作为下一个簇类中心
     * 3.重新分配节点，并重新计算新的中心
     * 4.重复2，3步，直到找到K个簇类中心
     *
     * @return 簇类结果
     */
    public static ClusterResult optimizedKMeansCluster(Graph graph, int k) {
        Map<Integer,List<Node>> map = new HashMap<Integer, List<Node>>();
        List<Node> nodes = new ArrayList<Node>();
        Collections.addAll(nodes,graph.getNodes());
        map.put(0,nodes);
        List<Integer> intList= reCalCenter(map,graph.getShortestEdges());
        System.out.println(intList.get(0));
        List<Integer> nodeIds = new ArrayList<Integer>();
        nodeIds.add(intList.get(0));
        while(nodeIds.size()<k){
            nodeIds = getNextCenter(nodeIds,graph.getNodes(),graph.getShortestEdges());
            ClusterResult clusterResult = KMeansCluster(nodeIds,graph,nodeIds.size());
            map = clusterResult.getNodeMap();
        }
        double maxDistance = getMaxDistance(map, graph.getShortestEdges());
        return new ClusterResult(map, maxDistance);
    }

    /**
     * 1.先随机找到kStar个初始点，并完成分配与更新簇类中心
     * 2.找出最临近的两个簇类，并完成合并。
     * 3.重新分配与更新簇类中心。
     * 4.重复第二三步直到簇个数只有k个为止。
     * @param graph
     * @param k
     * @param kStar
     * @return
     */
    public static ClusterResult KStarMeansCluster(Graph graph, int k,int kStar) {
        return null;
    }

    /**
     * 寻找下一个中心
     * @param presentCenter
     * @param nodes
     * @param shortestPathLength
     * @return
     */
    public static List<Integer> getNextCenter(List<Integer> presentCenter, Node[] nodes, double[][] shortestPathLength) {
        double maxDistance = 0;
        int nextCenter = 0;
        for (Node node : nodes) {
            if (isExist(presentCenter,node.getId())){
                continue;
            }
            int distance = 0;
            for (Integer i : presentCenter) {
                if (node.getId() == i) {
                    continue;
                }
                distance += shortestPathLength[i][node.getId()];
            }
            if (distance>maxDistance){
                maxDistance = distance;
                nextCenter = node.getId();
            }
        }
        presentCenter.add(nextCenter);
        return presentCenter;
    }

    /**
     * 判断两次聚类中心点是否相等
     *
     * @param oldCenters
     * @param newCenters
     * @return
     */
    public static boolean centersEqual(List<Integer> oldCenters, List<Integer> newCenters) {
        if (oldCenters == null || newCenters == null) {
            return false;
        }
        if (oldCenters.size() != newCenters.size()) {
            return false;
        }
        for (Integer i : oldCenters) {
            if (!isExist(newCenters, i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 重新计算新的簇类中心点
     *
     * @param map
     * @param shortestPathLength
     * @return
     */
    public static List<Integer> reCalCenter(Map<Integer, List<Node>> map, double[][] shortestPathLength) {
        List<Integer> newCenters = new ArrayList<Integer>();
        for (Integer oldCenter : map.keySet()) {
            List<Node> nodes = map.get(oldCenter);
            int center = oldCenter;
            double distance = Integer.MAX_VALUE;
            for (int i = 0; i < nodes.size(); i++) {
                double subMaxDistance = 0;
                for (int j = 0; j < nodes.size(); j++) {
                    if (i == j) {
                        continue;
                    }
                    double tmpDistance = shortestPathLength[nodes.get(i).getId()][nodes.get(j).getId()];
                    if (tmpDistance > subMaxDistance) {
                        subMaxDistance = tmpDistance;
                    }
                }
                if (subMaxDistance < distance) {
                    distance = subMaxDistance;
                    center = nodes.get(i).getId();
                }
            }
            newCenters.add(center);
        }
        return newCenters;
    }

    /**
     * 分配点到不同的簇类
     *
     * @param centers
     * @param nodes
     * @param shortestPathLength
     * @return
     */
    public static Map<Integer, List<Node>> distributeNodes(List<Integer> centers, Node[] nodes, double[][] shortestPathLength) {
        Map<Integer, List<Node>> map = initialMap(centers);
        for (Node node : nodes) {
            //如果初始点含该id
            if (isExist(centers, node.getId())) {
                continue;
            } else {
                int tmpCenter = centers.get(0);
                double tmpDistance = shortestPathLength[tmpCenter][node.getId()];

                for (int i = 1; i < centers.size(); i++) {
                    if (shortestPathLength[centers.get(i)][node.getId()] < tmpDistance) {
                        tmpDistance= shortestPathLength[centers.get(i)][node.getId()];
                        tmpCenter = centers.get(i);
                    }
                }
                map.get(tmpCenter).add(node);
            }
        }
        return map;
    }

    public static double getMaxDistance(Map<Integer, List<Node>> map, double[][] shortestPathLength) {
        double maxDistance = 0;
        for (Integer center : map.keySet()) {
            List<Node> nodes = map.get(center);
            for (int i = 0; i < nodes.size(); i++) {
                for (int j = 0; j < nodes.size(); j++) {
                    if (i == j) {
                        continue;
                    }
                    double tmpDistance = shortestPathLength[nodes.get(i).getId()][nodes.get(j).getId()];
                    if (tmpDistance > maxDistance) {
                        maxDistance = tmpDistance;
                    }
                }
            }
        }
        return maxDistance;
    }

    /**
     * 获取随机数list
     *
     * @param k        随机数数量
     * @param nodeSize 随机数范围的最大值
     * @return
     */
    public static List<Integer> getRandomList(int k, int nodeSize) {
        if (k > nodeSize) {
            System.err.println("error K:the number of K is larger than nodeSize");
        }
        List<Integer> integerList = new ArrayList<Integer>();
        Random random = new Random();
        while (integerList.size() < k) {
            int randomNumber = random.nextInt(nodeSize);
            if (!isExist(integerList, randomNumber)) {
                integerList.add(randomNumber);
            }
        }
        return integerList;
    }

    /**
     * 判断list中是否含这个数字
     *
     * @param list
     * @param number
     * @return
     */
    public static boolean isExist(List<Integer> list, int number) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == number) {
                return true;
            }
        }
        return false;
    }

    public static Map<Integer, List<Node>> initialMap(List<Integer> integers) {
        Map<Integer, List<Node>> map = new HashMap<Integer, List<Node>>();
        for (Integer i : integers) {
            map.put(i, new ArrayList<Node>());
        }
        return map;
    }
}
