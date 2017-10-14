package Andice.dice.view;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
//import java.nio.ByteBuffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
//import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;


import Andice.dice.view.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLUtils;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;

public class GLRender implements Renderer   
{   
	boolean key = true;   
    float xrot = 0.0f;   
    float yrot = 0.0f;   
    float xrot2 = 0.0f;   
    float yrot2 = 0.0f; 
    float oldxrot=0;
    float oldyrot=0;
    float xspeed, yspeed;   
    float z = -5.0f;   
    float dt=1f;
    float friction=0.5f;
    float xacc;
    float yacc;
    float zacc;
    float yangle;
    float zangle;
    float gravity = -1f;
    float ground = -10;
    cube DICE=new cube(); 
    cube DICE2=new cube();
    cube DICE3=new cube();
    private Context context;
    IntBuffer textureBuffer = IntBuffer.allocate(48);   //若要新增內建圖片增加此加數  
    FloatBuffer lightAmbient = FloatBuffer.wrap(new float[]{1.0f,1.0f,1.0f,1.0f});    
    FloatBuffer lightDiffuse = FloatBuffer.wrap(new float[]{1.0f,1.0f,1.0f,1.0f});    
    FloatBuffer lightPosition = FloatBuffer.wrap(new float[]{0.0f,0.0f,2.0f,1.0f}); 
    Matrix MATRIX=new Matrix(1,0,0,0,1,0,0,0,1);
       
    int [] texture;
    int t0 = 0,t1 = 1,t2 = 2,t3 = 3,t4 = 4,t5 = 5;
    int t02 = 0,t12 = 1,t22 = 2,t32 = 3,t42 = 4,t52 = 5;
    int t03 = 0,t13 = 1,t23 = 2,t33 = 3,t43 = 4,t53 = 5;
    int scar = 0;
    int i =0;
    int d1 = 1,d2 = 1,d3 = 1;
    int mode = 0;
    
    ByteBuffer indices1=ByteBuffer.allocateDirect(4);  
    ByteBuffer indices2=ByteBuffer.allocateDirect(4);
    ByteBuffer indices3=ByteBuffer.allocateDirect(4);
    ByteBuffer indices4=ByteBuffer.allocateDirect(4);
    ByteBuffer indices5=ByteBuffer.allocateDirect(4);
    ByteBuffer indices6=ByteBuffer.allocateDirect(4);
    
    
    public GLRender(Context context){  
  	    this.context = context;
  	    

      indices1 = ByteBuffer.wrap(new byte[]{   

      		0,1,3,2,   

      });   
      indices2 = ByteBuffer.wrap(new byte[]{   
   
              4,5,7,6,   

      });   
      indices3 = ByteBuffer.wrap(new byte[]{   
  
              8,9,11,10,   
 
      });   
      indices4 = ByteBuffer.wrap(new byte[]{   
 
              12,13,15,14,   
  
      });   
      indices5 = ByteBuffer.wrap(new byte[]{   

              16,17,19,18,   

      });   
      indices6 = ByteBuffer.wrap(new byte[]{   

              20,21,23,22,   
      });
  	    
  	  }
    

    @Override  
    public void onDrawFrame(GL10 gl) 
   
    {   
		xrot2=0;
		yrot2=0;
    	if(xrot!=0||yrot!=0){
    		if(xrot!=oldxrot||yrot!=oldyrot){
    			xrot2=xrot;
    			yrot2=yrot;
    			oldxrot=xrot;
    			oldyrot=yrot;
    		}
    	}
    	
    	
    	gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);     
        gl.glMatrixMode(GL10.GL_MODELVIEW); 
        
         
        gl.glEnable(GL10.GL_LIGHTING);   
        ////////////////   

           

        gl.glLoadIdentity();  
        //DICE.center[0]+=(DICE.MASS.force.x/DICE.MASS.m)*dt;
        if(d1 == 1)
         move(DICE,xacc,yacc,0);
        move(DICE,-xrot2,yrot2,0);
        if(d2 == 1)
         move(DICE2,xacc,yacc,0);
        move(DICE2,-xrot2,yrot2,0);
        if(d3 == 1)
         move(DICE3,xacc,yacc,0);
        move(DICE3,-xrot2,yrot2,0);
        if(d1 == 1 && d2 == 1)
         collision(DICE,DICE2);
        if(d1 == 1 && d3 == 1)
         collision(DICE,DICE3);
        if(d2 == 1 && d3 == 1)
         collision(DICE2,DICE3);
        
        if(d1 == 1)
         Friction(DICE);
        if(d2 == 1)
         Friction(DICE2);
        if(d3 == 1)
         Friction(DICE3);
         

        //collision(DICE,DICE2);
        //DrawCube(gl);  
       
        if(d1 == 1)
        collisionwall(DICE);
        if(d2 == 1)
        collisionwall(DICE2);
        if(d3 == 1)
        collisionwall(DICE3);
        
        if(d1 == 1)
        collisionground(DICE);
        if(d2 == 1)
        collisionground(DICE2);
        if(d3 == 1)
        collisionground(DICE3);

        if(d1==1){
        
        DICE.center[0] += DICE.MASS.force.x / DICE.MASS.m;
		DICE.center[1] += DICE.MASS.force.y / DICE.MASS.m;
		DICE.center[2] += DICE.MASS.force.z / DICE.MASS.m;
		
		Quater(DICE, DICE.MASS.force.x / DICE.MASS.I, -DICE.MASS.force.y / DICE.MASS.I);
        }	
		
        if(d2==1){
    	DICE2.center[0] += DICE2.MASS.force.x / DICE2.MASS.m;
    	DICE2.center[1] += DICE2.MASS.force.y / DICE2.MASS.m;
    	DICE2.center[2] += DICE2.MASS.force.z / DICE2.MASS.m;

		Quater(DICE2, DICE2.MASS.force.x / DICE2.MASS.I, -DICE2.MASS.force.y / DICE2.MASS.I);
        }
        
