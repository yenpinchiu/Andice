package Andice.dice.view;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import Andice.dice.view.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class camera extends Activity implements SurfaceHolder.Callback {

	  private Camera mCamera01;
	  private Button mButton01, mButton02;
	  
	  private static String TAG = "HIPPO_DEBUG";
	  private SurfaceView mSurfaceView01;
	  private SurfaceHolder mSurfaceHolder01;
	  private boolean bIfPreview = false;
	  
	  /* 將照下來的圖檔儲存在此 */
	  private String strCaptureFilePath = Environment.getExternalStorageDirectory()+"/dicepic/";
	  
	  /** Called when the activity is first created. */
	  @Override
	  public void onCreate(Bundle savedInstanceState)
	  {
	    super.onCreate(savedInstanceState);
	    
	    setContentView(R.layout.came);

	    /* 取得螢幕解析像素 */
	    DisplayMetrics dm = new DisplayMetrics();
	    getWindowManager().getDefaultDisplay().getMetrics(dm);
	    mSurfaceView01 = (SurfaceView) findViewById(R.id.SurfaceView01);
	    mSurfaceHolder01 = mSurfaceView01.getHolder();
	    mSurfaceHolder01.addCallback(camera.this);
	    mSurfaceHolder01.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	    
	    mButton01 = (Button)findViewById(R.id.CButton01);
	    mButton02 = (Button)findViewById(R.id.CButton02);
	    
	    /* 開啟相機及Preview */
	    mButton01.setOnClickListener(new Button.OnClickListener()
	    {
	      public void onClick(View v)
	      {
	        initCamera();
	      }
	    });
	    
	    /* 拍照 */
	    mButton02.setOnClickListener(new Button.OnClickListener()
	    {
	      public void onClick(View arg0)
	      {
	        if(checkSDCard())
	        {
	          takePicture();
	        }
	      }
	    });
	  }
	  
	  private void initCamera()
	  {
	    if(!bIfPreview)
	    {	     
	      try
	      {
	        mCamera01 = Camera.open();
	      }
	      catch(Exception e)
	      {
	        Log.e(TAG, e.getMessage());
	      }
	    }
	    
	    if (mCamera01 != null && !bIfPreview)
	    {
	      try
	      {
	        Log.i(TAG, "inside the camera");
	        mCamera01.setPreviewDisplay(mSurfaceHolder01);
	        Camera.Parameters parameters = mCamera01.getParameters();
	        parameters.setPictureFormat(PixelFormat.JPEG);
	        
                parameters.setPreviewSize(320, 240);
	            parameters.setPictureSize(512, 384);
	            mCamera01.setParameters(parameters);
	            mCamera01.setPreviewDisplay(mSurfaceHolder01);
	            mCamera01.startPreview();            
	            bIfPreview = true;
	      }
	      catch (IOException e)
	      {
	        mCamera01.release();
	        mCamera01 = null;
	        Log.i(TAG, e.toString());
	        e.printStackTrace();
	      }
	    }
	  }
	  

	  private void takePicture() 
	  {
	    if (mCamera01 != null && bIfPreview) 
	    {
	      mCamera01.takePicture(shutterCallback, rawCallback, jpegCallback);
	      
	      Log.e("P","123");
	    }
	  }
	  
	  private void resetCamera()
	  {
	    if (mCamera01 != null && bIfPreview)
	    {
	      mCamera01.stopPreview();
	      
	      mCamera01.release();
	      mCamera01 = null;
	      
	      Log.i(TAG, "stopPreview");
	      bIfPreview = false;
	    }
	  }
	   
	  private ShutterCallback shutterCallback = new ShutterCallback() 
	  { 
	    public void onShutter() 
	    { 
	      // Shutter has closed 
	    } 
	  }; 
	   
	  private PictureCallback rawCallback = new PictureCallback() 
	  { 
	    public void onPictureTaken(byte[] _data, Camera _camera) 
	    { 
	    } 
	  }; 

	  private PictureCallback jpegCallback = new PictureCallback() 
	  {
	    public void onPictureTaken(byte[] _data, Camera _camera)
	    {
	      Bitmap bm = BitmapFactory.decodeByteArray(_data, 0, _data.length); 
	      File myCaptureFile = new File(strCaptureFilePath +  "tmp.jpg" );
	      
	      try
	      {
	        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
	        Bitmap tmp = Bitmap.createScaledBitmap(bm, 512, 512,false);
	        tmp.compress(Bitmap.CompressFormat.PNG, 80, bos);
	        
	        bos.flush();
	        
	        bos.close();
	        
	        resetCamera();
	        initCamera();
	      }
	      catch (Exception e)
	      {
	        Log.e(TAG, e.getMessage());
	        Log.e(TAG, e.toString());
	      }
	    }
	  };
	  
	  public void mMakeTextToast(String str, boolean isLong)
	  {
	    if(isLong==true)
	    {
	      Toast.makeText(camera.this, str, Toast.LENGTH_LONG).show();
	    }
	    else
	    {
	      Toast.makeText(camera.this, str, Toast.LENGTH_SHORT).show();
	    }
	  }
	  
	  private boolean checkSDCard()
	  {
	    if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
	    {
	      return true;
	    }
	    else
	    {
	      return false;
	    }
	  }
	  
	  public void surfaceChanged(SurfaceHolder surfaceholder, int format, int w, int h)
	  {
	    Log.i(TAG, "Surface Changed1");
	  }
	  
	  public void surfaceCreated(SurfaceHolder surfaceholder)
	  {
	    Log.i(TAG, "Surface Changed2");
	  }
	  @Override
	  protected void onPause()
	  {
	    try
	    {
	      resetCamera();
	      mCamera01.release();
	    }
	    catch(Exception e)
	    {
	      e.printStackTrace();
	    }
	    super.onPause();
	  }

	  public void surfaceDestroyed(SurfaceHolder holder)
	  {
	    
	  }
	   
	}