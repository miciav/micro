package it.tasd.example.micro;

import java.sql.DriverManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import static org.assertj.core.api.Assertions.assertThat;

/** Regression for the old JDBC handshake failure, against a real MariaDB server. */
@EnabledIfEnvironmentVariable(named = "MARIADB_TEST_URL", matches = ".+")
class MariaDbTlsTests {
    @Test
    void authenticatesAndQueriesOverVerifiedTls() throws Exception {
        try (var connection = DriverManager.getConnection(System.getenv("MARIADB_TEST_URL"),
                System.getenv().getOrDefault("DB_USERNAME", "micro"),
                System.getenv().getOrDefault("DB_PASSWORD", "tasd"));
             var statement = connection.createStatement()) {
            try (var result = statement.executeQuery("SHOW SESSION STATUS LIKE 'Ssl_version'")) {
                assertThat(result.next()).isTrue();
                assertThat(result.getString(2)).startsWith("TLSv1.");
                System.out.println("Verified JDBC connection: " + result.getString(2));
            }
            try (var result = statement.executeQuery("SELECT 1")) {
                assertThat(result.next()).isTrue();
                assertThat(result.getInt(1)).isEqualTo(1);
            }
        }
    }
}
