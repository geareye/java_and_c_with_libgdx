package com.simpleprogrammer.nullapointershooter;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.simpleprogrammer.nullapointershooter.States.GameStateManager;
import com.simpleprogrammer.nullapointershooter.States.MenuState;

import android.util.Log;
public class AreYouAlive implements ApplicationListener 
{
	//The GameStateManager is the stack that renders the viewing angle of the user.
	private GameStateManager gsm;
	
	//classes
	public static final int SCREEN_HEIGHT = 800;
	public static final int SCREEN_WIDTH = 480;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	
	//Texture Packing
	private TextureAtlas atlas;
	
	@Override
	public void create() 
	{	//So that you don't have to have a power of 2 size images
		Texture.setEnforcePotImages(false);
		gsm = new GameStateManager();
		//We use batch to draw our sprites.
		batch = new SpriteBatch();
		Gdx.gl.glClearColor(1, 0, 0, 1);

		//Push MenuState to the top of the state stack in GameStateManager.
		gsm.push(new MenuState(gsm));
	}
	
	public TextureAtlas getAtlas()
	{
		return atlas;
	}

	@Override
	public void dispose() 
	{
		batch.dispose();
	}

	@Override
	public void render() 
	{		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gsm.update(Gdx.graphics.getDeltaTime());//diff. btw render times.
		gsm.render(batch);
	}

	private void handleInput() 
	{
		Vector3 touchPosition = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
		camera.unproject(touchPosition);
		if( Gdx.input.isTouched() )
		{
			//Log.d("======> X: %f" + touchPosition.x , "======> Y: %f" + touchPosition.y);			
		}
	}

	@Override
	public void resize(int width, int height) 
	{
	}

	@Override
	public void pause() 
	{
	}

	@Override
	public void resume() 
	{
	}
}
