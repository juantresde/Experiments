package engine.util.menu;

import java.util.Stack;

import engine.input.Control;
import engine.rendering.Color;
import engine.rendering.IRenderContext;
import engine.rendering.SpriteSheet;
import engine.util.Delay;

public class MenuStack {
	private Stack<Menu> menuStack;
	private Menu defaultMenu;
	private SpriteSheet font;
	private Control downKey;
	private Control upKey;
	private Control activateKey;
	private Control toggleKey;
	private Delay moveDelay;
	private Delay toggleDelay;
	private Delay activateDelay;
	private Color fontColor;
	private Color selectionColor;
	private double offsetX;
	private double offsetY;

	public MenuStack(SpriteSheet font, Color fontColor, Color selectionColor,
			double offsetX, double offsetY, Control upKey,
			Control downKey, Control activateKey,
			Control toggleKey, double usageDelayLength, Menu defaultMenu) {
		this.font = font;
		this.menuStack = new Stack<Menu>();
		this.toggleKey = toggleKey;
		this.toggleDelay = new Delay(usageDelayLength);
		this.defaultMenu = defaultMenu;
		this.fontColor = fontColor;
		this.selectionColor = selectionColor;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.upKey = upKey;
		this.downKey = downKey;
		this.activateKey = activateKey;
		this.moveDelay = new Delay(usageDelayLength);
		this.activateDelay = new Delay(usageDelayLength);
	}

	public void close() {
		while(isShowing()) {
			pop();
		}
	}
	
	public void toggleVisibility() {
		if (isShowing()) {
			pop();
		} else {
			push(defaultMenu);
		}
	}
	public void update(double delta) {
		if (toggleDelay.over(delta) && toggleKey.isDown()) {
			toggleDelay.reset();
			toggleVisibility();
		}

		if (!isShowing()) {
			return;
		}

		boolean canMove = moveDelay.over(delta);
		if (canMove && upKey.isDown()) {
			getCurrentMenu().move(-1);
			moveDelay.reset();
		}
		if (canMove && downKey.isDown()) {
			getCurrentMenu().move(1);
			moveDelay.reset();
		}

		if (activateDelay.over(delta) && activateKey.isDown()) {
			getCurrentMenu().activate();
			activateDelay.reset();
		}
	}

	public void pop() {
		Menu menu = getCurrentMenu();
		if (menu != null) {
			menu.setMenuStack(null);
			menuStack.pop();
		}
	}

	public void push(Menu menu) {
		menu.setMenuStack(this);
		menuStack.push(menu);
	}

	public void render(IRenderContext target) {
		if (isShowing()) {
			getCurrentMenu().render(target, font, offsetX, offsetY,
					selectionColor, fontColor);
		}
	}

	private Menu getCurrentMenu() {
		if (menuStack.empty()) {
			return null;
		}
		return menuStack.peek();
	}

	public boolean isShowing() {
		return !menuStack.empty();
	}
}
