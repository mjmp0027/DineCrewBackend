package com.dinecrew.dinecrewbackend;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;

@SpringBootApplication
@EnableMongoRepositories(basePackages = {"com.dinecrew.dinecrewbackend.pedidos", "com.dinecrew.dinecrewbackend.usuarios", "com.dinecrew.dinecrewbackend.mesas", "com.dinecrew.dinecrewbackend.cuentas", "com.dinecrew.dinecrewbackend.notificaciones"})
public class DineCrewBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(DineCrewBackendApplication.class, args);
	}

	@PostConstruct
	public void initFirebase() throws IOException {
		FileInputStream serviceAccount = new FileInputStream("src/main/resources/dinecrew-c0451-firebase-adminsdk-6avfn-df22dac655.json");
		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.build();

		FirebaseApp.initializeApp(options);

	}

}
