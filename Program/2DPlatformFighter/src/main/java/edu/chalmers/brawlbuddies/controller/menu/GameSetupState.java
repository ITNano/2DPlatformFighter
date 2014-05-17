package edu.chalmers.brawlbuddies.controller.menu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import edu.chalmers.brawlbuddies.controller.Controller;
import edu.chalmers.brawlbuddies.controller.Constants;
import edu.chalmers.brawlbuddies.controller.Player;
import edu.chalmers.brawlbuddies.controller.input.InputHandlerChooser;
import edu.chalmers.brawlbuddies.view.menu.GameSetupView;
import edu.chalmers.brawlbuddies.view.menu.MenuView;
import edu.chalmers.brawlbuddies.view.menu.MultiChoiceMenuItem;
import edu.chalmers.brawlbuddies.view.menu.MultiChoiceOption;

public class GameSetupState extends BasicGameState implements MenuListener{

	private MenuView view;
	private MenuHandler handler;
	private StateBasedGame game;

	/**
	 * Creates a new Menu state
	 */
	public GameSetupState() {
		view = new GameSetupView();
		handler = new MenuHandler(view);
		handler.addMenuListener(this);
	}

	/**
	 * Inits all variables that are needed etc.
	 * @param container The slick game container
	 * @param game The parent controller
	 */
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
	}

	/**
	 * Renders the menu on the screen
	 * @param container The slick game container
	 * @param game The parent controller
	 * @param g The graphics object to draw with
	 * @throws SlickException If the slick engine finds invalid data
	 */
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		view.render(container, g);
	}

	/**
	 * Updates the data of the menu
	 * @param container The slick game container
	 * @param game The parent controller
	 * @param delta The time in ms since the last update
	 * @throws SlickException If the slick engine finds invalid data
	 */
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		this.game = game;
		handler.update(container, delta);
	}

	/**
	 * Determines the ID of this game state as declared in the
	 * edu.chalmers.brawlbuddies.controller.Constants class
	 * @return The ID of this game state
	 */
	@Override
	public int getID() {
		return Constants.GAMESTATE_GAME_SETUP;
	}
	
	public void startGame(){
		if(canStart()){
			String p1_character = view.get("p1_character").getValue();
			int p1_control = Integer.parseInt(view.get("p1_control").getValue());
			String p2_character = view.get("p2_character").getValue();
			int p2_control = Integer.parseInt(view.get("p2_control").getValue());
			String map = view.get("map").getValue();
			String mode = view.get("mode").getValue();
			
			Player[] players = { new Player("Player1", InputHandlerChooser.getInstance().getInputHandler(p1_control, false)),
								 new Player("Player2", InputHandlerChooser.getInstance().getInputHandler(p2_control, isSecondControl(p1_control, p2_control)))};
			String[] charNames = {p1_character, p2_character};
			((Controller)game).startGame(players, charNames, map, mode);
		}
	}
	
	private boolean isSecondControl(int p1, int p2){
		InputHandlerChooser handler = InputHandlerChooser.getInstance();
		return handler.usesKeyboard(p1) && handler.usesKeyboard(p2);
	}
	
	private boolean canStart(){
		return view.get("p2_control").getValue() != null;
	}
	
	public void gotoMainMenu(){
		game.enterState(Constants.GAMESTATE_MAIN_MENU);
	}

	public void menuActivated(MenuEvent event){
		if(event.getName().equals("startGame")){
			if(canStart()){
				startGame();
			}
		}else if(event.getName().equals("gotoMain")){
			gotoMainMenu();
		}else if(event.getName().equals("p1_control")){
			MultiChoiceMenuItem p2_control = (MultiChoiceMenuItem)(view.getMenuItems().get(3));

			for(MultiChoiceOption option : p2_control.getOptions()){
				p2_control.enable(option.getCodeValue(), !option.getCodeValue().equals(event.getValue()));
			}
		}

		view.get("startGame").setError(!canStart());
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException{
		view.update();
	}
	
}