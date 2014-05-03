package edu.chalmers.brawlbuddies.model.Skills;

import edu.chalmers.brawlbuddies.model.world.*;
import edu.chalmers.brawlbuddies.model.world.Character;

/**
 * Describes a damage effect
 * 
 * @author David Gustafsson
 * 
 */
public class DamageEffect implements Effect {
	private int creatorId;
	private float damage;

	/**
	 * Creates a Effect with a playerId describing the playerSkills or effect
	 * that belongs to the effect and damage describing the amount of damage a
	 * object takes when exposed to the effect.
	 * 
	 * @param creatorId
	 * @param damage
	 */
	public DamageEffect(float damage) {
		this.damage = damage;
	}
	public void setCreatorId(int creatorId){
		this.creatorId = creatorId;
	}

	public boolean effect(GameObject sender , GameObject reciever) {
		if (reciever instanceof ICharacter) {
			if (((ICharacter) reciever).getID() != creatorId) { //TODO fix when gameObject have ID
				System.out.println("i hit someone");
				((ICharacter) reciever).takeDamage(damage);
				return true;
			}
		} else if (reciever instanceof DamageAble) {
			((DamageAble) reciever).takeDamage(damage);
			return true;
		}
		return false;
	}
}