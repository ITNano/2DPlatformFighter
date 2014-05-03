package edu.chalmers.brawlbuddies.model.Skills;

import java.util.ArrayList;
import java.util.List;

public class Skill implements ISkill{
	
	private int cooldown;
	private int cooldownLeft = 0;
	private int ownerID;
	
	private List<SkillPart> skillParts = new ArrayList<SkillPart>();
	
	private boolean skillActive;
	private int currentSkillpart;
	private ICharacter currentCaster;
	
	public Skill(int cd, int id) {
		this.cooldown = cd;
		this.ownerID = id;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void activate(ICharacter ch) {
		if (isReady()) {
			activate(ch, 0);
		}
	}
	
	private void activate(ICharacter ch, int delta) {
		if (!skillActive) {
			skillActive = true;
			currentCaster = ch;
			resetCooldown();
		}
		boolean aborted = false;
		for (int i=currentSkillpart; i<skillParts.size(); i++) {
			if (!skillParts.get(i).activate(ch, delta)) {
				currentSkillpart = i;
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
		if (!skillActive) {
			cooldownLeft -= delta;
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
	 * Resets the cooldown to full.
	 */
	public void resetCooldown() {
		cooldownLeft = cooldown;
	}
}
