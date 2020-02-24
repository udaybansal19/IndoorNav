package com.example.indoorNav.MapNodes;

import com.google.ar.core.Pose;
import com.google.ar.sceneform.AnchorNode;

import java.util.ArrayList;
import java.util.Collections;

public class MapNode extends AnchorNode {
    public AnchorNode anchorNode;
    public String mapNodeId;
    //TODO: Check if a priority queue would be better for storing edge weights.
    public ArrayList<MapNode> adjacentAnchors;
    public MapNode() {
        anchorNode = new AnchorNode();
        mapNodeId = new String();
        adjacentAnchors = new ArrayList<>();
    }

    public void setParent (AnchorNode parentNode) {
        anchorNode.setParent(parentNode);
    }

    public double getEdgeWeight (MapNode mapNode) {
        Pose startPose = anchorNode.getAnchor().getPose();
        Pose endPose = mapNode.anchorNode.getAnchor().getPose();

        // Compute the difference vector between the two hit locations.
        float dx = startPose.tx() - endPose.tx();
        float dy = startPose.ty() - endPose.ty();
        float dz = startPose.tz() - endPose.tz();

        // Compute the straight-line distance.
        double distanceMeters = (float) Math.sqrt(dx*dx + dy*dy + dz*dz);

        return distanceMeters;
    }
}
