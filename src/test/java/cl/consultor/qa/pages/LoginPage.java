package cl.consultor.qa.pages;

import cl.consultor.qa.utils.Waits;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

public class LoginPage {
    private static final By USERNAME = By.id("user-name");
    private static final By PASSWORD = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");
    private static final By ERROR = By.cssSelector("[data-test='error']");
    private final WebDriver driver;
    private final Waits waits;

    public LoginPage(WebDriver driver, Duration timeout) {
        this.driver = driver;
        this.waits = new Waits(driver, timeout);
    }

    public void open(String baseUrl) {
        driver.get(baseUrl);
    }

    public void loginAs(String username, String password) {
        waits.visible(USERNAME).sendKeys(username);
        waits.visible(PASSWORD).sendKeys(password);
        waits.clickable(LOGIN_BUTTON).click();
    }

    public String errorMessage() {
        return waits.visible(ERROR).getText();
    }
}
