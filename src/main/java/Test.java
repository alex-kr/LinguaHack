import CLOPE.CLOPECluster;
import CLOPE.CLOPEClusterer;

import core.Instance;
import core.instances.QualitativeInstance;

import java.util.*;

import static utils.MapUtil.sortByValues;

/**
 * Created by pc on 05.04.2015.
 */
public class Test {

/*
    public static void main(String[] args){
        HashMap<Long, CLOPECluster> clusters = new HashMap<Long, CLOPECluster>();
        QualitativeInstance inst = new QualitativeInstance(1L, Arrays.asList(1L, 2L, 3L));
        CLOPECluster cluster1 = new CLOPECluster();
        CLOPECluster cluster2 = new CLOPECluster();
        CLOPECluster cluster3 = new CLOPECluster();
        CLOPECluster cluster4 = new CLOPECluster();
        CLOPECluster cluster5 = new CLOPECluster();
        CLOPECluster cluster6 = new CLOPECluster();
        CLOPECluster cluster7 = new CLOPECluster();
        cluster1.addInstance(inst);
        cluster1.addInstance(inst);
        cluster1.addInstance(inst);
        cluster1.addInstance(inst);
        cluster2.addInstance(inst);
        cluster3.addInstance(inst);
        cluster3.addInstance(inst);
        cluster4.addInstance(inst);
        cluster4.addInstance(inst);
        cluster5.addInstance(inst);
        cluster6.addInstance(inst);
        cluster6.addInstance(inst);
        cluster6.addInstance(inst);
        cluster6.addInstance(inst);
        cluster7.addInstance(inst);
        cluster7.addInstance(inst);
        clusters.put(1L, cluster1);
        clusters.put(2L, cluster2);
        clusters.put(3L, cluster3);
        clusters.put(4L, cluster4);
        clusters.put(5L, cluster5);
        clusters.put(6L, cluster6);
        clusters.put(7L, cluster7);
        Map m = sortByValues((Map) clusters);
        System.out.println(m.toString());

    }
*/


    public static String testIt() {
        List<Instance> instances = new ArrayList<Instance>();
        List<Long> tmp= new ArrayList<Long>();  tmp.add(10L);
        System.out.println("Point 1");

            for (int i = 0; i < 1000; i++) {
                System.out.println("Point 2:" + i);
                Set<Long> items = new HashSet<Long>();
                for (int j = 0; j < (int) (Math.random() * 10 + 1); j++) {
                    Long k = (Long) Math.round(Math.random() * 50);

                        items.add(k);

                }
                System.out.println("Point 3: " + items.toString() + "\n\n\n");
                instances.add(new QualitativeInstance(Long.valueOf(i), items));
            }
            System.out.println(instances);
            CLOPEClusterer clusterer = new CLOPEClusterer();
            try {
                clusterer.setMinClusterSize(5);
                clusterer.setRepulsion(1.5);
                clusterer.buildClusterer(instances);
                return clusterer.clusterAssignments.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

}
