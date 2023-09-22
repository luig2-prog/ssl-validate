package org.konecta.ssl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateException;
import java.util.Optional;

public class SSLValidate {

    public static void main(String[] args) {
        String urlStr = "https://www.ejemplo.com"; // Reemplaza esto con la URL del dominio que deseas verificar

        try {
            // Crea un TrustManager personalizado para aceptar todos los certificados SSL
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

            // Configura SSL para confiar en todos los certificados
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCertificates, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

            // Abre una conexión a la URL
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // Obtiene la sesión SSL de la conexión
            Optional<SSLSession> session = ((HttpsURLConnection) connection).getSSLSession();

            // Verifica si el certificado es válido
            if(session.isPresent()) {
                SSLSession sslSession = session.get();
                if (sslSession.isValid()) {
                    System.out.println("El certificado es válido.");
                } else {
                    System.out.println("El certificado no es válido.");
                }
            } else {
                System.out.println("No se pudo optener");
            }


            // Cierra la conexión
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}