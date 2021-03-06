/*
 * Copyright (C) 2011 The Android Open Source Project
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
package com.orientation_build01;

import java.nio.*;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;

/**
 * Provides drawing instructions for a GLSurfaceView object. This class
 * must override the OpenGL ES drawing lifecycle methods:
 * <ul>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceCreated}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onDrawFrame}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceChanged}</li>
 * </ul>
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {

//    private Triangle mTriangle;
//    private Square mSquare;
	private Maze mMaze;
    private float mAngle=0.0f;
    private float x=0f,y=0f,z=0f;
    private double size=Maze.Size;
    private final double height=0.5;
    
    
    /*testing comment, the following three lines are the material parameters for ambient, diffuse and spot light*/
    float[] mat_amb = { 1.0f,  1.0f,  1.0f, 1.0f,};  
	 float[] mat_diff = {1.0f, 1.0f, 1.0f, 1.0f,};  
	 float[] mat_spec = {1.0f, 1.0f, 1.0f, 1.0f,};  
    
   float[] amb = { 1.0f, 1.0f, 1.0f, 1.0f, };  
   float[] diff = { 1.0f, 1.0f, 1.0f, 1.0f, };  
   float[] spec = { 1.0f, 1.0f, 1.0f, 1.0f, };  
//   float[] pos = { 0f, 0f, 2f, 1.0f, };  
//   float[] spot_dir = { 1.0f, 1.0f, 0.0f, };  
	 
   private Context 	context;

   /** Constructor to set the handed over context */
   public MyGLRenderer(Context context) {
   	this.context = context;
   }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
//        mTriangle = new Triangle();
//        mSquare = new Square();
        mMaze = new Maze();
		mMaze.setData(20,20);
		mMaze.creatMaze();
		
			// Load the texture for the square
			mMaze.loadGLTexture(gl, this.context);
			
			gl.glEnable(GL10.GL_TEXTURE_2D);			//Enable Texture Mapping ( NEW )
			gl.glShadeModel(GL10.GL_SMOOTH); 			//Enable Smooth Shading
			gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); 	//Black Background
			gl.glClearDepthf(1.0f); 					//Depth Buffer Setup
