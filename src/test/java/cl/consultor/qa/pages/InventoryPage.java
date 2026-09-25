package cl.consultor.qa.pages;

import cl.consultor.qa.utils.Waits;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class InventoryPage {
    private static final By TITLE = By.cssSelector("[data-test='title']");
    private final Waits waits;

    public InventoryPage(WebDriver driver, long timeoutSeconds) {
        this.waits = new Waits(driver, timeoutSeconds);
    }

    public String title() {
        return waits.visible(TITLE).getText();
    }
}
