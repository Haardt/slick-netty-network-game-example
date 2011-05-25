
package de.iritgo.skillfull.bullet.action;


import com.artemis.utils.Utils;

import de.iritgo.skillfull.bullet.Bullet;
import de.iritgo.skillfull.bullet.BulletAction;
import de.iritgo.skillfull.bullet.BulletDirector;
import de.iritgo.skillfull.bullet.BulletTimer;


public class DriveAction extends BulletAction
{
	private int timer;

	private float x;

	private float y;

	private Bullet initBullet;

	private float acceleration;

	private float rotation;

	private float movedWay;

	private float speed;

	private int lastCallTime;

	private float lastMovedWay;

	private float lastNextPosX;

	private float lastNextPosY;

	private float lastMoved;

	public DriveAction (Bullet bullet)
	{
		initBullet = new Bullet (bullet);
		rotation = - 1;
		acceleration = 0.0f;
		actionDone = false;
	}

	@Override
	public boolean perform (int delta, BulletDirector bulletDirector, Bullet bullet)
	{
		int time = getTime ();

		float posInTime = (float) time / (float) getStopTime ();
		if (posInTime > 1)
		{
			posInTime = 1;
		}
		if (rotation != - 1)
			bullet.setRotation (Utils.lerpDegrees (initBullet.getRotation (), rotation, posInTime));

		movedWay = (0.5f * (acceleration / 1000) * ((time) * (time))) + ((speed / 1000) * (time));
		System.out.println ("D: " + movedWay + ">" + time);

		float nextPosX = Utils.getXAtEndOfRotatedLineByOrigin (0, movedWay - lastMoved, bullet.getRotation ());
		float nextPosY = Utils.getYAtEndOfRotatedLineByOrigin (0, movedWay - lastMoved, bullet.getRotation ());

		if (movedWay - lastMoved == 0)
			System.out.println (movedWay - lastMoved);
		lastMoved = movedWay;

		bullet.setX (initBullet.getX () + movedWay);
		bullet.setY (initBullet.getY () + nextPosY);
		return actionDone;
	}

	@Override
	public void performDone (int delta, BulletDirector bulletDirector, Bullet bullet)
	{
		int time = getTime ();

		inactive ();
		bullet.setMovedWay (movedWay);
		bullet.setAcceleration (acceleration);
		if (rotation != -1)
			bullet.setRotation (rotation);

		bullet.setLastSpeed (((acceleration) * (time)) + (speed));
	}

	public DriveAction acceleration (float acceleration)
	{
		this.acceleration = acceleration;
		return this;
	}

	public DriveAction movedWay (float movedWay)
	{
		this.movedWay = movedWay;
		return this;
	}

	public DriveAction rotate (float rotation)
	{
		this.rotation = rotation;
		return this;
	}

	public DriveAction time (int time)
	{
		stopTime (time);
		return this;
	}

	public DriveAction dontWait ()
	{
		actionDone = true;
		return this;
	}

	public DriveAction time (int startTime, int stopTime)
	{
		startTime (startTime);
		stopTime (startTime + stopTime);
		return this;
	}

	public DriveAction speed (float speed)
	{
		this.speed = speed;
		return this;
	}

	public DriveAction withLastSpeed ()
	{
		speed = initBullet != null ? initBullet.getLastSpeed () : speed;
		return this;
	}
}
