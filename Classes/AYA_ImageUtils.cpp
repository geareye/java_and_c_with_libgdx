#include "JniExample.h"

#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <time.h>

//#include<pthread.h>

#define  LOG_TAG    "libgl2jni"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

const int MEDIAN_BLUR_FILTER_SIZE = 3;
const int LAPLACIAN_FILTER_SIZE = 3;
const int EDGES_THRESHOLD = 80;

SharpnessResults *pResults;
ImageContainer *pImage;

void initRun()
{
	//Make space for the buffer.
	if (pResults == NULL)
	{
		
		pResults = new SharpnessResults;
		memset(pResults->arrSharpnessPoints,0,BUFFER_LEN);
		pResults->nCurentIndex = 0;
		pResults->nCurrentJavaSeconds = 0;
	}
	else
		

	//new for the image processing struct
	//if (pImage == NULL)
		//pImage = new ImageContainer;

}

// ////////////////////////////////////////////
// //THERE IS A BUG WITH ERASING THE MEMORY.
// void endRun()
// {
	// CCLOG("Dynamic memory erased.");

	// //if (pImage != NULL)
		// //delete pImage;
	// //if (pResults != NULL)
		// //delete pResults;
// }

// void ClearSharpnessBuffer()
// {
	// memset(pResults->arrSharpnessPoints,0,BUFFER_LEN);
// }

// int getSharpnessIndexFromJava()
// {
	// CCLOG("getSharpnessIndexFromJava().");
    // pthread_mutex_lock(&frameLocker);
	// return pResults->nCurentIndex;
    // pthread_mutex_unlock(&frameLocker);
// }


// void populateSharpnessBuffer(float nSharpness, int nCount, int nSeconds)
// {
	// CCLOG("populateSharpnessBuffer().");

    // pthread_mutex_lock(&frameLocker);
    // pResults->arrSharpnessPoints[nCount % BUFFER_LEN] = nSharpness;
	// pResults->nCurentIndex = nCount % BUFFER_LEN;
	// pResults->nCurrentJavaSeconds = nSeconds;
    // pthread_mutex_unlock(&frameLocker);

// }

// //float SharpnessMeasure(Mat* I, uint8_t* Gx, uint8_t* Gy, uint8_t* g, uint8_t* pImg_t)
// float SharpnessMeasure(Mat& I, uint8_t* Gx, uint8_t* Gy, uint8_t* g, uint8_t* pImg_t)
// {
	// int 			 i,j;
	// float			 nSharpnessMeasure;
	// int 			 temp;

	// int nRows = I.rows;
	// int nCols = I.cols;

	// nSharpnessMeasure = get_sharpness(nRows/4, nCols/4, I, Gx, Gy, g, pImg_t);

    // //CHANGE JAVA SO THAT IT SENDS index SO EACH SHARPNESS IS INDEXED INTO THE pResults array, and it's FFT is taken.
    // //pResults->arrSharpnessPoints[i] = nSharpnessMeasure;

	// return nSharpnessMeasure;
// }

// int GetCurrentJavaSeconds()
// {
	// CCLOG("GetCurrentJavaSeconds().");
	// return pResults->nCurrentJavaSeconds;
// }

// //////////////////////////////////
// //////////////////////////////////
// float get_sharpness(int nRows, int nCols, Mat& mImg, uint8_t* Gx, uint8_t* Gy, uint8_t* g, uint8_t* pImg_t)
// {
	// int i,j, temp = 0;
	// int m, n;
	// double x,y;
	// float tempSum=0;
	// float sum;
	// float sharpnessMeasure;

	// Mat sharpImage(nRows, nCols, CV_8UC1);
	// Mat product(nRows, nCols, CV_8UC1);
	// Mat tempImg(3,3,CV_8UC1);
	// Mat tempGx(3,3,CV_8UC1);
	// Mat tempGy(3,3,CV_8UC1);

	// for(i=0; i<nRows; i++)
	// {
		// //Get a pointer to the ith row
		// uchar *pRow = mImg.ptr(i);
		// tempSum += *(pRow + 19);
	// }

	// CCLOG("VALUE : %f", tempSum/nRows);
