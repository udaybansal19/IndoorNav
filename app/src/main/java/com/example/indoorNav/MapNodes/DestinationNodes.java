package com.example.indoorNav.MapNodes;

import com.google.ar.sceneform.AnchorNode;

public class DestinationNodes extends MapNode {

    //Name of the destination
    public String name;
    //Description of location
    public String destDescrip;

    public DestinationNodes(AnchorNode anchorNode, String name) {
        super(anchorNode);
        this.name = name;
        //TODO: Add destination pointer renderable to the anchor.
    }
}
