CREATE TABLE usuario
(
    uuid     UUID NOT NULL,
    usuario  VARCHAR(255),
    password VARCHAR(255),
    CONSTRAINT pk_usuario PRIMARY KEY (uuid)
);