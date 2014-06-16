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
import java.util.HashMap;
import java.util.List;
import javax.xml.bind.JAXBException;
import mx.edu.um.mateo.contabilidad.facturas.dao.ProveedorSatDao;
import mx.edu.um.mateo.contabilidad.facturas.dao.impl.ProveedorSatDaoHibernate;

/**
 *
 * @author develop
 */
public class LectorFacturas {

    
    private static final String COMPROBANTE_XML = "/home/develop/LCO_2014-03-28_4.XML";
    private static List<ProveedorSat> proveedoresSat = new ArrayList<>();

    public static void main(String[] args) throws JAXBException, IOException {
//        ProveedorSatDao dao=new ProveedorSatDaoHibernate();
        List<HashMap> li = null;  
        ProveedorSat proveedorSat = null;
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
                if (n == 20) {
                    li = cortes(lineas);
//                    n = 0;
                    break;
                }

            }

            entrada.close();
        } catch (Exception e) {
            System.out.println("stacktrace");
            e.printStackTrace();
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
//        for (HashMap<String, String> x : li) {
//            proveedorSat = new ProveedorSat();
//            proveedorSat.setRfc(x.get("RFC"));
//            proveedorSat.setEstatusCerfiticado(x.get("EstatusCertificado"));
//            proveedorSat.setEstatusCerfiticado(x.get("noCertificado"));
//            proveedorSat.setEstatusCerfiticado(x.get("FechaFinal"));
//            proveedorSat.setEstatusCerfiticado(x.get("FechaInicio"));
//            proveedorSat.setEstatusCerfiticado(x.get("ValidezObligaciones"));
//            proveedoresSat.add(proveedorSat);
//            System.out.println("hash" + x.toString());
//        }
//        for (ProveedorSat pSat : proveedoresSat) {
//            dao.crea(proveedorSat);
//        }
    }

    public static List<HashMap> cortes(List<String> lista) {
        StringBuilder proveedorSat = new StringBuilder();
        Boolean validez = false, status = false, inicio = false, fechafinal = false, noCertificado = false;
        String validezs = null, statuss = null, inicios = null, finals = null, noCertificados = null;
        HashMap<String, String> map = new HashMap<>();
        List<HashMap> ret = new ArrayList<>();

        for (String x : lista) {
            if (x.contains("RFC")) {
                map = new HashMap<>();
                proveedorSat = new StringBuilder();
                String[] ar = x.split("\"");
                String rfc = ar[1];
                proveedorSat.append(ar[1]);
                map.put("RFC", rfc);
                System.out.println("rfc" + rfc);
            }
            if (x.contains("ValidezObligaciones")) {
                String[] ar = x.split("=");
                for (String c : ar) {
                    String[] a = c.split(" ");
                    for (String b : a) {
                        System.out.println("b" + b);
                        if (b.contains("Obligaciones")) {
                            validez = true;
                            validezs = b;
                            map.put(b, null);
                        }
                        if (validez && !b.contains("Obligaciones")) {
                            String v = clean(b);
                            map.put(validezs, v);
                            validez = false;
                            validezs = null;
                        }
                        if (b.contains("Estatus")) {
                            status = true;
                            statuss = b;
                            map.put(b, null);
                        }
                        if (status && !b.contains("Estatus")) {
                            String e = clean(b);
                            map.put(statuss, e);
                            status = false;
                            statuss = null;
                        }
                        if (b.contains("noCertificado")) {
                            noCertificado = true;
                            noCertificados = b;
                            map.put(b, null);
                        }
                        if (noCertificado && !b.contains("noCertificado")) {
                            String no = clean(b);
                            map.put(noCertificados, no);
                            noCertificado = false;
                            noCertificados = null;
                        }
                        if (b.contains("Final")) {
                            fechafinal = true;
                            finals = b;
                            map.put(b, null);
                        }
                        if (fechafinal && !b.contains("Final")) {
                            String f = clean(b);
                            map.put(finals, f);
                            fechafinal = false;
                            finals = null;
                        }
                        if (b.contains("Inicio")) {
                            inicio = true;
                            inicios = b;
                            map.put(b, null);
                        }
                        if (inicio && !b.contains("Inicio")) {
                            String i = clean(b);
                            map.put(inicios, i);
                            inicio = false;
                            inicios = null;
                        }
                    }

                }
                ret.add(map);
            }
        }
//        for (String x : proveedoresSat) {
//            System.out.println(x);
//        }
//<lco:Certificado ValidezObligaciones="1" EstatusCertificado="A" noCertificado="00001000000104028156" FechaFinal="2015-07-19T18:35:37" FechaInicio="2011-07-19T18:34:57">
        return ret;
    }

    public static String clean(String param) {
        String limpio = null;
        String[] ar = param.split("\"");
        limpio = ar[1];
        return limpio;
    }
}
