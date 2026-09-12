# Address XML Importer

Aplikace v Java / Spring Boot, která stáhne zazipovaná data z RÚIAN (obec Kopidlno), zpracuje XML pomocí DOM parseru a uloží obec a její části do PostgreSQL databáze.

## Použité technologie
* **Java 17 / Spring Boot 3** (Spring Data JPA, Hibernate)
* **PostgreSQL 16**
* **XML DOM Parser**
* **Docker**
* **Lombok**

## Databázové schéma
Data se ukládají do dvou tabulek definovaných v `src/main/resources/schema.sql`:
* `obec` (`kod` PK, `nazev`)
* `cast_obce` (`kod` PK, `nazev`, `obec_kod` FK)

## Spuštění projektu

### 1. Kompletní spuštění v Dockeru
V kořenovém adresáři projektu spusťte:

```bash
docker compose up --build
```

### 2. Spuštění z IDE (IntelliJ IDEA)
Spusťte pouze databázi:

```bash
docker compose up -d postgres
```

Spusťte třídu `AddressImporterApplication.java`.

Pro ověření spusťte test `AddressImporterApplicationTests.java`.