package model;

import util.Constant;

/**
 * @author yiwenqiu
 * @Description 图实体类
 * @date 22:00 2017/11/14
 */
public class Graph {
    private Node[] nodes;
    /**
     * 相邻链路长度
     */
    private double[][] edges;
    /**
     * 最短路径长度
     */
    private double[][] shortestEdges;

    private int[][] shortestEdgesHop;

    public Node[] getNodes() {
        return nodes;
    }

    public void setNodes(Node[] nodes) {
        this.nodes = nodes;
    }

    public double[][] getEdges() {
        return edges;
    }

    public void setEdges(double[][] edges) {
        this.edges = edges;
    }

    public double[][] getShortestEdges() {
        return shortestEdges;
    }

    public void setShortestEdges(double[][] shortestEdges) {
        this.shortestEdges = shortestEdges;
    }

    public int[][] getShortestEdgesHop() {
        return shortestEdgesHop;
    }

    public void setShortestEdgesHop(int[][] shortestEdgesHop) {
        this.shortestEdgesHop = shortestEdgesHop;
    }

    /**
     * 初始化链路数组，节点到自身节点=0，不相连的节点为inf
     * @param size
     * @return
     */
    public double[][] initEdges(int size){
        this.edges = new double[size][size];
        for (int i=0;i<size;i++){
            for (int j=0;j<size;j++){
                if (i == j){
                    this.edges[i][j]=0;
                }else {
                    this.edges[i][j]= Constant.INF;
                }
            }
        }
        return this.edges;
    }
}
