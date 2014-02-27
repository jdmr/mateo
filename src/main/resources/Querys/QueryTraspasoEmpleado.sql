--Query para el traspaso de empleados con usuario en oracle a postgres, las lineas
--comentadas son porque algunos empleados no tienen la relacion de usuario.
select 
e.id, e.clave, e.nombre, e.appaterno, e.apmaterno, e.fechanacimiento, e.direccion, e.genero,e.status,
e.nacionalidad, ep.estadocivil, ep.conyuge, ep.fechamatrimonio, ep.madre, ep.padre, ep.finado_padre, ep.finado_madre,
ep.iglesia, ep.responsabilidad, el.cuenta, el.curp, el.escalafon, el.imms, el.rfc, el.turno, el.fecha_baja,
el.antiguedad_base,el.fecha_antiguedad_base, el.fecha_alta, el.antiguedad_fiscal, el.modalidad, 
el.experiencia_fuera_um, el.ife, el.rango, el.adventista//, ap.username, ap.email 
from 
aron.empleado e, aron.empleadopersonales ep, aron.empleadolaborales el
-- ,noe.app_user ap, noe.user_relacion ur  
where e.id=ep. id 
and e.id=el.id 
-- and e.id=ur.empleado_id 
and e.status='A'
-- and ap.id=ur.id