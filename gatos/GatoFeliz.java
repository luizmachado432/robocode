package gatos;

import robocode.AdvancedRobot;
import robocode.HitRobotEvent;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;
import java.awt.Color;

public class GatoFeliz extends AdvancedRobot {

    // Variáveis para rastreamento de tempo
    private long scanStartTime = -1; // Tempo em que o robô começou a focar no inimigo
    private static final long MAX_FOCUS_TIME = 2000; // Tempo máximo de foco (em milissegundos)

    // Variáveis para previsão de trajetória
    private double enemyX; // Posição X estimada do robô inimigo
    private double enemyY; // Posição Y estimada do robô inimigo

    // Método principal do robô, chamado quando a batalha começa
    public void run() {
        // Configuração inicial do robô
        configureRobotColors();

        // Move o robô para alinhar com a parede e começa a girar o radar
        alignRobotWithWall();
        turnRadarRight(360);

        // Loop principal do robô
        while (true) {
            moveAndTurn();
            checkMaxFocusTime();
            execute();
        }
    }

    // Configura as cores do robô
    private void configureRobotColors() {
        setColors(Color.pink, Color.pink, Color.pink, Color.pink, Color.pink);
        setGunColor(Color.pink);
        setRadarColor(Color.pink);
        setScanColor(Color.pink);
        setBulletColor(Color.pink);
    }

    // Ajusta o robô para alinhar com a parede e move até ela
    private void alignRobotWithWall() {
        double moveAmount = Math.max(getBattleFieldWidth(), getBattleFieldHeight());
        turnLeft(getHeading() % 90);
        ahead(moveAmount);
        turnRight(90);
    }

    // Movimenta e gira o robô
    private void moveAndTurn() {
        double moveAmount = Math.max(getBattleFieldWidth(), getBattleFieldHeight());
        ahead(moveAmount);
        turnGunRight(360);
        turnRight(90);
    }

    // Verifica se o tempo de foco no inimigo excedeu o máximo permitido
    private void checkMaxFocusTime() {
        if (scanStartTime != -1 && getTime() - scanStartTime > MAX_FOCUS_TIME) {
            scanStartTime = -1;
            // Adicionar aqui comportamento adicional ao exceder o tempo de foco
        }
    }

    // Chamado quando o robô colide com outro robô
    public void onHitRobot(HitRobotEvent e) {
        double gunTurnAmt = Utils.normalRelativeAngleDegrees(e.getBearing() + (getHeading() - getGunHeading()));
        turnGunRight(gunTurnAmt);
        fire(3);

        if (e.getBearing() > -90 && e.getBearing() < 90) {
            back(100);
        } else {
            ahead(100);
        }
    }

    // Chamado quando o robô escaneia outro robô
    public void onScannedRobot(ScannedRobotEvent e) {
        if (scanStartTime == -1) {
            scanStartTime = getTime();
        }

        double absoluteBearing = getHeadingRadians() + e.getBearingRadians();
        enemyX = getX() + e.getDistance() * Math.sin(absoluteBearing);
        enemyY = getY() + e.getDistance() * Math.cos(absoluteBearing);

        double bulletPower = calculateFirePower(e.getDistance());
        double bulletVelocity = 20 - 3 * bulletPower;
        double bulletTravelTime = e.getDistance() / bulletVelocity;

        double futureX = enemyX + e.getVelocity() * Math.sin(e.getHeadingRadians()) * bulletTravelTime;
        double futureY = enemyY + e.getVelocity() * Math.cos(e.getHeadingRadians()) * bulletTravelTime;

        double futureBearing = Math.atan2(futureX - getX(), futureY - getY());
        double gunTurnAmt = Utils.normalRelativeAngleDegrees(Math.toDegrees(futureBearing) - getGunHeading());

        turnGunRight(gunTurnAmt);

        if (getGunHeat() == 0) {
            fire(bulletPower);
        }
    }

    // Calcula a força de tiro com base na distância
    public double calculateFirePower(double distance) {
        if (distance < 700) {
            return 3.0; // Distância curta, força máxima
        } else if (distance < 800) {
            return 2.0; // Distância média, força média
        } else if (distance < 900) {
            return 1.0; // Distância longa, força baixa
        } else {
            return 0.1; // Distância muito longa, força mínima
        }
    }

    	// dancinha da vitória garantida rapaz!
    private void embrasando() {
        for (int i = 0; i < 3; i++) {
		
        // dança y dança
        turnLeft(45);
        turnRight(90);
        turnLeft(90);
        turnRight(45);
	turnLeft(45);
        turnRight(90);
        turnLeft(90);
        turnRight(45);
        }
    }
    // método para rodar a dancinha quando o robo vencer 
    // gato feliz é muito feliz quando vence
    public void onWin(WinEvent event) {
        embrasando();
    }
}


