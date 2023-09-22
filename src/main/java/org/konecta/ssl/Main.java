package org.konecta.ssl;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;

public class Main {
    public static void main(String[] args) {

//        String url = "https://www.example.com";
//        String url = "https://ejemplo.com";
        String url = "https://saralabs.grupokonecta.co:8591/externa_frontend/login";

        try {
            // Configura un TrustManager que no realiza ninguna validación
            TrustManager[] trustAllCertificates = new TrustManager[]{
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

            // Configura la conexión SSL para confiar en todos los certificados
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCertificates, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

            // Crea una conexión HTTPS
            URL urlObj = new URL(url);
            HttpsURLConnection connection = (HttpsURLConnection) urlObj.openConnection();

            // Realiza la conexión y obtiene el certificado
            connection.connect();
            X509Certificate[] certs = (X509Certificate[]) connection.getServerCertificates();

            // Validación adicional del certificado
            for (X509Certificate cert : certs) {
                try {
                    cert.checkValidity(); // Verificar validez
                    // Aquí puedes agregar comprobaciones adicionales, como verificar el dominio del certificado.
                    System.out.println("Sujeto: " + cert.getSubjectDN());
                    System.out.println("Emisor: " + cert.getIssuerDN());
                    System.out.println("Número de serie: " + cert.getSerialNumber());
                    System.out.println("El certificado es válido.");
                } catch (CertificateExpiredException | CertificateNotYetValidException e) {
                    System.err.println("El certificado no es válido.");
                    e.printStackTrace();
                }
            }

            // Cierra la conexión
            connection.disconnect();
        } catch (IOException | java.security.NoSuchAlgorithmException
                 | java.security.KeyManagementException e) {
            e.printStackTrace();
        }
    }
}
