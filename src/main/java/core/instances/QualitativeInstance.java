package core.instances;


import core.Instance;
import java.util.Set;

public class QualitativeInstance implements Instance {

    private Long id;
    private Set<Long> items;

    public QualitativeInstance(Long id, Set<Long> items) {
        this.id = id;
        this.items = items;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("id: " + id + " [");
        for (Long item: items) {
            sb.append(item + ", ");
        }
        sb.append("] \n");
        return sb.toString();
    }



    public Long getId() {
        return id;
    }


    public int getSize() {
        return items.size();
    }

    public Set<Long> getItems() {
        return items;
    }


}
