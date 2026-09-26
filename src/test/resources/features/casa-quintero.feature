@casa-quintero @smoke
Feature: Instalación inicial de Casa Quintero
  Como propietario de una instancia nueva
  quiero encontrar la pantalla de preparación privada
  para poder crear la primera cuenta autorizada.

  Scenario: La pantalla de preparación está disponible
    Given la aplicación Casa Quintero está disponible
    Then se muestra el formulario de acceso privado de Casa Quintero
