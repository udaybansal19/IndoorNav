package com.example.indoorNav.MapNodes;

import com.google.ar.sceneform.AnchorNode;

public class MapNode {
    public AnchorNode anchorNode;
    public String mapNodeId;
    public MapNode() {
        anchorNode = new AnchorNode();
        mapNodeId = new String();
    }
}
