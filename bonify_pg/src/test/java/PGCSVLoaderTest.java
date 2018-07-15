import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.postgresql.core.BaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
public class PGCSVLoaderTest {
    PGCSVLoader pgcsvLoader = new PGCSVLoader();

    @After
    public void after() throws SQLException {
        Connection conn = pgcsvLoader.getConnection();
        String sql = "DELETE FROM bank";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.execute();
    }
    @Before
    public void before(){

        PGCSVLoader.loadApplicationProperties();
    }

    @Test
    public void copyBankData() {
        Assert.assertTrue(pgcsvLoader.copyBankData());
    }

    @Test
    public void insertData() throws SQLException, IOException {
        Connection connection = pgcsvLoader.getConnection();
        long inserted = pgcsvLoader.insertData((BaseConnection) connection);
        Assert.assertEquals(inserted, 4);
    }

    @Test
    public void findData() throws IOException, SQLException {
        Connection connection = pgcsvLoader.getConnection();
        pgcsvLoader.insertData((BaseConnection) connection);
        Assert.assertEquals("Eurocity", pgcsvLoader.findData(10030700));
    }

    @Test
    public void getConnection() throws SQLException {
        Assert.assertNotNull(pgcsvLoader.getConnection());
    }

    @Test
    public void loadApplicationProperties() {

        Assert.assertEquals(PGCSVLoader.prop.size(), 5);
    }
}