// /*
	// int k=0;
	// for(i=0; i<nRows; i++)
	// {
		// for(j=0; j<nCols; j++)
		// {
			// product.at<uchar>(i,j)=k++;
		// }
	// }

	// for(i=0; i<nRows; i++)
	// {
		// for(j=0; j<nCols; j++)
		// {
			// //==================
			// for(m=0; m<3; m++)
			// {
				// for(n=0; n<3; n++)
				// {
					// tempImg.at<uchar>(m,n)= mImg.at<uchar>(i+m,j+n);
				// }
			// }
			// //==================//==================
			// for(m=0; m<3; m++)
			// {
				// for(n=0; n<3; n++)
				// {
					// tempGx.at<uchar>(m,n)=tempImg.at<uchar>(m,n) * (uchar) m_kernelSharpnessGx[m][n];
				// }
			// }
			// for(m=0; m<3; m++)
			// {
				// for(n=0; n<3; n++)
				// {
					// tempSum+=(float) tempGx.at<uchar>(m,n);
				// }
			// }

			// x=tempSum;
			// //==================//==================
			// tempSum=0;
			// for(m=0; m<3; m++)
			// {
				// for(n=0; n<3; n++)
				// {
					// tempGy.at<uchar>(m,n)=tempImg.at<uchar>(m,n) * (uchar) m_kernelSharpnessGy[m][n];
				// }
			// }
			// for(m=0; m<3; m++)
			// {
				// for(n=0; n<3; n++)
				// {
					// tempSum+=tempGy.at<uchar>(m,n);
				// }
			// }

			// y=tempSum;
			// tempSum=0;
			// //==================//==================
			// sum = (x*x) + (y*y);
			// sum=sqrt(sum);
			// sharpImage.at<uchar>(i,j) = sum;
			// sum=0;
		// }
	// }

	// sharpnessMeasure=0;
	// for(i=0; i<nRows; i++)
	// {
		// for(j=0; j<nCols; j++)
		// {
			// sharpnessMeasure += sharpImage.at<uchar>(i,j);
		// }
	// }
	// sharpnessMeasure = sharpnessMeasure / (nRows*nCols);
// */
	// sharpImage.release();
	// product.release();
	// tempImg.release();
	// tempGx.release();
	// tempGy.release();

	// return sharpnessMeasure;
// }
// /*void get_sharpness(int height, int width, Mat& mImg, float* sharpnessMeasure, Mat& Gx, Mat& Gy, Mat& g, Mat& pImg_t)
// {
	// int i,j;
	// int m, n;
	// double x,y;
	// float tempSum=0;
	// float sum;
	// float tempImg[3][3];
	// float tempGx[3][3];
	// float tempGy[3][3];

	// int nRows =mImg.rows;
	// int nCols = mImg.cols;
	// Mat sharpImage(nRows, nCols, CV_8UC1);
	// Mat product(nRows, nCols, CV_8UC1);

	// for(i=0; i<height; i++)
	// {
		// for(j=0; j<width; j++)
		// {
			// //==================
			// for(m=0; m<3; m++)
			// {
				// for(n=0; n<3; n++)
				// {
					// tempImg[m][n]=mImg.at<uchar>(i+m,j+n);
				// }
			// }
			// //==================
			// for(m=0; m<3; m++)
			// {
				// for(n=0; n<3; n++)
				// {
					// tempGx[m][n]=tempImg[m][n] * m_kernelSharpnessGx[m][n];
				// }
			// }
			// for(m=0; m<3; m++)
				// for(n=0; n<3; n++)
					// tempSum+=tempGx[m][n];
			// x=tempSum;
			// tempSum=0;
			// for(m=0; m<3; m++)
			// {
				// for(n=0; n<3; n++)
				// {
					// tempGy[m][n]=tempImg[m][n] * m_kernelSharpnessGy[m][n];
				// }
			// }
			// for(m=0; m<3; m++)
				// for(n=0; n<3; n++)
					// tempSum+=tempGy[m][n];
			// y=tempSum;
			// tempSum=0;
			// sum = (x*x) + (y*y);
			// sum=sqrt(sum);
			// sharpImage.at<uchar>(i,j) = sum;
			// sum=0;
		// }
	// }

	// gradient(height,width,sharpImage,Gx,Gy,g,pImg_t);

	// for(i=0; i<height-2; i++)
	// {
		// for(j=0; j<width-2; j++)
		// {
			// product.at<uchar>(i,j) = (Gx.at<uchar>(i,j)*Gx.at<uchar>(i,j)) + (Gy.at<uchar>(i,j)*Gy.at<uchar>(i,j));
			// product.at<uchar>(i,j) = sqrt(product.at<uchar>(i,j));
		// }
	// }
	// sum=0;
	// for(i=0; i<height-3; i++)
	// {
		// for(j=0; j<width-4; j++)
			// sum+=product.at<uchar>(i,j);
	// }

	// //Free memory
	// sharpImage.release();
	// product.release();

	// //overall zone
	// *sharpnessMeasure = sum/((height-2)*(width-4));
