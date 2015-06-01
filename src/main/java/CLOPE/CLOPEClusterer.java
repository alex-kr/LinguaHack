package CLOPE;

import core.Clusterer;
import core.Instance;

import java.util.*;
import static utils.MapUtil.*;

public class CLOPEClusterer implements Clusterer {


    private final double REPULSION_DEFAULT = 2;

    private final int MIN_CLUSTER_SIZE_DEFAULT = 1;


    private double repulsion = REPULSION_DEFAULT;

    private int minClusterSize = MIN_CLUSTER_SIZE_DEFAULT;

    /**
     *<key = instanceId, value = clusterId>
     */
    public HashMap<Long, Long> clusterAssignments = new HashMap<Long, Long>();

    public TreeMap<Long, CLOPECluster> clusters = new TreeMap<Long, CLOPECluster>();

    public void setRepulsion(double repulsion) {
        this.repulsion = repulsion;
    }


    public void setMinClusterSize(int minClusterSize) {
        this.minClusterSize = minClusterSize;
    }


    private void addInstanceToBestCluster(Instance inst) {
        if (!addInstanceToBestExistingClusterIfProfitable(inst)) {
            addInstanceToNewCluster(inst);
        }
    }

    private void addInstanceToNewCluster(Instance inst) {
        CLOPECluster newCluster = new CLOPECluster();
        clusters.put(newCluster.getId(), newCluster);
        newCluster.addInstance(inst);
        clusterAssignments.put(inst.getId(), newCluster.getId());
    }

    /*
    * returns false if deltaAdd in new cluster is higher
    *
    */
    private boolean addInstanceToBestExistingClusterIfProfitable(Instance inst) {
        double delta;
        double deltaMax;
        Long clusterMaxId = null;
        int tempS = inst.getSize();
        int tempW = inst.getSize();
        deltaMax = tempS / Math.pow(tempW, repulsion);

        for (CLOPECluster cluster : clusters.values()) {
            delta = cluster.getDeltaAdd(inst, repulsion);
            if (delta > deltaMax) {
                deltaMax = delta;
                clusterMaxId = cluster.getId();
            }
        }

        if (clusterMaxId != null) {
            (clusters.get(clusterMaxId)).addInstance(inst);
            clusterAssignments.put(inst.getId(), clusterMaxId);
            return true;
        }

        return false;
    }

    private void addInstanceToBestExistingCluster(Instance inst) {
        double delta;
        double deltaMax;
        Long clusterMaxId = clusters.firstKey();
        deltaMax = (clusters.get(clusterMaxId)).getDeltaAdd(inst, repulsion);

        for (CLOPECluster cluster : clusters.values()) {
            delta = cluster.getDeltaAdd(inst, repulsion);
            if (delta > deltaMax) {
                deltaMax = delta;
                clusterMaxId = cluster.getId();
            }
        }

        (clusters.get(clusterMaxId)).addInstance(inst);
        clusterAssignments.put(inst.getId(), clusterMaxId);

    }



    /**
     * Move instance to best cluster
     */
    private boolean moveInstanceToBestCluster(Instance inst) {
        boolean moved = false;
        Long clusterId = clusterAssignments.get(inst.getId());
        //
        Long oldClusterId = clusterAssignments.get(inst.getId());
        //
        double delta = 0.0;
        double deltaMax = 0.0;

        (clusters.get(clusterId)).deleteInstance(inst);
        deltaMax = (clusters.get(clusterId)).getDeltaAdd(inst, repulsion);

        for (CLOPECluster cluster : clusters.values()) {
            delta = cluster.getDeltaAdd(inst, repulsion);
            if (delta > deltaMax) {
                deltaMax = delta;
                clusterId = cluster.getId();
                moved = true;
            }
        }

        delta = inst.getSize() / Math.pow(inst.getSize(), repulsion);
        if (delta > deltaMax) {
            addInstanceToNewCluster(inst);
            moved = true;
            System.err.println("Instance " + inst.getId() + " moved to new cluster");
        } else {
            (clusters.get(clusterId)).addInstance(inst);
            clusterAssignments.put(inst.getId(), clusterId);
        }
        /*if (oldClusterId != clusterId) {
            System.err.println("Instance " + inst.getId() + " moved from cluster " + oldClusterId + " to cluster " + clusterId);
            System.err.println("DeltaAdd: " + deltaMax);
            System.err.println("Instance " + inst.getId() + " val: " + inst.toString());
        }*/
        return moved;


    }

    private void redistributeInstances(List<Instance> data) {
        boolean moved;
        do {
            moved = false;
            for (Instance inst: data) {
                if (moveInstanceToBestCluster(inst)) {
                    moved = true;
                }
            }
        } while (moved);
    }

    public HashMap<Long, Long> buildClusterer(List<Instance> data) throws Exception {
        clusters.clear();
        clusterAssignments.clear();

        //Phase 1
        for (Instance inst: data) {
            addInstanceToBestCluster(inst);
        }
        //Phase 2
        redistributeInstances(data);

        //Phase 3

        Map<Long, CLOPECluster> sortedClusters = sortByValues(clusters);
        for (CLOPECluster cluster: sortedClusters.values()) {
            if (cluster.getSize() < minClusterSize) {
                List<Instance> instancesToMove = cluster.getInstances();
                clusters.remove(cluster.getId());
                for (Instance inst: instancesToMove) {
                    addInstanceToBestExistingCluster(inst);
                }
            }
        }

        return clusterAssignments;
    }
}
