package com.example.indoorNav.Cloud;

import com.example.indoorNav.MapNodes.MapNode;
import com.google.ar.core.Anchor;
import com.google.ar.core.Session;

public class HostAnchor extends CloudAnchor {

    public void host(Session session, MapNode mapNode) {

        session.hostCloudAnchor(mapNode.anchorNode.getAnchor());

    }
}
