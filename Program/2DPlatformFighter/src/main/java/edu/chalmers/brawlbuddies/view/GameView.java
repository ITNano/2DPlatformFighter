package edu.chalmers.brawlbuddies.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.tiled.TiledMap;

import edu.chalmers.brawlbuddies.eventbus.EventBus;
import edu.chalmers.brawlbuddies.eventbus.EventBusEvent;
import edu.chalmers.brawlbuddies.eventbus.IEventBusSubscriber;

public class GameView implements IEventBusSubscriber, IView {
	private TiledMap map;
	private Map<Integer, Animation> objectAnims;
	private Map<Integer, IDrawable> objects;
	private Map<Integer, HudImage> huds = new HashMap<Integer, HudImage>();
	
	private SideScroller scroller;
	private boolean resolutionSet = false;

	public GameView() {
		objectAnims = new TreeMap<Integer, Animation>();
		objects = new TreeMap<Integer, IDrawable>();
		scroller = new SideScroller(null, null, 200, 200, 1.5d);
		EventBus.getInstance().addSubscriber(this);
	}

	public void render(GameContainer gc, Graphics g) {
		if(!resolutionSet){
			this.scroller.setResolution(gc.getWidth(), gc.getHeight());
			this.resolutionSet = true;
		}
		
		g.scale(scroller.getScale(), scroller.getScale());
		g.translate(scroller.getX(), scroller.getY());
		map.render(0, 0);
		for (Map.Entry<Integer, IDrawable> entry : objects.entrySet()) {
			entry.getValue().render(gc, g);
		}
		
		g.translate(-scroller.getX(), -scroller.getY());
		g.scale(1.0f/scroller.getScale(), 1.0f/scroller.getScale());
		for (Map.Entry<Integer, HudImage> entry : huds.entrySet()) {
			entry.getValue().render(gc, g);
		}
	}

	public void eventPerformed(EventBusEvent event) {
		if (event.getName().equals("createObject")) {
			createObjectImage((IWrapper) event.getRecipient());
		} else if (event.getName().equals("removeObject")) {
			removeObjectImage((IWrapper) event.getRecipient());
		} else if (event.getName().equals("updateObject")) {
			updateObject((IWrapper) event.getRecipient(),
					(IWrapper) event.getActor());
		} else if (event.getName().equals("skillUsed")) {
			((CharacterImage) objects.get((Integer) event.getRecipient()))
					.useSkill((IWrapper) event.getActor());
		} else if (event.getName().equals("createMap")) {
			this.map = (TiledMap) event.getRecipient();
			this.scroller.setMapSize(map.getWidth()*map.getTileWidth(), map.getHeight()*map.getTileHeight());
		} else if (event.getName().equals("createAnimation")) {
			this.objectAnims.put(
					((IWrapper) event.getRecipient()).getTypeID(),
					AnimationMapFactory.create(
							((IWrapper) event.getRecipient()).getTypeID()).get(
							"idle"));
		}
	}

	public HudImage createHud(IWrapper obj) {
		return new HudImage(obj);
	}

	public void addHud(IWrapper obj) {
		if (obj.getClass() == SkillWrapper.class) {
			SkillWrapper skill = (SkillWrapper) obj;
			if (!huds.containsKey(skill.getOwnerID())) {
				HudImage hud = createHud(obj);
				huds.put(skill.getOwnerID(), hud);
			}
		} else if (obj.getClass() == HealthWrapper.class) {
			HealthWrapper health = (HealthWrapper) obj;
			if (!huds.containsKey(health.getID())) {
				HudImage hud = createHud(obj);
				huds.put(health.getID(), hud);
			}
		}

	}

	public void removeObjectImage(IWrapper obj) {
		int uniqeID = obj.getUniqeID();
		objects.remove(uniqeID);
	}

	public void updateObject(IWrapper obj1, IWrapper obj2) {
		if (obj1.getClass() == SkillWrapper.class) {
			huds.get(obj1.getUniqeID()).update(obj1, obj2);
		} else if (obj1.getClass() == HealthWrapper.class) {
			huds.get(obj1.getUniqeID()).update(obj1, obj2);
		} else {
			objects.get(obj1.getUniqeID()).update(obj1, obj2);
		}
	}

	private void addObjectImage(IDrawable drawable) {
		if (drawable != null) {
			objects.put(drawable.getUniqeID(), drawable);
		}
	}

	private IDrawable createObjectImage(IWrapper obj) {
		if (obj.getClass() == SkillWrapper.class) {
			createSkillImage(obj);
			return null;
		} else if (obj.getClass() == HealthWrapper.class) {
			createHealthImage(obj);
			return null;
		} else if (obj.getClass() == CharacterWrapper.class) {
			CharacterImage image = new CharacterImage((CharacterWrapper) obj);
			addObjectImage(image);
			return image;
		} else if (obj.getClass() == ProjectileWrapper.class) {
			ProjectileImage image = new ProjectileImage(objectAnims.get(obj
					.getTypeID()), obj.getUniqeID());
			addObjectImage(image);
			return image;
		} else {
			return null;
		}
	}

	private void createSkillImage(IWrapper obj) {
		SkillWrapper skillW = (SkillWrapper) obj;
		if (!huds.containsKey(skillW.getOwnerID())) {
			addHud(obj);
		}
		SkillImage skill = new SkillImage(skillW);
		huds.get(skill.getID()).addSkill(skill);
	}

	private void createHealthImage(IWrapper obj) {
		HealthWrapper healthW = (HealthWrapper) obj;
		System.out.println();
		if (!huds.containsKey(healthW.getID())) {
			addHud(obj);
		}
		HealthImage image = new HealthImage(healthW);
		huds.get(healthW.getID()).addHealthBar(image);
	}
	
	public void resolutionUpdated(){
		this.resolutionSet = false;
	}

}
