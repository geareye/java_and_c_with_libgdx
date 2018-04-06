#pragma once

#include <stdio.h>

// Include OpenCV's C++ Interface
// #include "opencv2/highgui/highgui_c.h"
// #include "opencv2/imgproc/imgproc_c.h"

#define MAXROW 800
#define MAXCOL 800
#define	BUFFER_LEN 512

//Structs for Sharpness
typedef struct
{
	float arrSharpnessPoints[BUFFER_LEN];
	int	  nCurentIndex;
	int	  nCurrentJavaSeconds;
	int	  bDataReady;
}SharpnessResults;

typedef struct
{
	uint8_t m_pImg[MAXROW][MAXCOL];
}ImageContainer;


//sharpness test kernels
static int m_kernelSharpnessGx[3][3] = {{1, 2, 1},{0,0,0},{-1,-2,-1}};
static int m_kernelSharpnessGy[3][3] = {{1, 0, -1},{2,0,-2},{1,0,-1}};

// //float 	SharpnessMeasure(Mat* I, uint8_t* Gx, uint8_t* Gy, uint8_t* g, uint8_t* pImg_t);
// float SharpnessMeasure(Mat& I, uint8_t* Gx, uint8_t* Gy, uint8_t* g, uint8_t* pImg_t);

// float get_sharpness(int height, int width, Mat& mImg, uint8_t* Gx, uint8_t* Gy, uint8_t* g, uint8_t* pImg_t);
// //void 	get_sharpness(int height, int width, Mat& mImg, float* sharpnessMeasure,
						// //Mat& Gx, Mat& Gy, Mat& g, Mat& pImg_t);
// //void 	gradient(int numRowsImg, int numColsImg,
						// //Mat& pImg, Mat& Gx,Mat& Gy, Mat& g, Mat& pImg_t);
// float 	testFunction(int pInt);
 void 	initRun();
// void 	endRun();
// void 	populateSharpnessBuffer(float nSharpness, int nCount, int nSeconds);
// int		getNumPulse(SharpnessResults* pResults, int nLength);
// int 	getSign(float nValue);
// int 	getSharpnessIndexFromJava();
// long 	getCurrentTimeInMillisec();
// void 	ClearSharpnessBuffer();

// //////////////////////////////////
// void 	reserveMemory();

//void 	removePepperNoise(Mat &mask);

