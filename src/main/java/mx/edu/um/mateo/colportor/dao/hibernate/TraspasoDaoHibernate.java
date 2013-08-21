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
package mx.edu.um.mateo.colportor.dao.hibernate;

import java.lang.String;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import mx.edu.um.mateo.colportor.dao.AsociadoDao;
import mx.edu.um.mateo.colportor.dao.ColportorDao;
import mx.edu.um.mateo.colportor.dao.DocumentoDao;
import mx.edu.um.mateo.colportor.dao.TemporadaColportorDao;
import mx.edu.um.mateo.colportor.dao.TemporadaDao;
import mx.edu.um.mateo.colportor.dao.TraspasoDao;
import mx.edu.um.mateo.colportor.model.Asociado;
import mx.edu.um.mateo.colportor.model.Colportor;
import mx.edu.um.mateo.colportor.model.Documento;
import mx.edu.um.mateo.colportor.model.TemporadaColportor;
import mx.edu.um.mateo.contabilidad.dao.EjercicioDao;
import mx.edu.um.mateo.contabilidad.model.Ejercicio;
import mx.edu.um.mateo.contabilidad.model.EjercicioPK;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.dao.EmpresaDao;
import mx.edu.um.mateo.general.dao.RolDao;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.inventario.dao.AlmacenDao;
import mx.edu.um.mateo.inventario.model.Almacen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Repository
@Transactional
public class TraspasoDaoHibernate extends BaseDao implements TraspasoDao {

    @Autowired
    private AsociadoDao asociadoDao;
    @Autowired
    private EmpresaDao empresaDao;
    @Autowired
    private RolDao rolDao;
    @Autowired
    private AlmacenDao almacenDao;
    @Autowired
    private ColportorDao colportorDao;
    @Autowired
    private EjercicioDao ejercicioDao;
    @Autowired
    private TemporadaDao temporadaDao;
    @Autowired
    private TemporadaColportorDao tmpClpDao;
    @Autowired
    private DocumentoDao docClpDao;
    @Autowired
    @Qualifier(value = "dataSourcePg")
    private DataSource dsPg;

    @Override
    @Transactional
    public void traspasaColportores() throws Exception {
        //Borrar colportores y asociados
        
        
        Usuario usuario = null;
        //Leer los colportores de rigel
        String COMANDO = "select * "
                + "from "
                + "( "
                + "select "
                + "case when tipo_user = 'S' then 'asociado' when tipo_user = 'Alum' or tipo_user = 'Lin' then 'colportor' end as entity_type, "
                + "id, 'FALSE', 'FALSE', last_name, '.', email, 'FALSE', 'TRUE', first_name, '71fe4783816d1cf739450b7b9a3fa0a92ce6e591' as password, "
                + "clave, version, 'A', 'Direccion','Colonia','Municipio','8262630900' as telefono,now(), '0000000', 'tipoClp', "
                + "1, '001-2013', 1, 1 "
                + "from app_user u "
                + ") a "
                + "where entity_type is not null "
                + "and id > 2 "
                + "order by id ";

        PreparedStatement pstmt = null;
        ResultSet rset = null;
        List <Usuario> mapa = new ArrayList<>();

        try {
            pstmt = dsPg.getConnection().prepareStatement(COMANDO);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                if (rset.getString("entity_type").equals("asociado")) {
                    usuario = new Asociado(
                            rset.getString("email"), rset.getString("password"), rset.getString("email"), rset.getString("first_name"), rset.getString("last_name"), ".",
                            "A", ".", "8262630900", "Av. Libertad 1300", "Barrio Matamoros", "Montemorelos");
                } else if (rset.getString("entity_type").equals("colportor")) {
                    usuario = new Colportor(
                            rset.getString("email"), rset.getString("password"), rset.getString("email"), rset.getString("first_name"), rset.getString("last_name"), ".",
                            rset.getString("clave"), "A", "8262630900", "Av. Libertad 1300", "Barrio Matamoros", "Montemorelos",
                            "tipoClp", "0000000", new Date());
                    if (((Colportor) usuario).getClave() == null) {
                        usuario.setUsername(usuario.getCorreo());
                        ((Colportor) usuario).setClave("00000");
                    }
                    if (!((Colportor) usuario).getCorreo().startsWith(((Colportor) usuario).getClave())) {
                        usuario.setUsername(usuario.getCorreo());
                    }
                }
                
                log.debug("Usuario {}", usuario);
                usuario.setPassword("1");
                mapa.add(usuario);
            }
        } catch (SQLException ex) {
            throw new Exception(ex);
        } finally {
            pstmt = null;
            rset = null;
        }
        
        log.debug("Num registros {}", mapa.size());

