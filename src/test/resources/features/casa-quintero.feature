@casa-quintero @smoke
Feature: Acceso inicial a Casa Quintero
  Como integrante de la familia
  quiero encontrar la pantalla de acceso privada
  para poder ingresar a la aplicación autorizada.

  Scenario: La pantalla de acceso está disponible
    Given la aplicación Casa Quintero está disponible
    Then se muestra el formulario de acceso privado de Casa Quintero
