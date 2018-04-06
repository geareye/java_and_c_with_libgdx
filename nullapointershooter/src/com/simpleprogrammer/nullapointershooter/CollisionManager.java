package com.simpleprogrammer.nullapointershooter;

public class CollisionManager 
{

	private AnimatedSprite spaceshipAnimated;
	private Enemy enemy;
	private ShotManager shotManager;

	public CollisionManager(AnimatedSprite spaceshipAnimated, Enemy enemy,ShotManager shotManager) 
	{
				this.spaceshipAnimated = spaceshipAnimated;
				this.enemy = enemy;
				this.shotManager = shotManager;
	}

	public void handleCollisions() 
	{
		handleEnemyShot();//detect if enemy's been shot.
		handlePlayerShot();
	}

	private void handlePlayerShot()
	{
		if(shotManager.enemyShotTouches(spaceshipAnimated.getBoundingBox()))
		{
			spaceshipAnimated.setDead(true);
		}
		
	}

	private void handleEnemyShot() 
	{
		//ask shot manager if any of the shots that a player shots touch enemy's bounding box?
		if(shotManager.playerShotTouches(enemy.getBoundingBox()))
			enemy.hit();
	}
	

}
