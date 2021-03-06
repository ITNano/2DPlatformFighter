package edu.chalmers.brawlbuddies.model.skills;

import edu.chalmers.brawlbuddies.model.world.ICharacter;

/**
 * A interface describing a part of a skill.
 * @author David Gustafsson
 * @revised Patrik Haar
 * @version 1.0
 */
public interface SkillPart {
	/**
	 * Activate the SkillPart with the Casting Character as a argument
	 * @param c The ICharacter activating the skill.
	 * @return <code>true</code> if the SkillPart executed, <code>false</code> otherwise.
	 */
	public boolean activate(ICharacter c);
	
	/**
	 * Updates the SkillPart with the time passed for timed actions.
	 * @param delta The time passed in milliseconds.
	 * @return How much time the SkillPart consumed in milliseconds.
	 */
	public int update(int delta);
	public void setCreatorID(int id);
}
