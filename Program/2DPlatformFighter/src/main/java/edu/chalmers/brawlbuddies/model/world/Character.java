package edu.chalmers.brawlbuddies.model.world;

import java.awt.Font;
import java.io.ObjectStreamException;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import edu.chalmers.brawlbuddies.model.Aim;
import edu.chalmers.brawlbuddies.model.Direction;
import edu.chalmers.brawlbuddies.model.Position;
import edu.chalmers.brawlbuddies.model.Velocity;
import edu.chalmers.brawlbuddies.model.Skills.ICharacter;
import edu.chalmers.brawlbuddies.model.Skills.ISkill;
import edu.chalmers.brawlbuddies.model.Skills.ProjectilePart;
import edu.chalmers.brawlbuddies.model.Skills.SkillPart;
import edu.chalmers.brawlbuddies.services.factories.AnimationMapFactory;
import edu.chalmers.brawlbuddies.statuseffects.IStatusEffect;
import edu.chalmers.brawlbuddies.statuseffects.StatusEffectList;
import edu.chalmers.brawlbuddies.util.CharacterActionListener;
import edu.chalmers.brawlbuddies.util.CharacterActionSupport;

/**
 * A class to represent a player-controlled character.
 * 
 * @author Patrik Haar
 * @version 0.3
 * @revised David Gustafsson
 * @revised Matz Larsson
 */
@XStreamAlias("character")
public class Character extends GameObject implements ICharacter {
	
	private int typeID;
	private StatusEffectList statusEffectList = new StatusEffectList();
	
	@XStreamAlias("name")
	private String name;
	@XStreamAlias("bio")
	private String bio;
	@XStreamAlias("health")
	private Health health;

	private ISkill[] skills;

	private Aim aim = new Aim(1, 0);
	private Direction lastDir = Direction.NONE;
	private boolean lastAimLeft;

	private CharacterActionSupport sup = new CharacterActionSupport(); 

	//TODO Temporary for drawing stuff.
	private TrueTypeFont font = new TrueTypeFont(new Font("Serif", Font.PLAIN, 20), false);
	private Animation idleAnim;
	
	/**
	 * Creates a Character.
	 * 
	 * @param cd
	 *            The data from which the character gets its attributes from.
	 * @param player
	 *            The Player controlling the character.
	 */
	public Character(Shape shape, int id) {
		super(new JumpMovement(), shape);
		this.typeID = id;
		this.idleAnim = AnimationMapFactory.create(id).get("idle"); // TODO Highly temporary test for animations.
		idleAnim.setPingPong(true);
		idleAnim.setAutoUpdate(true);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}
	/**
	 * Set the skills for the character
	 * @param skills
	 */
	public void setSkills(ISkill[] skills) {
		this.skills = skills;
	}

	/**
	 * Activate a Skill in the skills array with the given positionNbr in the array if it exists
	 * 
	 * @param positionNbr
	 */
	public void activateSkill(int positionNbr) {
		if (skills != null && (positionNbr > -1) && (positionNbr < skills.length)
				&& skills[positionNbr] != null) {
			skills[positionNbr].activate(this);
		}
	}

	@Override
	public void setMovement(Movement movement) {
		if (!(movement instanceof JumpMovement)) {
			throw new IllegalArgumentException("Only JumpMovements accepted");
		} else {
			super.setMovement(movement);
		}
	}

	@Override
	public JumpMovement getMovement() {
		return (JumpMovement) super.getMovement();
	}

	public void setBaseSpeed(Velocity baseSpeed) {
		this.getMovement().setBaseSpeed(baseSpeed);
	}

	public void setBaseJumpSpeed(float baseJump) {
		this.getMovement().setBaseJumpSpeed(baseJump);
	}

	public void setMaxJumps(int maxJumps) {
		this.getMovement().setMaxJumps(maxJumps);
	}

	/**
	 * Updates the velocity and position of the Character and returns the new
	 * position.
	 * 
	 * @param delta The time passed since last update in milliseconds.
	 * @return The position after the movement.
	 */
	public Position update(int delta) {
		statusEffectList.Update(delta, this);
		updateCooldowns(delta);
		idleAnim.update(delta);
		return this.getMovement().nextPosition(this.getCenterPosition(), delta);
	}

	/**
	 * Makes the character move to the left/right/up/down depending on the
	 * Direction.
	 */
	public void move(Direction dir) {
		this.getMovement().move(dir);
		//updateAim(dir);
		lastDir = dir;
	}

