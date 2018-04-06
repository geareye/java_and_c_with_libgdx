package com.simpleprogrammer.nullapointershooter;



import android.os.Debug;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Gdx;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
 

public class VoiceManager implements Runnable
{
	private static final String TAG = "RUNNABLE_DEBUG_VOICE";	
	public  Thread mThread;
	
	private short[] arrShortMicBuffer; 
	private float[] arrFloatMicBuffer; 
	private short[] arrShortBaseline;

	
	
	public VoiceManager() {
		arrShortMicBuffer = new short[512];
		arrFloatMicBuffer = new float[512];
		arrShortBaseline  = new short[512];
		mThread = new Thread(this);
		mThread.start();
		this.bRecordBaseline = true;
 	}
	
 	private boolean bMicrophoneIsInitialized = false;
	private boolean bWaitForProcessing = true;
	private boolean bThreadRun;
	private boolean bRecordBaseline;

 	private AudioRecord recorder = null;
	private int N;
	
	 @Override
		public void run() 
		{
 		 	//Log.i(TAG, "Starting processing thread");
			bThreadRun = true;
		
			try {
	            while ((bThreadRun && !bMicrophoneIsInitialized) || bWaitForProcessing) 
	            {
	                synchronized (this) 
	                {
  	                    wait(100);  // wait 100 milliseconds before trying again.
	                    
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
	 	            	if(bRecordBaseline)
		 	   	      	{
		 	   		 		long t= System.currentTimeMillis();
		 	   		 		long end = t+5000;
		 	   		 		while(System.currentTimeMillis() < end) {
		 	   		 		  // do something
		 	   		 			N = recorder.read(arrShortBaseline, 0, arrShortBaseline.length);
		 	     		 		}
		 	   	      		
 		 	   	      		this.bRecordBaseline = false;
		 	   	      	}
	 	            	
	 	              N = recorder.read(arrShortMicBuffer, 0, arrShortMicBuffer.length);	 	              
	 	              arrFloatMicBuffer = getFftArray();	 	              
	 	              wait(10);

	 				  if (Thread.interrupted())
	 					return;
 	 	            }	            
	 	            
	 	        }//end of background processing loop.
	        } catch (Exception e){
    	}       
		 
};

//////////////////////////////////////////////////////////
//float for now, because the native side is expecting a float, but should be converted to short
public float[] getFftArray()
{
	float[] arrOutput;
	arrOutput = new float[256];		
	
	
	for (int i=0; i< arrShortMicBuffer.length; i++)
 		arrShortMicBuffer[i] = (short) (arrShortBaseline[i] - arrShortMicBuffer[i]);
		
	int intOutput = FibLib.nativeProcessFftOnShortArray(arrShortMicBuffer);
 	arrOutput = FibLib.nativeGetIntensityArrayFrequencyMagnitude();
	
	 try {
			wait(10);//Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

 	//Log.d("SHARPNESS", "SHARPNESS " + Float.toString(output));	                
    return arrOutput;
}

/////////////////////////////////////////
public void startMicrophone() {
    //Stop waiting for processing.
    bWaitForProcessing   = false;
   
     N = AudioRecord.getMinBufferSize(8000,AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_16BIT);

      recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                                 8000,
                                 AudioFormat.CHANNEL_IN_MONO,
                                 AudioFormat.ENCODING_PCM_16BIT,
                                 N*10);

      recorder.startRecording();

      //Signal that accelerometer can start.
      bMicrophoneIsInitialized = true;
}	 

public void finish(){
	stopMeasuring();
    mThread.interrupt();
}

