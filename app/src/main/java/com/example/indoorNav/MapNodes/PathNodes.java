package com.example.indoorNav.MapNodes;

import com.example.indoorNav.Cloud.HostAnchor;
import com.google.ar.sceneform.AnchorNode;

//Node placed on path for cloud support and existing featues support.
public class PathNodes extends MapNode {

    public PathNodes(AnchorNode anchorNode) {

        this.anchorNode = anchorNode;

    }

    public void hostNode(MapNode mapNode) {
        HostAnchor hostAnchor = new HostAnchor();
        hostAnchor.host(mapNode);
    }

}
