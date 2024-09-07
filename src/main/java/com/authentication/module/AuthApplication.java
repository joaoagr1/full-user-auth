package com.authentication.module;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.net.ssl.*;
import java.security.cert.X509Certificate;

@SpringBootApplication
public class AuthApplication {

	public static void main(String[] args) {
		// Configuração para desabilitar a verificação de certificados SSL
		disableSslVerification();

		SpringApplication.run(AuthApplication.class, args);
	}

	private static void disableSslVerification() {
		try {
			System.out.println("Desabilitando verificação SSL...");

			// Criar um TrustManager que aceita todos os certificados
			TrustManager[] trustAll = new TrustManager[]{
					new X509TrustManager() {
						public X509Certificate[] getAcceptedIssuers() {
							return null;
						}

						public void checkClientTrusted(X509Certificate[] certs, String authType) {
						}

						public void checkServerTrusted(X509Certificate[] certs, String authType) {
						}
					}
			};

			// Inicializar o SSLContext com o TrustManager que aceita todos os certificados
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAll, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Desabilitar a verificação do hostname
			HostnameVerifier allHostsValid = (hostname, session) -> true;
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

			System.out.println("Verificação SSL desabilitada.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erro ao desabilitar verificação SSL: " + e.getMessage());
		}
	}
}