 public void stopMeasuring() {
    bWaitForProcessing   = true;
 }
 
public short[] getMicBuffer(){
	return arrShortMicBuffer;
}

public float[] getFloatMicBuffer(){ 	
	return arrFloatMicBuffer;
	 
}

public float getFrequencyXAxis(int i) {
	float[] arrFreq = new float[] {0f,0.500000000000000f,1f,1.50000000000000f,2f,2.50000000000000f,3f,3.50000000000000f,4f,4.50000000000000f,5f,5.50000000000000f,6f,6.50000000000000f,7f,7.50000000000000f,8f,8.50000000000000f,9f,9.50000000000000f,10f,10.5000000000000f,11f,11.5000000000000f,12f,12.5000000000000f,13f,13.5000000000000f,14f,14.5000000000000f,15f,15.5000000000000f,16f,16.5000000000000f,17f,17.5000000000000f,18f,18.5000000000000f,19f,19.5000000000000f,20f,20.5000000000000f,21f,21.5000000000000f,22f,22.5000000000000f,23f,23.5000000000000f,24f,24.5000000000000f,25f,25.5000000000000f,26f,26.5000000000000f,27f,27.5000000000000f,28f,28.5000000000000f,29f,29.5000000000000f,30f,30.5000000000000f,31f,31.5000000000000f,32f,32.5000000000000f,33f,33.5000000000000f,34f,34.5000000000000f,35f,35.5000000000000f,36f,36.5000000000000f,37f,37.5000000000000f,38f,38.5000000000000f,39f,39.5000000000000f,40f,40.5000000000000f,41f,41.5000000000000f,42f,42.5000000000000f,43f,43.5000000000000f,44f,44.5000000000000f,45f,45.5000000000000f,46f,46.5000000000000f,47f,47.5000000000000f,48f,48.5000000000000f,49f,49.5000000000000f,50f,50.5000000000000f,51f,51.5000000000000f,52f,52.5000000000000f,53f,53.5000000000000f,54f,54.5000000000000f,55f,55.5000000000000f,56f,56.5000000000000f,57f,57.5000000000000f,58f,58.5000000000000f,59f,59.5000000000000f,60f,60.5000000000000f,61f,61.5000000000000f,62f,62.5000000000000f,63f,63.5000000000000f,64f,64.5000000000000f,65f,65.5000000000000f,66f,66.5000000000000f,67f,67.5000000000000f,68f,68.5000000000000f,69f,69.5000000000000f,70f,70.5000000000000f,71f,71.5000000000000f,72f,72.5000000000000f,73f,73.5000000000000f,74f,74.5000000000000f,75f,75.5000000000000f,76f,76.5000000000000f,77f,77.5000000000000f,78f,78.5000000000000f,79f,79.5000000000000f,80f,80.5000000000000f,81f,81.5000000000000f,82f,82.5000000000000f,83f,83.5000000000000f,84f,84.5000000000000f,85f,85.5000000000000f,86f,86.5000000000000f,87f,87.5000000000000f,88f,88.5000000000000f,89f,89.5000000000000f,90f,90.5000000000000f,91f,91.5000000000000f,92f,92.5000000000000f,93f,93.5000000000000f,94f,94.5000000000000f,95f,95.5000000000000f,96f,96.5000000000000f,97f,97.5000000000000f,98f,98.5000000000000f,99f,99.5000000000000f,100f,100.500000000000f,101f,101.500000000000f,102f,102.500000000000f,103f,103.500000000000f,104f,104.500000000000f,105f,105.500000000000f,106f,106.500000000000f,107f,107.500000000000f,108f,108.500000000000f,109f,109.500000000000f,110f,110.500000000000f,111f,111.500000000000f,112f,112.500000000000f,113f,113.500000000000f,114f,114.500000000000f,115f,115.500000000000f,116f,116.500000000000f,117f,117.500000000000f,118f,118.500000000000f,119f,119.500000000000f,120f,120.500000000000f,121f,121.500000000000f,122f,122.500000000000f,123f,123.500000000000f,124f,124.500000000000f,125f,125.500000000000f,126f,126.500000000000f,127f,127.500000000000f,128f,128.500000000000f,129f,129.500000000000f,130f,130.500000000000f,131f,131.500000000000f,132f,132.500000000000f,133f,133.500000000000f,134f,134.500000000000f,135f,135.500000000000f,136f,136.500000000000f,137f,137.500000000000f,138f,138.500000000000f,139f,139.500000000000f,140f,140.500000000000f,141f,141.500000000000f,142f,142.500000000000f,143f,143.500000000000f,144f,144.500000000000f,145f,145.500000000000f,146f,146.500000000000f,147f,147.500000000000f,148f,148.500000000000f,149f,149.500000000000f,150f,150.500000000000f,151f,151.500000000000f,152f,152.500000000000f,153f,153.500000000000f,154f,154.500000000000f,155f,155.500000000000f,156f,156.500000000000f,157f,157.500000000000f,158f,158.500000000000f,159f,159.500000000000f,160f,160.500000000000f,161f,161.500000000000f,162f,162.500000000000f,163f,163.500000000000f,164f,164.500000000000f,165f,165.500000000000f,166f,166.500000000000f,167f,167.500000000000f,168f,168.500000000000f,169f,169.500000000000f,170f,170.500000000000f,171f,171.500000000000f,172f,172.500000000000f,173f,173.500000000000f,174f,174.500000000000f,175f,175.500000000000f,176f,176.500000000000f,177f,177.500000000000f,178f,178.500000000000f,179f,179.500000000000f,180f,180.500000000000f,181f,181.500000000000f,182f,182.500000000000f,183f,183.500000000000f,184f,184.500000000000f,185f,185.500000000000f,186f,186.500000000000f,187f,187.500000000000f,188f,188.500000000000f,189f,189.500000000000f,190f,190.500000000000f,191f,191.500000000000f,192f,192.500000000000f,193f,193.500000000000f,194f,194.500000000000f,195f,195.500000000000f,196f,196.500000000000f,197f,197.500000000000f,198f,198.500000000000f,199f,199.500000000000f,200f,200.500000000000f,201f,201.500000000000f,202f,202.500000000000f,203f,203.500000000000f,204f,204.500000000000f,205f,205.500000000000f,206f,206.500000000000f,207f,207.500000000000f,208f,208.500000000000f,209f,209.500000000000f,210f,210.500000000000f,211f,211.500000000000f,212f,212.500000000000f,213f,213.500000000000f,214f,214.500000000000f,215f,215.500000000000f,216f,216.500000000000f,217f,217.500000000000f,218f,218.500000000000f,219f,219.500000000000f,220f,220.500000000000f,221f,221.500000000000f,222f,222.500000000000f,223f,223.500000000000f,224f,224.500000000000f,225f,225.500000000000f,226f,226.500000000000f,227f,227.500000000000f,228f,228.500000000000f,229f,229.500000000000f,230f,230.500000000000f,231f,231.500000000000f,232f,232.500000000000f,233f,233.500000000000f,234f,234.500000000000f,235f,235.500000000000f,236f,236.500000000000f,237f,237.500000000000f,238f,238.500000000000f,239f,239.500000000000f,240f,240.500000000000f,241f,241.500000000000f,242f,242.500000000000f,243f,243.500000000000f,244f,244.500000000000f,245f,245.500000000000f,246f,246.500000000000f,247f,247.500000000000f,248f,248.500000000000f,249f,249.500000000000f,250f,250.500000000000f,251f,251.500000000000f,252f,252.500000000000f,253f,253.500000000000f,254f,254.500000000000f,255f,255.500000000000f,256};
	return arrFreq[i];
	
}

	
}
