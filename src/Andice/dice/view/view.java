package Andice.dice.view;

import java.io.File;
import java.util.List;

import Andice.dice.view.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.Toast;



public class view extends Activity{   
    
    GLRender render = new GLRender(this);   
        
    SensorManager sensorManager, angleManager;
    boolean accelerometerPresent, anglePresent;
    Sensor accelerometerSensor, angleSensor;
    
    
    private float mPreviousX;   
    private float mPreviousY; 
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;   
    private final float TRACKBALL_SCALE_FACTOR = 36.0f;   

    /** Called when the activity is first created. */ 
    PopupWindow popUp;
    LinearLayout layout;
    private int pictureNum = 0;
    private Db4oHelper db;
    private Db4oHelper2 db2;
    private String who = "one";
    private int which = 0; 
       
    @Override  
    public void onCreate(Bundle savedInstanceState) {   
    	sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
    	angleManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        List<Sensor> angleList = angleManager.getSensorList(Sensor.TYPE_ORIENTATION);
        super.onCreate(savedInstanceState);   
        
        GLSurfaceView glView = new GLSurfaceView(this);   
           
        glView.setRenderer(render);   
        setContentView(glView);  
        
        
        File dicepic = new File( Environment.getExternalStorageDirectory()+"/dicepic/");
        dicepic.mkdirs();
        
        db = new Db4oHelper(this);
       
        
        if(db.getdice("one") == null)
        {
        db.addData("one",0,1,2,3,4,5);
        db.addData("two",0,1,2,3,4,5);
        db.addData("three",0,1,2,3,4,5);
        }
        
        render.t0 = db.getdice("one").face1;
    	render.t1 = db.getdice("one").face2;
    	render.t2 = db.getdice("one").face3;
    	render.t3 = db.getdice("one").face4;
    	render.t4 = db.getdice("one").face5;
    	render.t5 = db.getdice("one").face6;
    	
    	render.t02 = db.getdice("two").face1;
    	render.t12 = db.getdice("two").face2;
    	render.t22 = db.getdice("two").face3;
    	render.t32 = db.getdice("two").face4;
    	render.t42 = db.getdice("two").face5;
    	render.t52 = db.getdice("two").face6;
    	
    	render.t03 = db.getdice("three").face1;
    	render.t13 = db.getdice("three").face2;
    	render.t23 = db.getdice("three").face3;
    	render.t33 = db.getdice("three").face4;
    	render.t43 = db.getdice("three").face5;
    	render.t53 = db.getdice("three").face6;
    	
    	db2 = new Db4oHelper2(this);
	    if(db2.getpath("one") == null)db2.addData("one", 0);
	    pictureNum = db2.getpath("one").p;
	    if(pictureNum == 35)pictureNum = 0;
	    
	    File existornot = new File(Environment.getExternalStorageDirectory()+"/dicepic/tmp.jpg");
	    if(existornot.exists())existornot.delete();
        
        if(sensorList.size() > 0){
            accelerometerPresent = true;
            accelerometerSensor = sensorList.get(0);
           }
           else{
            accelerometerPresent = false;
           }
        
        if(angleList.size() > 0){
            anglePresent = true;
            angleSensor = angleList.get(0);
           }
           else{
        	anglePresent = false;
           }
        
        
          
    }   
    
