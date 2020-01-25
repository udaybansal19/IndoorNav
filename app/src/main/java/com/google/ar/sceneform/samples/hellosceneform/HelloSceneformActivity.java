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

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;
import com.google.ar.core.Anchor;
import com.google.ar.core.Config;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.math.Vector3Evaluator;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.core.Config;
import com.google.ar.sceneform.ux.TransformableNode;

import java.sql.Time;
import java.util.Random;
import java.util.Timer;
import java.util.*;
import java.util.TimerTask;

/**
 * This is an example activity that uses the Sceneform UX package to make common AR tasks easier.
 */
public class HelloSceneformActivity extends AppCompatActivity {
  private static final String TAG = HelloSceneformActivity.class.getSimpleName();
  private static final double MIN_OPENGL_VERSION = 3.0;

  private ArFragment arFragment;
  private ModelRenderable andyRenderable;
  private ModelRenderable playerRenderable;
  ArrayList<ModelRenderable> spaceRenderable = new ArrayList<ModelRenderable>();
  private  ModelRenderable highlight;
  private ObjectAnimator objectAnimation;

    private int oneTimeFlag = 0;
    private AnchorNode prevAnchorNode;
    private AnchorNode endNode;
    private Node playerNode;
    private Node andy;
    private Node st,en,temp,temp2;

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
      //For Arrow
      String arg[] = {"Astronaut.sfb",
      "jupiter.sfb",
      "mars.sfb",
              "mercury.sfb","neptune.sfb","pluto.sfb","saturn.sfb","Spacestation.sfb","ufo.sfb","uranus.sfb","venus.sfb","Earth.sfb"
      };
      for(int i=0;i<12;i++)
      modelRender(arg[i],i);
    ModelRenderable.builder()
        .setSource(this, Uri.parse("spaceship.sfb"))
        .build()
        .thenAccept(renderable -> playerRenderable = renderable)
        .exceptionally(
            throwable -> {
              Toast toast =
                  Toast.makeText(this, "Unable to load playerRenderable", Toast.LENGTH_LONG);
              toast.setGravity(Gravity.CENTER, 0, 0);
              toast.show();
              return null;
            });
      ModelRenderable.builder()
              .setSource(this, R.raw.andy)
              .build()
              .thenAccept(renderable -> andyRenderable = renderable)
              .exceptionally(
                      throwable -> {
                          Toast toast =
                                  Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                          toast.setGravity(Gravity.CENTER, 0, 0);
                          toast.show();
                          return null;
                      });



