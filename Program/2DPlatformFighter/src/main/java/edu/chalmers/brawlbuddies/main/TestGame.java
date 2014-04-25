package edu.chalmers.brawlbuddies.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.tiled.TiledMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.thoughtworks.xstream.XStream;

import edu.chalmers.brawlbuddies.controller.Player;
import edu.chalmers.brawlbuddies.model.Aim;
import edu.chalmers.brawlbuddies.model.BrawlBuddies;
import edu.chalmers.brawlbuddies.model.Direction;
import edu.chalmers.brawlbuddies.model.Position;
import edu.chalmers.brawlbuddies.model.Skills.Effect;
import edu.chalmers.brawlbuddies.model.world.Character;
import edu.chalmers.brawlbuddies.model.world.CharacterFactory;
import edu.chalmers.brawlbuddies.model.world.GameMap;
import edu.chalmers.brawlbuddies.model.world.GameObject;
import edu.chalmers.brawlbuddies.model.world.Projectile;
import edu.chalmers.brawlbuddies.model.world.ProjectileCreator;
import edu.chalmers.brawlbuddies.model.world.World;
import edu.chalmers.brawlbuddies.util.CharacterActionListener;

/**
 * A test game for the basic function of the model
 * 
 * @author David Gustafsson
 * 
 */

public class TestGame extends BasicGame implements CharacterActionListener {
	private BrawlBuddies game;
	private ProjectileCreator pc;
	private List<Projectile> projectiles = new ArrayList<Projectile>();

	/**
	 * Creates a new instance of test game
	 * 
	 * @param game
	 *            the game model for this test game
	 */
	public TestGame() {
		super("Demo");
	}
	public void startGame(Player[] players){
		game = new BrawlBuddies(players, new World(players, new GameMap()));
	}

	/**
	 * Renders the test game
	 */
	public void render(GameContainer gc, Graphics g) throws SlickException {
		game.getWorld().getMap().render(0,0);

		for(Player p:game.getPlayers()){
			p.getCharacter().draw();
			// Following Code is used to check if bob takes damage or if hes dead
			if( p.getCharacter().getHealth() == 0){
				g.setColor(Color.gray);
				g.drawString("Bob is dead. RIP BOB", 25, 25);
			}
			else if( p.getCharacter().getMaxHealth() != p.getCharacter().getHealth()){
				g.setColor(Color.darkGray);
				g.drawString("Bob is hurt", 25 , 25);
			}
			else{
				g.setColor(Color.black);
				g.drawString("Bob is ok used left shift to hurt him", 25, 25);
			}

		}
		for(int i=0; i< projectiles.size(); i++) {
			g.setColor(Color.black);
			g.fill(projectiles.get(i).getShape());
		}
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		this.pc = new ProjectileCreator(new Circle(0,0,10), 1000, 5000);
		Player[] players = { new Player("Player1", generateBob()) };
		// Make this testgame a listener for events from character
		players[0].getCharacter().addCharacterActionListener(this);
		this.startGame(players);
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		Player[] players = game.getPlayers();
		Direction dir = Direction.NONE;
		if (gc.getInput().isKeyDown(Input.KEY_W)) {
			dir = dir.add(Direction.UP);
		}
		if (gc.getInput().isKeyDown(Input.KEY_S)) {
			dir = dir.add(Direction.DOWN);
		}
		if (gc.getInput().isKeyDown(Input.KEY_D)) {
			dir = dir.add(Direction.RIGHT);
		}
		if (gc.getInput().isKeyDown(Input.KEY_A)) {
			dir = dir.add(Direction.LEFT);
		}
		game.move(players[0], dir);
		
		if (gc.getInput().isKeyPressed(Input.KEY_SPACE)) {
			game.jump(players[0]);
		}
		if (gc.getInput().isKeyPressed(Input.KEY_RSHIFT)) {
			players[0].getCharacter().activateSkill(0);
			//projectiles.add(pc.fire(players[0].getCharacter().getCenterPosition(), players[0].getCharacter().getAim()));
		}
		if (gc.getInput().isKeyPressed(Input.KEY_LSHIFT)){
			players[0].getCharacter().activateSkill(1);
		}
		game.update(delta);
		for(int i=0; i< projectiles.size(); i++) {
			if(projectiles.get(i).isActive()) {
				projectiles.get(i).update(delta);
			} else {
				projectiles.remove(i);
			}
		}
	}

	private Character generateBob() {
		return CharacterFactory.createCharacter("bob");
	}
	
	
	
	public static void main(String[] args) {
		try {
			TestGame game = new TestGame();
			AppGameContainer appgc;
			appgc = new AppGameContainer(game);

			appgc.setDisplayMode(1024, 768, false);
			appgc.start();
		} catch (SlickException ex) {
			Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	public void characterActionPerformed(Projectile p) {
		projectiles.add(p);
		
	}
	public void characterActionPerformed(GameObject o) {
		// TODO Auto-generated method stub
		
	}
	public void characterActionPerformed(Character c, Effect e) {
		// TODO Auto-generated method stub
		
	}
	public void characterActionPerformed(Effect e) {
		// TODO Auto-generated method stub
		
	}
}
