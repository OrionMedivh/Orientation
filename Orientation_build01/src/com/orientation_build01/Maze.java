package com.orientation_build01;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.*;
import android.opengl.*;
    
 public class Maze
    {
  	   int Row=1;//default
  	   int Column=1;//default
  	   int[] walls;
  	   int bufferNo=0;
		static double Size=2.0; // size of the cell.

		private FloatBuffer textureBuffer;  // buffer holding the texture coordinates
		private float texture[] = {         
		        // Mapping coordinates for the vertices
		        0.0f, 1.0f,     // top left     (V2)
		        0.0f, 0.0f,     // bottom left  (V1)
		        1.0f, 1.0f,     // top right    (V4)
		        1.0f, 0.0f      // bottom right (V3)
		};


  	    // number of coordinates per vertex in this array
  	    static final int COORDS_PER_VERTEX = 3;

  	    float color[] = { 1.0f, 1.0f, 1.0f, 1.0f };
  	    static FloatBuffer[] vertexBuffer;
  	    static ShortBuffer[] drawListBuffer;
		static final short drawOrder[] = { 0, 1, 2, 1, 3, 2 }; // order to draw vertices
  	   
  	   public void setData(int x,int y)
  	   {
  	   /*get Row and Column*/
  	       Row=y;
  		    Column=x;
  		    vertexBuffer=new FloatBuffer[x*y*2];
  		    drawListBuffer=new ShortBuffer[x*y*2];
  	   }
  	 
  	   public void creatMaze()
  	   {
          DisjSets maze=new DisjSets(Row*Column);
  	     walls =new int[Row*Column];
  		  /* value/2 = the top wall status, value%2 = left one*/
  		  /*0 means no left wall, no top wall,*/
  		  /*1 means exists left wall, no top wall.*/
  		  /*2 means no left wall, exists top wall.*/
  		  /*3 means exist left and top wall.*/
  		
          for (int i=0;i<Row*Column;i++)
  		  {
  		    if (i/Column==0)
  		    {
  		       if (i%Column==0)
  			        walls[i]=0;
  			    else 
  			        walls[i]=1;
  		    }
  		    else
  		    { 
  		       if(i%Column==0)
                   walls[i]=2;
               else 
  			        walls[i]=3;
  		    }		  
  		  }

  		
  		
  		int counter=Row*Column-1;
  		
  		while(counter!=0)
  		{			 
  		/*	 System.out.println("counter"+counter); */
  			 
  			 int index= (int)(Math.random()*Column*Row);
  			 
  		/*	 System.out.println("index"+index);*/
  			 		 
  			 switch(walls[index])
  			 {
  			     case 0:
  				  break;
  				  
  				  case 1:
  				      if (maze.find(index)!=maze.find(index-1))
  				      {
  				          maze.union(maze.find(index),maze.find(index-1));
  							 counter--;
  						    walls[index]-=1;
  						/*    System.out.println("Knock down the left!"); */
  					   }
  						break;
  				  
  				  case 2:
  				      if (maze.find(index)!=maze.find(index-Column))
  						{
  				          maze.union(maze.find(index),maze.find(index-Column));
  							 counter--;
  						    walls[index]-=2;
  						/*    System.out.println("Knock down the top!"); */
  					   }
  						break;			  
  				  
  				  case 3:
  				  if (Math.random()>0.5)
  				  {	
  				      if (maze.find(index)!=maze.find(index-1))
  				      {
  				          maze.union(maze.find(index),maze.find(index-1));
  							 counter--;
  						    walls[index]-=1;
  						/*    System.out.println("Knock down the left!"); */
  					   }
  						break;
  				  }
  				  else
  				  {
  				      if (maze.find(index)!=maze.find(index-Column))
  						{
  				          maze.union(maze.find(index),maze.find(index-Column));
  							 counter--;
  						    walls[index]-=2;
  						 /*   System.out.println("Knock down the top!");*/
  					   }
  						break;	
  				  }		 
  			 }		
  		}
  		
  		initialize();
  	 }
  	   
  	   private void initialize(){
		 
			double offset=0.0;

			for(int i=0;i<Row*Column;i++) 
			{
				double x=Size*(i/Column)+offset;
				double y= Size*(i%Column)+offset;
				switch(walls[i]) 
				{
			     case 0:
				 break;
		  		  /* value/2 = the top wall status, value%2 = left one*/
		  		  /*0 means no left wall, no top wall,*/
		  		  /*1 means exists left wall, no top wall.*/
		  		  /*2 means no left wall, exists top wall.*/
		  		  /*3 means exist left and top wall.*/
				 
				 case 1:  //draw left wall
//					 canvas.drawLine(x,y,x+Size,y, mPaint);
					 initializeBuffer((float)x,(float)y,(float)0,(float)(x+Size),(float)y,(float)0,bufferNo);
					 bufferNo++;
				 break;
				 
				 case 2: //draw top wall
//					 canvas.drawLine(x,y,x,y+Size, mPaint);
					 initializeBuffer((float)x,(float)y,(float)0,(float)x,(float)(y+Size),(float)0,bufferNo);
					 bufferNo++;
					 break;
				 
				 case 3: // left and top wall
//					 canvas.drawLine(x,y,x,y+Size, mPaint);
					 initializeBuffer((float)x,(float)y,(float)0,(float)x,(float)(y+Size),(float)0,bufferNo);
					 bufferNo++;
					 initializeBuffer((float)x,(float)y,(float)0,(float)(x+Size),(float)y,(float)0,bufferNo);
					 bufferNo++;
					 break;
				}
 
			}
			
			//draw rectangle outline, leave an entrance and exit.
			double top=0+offset,
					left=0+offset,
					bottom=Size*Row+offset,
					right=Size*Column+offset;
//			canvas.drawLine(top,left+Size,top,right, mPaint);
			 initializeBuffer((float)top,(float)left,(float)0,(float)top,(float)right,(float)0,bufferNo);
			 bufferNo++;
//			canvas.drawLine(top+Size,left,bottom,left, mPaint);
			 initializeBuffer((float)top,(float)left,(float)0,(float)bottom,(float)left,(float)0,bufferNo);
			 bufferNo++;
//			canvas.drawLine(bottom,left,bottom,right-Size, mPaint);
			 initializeBuffer((float)bottom,(float)left,(float)0,(float)bottom,(float)(right-Size),(float)0,bufferNo);
			 bufferNo++;
//			canvas.drawLine(top,right,bottom-Size,right, mPaint);
			 initializeBuffer((float)top,(float)right,(float)0,(float)(bottom-Size),(float)right,(float)0,bufferNo);
			 bufferNo++;

  	   }
  	   
  	   private void initializeBuffer(float x1,float y1,float z1,
  			 float x2,float y2, float z2, int BufferID)
  	   {
  		    float mazeCoords[]={
  		        // in counterclockwise order:
  		        x1,y1,z1,// 
  		       x2,y2,z2,//
  		       x1,y1,(float)(z1+Size),
  		       x2,y2,(float)(z2+Size)
  		    };
  		   
  	        // initialize vertex byte buffer for shape coordinates
  	        ByteBuffer bb = ByteBuffer.allocateDirect(
  	                // (number of coordinate values * 4 bytes per float)
  	        		mazeCoords.length
  	        		 * 4);
  	        // use the device hardware's native byte order
  	        bb.order(ByteOrder.nativeOrder());

  	        // create a floating point buffer from the ByteBuffer
  	        vertexBuffer[BufferID] = bb.asFloatBuffer();
  	        // add the coordinates to the FloatBuffer
  	        vertexBuffer[BufferID].put(mazeCoords);
  	        // set the buffer to read the first coordinate
  	        vertexBuffer[BufferID].position(0);
  	        // initialize byte buffer for the draw list
  	        ByteBuffer dlb = ByteBuffer.allocateDirect(
  	                // (# of coordinate values * 2 bytes per short)
  	                drawOrder.length * 2);
  	        dlb.order(ByteOrder.nativeOrder());
  	        drawListBuffer[BufferID] = dlb.asShortBuffer();
  	        drawListBuffer[BufferID].put(drawOrder);
  	        drawListBuffer[BufferID].position(0); 
  	        
  			bb = ByteBuffer.allocateDirect(texture.length * 4);
  			bb.order(ByteOrder.nativeOrder());
  			textureBuffer = bb.asFloatBuffer();
  			textureBuffer.put(texture);
  			textureBuffer.position(0);
  	   }
  	 
     public void draw(GL10 gl) {
    	 
 		// bind the previously generated texture
 		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
 		
     // Since this shape uses vertex arrays, enable them
     gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
     // draw the shape
     gl.glColor4f(       // set color:
             color[0], color[1],
             color[2], color[3]);
     
     for (int i=0;i<bufferNo;i++){
     gl.glVertexPointer( // point to vertex data:
             COORDS_PER_VERTEX,
             GL10.GL_FLOAT, 0, vertexBuffer[i]);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
     
//     gl.glDrawArrays(    // draw shape:
//             GL10.GL_LINES, 0,
//             6 / COORDS_PER_VERTEX);   
     
     gl.glDrawElements(  // draw shape:
             GL10.GL_TRIANGLES,
             drawOrder.length, GL10.GL_UNSIGNED_SHORT,
             drawListBuffer[i]);
     }
     // Disable vertex array drawing to avoid
     // conflicts with shapes that don't use it
     gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
 }
  	   
  	   public int getwalls(int x)
  	   {
  	     return walls[x];
  	   }
  	 
  	   public int getRow()
  	   {
  	     return Row;
  	   }
  	 
  	   public int getColumn()
  	   {
  	     return Column;
  	   }	
  	   
  	   class DisjSets
  	   {
  	      /** Construct the disjoint sets object.
  	       * @param numElements the initial number of disjoint sets.
  	       */
  	      public DisjSets( int numElements )
  	      {
  	          s = new int [ numElements ];
  	          for( int i = 0; i < s.length; i++ )
  	              s[ i ] = -1;
  	      }

  	      /**   Union two disjoint sets.  
  	       *     Assume root1 and root2 are distinct and represent set names.
  	       * @param root1 the root of set 1.
  	       * @param root2 the root of set 2.          */
  	      public void union( int root1, int root2 )
  	      {
  	          s[ root2 ] = root1;
  	      }
  			  
  			  /** Perform a find.   Error checks omitted again for simplicity.
  	       * @param x the element being searched for.
  	       * @return the set containing x.         */
  	      public int find( int x )
  	      {
  	          if( s[ x ] < 0 )
  	              return x;
  	          else
  	              return find( s[ x ] );
  	      }

  	      private int [] s;
  		}
  	   
  	 /** The texture pointer */
  	 private int[] textures = new int[1];

  	 public void loadGLTexture(GL10 gl, Context context) {
  	 	// loading texture
  	 	Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
  	 			R.drawable.bricks);

  	 	// generate one texture pointer
  	 	gl.glGenTextures(1, textures, 0);
  	 	// ...and bind it to our array
  	 	gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
  	 	
  	 	// create nearest filtered texture
  	 	gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
  	 	gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
  	 	
  	 	// Use Android GLUtils to specify a two-dimensional texture image from our bitmap 
  	 	GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
  	 	
  	 	// Clean up
  	 	bitmap.recycle();
  	 }

  	 }

