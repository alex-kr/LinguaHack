package to;

import core.instances.QualitativeInstance;

import java.util.List;

/**
 * Created by pc on 02.06.2015.
 */
public class DataSetTo {
    Double repulsion;
    Integer minSize;
    List<QualitativeInstance> data;


    public Double getRepulsion() {
        return repulsion;
    }

    public void setRepulsion(Double repulsion) {
        this.repulsion = repulsion;
    }

    public Integer getMinSize() {
        return minSize;
    }

    public void setMinSize(Integer minSize) {
        this.minSize = minSize;
    }

    public List<QualitativeInstance> getData() {
        return data;
    }

    public void setData(List<QualitativeInstance> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DataSetTo{" +
                "repulsion=" + repulsion +
                ", minSize=" + minSize +
                ", data=" + data +
                '}';
    }
}
