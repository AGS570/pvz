package pvz.logic.gameobjects;

import pvz.logic.Game;
import pvz.logic.Position;
import pvz.logic.ZombiesManager;
import pvz.view.Messages;

public class Zombie {
	private int health;
	private int fr;
	private ZombiesManager zmn;
	private Position pos;
	private int moveCycles;
	private int damage;
	
	public Zombie(Position pos, ZombiesManager zmn) {
		this.health = 5;
		this.zmn = zmn;
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
		if (pos.getCol() == pos2.getCol() && pos.getRow() == pos2.getRow()) {
			return true;
		}
		return false;
	}


	public void update() {
		// TODO Auto-generated method stub
		if (this.pos.getCol() == 0) {
			this.zmn.finishGame(false);
			return;
		}
		Position nextPos = new Position(this.pos.getRow(),this.pos.getCol()-1);
		
		if(this.zmn.isPositionEmpty(nextPos)) {
			
			if(moveCycles <= 0) {
				movement(nextPos);
				moveCycles = 2;
			}else {
				this.moveCycles--;
			}
		}else {
			if(this.zmn.plantsAhead(nextPos)) {
				this.zmn.attack(nextPos,this.damage);
			}
		}
	}


	private void movement(Position nextPos) {
		// TODO Auto-generated method stub
		this.pos = nextPos;
	}
	
	public boolean isAlive() {
		return this.health>0;
	}


	public void receiveInteraction(int damage) {
		// TODO Auto-generated method stub
		this.health -= damage;
	}
	
}
