package cn.superfw.framework.config;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.superfw.framework.helper.JdbcHelper;


/**
 * 脚本执行类
 * @author chenchao
 * @since 1.0
 */
public class ScriptsExecutor {
    private static final Logger log = LoggerFactory.getLogger(ScriptsExecutor.class);
    /** 数据源 */
    private DataSource dataSource;
    /** DB重建开关 */
    private boolean dropAndCreate;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setDropAndCreate(boolean dropAndCreate) {
        this.dropAndCreate = dropAndCreate;
    }

    public void run() {
        log.info("scripts running......");
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            if(!dropAndCreate) {
                log.info("script has completed execution.skip this step");
                return;
            }
            String databaseType = JdbcHelper.getDatabaseType(conn);
            String[] schemas = new String[]{"db/schema-" + databaseType + ".sql", "db/init-data.sql"};
            ScriptRunner runner = new ScriptRunner(conn);
            for(String schema : schemas) {
                runner.runScript(schema);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                JdbcHelper.close(conn);
            } catch (SQLException e) {
                //ignore
            }
        }
    }
}
