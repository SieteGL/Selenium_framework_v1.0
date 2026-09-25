package cl.consultor.qa.steps;

import cl.consultor.qa.components.NavigationMenu;
import cl.consultor.qa.config.Config;
import cl.consultor.qa.config.ConfigLoader;
import cl.consultor.qa.driver.DriverManager;
import cl.consultor.qa.pages.InventoryPage;
import cl.consultor.qa.pages.LoginPage;
import cl.consultor.qa.utils.TestData;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

public class LoginSteps {
    private final Config config = ConfigLoader.load();

    private LoginPage loginPage() {
        return new LoginPage(DriverManager.getDriver(), config.timeoutSeconds());
    }

    @Given("el usuario se encuentra en la página de inicio de sesión")
    public void userIsOnLoginPage() {
        loginPage().open(config.baseUrl());
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
        Assertions.assertEquals("Products", new InventoryPage(DriverManager.getDriver(), config.timeoutSeconds()).title(), "La página de inventario no fue mostrada.");
    }

    @When("abre el menú de navegación")
    public void openNavigationMenu() {
        new NavigationMenu(DriverManager.getDriver(), config.timeoutSeconds()).open();
    }

    @Then("debería visualizar la opción de cerrar sesión")
    public void shouldSeeLogout() {
        Assertions.assertTrue(new NavigationMenu(DriverManager.getDriver(), config.timeoutSeconds()).hasLogoutOption(), "La opción Logout debería estar visible.");
    }

    @Then("debería visualizar un mensaje de autenticación inválida")
    public void shouldSeeAuthenticationError() {
        Assertions.assertTrue(loginPage().errorMessage().contains("Username and password do not match"), "Se esperaba un mensaje de credenciales inválidas.");
    }
}
