-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Anamakine: 127.0.0.1
-- Üretim Zamanı: 20 Eki 2021, 23:12:57
-- Sunucu sürümü: 10.4.20-MariaDB
-- PHP Sürümü: 8.0.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Veritabanı: `companyall`
--

DELIMITER $$
--
-- Yordamlar
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `ProcDeleteChosenImage` (IN `image_name` VARCHAR(255))  BEGIN
	DELETE FROM product_file_name WHERE product_file_name.file_name = image_name;

END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `address`
--

CREATE TABLE `address` (
  `id` int(11) NOT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `last_modified_by` varchar(255) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `detail` varchar(200) NOT NULL,
  `type` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Tablo döküm verisi `address`
--

INSERT INTO `address` (`id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `detail`, `type`) VALUES
(1, 'Ali Bilmem', '2021-10-20 23:40:40', 'Ali Bilmem', '2021-10-20 23:40:40', 'GOP BULVARI MAHMUTPAŞA MAH. KUŞAK APT. NO: 289', 'Home'),
(2, 'Ali Bilmem', '2021-10-20 23:41:28', 'Ali Bilmem', '2021-10-20 23:41:28', 'YTÜ  - Kampüs\n', 'Work'),
(3, 'Ali Bilmem', '2021-10-20 23:46:38', 'Ali Bilmem', '2021-10-20 23:46:38', 'İstanbul - Güngören\n', 'Home');

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `advertisement`
--

CREATE TABLE `advertisement` (
  `id` int(11) NOT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `last_modified_by` varchar(255) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `click` bigint(20) DEFAULT NULL,
  `finish_date` varchar(255) NOT NULL,
  `height` int(11) NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `link` varchar(255) NOT NULL,
  `name` varchar(50) NOT NULL,
  `no` varchar(255) NOT NULL,
  `start_date` varchar(255) NOT NULL,
  `status` varchar(255) NOT NULL,
  `view` int(11) NOT NULL,
  `width` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Tablo döküm verisi `advertisement`
--

INSERT INTO `advertisement` (`id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `click`, `finish_date`, `height`, `image`, `link`, `name`, `no`, `start_date`, `status`, `view`, `width`) VALUES
(1, NULL, NULL, 'Ali Bilmem', '2021-10-20 23:29:41', 0, '2021-10-24 12:00', 174, '', 'www.hepsiburada.com', 'Hepsiburada', '4761423359', '2021-10-20 12:00', 'Active', 1000, 580),
(2, 'Ali Bilmem', '2021-10-20 23:26:35', 'Ali Bilmem', '2021-10-20 23:26:35', 0, '2021-10-24 12:00', 174, '', 'www.hepsiburada.com', 'Hepsiburada2', '4761595172', '2021-10-20 12:00', 'Active', 1000, 580),
(3, 'Ali Bilmem', '2021-10-20 23:28:14', 'Ali Bilmem', '2021-10-20 23:28:14', 0, '2021-10-25 12:00', 174, '', 'www.hepsiburada.com', 'Hepsiburada3', '4761694863', '2021-10-22 12:00', 'Active', 1000, 580);

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `announcement`
--

CREATE TABLE `announcement` (
  `id` int(11) NOT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `last_modified_by` varchar(255) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `date` varchar(255) DEFAULT NULL,
  `detail` varchar(500) NOT NULL,
  `no` varchar(255) NOT NULL,
  `status` varchar(255) NOT NULL,
  `title` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Tablo döküm verisi `announcement`
--

INSERT INTO `announcement` (`id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `date`, `detail`, `no`, `status`, `title`) VALUES
(6, NULL, NULL, 'Ali Bilmem', '2021-10-20 23:55:15', '20-10-2021 23:54:47', 'asdgtserhbgfd\n', '4763287927', 'Active', 'Deneme-Güncelleme'),
(7, 'Ali Bilmem', '2021-10-20 23:55:02', 'Ali Bilmem', '2021-10-20 23:55:02', '20-10-2021 23:55:02', 'zdersfftgklmö\n', '4763302976', 'Active', 'deneme -3'),
(8, 'Ali Bilmem', '2021-10-20 23:56:21', 'Ali Bilmem', '2021-10-20 23:56:21', '20-10-2021 23:56:21', 'stzdejdytrhkftyuhj\n', '4763381715', 'Active', 'deneme');

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `city`
--

CREATE TABLE `city` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `country_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `contents`
--

CREATE TABLE `contents` (
  `id` int(11) NOT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `last_modified_by` varchar(255) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `date` varchar(255) DEFAULT NULL,
  `description` varchar(255) NOT NULL,
  `details` text NOT NULL,
  `no` varchar(255) DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Tablo döküm verisi `contents`
--

INSERT INTO `contents` (`id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `date`, `description`, `details`, `no`, `status`, `title`) VALUES
(1, 'Ali Bilmem', '2021-10-12 19:10:05', 'Ali Bilmem', '2021-10-12 19:10:05', '12-10-2021 19:10:05', 'Deneme Description - 1 ', 'Deneme - 1', '753608', 'Active', 'Deneme - 1'),
(2, 'Ali Bilmem', '2021-10-12 19:10:18', 'Ali Bilmem', '2021-10-12 19:10:18', '12-10-2021 19:10:18', 'Deneme Description - 2', 'Deneme - 2', '037713', 'Active', 'Deneme - 2'),
(3, 'Ali Bilmem', '2021-10-12 19:10:25', 'Ali Bilmem', '2021-10-12 19:10:25', '12-10-2021 19:10:25', 'Deneme Description - 3', 'Deneme - 3', '452384', 'Active', 'Deneme - 3'),
(4, 'Ali Bilmem', '2021-10-12 19:10:35', 'Ali Bilmem', '2021-10-12 19:10:35', '12-10-2021 19:10:35', 'Deneme Description - 4', 'Deneme - 4', '429471', 'Passive', 'Deneme - 4'),
(5, 'Ali Bilmem', '2021-10-12 19:10:43', 'Ali Bilmem', '2021-10-12 19:10:43', '12-10-2021 19:10:43', 'Deneme Description - 5', 'Deneme - 5', '551513', 'Passive', 'Deneme - 5'),
(6, 'Ali Bilmem', '2021-10-12 19:10:52', 'Ali Bilmem', '2021-10-12 19:10:52', '12-10-2021 19:10:52', 'Deneme Description - 6', 'Deneme - 6', '687385', 'Active', 'Deneme - 6'),
(8, 'Ali Bilmem', '2021-10-12 19:11:10', 'Ali Bilmem', '2021-10-12 19:11:10', '12-10-2021 19:11:10', 'Deneme Description - 8', 'Deneme - 8', '364339', 'Passive', 'Deneme - 8'),
(9, 'Ali Bilmem', '2021-10-12 19:11:17', 'Ali Bilmem', '2021-10-12 19:11:17', '12-10-2021 19:11:17', 'Deneme Description - 9', 'Deneme - 9', '916868', 'Active', 'Deneme - 9'),
(10, 'Ali Bilmem', '2021-10-12 19:11:25', 'Ali Bilmem', '2021-10-17 01:46:02', '12-10-2021 19:11:25', 'Deneme Description - 10', 'Deneme - 10', '750180', 'Active', 'Deneme - 10'),
(11, 'Ali Bilmem', '2021-10-12 19:10:52', 'Ali Bilmem', '2021-10-13 14:13:33', '13-10-2021 14:13:33', 'Deneme Description - 11', 'Deneme - 11  gfsdhjfg', '829328', 'Active', 'Deneme - 11'),
(12, 'Ali Bilmem', '2021-10-12 19:11:43', 'Ali Bilmem', '2021-10-12 19:11:43', '13-10-2021 19:11:43', 'Deneme Description - 12', 'Deneme - 12', '842038', 'Active', 'Deneme - 12'),
(15, 'Ali Bilmem', '2021-10-13 12:07:17', 'Ali Bilmem', '2021-10-13 12:07:17', '13-10-2021 12:07:16', 'Deneme Description - 13', 'Deneme - 13', '513445', 'Active', 'Deneme - 13'),
(23, 'Ali Bilmem', '2021-10-13 16:57:25', 'Ali Bilmem', '2021-10-13 16:57:25', '13-10-2021 16:57:25', 'İçerik Kısa Tanım', 'İçerik tanımlama detayları burada yazılacak. ', '536040', 'Passive', 'İçerik Başlığı'),
(24, 'Ali Bilmem', '2021-10-13 17:06:00', 'Ali Bilmem', '2021-10-13 17:06:00', '13-10-2021 17:06:00', 'Dolar 9 TL oldu.', '12.10.2021 tarihi itibariyle dolar 9 ₺ \'yi gördü ve rekor tazeledi. ', '017188', 'Active', 'Ekonomik Parametreler'),
(26, 'Ali Bilmem', '2021-10-13 18:38:43', 'Ali Bilmem', '2021-10-13 17:31:14', '13-10-2021 17:31:14', 'TOGG Yerlilik Oranı', 'Togg yerlilik oranı %80 \'e yaklaştı.', '262909', 'Active', 'TOGG'),
(27, NULL, NULL, 'Ali Bilmem', '2021-10-20 23:31:51', '20-10-2021 23:31:37', 'Elasticsearch', 'Elasticsearch ', '400007', 'Passive', 'Java YP Programı');

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `country`
--

CREATE TABLE `country` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `customer`
--

CREATE TABLE `customer` (
  `id` int(11) NOT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `last_modified_by` varchar(255) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `city` varchar(255) NOT NULL,
  `country` varchar(255) NOT NULL,
  `mail` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL,
  `no` varchar(255) NOT NULL,
  `phone1` varchar(255) NOT NULL,
  `phone2` varchar(255) DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  `surname` varchar(50) NOT NULL,
  `taxno` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Tablo döküm verisi `customer`
--

INSERT INTO `customer` (`id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `city`, `country`, `mail`, `name`, `no`, `phone1`, `phone2`, `status`, `surname`, `taxno`) VALUES
(2, 'Ali Bilmem', '2021-10-20 23:41:28', 'Ali Bilmem', '2021-10-20 23:48:11', 'HI', 'CA', 'berat@mail.com', 'Berat ', '4762488030', '5553217587', '', 'Active', 'Yılmaz', '55532175870');

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `customer_addresses`
--

CREATE TABLE `customer_addresses` (
  `customer_id` int(11) NOT NULL,
  `addresses_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Tablo döküm verisi `customer_addresses`
--

INSERT INTO `customer_addresses` (`customer_id`, `addresses_id`) VALUES
(2, 2),
(2, 3);

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `customer_trash`
--

CREATE TABLE `customer_trash` (
  `id` int(11) NOT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `last_modified_by` varchar(255) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `mail` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Tablo döküm verisi `customer_trash`
--

INSERT INTO `customer_trash` (`id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `mail`, `name`, `phone`, `surname`) VALUES
(1, 'Ali Bilmem', '2021-10-20 23:48:26', 'Ali Bilmem', '2021-10-20 23:48:26', 'eruyar123@gmail.com', 'EYÜP EYMEN', '5548398073', 'ERUYAR');

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `indent`
--

CREATE TABLE `indent` (
  `id` int(11) NOT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `last_modified_by` varchar(255) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `adress_index` int(11) NOT NULL,
  `date` varchar(255) NOT NULL,
  `no` varchar(255) NOT NULL,
  `order_status` bit(1) NOT NULL,
  `status` varchar(255) NOT NULL,
  `customer_id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `likes`
--

CREATE TABLE `likes` (
  `id` int(11) NOT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `last_modified_by` varchar(255) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `rating` int(11) DEFAULT NULL,
  `indent_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Tablo döküm verisi `likes`
--

INSERT INTO `likes` (`id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `rating`, `indent_id`) VALUES
(1, 'Ali Bilmem', '2021-10-19 02:22:21', 'Ali Bilmem', '2021-10-19 02:22:21', 5, 1),
(2, 'Ali Bilmem', '2021-10-19 02:23:16', 'Ali Bilmem', '2021-10-19 02:23:16', 5, 1),
(3, 'Ali Bilmem', '2021-10-19 02:23:20', 'Ali Bilmem', '2021-10-19 02:23:20', 4, 1),
(4, 'Ali Bilmem', '2021-10-19 02:23:25', 'Ali Bilmem', '2021-10-19 02:23:25', 3, 1),
(5, 'Ali Bilmem', '2021-10-19 02:23:27', 'Ali Bilmem', '2021-10-19 02:23:27', 5, 1),
(6, 'Ali Bilmem', '2021-10-19 03:02:26', 'Ali Bilmem', '2021-10-19 03:02:26', 2, 1),
(7, 'Ali Bilmem', '2021-10-19 19:40:54', 'Ali Bilmem', '2021-10-19 19:40:54', 5, 2);

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `likes_product`
--

CREATE TABLE `likes_product` (
  `id` int(11) NOT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `last_modified_by` varchar(255) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `total_like` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Tablo döküm verisi `likes_product`
--

INSERT INTO `likes_product` (`id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `total_like`, `product_id`) VALUES
(1, 'Ali Bilmem', '2021-10-19 02:22:21', 'Ali Bilmem', '2021-10-19 02:22:21', 5, 27),
(2, 'Ali Bilmem', '2021-10-19 02:23:16', 'Ali Bilmem', '2021-10-19 02:23:16', 5, 27),
(3, 'Ali Bilmem', '2021-10-19 02:23:20', 'Ali Bilmem', '2021-10-19 02:23:20', 4, 27),
(4, 'Ali Bilmem', '2021-10-19 02:23:25', 'Ali Bilmem', '2021-10-19 02:23:25', 3, 27),
(5, 'Ali Bilmem', '2021-10-19 02:23:27', 'Ali Bilmem', '2021-10-19 02:23:27', 5, 27),
(6, 'Ali Bilmem', '2021-10-19 03:02:26', 'Ali Bilmem', '2021-10-19 03:02:26', 2, 27),
(7, 'Ali Bilmem', '2021-10-19 19:40:54', 'Ali Bilmem', '2021-10-19 19:40:54', 5, 25);

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `product`
--

CREATE TABLE `product` (
  `id` int(11) NOT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `last_modified_by` varchar(255) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `campaig_name` varchar(255) DEFAULT NULL,
  `campaign_details` varchar(255) DEFAULT NULL,
  `campaign_status` varchar(255) NOT NULL,
  `date` varchar(255) DEFAULT NULL,
  `description` varchar(255) NOT NULL,
  `details` text NOT NULL,
  `name` varchar(255) NOT NULL,
  `no` varchar(255) DEFAULT NULL,
  `price` decimal(19,2) DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  `campaign_name` varchar(255) DEFAULT NULL,
  `total_like` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Tablo döküm verisi `product`
--

INSERT INTO `product` (`id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `campaig_name`, `campaign_details`, `campaign_status`, `date`, `description`, `details`, `name`, `no`, `price`, `status`, `campaign_name`, `total_like`) VALUES
(2, 'Ali Bilmem', '2021-10-14 23:18:33', 'Ali Bilmem', '2021-10-15 22:06:14', NULL, '', 'No', '14-10-2021 23:18:32', 'A7 RS', 'Vahşi ve yabani mi? Ya da harekete hazır ve estetik mi? RS 7 Sportback² ile her ikisini de elde edebilirsiniz. Coupé benzeri, metalik tango kırmızı şık tasarım çizgileri, V8 TFSI motorun² kaba kuvvetini karşılar. Yenilikçi teknoloji sezgisel dinamiklerle buluşuyor. Artık taviz vermek zorunda kalmamanın büyüsünü yaşayın.\nSportif tavan çizgisi ile Audi RS 7 Sportback², hangi ortamda bulunursa bulunsun hayranlık uyandıran bir görünüme sahiptir. Çarpıcı tasarım, bütünüyle herkesi etkilemede asla başarısız olmaz. Önde ve arkada geniş tekerlek kemerli karakteristik RS geniş gövdesi, kesintisiz LED ışık şeridi ve geniş Audi çerçevesiz Singleframe ızgarası, olağanüstü performansın gözle görünen ifadesidir.', 'Audi', '4242712900', '3978168.00', 'Available', NULL, 0),
(4, 'Ali Bilmem', '2021-10-14 23:43:44', 'Ali Bilmem', '2021-10-16 19:30:07', NULL, '', 'No', '14-10-2021 23:43:44', 'A6 RS', 'Genişletilmiş singleframe\'den, sportif krom detaylı yan hava menfezlerine ve dar formlu farlara kadar ilerici bir yapıya sahip olan A6 Sedan\'ın ön kısmı ilk bakışta harika bir etki yaratır. Tekerlek kemerlerinin üstündeki güçlü kavislere sahip keskinleştirilmiş omuz çizgisi, quattro mirasını vurgular. Dış aynalar, tornado çizgisi üzerinde sportif bir şekilde konumlandırılmıştır. Üstelik coupé benzeri akıcı tavan çizgisi, karakteristik alüminyum stiline sahip, zarif ve üç boyutlu olarak biçimlendirilen arka kısma ulaşır.\nOpsiyonel olarak sunulan dinamik gösterge lambalı HD Matrix LED farlar iki farklı açıdan etkileyicidir. Sürücüye, yaklaşan trafiği göz kamaştırmadan optimum yol aydınlatması sağlar ve karakteristik aydınlatma segmentlerinin dışa odaklanması sayesinde A6 Sedan\'ın etkin ışın genişliğini en üst seviyeye çıkarır.', 'Audi', '4244224483', '1871654.00', 'Available', '', 0),
(5, NULL, NULL, 'Ali Bilmem', '2021-10-16 19:37:19', NULL, '', 'No', '16-10-2021 19:35:45', ' A5 Coupe', ' Audi A5 Sportback ile farkınızı ortaya koyarsınız. Bu beş kapılı aile otomobilinin çarpıcı tasarımı, ona derinlik ve genişlik kazandırır. Buna ek olarak, isteğe bağlı Matrix LED farlar ve geniş bir bilgi-eğlence platformu gibi öncü teknolojiler sunar.\nGeniş hava girişleri ve krom giydirmeler bu sportif beş kapılı aracın ön kısmına keskin görünümünü kazandırır. Yanlarda, orta ağırlıkta kapı eşikleri aracın sportifliğine vurgu yaparken ve göze çarpan tekerlek kemerleri de isteğe bağlı sunulan 20 inç alaşım jantları vurgular. Difüzördeki mat alüminyum gümüş renkte yatay şerit ve ikizkenar yamuk şeklindeki egzoz boruları, arka kısmın genişliğini etkileyici bir şekilde ön plana çıkarır. Buna ek olarak; opsiyonel, Advanced ve S line donanım paketleri sayesinde Audi A5 Sportback\'i özelleştirmek de mümkündür.\nLED teknolojili farlar artık Audi A5 Sportback\'de standarttır. Audi lazer ışıklı Matrix LED farlar opsiyonel olarak mevcuttur. Audi lazer ışığı, yüksek araç hızlarında uzun LED farları tamamlar ve görüş mesafesini neredeyse iki katına çıkararak uzun LED fara göre gözle görülür derecede daha fazla emniyet ve rahatlık sağlar. coming home/leaving home (eve geliş/evden çıkış) özelliğinin dinamik ışık sıralaması ve dinamik göstergeler de bu aydınlatma paketinin birer parçasıdır.', 'Audi', '4244440654', '992305.00', 'Available', '', 0),
(6, 'Ali Bilmem', '2021-10-14 23:49:57', 'Ali Bilmem', '2021-10-16 19:25:55', NULL, ' ', 'No', '14-10-2021 23:49:57', ' A4 RS', ' Hassas bir şekilde keskinleştirilmiş ön görünüm ve karakteristik arka kısma sahip ilerici tasarımı sayesinde, Audi A4 Sedan\'ın sportif karakteri her zamankinden daha fazla etkileyici: Birinci sınıf kaliteye sahip ve işlevsel bir iç tasarım, dijitalleşme, bilgi-eğlence ve sürücü asistan sistemleri² alanlarındaki yenilikçi teknolojiler ve bir dizi güçlü motor paketi tamamlar.\nAudi A4 Sedan\'ın yandan görünümü, dikkatleri anında tekerleklere, geniş kanatlara ve çarpıcı marşpiyel çıtalarına çeker. Arka kısımda, LED lambalar kesintisiz bir krom şeritle bağlanır. Yamuk şeklindeki entegre egzoz borularına sahip kayık difüzör alanı, sportif bir görünüm sağlar. Derin ve geniş Singleframe çerçeve, tampon ve frapan geniş hava girişleri ön tarafta güçlü bir birliktelik oluşturur.', 'Audi', '4244597842', '882950.00', 'Available', ' ', 0),
(7, 'Ali Bilmem', '2021-10-14 23:51:24', 'Ali Bilmem', '2021-10-16 19:32:26', NULL, '', 'No', '14-10-2021 23:51:24', 'A3 RS ', 'Hızla değişen bir dünyada, zorluklara hazırlıklı bir arkadaşa ihtiyacınız var. İlerici tasarım ve yenilikçi teknolojiler ile İlerleme somut hale geldi. \nMetalik atol mavisi ve yeni Audi A3 Sportback edition one. Etkileyici çamurluk kaplamalarına, daha geniş bir iz genişliğine ve yan tarafta sürekli bir omuz çizgisine sahiptir. Bal peteği görünümüne sahip, yeni, genişletilmiş Audi Singleframe ızgara ile birlikte, Audi A3 Sportback her zaman olduğundan daha atletik görünmektedir.\nYeni progresif direksiyon ve opsiyonel Audi sürüş modu seçimi özelliği ile, yeni Audi A3 Sportback her türlü yola çıkmaya hazırdır. Audi sürüş modu seçimi özelliği ile, ister konforlu, ister dinamik, otomatik veya daha özelleştirilmiş tercih edin, A3 Sportback’in karakterini sürüş tarzınıza uyacak şekilde ayarlayabilirsiniz. Verimlilik modu yakıt tasarrufu yapmanıza yardımcı olabilir.', 'Audi', '4244684312', '650895.00', 'Available', '', 0),
(8, 'Ali Bilmem', '2021-10-14 23:53:19', 'Ali Bilmem', '2021-10-15 22:09:06', NULL, ' ', 'No', '14-10-2021 23:53:19', 'A8 L', 'Audi A8 L\'in, arkasında özellikle yüksek oturma konforu ile lüks ve ekstra geniş bir iç mekana sahip olacaksınız. Gelişmiş sürücü asistan sistemleri² ve ağ bağlantılı sistemler, günlük yaşamınızı gözle görülür derecede kolaylaştırır.\"\nOrta konsoldaki akustik ve dokunsal geri bildirime sahip iki dokunmatik ekran, bilgi-eğlence sisteminin ve ayrıca klima, konfor ve rahatlık işlevlerinin sezgisel çalışmasını sağlar. Gelişmiş ses kontrolü⁵ araçla etkileşimin bir diyalog şeklinde yürütülmesini sağlar. Audi connect⁷ sayesinde Amazon Alexa⁶ entegrasyonunu kullanabilirsiniz.\nAudi exclusive⁸ kişiselleştirme programı, kişisel tasarım fikirlerinizi biçimlendirir: İç mekan için çok çeşitli seçenekleri dilediğiniz biçimde birleştirin ve dış mekanın, \"ara\" mavisi, kristal görünümü, metalik Suzuka grisi, Goodwood yeşili, sedefli, ipanema kahverengi ve metalik gibi yansıtıcı renklerle göz alıcı olmasını sağlayın.', 'Audi', '4244799112', '3108155.00', 'Available', ' ', 0),
(15, NULL, NULL, 'Ali Bilmem', '2021-10-16 19:26:25', NULL, '', 'No', '15-10-2021 01:24:46', 'A3 Sportback', 'Hızla değişen bir dünyada, zorluklara hazırlıklı bir arkadaşa ihtiyacınız var. İlerici tasarım ve yenilikçi teknolojiler ile İlerleme somut hale geldi.\nMetalik atol mavisi ve yeni Audi A3 Sportback edition one. Etkileyici çamurluk kaplamalarına, daha geniş bir iz genişliğine ve yan tarafta sürekli bir omuz çizgisine sahiptir. Bal peteği görünümüne sahip, yeni, genişletilmiş Audi Singleframe ızgara ile birlikte, Audi A3 Sportback her zaman olduğundan daha atletik görünmektedir.\nYeni progresif direksiyon ve opsiyonel Audi sürüş modu seçimi özelliği ile, yeni Audi A3 Sportback her türlü yola çıkmaya hazırdır. Audi sürüş modu seçimi özelliği ile, ister konforlu, ister dinamik, otomatik veya daha özelleştirilmiş tercih edin, A3 Sportback’in karakterini sürüş tarzınıza uyacak şekilde ayarlayabilirsiniz. Verimlilik modu yakıt tasarrufu yapmanıza yardımcı olabilir.', 'Audi ', '4248391159', '522819.00', 'Available', '', 0),
(16, 'Ali Bilmem', '2021-10-15 01:25:58', 'Ali Bilmem', '2021-10-16 19:33:52', NULL, '', 'No', '15-10-2021 01:25:58', 'A5 Sportback', 'Geniş hava girişleri ve krom giydirmeler bu sportif beş kapılı aracın ön kısmına keskin görünümünü kazandırır. Yanlarda, orta ağırlıkta kapı eşikleri aracın sportifliğine vurgu yaparken ve göze çarpan tekerlek kemerleri de isteğe bağlı sunulan 20 inç alaşım jantları vurgular. Difüzördeki mat alüminyum gümüş renkte yatay şerit ve ikizkenar yamuk şeklindeki egzoz boruları, arka kısmın genişliğini etkileyici bir şekilde ön plana çıkarır. Buna ek olarak; opsiyonel, Advanced ve S line donanım paketleri sayesinde Audi A5 Sportback\'i özelleştirmek de mümkündür.', 'Audi', '4250358778', '750000.00', 'Available', '', 0),
(17, 'Ali Bilmem', '2021-10-15 01:26:49', 'Ali Bilmem', '2021-10-16 19:26:40', NULL, '', 'No', '15-10-2021 01:26:49', 'A4 Avant', 'A4 Avant şunları temsil ediyor: sportiflik, kalite, dijitalleşme ve çok yönlülük. Otomobilin özgür karakteri görsel olarak da güçlü bir şekilde ifade edilir. Yüksek kaliteli iç tasarımda, öncü teknolojiler dijital yaşamınızı araç içinde de sürdürmenizi sağlar. Ve geniş bagaj alanı, bir dizi esnek taşıma seçeneği sunar.', 'Audi', '4250409438', '988740.00', 'Available', '', 0),
(18, 'Ali Bilmem', '2021-10-15 01:28:32', 'Ali Bilmem', '2021-10-16 19:41:46', NULL, '', 'No', '15-10-2021 01:28:32', 'S400d ', 'Yeni Mercedes-Benz S-Serisi’nin net tasarımı önemli detaylara odaklanmıştır: Dış mekanda mükemmel orantılar ve iç mekanda zamana uygun lüks. Bu sayede Yeni S-Serisi’nin trend belirleyen yenilikleri, vazgeçmek istemeyeceğiniz eşsiz bir görünüme sahip olur.\nModern lüks ve birinci sınıf konfor Yeni S-Serisi’nde yeni bir seviyeye ulaşıyor. Gelecekte otomobil kullanmak ulaşımdan çok daha fazlasını ifade edecek. Yeni S-Serisi ideal inziva yeridir. Yenilikçi konfor özellikleri, sürücü koltuğundan arka bölüme kadar birinci sınıf bir sürüş deneyimi sağlar.', 'Mercedes-Benz', '4250512205', '4500000.00', 'Available', '', 0),
(19, 'Ali Bilmem', '2021-10-15 01:29:54', 'Ali Bilmem', '2021-10-16 19:42:03', NULL, '', 'No', '15-10-2021 01:29:54', 'C200 4-Matic', 'Yeni C-Serisi’nin, Edition 1 AMG adındaki ilk üretime özel paketinde kapsamlı bir donanım kombinasyonu sunuluyor. Maksimum ayrıcalık ve konfor için tasarlanan Yeni C-Serisi Edition 1 AMG, hayatınızı kolaylaştıracak özelliklerle donatıldı. Otomatik bagaj kapağı kapatma sistemi ve KEYLESS-GO, sürücü ve yolcular için maksimum konfor sunarken, 19 inç çok kollu jantlar ve AMG tasarımı gövde rengi bagaj üstü spoyleri sportif bileşenleri oluşturuyor. DIGITAL LIGHT ve Kör Nokta Yardımcısı ise yüksek güvenlik beklentilerini karşılıyor.\n', 'Mercedes-Benz', '4250594209', '898054.00', 'Available', '', 0),
(20, 'Ali Bilmem', '2021-10-15 23:32:07', 'Ali Bilmem', '2021-10-17 01:10:13', NULL, '', 'No', '15-10-2021 23:32:07', 'Deneme', 'deneme', 'Deneme', '4329927445', '1000.00', 'Unavailable', '', 0),
(23, 'Ali Bilmem', '2021-10-17 02:05:45', 'Ali Bilmem', '2021-10-17 02:17:34', NULL, '', 'No', '17-10-2021 02:05:45', 'Iphone 13 - 128 GB', 'Yepyeni bir mimari tasarladık. En büyük Geniş kamera sensörüne sahip olan en iyi çift kamera sistemimizi buraya sığdırmak için lensleri 45 derece döndürdük. Ayrıca sensör bazlı optik görüntü stabilizasyonu özelliğimize de yer açtık. Ve Ultra Geniş kameraya daha hızlı bir sensör ekledik.\n\nYeni Ultra Geniş kamera, fotoğraflarınızın karanlık alanlarında daha fazla ayrıntıyı ortaya çıkarıyor\n\nYeni Geniş kamera, daha iyi fotoğraflar ve videolar için %47 daha fazla ışık yakalıyor\n\nYeni sensör bazlı optik görüntü stabilizasyonu sayesinde siz sabit duramasanız bile çekimleriniz sabit oluyor\niOS 15 ile, FaceTime’daki görüşmenize devam ederken aynı anda filmleri, müzikleri veya ekranınızdaki herhangi bir şeyi paylaşabiliyorsunuz. Odağınızı kaybetmemek için o an yaptığınız işle ilgisi olmayan tüm bildirimleri filtreleyebiliyorsunuz. Ayrıca görsellerdeki metinlerle etkileşime geçerek hızlıca mail göndermek, arama yapmak, yol tarifi almak ve daha fazlasını yapmak mümkün hale geliyor.\n', 'Apple', '4425545368', '13499.00', 'Available', '', 0),
(24, 'Ali Bilmem', '2021-10-17 02:37:13', 'Ali Bilmem', '2021-10-17 02:37:32', NULL, '', 'No', '17-10-2021 02:37:13', 'Iphone 13 Pro Max - 128 GB', 'Pro kamera sistemimizde bugüne kadarki en büyük gelişme. Çok daha fazla ayrıntı yakalayan ileri seviye donanım. Yeni fotoğraf ve film çekimi teknikleri sunan süper akıllı yazılım. Ve tüm bunları mümkün kılan akılalmaz hızlı bir çip. Yepyeni bir çekim deneyimine hazır olun.\n\nMakro çekim özelliği şimdi iPhone’da.\nYeniden tasarlanan lensi ve güçlü otomatik netleme sistemi sayesinde yeni Ultra Geniş kamera süjenize 2 cm uzaktan odaklanabiliyor. Bu sayede en küçük ayrıntıların bile büyüleyici görünmesini sağlıyor. Bir yaprağı soyut sanata dönüştürün. Bir tırtılın tüylerini çekin. Bir çiy damlasını kocaman gösterin. Küçük şeylerin güzelliğini keşfedin.\n\nMakro video da çekmek isteyen?\nMakro fotoğraflar sadece başlangıç. Çünkü makro videolar da çekebiliyorsunuz. Üstelik ağır çekim veya hızlı çekim modlarında da. Büyülenmeye hazır olun.\n\n', 'Apple', '4427433539', '21600.00', 'Available', '', 0),
(25, 'Ali Bilmem', '2021-10-17 02:42:28', 'Ali Bilmem', '2021-10-19 19:40:54', NULL, '', 'No', '17-10-2021 02:42:28', 'Iphone 13 Pro - 128 GB', 'Pro kamera sistemimizde bugüne kadarki en büyük gelişme. Çok daha fazla ayrıntı yakalayan ileri seviye donanım. Yeni fotoğraf ve film çekimi teknikleri sunan süper akıllı yazılım. Ve tüm bunları mümkün kılan akılalmaz hızlı bir çip. Yepyeni bir çekim deneyimine hazır olun. Makro çekim özelliği şimdi iPhone’da. Yeniden tasarlanan lensi ve güçlü otomatik netleme sistemi sayesinde yeni Ultra Geniş kamera süjenize 2 cm uzaktan odaklanabiliyor. Bu sayede en küçük ayrıntıların bile büyüleyici görünmesini sağlıyor. Bir yaprağı soyut sanata dönüştürün. Bir tırtılın tüylerini çekin. Bir çiy damlasını kocaman gösterin. Küçük şeylerin güzelliğini keşfedin. Makro video da çekmek isteyen? Makro fotoğraflar sadece başlangıç. Çünkü makro videolar da çekebiliyorsunuz. Üstelik ağır çekim veya hızlı çekim modlarında da. Büyülenmeye hazır olun.', 'Apple', '4427748420', '18899.00', 'Available', '', 5),
(26, NULL, NULL, 'Ali Bilmem', '2021-10-18 22:46:50', NULL, '', 'No', '17-10-2021 02:55:55', 'Iphone 13 Mini - 128 GB', 'Yepyeni bir mimari tasarladık. En büyük Geniş kamera sensörüne sahip olan en iyi çift kamera sistemimizi buraya sığdırmak için lensleri 45 derece döndürdük. Ayrıca sensör bazlı optik görüntü stabilizasyonu özelliğimize de yer açtık. Ve Ultra Geniş kameraya daha hızlı bir sensör ekledik. Yeni Ultra Geniş kamera, fotoğraflarınızın karanlık alanlarında daha fazla ayrıntıyı ortaya çıkarıyor Yeni Geniş kamera, daha iyi fotoğraflar ve videolar için %47 daha fazla ışık yakalıyor Yeni sensör bazlı optik görüntü stabilizasyonu sayesinde siz sabit duramasanız bile çekimleriniz sabit oluyor iOS 15 ile, FaceTime’daki görüşmenize devam ederken aynı anda filmleri, müzikleri veya ekranınızdaki herhangi bir şeyi paylaşabiliyorsunuz. Odağınızı kaybetmemek için o an yaptığınız işle ilgisi olmayan tüm bildirimleri filtreleyebiliyorsunuz. Ayrıca görsellerdeki metinlerle etkileşime geçerek hızlıca mail göndermek, arama yapmak, yol tarifi almak ve daha fazlasını yapmak mümkün hale geliyor.', 'Apple', '4428109669', '11998.00', 'Unavailable', '', 0),
(27, 'Ali Bilmem', '2021-10-18 23:00:25', 'Ali Bilmem', '2021-10-19 03:02:26', NULL, '', 'No', '18-10-2021 23:00:24', 'Watch Series 7 Gps, 41 MM', 'Daha büyük ekran, Apple Watch deneyimini bambaşka bir boyuta taşıyor. Şimdi saatinizi kullanmanız ve\nekrandakileri okumanız çok daha kolay. Karşınızda Apple Watch Series 7. Bugüne kadarki en büyük ve\nen parlak fikrimiz.\nFizik kurallarını biraz esnettik. İşin zor kısmı, aygıtın boyutlarını neredeyse hiç büyütmeden daha büyük bir ekran yaratmaktı. Bunu yapabilmek için ekranı tamamen yeniden tasarladık ve kenarları %40 incelterek Series 6’ya göre yaklaşık %20 daha büyük bir ekran alanı elde ettik. Series 3’e göre ise ekran alanını %50’nin de üzerinde büyütmüş olduk. Nasıl? Büyük haber, değil mi?\nHep Açık Retina ekran. Daha da\nparlak bir fikir. Saatin kaç olduğunu\nveya kadranınızdaki diğer bilgileri ve komplikasyonları görmek için bileğinizi kaldırmanıza ya da ekrana dokunmanıza gerek yok. Çünkü ekran hep açık. Ve şimdi iç mekanlarda bileğiniz aşağıdayken %70 daha parlak.', 'Apple', '4587224873', '4399.00', 'Available', '', 4);

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `product_category`
--

CREATE TABLE `product_category` (
  `id` int(11) NOT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `last_modified_by` varchar(255) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `date` varchar(255) DEFAULT NULL,
  `details` text NOT NULL,
  `name` varchar(255) NOT NULL,
  `no` varchar(255) DEFAULT NULL,
  `status` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Tablo döküm verisi `product_category`
--

INSERT INTO `product_category` (`id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `date`, `details`, `name`, `no`, `status`) VALUES
(1, 'Ali Bilmem', '2021-10-13 23:51:33', 'Ali Bilmem', '2021-10-13 23:51:33', '13-10-2021 23:51:33', 'Electronic - Details', 'Electronic', '4158292940', 'Active'),
(2, 'Ali Bilmem', '2021-10-13 23:56:20', 'Ali Bilmem', '2021-10-13 23:56:20', '13-10-2021 23:56:20', 'Sport, Outdoor - Details', 'Sport, Outdoor', '4158580588', 'Active'),
(3, 'Ali Bilmem', '2021-10-14 00:05:36', 'Ali Bilmem', '2021-10-14 00:05:36', '14-10-2021 00:05:36', 'Automobile - Details', 'Automobile ', '4159136326', 'Active'),
(5, NULL, NULL, 'Ali Bilmem', '2021-10-14 02:56:00', '14-10-2021 02:56:00', 'Home, Life, Office - Details', 'Home, Life, Office', '4159407631', 'Passive'),
(6, NULL, NULL, 'Ali Bilmem', '2021-10-14 02:37:21', '14-10-2021 02:37:21', 'Cosmetic, Personal Care - Details', 'Cosmetic, Personal Care', '4159665733', 'Active'),
(7, 'Ali Bilmem', '2021-10-14 00:19:48', 'Ali Bilmem', '2021-10-14 00:19:48', '14-10-2021 00:19:48', 'Pet Shop - Details', 'Pet Shop', '4159988722', 'Active'),
(8, 'Ali Bilmem', '2021-10-14 00:23:21', 'Ali Bilmem', '2021-10-14 00:23:21', '14-10-2021 00:23:21', 'Fashion - Details', 'Fashion', '4160201671', 'Active'),
(9, 'Ali Bilmem', '2021-10-14 02:08:51', 'Ali Bilmem', '2021-10-14 02:08:51', '14-10-2021 02:08:51', 'Garden - Detail', 'Garden', '4166531418', 'Active'),
(10, 'Ali Bilmem', '2021-10-14 02:09:33', 'Ali Bilmem', '2021-10-14 02:09:33', '14-10-2021 02:09:33', 'Book - Detail', 'Book', '4166573244', 'Active'),
(11, 'Ali Bilmem', '2021-10-14 02:10:13', 'Ali Bilmem', '2021-10-14 02:10:13', '14-10-2021 02:10:13', 'Film - Detail', 'Film', '4166613697', 'Active'),
(12, 'Ali Bilmem', '2021-10-14 02:11:19', 'Ali Bilmem', '2021-10-14 02:11:19', '14-10-2021 02:11:19', 'Game - Detail', 'Game', '4166679709', 'Active'),
(16, 'Ali Bilmem', '2021-10-15 17:52:14', 'Ali Bilmem', '2021-10-15 17:56:16', '15-10-2021 17:52:14', 'Deneme - DEtails', 'Deneme', '4309534750', 'Passive');

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `product_file_name`
--

CREATE TABLE `product_file_name` (
  `product_id` int(11) NOT NULL,
  `file_name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Tablo döküm verisi `product_file_name`
--

INSERT INTO `product_file_name` (`product_id`, `file_name`) VALUES
(2, '52f2947c-84e6-4fb3-a7e5-5c25314f0f827.jpg'),
(2, 'aff4d2b6-be71-48a7-87eb-21545d8bc3172.jpg'),
(2, 'efd3a9a7-b3d0-4e25-9277-418185e72c3dt.jpg'),
(8, '0881b424-45ae-4a03-8354-7ed1f13bce5f6.jpg'),
(8, '3b509677-df20-4a62-962b-e80133fa3a11h.jpg'),
(8, '0ffa5e60-c836-4a59-9e36-cde378816f50y.jpg'),
(6, 'ce440174-e6c9-4e04-8bef-be747d582393.jfif'),
(6, '9c0845dc-4c24-43b5-9a71-e1f1c1f2f324.jfif'),
(6, 'c1459a80-a08e-450c-ae4b-dfb02e8943da.webp'),
(15, '6c58010d-42a0-4803-906e-386670a3a006r.jpg'),
(15, 'd6ac8dcf-5289-4105-bc64-ef94e6e26942n.jpg'),
(15, '4bd4fe10-36a4-478a-b9ac-4cb27f29be48k.jpg'),
(17, 'a0f5e932-7047-49a5-baf7-c8323c13ffa01.jpg'),
(17, 'fe8edda1-72c5-428e-bbe6-dcd17ff83f0f.jfif'),
(17, '18802e8c-766a-4c07-ae11-3fc81ced2b20.jfif'),
(4, '120468c5-5a7f-421a-b326-63ecfdcbfe658.jpg'),
(4, 'fa3f190e-8002-419b-bb55-b926097dee19d.jpg'),
(4, '55737984-19c5-4527-ac4d-1bb9a3fea08dd.jpg'),
(7, '619199bd-51a6-49a8-829b-39fae3e833d0k.jpg'),
(7, '3c7dca7c-5fb8-468a-b1f6-4195b98d46f1d.jpg'),
(7, '42c6c86d-70b9-441c-bfbc-ee1f540aa606d.jpg'),
(16, 'a531c2bd-c640-4000-b3c5-4f2e15723095i.jpg'),
(16, 'a89de74d-c157-41a2-b7e5-86341d01e0f2b.jpg'),
(16, '240cb2c5-1d4e-4245-b139-b2dca0840f703.jpg'),
(5, '4231fde4-6f49-4918-94a1-5fbec6bc8c30.jfif'),
(5, '967a22ee-7185-4d7a-967a-3bc8eb4f8614.jfif'),
(5, '1e307a91-7d7b-47f5-8f18-e085089af5cf.jfif'),
(18, 'd443c531-98d0-403e-b1d7-d43c965e2e066.jpg'),
(18, '72004883-a7f9-4cbb-aec7-fb599fe744793.jpg'),
(18, '86ac481f-bad0-4e6d-90cd-61e43b5d914d5.jpg'),
(19, '83f45013-8af9-46d8-95e2-39420b6447841.jpg'),
(19, '7ede6da4-7eed-4e0a-8797-9e183bf020f01.jpg'),
(19, '30a7c296-0ef5-46d1-af37-534f7cc95b5fi.jpg'),
(23, '6c2aab8d-1978-4912-a5ba-f555846608c6t.jpg'),
(23, '0148a9bd-ab4e-463c-bc7a-769437282b35t.jpg'),
(23, '532d1b3d-3155-4e18-99f9-d8c3a3eb57dft.jpg'),
(23, '91d4678d-a683-4d40-993b-82f0ffab6290t.jpg'),
(23, '1abd3d36-462c-4f21-963e-a438f05d4b68t.jpg'),
(24, '8617d413-d6be-4b8d-a399-57b01414008ct.jpg'),
(24, 'a5551ab4-e194-47fb-9ca2-9b1b6ac1d7e6t.jpg'),
(24, '33799ca7-6b9b-49cf-876a-42037a005141t.jpg'),
(24, 'a7b2dd2d-96a2-4a80-a55e-ad3f72fc464dt.jpg'),
(24, '5e11a1ac-4ae8-4dbb-b992-31a3f88bfedat.jpg'),
(25, '03afa8a8-5b83-4510-8ea4-64eb3d8303b8t.jpg'),
(25, '5bb16ddb-2b02-4944-81b5-7d53c1f6e807t.jpg'),
(25, 'b8818bbc-f01d-4199-b485-57242ca9c203t.jpg'),
(25, '2da655f4-58e9-45a2-9e11-324d29f5c5f5t.jpg'),
(25, 'f38a9c2a-e190-4675-9bf7-9ad2998cea34t.jpg'),
(26, '331b419d-a41d-44a4-8762-7495a05403d2t.jpg'),
(26, 'd83756d7-a91e-4cba-8415-35c1603e8abat.jpg'),
(26, '7b6d89ec-7cb5-4bda-a1cc-a990b0c70583t.jpg'),
(26, '1de61ee1-9744-4ebb-ada4-e591b30698e9t.jpg'),
(26, '2e2cfeac-d819-4cdc-b8ad-292c538a5aa4t.jpg'),
(27, '11b86ddc-7967-465d-9ad2-619340a8e0c9t.jpg'),
(27, '577ed005-088d-420a-bc7f-80050ca476d2t.jpg'),
(27, 'fe96234c-3e5d-4410-82dc-93440d941fc6t.jpg'),
(27, '7d581fcb-ccb9-40e7-8a15-96109faf95c9t.jpg'),
(27, 'b73ad356-8639-4e9d-ac76-c9bcec501d57t.jpg');

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `product_product_categories`
--

CREATE TABLE `product_product_categories` (
  `product_id` int(11) NOT NULL,
  `product_categories_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Tablo döküm verisi `product_product_categories`
--

INSERT INTO `product_product_categories` (`product_id`, `product_categories_id`) VALUES
(2, 3),
(4, 3),
(6, 3),
(7, 3),
(8, 3),
(15, 3),
(16, 3),
(17, 3),
(18, 3),
(19, 3),
(20, 2),
(5, 3),
(23, 1),
(24, 1),
(25, 1),
(26, 1),
(27, 1);

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `role`
--

CREATE TABLE `role` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Tablo döküm verisi `role`
--

INSERT INTO `role` (`id`, `name`) VALUES
(1, 'ROLE_ADMIN'),
(2, 'ROLE_CUSTOMER');

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `survey`
--

CREATE TABLE `survey` (
  `id` int(11) NOT NULL,
  `date` varchar(255) DEFAULT NULL,
  `detail` varchar(50) NOT NULL,
  `no` bigint(20) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `title` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `survey_option`
--

CREATE TABLE `survey_option` (
  `id` int(11) NOT NULL,
  `date` varchar(255) DEFAULT NULL,
  `no` bigint(20) DEFAULT NULL,
  `title` varchar(50) DEFAULT NULL,
  `vote` int(11) DEFAULT NULL,
  `survey_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `bio` varchar(255) DEFAULT NULL,
  `birthday` varchar(255) DEFAULT NULL,
  `company_address` varchar(200) NOT NULL,
  `company_name` varchar(100) NOT NULL,
  `company_phone` varchar(13) NOT NULL,
  `company_sector` varchar(255) DEFAULT NULL,
  `email` varchar(50) NOT NULL,
  `enabled` bit(1) NOT NULL,
  `name` varchar(50) NOT NULL,
  `no` bigint(20) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `profile_image` varchar(255) DEFAULT NULL,
  `surname` varchar(50) NOT NULL,
  `token_expired` bit(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Tablo döküm verisi `user`
--

INSERT INTO `user` (`id`, `bio`, `birthday`, `company_address`, `company_name`, `company_phone`, `company_sector`, `email`, `enabled`, `name`, `no`, `password`, `profile_image`, `surname`, `token_expired`) VALUES
(1, 'Deneme', '1997-08-25', 'GOP BULVARI MAHMUTPAŞA MAH. KUŞAK APT. NO: 289', 'Company All - Güncelleme', '05548398073', 'Software', 'eruyar123@gmail.com', b'1', 'Eymen', 4489090294, '$2a$10$bR.EcsO3lTq/mYztAl8qmO8a3FLS27z7GEtsFadEcZSqjxW3.Cz6G', 'defaultProfileImage.png', 'ERUYAR', b'1');

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `user_roles`
--

CREATE TABLE `user_roles` (
  `user_id` int(11) NOT NULL,
  `roles_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Tablo döküm verisi `user_roles`
--

INSERT INTO `user_roles` (`user_id`, `roles_id`) VALUES
(1, 1);

--
-- Dökümü yapılmış tablolar için indeksler
--

--
-- Tablo için indeksler `address`
--
ALTER TABLE `address`
  ADD PRIMARY KEY (`id`);

--
-- Tablo için indeksler `advertisement`
--
ALTER TABLE `advertisement`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_3r3r5nfx9py2pmpdcpvs6enom` (`name`),
  ADD UNIQUE KEY `UK_6y0lrbro8pl4gvmno2ql6bd74` (`no`);

--
-- Tablo için indeksler `announcement`
--
ALTER TABLE `announcement`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_4xch47dqv9dxhnxgwudp7s50u` (`no`),
  ADD UNIQUE KEY `UK_1u2ubk1rhxer3qi2my019helb` (`title`);

--
-- Tablo için indeksler `city`
--
ALTER TABLE `city`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_qsstlki7ni5ovaariyy9u8y79` (`name`),
  ADD KEY `FKrpd7j1p7yxr784adkx4pyepba` (`country_id`);

--
-- Tablo için indeksler `contents`
--
ALTER TABLE `contents`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_en0utx3suqotcyyt9okr1mje2` (`title`),
  ADD UNIQUE KEY `UK_37kb7e6x43n8m7xu4yfsnfix6` (`no`);

--
-- Tablo için indeksler `country`
--
ALTER TABLE `country`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_llidyp77h6xkeokpbmoy710d4` (`name`);

--
-- Tablo için indeksler `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_9bs0cm53439brbopbpxhg68e` (`mail`),
  ADD UNIQUE KEY `UK_6fs0k5vchx2ybw7ut4ryn7syj` (`no`),
  ADD UNIQUE KEY `UK_8oy1ba5w2w0ler23y5f2920el` (`phone1`),
  ADD UNIQUE KEY `UK_nrkb4ot9aela9u4jfucelgsfq` (`taxno`);

--
-- Tablo için indeksler `customer_addresses`
--
ALTER TABLE `customer_addresses`
  ADD UNIQUE KEY `UK_p421kx7b3ifw5w8mfkt6t40gy` (`addresses_id`),
  ADD KEY `FKt5007akl8ydn1dskefawemfw0` (`customer_id`);

--
-- Tablo için indeksler `customer_trash`
--
ALTER TABLE `customer_trash`
  ADD PRIMARY KEY (`id`);

--
-- Tablo için indeksler `indent`
--
ALTER TABLE `indent`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_afklfiigb30j6sac586xrn3ks` (`no`),
  ADD KEY `FK51oarrtn6me3wgperfe8jxj85` (`customer_id`),
  ADD KEY `FK6nne10pvjsej05hqh06f13kjn` (`product_id`);

--
-- Tablo için indeksler `likes`
--
ALTER TABLE `likes`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKnb0ln9f72axbi3xvsp0wabew4` (`indent_id`);

--
-- Tablo için indeksler `likes_product`
--
ALTER TABLE `likes_product`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK5bf06t1rfu0e2vqy01xrwc8jt` (`product_id`);

--
-- Tablo için indeksler `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_on9cwd9r8tem0iaekvy4as0u8` (`no`);

--
-- Tablo için indeksler `product_category`
--
ALTER TABLE `product_category`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_9qvug0bmpkmxkkx33q51m7do7` (`name`),
  ADD UNIQUE KEY `UK_pkdx3s47cck5bspi2iwt3rsna` (`no`);

--
-- Tablo için indeksler `product_file_name`
--
ALTER TABLE `product_file_name`
  ADD KEY `FKekepvata0k6ydvkwr7nl8le9o` (`product_id`);

--
-- Tablo için indeksler `product_product_categories`
--
ALTER TABLE `product_product_categories`
  ADD KEY `FKgvcomjjojaj7m0a8av3s2j2x7` (`product_categories_id`),
  ADD KEY `FKhhkr6xlu6jv4yk1ne1hsoccoh` (`product_id`);

--
-- Tablo için indeksler `role`
--
ALTER TABLE `role`
  ADD PRIMARY KEY (`id`);

--
-- Tablo için indeksler `survey`
--
ALTER TABLE `survey`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_srmtb7blo2xhnc7f34asxrqyq` (`title`),
  ADD UNIQUE KEY `UK_4p0qxw868niy299c7j9bv6jtc` (`no`);

--
-- Tablo için indeksler `survey_option`
--
ALTER TABLE `survey_option`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_kgded0cux6xjra4oe0tlpevh5` (`no`),
  ADD KEY `FK72e52dfgmv103prea5jt4tcfe` (`survey_id`);

--
-- Tablo için indeksler `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_5ocerldbcl3wc9rlopwfbu2a5` (`company_name`),
  ADD UNIQUE KEY `UK_lv1bisnundbmp75qxw9npm54q` (`company_phone`),
  ADD UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpe` (`email`),
  ADD UNIQUE KEY `UK_3dgsecoiycrv5hwxsbpf2850g` (`no`);

--
-- Tablo için indeksler `user_roles`
--
ALTER TABLE `user_roles`
  ADD KEY `FKj9553ass9uctjrmh0gkqsmv0d` (`roles_id`),
  ADD KEY `FK55itppkw3i07do3h7qoclqd4k` (`user_id`);

--
-- Dökümü yapılmış tablolar için AUTO_INCREMENT değeri
--

--
-- Tablo için AUTO_INCREMENT değeri `address`
--
ALTER TABLE `address`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Tablo için AUTO_INCREMENT değeri `advertisement`
--
ALTER TABLE `advertisement`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Tablo için AUTO_INCREMENT değeri `announcement`
--
ALTER TABLE `announcement`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Tablo için AUTO_INCREMENT değeri `city`
--
ALTER TABLE `city`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Tablo için AUTO_INCREMENT değeri `contents`
--
ALTER TABLE `contents`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- Tablo için AUTO_INCREMENT değeri `country`
--
ALTER TABLE `country`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Tablo için AUTO_INCREMENT değeri `customer`
--
ALTER TABLE `customer`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Tablo için AUTO_INCREMENT değeri `customer_trash`
--
ALTER TABLE `customer_trash`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Tablo için AUTO_INCREMENT değeri `indent`
--
ALTER TABLE `indent`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Tablo için AUTO_INCREMENT değeri `likes`
--
ALTER TABLE `likes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Tablo için AUTO_INCREMENT değeri `likes_product`
--
ALTER TABLE `likes_product`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Tablo için AUTO_INCREMENT değeri `product`
--
ALTER TABLE `product`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- Tablo için AUTO_INCREMENT değeri `product_category`
--
ALTER TABLE `product_category`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- Tablo için AUTO_INCREMENT değeri `role`
--
ALTER TABLE `role`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Tablo için AUTO_INCREMENT değeri `survey`
--
ALTER TABLE `survey`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Tablo için AUTO_INCREMENT değeri `survey_option`
--
ALTER TABLE `survey_option`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Tablo için AUTO_INCREMENT değeri `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Dökümü yapılmış tablolar için kısıtlamalar
--

--
-- Tablo kısıtlamaları `city`
--
ALTER TABLE `city`
  ADD CONSTRAINT `FKrpd7j1p7yxr784adkx4pyepba` FOREIGN KEY (`country_id`) REFERENCES `country` (`id`);

--
-- Tablo kısıtlamaları `customer_addresses`
--
ALTER TABLE `customer_addresses`
  ADD CONSTRAINT `FK8s97nbr10j06pjiupyk4ehhom` FOREIGN KEY (`addresses_id`) REFERENCES `address` (`id`),
  ADD CONSTRAINT `FKt5007akl8ydn1dskefawemfw0` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`);

--
-- Tablo kısıtlamaları `indent`
--
ALTER TABLE `indent`
  ADD CONSTRAINT `FK51oarrtn6me3wgperfe8jxj85` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`),
  ADD CONSTRAINT `FK6nne10pvjsej05hqh06f13kjn` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`);

--
-- Tablo kısıtlamaları `likes`
--
ALTER TABLE `likes`
  ADD CONSTRAINT `FKnb0ln9f72axbi3xvsp0wabew4` FOREIGN KEY (`indent_id`) REFERENCES `indent` (`id`);

--
-- Tablo kısıtlamaları `likes_product`
--
ALTER TABLE `likes_product`
  ADD CONSTRAINT `FK5bf06t1rfu0e2vqy01xrwc8jt` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`);

--
-- Tablo kısıtlamaları `product_file_name`
--
ALTER TABLE `product_file_name`
  ADD CONSTRAINT `FKekepvata0k6ydvkwr7nl8le9o` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`);

--
-- Tablo kısıtlamaları `product_product_categories`
--
ALTER TABLE `product_product_categories`
  ADD CONSTRAINT `FKgvcomjjojaj7m0a8av3s2j2x7` FOREIGN KEY (`product_categories_id`) REFERENCES `product_category` (`id`),
  ADD CONSTRAINT `FKhhkr6xlu6jv4yk1ne1hsoccoh` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`);

--
-- Tablo kısıtlamaları `survey_option`
--
ALTER TABLE `survey_option`
  ADD CONSTRAINT `FK72e52dfgmv103prea5jt4tcfe` FOREIGN KEY (`survey_id`) REFERENCES `survey` (`id`);

--
-- Tablo kısıtlamaları `user_roles`
--
ALTER TABLE `user_roles`
  ADD CONSTRAINT `FK55itppkw3i07do3h7qoclqd4k` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `FKj9553ass9uctjrmh0gkqsmv0d` FOREIGN KEY (`roles_id`) REFERENCES `role` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
