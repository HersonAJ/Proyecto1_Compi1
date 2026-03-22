import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Servidor local para formularios .pkm
 *
 * Compilar:  javac ServidorPKM.java
 * Ejecutar:  java ServidorPKM
 */
public class ServidorPKM {

    private static final int PUERTO = 5000;
    private static final String CARPETA = "formularios_pkm";

    public static void main(String[] args) throws Exception {
        // Crear carpeta si no existe
        new File(CARPETA).mkdirs();

        // Crear e iniciar servidor
        HttpServer servidor = HttpServer.create(new InetSocketAddress(PUERTO), 0);
        servidor.createContext("/formularios", new ManejadorFormularios());
        servidor.start();

        // Mostrar información de conexión
        String ipLocal = InetAddress.getLocalHost().getHostAddress();
        System.out.println("==================================================");
        System.out.println("  SERVIDOR PKM");
        System.out.println("  URL: http://" + ipLocal + ":" + PUERTO);
        System.out.println("  Carpeta: " + new File(CARPETA).getAbsolutePath());
        System.out.println("==================================================");
    }

    /**
     * Maneja todas las peticiones HTTP a /formularios
     */
    static class ManejadorFormularios implements HttpHandler {

        @Override
        public void handle(HttpExchange peticion) throws IOException {
            // Permitir conexiones desde cualquier origen
            peticion.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            peticion.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            peticion.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

            String metodo = peticion.getRequestMethod();
            String ruta = peticion.getRequestURI().getPath();

            // Preflight de CORS
            if (metodo.equals("OPTIONS")) {
                peticion.sendResponseHeaders(200, -1);
                return;
            }

            // Dirigir a la acción correspondiente
            if (metodo.equals("GET") && ruta.equals("/formularios")) {
                listar(peticion);
            } else if (metodo.equals("POST") && ruta.equals("/formularios")) {
                subir(peticion);
            } else if (metodo.equals("GET") && ruta.startsWith("/formularios/")) {
                String nombreArchivo = ruta.substring("/formularios/".length());
                descargar(peticion, nombreArchivo);
            } else {
                enviarRespuesta(peticion, 404, "{\"error\": \"Ruta no encontrada\"}");
            }
        }

        /**
         * GET /formularios
         * Retorna la lista de archivos .pkm con sus metadatos
         */
        private void listar(HttpExchange peticion) throws IOException {
            File carpeta = new File(CARPETA);
            File[] archivosPkm = carpeta.listFiles((dir, nombre) -> nombre.endsWith(".pkm"));

            StringBuilder listaJson = new StringBuilder("[");
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yy");
            SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");

            if (archivosPkm != null) {
                for (int i = 0; i < archivosPkm.length; i++) {
                    File archivo = archivosPkm[i];
                    Date fechaModificacion = new Date(archivo.lastModified());
                    String autorEncontrado = buscarAutorEnArchivo(archivo);

                    listaJson.append("{");
                    listaJson.append("\"nombre\": \"").append(archivo.getName()).append("\",");
                    listaJson.append("\"autor\": \"").append(autorEncontrado).append("\",");
                    listaJson.append("\"fecha\": \"").append(formatoFecha.format(fechaModificacion)).append("\",");
                    listaJson.append("\"hora\": \"").append(formatoHora.format(fechaModificacion)).append("\"");
                    listaJson.append("}");

                    if (i < archivosPkm.length - 1) listaJson.append(",");
                }
            }

            listaJson.append("]");
            int totalArchivos = archivosPkm != null ? archivosPkm.length : 0;
            System.out.println("[LISTAR] " + totalArchivos + " formularios encontrados");
            enviarRespuesta(peticion, 200, listaJson.toString());
        }

