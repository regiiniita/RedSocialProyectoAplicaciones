USE thespot;

INSERT INTO categorias (nombre, descripcion, total_lugares) VALUES
('Restaurantes', 'Lugares para comer: comida regional, mariscos, tacos y más.', 2),
('Cafeterías', 'Cafés para trabajar, estudiar o platicar.', 2),
('Parques', 'Espacios al aire libre para caminar, correr o pasar la tarde.', 1),
('Museos', 'Museos y centros culturales con exposiciones.', 1),
('Bares', 'Bares y cervecerías para salir en la noche.', 1);

INSERT INTO lugares (nombre, categoria_id, direccion, descripcion, imagen) VALUES
('Tacos El Güero',             (SELECT id FROM categorias WHERE nombre = 'Restaurantes'), 'Calle Hidalgo 214, Centro', 'Taquería de carne asada y tripitas, abierta en la noche.', NULL),
('Mariscos La Ballena',        (SELECT id FROM categorias WHERE nombre = 'Restaurantes'), 'Blvd. Rodríguez 1180, Col. Pitic', 'Cócteles, aguachiles y tostadas de mariscos.', NULL),
('Café Desierto',              (SELECT id FROM categorias WHERE nombre = 'Cafeterías'), 'Av. Universidad 58, Centro Norte', 'Café de especialidad con espacio para estudiar.', NULL),
('La Tostadora',               (SELECT id FROM categorias WHERE nombre = 'Cafeterías'), 'Calle Juárez 402, Col. Modelo', 'Cafetería pequeña con pan dulce hecho en casa.', NULL),
('Parque Las Palmas',          (SELECT id FROM categorias WHERE nombre = 'Parques'), 'Av. Las Palmas s/n, Col. Las Palmas', 'Parque con pista para correr y áreas verdes.', NULL),
('Museo de Historia Regional', (SELECT id FROM categorias WHERE nombre = 'Museos'), 'Calle Serdán 30, Centro', 'Exposiciones sobre la historia de la región.', NULL),
('Cervecería Sahuaro',         (SELECT id FROM categorias WHERE nombre = 'Bares'), 'Blvd. Kino 905, Col. Lomas', 'Cerveza artesanal y música en vivo.', NULL);

INSERT INTO resenas (fecha, autor_id, lugar_id, calificacion, texto, imagen, estado) VALUES
(CURDATE(),                   (SELECT id FROM usuarios WHERE username = 'regiiniita'), (SELECT id FROM lugares WHERE nombre = 'Tacos El Güero'), 5, 'Los mejores tacos de asada, la salsa de la casa está buenísima.', NULL, 'PUBLICADA'),
(CURDATE(),                   (SELECT id FROM usuarios WHERE username = 'Yisus0714'),  (SELECT id FROM lugares WHERE nombre = 'Tacos El Güero'), 4, 'Muy buenos y rápidos. En la noche se llena.', NULL, 'PUBLICADA'),
(CURDATE() - INTERVAL 10 DAY, (SELECT id FROM usuarios WHERE username = 'regiiniita'), (SELECT id FROM lugares WHERE nombre = 'Tacos El Güero'), 4, 'Buen sabor, aunque el lugar es pequeño.', NULL, 'PUBLICADA'),
(CURDATE(),                   (SELECT id FROM usuarios WHERE username = 'Yisus0714'),  (SELECT id FROM lugares WHERE nombre = 'Mariscos La Ballena'), 5, 'El aguachile negro es espectacular.', NULL, 'PUBLICADA'),
(CURDATE() - INTERVAL 20 DAY, (SELECT id FROM usuarios WHERE username = 'regiiniita'), (SELECT id FROM lugares WHERE nombre = 'Mariscos La Ballena'), 4, 'Buenas porciones, un poco caro.', NULL, 'PUBLICADA'),
(CURDATE(),                   (SELECT id FROM usuarios WHERE username = 'regiiniita'), (SELECT id FROM lugares WHERE nombre = 'Café Desierto'), 5, 'Ideal para estudiar, buen wifi y el latte muy rico.', NULL, 'PUBLICADA'),
(CURDATE() - INTERVAL 5 DAY,  (SELECT id FROM usuarios WHERE username = 'Yisus0714'),  (SELECT id FROM lugares WHERE nombre = 'Café Desierto'), 3, 'El café bien, los postres caros.', NULL, 'PUBLICADA'),
(CURDATE() - INTERVAL 3 DAY,  (SELECT id FROM usuarios WHERE username = 'Yisus0714'),  (SELECT id FROM lugares WHERE nombre = 'La Tostadora'), 4, 'Las conchas recién hechas son lo mejor.', NULL, 'PUBLICADA'),
(CURDATE() - INTERVAL 12 DAY, (SELECT id FROM usuarios WHERE username = 'regiiniita'), (SELECT id FROM lugares WHERE nombre = 'Parque Las Palmas'), 5, 'Muy limpio y con mucha sombra.', NULL, 'PUBLICADA'),
(CURDATE() - INTERVAL 30 DAY, (SELECT id FROM usuarios WHERE username = 'Yisus0714'),  (SELECT id FROM lugares WHERE nombre = 'Museo de Historia Regional'), 4, 'Interesante y económico.', NULL, 'PUBLICADA'),
(CURDATE() - INTERVAL 2 DAY,  (SELECT id FROM usuarios WHERE username = 'regiiniita'), (SELECT id FROM lugares WHERE nombre = 'Cervecería Sahuaro'), 5, 'La IPA de la casa está excelente.', NULL, 'PUBLICADA');