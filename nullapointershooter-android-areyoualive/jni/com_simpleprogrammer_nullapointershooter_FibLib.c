#include "com_simpleprogrammer_nullapointershooter_FibLib.h"
#include "stddef.h"
#include <stdlib.h>
#include <stdio.h>
#include <android/log.h>
#include <unistd.h>

#define PRINT_LOG 0

//#include <complex.h>
#include <signal.h>

#define PI 3.141594
// ndk is under C:\Users\gyalcin\Downloads\android-ndk-r10e

#define APPNAME "SHARPNESS"
#define STANDARD_DEVIATION "STD"

#define MIN(a,b) (((a)<(b))?(a):(b))
#define MAX(a,b) (((a)>(b))?(a):(b))

////__android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "The value of 1 + 1 is %d", 1+1);

//sharpness test kernels
static int m_kernelSharpnessGx[3][3] = {{1, 2, 1},{0,0,0},{-1,-2,-1}};
static int m_kernelSharpnessGy[3][3] = {{1, 0, -1},{2,0,-2},{1,0,-1}};
static int m_kernelSharpness[3][3] = {{0, -1, 0},{-1,5,-1},{0,-1,0}};
static int quick_mask[3][3] =
{
	{ -1, 0, -1 },
	{ 0,  4,  0 },
	{ -1, 0, -1 }
};
static int embossFilter[3][3] =
{
	{ -4, -4,  0  },
	{ -4,  1,  4  },
	{ 0,   4,  4  }
};
static float fir1[] = {
		-0,8.31388256735844e-006,0.000695793127772279,-0.000718784565480336,-0.0025845191135456,0.00386210730312437,0.00422861012346586,-0.0108123855085727,-0.00269611409152577,0.021725537259672,-0.00625859251133658,-0.0351559361082957,0.0289898665082602,0.0483237418948895,-0.0821138993000567,-0.057976801726676,0.309722478839035,0.561521167973405,0.309722478839035,-0.0579768017266759,-0.0821138993000567,0.0483237418948895,0.0289898665082602,-0.0351559361082957,-0.00625859251133658,0.021725537259672,-0.00269611409152577,-0.0108123855085727,0.00422861012346586,0.00386210730312437,-0.0025845191135456,-0.000718784565480338,0.000695793127772279,8.31388256735844e-006,-0};
