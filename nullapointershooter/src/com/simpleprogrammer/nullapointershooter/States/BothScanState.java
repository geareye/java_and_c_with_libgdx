package com.simpleprogrammer.nullapointershooter.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.simpleprogrammer.nullapointershooter.AreYouAlive;

public class BothScanState extends State{

	private Texture background;
	private static final float MISC_BUTTON_X_MIN_BOUND = (float) 1.3913;
	private static final float MISC_BUTTON_X_MAX_BOUND = (float) 1.28;
	
	private static final float BODY_SCAN_BUTTON_X_MIN_BOUND = (float) 2.08696;
	private static final float BODY_SCAN_BUTTON_X_MAX_BOUND = (float) 1.84615;
	
	private static final float FINGER_SCAN_BUTTON_X_MIN_BOUND = (float) 4.48598;
	private static final float FINGER_SCAN_BUTTON_X_MAX_BOUND = (float) 3.50365;
	
	private static final float BUTTON_Y_MIN_BOUND = (float) 1.29032;
	private static final float BUTTON_Y_MAX_BOUND = (float) 1.19403;
	
	protected BothScanState(GameStateManager gsm) {
		super(gsm);
		Texture.setEnforcePotImages(false);

		background = new Texture(Gdx.files.internal("data/bothscan.png"));
	}

	@Override
	protected void handleInput() {
		
	}

	@Override
	public void update(float dt) {
		handleInput();			
	}

	@Override
	public void render(SpriteBatch sb) {
		sb.begin();
		sb.draw(background, 0, 0,  AreYouAlive.SCREEN_WIDTH, AreYouAlive.SCREEN_HEIGHT);
		sb.end();	
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
