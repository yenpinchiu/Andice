package Andice.dice.view;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;

public class cube {
    float one = 1.0f;//0x10000;  
    FloatBuffer vertices;
    FloatBuffer normals;
    FloatBuffer texCoords;
    ByteBuffer indices1=ByteBuffer.allocateDirect(4);  
    ByteBuffer indices2=ByteBuffer.allocateDirect(4);
    ByteBuffer indices3=ByteBuffer.allocateDirect(4);
    ByteBuffer indices4=ByteBuffer.allocateDirect(4);
    ByteBuffer indices5=ByteBuffer.allocateDirect(4);
    ByteBuffer indices6=ByteBuffer.allocateDirect(4);
    float [] TM;
    float [] IM;
    float [] center;
    float collision;
    float []collisionvertices;
    float []collisionnormal;
    float [] angle;
    Quaternion Q;
    int ifQ=0;
    int ifF=0;
    Mass MASS;

    
    
    cube(){
    	MASS=new Mass(100, 200);
    	center=new float[]{
    			0,0,0
    	};
    	angle=new float[]{
    			0,0,0
    	};
    	TM= new float[]{
    			1.0f, 0.0f, 0.0f, 0.0f,
     		   0.0f, 1.0f, 0.0f, 0.0f,
     		   0.0f, 0.0f, 1.0f, 0.0f,
     		   0.0f, 0.0f, 0.0f, 1.0f
     		};
    	IM= new float[]{
    			1.0f, 0.0f, 0.0f, 0.0f,
     		   0.0f, 1.0f, 0.0f, 0.0f,
     		   0.0f, 0.0f, 1.0f, 0.0f,
     		   0.0f, 0.0f, 0.0f, 1.0f
     		};
    	collision=(float) Math.sqrt((double)2f);
    	collisionvertices=new float[]{
        	 	-one,  one, -one,   //v0  
                -one,  one,  one,   //v1  
                 one,  one,  one,   //v2  
                 one,  one, -one,   //v3  
                -one, -one, -one,   //v4  
                -one, -one,  one,   //v5  
                 one, -one,  one,   //v6  
                 one, -one, -one    //v7 
    	};
    	collisionnormal=new float[]{
                0,0,one,     
                0,0,-one,   
                0,one,0,   
                0,-one,0,   
                one,0,0,      
                -one,0,0, 
    	};
    	
    	ByteBuffer vbb = ByteBuffer.allocateDirect(72* 4);     
    	vbb.order(ByteOrder.nativeOrder());   
    	vertices = vbb.asFloatBuffer();     
    	
    	float vert[]={   

                -one,one,one, //1
                -one,-one,one,//5
                one,-one,one, //6
                one,one,one,  //2
        		
                -one,-one,-one,   //4
                -one,one,-one,    //0
                one,one,-one,     //3
                one,-one,-one,    //7
                   
                -one,one,-one,   //0
                -one,one,one,    //1
                one,one,one,     //2
                one,one,-one,    //3
                   
                -one,-one,-one,  //4
                one,-one,-one,   //7
                one,-one,one,    //6
                -one,-one,one,   //5
                   
                one,-one,-one,   //7
                one,one,-one,    //3
                one,one,one,     //2
                one,-one,one,    //6
                   
                -one,-one,-one,  //4
                -one,-one,one,   //5
                -one,one,one,    //1
                -one,one,-one,   //0
                   
        };
    	
        for (int i = 0; i < 72; i++) {   
        	vertices.put(vert[i]);
        }
        vertices.position(0);
    	
  /*  vertices = FloatBuffer.wrap(new float[]{   

            -one,one,one, //1
            -one,-one,one,//5
            one,-one,one, //6
            one,one,one,  //2
    		
            -one,-one,-one,   //4
            -one,one,-one,    //0
            one,one,-one,     //3
            one,-one,-one,    //7
               
            -one,one,-one,   //0
            -one,one,one,    //1
            one,one,one,     //2
            one,one,-one,    //3
               
            -one,-one,-one,  //4
            one,-one,-one,   //7
            one,-one,one,    //6
            -one,-one,one,   //5
               
            one,-one,-one,   //7
            one,one,-one,    //3
            one,one,one,     //2
            one,-one,one,    //6
               
            -one,-one,-one,  //4
            -one,-one,one,   //5
            -one,one,one,    //1
            -one,one,-one,   //0
               
    });   */
        
    	ByteBuffer nbb = ByteBuffer.allocateDirect(72* 4);     
    	nbb.order(ByteOrder.nativeOrder());   
    	normals = nbb.asFloatBuffer();     
    	
    	float[] nor={   
                0,0,one,   
                0,0,one,   
                0,0,one,   
                0,0,one,   
                   
                0,0,-one,   
                0,0,-one,   
                0,0,-one,   
                0,0,-one,   
                   
                0,one,0,   
                0,one,0,   
                0,one,0,   
                0,one,0,   
                   
                0,-one,0,   
                0,-one,0,   
                0,-one,0,   
                0,-one,0,   
                   
                one,0,0,   
                one,0,0,   
                one,0,0,   
                one,0,0,   
                   
                -one,0,0,   
                -one,0,0,   
                -one,0,0,   
                -one,0,0,   
        };
    	
        for (int i = 0; i < 72; i++) {   
        	normals.put(nor[i]);
        }
        normals.position(0);
  /*         
       normals = FloatBuffer.wrap(new float[]{   
                0,0,one,   
                0,0,one,   
                0,0,one,   
                0,0,one,   
                   
                0,0,-one,   
                0,0,-one,   
                0,0,-one,   
                0,0,-one,   
                   
                0,one,0,   
                0,one,0,   
                0,one,0,   
                0,one,0,   
                   
                0,-one,0,   
                0,-one,0,   
                0,-one,0,   
                0,-one,0,   
                   
                one,0,0,   
                one,0,0,   
                one,0,0,   
                one,0,0,   
                   
                -one,0,0,   
                -one,0,0,   
                -one,0,0,   
                -one,0,0,   
        });   
           */
        
    	ByteBuffer tbb = ByteBuffer.allocateDirect(48* 4);     
    	tbb.order(ByteOrder.nativeOrder());   
    	texCoords = tbb.asFloatBuffer(); 
    	
    	float [] tex={   
        		0,0,0,one,one,one,one,0,
        		0,0,0,one,one,one,one,0,
        		0,0,0,one,one,one,one,0,
        		0,0,0,one,one,one,one,0,
        		0,0,0,one,one,one,one,0,
        		0,0,0,one,one,one,one,0,
        };
    	
        for (int i = 0; i < 48; i++) {   
        	texCoords.put(tex[i]);
        }
        texCoords.position(0);
        
      /*  texCoords = FloatBuffer.wrap(new float[]{   
        		0,0,0,one,one,one,one,0,
        		0,0,0,one,one,one,one,0,
        		0,0,0,one,one,one,one,0,
        		0,0,0,one,one,one,one,0,
        		0,0,0,one,one,one,one,0,
        		0,0,0,one,one,one,one,0,
        });   */
        
       // ByteBuffer i1 = ByteBuffer.allocateDirect(4);     
        indices1.order(ByteOrder.nativeOrder());    

        byte[] i11={   

           		0,1,3,2 

           };
        for (int i = 0; i < 4; i++) {   
        	indices1.put(i11[i]);
        }
        indices1.position(0);
        
        /*indices1 = ByteBuffer.wrap(new byte[]{   
             0,1,2,0,2,3,
             4,5,6,4,6,7,
             8,9,10,8,10,11,
             12,13,14,12,14,15,
             16,17,18,16,18,19,
             20,21,22,20,22,23,
        		//0,1,3,2,   
                /*0,0,0,0,   
                0,0,0,0,   
                0,0,0,0,   
                0,0,0,0,   
                0,0,0,0, 
        });   */
        indices2 = ByteBuffer.wrap(new byte[]{   
                //0,0,0,0,   
                4,5,7,6,   
               /* 0,0,0,0,   
                0,0,0,0,   
                0,0,0,0,   
                0,0,0,0, */
        });   
        indices3 = ByteBuffer.wrap(new byte[]{   
                //0,0,0,0,   
                //0,0,0,0,   
                8,9,11,10,   
                /*0,0,0,0,   
                0,0,0,0,   
                0,0,0,0,*/   
        });   
        indices4 = ByteBuffer.wrap(new byte[]{   
                /*0,0,0,0,   
                0,0,0,0,   
                0,0,0,0,*/  
                12,13,15,14,   
                //0,0,0,0,   
                //0,0,0,0,   
        });   
        indices5 = ByteBuffer.wrap(new byte[]{   
                /*0,0,0,0,   
                0,0,0,0,   
                0,0,0,0,   
                0,0,0,0, */  
                16,17,19,18,   
                //0,0,0,0,   
        });   
        indices6 = ByteBuffer.wrap(new byte[]{   
               /* 0,0,0,0,   
                0,0,0,0,   
                0,0,0,0,   
                0,0,0,0,   
                0,0,0,0,  */
                20,21,23,22,   
        });   
           
    }
    
    void draw(GL10 gl){
          gl.glNormalPointer(GL10.GL_FLOAT, 0, normals);   
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertices);   
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoords);  
        }
    
    /*void drawtexture(GL10 gl){
    	Log.v("texture", "1");
    	
    }*/

    
}
