create database hospital_management_system;
use hospital_management_system;
drop database hospital_management_system;

create table kullanicilar(
	id int auto_increment primary key,
    tc_kimlik char(11) not null unique,
    ad varchar(100) not null,
    soyad varchar(100) not null,
    dogum_tarihi date not null,
    cinsiyet char(1) not null CHECK (cinsiyet IN ('E', 'K')),
    telefon char(17) not null,
    email varchar(100),
    adres varchar(100) not null,
    sifre varchar(100) not null,
    hatali_giris int,
    ekle_tarih date,
    ekle_kullanici_id int,
    guncelle_tarihi date,
    guncelle_kullanici_id int,
    
    FOREIGN KEY (ekle_kullanici_id) REFERENCES kullanicilar(id),
    FOREIGN KEY (guncelle_kullanici_id) REFERENCES kullanicilar(id)
);
create table bolumler(
	id int auto_increment primary key,
    bolum_adi varchar(100),
	ekle_tarih date,
    ekle_kullanici_id int,
    guncelle_tarihi date,
    guncelle_kullanici_id int,
    
    FOREIGN KEY (ekle_kullanici_id) REFERENCES kullanicilar(id),
    FOREIGN KEY (guncelle_kullanici_id) REFERENCES kullanicilar(id)
);

create table doktorlar(
	id int auto_increment primary key,
    tc_kimlik char(11) not null unique,
    ad varchar(100) not null,
    soyad varchar(100) not null,
    dogum_tarihi date not null,
    cinsiyet char(1) not null CHECK (cinsiyet IN ('E', 'K')),
    telefon char(17) not null,
    email varchar(100) not null,
    adres varchar(100) not null,
    bolum_id int,
	ekle_tarih date,
    ekle_kullanici_id int,
    guncelle_tarihi date,
    guncelle_kullanici_id int,
    
    FOREIGN KEY (ekle_kullanici_id) REFERENCES kullanicilar(id),
    FOREIGN KEY (guncelle_kullanici_id) REFERENCES kullanicilar(id),
    FOREIGN KEY (bolum_id) REFERENCES bolumler(id)
);

create table hastalar(
	id int auto_increment primary key,
    tc_kimlik char(11) not null unique,
    ad varchar(100) not null,
    soyad varchar(100) not null,
    dogum_tarihi date not null,
    cinsiyet char(1) not null CHECK (cinsiyet IN ('E', 'K')),
    telefon char(17) not null,
    email varchar(100) not null,
    adres varchar(100) not null,
    ekle_tarih date,
    ekle_kullanici_id int,
    guncelle_tarihi date,
    guncelle_kullanici_id int,
    
    FOREIGN KEY (ekle_kullanici_id) REFERENCES kullanicilar(id),
    FOREIGN KEY (guncelle_kullanici_id) REFERENCES kullanicilar(id)
);

create table randevu_saatleri(
	id int auto_increment primary key,
    sira int not null,
    saat varchar(5) not null
);

create table randevular(
	id int auto_increment primary key,
    bolum_id int,
    doktor_id int,
    hasta_id int,
    tarih date,
    saat_id int,
    durum CHAR(1) CHECK (durum IN ('A', 'I')),
    ekle_tarih date,
    ekle_kullanici_id int,
    guncelle_tarihi date,
    guncelle_kullanici_id int,
    
    FOREIGN KEY (bolum_id) REFERENCES bolumler(id),
    FOREIGN KEY (doktor_id) REFERENCES doktorlar(id),
    FOREIGN KEY (hasta_id) REFERENCES hastalar(id),
    FOREIGN KEY (saat_id) REFERENCES randevu_saatleri(id),
    FOREIGN KEY (ekle_kullanici_id) REFERENCES kullanicilar(id),
    FOREIGN KEY (guncelle_kullanici_id) REFERENCES kullanicilar(id)
);

CREATE VIEW doktor_bolumleri AS
SELECT 
    d.id AS doktor_id,
    d.tc_kimlik,
    d.ad,
    d.soyad,
    d.dogum_tarihi,
    d.cinsiyet,
    d.telefon,
    d.email,
    d.adres,
    b.bolum_adi
FROM doktorlar d
JOIN bolumler b ON d.bolum_id = b.id;

drop view v_randevular;

CREATE VIEW v_randevular as
SELECT
	r.bolum_id,
    b.bolum_adi,
    r.doktor_id,
    d.ad+' '+d.soyad as doktor_adsoyad,
    r.hasta_id,
    h.ad+' '+h.soyad as hasta_adsoyad,
    r.tarih,
    r.saat_id
FROM randevular r
JOIN bolumler b on b.id = r.bolum_id
JOIN doktorlar d on d.id = r.doktor_id
JOIN hastalar h on h.id = r.hasta_id;
    

CREATE VIEW v_randevular AS
SELECT
    r.bolum_id,
    b.bolum_adi,
    r.doktor_id,
    CONCAT(d.ad, ' ', d.soyad) AS doktor_adsoyad,
    r.hasta_id,
    CONCAT(h.ad, ' ', h.soyad) AS hasta_adsoyad,
    r.tarih,
    r.saat_id
FROM randevular r
JOIN bolumler b ON b.id = r.bolum_id
JOIN doktorlar d ON d.id = r.doktor_id
JOIN hastalar h ON h.id = r.hasta_id;