//			gl.glEnable(GL10.GL_DEPTH_TEST); 			//Enables Depth Testing
			gl.glDepthFunc(GL10.GL_LEQUAL); 			//The Type Of Depth Testing To Do
			
			//Really Nice Perspective Calculations
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST); 	
    }

    @Override
    public void onDrawFrame(GL10 gl) {
    	
        // Draw background color
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        gl.glEnable(GL10.GL_DEPTH_TEST); 
//        gl.glEnable(GL10.GL_CULL_FACE);  
          
//        gl.glEnable(GL10.GL_LIGHTING);  
//        gl.glEnable(GL10.GL_LIGHT0);  
        ByteBuffer abb  
        = ByteBuffer.allocateDirect(amb.length*4);  
        abb.order(ByteOrder.nativeOrder());  
        FloatBuffer ambBuf = abb.asFloatBuffer();  
        ambBuf.put(amb);  
        ambBuf.position(0);  
          
        ByteBuffer dbb  
        = ByteBuffer.allocateDirect(diff.length*4);  
        dbb.order(ByteOrder.nativeOrder());  
        FloatBuffer diffBuf = dbb.asFloatBuffer();  
        diffBuf.put(diff);  
        diffBuf.position(0);  
          
        ByteBuffer sbb  
        = ByteBuffer.allocateDirect(spec.length*4);  
        sbb.order(ByteOrder.nativeOrder());  
        FloatBuffer specBuf = sbb.asFloatBuffer();  
        specBuf.put(spec);  
        specBuf.position(0);  
          
//        ByteBuffer pbb  
//        = ByteBuffer.allocateDirect(pos.length*4);  
//        pbb.order(ByteOrder.nativeOrder());  
//        FloatBuffer posBuf = pbb.asFloatBuffer();  
//        posBuf.put(pos);  
//        posBuf.position(0);  
          
//        ByteBuffer spbb  
//        = ByteBuffer.allocateDirect(spot_dir.length*4);  
//        spbb.order(ByteOrder.nativeOrder());  
//        FloatBuffer spot_dirBuf = spbb.asFloatBuffer();  
//        spot_dirBuf.put(spot_dir);  
//        spot_dirBuf.position(0);  
           
      float[] pos = { (float)(x+(0.5*size)), (float)(y+(0.5*size)), (float)(z+1.2*size), 1.0f };  
//    float[] pos = { 0f,0f,1f, 1f };
//        float[] pos = { x,y,z+(float)(height*size), 1.0f };  
  float[] spot_dir = { (float) Math.cos(-mAngle/180.0f*Math.PI), (float) Math.sin(-mAngle/180.0f*Math.PI), 0.0f };  
//  float[] spot_dir = { 1f, 0f, 0f }; 
    
    gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, ambBuf);  
    gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, diffBuf);  
    gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, specBuf);  
    gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, pos,0);  
    gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPOT_DIRECTION,  
    spot_dir,0);  
    gl.glLightf(GL10.GL_LIGHT0, GL10.GL_SPOT_EXPONENT, 128);  
    gl.glLightf(GL10.GL_LIGHT0, GL10.GL_SPOT_CUTOFF, 90.0f);
        		   
        		 ByteBuffer mabb  
        		 = ByteBuffer.allocateDirect(mat_amb.length*4);  
        		 mabb.order(ByteOrder.nativeOrder());  
        		 FloatBuffer mat_ambBuf = mabb.asFloatBuffer();  
        		 mat_ambBuf.put(mat_amb);  
        		 mat_ambBuf.position(0);  
        		   
        		 ByteBuffer mdbb  
        		 = ByteBuffer.allocateDirect(mat_diff.length*4);  
        		 mdbb.order(ByteOrder.nativeOrder());  
        		 FloatBuffer mat_diffBuf = mdbb.asFloatBuffer();  
        		 mat_diffBuf.put(mat_diff);  
        		 mat_diffBuf.position(0);  
        		   
        		 ByteBuffer msbb  
        		 = ByteBuffer.allocateDirect(mat_spec.length*4);  
        		 msbb.order(ByteOrder.nativeOrder());  
        		 FloatBuffer mat_specBuf = msbb.asFloatBuffer();  
        		 mat_specBuf.put(mat_spec);  
        		 mat_specBuf.position(0);  
        		   
        		 gl.glMaterialfv(GL10.GL_FRONT_AND_BACK,  
        		 GL10.GL_AMBIENT, mat_ambBuf);  
        		 gl.glMaterialfv(GL10.GL_FRONT_AND_BACK,  
        		 GL10.GL_DIFFUSE, mat_diffBuf);  
        		 gl.glMaterialfv(GL10.GL_FRONT_AND_BACK,  
        		 GL10.GL_SPECULAR, mat_specBuf);  
        		 gl.glMaterialf(GL10.GL_FRONT_AND_BACK,  
        		 GL10.GL_SHININESS, 30.0f);  
        
        
        // Set GL_MODELVIEW transformation mode
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();   // reset the matrix to its default state

        // When using GL_MODELVIEW, you must set the view point
        GLU.gluLookAt(gl, 0f, 0f, 0f, 1.0f, 0f, 0f, 0.0f, 0.0f, -1.0f);
             
        // Create a rotation for the maze

        // Use the following code to generate constant rotation.
        // Leave this code out when using TouchEvents.
        // long time = SystemClock.uptimeMillis() % 4000L;
        // float angle = 0.090f * ((int) time);

        gl.glRotatef(mAngle, 0.0f, 0.0f, 1.0f);    
        gl.glTranslatef((float)(-0.5*size),(float)(-0.5*size),(float)(-height*size));
        gl.glTranslatef(-x, -y, -z);
                
        // Draw maze
        mMaze.draw(gl);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // Adjust the viewport based on geometry changes
        // such as screen rotations
        gl.glViewport(0, 0, width, height);

        // make adjustments for screen ratio
        float ratio = (float) width / height;
        gl.glMatrixMode(GL10.GL_PROJECTION);        // set matrix to projection mode
        gl.glLoadIdentity();                        // reset the matrix to its default state
//        gl.glFrustumf(-ratio, ratio, -50f, 50f, 0.1f, 50f);  // apply the projection matrix
        GLU.gluPerspective(gl, 60f, ratio, 0.01f, 500f);
    }

    /**
     * Returns the rotation angle of the triangle shape (mTriangle).
     *
     * @return - A float representing the rotation angle.
     */
    public float getAngle() {
        return mAngle;
    }

    /**
     * Sets the rotation angle of the triangle shape (mTriangle).
     */
    public void setAngle(float angle) {
        mAngle = angle;
    }
    
    public float getX(){
    	return x;
    }
    
    public float getY(){
    	return y;
    }
    
    public float getZ(){
    	return z;
    }
    
    public void setXYZ(float x,float y,float z){
    	this.x=x;
    	this.y=y;
    	this.z=z;
    }
    
    
}