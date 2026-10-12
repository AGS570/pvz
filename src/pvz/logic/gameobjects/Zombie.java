package pvz.logic.gameobjects;

import pvz.logic.Game;
import pvz.logic.Position;
import pvz.view.Messages;

public class Zombie {
	private int health;
	private int fr;
	private Game game;
	private Position pos;
	private int moveCycles;
	private int damage;
	
	public Zombie(Position pos, Game game) {
		this.health = 5;
		this.game = game;
		this.pos = pos;
		this.fr = 1;
		this.moveCycles = 2;
		this.damage = 1;
	}
	

	public String getDescription() {
		// TODO Auto-generated method stub
		return Messages.ZOMBIE_ICON.formatted(health);
	}
	
	public boolean isInPosition(Position pos2) {
		// TODO Auto-generated method stub
		if (pos.getCol() == pos2.getCol() & pos.getRow() == pos2.getRow()) {
			return true;
		}
		return false;
	}


	public void update() {
		// TODO Auto-generated method stub
		if (this.pos.getCol() == 0) {
			this.game.finishGame();
			return;
		}
		Position nextPos = new Position(this.pos.getRow(),this.pos.getCol()-1);
		
		if(this.game.isPositionEmpty(nextPos)) {
			
			if(moveCycles <= 0) {
				movement(nextPos);
				moveCycles = 2;
			}else {
				this.moveCycles--;
			}
		}else {
			if(this.game.plantsAhead(nextPos)) {
				this.game.attack(nextPos,this.damage);
			}
		}
	}


	private void movement(Position nextPos) {
		// TODO Auto-generated method stub
		this.pos = nextPos;
	}
	
}