        //Inserta colportores y asociados
        Empresa empresa = empresaDao.obtiene(1L);
        Almacen almacen = almacenDao.obtiene(1L);
        EjercicioPK key = new EjercicioPK("001-2013", empresa.getOrganizacion());
        Ejercicio ejercicio = ejercicioDao.obtiene(key);
        try{
            for (Usuario us : mapa) {
                log.debug("Leyendo usuario {}", us);
                if (us.isTipoAsociado()) {
                    Set rolesClp = new HashSet();
                    rolesClp.add(rolDao.obtiene("ROLE_ASOC"));
                    us.setRoles(rolesClp);
                    us.setEmpresa(empresa);
                    us.setAlmacen(almacen);
                    us.setEjercicio(ejercicio);
                    asociadoDao.crea((Asociado) us, null);
                } else if (us.isTipoColportor()) {
                    Set rolesClp = new HashSet();
                    rolesClp.add(rolDao.obtiene("ROLE_CLP"));
                    us.setRoles(rolesClp);
                    us.setEmpresa(empresa);
                    us.setAlmacen(almacen);
                    us.setEjercicio(ejercicio);
                    colportorDao.crea((Colportor) us, null);
                }
            }
        }catch(Exception e){
            log.error("Error al intentar insertar los colportores y asociados");
            throw new Exception(e);
        }
    }
    
    @Transactional
    public void traspasaTemporadasColportor() throws Exception {
        String COMANDO = "select u.id, u.clave, tc.status, tc.objetivo, tc.temporada_id, tc.user_captura, tc.fecha_captura, au.clave as asociado " +
            "from temporada_colportor tc, app_user u, app_user au " +
            "where u.id = colportor_id " +
            "and au.id = user_captura ";
        
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        
        TemporadaColportor tmpClp = null;
        Integer conta = 0;

        try {
            pstmt = dsPg.getConnection().prepareStatement(COMANDO);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                tmpClp = new TemporadaColportor(rset.getString("status"), rset.getString("objetivo"), "");
                tmpClp.setAsociado(asociadoDao.obtiene(rset.getString("asociado")));
                tmpClp.setColportor(colportorDao.obtiene(rset.getString("clave")));
                tmpClp.setFecha(rset.getDate("fecha_captura"));
                tmpClp.setTemporada(temporadaDao.obtiene(rset.getLong("temporada_id")));
                
                log.debug("creando temporadaClp # {}, {}", conta++, tmpClp);
                
                tmpClpDao.crea(tmpClp);
            }
        } catch (SQLException ex) {
            throw new Exception(ex);
        } finally {
            pstmt = null;
            rset = null;
        }
        
        
    }
    
    public void traspasaDocumentos() throws Exception{
        String COMANDO = "select a.fecha, a.folio, a.importe, a.observaciones, a.tipodedocumento, u.clave, tc.temporada_id " +
            "from temporada_colportor tc, app_user u,  " +
            "(  " +
            "select fecha, folio, case when tipo_documento_id = 5 then importe_bonificable else importe end as importe, observaciones,   " +
            "case when tipo_documento_id = 1 then 'Deposito_Caja'   " +
            "        when tipo_documento_id = 2 then 'Deposito_Banco'   " +
            "        when tipo_documento_id = 3 then 'Diezmo'   " +
            "        when tipo_documento_id = 4 then 'Notas_De_Compra'   " +
            "        when tipo_documento_id = 5 then 'Boletin'   " +
            "        when tipo_documento_id = 6 then 'Informe'   " +
            "end as tipodedocumento, temporada_colportor_id  " +
            "from documento_colportor  " +
            ") as a  " +
            "where a.temporada_colportor_id = tc.id  " +
            "and tc.colportor_id = u.id ";
        
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        
        Documento docClp = null;
        Integer conta = 0;

        try {
            pstmt = dsPg.getConnection().prepareStatement(COMANDO);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                docClp = new Documento();
                docClp.setFecha(rset.getDate("fecha"));
                docClp.setFolio(rset.getString("folio"));
                docClp.setImporte(rset.getBigDecimal("importe"));
                docClp.setObservaciones(rset.getString("observaciones"));
                docClp.setTemporadaColportor(tmpClpDao.obtiene(colportorDao.obtiene(rset.getString("clave")), temporadaDao.obtiene(rset.getLong("temporada_id"))));
                docClp.setTipoDeDocumento(rset.getString("tipodedocumento"));
                
                log.debug("creando documento # {}, {}", conta++, docClp);
                
                docClpDao.crea(docClp);
            }
        } catch (SQLException ex) {
            throw new Exception(ex);
        } finally {
            pstmt = null;
            rset = null;
        }
    }
}