static float hhan[] = {
0,3.77965772740962e-005,0.000151180594771427,0.000340134910380874,0.000604630956796859,0.000944628745838338,0.00136007687449446,0.00185091253269609,0.00241706151281168,0.00305843822086654,0.00377494568948339,0.00456647559254264,0.00543290826155973,0.0063741127037773,0.00738994662196968,0.00848025643595607,0.00964487730581998,0.0108836331568305,0.0121963367060627,0.0135827894907121,0.0150427818980994,0.0165760931973613,0.0181824915728215,0.019861734159039,0.0216135670775249,0.0234377254751261,0.0253339335640674,0.0273019046636465,0.0293413412435765,0.0314519349689681,0.033633366746946,0.0358853067748912,0.0382074145903025,0.0405993391222701,0.0430607187445522,0.0455911813302485,0.0481903443080604,0.0508578147201305,0.0535931892814526,0.0563960544408427,0.0592659864434626,0.0622025513948853,0.0652053053266945,0.0682737942636061,0.0714075542921032,0.0746061116305735,0.0778689827009384,0.0811956742017641,0.0845856831828408,0.0880384971212229,0.091553593998715,0.0951304423807943,0.0987685014969555,0.102467221322469,0.106226042661535,0.110044397231829,0.113921707750418,0.117857388021034,0.121850843022703,0.125901468999704,0.130008653552846,0.134171775732054,0.138390206130253,0.14266330697852,0.146990432242509,0.151370927720124,0.15580413114042,0.160289372263735,0.164825972983019,0.169413247426352,0.174050502060644,0.17873703579648,0.183472140094124,0.188255099070633,0.193085189608094,0.197961681462944,0.20288383737638,0.207850913185816,0.212862157937393,0.217916813999513,0.223014117177384,0.228153296828549,0.233333575979408,0.238554171442674,0.243814293935788,0.249113148200246,0.254449933121827,0.259823841851719,0.265234061928494,0.270679775400948,0.276160158951759,0.281674384021968,0.287221616936238,0.292801019028898,0.29841174677074,0.304052951896545,0.309723781533331,0.315423378329296,0.321150880583437,0.326905422375828,0.332686133698534,0.338492140587147,0.344322565252915,0.350176526215451,0.356053138436006,0.361951513451266,0.367870759507683,0.373809981696295,0.379768282088018,0.385744759869409,0.391738511478851,0.397748630743159,0.403774209014585,0.40981433530819,0.415868096439573,0.421934577162933,0.42801286030944,0.434102026925899,0.440201156413684,0.446309326667918,0.452425614216887,0.45854909436165,0.464678841315846,0.470813928345655,0.476953427909915,0.483096411800347,0.489241951281889,0.49538911723311,0.501536980286678,0.507684610969869,0.513831079845091,0.5199754576504,0.526116815439995,0.532254224724658,0.538386757612132,0.544513486947405,0.550633486452881,0.556745830868423,0.56284959609124,0.568943859315596,0.575027699172326,0.581100195868139,0.587160431324672,0.593207489317293,0.599240455613625,0.605258418111759,0.611260466978157,0.617245694785205,0.623213196648401,0.629162070363163,0.635091416541232,0.641000338746643,0.646887943631258,0.652753341069825,0.658595644294553,0.664413970029181,0.670207438622517,0.675975174181427,0.68171630470326,0.687429962207682,0.693115282867903,0.698771407141278,0.704397479899253,0.709992650556653,0.715556073200279,0.721086906716794,0.726584314919893,0.73204746667672,0.737475536033525,0.742867702340536,0.748223150376032,0.75354107046959,0.7588206586245,0.764061116639314,0.769261652228528,0.774421479142359,0.779539817285623,0.784615892835666,0.789648938359361,0.794638192929131,0.799582902237994,0.804482318713598,0.809335701631252,0.814142317225904,0.818901438803083,0.823612346848765,0.828274329138148,0.832886680843338,0.837448704639904,0.841959710812305,0.84641901735817,0.850825950091399,0.855179842744098,0.859480037067308,0.86372588293052,0.867916738419968,0.87205196993568,0.876130952287266,0.880153068788438,0.884117711350248,0.888024280573021,0.891872185836974,0.895660845391512,0.899389686443183,0.903058145242268,0.906665667168023,0.910211706812523,0.913695728063121,0.917117204183505,0.920475617893327,0.923770461446416,0.927001236707534,0.930167455227694,0.933268638318005,0.936304317122042,0.93927403268673,0.942177336031735,0.945013788217338,0.947782960410804,0.95048443395121,0.95311780041274,0.955682661666441,0.958178629940404,0.960605327878401,0.962962388596925,0.965249455740667,0.967466183536386,0.969612236845188,0.971687291213196,0.973691032920598,0.97562315902908,0.977483377427628,0.979271406876687,0.980986977050685,0.9826298285789,0.984199713084672,0.985696393222957,0.987119642716209,0.988469246388591,0.989745000198504,0.990946711269438,0.992074197919133,0.993127289687043,0.99410582736011,0.995009662996835,0.995838659949645,0.99659269288555,0.997271647805093,0.997875422059586,0.998403924366628,0.998857074823906,0.999234804921275,0.999537057551115,0.999763787016967,0.99991495904044,0.999990550766394,0.999990550766394,0.99991495904044,0.999763787016967,0.999537057551115,0.999234804921275,0.998857074823906,0.998403924366628,0.997875422059586,0.997271647805093,0.99659269288555,0.995838659949645,0.995009662996835,0.99410582736011,0.993127289687043,0.992074197919133,0.990946711269438,0.989745000198504,0.988469246388591,0.987119642716209,0.985696393222957,0.984199713084672,0.9826298285789,0.980986977050685,0.979271406876687,0.977483377427628,0.97562315902908,0.973691032920598,0.971687291213196,0.969612236845189,0.967466183536386,0.965249455740667,0.962962388596925,0.960605327878401,0.958178629940405,0.955682661666441,0.95311780041274,0.95048443395121,0.947782960410804,0.945013788217338,0.942177336031735,0.93927403268673,0.936304317122042,0.933268638318005,0.930167455227694,0.927001236707534,0.923770461446416,0.920475617893328,0.917117204183505,0.913695728063121,0.910211706812523,0.906665667168023,0.903058145242268,0.899389686443183,0.895660845391512,0.891872185836974,0.888024280573021,0.884117711350248,0.880153068788438,0.876130952287266,0.87205196993568,0.867916738419968,0.86372588293052,0.859480037067308,0.855179842744099,0.850825950091399,0.84641901735817,0.841959710812305,0.837448704639904,0.832886680843338,0.828274329138148,0.823612346848765,0.818901438803083,0.814142317225904,0.809335701631252,0.804482318713599,0.799582902237994,0.794638192929131,0.789648938359361,0.784615892835666,0.779539817285623,0.77442147914236,0.769261652228528,0.764061116639314,0.7588206586245,0.753541070469591,0.748223150376032,0.742867702340537,0.737475536033525,0.73204746667672,0.726584314919893,0.721086906716794,0.715556073200279,0.709992650556654,0.704397479899253,0.698771407141278,0.693115282867903,0.687429962207682,0.68171630470326,0.675975174181427,0.670207438622517,0.664413970029181,0.658595644294553,0.652753341069825,0.646887943631258,0.641000338746643,0.635091416541232,0.629162070363163,0.6232131966484,0.617245694785205,0.611260466978157,0.605258418111759,0.599240455613625,0.593207489317293,0.587160431324672,0.58110019586814,0.575027699172326,0.568943859315596,0.56284959609124,0.556745830868423,0.550633486452881,0.544513486947404,0.538386757612133,0.532254224724658,0.526116815439995,0.5199754576504,0.513831079845091,0.507684610969869,0.501536980286678,0.49538911723311,0.489241951281889,0.483096411800347,0.476953427909915,0.470813928345655,0.464678841315846,0.458549094361651,0.452425614216887,0.446309326667919,0.440201156413684,0.434102026925899,0.42801286030944,0.421934577162933,0.415868096439574,0.40981433530819,0.403774209014585,0.397748630743159,0.391738511478851,0.385744759869409,0.379768282088018,0.373809981696295,0.367870759507684,0.361951513451266,0.356053138436005,0.350176526215452,0.344322565252915,0.338492140587147,0.332686133698534,0.326905422375828,0.321150880583437,0.315423378329297,0.309723781533332,0.304052951896545,0.29841174677074,0.292801019028898,0.287221616936238,0.281674384021968,0.276160158951759,0.270679775400947,0.265234061928494,0.259823841851719,0.254449933121828,0.249113148200246,0.243814293935788,0.238554171442674,0.233333575979408,0.228153296828549,0.223014117177384,0.217916813999514,0.212862157937393,0.207850913185816,0.20288383737638,0.197961681462944,0.193085189608094,0.188255099070633,0.183472140094124,0.17873703579648,0.174050502060644,0.169413247426353,0.164825972983019,0.160289372263735,0.15580413114042,0.151370927720124,0.146990432242509,0.14266330697852,0.138390206130252,0.134171775732054,0.130008653552846,0.125901468999705,0.121850843022703,0.117857388021034,0.113921707750418,0.110044397231829,0.106226042661535,0.102467221322469,0.0987685014969555,0.0951304423807942,0.091553593998715,0.0880384971212229,0.0845856831828409,0.081195674201764,0.0778689827009387,0.0746061116305735,0.0714075542921033,0.0682737942636061,0.0652053053266948,0.0622025513948854,0.0592659864434625,0.0563960544408427,0.0535931892814527,0.0508578147201306,0.0481903443080604,0.0455911813302486,0.0430607187445523,0.0405993391222702,0.0382074145903025,0.0358853067748913,0.0336333667469461,0.031451934968968,0.0293413412435765,0.0273019046636465,0.0253339335640675,0.0234377254751261,0.0216135670775249,0.0198617341590389,0.0181824915728216,0.0165760931973613,0.0150427818980994,0.0135827894907122,0.0121963367060628,0.0108836331568306,0.00964487730581992,0.00848025643595618,0.00738994662196968,0.00637411270377736,0.00543290826155973,0.00456647559254275,0.00377494568948344,0.00305843822086654,0.00241706151281168,0.00185091253269615,0.00136007687449446,0.000944628745838338,0.000604630956796859,0.000340134910380874,0.000151180594771427,3.77965772740962e-005,0
};
#define filterWidth 8
#define filterHeight 1
double filter[filterHeight][filterWidth] =
{
   -1/8, -1/8, -1/8, -1/8, 8/8, -1/8, -1/8, -1/8
};

double factor = 1.0;
double bias = 0.0;

static struct
{
    uint16_t                 results_reply_pad;
results_camera_struct_t      results;
} static_memory_camera;

static struct
{
    uint16_t                 			   results_reply_pad;
    results_accelerometer_struct_t         results;
} static_memory_accelerometer;

static jlong fib(jlong n)
{
	//if n is less than or eq. to 0 return 0, if n is 1 return 1, otherwise return  fibJR(n - 1) + fibJR(n - 2)
	return n <= 0? 0 : n == 1? 1 : fib(n - 1) + fib(n - 2);
}

static float logMag(COMPLEX p) {
    return 20 * (float) log10( sqrt( p.real * p.real + p.imag * p.imag ) );
}

/*Square Root*/
static uint32_t testSqrt(uint32_t y)
{
	uint32_t    x_old;
	uint32_t    x_new;
	uint32_t    testy;
	uint32_t    nbits;
	uint32_t    i;

	if (0 == y)
	{
		return (0);
	}

	/* select a good starting value using binary logarithms: */
	nbits = (4 * 8);    /*  */
	for (i = 4, testy = 16;; i += 2, testy <<= 2)
	{
		if (i >= nbits || y <= testy)
		{
			x_old = (1 << (i / 2));       /* x_old = sqrt(testy) */
			break;
		}
	}

	/* x_old >= sqrt(y) */
	/* use the Babylonian method to arrive at the integer square root: */
	for (;;)
	{
		x_new = (y / x_old + x_old) / 2;
		if (x_old <= x_new)
		{
			break;
		}
		x_old = x_new;
	}

	/* make sure that the answer is right: */
	if (x_old * x_old > y || (x_old + 1) * (x_old + 1) <= y)
	{
		return (0);
	}

	return (x_old);
}