        if(d3==1){
		DICE3.center[0] += DICE3.MASS.force.x / DICE3.MASS.m;
    	DICE3.center[1] += DICE3.MASS.force.y / DICE3.MASS.m;
    	

		Quater(DICE3, DICE3.MASS.force.x / DICE3.MASS.I, -DICE3.MASS.force.y / DICE3.MASS.I);
        }
        
        if(d1==1){
		flat(DICE);
        }
        
        if(d2==1){
    	flat(DICE2);
        }
        
        if(d3==1){
    	flat(DICE3);
        }
     	
		if(d1 == 1)
    	DrawCube(gl, DICE, DICE.center[0], DICE.center[1], DICE.center[2],t0,t1,t2,t3 ,t4,t5);
		if(d2 == 1)
    	DrawCube(gl, DICE2, DICE2.center[0], DICE2.center[1], DICE2.center[2],t02,t12,t22,t32,t42,t52);
		if(d3 == 1)
    	DrawCube(gl, DICE3, DICE3.center[0], DICE3.center[1], DICE3.center[2],t03,t13,t23,t33,t43,t53);
 
           

        if (key)   
        {   
            gl.glEnable(GL10.GL_BLEND);     // 
            gl.glDisable(GL10.GL_DEPTH_TEST);   // 
        }   
        else    
        {   
            gl.glDisable(GL10.GL_BLEND);        //  
            gl.glEnable(GL10.GL_DEPTH_TEST);    // 
        }  
    }   
  
    @Override  
    public void onSurfaceChanged(GL10 gl, int width, int height)   
    {   
        float ratio = (float) width / height;   
   
        gl.glViewport(0, 0, width, height);   
  
        gl.glMatrixMode(GL10.GL_PROJECTION);   
   
        gl.glLoadIdentity();   
 
        // gl.glFrustumf(-ratio, ratio, -1, 1, 1, 50.0f);   
        gl.glOrthof(-7*ratio, 7*ratio, -7, 7,1,50f);
        //gl.gluPerpective(fovy, aspect, near, far)

        gl.glMatrixMode(GL10.GL_MODELVIEW);    

        gl.glLoadIdentity();   
    }  
  
    @Override  
    public void onSurfaceCreated(GL10 gl, EGLConfig config)   
    {   
        gl.glDisable(GL10.GL_DITHER);  
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);   
        // 黑色背景   
        gl.glClearColor(0, 0, 0, 0);   
        gl.glEnable(GL10.GL_CULL_FACE);   
        gl.glShadeModel(GL10.GL_SMOOTH);   
        gl.glEnable(GL10.GL_DEPTH_TEST);   
        gl.glColor4f(1.0f,1.0f,1.0f,0.5f);     
        gl.glBlendFunc(GL10.GL_SRC_ALPHA,GL10.GL_ONE);   
        gl.glClearDepthf(1.0f);   
        gl.glDepthFunc(GL10.GL_LEQUAL);   
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);   
        LoadTexture(gl,context); 
        gl.glEnable(GL10.GL_TEXTURE_2D);   
        
        DICE.center=new float[]{
        		0,0.5f*z,1.5f*z
        		
        };
        DICE2.center=new float[]{
        		0, -0.5f*z, 1.5f*z
       		
       };
        
        DICE3.center=new float[]{
        		0,0,1.5f*z
        		
        };
  
        gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_AMBIENT, lightAmbient);   

        gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_DIFFUSE, lightDiffuse);   
  

        gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_POSITION, lightPosition);   
           
  
        gl.glEnable(GL10.GL_LIGHT1);   
           

        gl.glEnable(GL10.GL_BLEND);     
    }   
    
	private void DrawCube(GL10 gl, cube dice, float xcenter, float ycenter, float zcenter,int f1,int f2,int f3,int f4,int f5,int f6){
		//FloatBuffer TM=FloatBuffer.wrap(DICE.TM);

        gl.glPushMatrix();
        gl.glTranslatef(xcenter,ycenter,zcenter);  
       /*double xrot2=xrot*Math.PI/180;
       double yrot2=yrot*Math.PI/180;      
       double zrot2=0
       
       Quater(DICE,xrot2,yrot2);//加上新角度*/
        
       if (mode == 2 || mode == 3)
       glrotate(gl,DICE);//顯示目前cube存的角度
       else
       glrotate(gl,dice);


        //Log.v("c"," x0:"+q3.x0+" math:"+Math.cos(xrot / 2));
        
    //    double nor=q3.norm();
        
        

 
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);   
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);   
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);   
          if(scar == 1){
        	  LoadTexture(gl,context);
        	  scar = 0;
          }		
        dice.draw(gl);
       /* gl.glNormalPointer(GL10.GL_FLOAT, 0, dice.normals);   
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, dice.vertices);   
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, dice.texCoords);   */     
               

    
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[f1]);
      //  dice.drawtexture(gl);
        
        gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4,  GL10.GL_UNSIGNED_BYTE, indices1); 
   
         
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[f2]);   
        gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4,  GL10.GL_UNSIGNED_BYTE, indices2);   
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[f3]);   
        gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4,  GL10.GL_UNSIGNED_BYTE, indices3);   
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[f4]);   
        gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4,  GL10.GL_UNSIGNED_BYTE, indices4);   
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[f5]);   
        gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4,  GL10.GL_UNSIGNED_BYTE, indices5);    
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[f6]);
        gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4,  GL10.GL_UNSIGNED_BYTE, indices6);
        
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);   
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);   
        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);   
        //xrot=0;
        //yrot=0;
        gl.glPopMatrix();
	}
    
	
	
    @SuppressWarnings("null")
	private void LoadTexture(GL10 gl, Context cont){  

        gl.glGenTextures(48, textureBuffer);   //若要新增內建圖片增加
        texture = textureBuffer.array();  
        Bitmap mBitmap1;   
        Bitmap mBitmap2;   
        Bitmap mBitmap3;   
        Bitmap mBitmap4;   
        Bitmap mBitmap5;   
        Bitmap mBitmap6;
        Bitmap mBitmap7;   
        Bitmap mBitmap8;   
        Bitmap mBitmap9;   
        Bitmap mBitmap10;   
        Bitmap mBitmap11;   
        Bitmap mBitmap12;
        
       // mBitmap1 = BitmapFactory.decodeResource(cont.getResources(), R.drawable.a1);   
        
        InputStream is = cont.getResources().openRawResource(R.drawable.a1);
        
        mBitmap1 = BitmapFactory.decodeStream(is);

        InputStream is2 = cont.getResources().openRawResource(R.drawable.a2);
        
        mBitmap2 = BitmapFactory.decodeStream(is2);
   
        InputStream is3 = cont.getResources().openRawResource(R.drawable.a3);
        
        mBitmap3 = BitmapFactory.decodeStream(is3);
        InputStream is4 = cont.getResources().openRawResource(R.drawable.a4);
        
        mBitmap4 = BitmapFactory.decodeStream(is4);
        
        InputStream is5 = cont.getResources().openRawResource(R.drawable.a5);
        
        mBitmap5 = BitmapFactory.decodeStream(is5);
        
        InputStream is6 = cont.getResources().openRawResource(R.drawable.a6);
        
        mBitmap6 = BitmapFactory.decodeStream(is6);
        
        InputStream is7 = cont.getResources().openRawResource(R.drawable.b1);
        
        mBitmap7 = BitmapFactory.decodeStream(is7);
        InputStream is8 = cont.getResources().openRawResource(R.drawable.b2);
        
        mBitmap8 = BitmapFactory.decodeStream(is8);
        
        InputStream is9 = cont.getResources().openRawResource(R.drawable.b3);
        
        mBitmap9 = BitmapFactory.decodeStream(is9);
        InputStream is10 = cont.getResources().openRawResource(R.drawable.b4);
        
        mBitmap10 = BitmapFactory.decodeStream(is10);
        InputStream is11 = cont.getResources().openRawResource(R.drawable.b5);
        
        mBitmap11 = BitmapFactory.decodeStream(is11);
        InputStream is12 = cont.getResources().openRawResource(R.drawable.b6);
        
        mBitmap12 = BitmapFactory.decodeStream(is12);

      /*  
        mBitmap2 = BitmapFactory.decodeResource(cont.getResources(), R.drawable.a2);   
        mBitmap3 = BitmapFactory.decodeResource(cont.getResources(), R.drawable.a3);   
        mBitmap4 = BitmapFactory.decodeResource(cont.getResources(), R.drawable.a4);   
        mBitmap5 = BitmapFactory.decodeResource(cont.getResources(), R.drawable.a5);   
        mBitmap6 = BitmapFactory.decodeResource(cont.getResources(), R.drawable.a6);
        
        mBitmap7 = BitmapFactory.decodeResource(cont.getResources(), R.drawable.b1);   
        mBitmap8 = BitmapFactory.decodeResource(cont.getResources(), R.drawable.b2);   
        mBitmap9 = BitmapFactory.decodeResource(cont.getResources(), R.drawable.b3);   
        mBitmap10 = BitmapFactory.decodeResource(cont.getResources(), R.drawable.b4);   
        mBitmap11 = BitmapFactory.decodeResource(cont.getResources(), R.drawable.b5);   
        mBitmap12 = BitmapFactory.decodeResource(cont.getResources(), R.drawable.b6);

*/
	    gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[0]);  
	    gl.glTexParameterf(GL10.GL_TEXTURE_2D,   
	                       GL10.GL_TEXTURE_MAG_FILTER,   
	                       GL10.GL_LINEAR);  
	    gl.glTexParameterf(GL10.GL_TEXTURE_2D,  
	                       GL10.GL_TEXTURE_MIN_FILTER,  
	                       GL10.GL_LINEAR);
	    

	    GLUtils.texImage2D( GL10.GL_TEXTURE_2D, 0, mBitmap1, 0 );  
	    
	    gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[1]);  
	    
	   // Log.e("1texture",""+texture[1]);
	    
	    gl.glTexParameterf(GL10.GL_TEXTURE_2D,   
	                       GL10.GL_TEXTURE_MAG_FILTER,   
	                       GL10.GL_LINEAR);  
	    gl.glTexParameterf(GL10.GL_TEXTURE_2D,  
	                       GL10.GL_TEXTURE_MIN_FILTER,  
	                       GL10.GL_LINEAR);
	    GLUtils.texImage2D( GL10.GL_TEXTURE_2D, 0, mBitmap2, 0 );   
	    
	    gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[2]);  
	    gl.glTexParameterf(GL10.GL_TEXTURE_2D,   
	                       GL10.GL_TEXTURE_MAG_FILTER,   
	                       GL10.GL_LINEAR);  
	    gl.glTexParameterf(GL10.GL_TEXTURE_2D,  
	                       GL10.GL_TEXTURE_MIN_FILTER,  
	                       GL10.GL_LINEAR);
	    GLUtils.texImage2D( GL10.GL_TEXTURE_2D, 0, mBitmap3, 0 );   
	    
	    gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[3]);  
	    gl.glTexParameterf(GL10.GL_TEXTURE_2D,   
	                       GL10.GL_TEXTURE_MAG_FILTER,   
	                       GL10.GL_LINEAR);  
	    gl.glTexParameterf(GL10.GL_TEXTURE_2D,  
	                       GL10.GL_TEXTURE_MIN_FILTER,  
	                       GL10.GL_LINEAR);
	    GLUtils.texImage2D( GL10.GL_TEXTURE_2D, 0, mBitmap4, 0 );   
	    
	    gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[4]);  
	    gl.glTexParameterf(GL10.GL_TEXTURE_2D,   
	                       GL10.GL_TEXTURE_MAG_FILTER,   
	                       GL10.GL_LINEAR);  
	    gl.glTexParameterf(GL10.GL_TEXTURE_2D,  
	                       GL10.GL_TEXTURE_MIN_FILTER,  
	                       GL10.GL_LINEAR);
	    GLUtils.texImage2D( GL10.GL_TEXTURE_2D, 0, mBitmap5, 0 );   
	    
	    gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[5]);  
	    gl.glTexParameterf(GL10.GL_TEXTURE_2D,   
	                       GL10.GL_TEXTURE_MAG_FILTER,   
	                       GL10.GL_LINEAR);  
	    gl.glTexParameterf(GL10.GL_TEXTURE_2D,  
	                       GL10.GL_TEXTURE_MIN_FILTER,  
	                       GL10.GL_LINEAR);
	    GLUtils.texImage2D( GL10.GL_TEXTURE_2D, 0, mBitmap6, 0 );
	    
	    gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[6]);  
	    gl.glTexParameterf(GL10.GL_TEXTURE_2D,   
	                       GL10.GL_TEXTURE_MAG_FILTER,   
	                       GL10.GL_LINEAR);  
	    gl.glTexParameterf(GL10.GL_TEXTURE_2D,  
	                       GL10.GL_TEXTURE_MIN_FILTER,  
	                       GL10.GL_LINEAR);
	    GLUtils.texImage2D( GL10.GL_TEXTURE_2D, 0, mBitmap7, 0 );  
	    
	    gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[7]);  
	    gl.glTexParameterf(GL10.GL_TEXTURE_2D,   
	                       GL10.GL_TEXTURE_MAG_FILTER,   
	                       GL10.GL_LINEAR);  
	    gl.glTexParameterf(GL10.GL_TEXTURE_2D,  
	                       GL10.GL_TEXTURE_MIN_FILTER,  
	                       GL10.GL_LINEAR);
	    GLUtils.texImage2D( GL10.GL_TEXTURE_2D, 0, mBitmap8, 0 );   
	    
	    gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[8]);  
	    gl.glTexParameterf(GL10.GL_TEXTURE_2D,   
	                       GL10.GL_TEXTURE_MAG_FILTER,   
	                       GL10.GL_LINEAR);  
	    gl.glTexParameterf(GL10.GL_TEXTURE_2D,  
	                       GL10.GL_TEXTURE_MIN_FILTER,  
	                       GL10.GL_LINEAR);
	    GLUtils.texImage2D( GL10.GL_TEXTURE_2D, 0, mBitmap9, 0 );   
	    
	    gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[9]);  
	    gl.glTexParameterf(GL10.GL_TEXTURE_2D,   
	                       GL10.GL_TEXTURE_MAG_FILTER,   
	                       GL10.GL_LINEAR);  
	    gl.glTexParameterf(GL10.GL_TEXTURE_2D,  
	                       GL10.GL_TEXTURE_MIN_FILTER,  
	                       GL10.GL_LINEAR);
	    GLUtils.texImage2D( GL10.GL_TEXTURE_2D, 0, mBitmap10, 0 );   
	    
	    gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[10]);  
	    gl.glTexParameterf(GL10.GL_TEXTURE_2D,   
	                       GL10.GL_TEXTURE_MAG_FILTER,   
	                       GL10.GL_LINEAR);  
	    gl.glTexParameterf(GL10.GL_TEXTURE_2D,  
	                       GL10.GL_TEXTURE_MIN_FILTER,  
	                       GL10.GL_LINEAR);
	    GLUtils.texImage2D( GL10.GL_TEXTURE_2D, 0, mBitmap11, 0 );   
	    
	    gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[11]);  
	    gl.glTexParameterf(GL10.GL_TEXTURE_2D,   
	                       GL10.GL_TEXTURE_MAG_FILTER,   
	                       GL10.GL_LINEAR);  
	    gl.glTexParameterf(GL10.GL_TEXTURE_2D,  
	                       GL10.GL_TEXTURE_MIN_FILTER,  
	                       GL10.GL_LINEAR);
	    GLUtils.texImage2D( GL10.GL_TEXTURE_2D, 0, mBitmap12, 0 );

	    InputStream gaga = null; 
