package com.simpleprogrammer.nullapointershooter.States;

import java.util.Arrays;

import sun.nio.cs.ext.MacCentralEurope;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.simpleprogrammer.nullapointershooter.AccelerometerPulseManager;
import com.simpleprogrammer.nullapointershooter.AreYouAlive;
import com.simpleprogrammer.nullapointershooter.FibLib;

public class BodyScanState extends State{

	private static AccelerometerPulseManager mPulseManager;
	
	private Texture background;

	private static float nPulseIdx;

	private static float MIN_X_PLOT = (float) (0.1042f) * AreYouAlive.SCREEN_WIDTH;
	private static float MAX_X_PLOT = (float) (AreYouAlive.SCREEN_WIDTH - (0.1042f) * AreYouAlive.SCREEN_WIDTH);
	private static float MIN_Y_PLOT = (float) (0.3125f) * AreYouAlive.SCREEN_HEIGHT;
	private static float MAX_Y_PLOT = (float) AreYouAlive.SCREEN_HEIGHT - (0.0625f) * AreYouAlive.SCREEN_HEIGHT;

	
	private ShapeRenderer shapeRenderer;
	private BitmapFont font;

	private static final float BODY_SCAN_BUTTON_X_MIN_BOUND = (float) 0.4583;
	private static final float BODY_SCAN_BUTTON_X_MAX_BOUND = (float) 0.5417;
	

	private static final float BUTTON_Y_MIN_BOUND = (float) 0.1125;
	private static final float BUTTON_Y_MAX_BOUND = (float) 0.2125;
	
	private float[] arrAccelerationValues;
	private static float nAccelerationMeasure = 0f; 
	private static float nPlotIndex = 0f;
	
	protected BodyScanState(GameStateManager gsm) {
		super(gsm);
		Texture.setEnforcePotImages(false);

		shapeRenderer = new ShapeRenderer();
		font = new BitmapFont();
	    font.setColor(Color.RED);
	    
		
		//Set the viewing area and initialize bird, background and tube
		cam.setToOrtho(false, AreYouAlive.SCREEN_WIDTH, AreYouAlive.SCREEN_HEIGHT);
		
		background = new Texture(Gdx.files.internal("data/bodyscan.png"));

		mPulseManager = new AccelerometerPulseManager();
		
		arrAccelerationValues = new float[500];
		//Arrays.fill(arrAccelerationValues,(MAX_X_PLOT - MIN_X_PLOT) / 2);
		
		nPulseIdx = 0f;
		nAccelerationMeasure = 0;
		mPulseManager.startAccelerometer();
	}

	@Override
	protected void handleInput() 
	{
			
		if (Gdx.input.justTouched())
		{
		    // Report touch position.
			Vector3 touchPosition = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
			cam.unproject(touchPosition);

			//Body Scan button is pressed
			if (
					( (touchPosition.x >= AreYouAlive.SCREEN_WIDTH * BODY_SCAN_BUTTON_X_MIN_BOUND) && (touchPosition.x <= AreYouAlive.SCREEN_WIDTH * BODY_SCAN_BUTTON_X_MAX_BOUND) ) && 
					( (touchPosition.y >= AreYouAlive.SCREEN_HEIGHT * BUTTON_Y_MIN_BOUND) && (touchPosition.y <= AreYouAlive.SCREEN_HEIGHT * BUTTON_Y_MAX_BOUND) )
				)
				gsm.set(new MenuState(gsm));
			}
		}
	

	@Override
	public void update(float dt) 
	{
		handleInput();	
		
		//	nAccelerationMeasure = mPulseManager.getAccelerationValue();
		nPlotIndex = mPulseManager.getPulseIteration();
		Log.d("Accceleration", "Accceleration: " + Float.toString(nAccelerationMeasure));
		Log.d("Index", "Index: " + Float.toString(nPlotIndex));
		arrAccelerationValues = mPulseManager.getAccelerationValue();
		// Buna gerek yok, cunku c arrAccelerationValues array'ini doldurakoyacak.
		//mapAccelerationValuesToPlotHeight(nAccelerationMeasure,(int)nPlotIndex);
		
		 //if (nPlotIndex == 499)
		//	Arrays.fill(arrAccelerationValues, (MAX_X_PLOT - MIN_X_PLOT) / 2);
			
	}

	private void mapAccelerationValuesToPlotHeight(float nAcceleration, int nIdx) {
		float value = 0f;
			float nSigma = FibLib.getStandardDevAccelerometer();//0f;//
			float nMean = FibLib.getMeanAccelerometer();//0f;//
			
			
			float midpoint = (MAX_X_PLOT - MIN_X_PLOT) / 2;
			float range = (MAX_X_PLOT - MIN_X_PLOT) / 6; //per sigma we get this pixel coordinates
			
			if (nSigma < 0.59f)
				value = midpoint;
			else if (nAcceleration < (nMean - 3 * nSigma)) 
			{
				value = MIN_X_PLOT;
			}
			else if (nAcceleration > (nMean + 3 * nSigma)) 
			{
				value = MAX_X_PLOT;
			}
			else
			{
				if (nAcceleration == nMean) 
					value = midpoint;
				else //there has been enough movement to create a variance that is within 3sigma, may be a heartbeat.
				{
					if (nAcceleration < nMean) 
						value = midpoint - range * ((nMean - nAcceleration) / nSigma);
					else
						value = midpoint + range * ((nAcceleration - nMean) / nSigma);					
				}										
					
				value = (value > MAX_X_PLOT) ? MAX_X_PLOT : value;
				value = (value < MIN_X_PLOT) ? MIN_X_PLOT : value;
			}


			arrAccelerationValues[nIdx] = value;		//arrAccelerationValues[nIdx] = nAcceleration;				
	}
	
	@Override
	public void render(SpriteBatch sb) {
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		sb.begin();
		//Draw the values to the screen at P(200,200)
		sb.draw(background, 0, 0,  AreYouAlive.SCREEN_WIDTH, AreYouAlive.SCREEN_HEIGHT);
		font.draw(sb, Float.toString(arrAccelerationValues[(int) nPlotIndex]), 200, 200);//font.draw(sb, Integer.toString(mPulseManager.getDebugIteration()), 200, 200);
		sb.end();	
		
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.GREEN);
		for(int i=(int)MIN_Y_PLOT; i<(int)MAX_Y_PLOT - 1; i++)
		{
			shapeRenderer.line(arrAccelerationValues[i-(int)MIN_Y_PLOT], i, arrAccelerationValues[(i+1)-(int)MIN_Y_PLOT], i+1);			
		}
		
		shapeRenderer.end();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	

}