        /**
         * POST /formularios
         * Recibe: {"nombre": "...", "contenido": "...", "autor": "..."}
         * Guarda el contenido como archivo .pkm
         */
        private void subir(HttpExchange peticion) throws IOException {
            String cuerpoRecibido = new String(peticion.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

            String nombreFormulario = obtenerValorDelJson(cuerpoRecibido, "nombre");
            String contenidoPkm = obtenerValorDelJson(cuerpoRecibido, "contenido");
            String autorFormulario = obtenerValorDelJson(cuerpoRecibido, "autor");

            if (nombreFormulario == null || contenidoPkm == null) {
                enviarRespuesta(peticion, 400, "{\"error\": \"Se requiere 'nombre' y 'contenido'\"}");
                return;
            }

            // Asegurar extensión .pkm
            if (!nombreFormulario.endsWith(".pkm")) {
                nombreFormulario += ".pkm";
            }

            // Guardar archivo
            File archivoDestino = new File(CARPETA, nombreFormulario);
            Files.writeString(archivoDestino.toPath(), contenidoPkm, StandardCharsets.UTF_8);

            System.out.println("[SUBIR] Guardado: " + nombreFormulario + " (autor: " + autorFormulario + ")");
            enviarRespuesta(peticion, 200, "{\"mensaje\": \"Formulario guardado correctamente\"}");
        }

        /**
         * GET /formularios/{nombre}
         * Retorna el contenido del archivo .pkm solicitado
         */
        private void descargar(HttpExchange peticion, String nombreArchivo) throws IOException {
            File archivoPkm = new File(CARPETA, nombreArchivo);

            if (!archivoPkm.exists()) {
                System.out.println("[DESCARGAR] No encontrado: " + nombreArchivo);
                enviarRespuesta(peticion, 404, "{\"error\": \"Formulario no encontrado\"}");
                return;
            }

            String contenidoPkm = Files.readString(archivoPkm.toPath(), StandardCharsets.UTF_8);

            // Escapar caracteres especiales para que el JSON sea válido
            String contenidoEscapado = contenidoPkm
                    .replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");

            String respuestaJson = "{\"nombre\": \"" + nombreArchivo + "\", \"contenido\": \"" + contenidoEscapado + "\"}";

            System.out.println("[DESCARGAR] Enviando: " + nombreArchivo);
            enviarRespuesta(peticion, 200, respuestaJson);
        }

        // ==================== UTILIDADES ====================

        /**
         * Envía una respuesta HTTP con el código y cuerpo indicados
         */
        private void enviarRespuesta(HttpExchange peticion, int codigoHttp, String cuerpoJson) throws IOException {
            byte[] bytesRespuesta = cuerpoJson.getBytes(StandardCharsets.UTF_8);
            peticion.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
            peticion.sendResponseHeaders(codigoHttp, bytesRespuesta.length);
            peticion.getResponseBody().write(bytesRespuesta);
            peticion.getResponseBody().close();
        }

        /**
         * Extrae el valor de un campo dentro de un JSON .
         */
        private String obtenerValorDelJson(String textoJson, String nombreCampo) {
            String marcador = "\"" + nombreCampo + "\"";
            int posicionCampo = textoJson.indexOf(marcador);
            if (posicionCampo < 0) return null;

            int posicionDosPuntos = textoJson.indexOf(":", posicionCampo + marcador.length());
            if (posicionDosPuntos < 0) return null;

            int inicioValor = textoJson.indexOf("\"", posicionDosPuntos + 1);
            if (inicioValor < 0) return null;

            // Buscar el cierre de la comilla ignorando escapes
            int finValor = inicioValor + 1;
            while (finValor < textoJson.length()) {
                char caracter = textoJson.charAt(finValor);
                if (caracter == '\\') {
                    finValor += 2; // Saltar caracter escapado
                } else if (caracter == '"') {
                    break;
                } else {
                    finValor++;
                }
            }

            if (finValor >= textoJson.length()) return null;

            // Extraer y desescapar el valor
            String valorExtraido = textoJson.substring(inicioValor + 1, finValor);
            valorExtraido = valorExtraido
                    .replace("\\\"", "\"")
                    .replace("\\n", "\n")
                    .replace("\\r", "\r")
                    .replace("\\t", "\t")
                    .replace("\\\\", "\\");

            return valorExtraido;
        }

        /**
         * Lee un archivo .pkm y busca la línea "Author:" en los metadatos
         * para extraer el nombre del autor.
         */
        private String buscarAutorEnArchivo(File archivoPkm) {
            try {
                String contenido = Files.readString(archivoPkm.toPath(), StandardCharsets.UTF_8);
                for (String linea : contenido.split("\n")) {
                    String lineaLimpia = linea.trim().toLowerCase();
                    if (lineaLimpia.startsWith("author:")) {
                        return linea.trim().substring("author:".length()).trim();
                    }
                }
            } catch (Exception e) {
                // Si no se puede leer, retornar anonimo
            }
            return "Anónimo";
        }
    }
}