package edu.chalmers.brawlbuddies.model;

import java.util.List;

import edu.chalmers.brawlbuddies.controller.Player;
import edu.chalmers.brawlbuddies.model.Skills.Effect;
import edu.chalmers.brawlbuddies.model.world.Character;
import edu.chalmers.brawlbuddies.model.world.GameObject;
import edu.chalmers.brawlbuddies.model.world.Projectile;
import edu.chalmers.brawlbuddies.model.world.World;
import edu.chalmers.brawlbuddies.util.CharacterActionListener;

/**
 * Holds the logic for the game.
 * 
 * @author Patrik Haar
 * @version 0.1
 */
public class BrawlBuddies implements CharacterActionListener {

	private World world;
	private Player[] players;

	public BrawlBuddies(Player[] p, World w) {
		players = p;
		for(int i = 0 ; i < players.length ; i++){
			players[i].getCharacter().addCharacterActionListener(this);
			System.out.println("added a CharacterListener to player " + i);
		}
		world = w;
	}

	/**
	 * Returns the players playing the game.
	 * 
	 * @return The Players.
	 */
	public Player[] getPlayers() {
		return players;
	}
	public List<Projectile> getProjectiles(){
		return world.getProjectiles();
	}
	/**
	 * Moves the character connected to the player in the given direction.
	 * 
	 * @param player
	 *            The Player connected to the event.
	 * @param dir
	 *            The Direction to move the character.
	 */
	public void move(Player player, Direction dir) {
		player.getCharacter().move(dir);
	}

	/**
	 * Make the character connected to the player jump.
	 * 
	 * @param player
	 *            The Player connected to the event.
	 */
	public void jump(Player player) {
		player.getCharacter().makeJump();
	}
	
	/**
	 * Sets the aim of the character of the given player
	 * @param player The player
	 * @param aimPosition The position to aim at
	 */
	public void setPlayerAim(Player player, Position aimPosition, boolean isRelative){
		player.getCharacter().setAim(aimPosition, isRelative);
	}

	/**
	 * Updates the Player´s positions and states.
	 * 
	 * @param delta
	 *            Time since last update in milliseconds.
	 */
	public void update(int delta) {
		world.update(delta);
	}

	/**
	 * Returns the world used by the game.
	 * 
	 * @return The world used by the game.
	 */
	public World getWorld() {
		return world;
	}

	public void characterActionPerformed(Projectile p) {
		world.getProjectiles().add(p);
		
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