static jint linspace(float a, float b, int n, float *u)
{
    double c;
    int i;

    /* step size */
    c = (b - a)/(n - 1);

    /* fill vector */
    for(i = 0; i < n - 1; ++i)
        u[i] = a + i*c;

    /* fix last entry to b */
    u[n - 1] = b;

    for (i=0; i<n; i++)
    	u[i] = (FS_IN/2) * u[i];

    /* done */
    return 1;
}

static void quicksort(float *arr, int low, int high)
{
  float pivot, i, j, temp;
  if(low < high) {
    pivot = low; // select a pivot element
    i = low;
    j = high;
    while(i < j) {
      // increment i till you get a number greater than the pivot element
      while(arr[(int)i] <= arr[(int)pivot] && i <= high)
        i++;
      // decrement j till you get a number less than the pivot element
      while(arr[(int)j] > arr[(int)pivot] && j >= low)
        j--;
      // if i < j swap the elements in locations i and j
      if(i < j) {
        temp = arr[(int)i];
        arr[(int)i] = arr[(int)j];
        arr[(int)j] = temp;
      }
    }

    // when i >= j it means the j-th position is the correct position
    // of the pivot element, hence swap the pivot element with the
    // element in the j-th position
    temp = arr[(int)j];
    arr[(int)j] = arr[(int)pivot];
    arr[(int)pivot] = temp;
    // Repeat quicksort for the two sub-arrays, one to the left of j
    // and one to the right of j
    quicksort(arr, low, j-1);
    quicksort(arr, j+1, high);
  }
}

//returns location of max
static int find_max(float* pArray, int size, float *val)
{
	float max;
	int i, loc = 0;

	max = pArray[0];
	for (i = 1; i<size; i++)
	{
		if (pArray[i] > max)
		{
			max = pArray[i];
			loc = i;
		}
	}

	*val = pArray[loc];
	return loc;
}

static jfloat get_median(float *arr)
{
	float nRet = 0;
	quicksort(arr,0,FILTER_BUFFER_SIZE - 1);
	nRet = arr[FILTER_BUFFER_SIZE/2];

	return nRet;
}


static void populate_histogram(int8_t *image, int S_ROW, int S_COL, int *bins)
{
	int row, col, minlim, maxlim, i, k=0;
	int count = 0;
	int8_t temp;

	for (i = 0; i<HISTOGRAM; i++)
	{
		if (i == 0)
		{
			minlim = -1000;
			maxlim = 0;
		}
		else if (i == HISTOGRAM - 1)
		{
			minlim = i;
			maxlim = 1000;
		}
		else
		{
			minlim = i;
			maxlim = i + 1;
		}

		for (row = 0; row<S_ROW; row++)
		{
			for (col = 0; col<S_COL; col++)
			{
				temp = image[k++];

				if (temp >= minlim && temp<maxlim)
					count += 1;
			}
		}

		bins[i] = count;
		count = 0;
	}
}

static float sum(int * arr, int firstindex, int lastindex)
{
	int i;
	float tempsum = 0.0;

	for (i = firstindex; i<lastindex; i++)
	{
		tempsum += (float) arr[i];
	}

	return tempsum;
}

static float calculate_signal(int * bins)
{
	float SIGNAL;
	float signalmin, signalmax, signalsum_min, percent, signalsum_max;
	int i;

	percent = 0.1;

	signalmin = 0;
	signalsum_min = 0;
	for (i = 1; i<HISTOGRAM; i++)
	{
		if (signalsum_min == 0)
		{
			if (sum(bins, 0, i) >= 0.5*percent*sum(bins, 0, HISTOGRAM))
				signalmin = i;
			else
				signalmin = 0;
		}
		else
			signalmin = 0;

		signalsum_min += signalmin;
	}

	signalmax = 0;
	signalsum_max = 0;
	for (i = 1; i<HISTOGRAM; i++)
	{
		if (signalsum_max == 0)
		{
			if (sum(bins, 0, i) >= (1 - 0.5*percent)*sum(bins, 0, HISTOGRAM))
				signalmax = i;
			else
				signalmax = 0;
		}
		else
			signalmax = 0;

		signalsum_max += signalmax;

		}
		SIGNAL = (float)signalsum_max - signalsum_min;

		return SIGNAL;
}

COMPLEX w_struct[PARAMETERS];
static void init_w_struct()
{
	int k, n = PARAMETERS;
	float a = 2 * PI/n;

	for (k=0; k<n; k++){
		w_struct[k].real 	  = cosf(k*a);
		w_struct[k].imag	  = sinf(k*a);
	}
}

//complex float w_cpx[PARAMETERS];
//static void init_w_cpx()
//{
//	int k = 0;
//	int n = PARAMETERS;
//	float a = 2* PI/n;//

	//for(k=0; k<n; k++){
		//w_cpx[k] = cosf(k*a) + 1i * sinf(k*a);
	//}

//#if 0
//	float complex a,b,c;
//	__real__(a) = 1.0;
//	__imag__(a) = 2.0;
//	__real__(b) = 2.0 * __imag__(a);
//	__imag__(b) = 3.0;
//	c = conj(b);////

	//return (int) __real__(c);
//#endif
//}

static jfloat get_moving_average(float *arr)
{
	float nSum = 0;
	float nRet = 0;
	int i;
	for(i=0; i<FILTER_BUFFER_SIZE; i++)
		nSum+=arr[i];
	nRet = nSum / FILTER_BUFFER_SIZE;
	return nRet;
}


static jfloat std_dev(jfloat *pData, jint size)
{
	jfloat mean=0.0;
	jfloat sum_deviation=0.0;
	jint i;
	for(i=0; i<size;++i)
	{
		mean += pData[i];
	}
	mean=mean/size;
	for(i=0; i<size;++i)
		sum_deviation += (pData[i] - mean)*(pData[i] - mean);

	return (float) sqrt(sum_deviation/size);
}

static jfloat find_mean(jfloat* pData, jint size)
{
	jint i;
	jfloat tempsum=0.0;
	for(i=0; i<size; i++)
	{
		tempsum+=pData[i];
	}

	return (jfloat) (tempsum/(jfloat)size);
}