     public boolean onTrackballEvent(MotionEvent e) {   
        // render.xrot += e.getX() * TRACKBALL_SCALE_FACTOR;   
       //  render.yrot += e.getY() * TRACKBALL_SCALE_FACTOR;  
          render.xrot = e.getX() * TRACKBALL_SCALE_FACTOR;   
          render.yrot = e.getY() * TRACKBALL_SCALE_FACTOR; 
            return true;   
        }   
         public boolean onTouchEvent(MotionEvent e) {   
            float x = e.getX();   
            float y = e.getY();   
            switch (e.getAction()) {   
            case MotionEvent.ACTION_MOVE:   
                float dx = x - mPreviousX;   
                float dy = y - mPreviousY;   
                //render.xrot += dx * TOUCH_SCALE_FACTOR;   
                //render.yrot += dy * TOUCH_SCALE_FACTOR; 
                render.xrot = dx * TOUCH_SCALE_FACTOR;   
                render.yrot = dy * TOUCH_SCALE_FACTOR;
            }   
            mPreviousX = x;   
            mPreviousY = y;   
            return true;   
        }   

    
    @Override
    protected void onResume() {
     // TODO Auto-generated method stub
     super.onResume();
      
     if(accelerometerPresent && anglePresent){
      sensorManager.registerListener(accelerometerListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
      sensorManager.registerListener(angleListener, angleSensor, SensorManager.SENSOR_DELAY_NORMAL);
      //Toast.makeText(this, "Register accelerometerListener", Toast.LENGTH_LONG).show();
     }
    }
     
    @Override
    protected void onStop() {
     // TODO Auto-generated method stub
     super.onStop();
      
     if(accelerometerPresent  && anglePresent){
      sensorManager.unregisterListener(accelerometerListener);
      angleManager.unregisterListener(angleListener);
      //Toast.makeText(this, "Unregister accelerometerListener", Toast.LENGTH_LONG).show();
     }
    }
    public boolean onKeyUp(int keyCode, KeyEvent event)   
    {   
        render.onKeyUp(keyCode, event);   
        return false;   
    }

         private SensorEventListener accelerometerListener = new SensorEventListener(){
        	 
        	 @Override
        	 public void onAccuracyChanged(Sensor arg0, int arg1) {
        	  // TODO Auto-generated method stub
        	  
        	 }
        	 
        	 @Override
        	 public void onSensorChanged(SensorEvent event) {
        	  // TODO Auto-generated method stub
        		 if (render.mode== 0 || render.mode== 2){
		        	  render.xacc = event.values[0] - (float)(SensorManager.STANDARD_GRAVITY * Math.sin(Math.PI/180 * render.zangle));
		        	  render.yacc = event.values[1] - (float)(SensorManager.STANDARD_GRAVITY * Math.sin(Math.PI/180 * -1*render.yangle));
       		 }
       		 else{
       			  render.xacc = event.values[0];
		        	  render.yacc = event.values[1];
       		 }
       	    render.zacc = event.values[2] - (float)(SensorManager.STANDARD_GRAVITY * Math.cos(Math.PI/180 * render.zangle));
        	 }};
        	 
        private SensorEventListener angleListener = new SensorEventListener(){
            	 
            	 @Override
            	 public void onAccuracyChanged(Sensor arg0, int arg1) {
            	  // TODO Auto-generated method stub 
            	 }
            	 
            	 @Override
            	 public void onSensorChanged(SensorEvent event) {
            	  // TODO Auto-generated method stub
            	  render.yangle = event.values[1];
            	  render.zangle = event.values[2];
            	 }};
            	 
            	 public boolean onCreateOptionsMenu(Menu menu) {
            		 super.onCreateOptionsMenu(menu);
            		 
            		 boolean result = super.onCreateOptionsMenu(menu);
            		    SubMenu dicestreet = menu.addSubMenu(0, 0, 0, "Dice"); 
            		    MenuInflater inflater = getMenuInflater();  
            		    inflater.inflate(R.menu.menu, dicestreet);
            		    
            		    menu.add(0, 4, 0, "Custom");
            		    
            		    boolean result2 = super.onCreateOptionsMenu(menu);
            		    SubMenu mode = menu.addSubMenu(0, 5, 0, "Mode");
            		    MenuInflater inflater2 = getMenuInflater();  
            		    inflater2.inflate(R.menu.menu2, mode);
            		    return true;

                 }
            	 
            	 @Override
            	 public boolean onOptionsItemSelected(MenuItem item)
            	  {
            	      super.onOptionsItemSelected(item);
            	      switch(item.getItemId()){         	          
            	          case R.id.check1: 
            	        	  if (item.isChecked()) item.setChecked(false);
            	        	  else item.setChecked(true);
            	        	  if(render.d1 == 0) render.d1 = 1;
                              else if(render.d1 == 1) render.d1 = 0;
            	          break;
            	          
            	          case R.id.check2:
            	        	  if (item.isChecked()) item.setChecked(false);
            	        	  else item.setChecked(true);
            	        	  if(render.d2 == 0) render.d2 = 1;
                         	  else if(render.d2 == 1) render.d2 = 0;
            	          break;
            	          
            	          case R.id.check3:
            	        	  if (item.isChecked()) item.setChecked(false);
            	        	  else item.setChecked(true);
            	        	  if(render.d3 == 0) render.d3 = 1;
                         	  else if(render.d3 == 1) render.d3 = 0;
            	          break;
            	          
            	          case 4:
      	        	        layout = new LinearLayout(this);
                  	    	Context mContext =  view.this;
                  	    	LayoutInflater mLayoutInflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
                  	    	View popunwindwow = mLayoutInflater.inflate(R.layout.custum, null);
                  	    	popUp = new PopupWindow(popunwindwow, LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
                  	    	popUp.showAtLocation(layout,Gravity.RIGHT|Gravity.BOTTOM, 0, 0);    
            	            break; 
            	            
            	          case R.id.item01:
            	        	  if (item.isChecked()){ item.setChecked(false);render.mode = render.mode - 1;}
            	        	  else { item.setChecked(true);render.mode = render.mode + 1;}
            	        	  
            	          break;
            	          
            	          case R.id.item02:
            	        	  if (item.isChecked()){ item.setChecked(false);render.mode = render.mode - 2;}
            	        	  else{ item.setChecked(true);render.mode = render.mode + 2;}
                	          break;
            	        }
            	        return true;
            	  }
            	 
            	 public void photo(View target){
            		 Intent i = new Intent();
                     i.setClassName("Andice.dice.view",
                                    "Andice.dice.view.camera");
                     startActivity(i);   
            	 }
            	 
            	 
            	 public void back(View target){
            		 
            		 popUp.dismiss();
            	 }
            	 
            	 public void who1(View target){
            		 
            		 Toast.makeText(this, "now edit dice one", Toast.LENGTH_SHORT).show();

            		 who = "one";
            	 }
            	 public void who2(View target){
            		 Toast.makeText(this, "now edit dice two", Toast.LENGTH_SHORT).show();
            		 who = "two";
            	 }
            	 public void who3(View target){
            		 Toast.makeText(this, "now edit dice three", Toast.LENGTH_SHORT).show();
            		 who = "three";
            	 }
            	 
            	 public void which1(View target){
            		 Toast.makeText(this, "now edit face one", Toast.LENGTH_SHORT).show();
            		 which = 0;
            	 }
            	 public void which2(View target){
            		 Toast.makeText(this, "now edit face two", Toast.LENGTH_SHORT).show();
            		 which = 1;
            	 }
            	 public void which3(View target){
            		 Toast.makeText(this, "now edit face three", Toast.LENGTH_SHORT).show();
            		 which = 2;
            	 }
            	 public void which4(View target){
            		 Toast.makeText(this, "now edit face four", Toast.LENGTH_SHORT).show();
            		 which = 3;
            	 }
            	 public void which5(View target){
            		 Toast.makeText(this, "now edit face five", Toast.LENGTH_SHORT).show();
            		 which = 4;
            	 }
            	 public void which6(View target){
            		 Toast.makeText(this, "now edit face six", Toast.LENGTH_SHORT).show();
            		 which = 5;
            	 }
            	 
            	 public void defaults(View target){
            		 db.addData(who,0,1,2,3,4,5);
            		 
            		 if(who == "one"){
                		 render.t0 = db.getdice("one").face1;
                		 render.t1 = db.getdice("one").face2;
                		 render.t2 = db.getdice("one").face3;
                		 render.t3 = db.getdice("one").face4;
                		 render.t4 = db.getdice("one").face5;
                		 render.t5 = db.getdice("one").face6;
                		 }
                		 if(who == "two"){
                    		 render.t02 = db.getdice("two").face1;
                    		 render.t12 = db.getdice("two").face2;
                    		 render.t22 = db.getdice("two").face3;
                    		 render.t32 = db.getdice("two").face4;
                    		 render.t42 = db.getdice("two").face5;
                    		 render.t52 = db.getdice("two").face6;
                    		 }
                		 if(who == "three"){
                    		 render.t03 = db.getdice("three").face1;
                    		 render.t13 = db.getdice("three").face2;
                    		 render.t23 = db.getdice("three").face3;
                    		 render.t33 = db.getdice("three").face4;
                    		 render.t43 = db.getdice("three").face5;
                    		 render.t53 = db.getdice("three").face6;
                    		 }
                		 
                		 Toast.makeText(this, "change to default style", Toast.LENGTH_SHORT).show();
                		 
            	 }
            	 
            	 public void style(View target){
            		 db.addData(who,6,7,8,9,10,11);
            		 
            		 if(who == "one"){
                		 render.t0 = db.getdice("one").face1;
                		 render.t1 = db.getdice("one").face2;
                		 render.t2 = db.getdice("one").face3;
                		 render.t3 = db.getdice("one").face4;
                		 render.t4 = db.getdice("one").face5;
                		 render.t5 = db.getdice("one").face6;
                		 }
                		 if(who == "two"){
                    		 render.t02 = db.getdice("two").face1;
                    		 render.t12 = db.getdice("two").face2;
                    		 render.t22 = db.getdice("two").face3;
                    		 render.t32 = db.getdice("two").face4;
                    		 render.t42 = db.getdice("two").face5;
                    		 render.t52 = db.getdice("two").face6;
                    		 }
                		 if(who == "three"){
                    		 render.t03 = db.getdice("three").face1;
                    		 render.t13 = db.getdice("three").face2;
                    		 render.t23 = db.getdice("three").face3;
                    		 render.t33 = db.getdice("three").face4;
                    		 render.t43 = db.getdice("three").face5;
                    		 render.t53 = db.getdice("three").face6;
                    		 }
                		 Toast.makeText(this, "change to stylish style", Toast.LENGTH_SHORT).show();
            	 }
            	 
            	 public void save(View target){
            		 
                     File tmp = new File(Environment.getExternalStorageDirectory()+"/dicepic/tmp.jpg");
            		 
            		 if(tmp.exists()){
            		 File tmp2 = new File(Environment.getExternalStorageDirectory()+"/dicepic/"+pictureNum+".jpg");
            		 tmp.renameTo(tmp2); 
            		 render.scar = 1;
            		            		 
            		 if(which == 0)db.addData(who,pictureNum+12,db.getdice(who).face2,db.getdice(who).face3,db.getdice(who).face4,db.getdice(who).face5,db.getdice(who).face6);
            		 if(which == 1)db.addData(who,db.getdice(who).face1,pictureNum+12,db.getdice(who).face3,db.getdice(who).face4,db.getdice(who).face5,db.getdice(who).face6);
            		 if(which == 2)db.addData(who,db.getdice(who).face1,db.getdice(who).face2,pictureNum+12,db.getdice(who).face4,db.getdice(who).face5,db.getdice(who).face6);
            		 if(which == 3)db.addData(who,db.getdice(who).face1,db.getdice(who).face2,db.getdice(who).face3,pictureNum+12,db.getdice(who).face5,db.getdice(who).face6);
            		 if(which == 4)db.addData(who,db.getdice(who).face1,db.getdice(who).face2,db.getdice(who).face3,db.getdice(who).face4,pictureNum+12,db.getdice(who).face6);
            		 if(which == 5)db.addData(who,db.getdice(who).face1,db.getdice(who).face2,db.getdice(who).face3,db.getdice(who).face4,db.getdice(who).face5,pictureNum+12);
          		 
            		 if(who == "one"){
            		 render.t0 = db.getdice("one").face1;
            		 render.t1 = db.getdice("one").face2;
            		 render.t2 = db.getdice("one").face3;
            		 render.t3 = db.getdice("one").face4;
            		 render.t4 = db.getdice("one").face5;
            		 render.t5 = db.getdice("one").face6;
            		 }
            		 if(who == "two"){
                		 render.t02 = db.getdice("two").face1;
                		 render.t12 = db.getdice("two").face2;
                		 render.t22 = db.getdice("two").face3;
                		 render.t32 = db.getdice("two").face4;
                		 render.t42 = db.getdice("two").face5;
                		 render.t52 = db.getdice("two").face6;
                		 }
            		 if(who == "three"){
                		 render.t03 = db.getdice("three").face1;
                		 render.t13 = db.getdice("three").face2;
                		 render.t23 = db.getdice("three").face3;
                		 render.t33 = db.getdice("three").face4;
                		 render.t43 = db.getdice("three").face5;
                		 render.t53 = db.getdice("three").face6;
                		 }
            		 
            		 db2.addData("one",pictureNum+1);
            		 pictureNum = db2.getpath("one").p;
            		 }
            		 
            		 Toast.makeText(this, "apply photo on dice ", Toast.LENGTH_SHORT).show();
            	 }
            	
}   



