package com.example.indoorNav.MapNodes;

import java.util.ArrayList;

public class pathQueue extends PathNodes {

    private String pathName;
    private ArrayList pathQueue = new ArrayList();

    public void addNode(MapNode mapNode) {
        pathQueue.add(mapNode);
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }
}