          arFragment.setOnTapArPlaneListener(
                  (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                      if (andyRenderable == null || oneTimeFlag!=0) {
                          return;
                      }
                      oneTimeFlag++;
                      // Create the Anchor.
                      Anchor anchor = hitResult.createAnchor();
                      endNode = new AnchorNode(anchor);
                      endNode.setParent(arFragment.getArSceneView().getScene());

                      // Create the transformable andy and add it to the anchor.
//                andy = new Node();
//                andy.setParent(startNode);
//                andy.setRenderable(andyRenderable);
                      // Create the end position and start the animation.
                      Vector3 pos = new Vector3(1f, 5f, 10f);
                      Node start = new Node();
                      Node end = new Node();
                      endNode.addChild(end);

                      start.setParent(end);
                      start.setLocalPosition(new Vector3(0f, 0f, -1f));
                      st = start;
                      en = end;

                      Vector3 point1, point2;
                      point1 = start.getWorldPosition();
                      point2 = end.getWorldPosition();

    /*
        First, find the vector extending between the two points and define a look rotation
        in terms of this Vector.
    */
                      final Vector3 difference = Vector3.subtract(point1, point2);
                      final Vector3 directionFromTopToBottom = difference.normalized();
                      final Quaternion rotationFromAToB =
                              Quaternion.lookRotation(directionFromTopToBottom, Vector3.up());
                      MaterialFactory.makeOpaqueWithColor(getApplicationContext(), new Color(0, 0, 0, 0.01f))
                              .thenAccept(
                                      material -> {
                            /* Then, create a rectangular prism, using ShapeFactory.makeCube() and use the difference vector
                                   to extend to the necessary length.  */
                                          ModelRenderable model = ShapeFactory.makeCube(
                                                  new Vector3(1f, 0f, difference.length()),
                                                  Vector3.zero(), material);
                            /* Last, set the world rotation of the node to the rotation calculated earlier and set the world position to
                                   the midpoint between the given points . */
                                          Node node = new Node();
                                          node.setParent(endNode);
                                          node.setRenderable(model);
                                          node.setWorldPosition(Vector3.add(point1, point2).scaled(.5f));
                                          node.setWorldRotation(rotationFromAToB);
                                      }
                              );
                      arFragment.getArSceneView().getSession().getConfig().setPlaneFindingMode(Config.PlaneFindingMode.DISABLED);
                      playerNode = endNode;
                      playerNode.setLocalScale(new Vector3(0.6f,0.6f,0.6f));
                      playerNode.setRenderable(playerRenderable);
                      allPlanetsMove();
                  });

  }

  private void allPlanetsMove(){
      AnimatorSet s = new AnimatorSet();
      s.play(planetsMove());
      s.addListener(new Animator.AnimatorListener() {
          @Override
          public void onAnimationStart(Animator animation) {
              //endNode.setParent(null);
          }

          @Override
          public void onAnimationEnd(Animator animation) {
              temp.setParent(null);
              allPlanetsMove();

          }

          @Override
          public void onAnimationCancel(Animator animation) {

          }

          @Override
          public void onAnimationRepeat(Animator animation) {
          }
      });
      s.start();
  }

  private Node planetsSet(){
      Node start = new Node();
      start.setParent(en);
      start.setLocalPosition(new Vector3(0f,0f,-1f));
      temp = start;

      Node st1 = new Node();
      Node st2 = new Node();
      Node st3 = new Node();

      st1.setParent(temp);
      st2.setParent(temp);
      st3.setParent(temp);

      temp.setLocalScale(new Vector3(0.5f,0.5f,0.5f));

      Random rand = new Random();
      int r = rand.nextInt(3);


      st1.setLocalPosition(new Vector3(0.6f,0f,0f));
      st2.setLocalPosition(new Vector3(0f,0f,0f));
      st3.setLocalPosition(new Vector3(-0.6f,0f,0f));

      ModelRenderable m1 = randomObj();
      ModelRenderable m2 = randomObj();
      while(m1==m2)
          m2 = randomObj();

          if (r % 3 != 0)
          {
              st1.setRenderable(m1);
              m1=m2;
          }
          if (r % 3 != 1)
          {
              st2.setRenderable(m1);
              if(r==0)
                  m1=m2;
          }
          if (r % 3 != 2)
              st3.setRenderable(m1);


      return temp;
  }

  private ObjectAnimator planetsMove(){

      // planetsAnimation(temp);

      objectAnimation = new ObjectAnimator();
      objectAnimation.setTarget(planetsSet());

      // All the positions should be world positions
      // The first position is the start, and the second is the end.
      objectAnimation.setObjectValues(temp.getWorldPosition(), en.getWorldPosition());

      // Use setWorldPosition to position andy.
      objectAnimation.setPropertyName("worldPosition");

      // The Vector3Evaluator is used to evaluator 2 vector3 and return the next
      // vector3.  The default is to use lerp.
      objectAnimation.setEvaluator(new Vector3Evaluator());
      // This makes the animation linear (smooth and uniform).
      objectAnimation.setInterpolator(new LinearInterpolator());
      // Duration in ms of the animation.
      objectAnimation.setDuration(1500);
      //objectAnimation.setRepeatCount(Animation.INFINITE);

      return objectAnimation;
    }

    private ModelRenderable randomObj(){
        Random rand = new Random();
        int r = rand.nextInt(1000);
        return spaceRenderable.get(r%spaceRenderable.size());
    }

    private void modelRender(String arg,int i){
          ModelRenderable.builder()
                  .setSource(this, Uri.parse(arg))
                  .build()
                  .thenAccept(renderable -> spaceRenderable.add(renderable))
                  .exceptionally(
                          throwable -> {
                              Toast toast =
                                      Toast.makeText(this, "Unable to load " + arg, Toast.LENGTH_LONG);
                              toast.setGravity(Gravity.CENTER, 0, 0);
                              toast.show();
                              return null;
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
