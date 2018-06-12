/**  
 * Proyecto: Juego de la vida.
 * Resuelve todos los aspectos del almacenamiento del DTO Simulacion.  
 * Utiliza base de datos db4o.
 * Colabora en el patron Fachada.
 * @since: prototipo2.0
 * @source: SimulacionesDAO.java  
 * @version: 2.1 - 2018.05.16
 * @author: ajp
 */
 
package accesoDatos.mySql;
 
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
 
import javax.swing.table.DefaultTableModel;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
 
 
import accesoDatos.DatosException;
import accesoDatos.OperacionesDAO;
import accesoDatos.db4o.MundosDAO;
import accesoDatos.db4o.UsuariosDAO;
import modelo.Mundo;
import modelo.Simulacion;
import modelo.Simulacion.EstadoSimulacion;
import modelo.Usuario;
import util.Fecha;
 
public class SimulacionesDAO implements OperacionesDAO {
 
    // Singleton  
    private static SimulacionesDAO instancia = null;
 
    // Elemento de almacenamiento. Base datos db4o
    private Connection db;
 
    private java.sql.Statement sentenciaSim;
    private ResultSet rsSimulaciones;
    private DefaultTableModel tmSimulaciones;
    private ArrayList<Object> bufferObjetos;
 
    /**
     *  Método estático de acceso a la instancia única.
     *  Si no existe la crea invocando al constructor interno.
     *  Utiliza inicialización diferida.
     *  Sólo se crea una vez; instancia única -patrón singleton-
     *  @return instancia
     */
    public static SimulacionesDAO getInstancia() {
       return null;
    }
 
    /**
     * Constructor por defecto de uso interno.
     * @throws SQLException
     * @throws DatosException
     */
    private SimulacionesDAO() throws SQLException, DatosException {
    
    }
 
    /**
     * Inicializa el DAO, detecta si existen las tablas de datos capturando la  
     * excepción SQLException.
     * @throws SQLException
     */
    private void inicializar()throws SQLException{
   
 
      }
   
 
    private void crearTablaSimulaciones() throws SQLException{
        java.sql.Statement sentencia = db.createStatement();
 
        sentencia.executeUpdate("CREATE TABLE usuarios("
                + "idUsr VARCHAR(20) NOT NULL,"
                + "fecha DATE NOT NULL,"
                + "mundo VARCHAR (10) NOT NULL"
                + "estado VARCHAR (10) NOT NULL"
                + "PRIMARY KEY(idUsr, fecha));");
    }
 
 
 
 
    /**
     *  Método para generar de datos predeterminados.
     */
    private void cargarPredeterminados() throws SQLException, DatosException {
     
    }
 
 
    //Operaciones DAO
 
    /**
     * Obtiene una Sesion dado un objeto, reenvía al método que utiliza idSesion.
     * @param obj - la Simulacion a buscar.
     * @return - la Simulacion encontrada.
     * @throws DatosException - si no existe.
     */
    @Override
    public Simulacion obtener(Object obj) throws DatosException  {
        return this.obtener(((Simulacion) obj).getIdSimulacion());
    }
 
    /**      
     * Obtiene una simulación dado su id.
     * @param idSimulacion - el idUsr+fecha de la Simulacion a buscar.  
     * @return - la Simulacion encontrada.
     * @throws DatosException - si no existe.
     */    
    public Simulacion obtener(String idSimulacion) throws DatosException {    
        try {
            rsSimulaciones=sentenciaSim.executeQuery("SELECT * FROM usuarios WHERE isSim ='"+ idSimulacion +"'");
 
            //Establece columndas y etiquetas
            establecerColumnasModelo();
 
            //Borrado previo de filas
            rellenaFilasModelo();
 
            //Volcado desde el resultSet
            rellenaFilasModelo();
 
 
            //Actualizar buffer de objetos
            sincronizaBufferObjetos();
            if(bufferObjetos.size()>0) {
                    return (Simulacion) bufferObjetos.get(0);
            }
        }
        catch(SQLException e){
            throw new DatosException("Obtener:"+ idSimulacion + "no existe");
        }
        return null;
 
    }
 
 
    private void sincronizaBufferObjetos() {
		// TODO Auto-generated method stub
		
	}

	private void establecerColumnasModelo() {
		// TODO Auto-generated method stub
		
	}

	private void rellenaFilasModelo() {
        Object[] datosFila = new Object[tmSimulaciones.getColumnCount()];
        //INCOMPLETO
        try {
            while (rsSimulaciones.next()) {
                for (int i=0; i< tmSimulaciones.getColumnCount();i++) {
                    datosFila[i]=rsSimulaciones.getObject(i+1);
                }
                ((DefaultTableModel)tmSimulaciones).addRow(datosFila);
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
        
    }

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * Metodo que devuelve el resultado de una consulta sql
	 * Con la lista completa de simulaciones
	 * @author GRUPO 1 DAM - Francisco Jurado Abad
	 */
	public List<Simulacion> obtenerTodos() {
		ResultSet rs = null;
		String sql = "SELECT * FROM Simulaciones";
		try {
			java.sql.Statement s =  db.createStatement();
			rs = s.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return (List<Simulacion>) rs;
		
	}
	/**
	 * Metodo para dar de alta una simulacion con una consulta sql
	 * @author GRUPO 1 DAM - Francisco Jurado Abad /
	 */
	@Override
	public void alta(Object obj) throws DatosException {
		assert obj != null;
		 
		Simulacion simulacion = (Simulacion) obj;
		java.sql.Statement st;
		String sql = "insert into simulacion (idUsr, fecha, mundo, estado) "
				+ "values ('" + obtener(simulacion.getIdSimulacion()); 
		try {
			st = db.createStatement();
			st.executeUpdate(sql);
			st.close();
		} catch (SQLException e) {
			//db.store(simulacion);
			
			e.printStackTrace();
		}
		throw new DatosException("Alta: " + simulacion.getIdSimulacion() + " ya existe.");
	}

	@Override
	public Object baja(String id) throws DatosException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void actualizar(Object obj) throws DatosException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String listarDatos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String listarId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void borrarTodo() {
		// TODO Auto-generated method stub
		
	}

    /**
     *  Cierra conexión.
     */
    @Override
    public void cerrar() {
        try {
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
