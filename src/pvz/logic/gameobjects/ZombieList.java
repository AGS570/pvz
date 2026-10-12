package pvz.logic.gameobjects;

import java.util.ArrayList;
import java.util.List;

import pvz.logic.Position;

public class ZombieList {
private List<Zombie> lista;
	
	public ZombieList() {
		this.lista = new ArrayList();
	}
	
	public boolean  insert(Zombie p) {
		return this.lista.add(p);
	}
	
	public void remove(Zombie p) {
		this.lista.remove(p);
	}
	
	public int size() {
		return this.lista.size();
	}
	
	public String PositionToString(Position pos) {
	    boolean encontrada = false;
	    int i = 0;

	    while (i < lista.size() && !encontrada) {
	        if(lista.get(i).isInPosition(pos)){
	        	return lista.get(i).getDescription();
	        }
	        i++;
	    }

	    return null;
	}
	
	public void update() {
		for(int i =0; i < this.lista.size(); i++) {
			if (this.lista.get(i).isAlive())
				this.lista.get(i).update();
			else remove(lista.get(i));
		}
	}

		public void receiveInteraction(Position nextPos, int damage) {
		// TODO Auto-generated method stub
		for(int i =0 ; i < lista.size();i++) {
			if(lista.get(i).isInPosition(nextPos)) {
				lista.get(i).receiveInteraction(damage);
				return ;
			}
		}
		return ;
		}

		public boolean isEmpty() {
			// TODO Auto-generated method stub
			return this.lista.isEmpty();
		}
}
