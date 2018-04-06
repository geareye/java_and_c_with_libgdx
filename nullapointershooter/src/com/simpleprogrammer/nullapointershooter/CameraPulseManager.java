package com.simpleprogrammer.nullapointershooter;

import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.hardware.Camera;
import android.hardware.Camera.ErrorCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

public class CameraPulseManager implements Runnable
{
	//private volatile String myParam;
	
	private static final String TAG = "RUNNABLE_DEBUG";

	public  Thread mThread;
	private Camera mCamera;
	private Camera.Parameters parameters; 
	
	SurfaceView 		mVideoCaptureView;
	SurfaceTexture		surfaceTexture;
	
	public boolean		bWaitForProcessing = true;
	private boolean 	bCameraIsInitialized = false;

	private int 		mFrameWidth;
    private int 		mFrameHeight;
    private byte[]  	mFrame;
    private byte[] 		mBuffer;
    private int 		mFrameSize;
    public int			nPulseIteration;
	private long 		startTime = 0L;
	
	private long 		t0 = 0L;
	private long 		t1 = 0L;
	
	public int 			nDebugIteration;	
	
	private float[] arrFFT;

	private boolean bThreadRun;
	public boolean bToggleLight = false; 

	private long timeInMilliseconds = 0;

	private volatile float nSharpnessMeasure = 0;

	private float[] arrSharpness;

    
	public CameraPulseManager() {
		//Log.d("Constructor", "CameraPulseManager");  
		mThread = new Thread(this);
		
		nPulseIteration = 0;
		timeInMilliseconds = 0;
		bToggleLight = false;
		this.arrSharpness = new float[500];
		nDebugIteration = 0;
		this.arrFFT = new float[512];
	}
	
	public static int[] applyGrayScale(byte [] data, int width, int height) {
	    int p;
	    int size = width*height;
	    int [] pixels = new int[size];
	    
	    for(int i = 0; i < size; i++) {
	        p = data[i] & 0xFF;
	        pixels[i] = 0xff000000 | p<<16 | p<<8 | p;
	    }
	    
	    return pixels;
	}
	