//remember to free memory in case of callocing
static void fft_org(COMPLEX *x, int m)
{
    static COMPLEX *w;           /* used to store the w complex array */
    static int mstore = 0;       /* stores m for future reference */
    static int n = 1;            /* length of fft stored for future */

    COMPLEX u,temp,tm;
    COMPLEX *xi,*xip,*xj,*wptr;

    int i,j,k,l,le,windex;

    double arg,w_real,w_imag,wrecur_real,wrecur_imag,wtemp_real;

    if(m != mstore) {

/* free previously allocated storage and set new m */

        if(mstore != 0) free(w);
        mstore = m;
        if(m == 0)
        	{
        	__android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "RETURNED");
        		return;       /* if m=0 then done */
        	}

/* n = 2**m = fft length */
         n = 1 << m;
        le = n/2;

/* allocate the storage for w */
         w = (COMPLEX *) calloc(le-1,sizeof(COMPLEX));
        if(!w) {
        	__android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "Unable to allocate complex W array");
            exit(1);
        }

/* calculate the w values recursively */
         arg = 4.0*atan(1.0)/le;         /* PI/le calculation */
        wrecur_real = w_real = cos(arg);
        wrecur_imag = w_imag = -sin(arg);
        xj = w;
        for (j = 1 ; j < le ; j++) {
            xj->real = (float)wrecur_real;
            xj->imag = (float)wrecur_imag;
            xj++;
            wtemp_real = wrecur_real*w_real - wrecur_imag*w_imag;
            wrecur_imag = wrecur_real*w_imag + wrecur_imag*w_real;
            wrecur_real = wtemp_real;
        }
    }

/* start fft */

    le = n;
    windex = 1;
    for (l = 0 ; l < m ; l++) {
        le = le/2;

/* first iteration with no multiplies */

        for(i = 0 ; i < n ; i = i + 2*le) {
            xi = x + i;
            xip = xi + le;
            temp.real = xi->real + xip->real;
            temp.imag = xi->imag + xip->imag;
            xip->real = xi->real - xip->real;
            xip->imag = xi->imag - xip->imag;
            *xi = temp;
        }

/* remaining iterations use stored w */

        wptr = w + windex - 1;
        for (j = 1 ; j < le ; j++) {
            u = *wptr;
            for (i = j ; i < n ; i = i + 2*le) {
                xi = x + i;
                xip = xi + le;
                temp.real = xi->real + xip->real;
                temp.imag = xi->imag + xip->imag;
                tm.real = xi->real - xip->real;
                tm.imag = xi->imag - xip->imag;
                xip->real = tm.real*u.real - tm.imag*u.imag;
                xip->imag = tm.real*u.imag + tm.imag*u.real;
                *xi = temp;
            }
            wptr = wptr + windex;
        }
        windex = 2*windex;
    }

/* rearrange data by bit reversing */

    j = 0;
    for (i = 1 ; i < (n-1) ; i++) {
        k = n/2;
        while(k <= j) {
            j = j - k;
            k = k/2;
        }
        j = j + k;
        if (i < j) {
            xi = x + i;
            xj = x + j;
            temp = *xj;
            *xj = *xi;
            *xi = temp;
        }
    }
}


static void fft(COMPLEX *x, int n)
{
	COMPLEX *w;

	COMPLEX u, temp, tm;
	COMPLEX *xi, *xip, *xj, *wptr;

	int i, j, k, l, le, windex,m;

	for (m = 0; (1 << m) < n; m++);
	w = &w_struct[0];

	windex = 1;

	le = n;
	for(l = 0; l < m; l++)
	{
		le /= 2;
		for(i=0; i<n; i=i+2*le)
		{//Even Sequence is handled here
			xi = x + i;
			xip = xi + le;

			temp.real = xi->real + xip->real;
			temp.imag = xi->imag + xip->imag;

			xip->real = xi->real - xip->real;
			xip->imag = xi->imag - xip->imag;

			*xi = temp;
		}
		 wptr = w + windex - 1;
		      for (j = 1; j < le; j++)
		      {//Odd Sequence is handled here
		         u = *wptr;
		         for (i = j; i < n; i += 2 * le)
		         {
		            xi = x + i;
		            xip = xi + le;
		            temp.real = xi->real + xip->real;
		            temp.imag = xi->imag + xip->imag;
		            tm.real = xi->real - xip->real;
		            tm.imag = xi->imag - xip->imag;
		            xip->real = tm.real * u.real - tm.imag * u.imag;
		            xip->imag = tm.real * u.imag + tm.imag * u.real;
		            *xi = temp;
		         }
		         wptr += windex;
		      }
		      windex *= 2;
		   }

		//Reverse the order
		   j = 0;
		   for (i = 1; i < n - 1; i++)
		   {
		      for (k = n / 2; k <= j; k /= 2)
		         j -= k;
		      j += k;
		      if (i < j)
		      {
		         xi = x + i;
		         xj = x + j;
		         temp = *xj;
		         *xj = *xi;
		         *xi = temp;
		      }
		   }
}

#define MAGNITUDE_CONSTANT_SHIFT 100
static jint apply_fft(float *datain, float *dataout)
{
	int i, j, k;
	float nScale = 1.0f /(float)10;
	float *pData = &static_memory_camera.results.arrSharpnessPoints[0];

 	COMPLEX *tempsig;

	//Allocate data for the sample and copy from the quantized image stream
 	tempsig = (COMPLEX *) calloc(PARAMETERS, sizeof(COMPLEX));

    /* Transfer data to complex datatype */
	for(i = 0; i<PARAMETERS; i++)
	{
		tempsig[i].real = datain[i];
		tempsig[i].imag = 0;
	}

	//FFT on samples
	fft_org(tempsig,9);


	for(j=0; j<PARAMETERS; j++)
	{
		pData[j] = MAGNITUDE_CONSTANT_SHIFT + logMag(tempsig[j]);
		dataout[j] = pData[j];
		//__android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "data[j] %f", data[j]);
	}



//
//	dataout[0] = pData[0];
//	dataout[1] = (pData[0] + pData[1])/2;
//	for (j=1 , i=1; i<PARAMETERS / 2; i++, j++)
//	{
//		dataout[2*i]     = pData[2*i - 1];
//		dataout[2*i + 1] = (pData[j] + pData[j+1])/2;
//	}

  	free(tempsig);

	return 42;
	////__android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "complex arithmetic %d", tempfunc());
}


