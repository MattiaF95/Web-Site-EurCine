UPDATE utente
SET password_hash = '$2b$10$YZ17OCUlJnmwZha5RehF6uDJ8LP72SkgbGlVuOBwLhid7p5bLV.eK'
WHERE email = 'admin@eurcine.it'
  AND password_hash = 'admin123';
