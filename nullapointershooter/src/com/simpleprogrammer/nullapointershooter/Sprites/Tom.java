package com.simpleprogrammer.nullapointershooter.Sprites;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Tom 
{
	private static int FORCE = 10;	
		
	private Texture textureTom;
	private Vector3 position;
	private Vector3	velocity;
	private int		nForce = 5;
	private	boolean bMove;
	
	private Random rand;

	public Tom(boolean bIsTomActive) // Tom's position on the screen.
	{		
		textureTom 	 = new Texture(Gdx.files.internal("data/bird.png"));
		position 	 = new Vector3(150, 150,0);
		velocity     = new Vector3(0,0,0);
		bMove 		 = bIsTomActive;
	}
	
	public void update(float dt)
	{
		if (bMove)
		{
			if (position.x > 0)
				velocity.add(nForce,0,0);			
			velocity.scl(dt);		
			position.add(velocity.x,0,0);
						
			if (position.x < 50)
			{
				position.x = 50;
				nForce = 5;
			}
			
			if (position.x > 250)
			{
				position.x = 250;
				nForce = -5;
			}
			
			//Re-scale velocity
			velocity.scl(1/dt);
		}
	}
	
	public void move()
	{
		bMove = true;	
	}
	
	public void stop()
	{
		position.x = position.y = 150;		
		bMove = false;	
	}
	
	
	public Vector3 getPosition() {
		return position;
	}

	public void setPosition(Vector3 position) {
		this.position = position;
	}

	public Texture getTexture() {
		return textureTom;
	}

	public void setBird(Texture tom) {
		this.textureTom = tom;
	}
}