/*	    
	     try{
	          URLConnection connection = new URL("file://"+Environment.getExternalStorageDirectory()+"/dicepic/tmp.jpg").openConnection();
	        gaga = connection.getInputStream();   
	        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[6]);//若要新增內建圖片增加此加數  
		    gl.glTexParameterf(GL10.GL_TEXTURE_2D,   
		                       GL10.GL_TEXTURE_MAG_FILTER,   
		                       GL10.GL_LINEAR);  
		    gl.glTexParameterf(GL10.GL_TEXTURE_2D,  
		                       GL10.GL_TEXTURE_MIN_FILTER,  
		                       GL10.GL_LINEAR);
		    GLUtils.texImage2D( GL10.GL_TEXTURE_2D, 0, BitmapFactory.decodeStream(gaga), 0 );
	        }catch (MalformedURLException e) {	
	       	 e.printStackTrace();
	    	} catch (IOException e) {
	    	 e.printStackTrace();
	    	} 
*/
	    int j;    
	    for(j=0;j<36;j++){
        try{
        URLConnection connection = new URL("file://"+Environment.getExternalStorageDirectory()+"/dicepic/"+String.valueOf(j)+".jpg").openConnection();
        gaga = connection.getInputStream();   
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[j+12]);//若要新增內建圖片增加此加數  
	    gl.glTexParameterf(GL10.GL_TEXTURE_2D,   
	                       GL10.GL_TEXTURE_MAG_FILTER,   
	                       GL10.GL_LINEAR);  
	    gl.glTexParameterf(GL10.GL_TEXTURE_2D,  
	                       GL10.GL_TEXTURE_MIN_FILTER,  
	                       GL10.GL_LINEAR);
	    GLUtils.texImage2D( GL10.GL_TEXTURE_2D, 0, BitmapFactory.decodeStream(gaga), 0 );
        }catch (MalformedURLException e) {	
       	 e.printStackTrace();
    	} catch (IOException e) {
    	 e.printStackTrace();
    	} 
	    } 
	    
	   
	    
