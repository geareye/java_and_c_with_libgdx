package com.simpleprogrammer.nullapointershooter;


import android.os.Debug;
import android.util.Log;

import com.badlogic.gdx.Gdx;


public class AccelerometerPulseManager implements Runnable
{
	//private volatile String myParam;
	
	private static final String TAG = "RUNNABLE_DEBUG";

	public  Thread mThread;
	
	public boolean		bWaitForProcessing = true;

	public int 			nDebugIteration;
    public int			nPulseIteration;


	private boolean bThreadRun;
	public boolean bToggleLight = false; 

	private long timeInMilliseconds = 0;

	//private volatile float[] nAccelerationMeasure;

	private float[] arrAcceleration;

	private boolean bAccelerometerIsInitialized;

    
	public AccelerometerPulseManager() {
		//Log.d("Constructor", "AccelerometerPulseManager");  
		mThread = new Thread(this);
		
		nPulseIteration = 0;
		timeInMilliseconds = 0;
		bToggleLight = false;
		this.arrAcceleration = new float[500];
		nDebugIteration = 0;
		bAccelerometerIsInitialized = false;
	}
	
	@Override
	public void run() 
	{
		//Log.i(TAG, "Starting processing thread");
		bThreadRun = true;
		 
		// Wait until the first camera frame is ready.
        try {
            while ((bThreadRun && !bAccelerometerIsInitialized) || bWaitForProcessing) 
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
 	            	
 	            	//timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
 	            	//int secs = (int) (timeInMilliseconds / 1000);
 	            	
 	            	
 	            	/*Get the accelerometer readings for x, y and z directions, 
 	            	 * and calculate the magnitude of acceleration from these variables..*/
 	            	arrAcceleration = processAccelerometerMagnitude( Gdx.input.getAccelerometerX(), 
				 	            										  Gdx.input.getAccelerometerY(),
				 	            										  Gdx.input.getAccelerometerZ() );
 	            	Log.d("PulseManager", "Index: " + Integer.toString(nPulseIteration));
 	            	nPulseIteration++;
 					nPulseIteration%=500;
					
 					if (Thread.interrupted())
 						return;

 	            }	            
 	            
 	        }//end of background processing loop.
        } catch (Exception e){
    	}       
		 
	};
	
	public float getPulseIteration(){
		return (float)nPulseIteration;
	}
	
	public float[] getAccelerationValue(){
		return arrAcceleration;
	}

    public int getDebugIteration()
    {
    	return nDebugIteration;
    }
    
    /* Gather the accelerometer readings for x, y and z directions  */
	public float[] processAccelerometerMagnitude(float x, float y, float z)
    {
		float[] output;
		output = new float[500];
        
        output = FibLib.nativeProcessAccelerationInfo(x, y, z);
        
        try {
			wait(10);//Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //Log.d("SHARPNESS", "SHARPNESS " + Float.toString(output));
                
        return output;
    }
	public float processAccelerometerMagnitude2()
    {
		float output;
		float x = Gdx.input.getAccelerometerX(); 
		float y = Gdx.input.getAccelerometerY();
		float z = Gdx.input.getAccelerometerZ();
		
		output = (float) Math.sqrt((x)*(x) + (y)*(y) + (z)*(z));
        return output;        //return output;
    }
	
	   
	public void startAccelerometer()
    {
    	//Initialize Memory
		FibLib.initializationStepAcceleration(AreYouAlive.SCREEN_HEIGHT, AreYouAlive.SCREEN_WIDTH);
		
    	//initRunJNI();    	
     	
        //Stop waiting for processing.
        bWaitForProcessing   = false;

        //Signal that accelerometer can start.
        bAccelerometerIsInitialized = true;
        
	    //Start the processing of accelerometer data by running in a runnable. 
	    mThread.start();
        
    }

	public void finish(){
		stopMeasuring();
        mThread.interrupt();
	}

	public void stopMeasuring() {
        bWaitForProcessing   = true;
        nPulseIteration = 0;
        //Clear JNI structs.
        //nJniClearReturn=endRunJNI();
	}
	
	

    
}