static jint setup_fft(float *datain, float *dataout)
{
	int i, j, k;
	COMPLEX *filtered;
	COMPLEX *tempsig;
	int ovrlap=60;
	float nScale = 1.0f /(float)PARAMETERS;
	float filt = 1.0/(PARAMETERS/10);

	//Allocate data for the sample and copy from the quantized image stream
	filtered = (COMPLEX *) calloc(PARAMETERS, sizeof(COMPLEX));
	tempsig = (COMPLEX *) calloc(PARAMETERS, sizeof(COMPLEX));

	//copy data into local signal variable
	for(i = 0; i<PARAMETERS; i++)
		tempsig[i].real = datain[i];

	//Copy filter into array and scale by 1/N
	for(i = 0; i<FILTER_LEN; i++)
		filtered[i].real = filt * fir1[i];

	//FFT on zero filled filter response
	fft(filtered,PARAMETERS);

	//FFT on samples
	fft(tempsig,PARAMETERS);


	for (i=0; i<PARAMETERS; i++) {
		filt = tempsig[i].real * filtered[i].real - tempsig[i].imag * filtered[i].imag;
		tempsig[i].real = tempsig[i].real * filtered[i].real - tempsig[i].imag * filtered[i].imag;
		tempsig[i].imag = filt;
	}

	//Inverse FFT on the multiplied sequence
	fft(tempsig,PARAMETERS);

	for(j=0; j<PARAMETERS; j++)
	{
		dataout[j] = tempsig[j].imag;
		dataout[j] += 50;
		//__android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "data[j] %f", data[j]);
	}

	free(filtered);
	free(tempsig);

	return 42;
	////__android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "complex arithmetic %d", tempfunc());


#if 0

		COMPLEX u, temp, tm;
		COMPLEX *xi, *xip, *wptr;
		int i, j, le, windex;
		int n = PARAMETERS;
		windex = 1;

		COMPLEX *x;
		//x->real = data;
		//x->imag = 0;

		init_w_struct();

		for (le = n/2; le>0; le/=2) {
			wptr = w_struct;
			for (j = 0; j<le; j++) {
				u = *wptr;
				for (i=j; i<n; i=i+2*le) {
					xi = x + i;
					xip = xi + le;
					temp.real = xi->real + xip->real;
					temp.imag = xi->imag + xip->imag;
					tm.real = xi->real + xip-> real;
					tm.imag = xi->imag + xip->imag;
					xip->real = tm.real*u.real - tm.imag*u.imag;
					xip->imag =tm.real*u.imag + tm.imag*u.real;
					*xi = temp;
				}
				wptr = wptr + windex;
			}
			windex = 2*windex;
		}

	return 0;




	/* Sub-image */
	int32_t nCutWidth, nCutHeight, nNumRows, nNumCols;
	int32_t i_max, i_min, j_max, j_min;
	int filterX, filterY;
	int x, y;
	int tempsum = 0;
	float temp = 0.0;
	int w = width;
	int h = height;

	nCutWidth  = (int32_t)(w * (0/100));
	nCutHeight = (int32_t)(h * (0/100));
	nNumRows = height - 2 * nCutHeight;
	nNumCols = width - 2 * nCutWidth;
	j_max = h - nCutHeight;
	j_min = nCutHeight;
	i_max = w - nCutWidth;
	i_min = nCutWidth;

	for(x = i_min; x < i_max; x++)
	for(y = j_min; y < j_max; y++)  {

	double red = 0.0;

    //multiply every value of the filter with corresponding image pixel
    for(filterY = 0; filterY < filterHeight; filterY++)
    for(filterX = 0; filterX < filterWidth; filterX++)
    {
      int imageX = (x - filterWidth / 2 + filterX + w) % w;
      int imageY = (y - filterHeight / 2 + filterY + h) % h;
      red += image[imageY * w + imageX] * filter[filterY][filterX];

    }

    tempsum += MIN(MAX(factor * red + bias,0),255);
  }

	temp = ((float)tempsum)/((float)h*w);

	//Assign value to array that is used for std and and mean
	static_memory_camera.results.arrSharpnessPoints[static_memory_camera.results.nCurrentIndex % PARAMETERS] = temp;
	static_memory_camera.results.nStd = std_dev(&static_memory_camera.results.arrSharpnessPoints[0], PARAMETERS);
	static_memory_camera.results.nMean = find_mean(&static_memory_camera.results.arrSharpnessPoints[0], PARAMETERS);
	static_memory_camera.results.nCurrentIndex++;


	return temp;
#endif
}

static jfloat calculateContrastMeasure(int8_t *image, uint32_t height, uint32_t width)
{
	////__android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "complex arithmetic %d", tempfunc());

	/* Sub-image */
	int32_t nCutWidth, nCutHeight, nNumRows, nNumCols;
	int32_t i_max, i_min, j_max, j_min;
	int filterX, filterY;
	int x, y, i , j;
	uint32_t tempsum = 0;
	float temp = 0.0;
	int w = width;
	int h = height;
	float *psharpnessdata = &static_memory_camera.results.arrSharpnessPoints[0];
	float *pfftdata = &static_memory_camera.results.arrFftPoints[0];
	int   *phist = &static_memory_camera.results.hist[0];

//	nCutWidth  = 0;
//	nCutHeight = 0;

	nCutWidth  = (int32_t)(w * (30/100));
	nCutHeight = (int32_t)(h * (30/100));
	nNumRows = height - 2 * nCutHeight;
	nNumCols = width - 2 * nCutWidth;
	j_max = h - nCutHeight;
	j_min = nCutHeight;
	i_max = w - nCutWidth;
	i_min = nCutWidth;

#if 0 //Convolution w/ kernel
	int f;
	int32_t r, c, a, b, rows, cols;
	int32_t gx, gy;
	uint32_t gxx, gyy;
	int32_t  sum;
	int32_t * pimg;

	uint32_t nTotal,sum_unsigned = 0;

	pimg = (int32_t *) calloc(height*width, sizeof(int32_t));

	gxx = 0;
	gyy = 0;

	rows = height;
	cols = width;
	//	1) Convolve test image with the quick_mask kernel and
	//store results in pimg
	for (r = 1; r<rows - 1; r++)
	{
		for (c = 1; c<cols - 1; c++)
		{
			sum = 0;
			for (a = -1; a<2; a++) {
				for (b = -1; b<2; b++) {
					sum = sum + image[(r + a)*cols + (c + b)] * quick_mask[a + 1][b + 1];
				}
			}

			pimg[r * cols + c] = sum;
		}
	}

 	for (r = 2; r < rows - 2; ++r) {
			for (c = 2; c < cols - 2; ++c) {

				gx = (pimg[r*rows + c + 1] - pimg[r*rows + c - 1]);
				gy = (pimg[(r + 1)*rows + c] - pimg[(r - 1)*rows + c] );

				sum = testSqrt((uint32_t)(gx*gx) + (uint32_t)(gy*gy));

				image[r*cols + c] = sum;
			}
		}


	gx = 0;
	gxx = 0;
	nTotal = 0;

	for (r = 3; r<rows-3; r++)
	{
		f = r * rows + 3;
		for (c = 3; c <= cols-3; c++, f++)
		{
			nTotal = image[f] + nTotal;
		}
	}

	temp = (float)(nTotal) /(float)((rows-3)*(cols-3));
	__android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "CALCULATION: %f", temp);


free(pimg);

#endif


#if 1 //Cheap way to find brightness

	uint32_t count = 0;
	for(i =0; i<height; i++)
	{
		tempsum = 0;
		for (j=0; j<width; j++) {
			tempsum += abs_val(image[count++]);
		}
		temp += tempsum/width;
	}
	temp /= height;
	__android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "Brightness: %f", temp);

#endif

#if 0 //single line filter method to find brightness


	for(x = i_min; x < i_max; x++)
	for(y = j_min; y < j_max; y++)  {

	double red = 0.0;

    //multiply every value of the filter with corresponding image pixel
    for(filterY = 0; filterY < filterHeight; filterY++)
    for(filterX = 0; filterX < filterWidth; filterX++)
    {
      int imageX = (x - filterWidth / 2 + filterX + w) % w;
      int imageY = (y - filterHeight / 2 + filterY + h) % h;
      red += image[imageY * w + imageX] * filter[filterY][filterX];
    }

    tempsum += MIN(MAX(factor * red + bias,0),255);
  }

	temp = ((float)tempsum)/((float)h*w);
