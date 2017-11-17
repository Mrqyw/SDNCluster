
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
        String dataSetBasePath = System.getProperty("user.dir");
        String gmlPath = "\\gml\\OS3E.gml";

        Graph graph = GmlUtil.converseGmlFile(dataSetBasePath+gmlPath);
        double maxDistance = 0;
        int sum = 0;
        for (int i=1;i<100;i++){
            ClusterResult clusterResult = ClusterUtil.KMeansCluster(null,graph,5);
            sum +=clusterResult.getMaxDistance();
            if (clusterResult.getMaxDistance()>maxDistance){
                maxDistance = clusterResult.getMaxDistance();
            }
        }
        System.out.println(maxDistance);
        System.out.println(sum/100);
        ClusterResult clusterResult = ClusterUtil.optimizedKMeansCluster(graph,5);
        System.out.println(clusterResult.getMaxDistance());
        for (Integer i:clusterResult.getNodeMap().keySet()){
            System.out.print("cluster:"+i+":");
            for (Node node:clusterResult.getNodeMap().get(i)){
                System.out.print(node.getId()+",");
            }
            System.out.println();
        }
    }

}
