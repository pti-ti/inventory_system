package com.pti_sa.inventory_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputFilter;
import java.net.URI;
import java.net.URISyntaxException;

@SpringBootApplication
public class InventorySystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventorySystemApplication.class, args);
	}

		/*// Solo abrir navegador si no estamos en Docker
		if (!isRunningInDocker()) {
			new Thread(() -> {
				try {
					String url = "http://localhost:8085";
					Thread.sleep(6500); // esperar para asegurar que el backend arrancó
					if (Desktop.isDesktopSupported()) {
						Desktop.getDesktop().browse(new URI(url));
						System.out.println("Navegador abierto.");
					} else {
						openBrowserWithCommand(url);
					}
				} catch (Exception e) {
					System.err.println("Error al abrir navegador: " + e.getMessage());
					openBrowserWithCommand("http://localhost:8085/auth/login");
				}
			}).start();
		}

		SpringApplication.run(InventorySystemApplication.class, args);
	}

	private static boolean isRunningInDocker() {
		return new File("/.dockerenv").exists();
	}

	private static void openBrowserWithCommand(String url) {
		String os = System.getProperty("os.name").toLowerCase();
		try {
			if (os.contains("win")) {
				Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
			} else if (os.contains("mac")) {
				Runtime.getRuntime().exec("open " + url);
			} else if (os.contains("nix") || os.contains("nux")) {
				Runtime.getRuntime().exec("xdg-open " + url);
			}
		} catch (Exception e) {
			System.err.println("Error al abrir navegador con comando: " + e.getMessage());
		}
	}*/
}
