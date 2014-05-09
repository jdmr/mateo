/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.model;

import java.io.File;
import java.io.IOException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 *
 * @author develop
 */
public class LCOMain {

    private static final String COMPROBANTE_XML = "./sapNomina.xml";

    public static void main(String[] args) throws JAXBException, IOException {

        Contribuyente c = new Contribuyente();
        c.setRFC("TOGO820901JP4");

        Certificado certificado = new Certificado();
        certificado.setEstatusCertificado("A");
        certificado.setFechaFinal("2017-05-03T15:34:21");
        certificado.setFechaInicio("2013-05-03T15:33:41");
        certificado.setNoCertificado("2013-05-03T15:33:41");
        certificado.setValidezObligaciones("1");
        c.setCertificado(certificado);
        // create JAXB context and instantiate marshaller
        JAXBContext context = JAXBContext.newInstance(Contribuyente.class);
        Marshaller m = context.createMarshaller();

        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http:/www.sat.gob.mx/cfd/LCO http:/www.sat.gob.mx/cfd/LCO LCO.xsd");

        // Write to System.out
        m.marshal(c, System.out);

        // Write to File
        m.marshal(c, new File(COMPROBANTE_XML));
    }

}
