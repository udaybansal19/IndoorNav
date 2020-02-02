/*
 * Copyright 2018 Google LLC. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.ar.sceneform.samples.hellosceneform;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.Toast;
import com.google.ar.core.Anchor;
import com.google.ar.core.Config;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.core.Config;
import com.google.ar.sceneform.ux.TransformableNode;

/**
 * This is an example activity that uses the Sceneform UX package to make common AR tasks easier.
 */
public class HelloSceneformActivity extends AppCompatActivity {
    private static final String TAG = HelloSceneformActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;

    private ArFragment arFragment;
    private ModelRenderable andyRenderable;
    private  ModelRenderable arrowRenderable;
    private  ModelRenderable markerRenderable;
    private Vector3 point1, point2;

    int c=0;
    int c1=0;
    private AnchorNode prevAnchorNode;

    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    // CompletableFuture requires api level 24
    // FutureReturnValueIgnored is not valid
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }

        setContentView(R.layout.activity_ux);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        // When you build a Renderable, Sceneform loads its resources in the background while returning
        // a CompletableFuture. Call thenAccept(), handle(), or check isDone() before calling get().
        ModelRenderable.builder()
                .setSource(this, R.raw.andy)
                .build()
                .thenAccept(renderable -> andyRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load highlight renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });

        ModelRenderable.builder()
                .setSource(this, Uri.parse("model.sfb"))
                .build()
                .thenAccept(renderable -> arrowRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load Arrow renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });

        ModelRenderable.builder()
                .setSource(this, Uri.parse("uploads_files_1993562_fbx.sfb"))
                .build()
                .thenAccept(renderable -> markerRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load Location marker renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });

        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (andyRenderable == null) {
                        return;
                    }

                    // Create the Anchor.
                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());



                    if(c==1) {
                        AnchorNode anchorNode1 = new AnchorNode(anchor);
                        anchorNode1.setParent(arFragment.getArSceneView().getScene());

                        point2 = anchorNode.getWorldPosition();

                        final Vector3 difference = Vector3.subtract(point1, point2);
                        final Vector3 directionFromTopToBottom = difference.normalized();
                        final Quaternion rotationFromAToB =
                                Quaternion.lookRotation(directionFromTopToBottom, Vector3.up());
                        MaterialFactory.makeTransparentWithColor(getApplicationContext(), new Color(0, 137, 244, (float)0.2))
                                .thenAccept(
                                        material -> {
                            /* Then, create a rectangular prism, using ShapeFactory.makeCube() and use the difference vector
                                   to extend to the necessary length.  */
                                            ModelRenderable model = ShapeFactory.makeCube(
                                                    new Vector3( 0.3f, 0.01f, difference.length()),
                                                    Vector3.zero(), material);
                            /* Last, set the world rotation of the node to the rotation calculated earlier and set the world position to
                                   the midpoint between the given points . */
                                            Node node = new Node();
                                            node.setParent(anchorNode1);
                                            node.setRenderable(model);
                                            node.setWorldPosition(Vector3.add(point1, point2).scaled(.5f));
                                            node.setWorldRotation(rotationFromAToB);
                                            prevAnchorNode.setLocalScale(new Vector3(0.1f,0.1f,0.1f));
                                            prevAnchorNode.setWorldRotation(rotationFromAToB);
                                            prevAnchorNode.setRenderable(arrowRenderable);
                                           anchorNode.setLocalScale(new Vector3(0.4f,0.4f,0.4f));
                                           anchorNode.setLocalPosition(new Vector3(0f,0f,0.5f));
                                            anchorNode.setRenderable(markerRenderable);
                                        }
                                );



//              // Create the transformable andy and add it to the anchor.
//              TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
//              node.getScaleController().setMaxScale(1000f);
//              node.getScaleController().setMinScale(50f);
//              node.setParent(anchorNode);
//              node.setRenderable(highlight);
//              node.select();
                    }else{
                        c=1;
                        point1 = anchorNode.getWorldPosition();
                        Anchor prevAnchor = hitResult.createAnchor();
                        prevAnchorNode = new AnchorNode(prevAnchor);
                        prevAnchorNode.setParent(arFragment.getArSceneView().getScene());
                    }
//                    if(c1==0)
//                    {
//                        c1++;
//                        anchorNode.setLocalScale(new Vector3(0.1f,0.1f,0.1f));
//                        anchorNode.setRenderable(arrowRenderable);
//                    }
                });
    }

    /**
     * Returns false and displays an error message if Sceneform can not run, true if Sceneform can run
     * on this device.
     *
     * <p>Sceneform requires Android N on the device as well as OpenGL 3.0 capabilities.
     *
     * <p>Finishes the activity if Sceneform can not run
     */
    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }
}