	/**
	 * Updates the current Aim of the character.
	 * 
	 * @param dir
	 *            The Direction to aim in.
	 */
	//TODO remove when testing is done.
	public void updateAim(Direction dir) {
		// Logic for the aiming.
		if (dir != Direction.NONE) {
			// Sets the aim to current movement.
			this.aim = new Aim(dir);
		} else {
			// If no movement is done, aim is set to the last horizontal-facing
			// direction.
			this.aim = this.lastAimLeft ? new Aim(Direction.LEFT) : new Aim(
					Direction.RIGHT);
		}
		if (dir.getX() != 0) {
			lastAimLeft = dir.getX() < 0 ? true : false;
		}
	}

	/**
	 * Updates the cooldown of the skills with the time passed.
	 * @param delta Time past in milliseconds.
	 */
	private void updateCooldowns(int delta) {
		for (ISkill s : skills) {
			s.update(delta);
		}
	}
	
	/**
	 * Makes the character jump if able.
	 */
	public void makeJump() {
		this.getMovement().jump();
	}

	/**
	 * Cancel a jump during the upwards movement.
	 */
	public void cancelJump() {
		this.getMovement().cancelJump();
	}

	public Aim getAim() {
		return this.aim;
	}
	
	public void setAim(Position aimPosition, boolean isRelative){
		if(isRelative){
			this.aim.set(aimPosition);
		}else{
			this.aim.set(aimPosition.subtract(this.getCenterPosition()));
		}
	}

	public void setMaxHealth(float a) {
		health.setMaxHealth(a);
	}

	public void setHealth(float a) {
		this.health = new Health(a);
	}

	public void takeDamage(float a) {
		health.takeDamage(a);
	}

	public void heal(float a) {
		health.heal(a);
	}

	public float getHealth() {
		return health.getHp();
	}
	public float getMaxHealth(){
		return health.getMaxHealth();
	}
	public void restoreHealth() {
		health.restoreHp();
	}

	/**
	 * Returns a copy of this character.
	 */
	@Override
	public GameObject copy() {
		// FIXME temporary solution. No need to copy.
		return null;
	}

	// TODO Temporary draw method to use a shape as the image for the upcoming
	// demo.
	public void draw() {
		Graphics g = new Graphics();
		g.setColor(Color.black);
		if( health.getMissingHealth() > 0){
		g.setColor(Color.yellow);
		}
		if( isDead()){
		g.setColor(Color.red);
		}
		
		g.setFont(font);
		g.drawString(""+health.getHp(), getX(), getY()-30);
		
		idleAnim.getImage(idleAnim.getFrame()).draw(getX()+(lastAimLeft?0:idleAnim.getWidth()), getY(), lastAimLeft?idleAnim.getWidth():-idleAnim.getWidth(), idleAnim.getHeight());
	}
	
	public void addCharacterActionListener(CharacterActionListener listener){
		sup.addListener(listener);
	}
	
	public void removeCharacterActionListener(CharacterActionListener listener){
		sup.removeListener(listener);
	}
	
	public void updateProjectile(Projectile p){
		// Sends a projectile to the projectilelist in the CharacterAction listener
		sup.sendEvent(p);
	}
	
	public boolean isDead(){
		return health.isDead();
	}
	
	private Object readResolve() throws ObjectStreamException {
		this.setShape(new Rectangle(0, 0, 50, 80));
		/*
		 * this.setBaseVelocity(0, 0); this.setVariableVelocity(0, 0);
		 * this.jumpsLeft = this.maxJumps;
		 */
		return this;
	}
	
	public void push(Velocity v) {
		Movement mov = getMovement();
		mov.setOuterSpeed(mov.getOuterSpeed().getX()+v.getX(), mov.getOuterSpeed().getY()+v.getY());
	}

	@Override
	public int getTypeID() {
		return typeID;
	}

	public void applyStatusEffect(IStatusEffect effect) {
		statusEffectList.add(effect);
		
	}

	public void addScale(float scale) {
		super.getMovement().addScale(scale);
		
	}

	public void removeScale(float scale) {
		super.getMovement().removeScale(scale);
	}

	public void restoreScale() {
		super.getMovement().restoreScale();
		
	}

	public void resetGravity() {
		super.getMovement().resetGravity(Movement.Alignment.BOTH);
		
	}
}
