package com.simpleprogrammer.nullapointershooter.States;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.simpleprogrammer.nullapointershooter.AreYouAlive;


public class MenuState extends State
{
	private boolean bInitialized;
	
	private static final float MISC_BUTTON_X_MIN_BOUND = (float) 0.7042;
	private static final float MISC_BUTTON_X_MAX_BOUND = (float) 0.7917;
	
	private static final float BODY_SCAN_BUTTON_X_MIN_BOUND = (float) 0.4583;
	private static final float BODY_SCAN_BUTTON_X_MAX_BOUND = (float) 0.5417;
	
	private static final float FINGER_SCAN_BUTTON_X_MIN_BOUND = (float) 0.1875;
	private static final float FINGER_SCAN_BUTTON_X_MAX_BOUND = (float) 0.2979;
	
	private static final float BUTTON_Y_MIN_BOUND = (float) 0.1125;
	private static final float BUTTON_Y_MAX_BOUND = (float) 0.2125;
	
	private Texture background;
	private Texture playBtn;
	private Texture btnFingerScanOff;
	private Texture btnFingerScanOn;
	private Texture btnBodyScanOn;
	private Texture btnBodyScanOff;

	public MenuState(GameStateManager gsm)
	{
		super(gsm);
		Texture.setEnforcePotImages(false);

		//Set the viewing area and initialize bird, background and tube
		cam.setToOrtho(false, AreYouAlive.SCREEN_WIDTH, AreYouAlive.SCREEN_HEIGHT);
		
		background	 	 = new Texture(Gdx.files.internal("data/bg2.png"));
		playBtn 	 	 = new Texture(Gdx.files.internal("data/playbtn.png"));
		btnFingerScanOff = new Texture(Gdx.files.internal("data/finger_pulse_off.png"));
		btnFingerScanOn  = new Texture(Gdx.files.internal("data/finger_pulse_on.png"));
		btnBodyScanOff 	 = new Texture(Gdx.files.internal("data/heart_pulse_off.png"));
		btnBodyScanOn 	 = new Texture(Gdx.files.internal("data/heart_pulse_on.png"));
		
		this.bInitialized = bInitialized;
	}

	@Override
	public void handleInput() 
	{
		//	Report touch position.
		Vector3 touchPosition = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
		cam.unproject(touchPosition);
		
		if (Gdx.input.justTouched())
		{
			
			//Misc button is pressed
			if (
					( (touchPosition.x >= AreYouAlive.SCREEN_WIDTH * MISC_BUTTON_X_MIN_BOUND) && (touchPosition.x <= AreYouAlive.SCREEN_WIDTH * MISC_BUTTON_X_MAX_BOUND) ) && 
					( (touchPosition.y >= AreYouAlive.SCREEN_HEIGHT * BUTTON_Y_MIN_BOUND) && (touchPosition.y <= AreYouAlive.SCREEN_HEIGHT * BUTTON_Y_MAX_BOUND) )
				)
			{
				//Log.d("PRESSED","MISC BUTTON");
				gsm.set(new MiscState(gsm));
				dispose();
			}
			//Body Scan button is pressed
			if (
					( (touchPosition.x >= AreYouAlive.SCREEN_WIDTH * BODY_SCAN_BUTTON_X_MIN_BOUND) && (touchPosition.x <= AreYouAlive.SCREEN_WIDTH * BODY_SCAN_BUTTON_X_MAX_BOUND) ) && 
					( (touchPosition.y >= AreYouAlive.SCREEN_HEIGHT * BUTTON_Y_MIN_BOUND) && (touchPosition.y <= AreYouAlive.SCREEN_HEIGHT * BUTTON_Y_MAX_BOUND) )
				)
			{
				//Log.d("PRESSED","BODY BUTTON");
				gsm.set(new BodyScanState(gsm));
				dispose();
			}
			//Finger Scan button is pressed
			if (
					( (touchPosition.x >= AreYouAlive.SCREEN_WIDTH * FINGER_SCAN_BUTTON_X_MIN_BOUND) && (touchPosition.x <= AreYouAlive.SCREEN_WIDTH * FINGER_SCAN_BUTTON_X_MAX_BOUND) ) && 
					( (touchPosition.y >= AreYouAlive.SCREEN_HEIGHT * BUTTON_Y_MIN_BOUND) && (touchPosition.y <= AreYouAlive.SCREEN_HEIGHT * BUTTON_Y_MAX_BOUND) )
				)
			{
				//Log.d("PRESSED","FINGER BUTTON");
				gsm.set(new FingerScanState(gsm));
				dispose();
			}
			
		}
				
	}

	@Override
	public void update(float dt) 
	{
		handleInput();	
	}

	@Override
	public void render(SpriteBatch sb) 
	{
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		sb.draw(background, 0, 0,  AreYouAlive.SCREEN_WIDTH, AreYouAlive.SCREEN_HEIGHT);
		sb.end();
	}

	//Get rid of background and playButton
	@Override
	public void dispose() 
	{
		background.dispose();
		playBtn.dispose();	
	}
}
