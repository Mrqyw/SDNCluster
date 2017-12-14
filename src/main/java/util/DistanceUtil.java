package util;

import model.Node;

import java.util.Arrays;

/**
 * @author yiwenqiu
 * @Description 计算两个经纬度间的距离
 * @date 22:18 2017/11/14
 */
public class DistanceUtil {
    /**
     *   地球半径 单位：米
     */
    private static final int RADIUS_INT = 6378137;

    /**
     * 两个节点的距离
     * @param node1
     * @param node2
     * @return
     */
    public static double distance(Node node1, Node node2){
        double lat1 = node1.getLatitude() * Math.PI / 180.0;
        double lat2 = node2.getLatitude() * Math.PI / 180.0;
        double long1 = node1.getLongitude();
        double long2 = node2.getLongitude();
        double a = lat1 - lat2;
        double b = (long1 - long2) * Math.PI / 180.0;
        double d;
        double sa2, sb2;
        sa2 = Math.sin(a / 2.0);
        sb2 = Math.sin(b / 2.0);
        d = 2
                * RADIUS_INT
                * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1)
                * Math.cos(lat2) * sb2 * sb2));
        return d;
    }

    /**
     * 返回edge数组对应的最短路径数组
     * @param edges
     * @return
     */
    public static double[][] getShortestPath(double[][] edges){
        double[][] sp = new double[edges.length][edges.length];
        for (int i = 0;i<edges.length;i++){
            sp[i] = dijkstra(edges,edges.length,i);
        }
        return sp;
    }

    /**
     * 获取最短路径对应的跳数，用于考虑可靠性时使用
     * @param edges
     * @return
     */
    public static int[][] getShortestPathRelHop(double[][] edges){
        return null;
    }

    /**
     * 返回一个点 到其他所有点的所有距离
     * @param graph
     * @param n
     * @param u
     * @return
     */
    public static double[] dijkstra(double[][] graph,int n,int u){
        double dist[]=new double[n];
        boolean s[]=new boolean[n];
        Arrays.fill(s, false);
        Arrays.fill(dist, Constant.INF);
        int v;
        double min;
        for(int i=0;i<n;i++){
            dist[i]=graph[u][i];
        }
        s[u]=true;
        while(true){
            min=Constant.INF;
            v=-1;
            //找到最小的dist
            for(int i=0;i<n;i++){
                if(!s[i]){
                    if(dist[i]<min){min=dist[i];v=i;}
                }
            }
            if(v==-1){
                //找不到更短的路径了
                break;
            }
            //更新最短路径
            s[v]=true;
            for(int i=0;i<n;i++){
                if(!s[i]&&
                        graph[v][i]!=Constant.INF&&
                        dist[v]+graph[v][i]<dist[i]){
                    dist[i]=dist[v]+graph[v][i];
                }
            }
        }
        return dist;
    }



}
