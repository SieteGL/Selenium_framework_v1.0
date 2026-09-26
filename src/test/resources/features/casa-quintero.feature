@casa-quintero @smoke
Feature: Acceso privado a Casa Quintero
  Como propietario autorizado
  quiero iniciar sesión en la casa
  para consultar el calendario privado.

  Scenario: Marco inicia sesión y llega al calendario
    Given la aplicación Casa Quintero está disponible
    Then se muestra el formulario de acceso privado de Casa Quintero
    When Marco inicia sesión en Casa Quintero
    Then se muestra el calendario privado de Casa Quintero
