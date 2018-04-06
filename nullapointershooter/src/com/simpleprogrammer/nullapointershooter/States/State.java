package com.simpleprogrammer.nullapointershooter.States;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public abstract class State  //abstract, because we don't want to instantiate any instances of State.	
{
	//Each state needs a camera
	protected OrthographicCamera cam;
	//Mouse or a pointer material
	protected Vector3 touch;
	//Manage our states like pause  
	protected GameStateManager gsm;
	
	//
	protected State(GameStateManager gsm)
	{
		this.gsm = gsm; //class field gsm is assigned to what gets passed to this class
		cam = new OrthographicCamera();
		touch = new Vector3();		
	}
	
	protected abstract void handleInput();
	public 	  abstract void update(float dt); //takes delta time
	public	  abstract void render(SpriteBatch sb); //takes in a SpriteBatch=container for everything  
 	public	  abstract void dispose();						   //that we need to render to the screen. 
}