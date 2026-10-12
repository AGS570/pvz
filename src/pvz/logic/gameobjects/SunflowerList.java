package pvz.logic.gameobjects;

import java.util.ArrayList;
import java.util.List;

import pvz.logic.Position;

public class SunflowerList {
	private List<SunFlower> lista;
	
	public SunflowerList() {
		this.lista = new ArrayList();
	}
	
	public boolean  insert(SunFlower p) {
		return this.lista.add(p);
	}
	
	public void remove(SunFlower p) {
		this.lista.remove(p);
	}
	
	public int size() {
		return this.lista.size();
	}
	
	public void update() {
		for(int i =0; i < this.lista.size(); i++) {
			if (this.lista.get(i).isAlive())
				this.lista.get(i).update();
			else remove(lista.get(i));
		}
	}
	public int getSun() {
		int ret =0;
		for(int i =0; i < lista.size(); i++) {
			ret += lista.get(i).getSun();
		}
		return ret;
	}
	
	public String PositionToString(Position pos) {
	    boolean encontrada = false;
	    int i = 0;

	    while (i < lista.size() && !encontrada) {
	        if(lista.get(i).isInPosition(pos)){
	        	return lista.get(i).getIcon();
	        }
	        i++;
	    }

	    return null;
	}

	public boolean receiveInteraction(Position nextPos, int damage) {
		// TODO Auto-generated method stub
		for(int i =0 ; i < lista.size();i++) {
			if(lista.get(i).isInPosition(nextPos)) {
				lista.get(i).receiveInteraction(damage);
				return true;
			}
		}
		return false;
	}
}
