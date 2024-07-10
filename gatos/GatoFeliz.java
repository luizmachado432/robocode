package gatos;

import robocode.HitRobotEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;

public class GatoFeliz extends Robot {
    boolean peek;
    double moveAmount;

    // Método principal do robô
    public void run() {
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

    // Evento quando o robô colide com outro robô
    public void onHitRobot(HitRobotEvent e) {
        if (e.getBearing() > -90 && e.getBearing() < 90) {
            back(100);
        } else {
            ahead(100);
        }
    }

    // Evento quando o robô escaneia outro robô
    public void onScannedRobot(ScannedRobotEvent e) {
        if (peek) {
            scan();
        }
    }
}

		}	
	}
}
