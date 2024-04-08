package diploma.cloud.repository;

import diploma.cloud.domain.File;
import diploma.cloud.domain.Users;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class RepositoryApp {
    private final DataSource dataSource;

    public RepositoryApp(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean checkCredentials(String login, String password) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE login = ? AND password = ?")) {
            ps.setString(1, login);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
   public Users findByAuthToken(String authToken) {
       try (Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE auth_token = ?")) {
           ps.setString(1, authToken);
           try (ResultSet rs = ps.executeQuery()) {
               if (rs.next()) {
                   return new Users(rs.getString("login"), rs.getString("password"), rs.getString("auth_token"));
               }
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
       return null;
   }

   public void save(File file) {
       try (Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO files (filename, hash) VALUES (?, ?)")) {
           ps.setString(1, file.getFilename());
           ps.setString(2, file.getHash());
           ps.executeUpdate();
       } catch (SQLException e) {
           e.printStackTrace();
       }
   }
   public File findByFilename (String filename) {
       try (Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM files WHERE filename = ?")) {
           ps.setString(1, filename);
           try (ResultSet rs = ps.executeQuery()) {
               if (rs.next()) {
                   return new File(rs.getString("filename"), rs.getString("hash"));
               }
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
       return null;
   }

   public void deleteByFilename (String filename) {
       try (Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM files WHERE filename = ?")) {
           ps.setString(1, filename);
           ps.executeUpdate();
       } catch (SQLException e) {
           e.printStackTrace();
       }
   }

   public List<File> findAll() {
       try (Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM files")) {
           try (ResultSet rs = ps.executeQuery()) {
               List<File> files = new java.util.ArrayList<>();
               while (rs.next()) {
                   files.add(new File(rs.getString("filename"), rs.getString("hash")));
               }
               return files;
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
       return null;
   }

}
