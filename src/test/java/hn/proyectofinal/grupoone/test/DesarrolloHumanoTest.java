package hn.proyectofinal.grupoone.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static java.time.Duration.ofSeconds;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;


public class DesarrolloHumanoTest {
	@Test
	public void testGuardarCurso() {
		// Inicializa el WebDriver para Chrome
		WebDriver driver = new ChromeDriver();
		// Abre la página web de inicio de sesión
		driver.get("http://localhost:8080/master-detail");
	}
}
