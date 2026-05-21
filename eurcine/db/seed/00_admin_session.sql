-- Crea tabella sessioni admin per autenticazione Bearer persistente
CREATE TABLE IF NOT EXISTS admin_session (
  id BIGINT NOT NULL AUTO_INCREMENT,
  token VARCHAR(255) NOT NULL,
  admin_id BIGINT NOT NULL,
  created_at DATETIME(6) NOT NULL,
  expires_at DATETIME(6) NOT NULL,
  revoked BIT(1) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_admin_session_token (token),
  KEY idx_admin_session_admin (admin_id),
  CONSTRAINT fk_admin_session_admin FOREIGN KEY (admin_id) REFERENCES admin (id)
);
