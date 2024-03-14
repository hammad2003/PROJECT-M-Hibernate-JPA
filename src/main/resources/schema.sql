-- Crear la tabla Juego
CREATE TABLE Juego (
    JuegoID SERIAL PRIMARY KEY,
    Nombre VARCHAR(255) NOT NULL,
    Descripcion TEXT
);

-- Crear la tabla Mod con detalles en formato JSONB
CREATE TABLE Mod (
    ModID SERIAL PRIMARY KEY,
    JuegoID INT,
    Nombre VARCHAR(255) NOT NULL,
    Autor VARCHAR(255) NOT NULL,
    Descripcion TEXT,
    FOREIGN KEY (JuegoID) REFERENCES Juego(JuegoID)
);

-- Crear la tabla Detalle
CREATE TABLE Detalle (
    DetalleID SERIAL PRIMARY KEY,
    ModID INT,
    Descripcion TEXT,
    FOREIGN KEY (ModID) REFERENCES Mod(ModID)
);

-- Crear la tabla Categoria
CREATE TABLE Categoria (
    CategoriaID SERIAL PRIMARY KEY,
    ModID INT,
    Nombre VARCHAR(255) NOT NULL,
    FOREIGN KEY (ModID) REFERENCES Mod(ModID)
);