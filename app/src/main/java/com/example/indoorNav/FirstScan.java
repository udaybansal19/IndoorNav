package com.example.indoorNav;

import android.view.MotionEvent;

import com.example.indoorNav.MapNodes.GlobalNode;
import com.example.indoorNav.MapNodes.MapNode;
import com.example.indoorNav.MapNodes.PathNodes;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.example.indoorNav.R;

public class FirstScan extends IndoorNavMainActivity {

    GlobalNode globalNode = new GlobalNode();

   public void makePathNode(AnchorNode anchorNode) {

       PathNodes pathNodes = new PathNodes();

       pathNodes.



   }






    private void highlight (AnchorNode anchorNode, AnchorNode first , AnchorNode last) {

        final Vector3 difference = Vector3.subtract(first.getWorldPosition(), last.getWorldPosition());
        final Vector3 directionFromTopToBottom = difference.normalized();
        final Quaternion rotationFromAToB =
                Quaternion.lookRotation(directionFromTopToBottom, Vector3.up());
        MaterialFactory.makeTransparentWithColor(getApplicationContext(), new Color(0, 137, 244, (float) 0.2))
                .thenAccept(
                        material -> {
                            /* Then, create a rectangular prism, using ShapeFactory.makeCube() and use the difference vector
                                   to extend to the necessary length.  */
                            ModelRenderable model = ShapeFactory.makeCube(
                                    new Vector3(0.3f, 0.01f, difference.length()),
                                    Vector3.zero(), material);
                            /* Last, set the world rotation of the node to the rotation calculated earlier and set the world position to
                                   the midpoint between the given points . */
                            Node node = new Node();
                            node.setParent(anchorNode);
                            node.setRenderable(model);
                            node.setWorldPosition(Vector3.add(first.getWorldPosition(),last.getWorldPosition()).scaled(.5f));
                            node.setWorldRotation(rotationFromAToB);
                        });

    }

}