#endif

	//Assign value to array that is used for std and and mean
	static_memory_camera.results.arrSharpnessPoints[static_memory_camera.results.nCurrentIndex % PARAMETERS] = temp;
	static_memory_camera.results.arrFftPoints[static_memory_camera.results.nCurrentIndex % PARAMETERS] = temp;

	//Obtain the FFT of the stream
	x = setup_fft(psharpnessdata,pfftdata);

	static_memory_camera.results.nStd = std_dev(&static_memory_camera.results.arrSharpnessPoints[0], PARAMETERS);
	static_memory_camera.results.nMean = find_mean(&static_memory_camera.results.arrSharpnessPoints[0], PARAMETERS);
	static_memory_camera.results.nCurrentIndex++;



	//ee 253
	//i=0;
	//temp=0.0;
	//i = find_max(&static_memory_camera.results.arrFftPoints[0], PARAMETERS,&temp);
	//__android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "FFT: value: %f", temp);
	//__android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "FFT: index: %d", i);
	//Get the fft




	//__android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "FFT ATTEMPT %d", 2);

	return temp;
}


#if 0
//Filter Image using kernels
static jfloat filterImage(int8_t *pImage, uint32_t height, uint32_t width)
{
	uint32_t i,j;
	uint32_t m, n;
	int32_t x,y;
	int32_t tempSum=0;
	int32_t sum;

	int8_t *pTempPixel;

	float sharpnessMeasure;
	float temp;
	int8_t  tempImg[3][3];
	int32_t tempGx[3][3];
	int32_t tempGy[3][3];

	/* Sub-image */
	int32_t nCutWidth, nCutHeight, nNumRows, nNumCols;
	int32_t i_max, i_min, j_max, j_min;
	nCutWidth  = (int32_t)(width * (40/100));
	nCutHeight = (int32_t)(height * (40/100));
	nNumRows = height - 2 * nCutHeight;
	nNumCols = width - 2 * nCutWidth;
	i_max = height - nCutHeight;
	i_min = nCutHeight;
	j_max = width - nCutWidth;
	j_min = nCutWidth;

	sharpnessMeasure=0;
	for(i = i_min; i < i_max; i++)
	{
		for(j = j_min; j < j_max; j++)
		{

			pTempPixel =  &pImage[i * (nNumRows) + nNumCols];

			//==================
			for(m=0; m<3; m++)
			{
				for(n=0; n<3; n++)
				{
					tempImg[m][n] = *pTempPixel++;//tempImg[m][n] =  *pImage++;
				}
			}

			//==================
			for(m=0; m<3; m++)
			{
				for(n=0; n<3; n++)
				{
					tempGx[m][n]= (int32_t)tempImg[m][n] * m_kernelSharpnessGx[m][n];
				}
			}

			for(m=0; m<3; m++)
			{
				for(n=0; n<3; n++)
				{
					tempSum += tempGx[m][n];
				}
			}

			////__android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "tempsum x %d", tempSum);

			x=tempSum;
			tempSum=0;
			for(m=0; m<3; m++)
			{
				for(n=0; n<3; n++)
				{
					tempGy[m][n]=(int32_t)tempImg[m][n] * m_kernelSharpnessGy[m][n];
				}
			}
			for(m=0; m<3; m++)
			{
				for(n=0; n<3; n++)
				{
					tempSum+=tempGy[m][n];
				}
			}

			////__android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "tempsum y %d", tempSum);

			y=tempSum;
			tempSum=0;
			sum = (jint) ((double)(x*x) + (double)(y*y));
			sum = (jint) sqrt((double) sum);
			//static_memory_camera.images.mSharpImage[i][j] = (int8_t) sum;

			////__android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "sum %d", sum);

			sharpnessMeasure += (float) sum;
			sum=0;
		}
	}

	sharpnessMeasure = sharpnessMeasure / (height * width);

	//Assign value to array that is used for std and and mean
	static_memory_camera.results.arrSharpnessPoints[static_memory_camera.results.nCurrentIndex % PARAMETERS] = sharpnessMeasure;
	static_memory_camera.results.nStd = std_dev(&static_memory_camera.results.arrSharpnessPoints[0], PARAMETERS);
	static_memory_camera.results.nMean = find_mean(&static_memory_camera.results.arrSharpnessPoints[0], PARAMETERS);
	static_memory_camera.results.nCurrentIndex++;


	return sharpnessMeasure;
}
#endif

// Example
JNIEXPORT jlong JNICALL Java_com_simpleprogrammer_nullapointershooter_FibLib_fibNR
  (JNIEnv *env, jclass clazz, jlong n) {

	return fib(n);
}
// Example
JNIEXPORT jlong JNICALL Java_com_simpleprogrammer_nullapointershooter_FibLib_fibNI
(JNIEnv *env, jclass clazz, jlong n) {

	jlong previous = -1;
	jlong results = 1;
	jlong i;
	for(i=0; i < n; i++)
	{
		jlong sum = results + previous;
		previous = results;
		results = sum;
	}

	return results;
}

//Initialize
JNIEXPORT jint JNICALL Java_com_simpleprogrammer_nullapointershooter_FibLib_initializationStep
(JNIEnv *env, jclass clazz)
{
	uint8_t  *p;
	uint32_t i;
	p = (uint8_t *)&static_memory_camera.results_reply_pad;

	for(i=0; i<sizeof(static_memory_camera); i++)
	{
		p[i] = 0;
	}

	//__android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "camera process initialized %d", 666);

	init_w_struct();


	return 0;
}

//Initialize
JNIEXPORT jint JNICALL Java_com_simpleprogrammer_nullapointershooter_FibLib_initializationStepAcceleration(JNIEnv *env, jclass clazz, jint height, jint width)
{
	uint8_t  *p;
	uint32_t i;
	p = (uint8_t *)&static_memory_accelerometer.results_reply_pad;

	for(i=0; i<sizeof(static_memory_accelerometer); i++)
	{
		p[i] = 0;
	}

	static_memory_accelerometer.results.HEIGHT = height;
	static_memory_accelerometer.results.WIDTH  = width;

	static_memory_accelerometer.results.MIN_X_PLOT = (0.1042) * static_memory_accelerometer.results.WIDTH;
	static_memory_accelerometer.results.MAX_X_PLOT = (static_memory_accelerometer.results.WIDTH - (0.1042) * static_memory_accelerometer.results.WIDTH);
	static_memory_accelerometer.results.MIN_Y_PLOT = (0.3125) * static_memory_accelerometer.results.HEIGHT;
	static_memory_accelerometer.results.MAX_Y_PLOT = static_memory_accelerometer.results.HEIGHT - (0.0625) * static_memory_accelerometer.results.HEIGHT;

	static_memory_accelerometer.results.MIDPOINT = (static_memory_accelerometer.results.MAX_X_PLOT - static_memory_accelerometer.results.MIN_X_PLOT) / 2;
	static_memory_accelerometer.results.RANGE = (static_memory_accelerometer.results.MAX_X_PLOT - static_memory_accelerometer.results.MIN_X_PLOT) / 4; //per sigma we get this pixel coordinates

	for(i=0; i<MAXACCELELEM; i++)
	{
		static_memory_accelerometer.results.arrAccelerometerPoints[i] = static_memory_accelerometer.results.MIDPOINT;
	}

	return 0;
}

