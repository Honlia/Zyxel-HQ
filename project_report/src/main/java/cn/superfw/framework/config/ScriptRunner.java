package cn.superfw.framework.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.superfw.framework.helper.AssertHelper;
import cn.superfw.framework.helper.StreamHelper;



/**
 * SQL脚本运行类
 * @author chenchao
 * @since 1.0
 */
public class ScriptRunner {
    private static final Logger log = LoggerFactory.getLogger(ScriptRunner.class);
    private static final String DEFAULT_DELIMITER = ";";
    //数据库连接对象
    private Connection connection;
    //默认的分隔符;
    private String delimiter = DEFAULT_DELIMITER;
    public ScriptRunner(Connection connection) {
        this.connection = connection;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public void runScript(String resource) throws IOException, SQLException {
        AssertHelper.notNull(resource);
        InputStream input = StreamHelper.getStreamFromClasspath(resource);
        Reader reader = new InputStreamReader(input, "UTF-8");
        runScript(reader);
    }

    /**
     * 根据reader读取sql脚本，并运行
     * @param reader 脚本资源
     */
    public void runScript(Reader reader) throws IOException, SQLException {
        AssertHelper.notNull(connection);
        try {
            boolean originalAutoCommit = connection.getAutoCommit();
            try {
                connection.setAutoCommit(true);
                runScript(connection, reader);
            } finally {
                connection.setAutoCommit(originalAutoCommit);
            }
        } catch (IOException e) {
            throw e;
        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error running script.  Cause: " + e, e);
        }
    }

    /**
     * 根据给定的sql脚本资源、数据库连接对象，执行sql脚本
     * @param conn 数据库连接对象
     * @param reader sql脚本资源
     * @throws IOException io异常
     * @throws SQLException sql异常
     */
    private void runScript(Connection conn, Reader reader)
            throws IOException, SQLException {
        StringBuffer command = null;
        try {
            LineNumberReader lineReader = new LineNumberReader(reader);
            String line = null;
            while ((line = lineReader.readLine()) != null) {
                if (command == null) {
                    command = new StringBuffer();
                }
                String trimmedLine = line.trim();
                if (trimmedLine.startsWith("--")) {
                    log.info(trimmedLine);
                } else if (trimmedLine.length() < 1 || trimmedLine.startsWith("//")) {
                    //Do nothing
                } else if (trimmedLine.length() < 1 || trimmedLine.startsWith("--")) {
                    //Do nothing
                } else if (trimmedLine.equals(getDelimiter()) || trimmedLine.endsWith(getDelimiter())) {
                    command.append(line.substring(0, line.lastIndexOf(getDelimiter())));
                    command.append(" ");
                    Statement statement = conn.createStatement();

                    log.info(command.toString());
                    try {
                        statement.execute(command.toString());
                    } catch (SQLException e) {
                        e.fillInStackTrace();
                        log.error("Error executing: " + command);
                    }

                    command = null;
                    try {
                        statement.close();
                    } catch (Exception e) {
                        //ignore
                    }
                    Thread.yield();
                } else {
                    command.append(line);
                    command.append(" ");
                }
            }
            conn.commit();
            log.info("conn is commited");

        } catch (SQLException e) {
            e.fillInStackTrace();
            throw e;
        } catch (IOException e) {
            e.fillInStackTrace();
            throw e;
        }
    }

    /**
     * 获取分隔符
     * @return delimiter
     */
    private String getDelimiter() {
        return delimiter;
    }
}
