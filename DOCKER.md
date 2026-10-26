# Docker Compose PostgreSQL Setup

This project uses Docker Compose to run PostgreSQL for local development.

## Prerequisites

- Docker and Docker Compose installed

## Running PostgreSQL

The existing Postgres container is running on host port `5432` (container `showlife_host_db`).  
The Compose stack maps Postgres to host port `5433` to avoid collision.

Start the database:

```bash
docker compose up -d
```

Check status:

```bash
docker compose ps
docker compose logs -f postgres
```

Stop:

```bash
docker compose down
```

Remove data volume (⚠️ destructive):

```bash
docker compose down -v
```

## Connection Details

- Host: localhost
- Port: 5433
- Database: asset_tracker
- User: asset_tracker_user
- Password: asset_tracker_password

The `application.properties` is configured to connect to `jdbc:postgresql://localhost:5433/asset_tracker`.

## Using Existing Image

`docker-compose.yml` uses the existing image `postgres:16-alpine` which matches the running container `showlife_host_db`. The container name `asset_tracker_postgres` is isolated from existing containers.

## Tips

- To connect with psql: `psql -h localhost -p 5433 -U asset_tracker_user -d asset_tracker`
- The volume `postgres_data` persists data on host.
