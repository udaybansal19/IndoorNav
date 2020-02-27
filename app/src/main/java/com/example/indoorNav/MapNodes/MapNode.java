package com.example.indoorNav.MapNodes;

import android.util.Pair;

import com.google.ar.core.Pose;
import com.google.ar.sceneform.AnchorNode;

import java.util.ArrayList;
import java.util.Collections;

public class MapNode extends AnchorNode {
    public AnchorNode anchorNode;
    public String mapNodeId;
    public ArrayList<Pair<Double,MapNode>> adjacentAnchors;
    public int accessLevel = 1;


    public MapNode() {
        anchorNode = new AnchorNode();
        mapNodeId = new String();
        adjacentAnchors = new ArrayList<>();
    }

    public MapNode(AnchorNode anchorNode) {
        this.anchorNode = anchorNode;
        mapNodeId = new String();
        adjacentAnchors = new ArrayList<>();
    }

    public void setParent (AnchorNode parentNode) {
        anchorNode.setParent(parentNode);
    }

    public void addAdjacentAnchor (MapNode mapNode) {

        Double weight = getEdgeWeight(mapNode);

        Pair<Double,MapNode> p = new Pair<Double, MapNode>(weight, mapNode);

        adjacentAnchors.add(p);

    }


    //TODO: Ensure edge weight returned is the distance along the plane only
    // i.e. straight distance between the nodes placed on the floor.
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
