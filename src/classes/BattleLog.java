package classes;

import javafx.scene.paint.Color;

public class BattleLog {
        private int time;
        private Color enemyColour;
        private boolean victory;
        public BattleLog(int t,Color c, boolean victory) {
            this.time = t;
            this.enemyColour = c;
            this.victory = victory;
        }

        public int getTime() {
            return time;
        }

        public Color getEnemyColour() {
            return enemyColour;
        }

        public boolean wasVictory() {
            return victory;
        }
}
