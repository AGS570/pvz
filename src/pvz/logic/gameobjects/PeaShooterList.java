package pvz.logic.gameobjects;

import java.util.ArrayList;
import java.util.List;

import pvz.logic.Position;

public class PeaShooterList {
	private List<PeaShooter> lista;
	
	public PeaShooterList() {
		this.lista = new ArrayList();
	}
	
	public boolean  insert(PeaShooter p) {
		return this.lista.add(p);
	}
	
	public void remove(PeaShooter p) {
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
	        	return lista.get(i).getIcon();
	        }
	        i++;
	    }

	    return null;
	}
	
	
}
