package com.linguahack.app.desktop;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roman Stetsiuk on 10/4/15.
 */
public class MatchingResult {
    private List<ResultBlock> contexts = new ArrayList<ResultBlock>();

    public MatchingResult() {
    }

    public List<ResultBlock> getResultBlock() {
        return contexts;
    }

    public void setResultBlock(List<ResultBlock> resultBlock) {
        this.contexts = resultBlock;
    }

    public void addBlock(ResultBlock block) {
        contexts.add(block);
    }
}
