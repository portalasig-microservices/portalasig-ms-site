# portalasig-ms-site

![Build and Verify Services](https://github.com/portalasig-microservices/portalasig-ms-site/actions/workflows/ci.yml/badge.svg?branch=develop)


Microservicio de sitios académicos de PortalAsig: cursos, semestres, sitios, secciones, horarios, evaluaciones, contenidos y participantes.

## Tests y cobertura

La suite de integración corre contra un MySQL 8 real levantado con Testcontainers
(`AbstractMysqlIntegrationTest` de core-lib), con rollback transaccional por test.

```bash
mvn clean verify
```

`verify` ejecuta checkstyle, los tests y el chequeo de cobertura JaCoCo:
el build **falla** si la cobertura baja de **70% líneas / 60% ramas**.

> Nota: con Docker ≥ 29 puede ser necesario `~/.docker-java.properties` con
> `api.version=1.44` para que Testcontainers hable con el daemon.

Estado actual: **80.5% líneas / 61.3% ramas** (60 tests).
