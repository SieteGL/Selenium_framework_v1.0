package cl.consultor.qa.pages;

import cl.consultor.qa.utils.Waits;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    private static final By USERNAME = By.id("user-name");
    private static final By PASSWORD = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");
    private static final By ERROR = By.cssSelector("[data-test='error']");
    private final WebDriver driver;
    private final Waits waits;

    public LoginPage(WebDriver driver, long timeoutSeconds) {
        this.driver = driver;
        this.waits = new Waits(driver, timeoutSeconds);
    }

    public void open(String baseUrl) {
        driver.get(baseUrl);
    }

    public void loginAs(String username, String password) {
        waits.visible(USERNAME).sendKeys(username);
        driver.findElement(PASSWORD).sendKeys(password);
        waits.clickable(LOGIN_BUTTON).click();
    }

    public String errorMessage() {
        return waits.visible(ERROR).getText();
    }
}
