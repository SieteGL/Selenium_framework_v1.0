package cl.consultor.qa.components;

import cl.consultor.qa.utils.Waits;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

/**
 * Component because this menu is navigation UI reusable beyond one page.
 */
public class NavigationMenu {
    private static final By MENU_BUTTON = By.id("react-burger-menu-btn");
    private static final By LOGOUT_LINK = By.id("logout_sidebar_link");
    private final Waits waits;

    public NavigationMenu(WebDriver driver, Duration timeout) {
        this.waits = new Waits(driver, timeout);
    }

    public void open() {
        waits.clickable(MENU_BUTTON).click();
    }

    public boolean hasLogoutOption() {
        return waits.visible(LOGOUT_LINK).isDisplayed();
    }
}
