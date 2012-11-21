/*
 * The MIT License
 *
 * Copyright 2012 Universidad de Montemorelos A. C.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package mx.edu.um.mateo.rh.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.rh.model.SolicitudVacaciones;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class SolicitudVacacionesDaoTest {
    private static final Logger log = LoggerFactory.getLogger(SolicitudVacacionesDaoTest.class);
    
    @Autowired
    private SolicitudVacacionesDao dao;
    
    @Test
    @Ignore
    public void testGetSolicitudVacacionesByFechaRecepcion() throws Exception{
        
    	SolicitudVacaciones solicitudVacaciones = new SolicitudVacaciones();
    	List<SolicitudVacaciones> list;
    	solicitudVacaciones.setStatus(Constantes.SOLICITUDSALIDA_STATUS_RECIBIDO);
    	list = dao.getSolicitudVacacionesByFechaInicial(solicitudVacaciones);
    	assertEquals(2, list.size());
    	
    	solicitudVacaciones.setStatus(Constantes.SOLICITUDSALIDA_STATUS_RECHAZADO);
    	list =  dao.getSolicitudVacacionesByFechaInicial(solicitudVacaciones);
    	assertEquals(14, list.size());
    	
    	solicitudVacaciones.setStatus(Constantes.SOLICITUDSALIDA_STATUS_AUTORIZADO);
    	list =  dao.getSolicitudVacacionesByFechaInicial(solicitudVacaciones);
    	assertEquals(6, list.size());
    	
    	solicitudVacaciones.setStatus(Constantes.SOLICITUDSALIDA_STATUS_TODOS);
    	list =  dao.getSolicitudVacacionesByFechaInicial(solicitudVacaciones);
    	assertEquals(22, list.size());
    }

    @Test
    @Ignore
    public void testGetSolicitudVacacionesByFechas() throws Exception {
        SolicitudVacaciones solicitudVacaciones = new SolicitudVacaciones();        
        solicitudVacaciones.setStatus("P");

        List<SolicitudVacaciones> results = dao.getSolicitudVacaciones(solicitudVacaciones);
        log.debug("{}",results);
        log.debug("{}",results.size());
        assertTrue(results.size() > 0);
    }
}
