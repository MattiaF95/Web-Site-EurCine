-- Cleanup after JWT migration: admin_session table is no longer used.
DROP TABLE IF EXISTS admin_session;
