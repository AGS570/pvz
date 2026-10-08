package logic.gameobjects;

import java.util.ArrayList;
import java.util.List;

public class SunflowerList {
	private List<SunFlower> lista;
	
	public SunflowerList() {
		this.lista = new ArrayList();
	}
	
	public void  insert(SunFlower p) {
		this.lista.add(p);
	}
	
	public void remove(SunFlower p) {
		this.lista.remove(p);
	}
	
	public int size() {
		return this.lista.size();
	}
	
}