// }

// //////////////////////////////////
// //////////////////////////////////
// void gradient(int numRowsImg, int numColsImg, Mat& pImg, Mat& Gx,Mat& Gy, Mat& g, Mat& pImg_t)
// {
	// int n,p;
	// int k,ndim,i;
	// int row, col;
	// ndim=2;

	// for(k=0; k<ndim; k++)
	// {
		// //X x Y
		// if(k == 0)
		// {
			// n=numRowsImg;
			// p=numColsImg;

			// //Take forward differences on left and right edges
			// for(i=0; i<numColsImg; i++)
				// g.at<uchar>(0,i) = pImg.at<uchar>(1,i) - pImg.at<uchar>(0,i);
			// for(i=0; i<numColsImg; i++)
				// g.at<uchar>(n,i) = pImg.at<uchar>(n,i) - pImg.at<uchar>(n-1,i);

			// for(row=2; row<n; row++)
			// {
				// for(col=0; col<p; col++)
				// {
					// g.at<uchar>(row-1,col)=(pImg.at<uchar>(row,col) - pImg.at<uchar>(row-2,col))/2;
				// }
			// }
			// //Obtain Gy
			// for(row=0; row<numRowsImg-2; row++)
			// {
				// for(col=0; col<numColsImg; col++)
				// {
					// Gy.at<uchar>(row,col)=g.at<uchar>(row,col);
				// }
			// }

			// //init. g[][] to zero
			// for(row=0; row<numRowsImg; row++)
				// for(col=0; col<numColsImg; col++)
					// g.at<uchar>(row,col)=0;
		// }
		// //Y x X
		// else
		// {
			// n=numColsImg;
			// p=numRowsImg;
			// //transpose image
			// for(row=0; row<numColsImg; row++)
			// {
				// for(col=0; col<numRowsImg; col++)
				// {
					// pImg_t.at<uchar>(row,col) = pImg.at<uchar>(col,row);
				// }
			// }

			// //Take forward differences on left and right edges
			// for(i=0; i<numColsImg; i++)
				// g.at<uchar>(0,i) = pImg_t.at<uchar>(1,i) - pImg_t.at<uchar>(0,i);
			// for(i=0; i<numColsImg; i++)
				// g.at<uchar>(n,i) = pImg_t.at<uchar>(n,i) - pImg_t.at<uchar>(n-1,i);

			// for(row=2; row<n; row++)
			// {
				// for(col=0; col<p; col++)
				// {
					// g.at<uchar>(row-1,col)=(pImg_t.at<uchar>(row,col) - pImg_t.at<uchar>(row-2,col))/2;
				// }
			// }
			// //tra	nspose image and Obtain Gx
			// for(row=0; row<p; row++)
			// {
				// for(col=0; col<n; col++)
				// {
					// Gx.at<uchar>(row,col) = g.at<uchar>(col,row);
				// }
			// }
		// }
	// }

// }*/

// float testFunction(int nIdx)
// {
	// SharpnessResults *pResults;
	// return pResults->arrSharpnessPoints[nIdx % BUFFER_LEN];
