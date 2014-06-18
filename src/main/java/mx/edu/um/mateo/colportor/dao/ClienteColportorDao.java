/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.dao;

import java.util.Map;
import mx.edu.um.mateo.colportor.model.ClienteColportor;

/**
 *
 * @author osoto
 */
public interface ClienteColportorDao {

    public Map<String, Object> lista(Map<String, Object> params);

    public ClienteColportor obtiene(Long id);

    public ClienteColportor crea(ClienteColportor clienteColportor);

    public ClienteColportor actualiza(ClienteColportor clienteColportor);

    public String elimina(Long id);
}
