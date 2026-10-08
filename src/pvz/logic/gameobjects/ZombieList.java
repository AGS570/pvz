package pvz.logic.gameobjects;

import java.util.ArrayList;
import java.util.List;

public class ZombieList {
private List<Zombie> lista;
	
	public ZombieList() {
		this.lista = new ArrayList();
	}
	
	public void  insert(Zombie p) {
		this.lista.add(p);
	}
	
	public void remove(Zombie p) {
		this.lista.remove(p);
	}
	
	public int size() {
		return this.lista.size();
	}
}
