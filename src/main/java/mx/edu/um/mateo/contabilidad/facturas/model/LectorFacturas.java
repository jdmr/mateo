/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.model;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBException;
import mx.edu.um.mateo.inventario.model.Salida;

/**
 *
 * @author develop
 */
public class LectorFacturas {

    private static final String COMPROBANTE_XML = "/home/develop/LCO_2014-03-28_4.XML";
    private static List<String> proveedoresSat = new ArrayList<>();

    public static void main(String[] args) throws JAXBException, IOException {
        try {
            FileInputStream fstream = new FileInputStream(COMPROBANTE_XML);
            DataInputStream entrada = new DataInputStream(fstream);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(entrada));
            int caracter;
            boolean menosque = false;
            List<String> lineas = new ArrayList<String>();
            StringBuilder frase = new StringBuilder();
            int n = 0;
            while ((caracter = buffer.read()) != -1) {
                if ((char) caracter == '<') {
                    frase = new StringBuilder();
                    menosque = true;
                }
                if (menosque) {
                    frase.append((char) caracter);
                }
                if ((char) caracter == '>') {
                    lineas.add(frase.toString());
                    frase = null;
                    n++;
                }
                if (n == 10) {
                    cortes(lineas);
                    n = 0;
                }

            }

            entrada.close();
        } catch (Exception e) {
            System.out.println("stacktrace");
            e.printStackTrace();
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
        System.out.println("salita" + proveedoresSat.size());
//        for (String x : proveedoresSat) {
//            System.out.println("proveedor" + x);
//        }
    }

    public static void cortes(List<String> lista) {
        StringBuilder proveedorSat = new StringBuilder();

        for (String x : lista) {
            if (x.contains("RFC")) {
                proveedorSat = new StringBuilder();
                String[] ar = x.split("\"");
                proveedorSat.append(ar[1]);
            }
            if (x.contains("ValidezObligaciones")) {
                String[] ar = x.split("\"");
                try {

                    proveedorSat.append(",").append(ar[1]);
                } catch (Exception e) {
                    System.out.println("error al intentar escribir validez obligaciones");
                }
                try {

                    proveedorSat.append(",").append(ar[3]);
                } catch (Exception e) {
                    System.out.println("error al intentar escribir status certificado");
                }
                try {
                    proveedorSat.append(",").append(ar[5]);

                } catch (Exception e) {
                    System.out.println("error al intentar escribir noCertificado");
                }
                try {

                    proveedorSat.append(",").append(ar[7]);
                } catch (Exception e) {
                    System.out.println("error al intentar escribir fecha final");
                }
                try {
                    proveedorSat.append(",").append(ar[9]);

                } catch (Exception e) {
                    System.out.println("error al intentar escribir fecha inicio");
                }
                proveedoresSat.add(proveedorSat.toString());
                proveedorSat = null;
            }
        }
//        for (String x : proveedoresSat) {
//            System.out.println(x);
//        }
//<lco:Certificado ValidezObligaciones="1" EstatusCertificado="A" noCertificado="00001000000104028156" FechaFinal="2015-07-19T18:35:37" FechaInicio="2011-07-19T18:34:57">
    }
}
