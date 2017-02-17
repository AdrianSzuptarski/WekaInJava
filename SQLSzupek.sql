CREATE TABLE R.daty
(
    id_data integer NOT NULL,
    dzien varchar(45),
    miesiac varchar(45),
    kwartal integer,
    rok integer,
    (id_data)
);


CREATE TABLE KSIEGARNIE
(
    id_KSIEGARNIE integer NOT NULL,
    region varchar(45),
    miasto varchar(45),
    ulica varchar(45),
    numer varchar(45),
    PRIMARY KEY (id_KSIEGARNIE)
);

CREATE TABLE klienci
(
    id_klient integer NOT NULL,
    imie varchar(45),
    nazwisko varchar(45),
    miasto varchar(45),
    PRIMARY KEY (id_klient)
);

CREATE TABLE ksiazki
(
    id_ksiazka integer NOT NULL,
    wydawnictwo varchar,
    autor varchar,
    tytul varchar,
    rokWydania integer,
    cena double precision,
    gatunek varchar,
    PRIMARY KEY (id_ksiazka)
);

CREATE TABLE zakupy
(
    id_trans integer NOT NULL,
    id_klient integer,
    id_KSIEGARNIE integer,
    id_ksiazka integer,
    id_data integer,
    koszt double precision,
    PRIMARY KEY (id_trans),
);
