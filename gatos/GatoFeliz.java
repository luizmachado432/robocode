package gatos;

import robocode.HitRobotEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;

// Definição da classe GatoFeliz que estende a classe Robot do Robocode
public class GatoFeliz extends Robot {
    // Variável para controlar se o robô deve espiar enquanto se move
    boolean peek;
    // Variável para armazenar a distância a ser movida
    double moveAmount;

    // Método principal do robô, chamado quando a batalha começa
    public void run() {
        // Define moveAmount como o maior valor entre a largura e a altura do campo de batalha
        moveAmount = Math.max(getBattleFieldWidth(), getBattleFieldHeight());
        
        // Inicializa peek como falso
        peek = false;

        // Ajusta o robô para ficar alinhado com a parede
        turnLeft(getHeading() % 90);
        // Move o robô até a parede
        ahead(moveAmount);

        // Define peek como verdadeiro para que o robô escaneie enquanto se move
        peek = true;
        // Gira o canhão 90 graus para a direita
        turnGunRight(90);
        // Gira o robô 90 graus para a direita
        turnRight(90);

        // Loop infinito para manter o robô se movendo
        while (true) {
            // Define peek como verdadeiro para que o robô escaneie enquanto se move
            peek = true;
            // Move o robô para a frente pela distância moveAmount
            ahead(moveAmount);
            // Define peek como falso para parar de escanear enquanto gira
            peek = false;
            // Gira o robô 90 graus para a direita
            turnRight(90);
        }
    }

    // Método chamado quando o robô colide com outro robô
    public void onHitRobot(HitRobotEvent e) {
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
        // Se peek for verdadeiro, o robô escaneia novamente
        if (peek) {
            scan();
        }
    }
}

