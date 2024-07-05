package pacode_De_Teste;
import robocode.*;

public class Testando extends Robot
{

	 
	public void run() {
		

		
		while(true) {
			ahead(100);
			back(100);
			getRadarHeadingRadians()
	}


	public void onScannedRobot(ScannedRobotEvent e){
		get
		fire(0.1);
		
	}


	public void onHitByBullet(HitByBulletEvent e){
	
		back(10);
		ahead(10);
}
	

	public void onHitWall(HitWallEvent e){
		
		back(30);
		turnRight(10);
		ahead(20);
		
	}	
	
	public void onHitRobot(HitRobotEvent e){
	


	}



}












































































