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
     * K-Means聚类
     * @param graph gml
     * @param k 聚类数量
     * @return 聚类结果
     */
    public static ClusterResult KMeansCluster(Graph graph, int k){
        return KMeansBaseCluster(null,graph,k);
    }

    /**
     * Optimized K-means
     * 1.找出整体聚类中心,通过KMeans来决定
     * 2.找到离现有的聚类中心最远的点作为下一个簇类中心
     * 3.重新分配节点，并重新计算新的中心
     * 4.重复2，3步，直到找到K个簇类中心
     *
     * @return 簇类结果
     */
    public static ClusterResult optimizedKMeansCluster(Graph graph, int k) {
        ClusterResult clusterResult = KMeansBaseCluster(null,graph,1);
        Map<Integer,List<Node>> map = clusterResult.getNodeMap();
        List<Integer> nodeIds= new ArrayList<Integer>(map.keySet());
        printList(nodeIds);
        while(nodeIds.size()<k){
//          nodeIds = getNextCenter(nodeIds,graph.getNodes(),graph.getShortestEdges());
            nodeIds = getNextCenter(map,graph.getShortestEdges());
            clusterResult = KMeansBaseCluster(nodeIds,graph,nodeIds.size());
            map = clusterResult.getNodeMap();
            nodeIds = new ArrayList<Integer>(map.keySet());

        }
        double maxDistance = getMaxDistance(map, graph.getShortestEdges());
        return new ClusterResult(map, maxDistance);
    }

    /**
     * K-means基础类
     * 1.随机选取节点，作为节点中心
     * 2.分配节点到不同的簇类
     * 3.更新簇类中心
     * 4.重新分配更新中心，直到簇类中心不变
     *
     * @return 簇类结果
     */
    private static ClusterResult KMeansBaseCluster(List<Integer> inputCenter, Graph graph, int k) {
        List<Integer> oldCenters;
        if(inputCenter==null){
            oldCenters = getRandomList(k, graph.getNodes().length);
            printList(oldCenters);
        }else{
            oldCenters = inputCenter;
        }
        Map<Integer, List<Node>> map = distributeNodes(oldCenters, graph.getNodes(), graph.getShortestEdges());
        List<Integer> newCenters = null;
        do {
            oldCenters = newCenters;
            newCenters = reCalCenter(map, graph.getShortestEdges());
            if (centersEqual(oldCenters, newCenters)){
                break;
            }
            map = distributeNodes(newCenters, graph.getNodes(), graph.getShortestEdges());
        } while (true);
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
     * @param kStar 当KStar值与图中节点数量一样，则退化成层次簇类方法。
     * @return
     */
    public static ClusterResult KStarMeansCluster(Graph graph, int k, int kStar) {
        ClusterResult clusterResult = KMeansBaseCluster(null,graph,kStar);
        Map<Integer, List<Node>> map = clusterResult.getNodeMap();
        while(map.size()>k){
            map = mergeClusters(map,graph.getShortestEdges());
            clusterResult = KMeansBaseCluster(new ArrayList<Integer>(map.keySet()),graph,map.size());
            map = clusterResult.getNodeMap();
        }
        double maxDistance = getMaxDistance(map, graph.getShortestEdges());
        return new ClusterResult(map, maxDistance);
    }

    /**
     * 合并两个最近簇
     * @param map
     * @param shortestPathLength
     * @return
     */
    private static Map<Integer,List<Node>> mergeClusters(Map<Integer,List<Node>> map,double[][] shortestPathLength){
        int[] closetCenters = new int[2];
        Set<Integer> centerSets = map.keySet();
        Integer[] centers = centerSets.toArray(new Integer[centerSets.size()]);
        //找出簇类中心最近的两个簇
        double minDistance = Integer.MAX_VALUE;
        for (int i=0;i<centers.length;i++){
            for (int j=0;j<centers.length;j++){
                if (i==j){
                    continue;
                }
                if (shortestPathLength[centers[i]][centers[j]]<minDistance){
                    minDistance = shortestPathLength[centers[i]][centers[j]];
                    closetCenters[0] = centers[i];
                    closetCenters[1] = centers[j];
                }
            }
        }
        //根据簇类点最相近来合并
/*        double minDistance = Integer.MAX_VALUE;
        for (int i=0;i<centers.length;i++){
            List<Node> nodes = map.get(centers[i]);
            for (int j = 0;j<centers.length;j++){
                if (i==j){
                    continue;
                }
                List<Node> otherNodes = map.get(centers[j]);
                for (Node node:nodes){
                    for(Node otherNode:otherNodes){
                        if (shortestPathLength[node.getId()][otherNode.getId()]<minDistance){
                            minDistance = shortestPathLength[node.getId()][otherNode.getId()];
                            closetCenters[0] = centers[i];
                            closetCenters[1] = centers[j];
                        }
                    }
                }
            }
        }*/

//        System.out.println("merge centers:"+closetCenters[0]+","+closetCenters[1]);
        map.get(closetCenters[0]).addAll(map.remove(closetCenters[1]));
        int newCenter = closetCenters[0];
        List<Node> mergeNodes = map.get(closetCenters[0]);
        map.remove(closetCenters[0]);
        newCenter = reCalCenter(newCenter,mergeNodes,shortestPathLength);
        map.put(newCenter,mergeNodes);
        return map;
    }

    /**
     * 寻找下一个中心,根据每个单独簇类中最远的节点。
     * @param presentMap 当前的Map
     * @param shortestPathLength 最短路径数组
     * @return
     */
    private static List<Integer> getNextCenter(Map<Integer,List<Node>> presentMap, double[][] shortestPathLength) {
        double maxDistance = 0;
        int nextCenter = 0;
        Set<Integer> centers = presentMap.keySet();
        List<Integer> centerList = new ArrayList<Integer>(centers);
        for (Integer center:centers){
            double tempDistance = 0;
            List<Node> nodes = presentMap.get(center);
            if (nodes.size()==0){
                continue;
            }
            int tempCenter = nodes.get(0).getId();
            for (Node node:nodes){

                if (tempDistance<shortestPathLength[node.getId()][center]){
                    tempDistance = shortestPathLength[node.getId()][center];
                    tempCenter = node.getId();
                }
            }
            if (maxDistance<tempDistance){
                maxDistance = tempDistance;
                nextCenter = tempCenter;
            }
        }
        centerList.add(nextCenter);
        return centerList;
    }

    /**
     * 寻找下一个中心,根据与现中心点的距离和求得的结果
     * @param presentCenter 当前的簇类中心集
     * @param nodes 整体节点集
     * @param shortestPathLength 最短路径数组
     * @return
     */
    private static List<Integer> getNextCenter(List<Integer> presentCenter, Node[] nodes, double[][] shortestPathLength) {
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
    private static boolean centersEqual(List<Integer> oldCenters, List<Integer> newCenters) {
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

    private static Integer reCalCenter(int oldCenter, List<Node> nodes, double[][] shortestPathLength){
        int center = oldCenter;
        double distance = Integer.MAX_VALUE;
        for (int i = 0; i < nodes.size(); i++) {
/*                double subMaxDistance = 0;
                //根据最大的距离求中心
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
                }*/
            //根据距离总和求中心，既求出来的中心为簇类离其他点总和最近的。
            double sumDistance = 0;
            for (int j=0;j<nodes.size();j++){
                if (i==j){
                    continue;
                }
                sumDistance+= shortestPathLength[nodes.get(i).getId()][nodes.get(j).getId()];
            }
            if (sumDistance < distance) {
                distance = sumDistance;
                center = nodes.get(i).getId();
            }
        }
        return center;
    }

    /**
     * 重新计算新的簇类中心点
0     * @param map
     * @param shortestPathLength
     * @return
     */
    private static List<Integer> reCalCenter(Map<Integer, List<Node>> map, double[][] shortestPathLength) {
        List<Integer> newCenters = new ArrayList<Integer>();
        for (Integer oldCenter : map.keySet()) {
            List<Node> nodes = map.get(oldCenter);
            newCenters.add(reCalCenter(oldCenter,nodes,shortestPathLength));
        }
        return newCenters;
    }

    /**
     * 分配点到不同的簇类
     * @param centers 聚类中心集
     * @param nodes 图中的点
     * @param shortestPathLength 最短路径
     * @return 分配结果
     */
    private static Map<Integer, List<Node>> distributeNodes(List<Integer> centers, Node[] nodes, double[][] shortestPathLength) {
        Map<Integer, List<Node>> map = initialMap(centers,nodes);
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

    private static double getMaxDistance(Map<Integer, List<Node>> map, double[][] shortestPathLength) {
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
     * @param k        随机数数量
     * @param nodeSize 随机数范围的最大值
     * @return 随机数List
     */
    private static List<Integer> getRandomList(int k, int nodeSize) {
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
     * @param list 集合
     * @param number 数字值
     * @return 集合中是否含有这个数字
     */
    private static boolean isExist(List<Integer> list, int number) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == number) {
                return true;
            }
        }
        return false;
    }

    /**
     * 初始化Map
     * @param integers
     * @param nodeArray
     * @return 经初始化后的Map
     */
    private static Map<Integer, List<Node>> initialMap(List<Integer> integers,Node[] nodeArray) {
        Map<Integer, List<Node>> map = new HashMap<Integer, List<Node>>();
        for (Integer i : integers) {
            List<Node> nodes = new ArrayList<Node>();
            nodes.add(nodeArray[i]);
            map.put(i, nodes);
        }
        return map;
    }

    /**
     * 打印簇类中心ID
     * @param integers
     */
    private static void printList(List<Integer> integers){
        if (Constant.DEBUG){
            for (Integer i:integers){
                System.out.print(i+",");
            }
            System.out.println();
        }
    }

    /**
     * 打印Map:含簇类中心与对应的簇点
     * @param map
     */
    private static void printMap(Map<Integer,List<Node>> map){
        if (Constant.DEBUG){
            for (Integer i:map.keySet()){
                System.out.print("cluster:"+i+":");
                for (Node node:map.get(i)){
                    System.out.print(node.getId()+"-"+node.getCity()+",");
                }
                System.out.println();
            }
        }
    }
}