JNIEXPORT jint JNICALL Java_com_simpleprogrammer_nullapointershooter_FibLib_nativeProcessFftOnShortArray
(JNIEnv *env, jclass clazz, jshortArray arrShort)
{
	jint nRetValue;
	jint i, j;
	nRetValue = 42;
	jfloat tempfilt, p1, p2;

	float *pfftdata = &static_memory_camera.results.arrFftPoints[0];
	float *pOperationalData = &static_memory_camera.results.arrOperationalData[0];
	float *pFreqPoints = &static_memory_camera.results.arrFrequencyPoints[0];

	/* Get a hold of the java array and populate the member image array with it */
	jshort* pIncomingData = (*env)->GetShortArrayElements(env,arrShort, NULL);
	for (i=0; i<PARAMETERS; i++)
		pOperationalData[i] = (jfloat) pIncomingData[i];

	nRetValue = apply_fft(pOperationalData,pfftdata);
	if (1 != linspace(0,1,256,pFreqPoints))
		exit(-1);

	//copy the index and FFT results to the same array and index into the middle of it in Java layer to properly plot.
	//1st half contains fft and 2nd half has the indices
	pfftdata[0] = 0;
	for (i=HALF_PARAMETERS; i<PARAMETERS; i++)
		pfftdata[i] = pFreqPoints[i - (HALF_PARAMETERS)];

	// interpolate the peak
	tempfilt = pfftdata[0];
	i=0;
	for (j=1; j<HALF_PARAMETERS; j++)
	{
		if(pfftdata[j] > tempfilt)
		{
			tempfilt = pfftdata[j];
			i=j;
		}
	}

	//peak location
	p1 = pfftdata[i] - pfftdata[i - 1];
	p2 = pfftdata[i] - pfftdata[i + 1];

	//assign location to 0th index
	pfftdata[0] = round((float)i + (p1 - p2)/(2*(p1+p2+1e-30)));

 	return nRetValue;
}

JNIEXPORT jfloat JNICALL Java_com_simpleprogrammer_nullapointershooter_FibLib_nativeProcessSharpness
(JNIEnv *env, jclass clazz, jint width, jint height, jbyteArray yuv)
{
	/////////////////////////
	//Deneme:
	double alpha  = 0.95;
	int    period = 20;
	double change = 0.0;
	/////////////////////////

	jint i,j, h, w;

	jfloat nRetValue = 0;
	int8_t nTempPixel = 0;
	uint32_t x;
	uint32_t y;
	jfloat sum = 0;

	h = height;
	w = width;

	height = 100;
	width = 100;

	int8_t *pStimulusData	= (int8_t*)malloc(height * width * sizeof(int8_t));

	/* Get a hold of the java array and populate the member image array with it */
	jbyte* pImage = (*env)->GetByteArrayElements(env,yuv, NULL);
	memcpy(&pStimulusData[0], (int8_t *)pImage, height * width * sizeof(int8_t));

#if PRINT_LOG
	FILE *pFile = fopen("sdcard/DCIM/images/frames.csv", "a+");

		fprintf(pFile,"\n----\n");
		x=0;
		for(i = 0; i<w; i++)
		{
			for(j = 0; j<h; j++)
			{
				fprintf(pFile,"%d,",pImage[x++]);
			}
			fprintf(pFile,"\n");
		}

		fflush(pFile);
		fclose(pFile);
#endif

#if 0
	//Allocate 2D array
	int8_t **arr2DImage = NULL;
	int8_t **arr2DFiltered = NULL;
	arr2DImage = (int8_t **)malloc(height * sizeof(int8_t *));
	arr2DFiltered = (int8_t **)malloc(height * sizeof(int8_t *));
	/* Allocate second dimension */
	for (i = 0; i < width; i++)
	{
		arr2DImage[i] = (int8_t *)malloc(width * sizeof(int8_t));
		arr2DFiltered[i] = (int8_t *)malloc(width * sizeof(int8_t));
	}
#endif

	/* Process the image */
	//nRetValue = filterImage(&pStimulusData[0],250,250);
	nRetValue = calculateContrastMeasure(&pStimulusData[0],100,100);

	/////////////////////////
	//Deneme:
	double value = alpha * static_memory_camera.results.oldValue + (1 - alpha) * nRetValue;
	change = value - static_memory_camera.results.oldValue;
	if (change < 0.0 && static_memory_camera.results.oldChange > 0.0)
		static_memory_camera.results.nNumPulse++;

 	////__android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "static_memory_camera.results.nNumPulse++ %d", static_memory_camera.results.nNumPulse);
 	////__android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "static_memory_camera.results.oldChange %f", static_memory_camera.results.oldChange);
 	////__android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "nRetValue %f", nRetValue);


 	static_memory_camera.results.oldValue = value;
 	static_memory_camera.results.oldChange = change;

	/////////////////////////


#if 0
	FILE *pFile = fopen("sdcard/DCIM/SharpnessValues.txt", "a");

	fflush(pFile);
	fclose(pFile);
#endif

	//nRetValue = filterImage(pStimulusData,height, width);
		(*env)->ReleaseByteArrayElements(env,yuv,pImage,0); /* abort to not copy back ontent	 */
		free(pStimulusData);
#if 0
		//Free memory
		for (i = 0; i < width; i++)
		{
			free(arr2DFiltered[i]);
			free(arr2DImage[i]);
		}
		free(arr2DImage);
		free(arr2DFiltered);
#endif


	return nRetValue;
}

JNIEXPORT jfloatArray JNICALL Java_com_simpleprogrammer_nullapointershooter_FibLib_nativeGetIntensityArrayFrequencyMagnitude
(JNIEnv *env, jclass clazz)
{
	jfloatArray result;
	result = (*env)->NewFloatArray(env, PARAMETERS);
	if (result == NULL) {
		return NULL; /* out of memory error thrown */
	}

	(*env)->SetFloatArrayRegion(env, result, 0, PARAMETERS, static_memory_camera.results.arrFftPoints);
	return result;
}


