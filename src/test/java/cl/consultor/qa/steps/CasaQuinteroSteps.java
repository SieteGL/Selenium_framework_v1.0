package cl.consultor.qa.steps;

import cl.consultor.qa.config.FrameworkConfig;
import cl.consultor.qa.driver.DriverManager;
import cl.consultor.qa.pages.CasaQuinteroLoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

public class CasaQuinteroSteps {
    private FrameworkConfig config() {
        return DriverManager.getConfig();
    }

    private CasaQuinteroLoginPage page() {
        return new CasaQuinteroLoginPage(DriverManager.getDriver(), config().timeout());
    }

    @Given("la aplicación Casa Quintero está disponible")
    public void casaQuinteroIsAvailable() {
        page().open(config().baseUrl().toString());
    }

    @Then("se muestra el formulario de acceso privado de Casa Quintero")
    public void privateLoginFormIsDisplayed() {
        Assertions.assertTrue(page().loginFormIsVisible(), "El formulario de acceso de Casa Quintero debe estar visible.");
    }
}
