CREATE TABLE Apparat(
	ApparatID INT NOT NULL,
	Navn VARCHAR(100),
	Beskrivelse VARCHAR(500),
	CONSTRAINT Apparat_pk PRIMARY KEY (ApparatID)
);

CREATE TABLE Treningsøkt(
	ØktID INT NOT NULL,
	Tidspunkt DATETIME,
	Varighet INT,
	Notat VARCHAR(500),
	CONSTRAINT Treningsøkt_pk PRIMARY KEY (ØktID)
);


CREATE TABLE Øvelse(
	ØvelseID INT NOT NULL,
    ØktID INT NOT NULL,
    Navn VARCHAR(60),
    Prestasjon INT,
    CONSTRAINT Øvelse_pk PRIMARY KEY (ØvelseID),
    CONSTRAINT Øvelse_fk FOREIGN KEY (ØktID) REFERENCES Treningsøkt(ØktID)
		ON UPDATE CASCADE
		ON DELETE CASCADE
);


CREATE TABLE Friøvelse(
	FriøvelseID INT NOT NULL,
	Beskrivelse VARCHAR(500),
    ØvelseID INT NOT NULL,
	CONSTRAINT Friøvelse_pk PRIMARY KEY (FriøvelseID),
	CONSTRAINT Friøvelse_fk FOREIGN KEY (ØvelseID) REFERENCES Øvelse(ØvelseID)
		ON UPDATE CASCADE
		ON DELETE CASCADE
);

CREATE TABLE Gruppe(
	GruppeID INT NOT NULL,
	Gruppenavn VARCHAR(60),
	CONSTRAINT Gruppe_pk PRIMARY KEY (GruppeID)

);

CREATE TABLE Fastmontert(
	FastmontertID INT NOT NULL,
	ØvelseID INT NOT NULL,
	Kilo INT,
	Sett INT,
	ApparatID INT NOT NULL,
	CONSTRAINT Fastmontert_pk PRIMARY KEY (FastmontertID),
	CONSTRAINT Fastmontert_fk1 FOREIGN KEY (ApparatID) REFERENCES Apparat(ApparatID)
		ON UPDATE CASCADE
		ON DELETE CASCADE,
	CONSTRAINT Fastmontert_fk2 FOREIGN KEY (ØvelseID) REFERENCES Øvelse(ØvelseID)
		ON UPDATE CASCADE
		ON DELETE CASCADE
);

CREATE TABLE ØvelseGruppe(
	GruppeID INT NOT NULL,
    ØvelseID INT NOT NULL,
	CONSTRAINT ØvelseGruppe_fk1 FOREIGN KEY (GruppeID) REFERENCES Gruppe(GruppeID) 
																	   ON DELETE CASCADE
                                                                       ON UPDATE CASCADE,
	CONSTRAINT ØvelseGruppe_fk2 FOREIGN KEY (ØvelseID) REFERENCES Øvelse(ØvelseID)
																		ON DELETE CASCADE 
                                                                        ON UPDATE CASCADE
);