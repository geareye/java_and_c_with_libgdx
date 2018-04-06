/* DO NOT EDIT THIS FILE - it is machine generated , my ass :) */
#include <jni.h>
#include <stdio.h>
#include <stdint.h>
#include <math.h>
#include <string.h>

/* Header for class com_simpleprogrammer_nullapointershooter_FibLib */

#ifndef _Included_com_simpleprogrammer_nullapointershooter_FibLib
#define _Included_com_simpleprogrammer_nullapointershooter_FibLib
#ifdef __cplusplus
extern "C" {
#endif

#define FILTER_LEN 35
#define MAXROW 2001
#define MAXCOL 2001
#define PARAMETERS 512
#define HALF_PARAMETERS PARAMETERS >> 1
#define MAXACCELELEM 500
#define FILTER_BUFFER_SIZE 10
#define HISTOGRAM 258
#define USING_HISTOGRAM_FOR_SIGNAL 1
#define FS_IN	8000
//Define internal complex struct
typedef struct {
	float real;
	float imag;
} COMPLEX;

//Structs for Sharpness
typedef struct results_camera_struct_s
{
	int    hist[PARAMETERS];
	float  arrSharpnessPoints[PARAMETERS];
	float  arrFftPoints[PARAMETERS];
	float  arrOperationalData[PARAMETERS];
	float  arrFrequencyPoints[HALF_PARAMETERS];	
	int	   nCurrentIndex;
	float  nStd;
	float  nMean;
	double oldValue;
	double oldChange;
	int    nNumPulse;

}results_camera_struct_t;

//Structs for Accelerometer
typedef struct results_accelerometer_struct_s
{
	float arrAccelerometerPoints[MAXACCELELEM];
	float arrEstimateVar[PARAMETERS];
	float arrFilterBuffer[FILTER_BUFFER_SIZE];
	int	  nCurrentIndex;
	float nStd;
	float nMean;
	float nValOld;

	int   HEIGHT;
	int   WIDTH;

	float MIN_X_PLOT;
	float MAX_X_PLOT;
	float MIN_Y_PLOT;
	float MAX_Y_PLOT;

	float MIDPOINT;
	float RANGE;	//per sigma we get this pixel coordinates

	int nNumPluse;
	int nFilterBufferCount;

}results_accelerometer_struct_t;

//Structs for Accelerometer
typedef struct beats_per_minute_s
{
	uint8_t arrValues[MAXROW];

}beats_per_minute_t;

uint8_t abs_val(int8_t value);

/*
 * Class:     com_simpleprogrammer_nullapointershooter_FibLib
 * Method:    fibNR
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_com_simpleprogrammer_nullapointershooter_FibLib_fibNR
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_simpleprogrammer_nullapointershooter_FibLib
 * Method:    fibNI
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_com_simpleprogrammer_nullapointershooter_FibLib_fibNI
  (JNIEnv *, jclass, jlong);

/*
 */
JNIEXPORT jfloat JNICALL Java_com_simpleprogrammer_nullapointershooter_FibLib_nativeProcessSharpness
(JNIEnv *, jclass, jint, jint, jshortArray);

/*
*/
JNIEXPORT jint JNICALL Java_com_simpleprogrammer_nullapointershooter_FibLib_nativeProcessFftOnShortArray
(JNIEnv *env, jclass clazz, jshortArray arrShort);

JNIEXPORT jfloatArray JNICALL Java_com_simpleprogrammer_nullapointershooter_FibLib_nativeProcessAccelerationInfo
(JNIEnv *env, jclass clazz, jfloat x, jfloat y, jfloat z);

/*
 */
JNIEXPORT jint JNICALL Java_com_simpleprogrammer_nullapointershooter_FibLib_initializationStep
(JNIEnv *, jclass);

/*
 */
JNIEXPORT jint JNICALL Java_com_simpleprogrammer_nullapointershooter_FibLib_initializationStepAcceleration
(JNIEnv *, jclass, jint x, jint y);

//Getters
JNIEXPORT jfloat JNICALL Java_com_simpleprogrammer_nullapointershooter_FibLib_getStandardDev
(JNIEnv *, jclass);

JNIEXPORT jfloat JNICALL Java_com_simpleprogrammer_nullapointershooter_FibLib_getMean
(JNIEnv *, jclass);

JNIEXPORT jfloat JNICALL Java_com_simpleprogrammer_nullapointershooter_FibLib_getStandardDevAccelerometer
(JNIEnv *env, jclass clazz);

JNIEXPORT jfloat JNICALL Java_com_simpleprogrammer_nullapointershooter_FibLib_getMeanAccelerometer
(JNIEnv *env, jclass clazz);

#ifdef __cplusplus
}
#endif
#endif