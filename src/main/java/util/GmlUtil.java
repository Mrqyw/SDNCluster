package util;


import model.Floyd;
import model.Graph;
import model.Node;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yiwenqiu
 * @Description 转换图
 * @date 22:00 2017/11/14
 */
public class GmlUtil {

    /**
     * GML文件转 Graph<N,E>
     * @param path
     * @return
     * @throws IOException
     */
    public static Graph converseGmlFile(String path) throws IOException {
        File file = FileUtil.isFileExist(path);
        if (file == null){
            return null;
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        List<Node> nodes = new ArrayList<Node>();
        String line = "";
        Graph graph = new Graph();
        while(true){
            if ((line = in.readLine())!=null){
                if (line.contains(Constant.GML_NODE_STRING)){
                    Node newNode = new Node();
                    while(true){
                        String lineString = in.readLine().trim();
                        if (lineString.contains(Constant.GML_END_STRING)){
                            break;
                        }
                        if (lineString.contains(Constant.GML_NODE_ID_STRING)){
                            newNode.setId(Integer.parseInt(lineString.split(Constant.SPACE_STRING)[1]));
                            continue;
                        }
                        if (lineString.contains(Constant.GML_NODE_CITY_STRING)){
                            newNode.setCity(lineString.split(Constant.SPACE_STRING)[1]);
                            continue;
                        }
                        if (lineString.contains(Constant.GML_NODE_LONGITUDE_STRING)){
                            newNode.setLongitude(Double.parseDouble(lineString.split(Constant.SPACE_STRING)[1]));
                            continue;
                        }
                        if (lineString.contains(Constant.GML_NODE_LATITUDE_STRING)){
                            newNode.setLatitude(Double.parseDouble(lineString.split(Constant.SPACE_STRING)[1]));
                            continue;
                        }
                    }
                    nodes.add(newNode);
                }else if(line.contains(Constant.GML_EDGE_STRING)){
                    //由于节点信息都放在链路信息前面，因此当读到链路信息时，表示节点信息已经读取完毕。
                    Node[] nodeArray = nodes.toArray(new Node[nodes.size()]);
                    if (graph.getNodes() == null){
                        graph.setNodes(nodeArray);
                    }
                    if (graph.getEdges() == null){
                        graph.initEdges(nodeArray.length);
                    }
                    int sourceId = 0;
                    int targetId = 0;
                    while(true){
                        String lineString = in.readLine().trim();
                        if (lineString.contains(Constant.GML_END_STRING)){
                            break;
                        }
                        if (lineString.contains(Constant.GML_EDGE_SOURCE_STRING)){
                            sourceId = Integer.parseInt(lineString.split(Constant.SPACE_STRING)[1]);
                            continue;
                        }
                        if (lineString.contains(Constant.GML_EDGE_TARGET_STRING)){
                            targetId = Integer.parseInt(lineString.split(Constant.SPACE_STRING)[1]);
                            continue;
                        }
                    }
                    graph.getEdges()[sourceId][targetId] = graph.getEdges()[targetId][sourceId] = DistanceUtil.distance(nodeArray[sourceId],nodeArray[targetId]);
                }else {
                    continue;
                }
            }else {
                break;
            }
        }
        Floyd floyd = new Floyd(graph.getEdges());
        graph.setShortestEdges(floyd.getPathMatrix());
        graph.setShortestEdgesHop(floyd.getHopMatrix());
        return graph;
    }



}
