package to;

import java.util.List;

/**
 * Created by pc on 02.06.2015.
 */
public class ClusterTO {
    Long clusterId;
    List<Long> items;

    public ClusterTO(Long clusterId, List<Long> items) {
        this.clusterId = clusterId;
        this.items = items;
    }

    public Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(Long clusterId) {
        this.clusterId = clusterId;
    }

    public List<Long> getItems() {
        return items;
    }

    public void setItems(List<Long> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "ClusterTO{" +
                "clusterId=" + clusterId +
                ", items=" + items +
                '}';
    }
}
