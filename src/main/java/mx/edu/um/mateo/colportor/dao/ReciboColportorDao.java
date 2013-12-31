/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.edu.um.mateo.colportor.dao;

import java.util.Map;
import mx.edu.um.mateo.colportor.model.ReciboColportor;

/**
 *
 * @author osoto
 */
public interface ReciboColportorDao {
    public Map<String, Object> lista(Map<String, Object> params);

    public ReciboColportor obtiene(Long id);

    public ReciboColportor crea(ReciboColportor ReciboColportor);

    public ReciboColportor actualiza(ReciboColportor ReciboColportor);

    public String elimina(Long id);
}
