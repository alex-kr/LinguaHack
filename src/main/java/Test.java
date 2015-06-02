import CLOPE.CLOPECluster;
import CLOPE.CLOPEClusterer;

import core.Instance;
import core.instances.QualitativeInstance;

import java.util.*;


/**
 * Created by pc on 05.04.2015.
 */
public class Test {




    public static HashMap<Long, List<Long>> testIt() {
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
            HashMap<Long,List<Long>> result = null;
            try {
                clusterer.setMinClusterSize(5);
                clusterer.setRepulsion(1.5);
                result = clusterer.buildClusterer(instances);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return result;
            }
        }

}