/////////////    	
	   // bitmap[n].recycle();     
	  }  
    
    public float[] MatrixMUL(float[] a,float[] b){
    	float[]c=new float[]{
       			a[0]*b[0]+a[1]*b[4]+a[2]*b[8]+a[3]*b[12],
       			a[0]*b[1]+a[1]*b[5]+a[2]*b[9]+a[3]*b[13],
       			a[0]*b[2]+a[1]*b[6]+a[2]*b[10]+a[3]*b[14],
       			a[0]*b[3]+a[1]*b[7]+a[2]*b[11]+a[3]*b[15],
       			a[4]*b[0]+a[5]*b[4]+a[6]*b[8]+a[7]*b[12],
       			a[4]*b[1]+a[5]*b[5]+a[6]*b[9]+a[7]*b[13],
       			a[4]*b[2]+a[5]*b[6]+a[6]*b[10]+a[7]*b[14],
       			a[4]*b[3]+a[5]*b[7]+a[6]*b[11]+a[7]*b[15],
       			a[8]*b[0]+a[9]*b[4]+a[10]*b[8]+a[11]*b[12],
       			a[8]*b[1]+a[9]*b[5]+a[10]*b[9]+a[11]*b[13],
       			a[8]*b[2]+a[9]*b[6]+a[10]*b[10]+a[11]*b[14],
       			a[8]*b[3]+a[9]*b[7]+a[10]*b[11]+a[11]*b[15],
       			a[12]*b[0]+a[13]*b[4]+a[14]*b[8]+a[15]*b[12],
       			a[12]*b[1]+a[13]*b[5]+a[14]*b[9]+a[15]*b[13],
       			a[12]*b[2]+a[13]*b[6]+a[14]*b[10]+a[15]*b[14],
       			a[12]*b[3]+a[13]*b[7]+a[14]*b[11]+a[15]*b[15],
      		};
    	
    	return c;
    
    }
    
    public float[] MatrixINV(float[] a){
    	//double[][] mtr = {{10,10,5},{10,30,15},{5,15,20}};   
    	float[] inv= new float[]{a[5]*a[10]-a[6]*a[9], a[2]*a[9]-a[1]*a[10],  a[1]*a[6]-a[2]*a[5],0,
    			                a[6]*a[8]-a[4]*a[10],  a[0]*a[10]-a[2]*a[8],  a[2]*a[4]-a[0]*a[6],0,
    			                a[4]*a[9]-a[5]*a[8],   a[1]*a[8]-a[0]*a[9],   a[0]*a[5]-a[1]*a[4],0,
    		                    0,0,0,1};   

    	
    	
        return inv;
    }
       
    public boolean onKeyUp(int keyCode, KeyEvent event)   
    {   
        key = !key;   
        return false;   
    }   
    
    void Quater(cube A,double xrot2,double yrot2){
        Quaternion q=new Quaternion(Math.cos(xrot2 / 2),0,1*Math.sin(xrot2/ 2),0);
        Quaternion q2=new Quaternion(Math.cos(yrot2  / 2),1*Math.sin(yrot2  / 2),0,0);
        Quaternion q3=q2.times(q);
        //A.Q=q3;
        if(A.ifQ==1){
            q3=q3.times(A.Q);
            A.Q=q3;
        }
        else if(xrot2>0||yrot2>0){
        	A.Q=q3;
        	A.ifQ=1;
        }
        
    }
    
    void Quater2(cube A,double rot,double x,double y, double z){
    	Quaternion q=new Quaternion(Math.cos(rot/ 2),
    			                    Math.sin(rot/ 2)*x,
    			                    Math.sin(rot/ 2)*y,
    			                    Math.sin(rot/ 2)*z
    	);
        if(A.ifQ==1){
            q=q.times(A.Q);
            A.Q=q;
        }
       /* else if(rot>0){
        	A.Q=q;
        	A.ifQ=1;
        }*/
    }
    
    void glrotate(GL10 gl,cube A){
        if(A.ifQ!=0){
        double theta = 2 * Math.acos(A.Q.x0);        
        double x = A.Q.x1 / (Math.sin(theta / 2));
        double y = A.Q.x2 / (Math.sin(theta / 2));
        double z = A.Q.x3 / (Math.sin(theta / 2));
        
        double thetaa=theta*360/(2*Math.PI);
 			//Log.v("c","x:"+x+" y:"+y+" z:"+z+" angle"+theta);
 			
 	
 			

           gl.glRotatef((float)thetaa,(float) x, (float)y,(float) z);

        }
    }
    
    void collisionwall (cube A) {
    	if(A.center[0]>4f||A.center[0]<-4f){
    		if(A.MASS.force.x*A.center[0]>0){
	    	  A.MASS.force.x=-1f*(A.MASS.force.x);
	    	  //A.ifG=0;
	    	  //A.MASS.torque.x=-1f*(A.MASS.torque.x);
    		}
    		
    	}
    /*	else if((5f+A.center[0])*(5f+A.center[0])<A.collision){
    		A.MASS.force.x=-1*(A.MASS.force.x);
    	}
    	*/
    	if(A.center[1]>6f||A.center[1]<-6f){
    		if(A.MASS.force.y*A.center[1]>0){
	    	A.MASS.force.y=-1f*(A.MASS.force.y);
	    	  //A.ifG=0;
	    	//A.MASS.torque.y=-1f*(A.MASS.torque.y);
    		}
    	}/*
    	else if((10f+A.center[1])*(10f+A.center[1])<A.collision){
	    	A.MASS.force.y=-1*(A.MASS.force.y);
    	}*/
    }
    
    void move(cube A,float xacc,float yacc,float zacc){
        	
    	    if (xacc > 0.001){
        		//DICE2.center[0] -= xacc*(float)0.1;
        		A.MASS.force.x -= (float)(xacc - 0.1);
        		A.MASS.torque.x -= (float)(xacc - 0.1);
        		A.ifF = 0;
        	}
        	if (yacc > 0.001){
        		//DICE2.center[1] -= yacc*(float)0.1;
        		A.MASS.force.y -= (float)(yacc - 0.1);
        		A.MASS.torque.y -= (float)(yacc - 0.1);
        		A.ifF = 0;
    		}
        	/*if (zacc > 0.001){
        		A.MASS.force.z -= 2000000000*(float)(zacc + 0.001);
        	}*/
        	
        	if (xacc < -0.001){
        		//DICE2.center[0] -= xacc*(float)0.1;
        		A.MASS.force.x -= (float)(xacc + 0.1);
        		A.MASS.torque.x -= (float)(xacc + 0.1);
        		A.ifF = 0;
        	}
        	if (yacc < -0.001){
        		//DICE2.center[1] -= yacc*(float)0.1;
        		A.MASS.force.y -= (float)(yacc + 0.1);
        		A.MASS.torque.x -= (float)(xacc + 0.1);
        		A.ifF = 0;
        	}
        	
        	/*if (zacc < -0.001){
        		A.MASS.force.z -= 2000000000*(float)(zacc + 0.001);
        	}*/
        	/*if (zacc > 0.1 || zacc < -0.1)
        		DICE2.center[2] -= zacc*(float)0.01;*/
    }
    
    void Friction(cube A){
        if(A.MASS.force.x<0){
        	if(A.MASS.force.x-friction*(A.MASS.force.x/(Math.abs(A.MASS.force.x)+Math.abs(A.MASS.force.y)))>0){
        		A.MASS.force.x=0;
        	}
        	else{
        		A.MASS.force.x=A.MASS.force.x-friction*(A.MASS.force.x/(Math.abs(A.MASS.force.x)+Math.abs(A.MASS.force.y)));
        	}
        }else if(A.MASS.force.x>0){
        	if(A.MASS.force.x-friction*(A.MASS.force.x/(Math.abs(A.MASS.force.x)+Math.abs(A.MASS.force.y)))<0){
        		A.MASS.force.x=0;
        	}
        	else{
        		A.MASS.force.x=A.MASS.force.x-friction*(A.MASS.force.x/(Math.abs(A.MASS.force.x)+Math.abs(A.MASS.force.y)));
        	}
        }
        
        
        if(A.MASS.force.y<0){
        	if(A.MASS.force.y-friction*(A.MASS.force.y/(Math.abs(A.MASS.force.x)+Math.abs(A.MASS.force.y)))>0){
        		A.MASS.force.y=0;
        	}
        	else{
        		A.MASS.force.y=A.MASS.force.y-friction*(A.MASS.force.y/(Math.abs(A.MASS.force.x)+Math.abs(A.MASS.force.y)));
        	}
        }else if(A.MASS.force.y>0){
        	if(A.MASS.force.y-friction*(A.MASS.force.y/(Math.abs(A.MASS.force.x)+Math.abs(A.MASS.force.y)))<0){
        		A.MASS.force.y=0;
        	}
        	else{
        		A.MASS.force.y=A.MASS.force.y-friction*(A.MASS.force.y/(Math.abs(A.MASS.force.x)+Math.abs(A.MASS.force.y)));
        	}
        }
        
        if(A.MASS.torque.x<0){
        	if(A.MASS.torque.x-friction*(A.MASS.torque.x/(Math.abs(A.MASS.torque.x)+Math.abs(A.MASS.torque.y)))>0){
        		A.MASS.torque.x=0;
        	}
        	else{
        		A.MASS.torque.x=A.MASS.torque.x-friction*(A.MASS.torque.x/(Math.abs(A.MASS.torque.x)+Math.abs(A.MASS.torque.y)));
        	}
        }else if(A.MASS.torque.x>0){
        	if(A.MASS.torque.x-friction*(A.MASS.torque.x/(Math.abs(A.MASS.torque.x)+Math.abs(A.MASS.torque.y)))<0){
        		A.MASS.torque.x=0;
        	}
        	else{
        		A.MASS.torque.x=A.MASS.torque.x-friction*(A.MASS.torque.x/(Math.abs(A.MASS.torque.x)+Math.abs(A.MASS.torque.y)));
        	}
        }
        
        
        if(A.MASS.torque.y<0){
        	if(A.MASS.torque.y-friction*(A.MASS.torque.y/(Math.abs(A.MASS.torque.x)+Math.abs(A.MASS.torque.y)))>0){
        		A.MASS.torque.y=0;
        	}
        	else{
        		A.MASS.torque.y=A.MASS.torque.y-friction*(A.MASS.torque.y/(Math.abs(A.MASS.torque.x)+Math.abs(A.MASS.torque.y)));
        	}
        }else if(A.MASS.torque.y>0){
        	if(A.MASS.torque.y-friction*(A.MASS.torque.y/(Math.abs(A.MASS.torque.x)+Math.abs(A.MASS.torque.y)))<0){
        		A.MASS.torque.y=0;
        	}
        	else{
        		A.MASS.torque.y=A.MASS.torque.y-friction*(A.MASS.torque.y/(Math.abs(A.MASS.torque.x)+Math.abs(A.MASS.torque.y)));
        	}
        }
        
        /*if(A.MASS.torque.x<0){
        	if(A.MASS.torque.x-friction*(A.MASS.torque.x/(Math.abs(A.MASS.torque.x)+Math.abs(A.MASS.torque.y)))>0){
        		A.MASS.torque.x=0;
        	}
        	else{
        		A.MASS.torque.x=A.MASS.torque.x-5*friction*(A.MASS.torque.x/(Math.abs(A.MASS.torque.x)+Math.abs(A.MASS.torque.y)));
        	}
        }else if(A.MASS.torque.x>0){
        	if(A.MASS.torque.x-friction*(A.MASS.torque.x/(Math.abs(A.MASS.torque.x)+Math.abs(A.MASS.torque.y)))<0){
        		A.MASS.torque.x=0;
        	}
        	else{
        		A.MASS.torque.x=A.MASS.torque.x-5*friction*(A.MASS.torque.x/(Math.abs(A.MASS.torque.x)+Math.abs(A.MASS.torque.y)));
        	}
        }
        
        
        if(A.MASS.torque.y<0){
        	if(A.MASS.torque.y-friction*(A.MASS.torque.y/(Math.abs(A.MASS.torque.x)+Math.abs(A.MASS.torque.y)))>0){
        		A.MASS.torque.y=0;
        	}
        	else{
        		A.MASS.torque.y=A.MASS.torque.y-3*friction*(A.MASS.torque.y/(Math.abs(A.MASS.torque.x)+Math.abs(A.MASS.torque.y)));
        	}
        }else if(A.MASS.torque.y>0){
        	if(A.MASS.torque.y-friction*(A.MASS.torque.y/(Math.abs(A.MASS.torque.x)+Math.abs(A.MASS.torque.y)))<0){
        		A.MASS.torque.y=0;
        	}
        	else{
        		A.MASS.torque.y=A.MASS.torque.y-3*friction*(A.MASS.torque.y/(Math.abs(A.MASS.torque.x)+Math.abs(A.MASS.torque.y)));
        	}
        }*/
        
        
    }
    
   void collision (cube A,cube B) {
    	
	   float r=(A.center[0]-B.center[0])*(A.center[0]-B.center[0])+(A.center[1]-B.center[1])*(A.center[1]-B.center[1]);
	    if(r<=(A.collision+B.collision)*(A.collision+B.collision)){
	    	
	    	if((A.MASS.force.x-B.MASS.force.x)*(A.center[0]-B.center[0])+(A.MASS.force.y-B.MASS.force.y)*(A.center[1]-B.center[1])<0){
	    	
	    	float FB=(float)Math.sqrt(A.MASS.force.x*A.MASS.force.x+A.MASS.force.y*A.MASS.force.y);
	    	float FA=(float)Math.sqrt(B.MASS.force.x*B.MASS.force.x+B.MASS.force.y*B.MASS.force.y);
	    	A.MASS.force.x=FA*(A.center[0]-B.center[0])/(Math.abs(A.center[0]-B.center[0])+Math.abs(A.center[1]-B.center[1]));

	    	A.MASS.force.y=FA*(A.center[1]-B.center[1])/(Math.abs(A.center[0]-B.center[0])+Math.abs(A.center[1]-B.center[1]));
	    	
	    	B.MASS.force.x=FB*(B.center[0]-A.center[0])/(Math.abs(A.center[0]-B.center[0])+Math.abs(A.center[1]-B.center[1]));

	    	B.MASS.force.y=FB*(B.center[1]-A.center[1])/(Math.abs(A.center[0]-B.center[0])+Math.abs(A.center[1]-B.center[1]));
	    	
	    	float RB = (float)Math.sqrt(A.MASS.torque.x*A.MASS.torque.x+A.MASS.torque.y*A.MASS.torque.y);
	    	float RA = (float)Math.sqrt(B.MASS.torque.x*B.MASS.torque.x+B.MASS.torque.y*B.MASS.torque.y);
	    	A.MASS.torque.x=RA*(A.center[0]-B.center[0])/(Math.abs(A.center[0]-B.center[0])+Math.abs(A.center[1]-B.center[1]));

	    	A.MASS.torque.y=RA*(A.center[1]-B.center[1])/(Math.abs(A.center[0]-B.center[0])+Math.abs(A.center[1]-B.center[1]));
	    	
	    	B.MASS.torque.x=RB*(B.center[0]-A.center[0])/(Math.abs(A.center[0]-B.center[0])+Math.abs(A.center[1]-B.center[1]));

	    	B.MASS.torque.y=RB*(B.center[1]-A.center[1])/(Math.abs(A.center[0]-B.center[0])+Math.abs(A.center[1]-B.center[1]));
	    	
	    	}
	    	
	    /*	
	        double theta = 2 * Math.acos(A.Q.x0);        
	        double x = A.Q.x1 / (Math.sin(theta / 2));
	        double y = A.Q.x2 / (Math.sin(theta / 2));
	        double z = A.Q.x3 / (Math.sin(theta / 2));
	        double thetaa=theta*360/(2*Math.PI);
	    	
	           float [] RM=new float[]{
	        		   (float)(Math.cos(thetaa)+(1-Math.cos(thetaa)*x*x)),
	        		   (float)((1-Math.cos(thetaa))*x*y-Math.sin(thetaa)*z),
	        		   (float)((1-Math.cos(thetaa))*x*y+Math.sin(thetaa)*y),
	        		   
	        		   (float)((1-Math.cos(thetaa))*x*y+Math.sin(thetaa)*z),
	        		   (float)(Math.cos(thetaa)+(1-Math.cos(thetaa)*y*y)),
	        		   (float)((1-Math.cos(thetaa))*y*z-Math.sin(thetaa)*x),
	        		   
	        		   (float)((1-Math.cos(thetaa))*x*z-Math.sin(thetaa)*y),
	        		   (float)((1-Math.cos(thetaa))*y*z+Math.sin(thetaa)*x),
	        		   (float)(Math.cos(thetaa)+(1-Math.cos(thetaa))*z*z)
	        		   
	           };
	           float[] rmA = null;
	          // float[] rmB = null;
	           for(int i=0;i<8;i++){
	        	   for(int j=0;j<3;j++){

	        				rmA[i]=RM[j]*DICE.collisionvertices[i];
	        				rmA[i+1]=RM[j+3]*DICE.collisionvertices[i+1];
	        				rmA[i+2]=RM[j+6]*DICE.collisionvertices[i+2];
	        		
	        	   }
	           }
	           DICE.collisionvertices=rm;  */
	    }
    	    
    	
    	
    }

   void gravitation(cube A){

		
		A.MASS.force.z += gravity;

		if (zacc < -0.001 || zacc > 0.001){
    		//DICE2.center[0] -= xacc*(float)0.1;
    		A.MASS.force.z -= (float)(0.1*zacc);
    		//A.MASS.torque.x -= (float)(xacc + 0.001)*3f;
    	}

   }

	void collisionground(cube A){
	   if(A.ifQ==1){
	       double theta = 2 * Math.acos(A.Q.x0);        
	       float x =(float)( A.Q.x1 / (Math.sin(theta / 2)));
	       float y =(float)( A.Q.x2 / (Math.sin(theta / 2)));
	       float z =(float)( A.Q.x3 / (Math.sin(theta / 2)));
	       
	       double t=theta*360/(2*Math.PI);
	       float c=(float)Math.cos(t);
	       float s=(float)Math.sin(t);
	       
	       float [][] RM=new float[][]{
	       	{c+(1-c)*x*x,(1-c)*x*y-s*z,(1-c)*x*z+s*y},
	       	{(1-c)*y*x+s*z,c+(1-c)*y*y,(1-c)*y*z-s*x},
	       	{(1-c)*z*x-s*y,(1-c)*z*y+s*x,c+(1-c)*z*z}
	       };
	       
	       float [] zv=new float[]{
	       		0,0,0,0,0,0,0,0
	       };
	       
	       float [] depth=new float[]{0, 0, 0};
	       float deepest_pt = 1000000;
	       float force_in_length;
	       float force_center_length;
	       float cos_theta;
	       int deepest = 0;
	       
	       	for(int i=0;i<8;i++){
		       	for(int j=0;j<3;j++){
		       		zv[i]+= RM[2][j]*A.collisionvertices[i*3+j];
		       	}
		       	if (zv[i] < deepest_pt)
	       			deepest = i;
	   		}
	       
	       if(zv[deepest]+A.center[2]<=ground&&A.MASS.force.z<0){
	    	   A.MASS.force.z=-0.6f*A.MASS.force.z;
		       for (int i=0; i<3; ++i){
		    	   for (int j=0; j<3; ++j){
		    		   depth[i] += RM[i][j]*A.collisionvertices[deepest*3+j];
		    	   }
		       }
		       A.MASS.force.x += (float)(0.8*Math.sin(depth[0]/Math.sqrt(depth[0]*depth[0] + depth[1]*depth[1])))*A.MASS.force.z;
		       A.MASS.force.y += (float)(0.8*Math.sin(depth[1]/Math.sqrt(depth[0]*depth[0] + depth[1]*depth[1])))*A.MASS.force.z;
		       A.MASS.torque.x += 0.1*A.MASS.force.x*A.MASS.force.z*Math.sin(depth[0]/Math.sqrt(depth[0]*depth[0] + depth[1]*depth[1]));
		       A.MASS.torque.y += 0.1*A.MASS.force.y*A.MASS.force.z*Math.sin(depth[0]/Math.sqrt(depth[0]*depth[0] + depth[1]*depth[1]));

	       }
	       
	   }else{
	   		if(-0.05f+A.center[2]<=ground&&A.MASS.force.z<0){
	   			A.MASS.force.z=-0.6f*A.MASS.force.z;
	   			return;
	   		}
	   }
	}
	
	void flat(cube A){
		if (A.ifQ==1 && 
				(A.MASS.force.x*A.MASS.force.x + A.MASS.force.y*A.MASS.force.y < 0.00001))
		{
			double theta = 2 * Math.acos(A.Q.x0);        
		       float x =(float)( A.Q.x1 / (Math.sin(theta / 2)));
		       float y =(float)( A.Q.x2 / (Math.sin(theta / 2)));
		       float z =(float)( A.Q.x3 / (Math.sin(theta / 2)));
		       
		      // double t=theta*360/(2*Math.PI);
		       float c=(float)Math.cos(theta);
		       float s=(float)Math.sin(theta);
		       
		       float [][] RM=new float[][]{
		       	{c+(1-c)*x*x,(1-c)*x*y-s*z,(1-c)*x*z+s*y},
		       	{(1-c)*y*x+s*z,c+(1-c)*y*y,(1-c)*y*z-s*x},
		       	{(1-c)*z*x-s*y,(1-c)*z*y+s*x,c+(1-c)*z*z}
		       };
		       
		       float [][] pt=new float[][]{
		       		{0,0,0},
		       		{0,0,0},
		       		{0,0,0},
		       		{0,0,0},
		       		{0,0,0},
		       		{0,0,0}
		       };

		       float [] deepest = new float[]{100, 100, 100};
		       
		       for (int k=0; k<6; ++k){
			       for (int i=0; i<3; ++i){
			    	   for (int j=0; j<3; ++j){
			    		   pt[k][i] += RM[i][j]*A.collisionnormal[k*3+j];
			    	   }
			       }
		       }
		       
		
		       
		       for (int i=0; i<6; ++i)
		    	   if (pt[i][2]<deepest[2]){
		    		   deepest[0] = pt[i][0];
		    		   deepest[1] = pt[i][1];
		    		   deepest[2] = pt[i][2];
		   
		    	   }
		
		       if (deepest[2] > -0.999){
		    	  
		    	      Quater2(A, 0.2*Math.acos(-1*deepest[2]), -1*deepest[1] / Math.sqrt(deepest[0]*deepest[0] + deepest[1]*deepest[1]), deepest[0] / Math.sqrt(deepest[0]*deepest[0] + deepest[1]*deepest[1]), 0);

		       }
		       
		}
	}

}  
