@e2e
Feature: Autenticación y navegación de inventario
  Como usuario de la tienda de demostración
  quiero iniciar sesión de forma segura
  para acceder al catálogo de productos.

  @smoke @regression @critical
  Scenario: Inicio de sesión válido y acceso a navegación
    Given el usuario se encuentra en la página de inicio de sesión
    When inicia sesión con credenciales válidas
    Then debería visualizar la página principal de inventario
    When abre el menú de navegación
    Then debería visualizar la opción de cerrar sesión

  @sanity @regression
  Scenario Outline: Rechazo de credenciales inválidas
    Given el usuario se encuentra en la página de inicio de sesión
    When inicia sesión con username "<username>" y password "<password>"
    Then debería visualizar un mensaje de autenticación inválida

    Examples:
      | username     | password         |
      | invalid_user | invalid_password |
