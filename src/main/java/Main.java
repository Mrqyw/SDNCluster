
import model.ClusterResult;
import model.Graph;
import model.Node;
import util.ClusterUtil;
import util.GmlUtil;

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

        //--------------------------K-means-------------------------------------------

        double maxDistance = 0;
        double minDistance = Integer.MAX_VALUE;
        double sum = 0;
        System.out.println();
        System.out.println("----------------------------K-means--------------------------------------");
        for (int i=1; i<100; i++){
            ClusterResult clusterResult = ClusterUtil.KMeansCluster(graph,6);
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
        System.out.println("average maxDistance in 100 times:"+sum/100);
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println();
        //--------------------------------------------------------------------------

        //-------------------------Optimized K-means----------------------------------
        ClusterResult clusterResult = ClusterUtil.optimizedKMeansCluster(graph,6);
        printResult(clusterResult,"Optimized K-means");
        //----------------------------------------------------------------------------

//        //--------------------------KStar-------------------------------------------
//        clusterResult = ClusterUtil.KStarMeansCluster(graph,5,15);
//        printResult(clusterResult,"kStar");
//        //--------------------------------------------------------------------------

    }

    private static void printResult(ClusterResult clusterResult, String method){
        System.out.println();
        System.out.println("----------------------------"+method+"--------------------------------------");
        System.out.println("MaxDistance:"+clusterResult.getMaxDistance());
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
