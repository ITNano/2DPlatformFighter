package edu.chalmers.brawlbuddies.services.factories;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.chalmers.brawlbuddies.Constants;
import edu.chalmers.brawlbuddies.model.Aim;
import edu.chalmers.brawlbuddies.model.Skills.Effect;
import edu.chalmers.brawlbuddies.model.Skills.ProjectilePart;
import edu.chalmers.brawlbuddies.model.Skills.SelfCastPart;
import edu.chalmers.brawlbuddies.model.Skills.Skill;
import edu.chalmers.brawlbuddies.model.Skills.WaitPart;
import edu.chalmers.brawlbuddies.model.world.ProjectileCreator;

public class SkillFactory {

	public static Skill create(String skillName, int ownerID) {
		
		Document xmlDoc = XMLReader.getDocument(Constants.SKILLS + skillName.toLowerCase() + ".xml");

		Element rootNode = xmlDoc.getDocumentElement();
		// Creating the Skill with it's cooldown
		Skill skill = new Skill(Integer.parseInt(rootNode.getAttribute("cooldown")), ownerID);
		// Start building the skill with it's different parts
		NodeList skillParts = rootNode.getElementsByTagName("skillparts").item(0).getChildNodes();
		for (int i=0; i<skillParts.getLength(); i++) {
			if (skillParts.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Node skillPart = skillParts.item(i);
				
				// Projectiles
				if (skillPart.getNodeName().equalsIgnoreCase("projectile")) {
					ProjectileCreator projectile = ProjectileFactory.create(skillPart.getAttributes().getNamedItem("name").getNodeValue());
					Aim aim = null;
					float aimOffset = 0;
					NamedNodeMap projParams = skillPart.getAttributes();
					for (int j=0; j<projParams.getLength(); j++) {
						String attribute = projParams.item(j).getNodeName();
						if (attribute.equalsIgnoreCase("aim")) {
							String[] aimParams = projParams.item(j).getNodeValue().split(",");
							aim = new Aim(Float.parseFloat(aimParams[0]),Float.parseFloat(aimParams[1]));
						} else if (attribute.equalsIgnoreCase("aim_offset")) {
							aimOffset = Float.parseFloat(projParams.item(j).getNodeValue());
						}
					}
					skill.addSkillPart(new ProjectilePart(projectile, aim, aimOffset));
					
				// Self Cast
				} else if (skillPart.getNodeName().equalsIgnoreCase("selfcast")) {
					NodeList effectList = skillPart.getChildNodes();
					List<Effect> effects = new ArrayList<Effect>();
					for (int j=0; j<effectList.getLength(); j++) {
						if (effectList.item(j).getNodeType() == Node.ELEMENT_NODE) {
							effects.add(EffectFactory.create(effectList.item(j)));
						}
					}
					skill.addSkillPart(new SelfCastPart(effects));
					
				// Wait
				} else if (skillPart.getNodeName().equalsIgnoreCase("wait")) {
					skill.addSkillPart(new WaitPart(Integer.parseInt(skillPart.getFirstChild().getNodeValue())));
					
				// Not supported
				} else {
					throw new IllegalArgumentException("The SkillPart: \"" + skillPart.getNodeName()
						+ "\" is not supported");
				}
			}
		}
		
		
		return skill;
	}
	
}