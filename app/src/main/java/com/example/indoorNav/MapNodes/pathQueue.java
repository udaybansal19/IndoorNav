package com.example.indoorNav.MapNodes;

import java.util.Deque;
import java.util.LinkedList;

public class pathQueue extends PathNodes {

    private String pathName;
    private Deque<MapNode> pathQueue = new LinkedList<MapNode>();
    private double queueWeight = 0;

    public void addNode(MapNode mapNode) {

        if(pathQueue.isEmpty()){
            pathQueue.add(mapNode);
        }else if(pathQueue.size() == 1){
            pathQueue.add(mapNode);
            queueWeight+=pathQueue.peek().getEdgeWeight(mapNode);

        }else{
            Double first = pathQueue.getFirst().getEdgeWeight(mapNode);
            Double last = pathQueue.peekLast().getEdgeWeight(mapNode);

            if(first >= last){
                pathQueue.addLast(mapNode);
                queueWeight+=last;
            }else{
                pathQueue.addFirst(mapNode);
                queueWeight+=first;
            }

        }
    }

    public double getQueueWeight() {
        return queueWeight;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }
}
