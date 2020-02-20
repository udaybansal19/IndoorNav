package com.example.indoorNav.MapNodes;

import com.example.indoorNav.Cloud.HostAnchor;

//Node placed on path for cloud support and existing featues support.
public class PathNodes extends MapNode {

    public void hostNode(MapNode mapNode) {
        HostAnchor hostAnchor = new HostAnchor();
        hostAnchor.host(mapNode);
    }



}
