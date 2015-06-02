package to;

import core.instances.QualitativeInstance;

import java.util.List;

/**
 * Created by pc on 02.06.2015.
 */
public class DataSetTo {
    double repulsion;
    int minSize;
    List<QualitativeInstance> data;


    public double getRepulsion() {
        return repulsion;
    }

    public void setRepulsion(double repulsion) {
        this.repulsion = repulsion;
    }

    public int getMinSize() {
        return minSize;
    }

    public void setMinSize(int minSize) {
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
