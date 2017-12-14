
import model.ClusterResult;
import model.Graph;
import model.Node;
import util.AverageLatencyUtil;
import util.ClusterUtil;
import util.GmlUtil;
import util.LoadBalanceUtil;

import java.io.IOException;

/**
 * @author yiwenqiu
 * @Description 程序入口
 * @date 22:18 2017/11/14
 */
public class Main {
    public static void main(String[] args) throws IOException {

        //--------------------------将gml文件转换成Graph数据结构--------------------------
        String dataSetBasePath = System.getProperty("user.dir");
        String gmlPath = "\\gml\\OS3E.gml";
        Graph graph = GmlUtil.converseGmlFile(dataSetBasePath+gmlPath);
        //-----------------------------------------------------------------------------

        ClusterResult clusterResult;
/*        //--------------------------K-means-------------------------------------------
        double maxDistance = 0;
        double minDistance = Integer.MAX_VALUE;
        double sum = 0;
        System.out.println();
        System.out.println("----------------------------K-means--------------------------------------");
        for (int i=1; i<10000; i++){
            clusterResult = ClusterUtil.KMeansCluster(graph,5);
            sum +=clusterResult.getMaxDistance();
            if (clusterResult.getMaxDistance()>maxDistance){
                maxDistance = clusterResult.getMaxDistance();
            }
            if (clusterResult.getMaxDistance()<minDistance){
                minDistance = clusterResult.getMaxDistance();
            }
        }
        System.out.println("maxDistance in 100 times:"+maxDistance);
        System.out.println("minDistance in 100 times:"+minDistance);
        System.out.println("average maxDistance in 100 times:"+sum/10000);
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println();
        //--------------------------------------------------------------------------*/

        //-------------------------Optimized K-means----------------------------------
        long beginTime = System.currentTimeMillis();
        clusterResult = ClusterUtil.optimizedKMeansCluster(graph,5);
        printResult(graph,clusterResult,"Optimized K-means");
        System.out.println("OptimizedKMeans Spent:"+(System.currentTimeMillis()-beginTime));
        //----------------------------------------------------------------------------

        //--------------------------KStar-------------------------------------------
/*        double maxDistance = 0;
        double minDistance = Integer.MAX_VALUE;
        double sum = 0;
        for (int i=0;i<100;i++){*/
        beginTime = System.currentTimeMillis();
        clusterResult = ClusterUtil.KStarMeansCluster(graph,5,graph.getNodes().length);
        printResult(graph,clusterResult,"kStar");
        System.out.println("kStar Spent:"+(System.currentTimeMillis()-beginTime));

        beginTime = System.currentTimeMillis();
        clusterResult = ClusterUtil.H_KClusterSBS(graph,5);
        printResult(graph,clusterResult,"H_KClusterSBS");
        System.out.println("H_KClusterSBS Spent:"+(System.currentTimeMillis()-beginTime));
/*            sum +=clusterResult.getMaxDistance();
            if (clusterResult.getMaxDistance()>maxDistance){
                maxDistance = clusterResult.getMaxDistance();
            }
            if (clusterResult.getMaxDistance()<minDistance){
                minDistance = clusterResult.getMaxDistance();
            }
        }
        System.out.println("maxDistance in 100 times:"+maxDistance);
        System.out.println("minDistance in 100 times:"+minDistance);
        System.out.println("average maxDistance in 100 times:"+sum/100);
        System.out.println();*/

        //--------------------------------------------------------------------------
    }

    private static void printResult(Graph graph,ClusterResult clusterResult, String method){
        System.out.println();
        System.out.println("----------------------------"+method+"--------------------------------------");
        System.out.println("MaxDistance:"+clusterResult.getMaxDistance());
        System.out.println("Average Distance:"+ AverageLatencyUtil.averageDistance(graph,clusterResult.getNodeMap()));
        System.out.println("SDNS:"+ LoadBalanceUtil.SDNS(graph,clusterResult.getNodeMap()));
        for (Integer i:clusterResult.getNodeMap().keySet()){
            System.out.print("cluster:"+i+":");
            for (Node node:clusterResult.getNodeMap().get(i)){
                System.out.print(node.getId()+"-"+node.getCity()+",");
            }
            System.out.println();
        }
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println();
    }

}
