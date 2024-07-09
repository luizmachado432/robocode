package gatos;
import robocode.*;
import robocode.HitRobotEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;//import java.awt.Color;

// API help : https://robocode.sourceforge.io/docs/robocode/robocode/Robot.html

/**
 * GatoFeliz - a robot by (your name here)
 */
public class GatoFeliz extends Robot{




	boolean peek; /
	double moveAmount; 

	
		moveAmount = Math.max(getBattleFieldWidth(), getBattleFieldHeight());
	
		peek = false;

		
		turnLeft(getHeading() % 90);
		ahead(moveAmount);
	
		peek = true;
		turnGunRight(90);
		turnRight(90);

		while (true) {
			
			peek = true;
			ahead(moveAmount);
			peek = false;
		
			turnRight(90);
		}
	}

	
	public void onHitRobot(HitRobotEvent e) {
		if (e.getBearing() > -90 && e.getBearing() < 90) {
			back(100);
		}
		else {
			ahead(100);
		}
	}

	
	public void onScannedRobot(ScannedRobotEvent e) {
		

		if (peek) {
			scan();
		}
	
	/**
	 * run: GatoFeliz's default behavior
	 */
	public void run() {
		// Initialization of the robot should be put here

		// After trying out your robot, try uncommenting the import at the top,
		// and the next line:

		// setColors(Color.red,Color.blue,Color.green); // body,gun,radar

		// Robot main loop
		while(true) {
			// Replace the next 4 lines with any behavior you would like
			ahead(100);
			turnGunRight(360);
			back(100);
			turnGunRight(360);
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		// Replace the next line with any behavior you would like
		fire(1);
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
		back(10);
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		// Replace the next line with any behavior you would like
		back(20);
		}	
	}
}
