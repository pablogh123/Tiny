package tinymvc.repositories.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import tinymvc.entities.Alumno;
import tinymvc.repositories.interfaces.I_AlumnoRepository;

public class AlumnoRepository implements I_AlumnoRepository {
    
    private Connection conn;

    public AlumnoRepository(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void save(Alumno alumno) {
        if(alumno==null) return;
        
        String query = "INSERT INTO alumnos(nombre, apellido, DNI, curso) VALUES (?,?,?,?)";
        
        try (PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)){
            
            ps.setString(1, alumno.getNombre());
            ps.setString(2, alumno.getApellido());
            ps.setInt(3, alumno.getDNI());
            ps.setString(4, alumno.getCurso());
            
            ps.execute();
            
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()) alumno.setId(rs.getInt(1));
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void remove(int id) {
        
        try (PreparedStatement ps= conn.prepareStatement("DELETE FROM alumnos WHERE id=?")){
            ps.setInt(1, id);
            ps.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void update(Alumno alumno) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Alumno> getAll() {
        List<Alumno> list = new ArrayList();

        try (ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM alumnos")){
            while(rs.next()){
                list.add(new Alumno(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getInt("DNI"),
                        rs.getString("curso")
                ));
            }
        } catch (Exception e) {
                System.out.println(e);
        }
            
        return list;
    }
    
    @Override
    public List<Alumno> getLikeApellido(String apellido) {
        List<Alumno> list = new ArrayList();
        try (ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM alumnos WHERE apellido LIKE '%"+apellido+"%'")){
            while(rs.next()){
                list.add(new Alumno(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("Apellido"),
                        rs.getInt("DNI"),
                        rs.getString("curso")
                ));
            }            
        } catch (Exception e) {
            System.out.println(e);
        }
        
        return list;
    }  
}
