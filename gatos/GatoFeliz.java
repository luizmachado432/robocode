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
        
        // Cálculo do ângulo para girar a arma
        double absoluteBearing = getHeadingRadians() + e.getBearingRadians();
        double gunTurn = absoluteBearing - getGunHeadingRadians();

        // Cálculo da posição futura do robô inimigo
        double future = e.getVelocity() * Math.sin(e.getHeadingRadians() - absoluteBearing) / Rules.getBulletSpeed(1);

        // Ajuste da arma para atirar na posição futura
        setTurnGunRightRadians(Utils.normalRelativeAngle(gunTurn + future));
        
         // Disparo com potência calculada
        double firePower = decideFirePower(e);
        setFire(firePower);
        }
    
    public double decideFirePower(ScannedRobotEvent e) {
        // Método que decide a potência do tiro com base em várias condições.
        
        double firePower = getOthers() == 1 ? 2.0 : 3.0;
        // Se houver apenas um robô inimigo, define a potência do tiro como 2.0. Caso contrário, define como 3.0.
        
        if (e.getDistance() > 200) {
            firePower = 1.0;
            // Se a distância até o robô inimigo for maior que 200 unidades, define a potência do tiro como 1.0.
        } else if (e.getDistance() < 100) {
            firePower = 3.0;
            // Se a distância até o robô inimigo for menor que 100 unidades, define a potência do tiro como 3.0.
        }


        // Ajusta a potência do tiro com base na energia atual do robô. 
        // Menor potência se a energia estiver baixa. 
        if (getEnergy() < 1) {
            firePower = 0.1;
            // Se a energia do robô for menor que 1, define a potência do tiro como 0.1.
        } else if (getEnergy() < 10) {
            firePower = 1.0;
            // Se a energia do robô for menor que 10, define a potência do tiro como 1.0.
        }
        
        return Math.min(e.getEnergy() / 4, firePower);
        // Evita desperdiçar energia garantindo que a potência do tiro não seja maior que um quarto da energia do inimigo.
    }
}
    }
}




package teste;

import robocode.AdvancedRobot;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.Rules;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class Teste extends AdvancedRobot {

    public void run() {
        setAdjustRadarForGunTurn(true);
        setAdjustRadarForRobotTurn(true);
        setTurnRadarRight(360);
		int conter = 0;

        // Movimentação inicial para uma parede
        double distParede = Math.max(getBattleFieldWidth(), getBattleFieldHeight());
        turnLeft(getHeading() % 90);
        ahead(distParede);
        turnGunRight(90);
        turnRight(90);

        while (true) {
            // Executa a lógica de movimentação e escaneamento
            for (int parede = 0; parede < 2; parede++) {
                ahead(distParede);
                back(distParede);

                if (parede == 2) {
                    parede = 0;
                }
            }
            turnRight(90);
        
            execute(); // Garante que as ações sejam executadas
        }
    }

    //private void executeMovements(double distParede) {
      //  while (true) {
		//scan();
          ///  for (int parede = 0; parede < 2; parede++) {
             //   ahead(distParede);
               // back(distParede);

                //if (parede == 2) {
                 //   parede = 0;
//                }
  //          }
   //         turnRight(90);
   //     }
 //   }

    public void onScannedRobot(ScannedRobotEvent e) {
        double angleToEnemy = getHeadingRadians() + e.getBearingRadians();
        double turnToEnemy = Utils.normalRelativeAngle(angleToEnemy - getRadarHeadingRadians());
        double extraTurn = Math.atan(36.0 / e.getDistance()) * (turnToEnemy >= 0 ? 1 : -1);
        setTurnRadarRightRadians(turnToEnemy + extraTurn);

        // Cálculo do ângulo para girar a arma
        double absoluteBearing = getHeadingRadians() + e.getBearingRadians();
        double gunTurn = Utils.normalRelativeAngle(absoluteBearing - getGunHeadingRadians());

        // Ajuste da arma para atirar na posição futura
        setTurnGunRightRadians(gunTurn);

        // Disparo com potência calculada
        double firePower = decideFirePower(e);
        setFire(firePower);

        // Continua girando o radar para manter o rastreamento
        //scan();
    }

    public double decideFirePower(ScannedRobotEvent e) {
        // Método que decide a potência do tiro com base em várias condições.

        double firePower = getOthers() == 1 ? 2.0 : 3.0;
        // Se houver apenas um robô inimigo, define a potência do tiro como 2.0. Caso contrário, define como 3.0.

        if (e.getDistance() > 200) {
            firePower = 1.0;
            // Se a distância até o robô inimigo for maior que 200 unidades, define a potência do tiro como 1.0.
        } else if (e.getDistance() < 100) {
            firePower = 3.0;
            // Se a distância até o robô inimigo for menor que 100 unidades, define a potência do tiro como 3.0.
        }

        // Ajusta a potência do tiro com base na energia atual do robô.
        // Menor potência se a energia estiver baixa.
        if (getEnergy() < 1) {
            firePower = 0.1;
            // Se a energia do robô for menor que 1, define a potência do tiro como 0.1.
        } else if (getEnergy() < 10) {
            firePower = 1.0;
            // Se a energia do robô for menor que 10, define a potência do tiro como 1.0.
        }

        return Math.min(e.getEnergy() / 4, firePower);
        // Evita desperdiçar energia garantindo que a potência do tiro não seja maior que um quarto da energia do inimigo.
    }

    public void onHitRobot(HitRobotEvent e) {
       // double eX = getX();
        //double eY = getY();

       // double altura = getBattleFieldHeight();
       // double largura = getBattleFieldWidth();

      //  double nextWall = Math.max(getBattleFieldWidth(), getBattleFieldHeight());

       // if (e.getBearing() > -90 && e.getBearing() < 90 && eX < 50) {
       //     turnRight(90);
       //     ahead(nextWall);
      //  } else if (e.getBearing() > -90 && e.getBearing() < 90 && eX > largura - 50) {
      //      turnLeft(90);
       //     ahead(nextWall);
     //   }
      //  if (e.getBearing() < -90 || e.getBearing() > 90 && eY < 50) {
       //     turnRight(90);
      //      ahead(nextWall);
       // } else if (e.getBearing() < -90 || e.getBearing() > 90 && eY > altura - 50) {
      //      turnLeft(90);
         //   ahead(nextWall);
      // }
	   turnLeft(90);
	 back(100);
    }
	

	public void onHitByBullet(HitByBulletEvent e) {
        double escapeWall = Math.max(getBattleFieldWidth(), getBattleFieldHeight());
     //   fuga();
	 turnLeft(90);
	 back(100);
        
    }


  
   // public void fuga() {
        // retornam as coordenadas da arena
        //double x = getX();
        //double y = getY();
        // retornam altura e largura da arena
        //double battlefieldWidth = getBattleFieldWidth();
       // double battlefieldHeight = getBattleFieldHeight();
        // utiliza as coordenadas e tamanho do campo de batalha para
        // que o robô fuja na direção correta evitando que ele colida com
        // a parede em que ele esteja passeando
       // if (x < 50) {
         //   turnRight(90);
       //     ahead(200);
       // } else if (x > battlefieldWidth - 50) {
        //    turnLeft(90);
        //    ahead(200);
       // }
       // if (y < 50) {
          //  turnRight(90);
           // ahead(200);
     //   } else if (y > battlefieldHeight - 50) {
       //     turnLeft(90);
        //    ahead(200);
     //   }
    //}
}

