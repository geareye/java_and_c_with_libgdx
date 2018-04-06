package com.simpleprogrammer.nullapointershooter.States;

import java.util.Arrays;
import java.util.Random;

import android.util.Log;

/*
 * Problems:
 * Image is oriented such that touch locations are flipped.
 * Sharpness needs plotting
 */






import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.simpleprogrammer.nullapointershooter.AreYouAlive;
import com.simpleprogrammer.nullapointershooter.CameraPulseManager;
import com.simpleprogrammer.nullapointershooter.FibLib;


public class FingerScanState extends State{

	private static final float BUTTON_X_COEFFICIENT = (float)0.6275f;
	private static final float BUTTON_Y_COEFFICIENT = (float)0.7083f;
	private static final float GRAPH_X0_COEFFICIENT = (float)0.125f;
	private static final float GRAPH_Y0_COEFFICIENT = (float)0.1875f;
	private static final float GRAPH_X1_COEFFICIENT = (float)0.525f;
	private static final float GRAPH_Y1_COEFFICIENT = (float)0.833f;
	private static final float GRAPH_MAX_Y 			= (float)1.5f;
	
	private static final float SCENE_WIDTH = 12.80f;
	private static final float SCENE_HEIGHT = 7.20f;
	
	private static final float FINGER_SCAN_BUTTON_X_MIN_BOUND = (float) 0.1875;
	private static final float FINGER_SCAN_BUTTON_X_MAX_BOUND = (float) 0.2979;
	
	private static final float BUTTON_Y_MIN_BOUND = (float) 0.1125;
	private static final float BUTTON_Y_MAX_BOUND = (float) 0.2125;
	
	
	private static float MIN_X_PLOT = (float) (0.1042f) * AreYouAlive.SCREEN_WIDTH;
	private static float MAX_X_PLOT = (float) (AreYouAlive.SCREEN_WIDTH - (0.1042f) * AreYouAlive.SCREEN_WIDTH);
	private static float MIN_Y_PLOT = (float) (0.3125f) * AreYouAlive.SCREEN_HEIGHT;
	private static float MAX_Y_PLOT = (float) AreYouAlive.SCREEN_HEIGHT - (0.0625f) * AreYouAlive.SCREEN_HEIGHT;
	
	private static float RANGE_PLOT = (MAX_X_PLOT - MIN_X_PLOT) / 6; //per sigma we get this pixel coordinates
	
	
	private ShapeRenderer shapeRenderer;
	private BitmapFont font;
	
	private Texture background;
	private float[] arrSharpnessValues;
	private static CameraPulseManager mPulseManager;
	private static float x1 = 0.0f;
	private static  float x2 = 0.0f;
	private static float y1 = 0.0f;
	private static  float y2 = 0.0f;
	
	//Number of steps in the plotting screen.
	float nStepY = (float) ((GRAPH_Y1_COEFFICIENT - GRAPH_Y0_COEFFICIENT) * AreYouAlive.SCREEN_HEIGHT);
	float nStepX = (float) ((GRAPH_X1_COEFFICIENT - GRAPH_X0_COEFFICIENT) * AreYouAlive.SCREEN_WIDTH);
	private boolean bDisposeCircles = false;
		
	private static float nSharpness = 0;
	private static float nPlotIndex = 0;

	private float[] arrFFTValues;
	
	protected FingerScanState(GameStateManager gsm) {
		super(gsm);
		Texture.setEnforcePotImages(false);
	
		arrFFTValues = new float[512];
		Arrays.fill(arrFFTValues,(MAX_X_PLOT - MIN_X_PLOT) / 2);

		shapeRenderer = new ShapeRenderer();
		font = new BitmapFont();
	    font.setColor(Color.RED);
	    
		//Set the viewing area and initialize bird, background and tube
		cam.setToOrtho(false, AreYouAlive.SCREEN_WIDTH, AreYouAlive.SCREEN_HEIGHT);
		
		mPulseManager = new CameraPulseManager();
		background = new Texture(Gdx.files.internal("data/fingerscan2.png"));
		
				
		//Start the pulse calculation.
		startPulseManager();
		
		arrSharpnessValues = new float[500];
		Arrays.fill(arrSharpnessValues,(MAX_X_PLOT - MIN_X_PLOT) / 2);
		//for (int x = -100; x < 100; ++x) {
		//	int i = (x + 100) * 2;
		//	debugFunction[i] = x;
		//	debugFunction[i + 1] = x * x;
		//	}
	}

