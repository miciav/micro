# micro
First microservice for the course of TASD - Unimib

## MariaDB laboratory version (0.3.0)

This version preserves `/api/sensors`, `/api/sensors/{id}` and
`/api/sensors/{id}/value`, using Java 21, Spring Boot 3.5.16 and
MariaDB Connector/J 3.5.10. The old `miciav/micro:0.2` image is unchanged.

```sh
docker compose pull
docker compose up -d --no-build
python3 scripts/check_api.py
```

MariaDB 11.4 runs natively on ARM64 and AMD64. Compose waits for the database
health check and uses a database-scoped `micro` account. Only the HTTP port is
published, on loopback. Override `MICRO_HTTP_PORT`, `DB_PASSWORD` and
`DB_ROOT_PASSWORD` when needed; the defaults are teaching credentials.
Use a fresh volume: this procedure does not migrate a MySQL 8 data directory.
The Demo profile deliberately recreates its schema at application startup.

The JDBC URL defaults to `sslMode=verify-full`. MariaDB Server 11.4 and
Connector/J 3.4+ support zero-configuration TLS verification using the server
certificate fingerprint and a nonempty password. SSL and certificate
verification are not disabled. See the [official TLS guide](https://mariadb.com/docs/connectors/mariadb-connector-j/using-tls-ssl-with-mariadb-java-connector).
`MYSQL_ADDRESS` and `MYSQL_PORT` retain their legacy names for compatibility.
`DB_NAME`, `DB_USERNAME`, `DB_PASSWORD` and `DB_SSL_MODE` configure the new driver.

```sh
mvn test
# Optional real-database test (server must be running and reachable):
MARIADB_TEST_URL='jdbc:mariadb://127.0.0.1:13306/db_micro?sslMode=verify-full' \
  DB_USERNAME=root DB_PASSWORD=tasd mvn test
```

The optional JDBC test asserts an encrypted, verified connection and a real
SQL result; it skips when `MARIADB_TEST_URL` is unset. Existing controller
and database tests use H2. Maven 3.6.3+ is required; the wrapper selects 3.9.16.

To reproduce dependency loss, stop `mysql-db` and request the value again:

```sh
docker compose stop mysql-db
curl -i http://localhost:8080/api/sensors/1/value
docker compose down -v
```

The final command deletes this Compose project's demonstration database volume.

## Published image

`miciav/micro:0.3.0` (also `miciav/micro:0.3`) contains both `linux/arm64`
and `linux/amd64`; Docker selects the matching runtime automatically.
Build the source locally with `docker compose up --build -d`.
The Java build stage runs on the builder's native architecture; its portable
JAR is copied into the target architecture's Java 21 runtime.

```sh
docker buildx build --platform linux/arm64,linux/amd64 \
  -t miciav/micro:0.3.0 -t miciav/micro:0.3 --push .
```