// }

// int getNumPulse(SharpnessResults* pResults, int nLength)
// {
	// float 	nSlopeChange = 0;
	// float 	nSlope;
	// int 	nUpdateSign = 0;
	// int 	nInitSign = 0;

	// //Get a pointer to data array.
	// float* pData = &pResults->arrSharpnessPoints[0];

	// for (int i=0; i<nLength - 1; i++)
	// {
		// nSlope = pResults->arrSharpnessPoints[i+1] - pResults->arrSharpnessPoints[i];
		// nInitSign = getSign(nSlope);

		 // if ((nInitSign == -1) || (nInitSign == 1))
		 // {
			// if (nInitSign*nUpdateSign == -1)
				// nSlopeChange++;

			// nUpdateSign = nInitSign;
		 // }
	// }


	// return (int) (nSlopeChange/2);
// }

// int getSign(float nValue)
// {
	// int sSign = 0;

	// if (nValue < 0)
		// sSign = -1;
	// else
		// sSign = 1;

	// return sSign;
// }

// long getCurrentTimeInMillisec()
// {
	// long currentTime;
	// auto timeInMillis = (long)std::time(nullptr); //returns long type
	// currentTime = (long) timeInMillis;

	// return currentTime;
// }


// /*
// //////////////////////////////////
// //////////////////////////////////
// void removePepperNoise(Mat &mask)
// {
    // // For simplicity, ignore the top & bottom row border.
    // for (int y=2; y<mask.rows-2; y++)
    // {
        // // Get access to each of the 5 rows near this pixel.
        // uchar *pThis = mask.ptr(y);
        // uchar *pUp1 = mask.ptr(y-1);
        // uchar *pUp2 = mask.ptr(y-2);
        // uchar *pDown1 = mask.ptr(y+1);
        // uchar *pDown2 = mask.ptr(y+2);

        // // For simplicity, ignore the left & right row border.
        // pThis += 2;
        // pUp1 += 2;
        // pUp2 += 2;
        // pDown1 += 2;
        // pDown2 += 2;
        // for (int x=2; x<mask.cols-2; x++)
        // {
            // uchar v = *pThis;   // Get the current pixel value (either 0 or 255).
            // // If the current pixel is black, but all the pixels on the 2-pixel-radius-border are white
            // // (ie: it is a small island of black pixels, surrounded by white), then delete that island.
            // if (v == 0)
            // {
                // bool allAbove = *(pUp2 - 2) && *(pUp2 - 1) && *(pUp2) && *(pUp2 + 1) && *(pUp2 + 2);
                // bool allLeft = *(pUp1 - 2) && *(pThis - 2) && *(pDown1 - 2);
                // bool allBelow = *(pDown2 - 2) && *(pDown2 - 1) && *(pDown2) && *(pDown2 + 1) && *(pDown2 + 2);
                // bool allRight = *(pUp1 + 2) && *(pThis + 2) && *(pDown1 + 2);
                // bool surroundings = allAbove && allLeft && allBelow && allRight;
                // if (surroundings == true) {
                    // // Fill the whole 5x5 block as white. Since we know the 5x5 borders
                    // // are already white, just need to fill the 3x3 inner region.
                    // *(pUp1 - 1) = 255;
                    // *(pUp1 + 0) = 255;
                    // *(pUp1 + 1) = 255;
                    // *(pThis - 1) = 255;
                    // *(pThis + 0) = 255;
                    // *(pThis + 1) = 255;
                    // *(pDown1 - 1) = 255;
                    // *(pDown1 + 0) = 255;
                    // *(pDown1 + 1) = 255;
                // }
                // // Since we just covered the whole 5x5 block with white, we know the next 2 pixels
                // // won't be black, so skip the next 2 pixels on the right.
                // pThis += 2;
                // pUp1 += 2;
                // pUp2 += 2;
                // pDown1 += 2;
                // pDown2 += 2;
            // }
            // // Move to the next pixel.
            // pThis++;
            // pUp1++;
            // pUp2++;
            // pDown1++;
            // pDown2++;
        // }
    // }
// }
// */

