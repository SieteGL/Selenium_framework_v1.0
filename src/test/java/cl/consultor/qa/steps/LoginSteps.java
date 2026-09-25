package cl.consultor.qa.steps;

import cl.consultor.qa.components.NavigationMenu;
import cl.consultor.qa.config.FrameworkConfig;
import cl.consultor.qa.driver.DriverManager;
import cl.consultor.qa.pages.InventoryPage;
import cl.consultor.qa.pages.LoginPage;
import cl.consultor.qa.utils.TestData;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

public class LoginSteps {
    private FrameworkConfig config() {
        return DriverManager.getConfig();
    }

    private LoginPage loginPage() {
        return new LoginPage(DriverManager.getDriver(), config().timeout());
    }

    @Given("el usuario se encuentra en la página de inicio de sesión")
    public void userIsOnLoginPage() {
        loginPage().open(config().baseUrl().toString());
    }

    @When("inicia sesión con credenciales válidas")
    public void loginWithValidCredentials() {
        loginPage().loginAs(TestData.value("valid.username"), TestData.value("valid.password"));
    }

    @When("inicia sesión con username {string} y password {string}")
    public void loginWithCredentials(String username, String password) {
        loginPage().loginAs(username, password);
    }

    @Then("debería visualizar la página principal de inventario")
    public void shouldSeeInventory() {
        Assertions.assertEquals("Products", new InventoryPage(DriverManager.getDriver(), config().timeout()).title(), "La página de inventario no fue mostrada.");
    }

    @When("abre el menú de navegación")
    public void openNavigationMenu() {
        new NavigationMenu(DriverManager.getDriver(), config().timeout()).open();
    }

    @Then("debería visualizar la opción de cerrar sesión")
    public void shouldSeeLogout() {
        Assertions.assertTrue(new NavigationMenu(DriverManager.getDriver(), config().timeout()).hasLogoutOption(), "La opción Logout debería estar visible.");
    }

    @Then("debería visualizar el mensaje de autenticación {string}")
    public void shouldSeeAuthenticationError(String expectedMessage) {
        String actualMessage = loginPage().errorMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage),
                () -> "Expected authentication error containing: '" + expectedMessage + "'. Actual: '" + actualMessage + "'.");
    }
}
