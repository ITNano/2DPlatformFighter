package edu.chalmers.brawlbuddies.view.menu;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import edu.chalmers.brawlbuddies.Constants;
import edu.chalmers.brawlbuddies.view.SideScroller;

public class SimpleMenuView implements MenuView{
	
	private List<MenuItem> items;
	private int selectedIndex = 0;
	private ParticleHandler particles;

	public SimpleMenuView() {
		items = new ArrayList<MenuItem>();
	}
	
	private void initParticles(int x, int y){
		try {
			particles = new ParticleHandler(Constants.IMAGES + "menus/particle.png");
			particles.addEmitter(Constants.DATA + "menus/background.xml", x, y);
		} catch (SlickException e) {
			System.out.println("Failed while reading particles");
		}
	}
	
	public void update(int delta){
		if(particles != null){
			particles.update(delta);
		}
	}

	public void render(GameContainer gc, Graphics g){
		if(particles == null){
			initParticles(gc.getWidth()/2, gc.getHeight()/2);
		}
		particles.render();
		for(MenuItem item : this.items){
			item.render(gc, g);
		}
	}
	
	public void add(MenuItem item){
		if(item != null){
			this.items.add(item);
		}
	}
	
	public void clearItems(){
		if(items != null){
			this.items.clear();
		}
	}
	
	public List<MenuItem> getMenuItems(){
		return this.items;
	}
	
	public MenuItem get(String itemName){
		for(MenuItem item : this.items){
			if(item.getItemName().equals(itemName)){
				return item;
			}
		}
		
		return null;
	}

	public MenuItem getSelectedItem(){
		return this.items.get(selectedIndex);
	}
	
	public void setSelectedItem(MenuItem item){
		for(int i = 0; i<items.size(); i++){
			if(items.get(i) == item){
				setSelection(i);
			}
		}
	}
	
	public void updateContents(){
		this.particles = null;
		setSelection(0);
		for(MenuItem item : this.items){
			item.recalculate();
		}
	}
	
	public void nextItem(){
		setSelection((this.selectedIndex+1)%items.size());
	}
	
	public void prevItem(){
		setSelection((this.selectedIndex-1+items.size())%items.size());
	}
	
	private void setSelection(int selectedIndex){
		if(selectedIndex>=0 && selectedIndex<this.items.size()){
			this.items.get(this.selectedIndex).setActive(false);
			this.selectedIndex = selectedIndex;
			this.items.get(this.selectedIndex).setActive(true);
		}
	}

	public SideScroller getScroller() {
		return null;
	}
}
