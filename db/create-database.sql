-- Creates the web game's database. Run once:
--   psql -d postgres -f db/create-database.sql
-- Tables are then created by the Spring server from the JPA entities (spring.jpa.hibernate.ddl-auto=update),
-- and demo data is added by DemoData on start (nonogram.demo-data=true).
-- The original course database gamestudio_jpa is not touched.
CREATE DATABASE horror_nonogram ENCODING 'UTF8';
