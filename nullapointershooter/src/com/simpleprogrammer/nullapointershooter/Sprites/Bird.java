package com.simpleprogrammer.nullapointershooter.Sprites;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/*
 * Needs:	 position, - where the bird is in our game world, 
 * 			 texture   - what it needs to be drawn to the screen
 * 			 velocity  - in which direction is it going? up, right, left down? 	
 */

public class Bird 
{
	private static final int GRAVITY = -15;
	private static final int MOVEMENT = 100;
	
	private Vector3 position;
	private Vector3 velocity;
	private Texture bird;
	private Rectangle bounds;
	
	public Bird(int x, int y) // starting positions x and y
	{
		position = new Vector3(x,y,0);
		velocity = new Vector3(0,0,0);//starting not moving
		bird     = new Texture(Gdx.files.internal("data/bird.png"));
		bounds   = new Rectangle(x, y, bird.getWidth(), bird.getHeight());
	}
	////////////////////////////////////
	////////////////////////////////////
	public void update(float dt)
	{
		//Add GRAVITY to velocity if bird is within our view,
		//Scale velocity and add it to the position.
		if (position.y > 0)
			velocity.add(0,GRAVITY,0);
		velocity.scl(dt);		
		position.add(MOVEMENT * dt,velocity.y,0);
		
		//Reset the position if bird has fallen below cam-view
		if (position.y < 0)
			position.y = 0;
		
		//Re-scale velocity
		velocity.scl(1/dt);
		
		bounds.setX(position.x);
		bounds.setY(position.y);
	}
	////////////////////////////////////
	////////////////////////////////////
	public void jump()
	{
		velocity.y = 250;
	}
	
	////////////////////////////////////
	public Rectangle getBounds()	{ return bounds; }	
	public Vector3   getPosition()	{ return position; }
	public Texture   getTexture() 	{ return bird; }
	////////////////////////////////////
	public void setPosition(Vector3 position)	{ this.position = position; }
	public void setBird(Texture bird)	{ this.bird = bird; }
}
