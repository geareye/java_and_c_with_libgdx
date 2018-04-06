package com.simpleprogrammer.nullapointershooter.Sprites;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;



public class Tube
{
	public static final int TUBE_WIDTH = 52;
	private static final int FLUCTUATION = 150;
	private static final int TUBE_GAP = 88;
	private static final int LOWEST_OPENING = 150;
	
	private Texture topTube, bottomTube;
	private Vector2 posTopTube, posBotTube;
	private Rectangle boundsTop, boundsBot;
	private Random rand;
	
	public Tube(float x)
	{
		//Define the texture for tubes and spawn them at random positions. 
		rand 	   = new Random();
		topTube    = new Texture(Gdx.files.internal("data/toptube.png"));
		bottomTube = new Texture(Gdx.files.internal("data/bottomtube.png"));
		
		posTopTube = new Vector2(x, rand.nextInt(FLUCTUATION) + TUBE_GAP + LOWEST_OPENING);
		posBotTube = new Vector2(x, posTopTube.y - TUBE_GAP - bottomTube.getHeight());
		
		boundsTop = new Rectangle(posTopTube.x, posTopTube.y, topTube.getWidth(), topTube.getHeight());
		boundsBot = new Rectangle(posBotTube.x, posBotTube.y, bottomTube.getWidth(), bottomTube.getHeight());
	}	
	
	public void reposition(float x)
	{
		posTopTube.set(x, rand.nextInt(FLUCTUATION) + TUBE_GAP + LOWEST_OPENING);
		posBotTube.set(x, posTopTube.y - TUBE_GAP - bottomTube.getHeight());
		boundsTop.setX(posTopTube.x);
		boundsTop.setY(posTopTube.y);
	}
	
	public boolean collides(Rectangle player)
	{
		//returns true if the player and tube overlaps.
		return (player.overlaps(boundsTop) || player.overlaps(boundsBot)); 
	}
	////////////////////////////////////	
	public Texture getTopTube()		{ return topTube; }
	public Texture getBottomTube()	{ return bottomTube; }
	public Vector2 getPosTopTube()  { return posTopTube; }
	public Vector2 getPosBotTube()  { return posBotTube; }
	////////////////////////////////////
	public void setTopTube(Texture topTube)		  { this.topTube = topTube; }	
	public void setBottomTube(Texture bottomTube) {	this.bottomTube = bottomTube; }
	public void setPosTopTube(Vector2 posTopTube) { this.posTopTube = posTopTube; }
	public void setPosBotTube(Vector2 posBotTube) { this.posBotTube = posBotTube; }
}
