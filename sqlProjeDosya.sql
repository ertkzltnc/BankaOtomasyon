-- phpMyAdmin SQL Dump
-- version 4.6.4
-- https://www.phpmyadmin.net/
--
-- Anamakine: 127.0.0.1
-- Üretim Zamanı: 14 Haz 2017, 20:10:39
-- Sunucu sürümü: 5.7.14
-- PHP Sürümü: 5.6.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Veritabanı: `odev`
--

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `hesaplar`
--

CREATE TABLE `hesaplar` (
  `hesap_id` int(11) NOT NULL,
  `musteri_id` int(11) NOT NULL,
  `hesap_tipi` int(11) NOT NULL,
  `bakiye` double NOT NULL,
  `kayit_tarihi` text COLLATE utf8_turkish_ci NOT NULL,
  `hesap_durumu` int(11) NOT NULL,
  `borc` int(11) NOT NULL,
  `hesap_no` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;

--
-- Tablo döküm verisi `hesaplar`
--

INSERT INTO `hesaplar` (`hesap_id`, `musteri_id`, `hesap_tipi`, `bakiye`, `kayit_tarihi`, `hesap_durumu`, `borc`, `hesap_no`) VALUES
(6, 1, 3, -5000, '2017/06/14 15:15:33', 1, 0, 2664),
(7, 2, 1, 637, '2017/06/14 15:16:11', 1, 0, 1999),
(8, 3, 2, 200, '2017/06/14 15:16:31', 1, 0, 2393),
(9, 4, 4, 10, '2017/06/14 15:16:59', 1, 0, 3599),
(13, 8, 2, 56, '2017/06/14 22:59:15', 1, 0, 2454);

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `hesap_islemleri`
--

CREATE TABLE `hesap_islemleri` (
  `islem_id` int(11) NOT NULL,
  `hesap_no` int(11) NOT NULL,
  `yapilan_islem` text COLLATE utf8_turkish_ci NOT NULL,
  `islem_tarihi` text COLLATE utf8_turkish_ci NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;

--
-- Tablo döküm verisi `hesap_islemleri`
--

INSERT INTO `hesap_islemleri` (`islem_id`, `hesap_no`, `yapilan_islem`, `islem_tarihi`) VALUES
(1, 2393, '200 TL yatirildi.', '2017/06/14 15:17:33'),
(2, 2664, '55000 TL yatirildi.', '2017/06/14 15:17:55'),
(3, 1999, '450 TL cekildi.', '2017/06/14 15:18:08'),
(4, 3599, '10 TL yatirildi.', '2017/06/14 15:18:25'),
(5, 1999, '1000 TL yatirildi.', '2017/06/14 15:38:35'),
(6, 2664, '60000 TL cekildi.', '2017/06/14 22:33:11'),
(7, 2454, '56 TL yatirildi.', '2017/06/14 22:59:26'),
(8, 1999, '12 TL yatirildi.', '2017/06/14 23:06:56'),
(9, 1999, '78 TL yatirildi.', '2017/06/14 23:07:00'),
(10, 1999, '24 TL cekildi.', '2017/06/14 23:07:03'),
(11, 1999, '47 TL yatirildi.', '2017/06/14 23:07:10'),
(12, 1999, '26 TL cekildi.', '2017/06/14 23:07:14');

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `musteriler`
--

CREATE TABLE `musteriler` (
  `musteri_id` int(11) NOT NULL,
  `musteri_adi` varchar(255) COLLATE utf8_turkish_ci NOT NULL,
  `musteri_soyadi` varchar(255) COLLATE utf8_turkish_ci NOT NULL,
  `musteri_tc` text COLLATE utf8_turkish_ci NOT NULL,
  `hesap_no` int(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;

--
-- Tablo döküm verisi `musteriler`
--

INSERT INTO `musteriler` (`musteri_id`, `musteri_adi`, `musteri_soyadi`, `musteri_tc`, `hesap_no`) VALUES
(1, 'Erdinc', 'Kiziltunc', '13643687213', 2664),
(2, 'Engin', 'Kiziltunc', '22636830516', 1999),
(3, 'Ertugrul', 'Kiziltunc', '22627830808', 2393),
(4, 'Enes', 'Kiziltunc', '22624830962', 3599),
(5, 'Ayse', 'kiziltunc', '17132273646', 2194);

--
-- Dökümü yapılmış tablolar için indeksler
--

--
-- Tablo için indeksler `hesaplar`
--
ALTER TABLE `hesaplar`
  ADD PRIMARY KEY (`hesap_id`);

--
-- Tablo için indeksler `hesap_islemleri`
--
ALTER TABLE `hesap_islemleri`
  ADD PRIMARY KEY (`islem_id`);

--
-- Tablo için indeksler `musteriler`
--
ALTER TABLE `musteriler`
  ADD PRIMARY KEY (`musteri_id`);

--
-- Dökümü yapılmış tablolar için AUTO_INCREMENT değeri
--

--
-- Tablo için AUTO_INCREMENT değeri `hesaplar`
--
ALTER TABLE `hesaplar`
  MODIFY `hesap_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;
--
-- Tablo için AUTO_INCREMENT değeri `hesap_islemleri`
--
ALTER TABLE `hesap_islemleri`
  MODIFY `islem_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;
--
-- Tablo için AUTO_INCREMENT değeri `musteriler`
--
ALTER TABLE `musteriler`
  MODIFY `musteri_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
