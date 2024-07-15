package teste;
import robocode.*;

public class Teste extends AdvancedRobot
{
	
	public void run() {
	//calcula a distancia ate a parede
		double distParede = Math.max(getBattleFieldWidth(), getBattleFieldHeight());
		//ajusta o angulo de movimentacao do robo para ele ir ate a parede
			turnLeft(getHeading() % 90);
		//faz o robo se mover na mesma distancia da parede
			ahead (distParede);
			turnGunRight(90);
			turnRight(90);
			
		while(true) {
		//loop q faz com q o robo dê umas escorregadas na msm parede antes de ir para a proxima
			for (int parede = 0;parede < 2;parede++){
			
				ahead (distParede);
				back(distParede);
				
				if (parede == 2){
				//quando chega a 2 repeticoes do loop o contador zera e o robo vai para a proxima parede
				parede = 0;
			}
		}
			turnRight(90);
	}
}

	public void onScannedRobot(ScannedRobotEvent e) {
	
		fire(1);
		
	}
	public void onHitRobot(HitRobotEvent e) {
   		
		double eX = getX();
		double eY = getY();
		
		double altura = getBattleFieldHeight();
		double largura = getBattleFieldWidth();
		
		double nextWall = Math.max(getBattleFieldWidth(), getBattleFieldHeight());
			
			if (e.getBearing() > -90 && e.getBearing() < 90 && eX < 50) {
				turnRight(90);
				ahead(nextWall);
			}
			else if (e.getBearing() > -90 && e.getBearing() < 90 && eX > largura - 50){
				turnLeft(90);
				ahead(nextWall);
			}
			if (e.getBearing() < -90 || e.getBearing() > 90 && eY < 50){
				turnRight(90);
				ahead(nextWall);
			}
			else if (e.getBearing() < -90 || e.getBearing() > 90 && eY > altura - 50){
				turnLeft(90);
				ahead(nextWall);
			}
		//	back(200);
			//turnRight(90);
			//turnLeft(getHeading() % 90);
			//ahead(nextWall);
		//	turnLeft(getHeading() % 90);
			//turnRight(90);
	//	else {
				//turnLeft(45);
			//	turnLeft(getHeading() % 90);
			//	ahead(nextWall);
			//	turnRight(90);	
			///}
	}
	
	public void onHitByBullet(HitByBulletEvent e) {
		
		double escapeWall = Math.max(getBattleFieldWidth(), getBattleFieldHeight());
		fuga();
		turnLeft(getHeading() % 90);
		ahead(escapeWall);
		turnRight(90);
		
	}
	public void fuga() {
	//retornam as coordenadas da arena
		double x = getX();
		double y = getY();
	//retornam altura e largura da arena
		double battlefieldWidth = getBattleFieldWidth();
		double battlefieldHeight = getBattleFieldHeight();
	//ultiliza as coordenadas e tamanho do campo de batalha para
	//que o robo fuja na direcao correta evitando que ele colida com
	//a parede em que ele esteja passeando
		if (x < 50) {
			turnRight(90);
			ahead(200);
		} else if (x > battlefieldWidth - 50) {
			turnLeft(90);
			ahead(200);
		}	
		if (y < 50) {
			turnRight(90);
			ahead(200);	
		}
		 else if (y > battlefieldHeight - 50) {
			turnLeft(90);
			ahead(200);
		}
	}
}