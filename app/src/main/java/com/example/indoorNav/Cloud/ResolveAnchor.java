package com.example.indoorNav.Cloud;

import com.example.indoorNav.MapNodes.MapNode;
import com.google.ar.core.Anchor;
import com.google.ar.core.Session;
import com.google.ar.sceneform.AnchorNode;

public class ResolveAnchor extends CloudAnchor{

    public void resolve (Session session, MapNode mapNode) {
        Anchor anchor = session.resolveCloudAnchor(mapNode.mapNodeId);
        mapNode.anchorNode.setAnchor(anchor);
    }
}
