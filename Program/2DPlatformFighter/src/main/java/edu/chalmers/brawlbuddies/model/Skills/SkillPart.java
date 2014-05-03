package edu.chalmers.brawlbuddies.model.Skills;
/**
 * A interface describing a part of a skill.
 * @author David Gustafsson
 * @revised Patrik Haar
 * @version 0.1
 */
public interface SkillPart {
	/**
	 * Activate the SkillPart with the Casting Character as a argument
	 * @param c The ICharacter activating the skill.
	 * @param delta The time passed since last call in milliseconds.
	 * @return <code>true</code> if the SkillPart executed, <code>false</code> otherwise.
	 */
	public boolean activate(ICharacter c, int delta);
	public void setCreatorID(int id);
}