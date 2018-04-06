package com.simpleprogrammer.nullapointershooter.States;

import java.util.Arrays;

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
import com.simpleprogrammer.nullapointershooter.AreYouAlive;
import com.simpleprogrammer.nullapointershooter.CameraPulseManager;
import com.simpleprogrammer.nullapointershooter.VoiceManager;

public class MiscState extends State{

	private Texture background;
	private static final float MISC_BUTTON_X_MIN_BOUND = (float) 1.3913;
	private static final float MISC_BUTTON_X_MAX_BOUND = (float) 1.28;
	

	private static float MIN_X_PLOT = (float) (0.1042f) * AreYouAlive.SCREEN_WIDTH;
	private static float MAX_X_PLOT = (float) (AreYouAlive.SCREEN_WIDTH - (0.1042f) * AreYouAlive.SCREEN_WIDTH);
	private static float MIN_Y_PLOT = (float) (0.3125f) * AreYouAlive.SCREEN_HEIGHT;
	private static float MAX_Y_PLOT = (float) AreYouAlive.SCREEN_HEIGHT - (0.0625f) * AreYouAlive.SCREEN_HEIGHT;

	
	private static final float BUTTON_Y_MIN_BOUND = (float) 1.29032;
	private static final float BUTTON_Y_MAX_BOUND = (float) 1.19403;
	private float[] arrMicValues;

	private static VoiceManager mVoiceManager;
	
	private BitmapFont font;
	private ShapeRenderer shapeRenderer;

	private float m_fFreqMagnitude;
	
	protected MiscState(GameStateManager gsm) {
		super(gsm);
		Texture.setEnforcePotImages(false);
		
		shapeRenderer = new ShapeRenderer();
		font = new BitmapFont();
	    font.setColor(Color.RED);
	    
	    //Set the viewing area and initialize bird, background and tube
	    cam.setToOrtho(false, AreYouAlive.SCREEN_WIDTH, AreYouAlive.SCREEN_HEIGHT);
	  		
	    
		mVoiceManager = new VoiceManager();
		arrMicValues  = new float [512];
		//background = new Texture(Gdx.files.internal("data/bg_org.png"));
		background    = new Texture(Gdx.files.internal("data/bg2.png"));
		
		mVoiceManager.startMicrophone();

	}

	@Override
	protected void handleInput() {
		if (Gdx.input.justTouched())
		{
			Vector3 touchPosition = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
			Log.d("Touch Position: X: %d" + touchPosition.x, "Touch Position y: %d" + touchPosition.y);
			
			if (
					( (touchPosition.x >= AreYouAlive.SCREEN_WIDTH  / MISC_BUTTON_X_MIN_BOUND) && (touchPosition.x <= AreYouAlive.SCREEN_WIDTH / MISC_BUTTON_X_MAX_BOUND) ) && 
					( (touchPosition.y >= AreYouAlive.SCREEN_HEIGHT / BUTTON_Y_MIN_BOUND) && (touchPosition.y <= AreYouAlive.SCREEN_HEIGHT/ BUTTON_Y_MAX_BOUND) )
				)
			{
				//Log.d("PRESSED","MISC BUTTON");
				gsm.set(new MenuState(gsm));
			}
		}
	}

	@Override
	public void update(float dt) {
		handleInput();
		arrMicValues = mVoiceManager.getFloatMicBuffer();
        //Log.d("this is my array", "arr: " + Arrays.toString(arrMicValues));
		int nIdxMaxFreq = (int) arrMicValues[0];
		SetMagnitude(arrMicValues[nIdxMaxFreq + 256]);
	}


	@Override
	public void render(SpriteBatch sb) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
 		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.BLACK);
 
		sb.begin();
		//Draw the values to the screen at P(200,200)
		font.draw(sb, Float.toString(GetMagnitude()), 200, 200);		
		
 		for(int i=0; i<256-1; i++)		
 			shapeRenderer.line(arrMicValues[i], arrMicValues[i+256], arrMicValues[i+1], arrMicValues[(i+1) + 256]);
		 		
 		sb.end(); 		
		shapeRenderer.end();	
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	

	private void SetMagnitude(float f) {
		
		this.m_fFreqMagnitude = f;
	}
	

	private float GetMagnitude() {
		
		return this.m_fFreqMagnitude;
	}
}
