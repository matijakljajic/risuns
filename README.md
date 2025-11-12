# Music Catalog

Viewer discretion is advised as it was cobbled together in 2-3 days.

Spring Boot + JSP application for browsing artists, albums, playlists, and chatting with other listeners acting as a showcase for Spring. It's basically [last.fm](https://last.fm/), but worse.

## Requirements

- Java 21
- Maven Wrapper (`./mvnw`) bundled with the repo
- Optional: Docker/Podman + MySQL 8

## Running Locally

### Quick start (H2 + seeded demo data)

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=h2,seed-jpa
```

This launches the app on `http://localhost:8080` using H2 in-memory storage and populates artists, albums, tracks, playlists, and users via `DataSeeder`. Good for demos and development.

### Integration-style run (H2, SQL fixtures)

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=h2,seed-sql
```

Uses deterministic SQL scripts in `src/main/resources/db/h2`—handy for tests or reproducible scenarios.

### MySQL option

1. Start MySQL (example using Podman):
   ```bash
   podman run --name mysql8 -d \
     -e MYSQL_ROOT_PASSWORD=secret \
     -e MYSQL_DATABASE=musicdb \
     -e MYSQL_USER=music \
     -e MYSQL_PASSWORD=musicpass \
     -p 3306:3306 \
     -v mysql8data:/var/lib/mysql:Z \
     mysql:8.0
   ```
2. Run the app:
   ```bash
   SPRING_PROFILES_ACTIVE=mysql,seed-sql ./mvnw spring-boot:run
   ```
   Remove `seed-sql` if you want to keep existing data untouched.

## Login Details (seeded profiles)

When seeding is enabled you’ll get several example accounts, including:

| Role | Username | Password |
| --- | --- | --- |
| Admin | `admin` | `admin` |
| User  | `matija` | `matija` |

You have to use the admin account to explore `/admin/**`.

## Project Layout

```
src/main/java/com/matijakljajic/music_catalog
├── config/        # Security + data seeding
├── model/         # JPA entities
├── repository/    # Spring Data repositories
├── service/       # Business logic (catalog, library, messaging, report, etc.)
└── web/           # Controllers grouped by feature (home, search, catalog, admin…)

src/main/webapp/WEB-INF/jsp
├── home/          # home/home.jsp
├── search/        # search/search.jsp
├── catalog/       # album & artist views
├── library/       # playlist pages
├── profile/       # user profile + report pages
├── messaging/     # chat UI
└── admin/         # admin dashboard
```

Jasper templates live in `src/main/resources/templates/reports/`.

# License

This catastrophe is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