	public float[] getIntensityArrayFrequencyMagnitude()
    {
		float[] output;
		output = new float[512];		
		output = FibLib.nativeGetIntensityArrayFrequencyMagnitude();
		
		 try {
				wait(10);//Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	 	//Log.d("SHARPNESS", "SHARPNESS " + Float.toString(output));	                
        return output;
    }
	
	@Override
	public void run() 
	{
		//Log.i(TAG, "Starting processing thread");
		bThreadRun = true;
		 
		// Wait until the first camera frame is ready.
        try {
            while ((bThreadRun && !bCameraIsInitialized) || bWaitForProcessing) 
            {
                synchronized (this) 
                {
                	nPulseIteration = 250;
                	//Log.i(TAG, "wait 100");
                    wait(100);  // wait 100 milliseconds before trying again.
                    
                   // nDebugIteration++;
                }
            }
        } catch (InterruptedException e) 
        {
            //nDebugIteration = 666;
            e.printStackTrace();
        }
        
        // Background processing loop.
        // Each iteration of this loop will process the latest camera image
        try {
        	 while (bThreadRun) 
        	 {
 	            // Process this frame.
 	            synchronized (this) 
 	            {  
 	            	int _errStatus = -1;
 	            	
 	            	 	            	
 	            	timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
 	            	int secs = (int) (timeInMilliseconds / 1000);
 	            	
 	            	//First sample time:
 	            	t0 = SystemClock.uptimeMillis();
 	            	
 	            	
 	            	//Obtain sharpness measure
 	            	wait(10);
 	            	nSharpnessMeasure = processFrame(mFrame);
 	            	arrFFT = getIntensityArrayFrequencyMagnitude();
 	            	
 	            	//Second sample time:
 	            	t1 = SystemClock.uptimeMillis();
   	            	
 	            	//nDebugIteration=777;
 	            	Log.d("TIME", "SECONDS: " + Float.toString(t1 - t0));
 	            	//_errStatus = feedBuffer(nSharpnessMeasure,nPulseIteration,secs);
 	            	//arrSharpness[nPulseIteration] = nSharpnessMeasure;

 	            	///Log.d("from run()", "Process Count" + Integer.toString(nPulseIteration)); 	            	
            	 
 	            	 //Report the memory usage
 	            	//long nMem;
 	            	//nMem = Runtime.getRuntime().totalMemory();
 	            	//Log.d("MEMORY REPORT", "MEMORY: VM Heap Size: %f" + nMem);
 	            	
 	            	//nMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
 	            	//Log.d("MEMORY REPORT", "MEMORY: Allocated VM Memory: %f" + nMem);
 	            	
 	            	//nMem = Runtime.getRuntime().maxMemory();
 	            	//Log.d("MEMORY REPORT", "MEMORY: VM Heap Size Limit: %f" + nMem);
 	            	
 	            	//nMem = Debug.getNativeHeapAllocatedSize();
 	            	//Log.d("MEMORY REPORT", "MEMORY: Native Allocated Memory: %f" + nMem);
 	            	
 	            	nPulseIteration++;
 					nPulseIteration%=500;
 					//Log.d("seconds", "seconds: %d" + secs);
 					//if ((secs % 10) == 0) 					//if (nSharpnessMeasure != 0)
 					//{
 					//	bToggleLight = !bToggleLight;
 					//	if (bToggleLight)
 						//{
 						//	parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
 						//	mCamera.setParameters(parameters);
 					//	}
 					//	else
 					//	{
 						//	parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
 						//	mCamera.setParameters(parameters);
 					//	}
 					//}
					
 					if (mThread.interrupted())
 						return;

 	            }	            
 	            
 	        }//end of background processing loop.
        } catch (Exception e){
    		//nDebugIteration = 999;//Log.e("CameraView", "Runnable Processing Issue");
    	}       
		 
	};
	
	public float getPulseIteration(){
		return (float)nPulseIteration;
	}
	
	public float getSharpnessValue(){
		return nSharpnessMeasure;
	}
	
	public int getFrameWidth() {
        return mFrameWidth;
    }

    public int getFrameHeight() {
        return mFrameHeight;
    }

    public int getDebugIteration()
    {
    	return nDebugIteration;
    }
    
	public float[] getFftArray(){
		return arrFFT;
	}

    
	public float processFrame(byte[] data)
    {
        //android.os.Debug.waitForDebugger(); 
        
        /*int i, j, n, m;
        double x,y;
        float tempSum=0;
    	float sum, sharpnessMeasure;
    	
    	int nRows = getFrameHeight();
    	int nCols = getFrameWidth();
    	
    	
    	
    	int[][]  tempImg = new int[3][3];
    	int[][]  tempGx = new int[3][3];
    	int[][]  tempGy = new int[3][3];
    	int[][]  sharpImage = new int[nRows][nCols];
    	int[][]  product = new int[nRows][nCols];
    		*/	

		// Just prepare the camera image for display without any modification. Uses C/C++ code through NDK (JNI).
        float output = 0;
        //int[] pixels = applyGrayScale(data, getFrameWidth(), getFrameHeight());
        output = FibLib.nativeProcessSharpness(getFrameHeight(), getFrameWidth(), data);
        //Log.d("SHARPNESS", "SHARPNESS " + Float.toString(output));
                
        return output;        //return output;
    }
	

public byte[] convertYuvToJpeg(byte[] data, Camera camera) {

    YuvImage image = new YuvImage(data, ImageFormat.NV21,
            camera.getParameters().getPreviewSize().width, camera.getParameters().getPreviewSize().height, null);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    int quality = 20; //set quality
    image.compressToJpeg(new Rect(0, 0, camera.getParameters().getPreviewSize().width, camera.getParameters().getPreviewSize().height), quality, baos);//this line decreases the image quality


    return baos.toByteArray();
}
	
	public void startCamera()
    {
    	//Initialize Memory
		FibLib.initializationStep();
		
    	//initRunJNI();    	
    	long nTotalMemory;
        nTotalMemory = Debug.getNativeHeapAllocatedSize();
    	stopPreviewAndFreeCamera();
    	
        SurfaceHolder videoCaptureViewHolder = null;

        //Stop waiting for processing.
        bWaitForProcessing   = false;
        //Signal that camera is ready.
    	//mCameraIsInitialized = true;  
    	
        try 
        {
            mCamera = Camera.open();
        } 
        catch (RuntimeException e) 
        {
            //Log.e("CameraTest", "Camera Open failed");
            return;
        }
        
        mCamera.setErrorCallback(new ErrorCallback() 
        {
            public void onError(int error, Camera camera) 
            {
            }
        }); 
        
        parameters = mCamera.getParameters();        //Camera.Parameters parameters = mCamera.getParameters();
        for(int i: parameters.getSupportedPreviewFormats()) 
        { 
        	//Log.e(TAG, "preview format supported are = "+i);
        }
         	
        parameters.setPreviewFpsRange(15000,30000);
        List<int[]> supportedPreviewFps=parameters.getSupportedPreviewFpsRange();
        Iterator<int[]> supportedPreviewFpsIterator=supportedPreviewFps.iterator();
        while(supportedPreviewFpsIterator.hasNext())
        {
            int[] tmpRate = supportedPreviewFpsIterator.next();
            StringBuffer sb=new StringBuffer();
            sb.append("supportedPreviewRate: ");
            for(int i=tmpRate.length,j=0;j<i;j++)            
                sb.append(tmpRate[j]+", ");            
            //Log.v("CameraTest",sb.toString());
        }        
        Size tmpSize = null;
        List<Size> supportedPreviewSizes=parameters.getSupportedPreviewSizes();
        Iterator<Size> supportedPreviewSizesIterator=supportedPreviewSizes.iterator();
        while(supportedPreviewSizesIterator.hasNext())
        {
           tmpSize=supportedPreviewSizesIterator.next();
            //Log.v("CameraTest","supportedPreviewSize.width = "+tmpSize.width+"supportedPreviewSize.height = "+tmpSize.height);
        }
    
        //Camera Image Size 
        Size size = parameters.getPreviewSize();
        mFrameHeight = size.height;
        mFrameWidth = size.width;      
        //Turn on FLASH
        parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
        mCamera.setParameters(parameters);
        SurfaceTexture surfaceTexture = new SurfaceTexture(0);

        if (null != mVideoCaptureView)
            videoCaptureViewHolder = mVideoCaptureView.getHolder();
        
        try 
        {
            mCamera.setPreviewTexture(surfaceTexture);
        } 
        catch (Throwable t) 
        {
        }
        
        //Log.v("CameraTest","Camera PreviewFrameRate = "+mCamera.getParameters().getPreviewFrameRate());
        Size previewSize=mCamera.getParameters().getPreviewSize();
        int dataBufferSize=(int)(previewSize.height*previewSize.width*
                               (ImageFormat.getBitsPerPixel(mCamera.getParameters().getPreviewFormat())/8.0));
       
        //mBuffer = new byte[dataBufferSize];
        /* The buffer where the current frame will be copied */
        mFrame = new byte[dataBufferSize];
        mCamera.addCallbackBuffer(new byte[dataBufferSize]);
         
        /* Now we can start a preview */
        try 
        {        
            mCamera.startPreview();
            mCamera.setPreviewCallbackWithBuffer(new Camera.PreviewCallback() 
            {
                private long timestamp=0;
                public synchronized void onPreviewFrame(byte[] data, Camera camera) 
                {                	            
                	timestamp=System.currentTimeMillis(); 
                    try
                    {	
                    	//Whenever a camera preview frame is ready, just copy it straight to our mFrame,
                        //and don't worry about blocking the main UI thread until it is safe.
                        System.arraycopy(data, 0, mFrame, 0, data.length);      
                        camera.addCallbackBuffer(mFrame);        //                camera.addCallbackBuffer(mBuffer);
                         // Signal that a camera frame is ready, without blocking the main thread.
                         bCameraIsInitialized = true;
                      //  Log.i(stringExample(),stringExample());
                    }
                    catch (Exception e) 
                    {
                    	//nDebugIteration = 555;
                        //Log.e("CameraTest", "addCallbackBuffer error");
                        return;
                    }
                    return;
                }
            });
         
        } 
        catch (Throwable e) 
        {
        	//nDebugIteration = 444;

            mCamera.release();
            mCamera = null;
            return;
        }
        
        //Start Time
        startTime = SystemClock.uptimeMillis();
	    //Start the processing of frames immediately.
	    mThread.start();
        
    }

	public void finish(){
		stopMeasuring();
        mThread.interrupt();
	}

	public void stopMeasuring() {
		int nJniClearReturn;
        stopPreviewAndFreeCamera();
        bWaitForProcessing   = true;
        bCameraIsInitialized = false;
        nPulseIteration = 0;
        //Clear JNI structs.
        //nJniClearReturn=endRunJNI();
	}
	
	//////////////////////////////////////////
	/**
     * When this function returns, mCamera will be null.
     */
    private void stopPreviewAndFreeCamera() {

        if (mCamera != null) {
            // Call stopPreview() to stop updating the preview surface.
            mCamera.stopPreview();
        
            // Important: Call release() to release the camera for use by other
            // applications. Applications should release the camera immediately
            // during onPause() and re-open() it during onResume()).
            mCamera.release();
        
            mCamera = null;
        }
    }

    
}
