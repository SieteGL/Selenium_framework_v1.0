package cl.consultor.qa.pages;

import cl.consultor.qa.utils.Waits;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

public class CasaQuinteroLoginPage {
    private static final By TITLE = By.xpath("//h1[normalize-space()='Casa Quintero']");
    private static final By EMAIL = By.cssSelector("input[name='email'][type='email']");
    private static final By PASSWORD = By.cssSelector("input[name='password'][type='password']");
    private static final By SUBMIT = By.xpath("//button[normalize-space()='Entrar a la casa']");
    private final WebDriver driver;
    private final Waits waits;

    public CasaQuinteroLoginPage(WebDriver driver, Duration timeout) {
        this.driver = driver;
        this.waits = new Waits(driver, timeout);
    }

    public void open(String baseUrl) {
        driver.get(baseUrl);
    }

    public boolean loginFormIsVisible() {
        return waits.visible(TITLE).isDisplayed()
                && waits.visible(EMAIL).isDisplayed()
                && waits.visible(PASSWORD).isDisplayed()
                && waits.clickable(SUBMIT).isDisplayed();
    }
}
