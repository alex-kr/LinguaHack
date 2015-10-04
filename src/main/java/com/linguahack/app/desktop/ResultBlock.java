package com.linguahack.app.desktop;

/**
 * Created by Roman Stetsiuk on 10/4/15.
 */
public class ResultBlock {
    private String relationship_type;
    private double percent;

    public ResultBlock(String relationship_type, double percent) {
        this.relationship_type = relationship_type;
        this.percent = percent;
    }

    public String getRelationship_type() {
        return relationship_type;
    }

    public void setRelationship_type(String relationship_type) {
        this.relationship_type = relationship_type;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    @Override
    public String toString() {
        return "ResultBlock{" +
                "relationship_type='" + relationship_type + '\'' +
                ", percent=" + percent +
                '}';
    }
}
