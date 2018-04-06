package com.simpleprogrammer.nullapointershooter;

public class FibLib {
	
	public static long fibJR(long n)
	{
		//if n is less than or eq. to 0 return 0, if n is 1 return 1, otherwise return  fibJR(n - 1) + fibJR(n - 2)
		return n <= 0? 0 : n == 1? 1 : fibJR(n - 1) + fibJR(n - 2);  
	}
	
	//body of this function does not exist, it will be provided natively.
	//Similar to having an abstract class and somebody else will implement it.	
	public native static long fibNR(long n);  
	
	//Itertarive approach:
	public static long fibJI(long n)
	{
		long previous = -1;
		long results = 1;
		for(long i=0; i < n; i++){
			long sum = results + previous;
			previous = results;
			results = sum;
		}
		
		return results;
	}
	
	public native static long fibNI(long n); 
	
	public native static float nativeProcessSharpness(int width, int height, byte[] yuv);
	public native static int   nativeProcessFftOnShortArray(short[] arrMicBuffer);
	
	public native static float[] nativeProcessAccelerationInfo(float x, float y, float z);	//public native static float nativeProcessAccelerationInfo(float x, float y, float z);
	public native static float[] nativeGetIntensityArrayFrequencyMagnitude();
	public native static int initializationStepAcceleration(int x, int y);
	public native static int initializationStep();
	
	public native static float getStandardDev();
	public native static float getMean();
	

	public native static float getStandardDevAccelerometer();
	public native static float getMeanAccelerometer();
	
	static {
		System.loadLibrary("com_simpleprogrammer_nullapointershooter_FibLib");
	}
}
