package edu.chalmers.brawlbuddies.model.world;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import edu.chalmers.brawlbuddies.Constants;
import edu.chalmers.brawlbuddies.model.Position;
import edu.chalmers.brawlbuddies.model.Velocity;
import edu.chalmers.brawlbuddies.model.skills.ISkill;
import edu.chalmers.brawlbuddies.model.skills.SkillFactory;
import edu.chalmers.brawlbuddies.util.ResourceLoader;
import edu.chalmers.brawlbuddies.util.ShapeFactory;
import edu.chalmers.brawlbuddies.util.XMLReader;

/**
 * A factory for creating characters.
 * @author Patrik Haar
 * @author Matz Larsson
 * @version 1.0
 */
public class CharacterFactory {
	
	/**
	 * Creates a character by reading from the xml file with
	 * the provided name.
	 * @param charName The name of the character.
	 * @return The created character with all stats set.
	 */
	public static ICharacter create(String charName) {
		return create(charName, 0, 0);
	}
	
	/**
	 * Creates a character by reading from the xml file with
	 * the provided name at the specified location.
	 * @param charName The name of the character.
	 * @param pos The Position of the character.
	 * @return The created character with all stats set.
	 */
	public static ICharacter create(String charName, Position pos) {
		return create(charName, pos.getX(), pos.getY());
	}
	
	/**
	 * Creates a character by reading from the xml file with
	 * the provided name at the specified location.
	 * @param charName The name of the character.
	 * @param x The x-location of the created Character.
	 * @param y The y-location of the created Character.
	 * @return The created character with all stats set.
	 */
	public static ICharacter create(String charName, float x, float y) {
		
		Document xmlDoc = XMLReader.getDocument(Constants.CHARACTER_DATA + charName + ".xml");
		Element rootNode = xmlDoc.getDocumentElement();
		
		// Setting the projectile fire offset
		String[] projParams = rootNode.getElementsByTagName("projOffset").item(0).getFirstChild().getNodeValue().split(",");
		Position projOffset = new Position(Float.parseFloat(projParams[0]),Float.parseFloat(projParams[1]));
		// Setting the hitbox
		NamedNodeMap shapeParams = rootNode.getElementsByTagName("hitbox").item(0).getAttributes();
		// Initiating the Character
		Character character = new Character(ShapeFactory.create(shapeParams.getNamedItem("shape").getNodeValue()
				, shapeParams.getNamedItem("parameters").getNodeValue(), x, y),
				Integer.parseInt(rootNode.getAttribute("id")), projOffset);
		CharacterWrapper wrapper = new CharacterWrapper(character);
		// Setting the health
		HealthWrapper health= new HealthWrapper(Float.parseFloat(xmlDoc.getElementsByTagName("health").item(0).getFirstChild().getNodeValue()), character.getID());
		character.setHealth(health);
		// Setting the movement
		float moveSpeed = Float.parseFloat(xmlDoc
				.getElementsByTagName("movespeed").item(0).getFirstChild().getNodeValue());
		float jumpSpeed = Float.parseFloat(xmlDoc
				.getElementsByTagName("jumpingpower").item(0).getFirstChild().getNodeValue());
		int maxJumps = Integer.parseInt(xmlDoc
				.getElementsByTagName("maxjumps").item(0).getFirstChild().getNodeValue());
		character.setMovement(new JumpMovement(new Velocity(moveSpeed, 0), jumpSpeed, maxJumps));
		// Setting the Skills
		NodeList skillList = rootNode.getElementsByTagName("skill");
		ISkill[] skills = new ISkill[skillList.getLength()];
		for (int i=0; i<skillList.getLength(); i++) {
			skills[i] = SkillFactory.create(skillList.item(i)
					.getFirstChild().getNodeValue(), character.getID());
		}
		character.setSkills(skills);
		
		return wrapper;
	}
	/**
	 * Get a map of the available characters with their name and document file path.
	 * @return Map<String, String> - a map of available characters
	 */
	public static Map<String, String> getAvailableCharacters(){
		List<String> fileNames = ResourceLoader.listFileNames(Constants.CHARACTER_DATA);
		Map<String, String> charNames = new LinkedHashMap<String, String>();
		for(int i = 0; i<fileNames.size(); i++){
			Document xmlDoc = XMLReader.getDocument(fileNames.get(i));
			Element rootNode = xmlDoc.getDocumentElement();
			String characterName = rootNode.getElementsByTagName("name").item(0).getFirstChild().getNodeValue();

			String path = fileNames.get(i);
			int startIndex = Math.max(path.lastIndexOf("/"), path.lastIndexOf("\\"))+1;
			String filename = path.substring(startIndex).substring(0, path.lastIndexOf(".")-startIndex);
			charNames.put(filename, characterName);
		}
		return charNames;
	}
}
