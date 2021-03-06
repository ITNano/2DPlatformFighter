package edu.chalmers.brawlbuddies.model.skills;

import java.util.ArrayList;
import java.util.List;

import edu.chalmers.brawlbuddies.model.world.ICharacter;

/**
 * Describes a skill that can be used by a character
 * @author Patrik Haar
 * @version 1.0
 *
 */

public class Skill implements ISkill{
	
	private int id;
	private int typeID;
	private int ownerID;
	private int cooldown;
	private int cooldownLeft = 0;
	
	private List<SkillPart> skillParts = new ArrayList<SkillPart>();
	
	private boolean skillActive;
	private int currentSkillpart;
	private ICharacter currentCaster;
	private String animName;
	
	/**
	 * Creates a new Skill with
	 * @param cd
	 * @param id
	 * @param typeID
	 * @param ownerID
	 * @param animation
	 */
	public Skill(int cd, int id, int typeID, int ownerID, String animation) {
		this.cooldown = cd;
		this.id = id;
		this.typeID = typeID;
		this.ownerID = ownerID;
		this.animName = animation;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean activate(ICharacter ch) {
		if (isReady()) {
			activate(ch, 1);
			return true;
		}
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getAnimationName() {
		return this.animName;
	}
	
	/**
	 * Activates the skill
	 * @param ch
	 * @param delta
	 */
	private void activate(ICharacter ch, int delta) {
		if (!skillActive) {
			resetCooldown();
			skillActive = true;
			currentCaster = ch;
		}
		boolean aborted = false;
		for (int i=currentSkillpart; i<skillParts.size(); i++) {
			delta -= skillParts.get(i).update(delta);
			if (!skillParts.get(i).activate(ch)) {
				currentSkillpart = i;
				delta -= 3;
				aborted = true;
				break;
			}
		}
		if (!aborted) {
			currentSkillpart = 0;
			skillActive = false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public int getID() {
		return this.id;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getTypeID() {
		return typeID;
	}
	
	/**
	 * Adds a SkillPart to this Skill.
	 * @param s The SkillPart to be added.
	 */
	public void addSkillPart(SkillPart s) {
		s.setCreatorID(ownerID);
		skillParts.add(s);
	}
	
	/**
	 * Checks if the skill is ready to use.
	 * @return <code>true</code> if skill is ready, <code>false</code> otherwise.
	 */
	public boolean isReady() {
		return cooldownLeft<=0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean update(int delta) {
		cooldownLeft -= delta;
		if (!skillActive) {
			if (isReady()){
				cooldownLeft = 0;
				return true;
			} else {
				return false;
			}
		} else {
			activate(currentCaster, delta);
			return false;
		}
	}
	
	/**
	 * Resets the cooldown time;
	 */
	public void resetCooldown() {
		cooldownLeft = cooldown;
	}
	
	/**
	 * Returns the ownerID
	 * return the ID of the character owning the skill
	 */
	public int getOwnerID() {
		return ownerID;
	}
	/**
	 * {@inheritDoc}
	 */
	public String toString(){
		return "Skill:" + " typID = " + typeID + " ownerID = " + ownerID + " cooldown = " + cooldown + " skillActive= " + skillActive + " currentSkillPart = " + currentSkillpart + " animName = " + animName;
	}

	public int getCooldown() {
		return cooldown;
	}
}
