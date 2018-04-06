package com.simpleprogrammer.nullapointershooter.States;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.simpleprogrammer.nullapointershooter.AreYouAlive;
import com.simpleprogrammer.nullapointershooter.FibLib;
import com.simpleprogrammer.nullapointershooter.Sprites.Bird;
import com.simpleprogrammer.nullapointershooter.Sprites.Tube;
import com.simpleprogrammer.nullapointershooter.Sprites.Tom;

public class PlayState extends State
{
	private static final int TUBE_SPACING = 125;
	private static final int TUBE_COUNT = 4;
		
	private Bird 	bird;
	private Texture bgOn, bgOff, bgCurrent;
	private Tom		tom;
	private Texture ground;
	private Vector2 groundPos1, groundPos2;
	
	private boolean bButtonCameraPulse;
	private boolean bButtonAccelerometerPulse;
	private boolean bJustTouched = false;
	
	public final int BIRD_LOCATION_Y = 350;
	
	private Array<Tube> tubes;
	
	private float viewWindow; //Window to look through and see the world. 
	private float posTubeEnd; //Ending position of the tube.
	
	////////////////////////////////////////////////////
	////////////////////////////////////////////////////
	protected PlayState(GameStateManager gsm) //PlayState is active during game play
	{
		super(gsm);
		Texture.setEnforcePotImages(false);
		
		float temp;
		temp = FibLib.fibNI(15);
		
		//Set the viewing area and initialize bird, background and tube
		cam.setToOrtho(false, AreYouAlive.SCREEN_WIDTH, AreYouAlive.SCREEN_HEIGHT);
		//bird 	= new Bird(50,BIRD_LOCATION_Y);
		bgOff 		= new Texture(Gdx.files.internal("data/AreYouAlive_RawBG_OFF.png"));
		bgOn 		= new Texture(Gdx.files.internal("data/AreYouAlive_RawBG_ON.png"));
		bgCurrent	= bgOff;
		//tom		= new Tom(true);
		
		//Ground starts originally from left side of our camera
		ground = new Texture(Gdx.files.internal("data/ground.png"));
		groundPos1 = new Vector2(cam.position.x - (cam.viewportWidth/2), 0);
		groundPos2 = new Vector2(cam.position.x - (cam.viewportWidth/2) + ground.getWidth(), 0);
		
		tubes = new Array<Tube>();		
		for(int i=1; i <= TUBE_COUNT; i++)
			tubes.add(new Tube(i * (TUBE_SPACING + Tube.TUBE_WIDTH)));
	}

	////////////////////////////////////////////////////
	////////////////////////////////////////////////////
	@Override
	protected void handleInput() 
	{	
		//Report touch position.
		Vector3 touchPosition = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
		cam.unproject(touchPosition);
		//System.out.print("Y: %f" + touchPosition.y);
		//System.out.print("X: %f" + touchPosition.x);
		//Log.d("----------------","--------------");
		//Log.d("TOUCH_POSITION","X: %d " + touchPosition.x);
		//Log.d("TOUCH_POSITION","Y: %d " + touchPosition.y);
		
		if (Gdx.input.isTouched())
		{
		//	bJustTouched = !bJustTouched;
		//	if (bJustTouched)
		//		bgCurrent = bgOff;
		//	else
		//		bgCurrent = bgOn;
//			if (Gdx.input.justTouched())
//				bird.jump();
			
//			tom.move();
		}
							
		//Start camera and flash based pulse calculation.
		if (bButtonCameraPulse)
		{
			
		}
		//Start accelerometer based pulse calculation.
		if (bButtonAccelerometerPulse)
		{
			
		}
	}

	////////////////////////////////////////////////////
	////////////////////////////////////////////////////
	@Override
	public void update(float dt) 
	{
		handleInput();
/*		bird.update(dt);
		tom.update(dt);
		cam.position.x = bird.getPosition().x + 80;
				
		for(Tube tube : tubes)
		{
			viewWindow = cam.position.x - (cam.viewportWidth/2);
			posTubeEnd = tube.getPosTopTube().x + tube.getTopTube().getWidth();			
			//If tube is off the side of the screen, reposition 	
			if (posTubeEnd < viewWindow)
				tube.reposition(tube.getPosTopTube().x + ((tube.TUBE_WIDTH + TUBE_SPACING) * TUBE_COUNT));
			if (tube.collides(bird.getBounds()))
				gsm.set(new PlayState(gsm));
		}
		cam.update();
*/		
	}

	////////////////////////////////////////////////////
	////////////////////////////////////////////////////
	@Override
	public void render(SpriteBatch sb) 
	{		
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		sb.draw(bgCurrent, cam.position.x - (cam.viewportWidth / 2), 0);
/*		
 		sb.draw(bird.getTexture(), bird.getPosition().x, bird.getPosition().y);
		for (Tube tube : tubes)
		{
			sb.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
			sb.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y);
		}
		sb.draw(tom.getTexture(), tom.getPosition().x, tom.getPosition().y);
		sb.draw(ground, groundPos1.x, groundPos1.y);
		sb.draw(ground, groundPos2.x, groundPos2.y);
*/		
		sb.end();
	}

	////////////////////////////////////////////////////
	////////////////////////////////////////////////////
	@Override
	public void dispose() {
		
	}
}
