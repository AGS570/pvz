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
	        	return lista.get(i).getDescription();
	        }
	    }

	    return null;
	}
}