	@Override
	protected void handleInput() 
	{		
		if (Gdx.input.justTouched())
		{
			// Report touch position.
			Vector3 touchPosition = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
			cam.unproject(touchPosition);
			
			//Log.d("Touch Position: X: %d" + touchPosition.x, "Touch Position y: %d" + touchPosition.y);		
				
			//If FingerScan is pressed while scan state is active, stop scanning and go back to menu state.
			if (( (touchPosition.x >= AreYouAlive.SCREEN_WIDTH * FINGER_SCAN_BUTTON_X_MIN_BOUND) && (touchPosition.x <= AreYouAlive.SCREEN_WIDTH * FINGER_SCAN_BUTTON_X_MAX_BOUND) ) && 
					( (touchPosition.y >= AreYouAlive.SCREEN_HEIGHT * BUTTON_Y_MIN_BOUND) && (touchPosition.y <= AreYouAlive.SCREEN_HEIGHT * BUTTON_Y_MAX_BOUND) ))
			{
				//Log.d("PRESSED","FINGER BUTTON");
				stopPulseManager();
			}
		}
	}

	@Override
	public void update(float dt) {
		int idx = 0;
		float fft = 0;
		
		handleInput();	
						
		//nSharpness = mPulseManager.getSharpnessValue();
		nPlotIndex = mPulseManager.getPulseIteration();
		
		arrFFTValues = mPulseManager.getFftArray();
		
		//Log.d("FingerScanState", "SHARPNESS " + Float.toString(nSharpness));
		//Log.d("FingerScanState", "Index " + Float.toString(nPlotIndex));
		
		//Map the sharpness value to the HEIGHT axis of the plot: 
		//This will allow the points to be dispersed proportional 
		//to the HEIGHT of the screen, instead of clustering the data
		//to a +/-nSharpness range. 
		//mapSharpnessValuesToPlotHeight(nSharpness,(int)nPlotIndex);
				
		//if (nPlotIndex == 512)
		//	Arrays.fill(arrFFTValues,(MAX_X_PLOT - MIN_X_PLOT) / 2);
			//Arrays.fill(arrSharpnessValues,(MAX_X_PLOT - MIN_X_PLOT) / 2);
		arrFFTValues[0]=0;
		for(idx=0; idx<512; idx++)
		{
			arrFFTValues[idx] = arrFFTValues[idx] % 500;
		}
		idx = find_max(arrFFTValues, 512, fft);
		
		Log.d("FFT RESULTS", "FFT RESULTS idx" + Integer.toString(idx)); 
		Log.d("FFT RESULTS", "FFT RESULTS fft" + Float.toString(arrFFTValues[idx]));
		
	}


	//returns location of max
	private int find_max(float[] pArray, int size, float val)
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