JNIEXPORT jfloatArray JNICALL Java_com_simpleprogrammer_nullapointershooter_FibLib_nativeProcessAccelerationInfo
(JNIEnv *env, jclass clazz, jfloat x, jfloat y, jfloat z)
{
	jfloatArray result;
	 result = (*env)->NewFloatArray(env, MAXACCELELEM);
	 if (result == NULL) {
	     return NULL; /* out of memory error thrown */
	 }

	    float nAcceleration, value;
	    float nSigma, nMean, nMidpoint, nRange;

	    int* nFilterIdx = &static_memory_accelerometer.results.nFilterBufferCount;
	    nMidpoint  = static_memory_accelerometer.results.MIDPOINT;
	 	nRange 	   = static_memory_accelerometer.results.RANGE;
	 	nSigma     = static_memory_accelerometer.results.nStd;
	 	nMean      = static_memory_accelerometer.results.nMean;

	 	//__android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "static_memory_accelerometer.results.nStd %f", static_memory_accelerometer.results.nStd);

	 	/*In order to gain enough sensitivity, multiply by 10.*/
	 	x=x*10;
	 	y=y*10;
	 	z=z*10;
	 	nAcceleration = sqrt((x * x) + (y * y) + (z * z));

	 	if (nSigma < 0.4f)
	 		value = nMidpoint;
	 	else if (nAcceleration < (nMean - 3 * nSigma))
	 	{
	 		value = static_memory_accelerometer.results.MIN_X_PLOT;
	 	}
	 	else if (nAcceleration > (nMean + 3 * nSigma))
	 	{
	 		value = static_memory_accelerometer.results.MAX_X_PLOT;
	 	}
	 	else
	 	{
	 		if (nAcceleration == nMean)
	 			value = nMidpoint;
	 		else //there has been enough movement to create a variance that is within 3sigma, may be a heartbeat.
	 		{
	 			static_memory_accelerometer.results.nNumPluse++;
	 			if (nAcceleration < nMean)
	 				value = nMidpoint - nRange * ((nMean - nAcceleration) / nSigma);
	 			else
	 				value = nMidpoint + nRange * ((nAcceleration - nMean) / nSigma);
	 		}

	 		value = (value > static_memory_accelerometer.results.MAX_X_PLOT) ? static_memory_accelerometer.results.MAX_X_PLOT : value;
	 		value = (value < static_memory_accelerometer.results.MIN_X_PLOT) ? static_memory_accelerometer.results.MIN_X_PLOT : value;
	 	}



	 	//Populate the filter array and sort & pick median.
	 	static_memory_accelerometer.results.arrFilterBuffer[*nFilterIdx % FILTER_BUFFER_SIZE] = value;
	 	if ( (*nFilterIdx % FILTER_BUFFER_SIZE) == 0)
		{
	 		value = get_moving_average(static_memory_accelerometer.results.arrFilterBuffer);
	 		value = get_median(static_memory_accelerometer.results.arrFilterBuffer);
	 		//Populate the vibration array.
			static_memory_accelerometer.results.arrAccelerometerPoints[static_memory_accelerometer.results.nCurrentIndex] = value;
		 	//__android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "static_memory_accelerometer.results.nNumPluse %d", static_memory_accelerometer.results.nNumPluse);
		 	//__android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "value %f", value);


			//Assign value to array that is used for std and and mean
			static_memory_accelerometer.results.nStd = std_dev(&static_memory_accelerometer.results.arrEstimateVar[0], PARAMETERS);
			static_memory_accelerometer.results.nMean = find_mean(&static_memory_accelerometer.results.arrEstimateVar[0], PARAMETERS);
			static_memory_accelerometer.results.arrEstimateVar[static_memory_accelerometer.results.nCurrentIndex % PARAMETERS] = nAcceleration;
			static_memory_accelerometer.results.nCurrentIndex = (static_memory_accelerometer.results.nCurrentIndex + 1) % MAXACCELELEM;
		}
	 	*nFilterIdx = *nFilterIdx + 1;





		//sleep(0.000001);


	  (*env)->SetFloatArrayRegion(env, result, 0, MAXACCELELEM, static_memory_accelerometer.results.arrAccelerometerPoints);
	  return result;




	//jfloat distance = 0;

	//distance = calculateAccelerometerMeasure(x, y, z);

	//	return distance;
}

static int convolve2D(float* in, float* out, int signalSizeX, int signalSizeY,
	float* kernel, int kernelSizeX, int kernelSizeY)
{
	int i, j, m, n;
	float *inPtr, *inPtr2, *outPtr, *kPtr;
	int kCenterX, kCenterY;
	int rowMin, rowMax;                             // to check boundary of input array
	int colMin, colMax;                             //

	// check validity of params
	if (!in || !out || !kernel)
		return 0;
	if (signalSizeX <= 0 || kernelSizeX <= 0)
		return 0;

	// find center position of kernel (half of kernel size)
	kCenterX = kernelSizeX >> 1;
	kCenterY = kernelSizeY >> 1;

	// init working  pointers
	inPtr = inPtr2 = &in[signalSizeX * kCenterY + kCenterX];  // note that  it is shifted (kCenterX, kCenterY),
	outPtr = out;
	kPtr = kernel;

	// start convolution
	for (i = 0; i < signalSizeY; ++i)                   // number of rows
	{
		// compute the range of convolution, the current row of kernel should be between these
		rowMax = i + kCenterY;
		rowMin = i - signalSizeY + kCenterY;

		for (j = 0; j < signalSizeX; ++j)              // number of columns
		{
			// compute the range of convolution, the current column of kernel should be between these
			colMax = j + kCenterX;
			colMin = j - signalSizeX + kCenterX;

			*outPtr = 0;                            // set to 0 before accumulate

			// flip the kernel and traverse all the kernel values
			// multiply each kernel value with underlying input data
			for (m = 0; m < kernelSizeY; ++m)        // kernel rows
			{
				// check if the index is out of bound of input array
				if (m <= rowMax && m > rowMin)
				{
					for (n = 0; n < kernelSizeX; ++n)
					{
						// check the boundary of array
						if (n <= colMax && n > colMin)
							*outPtr += *(inPtr - n) * *kPtr;
						++kPtr;                     // next kernel
					}
				}
				else
					kPtr += kernelSizeX;            // out of bound, move to next row of kernel

				inPtr -= signalSizeX;                 // move input data 1 raw up
			}

			kPtr = kernel;                          // reset kernel to (0,0)
			inPtr = ++inPtr2;                       // next input
			++outPtr;                               // next output
		}
	}
	return 1;
}

uint8_t abs_val(int8_t value)
{
	uint8_t nResult;
	uint8_t temp = value >> ((sizeof(value) * 8) - 1);     // make a mask of the sign bit
	value ^= temp;                   // toggle the bits if value is negative
	value += temp & 1;               // add one if value was negative

	nResult = value;
	return nResult;
}


////////////////////////////////////////////////////////////////
//Getters
JNIEXPORT jfloat JNICALL Java_com_simpleprogrammer_nullapointershooter_FibLib_getStandardDev(JNIEnv *env, jclass clazz)
{
	return static_memory_camera.results.nStd;
}

JNIEXPORT jfloat JNICALL Java_com_simpleprogrammer_nullapointershooter_FibLib_getMean(JNIEnv *env, jclass clazz)
{
	return static_memory_camera.results.nMean;
}
JNIEXPORT jfloat JNICALL Java_com_simpleprogrammer_nullapointershooter_FibLib_getStandardDevAccelerometer(JNIEnv *env, jclass clazz)
{
	return static_memory_accelerometer.results.nStd;
}

JNIEXPORT jfloat JNICALL Java_com_simpleprogrammer_nullapointershooter_FibLib_getMeanAccelerometer(JNIEnv *env, jclass clazz)
{
	return static_memory_accelerometer.results.nMean;
}
//////////////////////////////////////////////////////////////////
