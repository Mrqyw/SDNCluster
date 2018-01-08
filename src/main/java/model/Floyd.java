package model;

import util.Constant;

/**
 * @author yiwenqiu
 * @Description 用于求最短距离
 * @date 10:19 2018/1/8
 */
public class Floyd {
    /**
     * 路径矩阵
     */
    private double[][] pathMatrix;
    /**
     * 前驱表
     */
    private int[][] preTable;
    /**
     * 跳矩阵
     */
    private int[][] hopMatrix;

    public Floyd(double[][] edges) {
        //路径矩阵（D），表示顶点到顶点的最短路径权值之和的矩阵，初始时，就是图的邻接矩阵。
        pathMatrix = new double[edges.length][edges.length];
        //前驱表（P），P[m][n] 的值为 m到n的最短路径的前驱顶点，如果是直连，值为n。也就是初始值
        preTable = new int[edges.length][edges.length];
        hopMatrix = new int[edges.length][edges.length];
        //初始化D,P
        for (int i = 0; i < edges.length; i++) {
            for (int j = 0; j < edges.length; j++) {
                pathMatrix[i][j] = edges[i][j];
                preTable[i][j] = j;
            }
        }

        //循环 中间经过顶点
        for (int k = 0; k < edges.length; k++) {
            //循环所有路径
            for (int m = 0; m < edges.length; m++) {

                for (int n = 0; n < edges.length; n++) {

                    double mn = pathMatrix[m][n];
                    double mk = pathMatrix[m][k];
                    double kn = pathMatrix[k][n];
                    double addedPath = (mk == Constant.INF || kn == Constant.INF) ? Constant.INF : mk + kn;

                    if (mn > addedPath) {
                        //如果经过k顶点路径比原两点路径更短，将两点间权值设为更小的一个
                        pathMatrix[m][n] = addedPath;
                        //前驱设置为经过下标为k的顶点
                        preTable[m][n] = preTable[m][k];
                    }
                }
            }
        }

        for (int m = 0; m < edges.length; m++) {
            for (int n = m + 1; n < edges.length; n++) {
                int hop = 1;
                int k = preTable[m][n];
                while (k != n) {
                    k = preTable[k][n];
                    hop++;
                }
                hopMatrix[m][n] = hopMatrix[n][m] = hop;
            }
        }
    }

    public double[][] getPathMatrix() {
        return pathMatrix;
    }

    public void setPathMatrix(double[][] pathMatrix) {
        this.pathMatrix = pathMatrix;
    }

    public int[][] getPreTable() {
        return preTable;
    }

    public void setPreTable(int[][] preTable) {
        this.preTable = preTable;
    }

    public int[][] getHopMatrix() {
        return hopMatrix;
    }

    public void setHopMatrix(int[][] hopMatrix) {
        this.hopMatrix = hopMatrix;
    }
}
