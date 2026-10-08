package pvz.logic.gameobjects;

import java.util.ArrayList;
import java.util.List;

public class PeaShooterList {
	private List<PeaShooter> lista;
	
	public PeaShooterList() {
		this.lista = new ArrayList();
	}
	
	public void  insert(PeaShooter p) {
		this.lista.add(p);
	}
	
	public void remove(PeaShooter p) {
		this.lista.remove(p);
	}
	
	public int size() {
		return this.lista.size();
	}
	
	
}