		val = max;
		return loc;
	}

	private void mapSharpnessValuesToPlotHeight(float nSharpness, int nIdx) {
		float sigma = FibLib.getStandardDev();//0f;//
		float mean  = FibLib.getMean();//0f;//
		float nValue = 0f; 
		float nMidPoint = (MAX_X_PLOT - MIN_X_PLOT) / 2;
		
		
		
		if (sigma < 0.59f)
			nValue = nMidPoint;
		else if (nSharpness < (mean - 3 * sigma))
		{
			nValue = MIN_X_PLOT;
		}
		else if (nSharpness > (mean + 3 * sigma))
		{
			nValue = MAX_X_PLOT;
		}
		else
		{
			if (nSharpness == mean)
			{
				nValue = nMidPoint;
			}
			else if (nSharpness < mean)
			{
				nValue = nMidPoint - RANGE_PLOT * ((mean - nSharpness)/sigma);				 
			}
			else //nSharpness > mean
			{
				nValue = nMidPoint + RANGE_PLOT * ((nSharpness - mean)/sigma);
			}
			
			nValue = (nValue > MAX_X_PLOT) ? MAX_X_PLOT : nValue;
			nValue = (nValue < MIN_X_PLOT) ? MIN_X_PLOT : nValue;
		}
			 
//		Random random = new Random();
		//int temp = random.nextInt(400) + 1;
		//Populate the sharpness array.		
		arrSharpnessValues[nIdx] = nValue; //temp
		
		
	

	}
	
	@Override
	public void render(SpriteBatch sb) {
		//int i;
		//float j;
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		sb.begin();
		sb.draw(background, 0, 0,  AreYouAlive.SCREEN_WIDTH, AreYouAlive.SCREEN_HEIGHT);
		font.draw(sb, Float.toString(arrFFTValues[(int) nPlotIndex]), 200, 200); 
		//font.draw(sb, Float.toString(arrSharpnessValues[(int) nPlotIndex]), 200, 200);//font.draw(sb, Integer.toString(mPulseManager.getDebugIteration()), 200, 200);		
		sb.end();
		
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.GREEN);
//
		for(int i=(int)MAX_Y_PLOT-256; i>(int)MIN_Y_PLOT - 1; i--)
		{
			//shapeRenderer.circle(arrSharpnessValues[i-(int)MIN_Y_PLOT], i, 4f, 30);
			shapeRenderer.line(arrFFTValues[(int)MAX_Y_PLOT - i], (int)MAX_Y_PLOT + 250 - i, arrFFTValues[(int)MAX_Y_PLOT-(i+1)], (int)MAX_Y_PLOT + 250 -(i+1));
			//shapeRenderer.line(arrSharpnessValues[i-(int)MIN_Y_PLOT], i, arrSharpnessValues[(i+1)-(int)MIN_Y_PLOT], i+1);//shapeRenderer.line(arrSharpnessValues[i-250], i, arrSharpnessValues[(i+1)-250], i+1);			
		}
//		for(int i=(int)MIN_Y_PLOT; i<(int)MAX_Y_PLOT - 1; i++)		//for(j=i=(int)MIN_Y_PLOT; i<(int)MAX_Y_PLOT; i++,j+=0.3)
//		{
//			//shapeRenderer.circle(arrSharpnessValues[i-(int)MIN_Y_PLOT], i, 4f, 30);
//			shapeRenderer.line(arrFFTValues[i-(int)MIN_Y_PLOT], i, arrFFTValues[(i+1)-(int)MIN_Y_PLOT], i+1);//shapeRenderer.line(arrSharpnessValues[i-250], i, arrSharpnessValues[(i+1)-250], i+1);
//			//shapeRenderer.line(arrSharpnessValues[i-(int)MIN_Y_PLOT], i, arrSharpnessValues[(i+1)-(int)MIN_Y_PLOT], i+1);//shapeRenderer.line(arrSharpnessValues[i-250], i, arrSharpnessValues[(i+1)-250], i+1);			
//		}
			
		
		//shapeRenderer.circle(this.x1, this.y1, 3, 30);		
		
		/*shapeRenderer.line(15, 730, 25, 720);		
		shapeRenderer.line(25, 720, 15, 700);
		shapeRenderer.line(15, 700, 25, 680);
		shapeRenderer.line(25, 680, 15, 660);*/
		
		shapeRenderer.end();
		
		/*//cam.update();
		shapeRenderer.setProjectionMatrix(cam.combined);
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.polyline(debugFunction);
		shapeRenderer.end();*/
		
		
	}

	@Override
	public void dispose() {
		shapeRenderer.dispose();
		font.dispose();
	}
	
	
	////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////
	
	private void startPulseManager()
	{
		mPulseManager.startCamera();
	}
	
	
	private void stopPulseManager()
	{
		mPulseManager.finish();
		gsm.set(new MenuState(gsm));
	}

}
