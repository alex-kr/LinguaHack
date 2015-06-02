package CLOPE;

import core.Cluster;
import core.Instance;
import core.instances.QualitativeInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CLOPECluster implements Cluster, Comparable {

    private static Long clustersCounter = 0L;

    private Long id;

    /**
     * Number of transactions
     */
    private int n = 0;

    /**
     * Number of distinct items (or width)
     */
    private int w = 0;

    /**
     * Size of cluster
     */
    private int s = 0;

    /**
     * Cluster instances
     */
    private List<QualitativeInstance> instances = new ArrayList<QualitativeInstance>();

    /**
     * Hash of <item, occurrence> pairs
     */
    HashMap<Long, Long> occ = new HashMap();

    public CLOPECluster(){
        this.id = clustersCounter;
        clustersCounter++;
    }

    public Long getId() {
        return id;
    }

    public List<QualitativeInstance> getInstances(){
        return instances;
    }

    public int getNumberOfTransactions(){
        return n;
    }

    public int getWidth(){
        return w;
    }

    public int getSize() {
        return s;
    }

    @Override
    public int compareTo(Object o) throws NullPointerException {
        int result = 0;
        if(o != null) {
            if (this.getNumberOfTransactions() < ((CLOPECluster)o).getNumberOfTransactions() ) {
                result =  -1;
            } else if (this.getNumberOfTransactions() == ((CLOPECluster)o).getNumberOfTransactions() ) {
                result = 0;
            } else {
                result = 1;
            }
        } else {
            throw new NullPointerException();
        }

        return result;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Cluster_id: ");
        sb.append(id);
        sb.append("\nItems:\n");
        for (HashMap.Entry<Long, Long> entry : occ.entrySet()) {
            sb.append(entry.getKey());
            sb.append(" : ");
            sb.append(entry.getValue());
            sb.append("\n");
        }
        return sb.toString();
    }


    public void addItem(Long item) {
        Long count;
        if (!this.occ.containsKey(item)) {
            this.occ.put(item, 1L);
        } else {
            count = this.occ.get(item);
            count++;
            this.occ.remove(item);
            this.occ.put(item, count);
        }
        this.s++;
    }


    public void deleteItem(Long item) {
        Long count;

        count = this.occ.get(item);

        if (count == 1) {
            this.occ.remove(item);

        } else {
            count--;
            this.occ.remove(item);
            this.occ.put(item, count);
        }
        this.s--;
    }

    /**
     * Calculate Delta
     */
    public double getDeltaAdd(QualitativeInstance inst, double r) {
        int newS;
        int newW;
        double profit;
        double newProfit;
        double deltaProfit;
        newS = 0;
        newW = occ.size();

        for (Long item: inst.getItems()){
            newS++;
            if (this.occ.get(item) == null) {
                newW++;
            }
        }

        newS += s;


        if (n == 0) {
            deltaProfit = newS / Math.pow(newW, r);
        } else {
            profit = s * n / Math.pow(w, r);
            newProfit = newS * (n + 1) / Math.pow(newW, r);
            deltaProfit = newProfit - profit;
        }
        return deltaProfit;
    }

    /**
     * Add instance to cluster
     */
    public void addInstance(QualitativeInstance inst) throws NullPointerException{
        for (Long item: inst.getItems()) {
                addItem(item);
        }
        this.w = this.occ.size();
        this.n++;
        instances.add(inst);
    }

    /**
     * Delete instance from cluster
     */
    public void deleteInstance(QualitativeInstance inst) throws NullPointerException{
        for (Long item: inst.getItems()) {
            deleteItem(item);
        }
        this.w = this.occ.size();
        this.n--;
        instances.remove(inst);
    }

    /**
     * Removes all instances from cluster
     */
/*    public void clear() {
        if (!instances.isEmpty()){
            for (Instance inst: instances){
                deleteInstance(inst);
            }
            instances.clear();
        }
    }*/

}
