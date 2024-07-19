package gatos;

import robocode.HitRobotEvent;
import robocode.AdvancedRobot;
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
        // Define as cores do robô
        setColors(Color.pink, Color.pink, Color.pink, Color.pink, Color.pink);
        setGunColor(Color.pink);
        setRadarColor(Color.pink);
        setScanColor(Color.pink);
        setBulletColor(Color.pink);

        // Define moveAmount como o maior valor entre a largura e a altura do campo de batalha
        double moveAmount = Math.max(getBattleFieldWidth(), getBattleFieldHeight());

        // Ajusta o robô para ficar alinhado com a parede
        turnLeft(getHeading() % 90);
        // Move o robô até a parede
        ahead(moveAmount);

        // Gira o robô 90 graus para a direita
        turnRight(90);

        // Girar o radar continuamente enquanto procura um robô
        turnRadarRight(360);

        // Loop infinito para manter o robô se movendo
        while (true) {
            // Move o robô para a frente pela distância moveAmount
            ahead(moveAmount);
            // Gira o canhão 360 graus
            turnGunRight(360);
            // Gira o robô 90 graus para a direita
            turnRight(90);

            // Verifica se o tempo de foco no inimigo excedeu o máximo permitido
            if (scanStartTime != -1 && (getTime() - scanStartTime > MAX_FOCUS_TIME)) {
                // Reseta o tempo de foco e o comportamento (opcional)
                scanStartTime = -1;
                // Alterar comportamento aqui (por exemplo, mudar a estratégia)
            }

            // Execute as ações pendentes
            execute();
        }
    }

    // Método chamado quando o robô colide com outro robô
    public void onHitRobot(HitRobotEvent e) {
        // Ajusta a rotação do canhão para apontar para o robô inimigo
        double gunTurnAmt = Utils.normalRelativeAngleDegrees(e.getBearing() + (getHeading() - getGunHeading()));
        turnGunRight(gunTurnAmt);

        // Dispara com toda a força
        fire(3);

        // Verifica se o robô inimigo está na frente
        if (e.getBearing() > -90 && e.getBearing() < 90) {
            // Se o robô inimigo estiver na frente, recua 100 unidades
            back(100);
        } else {
            // Se o robô inimigo estiver atrás, avança 100 unidades
            ahead(100);
        }
    }

    // Método chamado quando o robô escaneia outro robô
    public void onScannedRobot(ScannedRobotEvent e) {
        // Atualiza o tempo de início de foco
        if (scanStartTime == -1) {
            scanStartTime = getTime();
        }

        // Calcula a localização exata do robô inimigo
        double absoluteBearing = getHeadingRadians() + e.getBearingRadians();
        enemyX = getX() + e.getDistance() * Math.sin(absoluteBearing);
        enemyY = getY() + e.getDistance() * Math.cos(absoluteBearing);

        // Calcula o tempo estimado para o tiro atingir o alvo
        double bulletPower = calculateFirePower(e.getDistance());
        double bulletVelocity = 20 - 3 * bulletPower;
        double bulletTravelTime = e.getDistance() / bulletVelocity;

        // Estima a posição futura do robô inimigo
        double futureX = enemyX + e.getVelocity() * Math.sin(e.getHeadingRadians()) * bulletTravelTime;
        double futureY = enemyY + e.getVelocity() * Math.cos(e.getHeadingRadians()) * bulletTravelTime;

        // Calcula o ângulo para a mira
        double futureBearing = Math.atan2(futureX - getX(), futureY - getY());
        double gunTurnAmt = Utils.normalRelativeAngleDegrees(Math.toDegrees(futureBearing) - getGunHeading());

        // Ajusta a rotação do canhão para a posição futura
        turnGunRight(gunTurnAmt);
        
        // Dispara com a força calculada
        if (getGunHeat() == 0) {
            fire(bulletPower);
        }
    }

    // Função para calcular a força de tiro com base na distância
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
}

