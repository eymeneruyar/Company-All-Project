-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Anamakine: 127.0.0.1
-- Üretim Zamanı: 16 Eki 2021, 13:59:41
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
(1, 'Ali Bilmem', '2021-10-12 19:10:05', 'Ali Bilmem', '2021-10-12 19:10:05', '12-10-2021 19:10:05', 'Deneme Description - 1 ', 'Deneme - 1', '753608', 'active', 'Deneme - 1'),
(2, 'Ali Bilmem', '2021-10-12 19:10:18', 'Ali Bilmem', '2021-10-12 19:10:18', '12-10-2021 19:10:18', 'Deneme Description - 2', 'Deneme - 2', '037713', 'active', 'Deneme - 2'),
(3, 'Ali Bilmem', '2021-10-12 19:10:25', 'Ali Bilmem', '2021-10-12 19:10:25', '12-10-2021 19:10:25', 'Deneme Description - 3', 'Deneme - 3', '452384', 'active', 'Deneme - 3'),
(4, 'Ali Bilmem', '2021-10-12 19:10:35', 'Ali Bilmem', '2021-10-12 19:10:35', '12-10-2021 19:10:35', 'Deneme Description - 4', 'Deneme - 4', '429471', 'passive', 'Deneme - 4'),
(5, 'Ali Bilmem', '2021-10-12 19:10:43', 'Ali Bilmem', '2021-10-12 19:10:43', '12-10-2021 19:10:43', 'Deneme Description - 5', 'Deneme - 5', '551513', 'passive', 'Deneme - 5'),
(6, 'Ali Bilmem', '2021-10-12 19:10:52', 'Ali Bilmem', '2021-10-12 19:10:52', '12-10-2021 19:10:52', 'Deneme Description - 6', 'Deneme - 6', '687385', 'active', 'Deneme - 6'),
(8, 'Ali Bilmem', '2021-10-12 19:11:10', 'Ali Bilmem', '2021-10-12 19:11:10', '12-10-2021 19:11:10', 'Deneme Description - 8', 'Deneme - 8', '364339', 'passive', 'Deneme - 8'),
(9, 'Ali Bilmem', '2021-10-12 19:11:17', 'Ali Bilmem', '2021-10-12 19:11:17', '12-10-2021 19:11:17', 'Deneme Description - 9', 'Deneme - 9', '916868', 'active', 'Deneme - 9'),
(10, 'Ali Bilmem', '2021-10-12 19:11:25', 'Ali Bilmem', '2021-10-12 19:11:25', '12-10-2021 19:11:25', 'Deneme Description - 10', 'Deneme - 10', '750180', 'active', 'Deneme - 10'),
(11, 'Ali Bilmem', '2021-10-12 19:10:52', 'Ali Bilmem', '2021-10-13 14:13:33', '13-10-2021 14:13:33', 'Deneme Description - 11', 'Deneme - 11  gfsdhjfg', '829328', 'active', 'Deneme - 11'),
(12, 'Ali Bilmem', '2021-10-12 19:11:43', 'Ali Bilmem', '2021-10-12 19:11:43', '13-10-2021 19:11:43', 'Deneme Description - 12', 'Deneme - 12', '842038', 'active', 'Deneme - 12'),
(15, 'Ali Bilmem', '2021-10-13 12:07:17', 'Ali Bilmem', '2021-10-13 12:07:17', '13-10-2021 12:07:16', 'Deneme Description - 13', 'Deneme - 13', '513445', 'active', 'Deneme - 13'),
(23, 'Ali Bilmem', '2021-10-13 16:57:25', 'Ali Bilmem', '2021-10-13 16:57:25', '13-10-2021 16:57:25', 'İçerik Kısa Tanım', 'İçerik tanımlama detayları burada yazılacak. ', '536040', 'passive', 'İçerik Başlığı'),
(24, 'Ali Bilmem', '2021-10-13 17:06:00', 'Ali Bilmem', '2021-10-13 17:06:00', '13-10-2021 17:06:00', 'Dolar 9 TL oldu.', '12.10.2021 tarihi itibariyle dolar 9 ₺ \'yi gördü ve rekor tazeledi. ', '017188', 'active', 'Ekonomik Parametreler'),
(26, 'Ali Bilmem', '2021-10-13 18:38:43', 'Ali Bilmem', '2021-10-13 17:31:14', '13-10-2021 17:31:14', 'TOGG Yerlilik Oranı', 'Togg yerlilik oranı %80 \'e yaklaştı.', '262909', 'active', 'TOGG');

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
  `campaign_name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Tablo döküm verisi `product`
--

INSERT INTO `product` (`id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `campaig_name`, `campaign_details`, `campaign_status`, `date`, `description`, `details`, `name`, `no`, `price`, `status`, `campaign_name`) VALUES
(2, 'Ali Bilmem', '2021-10-14 23:18:33', 'Ali Bilmem', '2021-10-15 22:06:14', NULL, '', 'No', '14-10-2021 23:18:32', 'A7 RS', 'Vahşi ve yabani mi? Ya da harekete hazır ve estetik mi? RS 7 Sportback² ile her ikisini de elde edebilirsiniz. Coupé benzeri, metalik tango kırmızı şık tasarım çizgileri, V8 TFSI motorun² kaba kuvvetini karşılar. Yenilikçi teknoloji sezgisel dinamiklerle buluşuyor. Artık taviz vermek zorunda kalmamanın büyüsünü yaşayın.\nSportif tavan çizgisi ile Audi RS 7 Sportback², hangi ortamda bulunursa bulunsun hayranlık uyandıran bir görünüme sahiptir. Çarpıcı tasarım, bütünüyle herkesi etkilemede asla başarısız olmaz. Önde ve arkada geniş tekerlek kemerli karakteristik RS geniş gövdesi, kesintisiz LED ışık şeridi ve geniş Audi çerçevesiz Singleframe ızgarası, olağanüstü performansın gözle görünen ifadesidir.', 'Audi', '4242712900', '3978168.00', 'Available', NULL),
(4, 'Ali Bilmem', '2021-10-14 23:43:44', 'Ali Bilmem', '2021-10-14 23:43:44', NULL, '', 'No', '14-10-2021 23:43:44', 'A6 RS', 'Genişletilmiş singleframe\'den, sportif krom detaylı yan hava menfezlerine ve dar formlu farlara kadar ilerici bir yapıya sahip olan A6 Sedan\'ın ön kısmı ilk bakışta harika bir etki yaratır. Tekerlek kemerlerinin üstündeki güçlü kavislere sahip keskinleştirilmiş omuz çizgisi, quattro mirasını vurgular. Dış aynalar, tornado çizgisi üzerinde sportif bir şekilde konumlandırılmıştır. Üstelik coupé benzeri akıcı tavan çizgisi, karakteristik alüminyum stiline sahip, zarif ve üç boyutlu olarak biçimlendirilen arka kısma ulaşır.\nOpsiyonel olarak sunulan dinamik gösterge lambalı HD Matrix LED farlar iki farklı açıdan etkileyicidir. Sürücüye, yaklaşan trafiği göz kamaştırmadan optimum yol aydınlatması sağlar ve karakteristik aydınlatma segmentlerinin dışa odaklanması sayesinde A6 Sedan\'ın etkin ışın genişliğini en üst seviyeye çıkarır.', 'Audi', '4244224483', '1871654.00', 'Available', ''),
(5, 'Ali Bilmem', '2021-10-14 23:47:20', 'Ali Bilmem', '2021-10-16 14:23:52', NULL, ' ', 'No', '14-10-2021 23:47:20', ' A5 RS', ' Audi A5 Sportback ile farkınızı ortaya koyarsınız. Bu beş kapılı aile otomobilinin çarpıcı tasarımı, ona derinlik ve genişlik kazandırır. Buna ek olarak, isteğe bağlı Matrix LED farlar ve geniş bir bilgi-eğlence platformu gibi öncü teknolojiler sunar.\nGeniş hava girişleri ve krom giydirmeler bu sportif beş kapılı aracın ön kısmına keskin görünümünü kazandırır. Yanlarda, orta ağırlıkta kapı eşikleri aracın sportifliğine vurgu yaparken ve göze çarpan tekerlek kemerleri de isteğe bağlı sunulan 20 inç alaşım jantları vurgular. Difüzördeki mat alüminyum gümüş renkte yatay şerit ve ikizkenar yamuk şeklindeki egzoz boruları, arka kısmın genişliğini etkileyici bir şekilde ön plana çıkarır. Buna ek olarak; opsiyonel, Advanced ve S line donanım paketleri sayesinde Audi A5 Sportback\'i özelleştirmek de mümkündür.\nLED teknolojili farlar artık Audi A5 Sportback\'de standarttır. Audi lazer ışıklı Matrix LED farlar opsiyonel olarak mevcuttur. Audi lazer ışığı, yüksek araç hızlarında uzun LED farları tamamlar ve görüş mesafesini neredeyse iki katına çıkararak uzun LED fara göre gözle görülür derecede daha fazla emniyet ve rahatlık sağlar. coming home/leaving home (eve geliş/evden çıkış) özelliğinin dinamik ışık sıralaması ve dinamik göstergeler de bu aydınlatma paketinin birer parçasıdır.', 'Audi', '4244440654', '992305.00', 'Available', ' '),
(6, 'Ali Bilmem', '2021-10-14 23:49:57', 'Ali Bilmem', '2021-10-14 23:49:57', NULL, ' ', 'No', '14-10-2021 23:49:57', ' A4 RS', ' Hassas bir şekilde keskinleştirilmiş ön görünüm ve karakteristik arka kısma sahip ilerici tasarımı sayesinde, Audi A4 Sedan\'ın sportif karakteri her zamankinden daha fazla etkileyici: Birinci sınıf kaliteye sahip ve işlevsel bir iç tasarım, dijitalleşme, bilgi-eğlence ve sürücü asistan sistemleri² alanlarındaki yenilikçi teknolojiler ve bir dizi güçlü motor paketi tamamlar.\nAudi A4 Sedan\'ın yandan görünümü, dikkatleri anında tekerleklere, geniş kanatlara ve çarpıcı marşpiyel çıtalarına çeker. Arka kısımda, LED lambalar kesintisiz bir krom şeritle bağlanır. Yamuk şeklindeki entegre egzoz borularına sahip kayık difüzör alanı, sportif bir görünüm sağlar. Derin ve geniş Singleframe çerçeve, tampon ve frapan geniş hava girişleri ön tarafta güçlü bir birliktelik oluşturur.', 'Audi', '4244597842', '882950.00', 'Available', ' '),
(7, 'Ali Bilmem', '2021-10-14 23:51:24', 'Ali Bilmem', '2021-10-14 23:51:24', NULL, '', 'No', '14-10-2021 23:51:24', 'A3 RS ', 'Hızla değişen bir dünyada, zorluklara hazırlıklı bir arkadaşa ihtiyacınız var. İlerici tasarım ve yenilikçi teknolojiler ile İlerleme somut hale geldi. \nMetalik atol mavisi ve yeni Audi A3 Sportback edition one. Etkileyici çamurluk kaplamalarına, daha geniş bir iz genişliğine ve yan tarafta sürekli bir omuz çizgisine sahiptir. Bal peteği görünümüne sahip, yeni, genişletilmiş Audi Singleframe ızgara ile birlikte, Audi A3 Sportback her zaman olduğundan daha atletik görünmektedir.\nYeni progresif direksiyon ve opsiyonel Audi sürüş modu seçimi özelliği ile, yeni Audi A3 Sportback her türlü yola çıkmaya hazırdır. Audi sürüş modu seçimi özelliği ile, ister konforlu, ister dinamik, otomatik veya daha özelleştirilmiş tercih edin, A3 Sportback’in karakterini sürüş tarzınıza uyacak şekilde ayarlayabilirsiniz. Verimlilik modu yakıt tasarrufu yapmanıza yardımcı olabilir.', 'Audi', '4244684312', '650895.00', 'Available', ''),
(8, 'Ali Bilmem', '2021-10-14 23:53:19', 'Ali Bilmem', '2021-10-15 22:09:06', NULL, ' ', 'No', '14-10-2021 23:53:19', 'A8 L', 'Audi A8 L\'in, arkasında özellikle yüksek oturma konforu ile lüks ve ekstra geniş bir iç mekana sahip olacaksınız. Gelişmiş sürücü asistan sistemleri² ve ağ bağlantılı sistemler, günlük yaşamınızı gözle görülür derecede kolaylaştırır.\"\nOrta konsoldaki akustik ve dokunsal geri bildirime sahip iki dokunmatik ekran, bilgi-eğlence sisteminin ve ayrıca klima, konfor ve rahatlık işlevlerinin sezgisel çalışmasını sağlar. Gelişmiş ses kontrolü⁵ araçla etkileşimin bir diyalog şeklinde yürütülmesini sağlar. Audi connect⁷ sayesinde Amazon Alexa⁶ entegrasyonunu kullanabilirsiniz.\nAudi exclusive⁸ kişiselleştirme programı, kişisel tasarım fikirlerinizi biçimlendirir: İç mekan için çok çeşitli seçenekleri dilediğiniz biçimde birleştirin ve dış mekanın, \"ara\" mavisi, kristal görünümü, metalik Suzuka grisi, Goodwood yeşili, sedefli, ipanema kahverengi ve metalik gibi yansıtıcı renklerle göz alıcı olmasını sağlayın.', 'Audi', '4244799112', '3108155.00', 'Available', ' '),
(15, NULL, NULL, 'Ali Bilmem', '2021-10-15 01:24:46', NULL, '', 'No', '15-10-2021 01:24:46', 'A3 Sportback', 'Hızla değişen bir dünyada, zorluklara hazırlıklı bir arkadaşa ihtiyacınız var. İlerici tasarım ve yenilikçi teknolojiler ile İlerleme somut hale geldi.\nMetalik atol mavisi ve yeni Audi A3 Sportback edition one. Etkileyici çamurluk kaplamalarına, daha geniş bir iz genişliğine ve yan tarafta sürekli bir omuz çizgisine sahiptir. Bal peteği görünümüne sahip, yeni, genişletilmiş Audi Singleframe ızgara ile birlikte, Audi A3 Sportback her zaman olduğundan daha atletik görünmektedir.\nYeni progresif direksiyon ve opsiyonel Audi sürüş modu seçimi özelliği ile, yeni Audi A3 Sportback her türlü yola çıkmaya hazırdır. Audi sürüş modu seçimi özelliği ile, ister konforlu, ister dinamik, otomatik veya daha özelleştirilmiş tercih edin, A3 Sportback’in karakterini sürüş tarzınıza uyacak şekilde ayarlayabilirsiniz. Verimlilik modu yakıt tasarrufu yapmanıza yardımcı olabilir.', 'Audi ', '4248391159', '522819.00', 'Available', ''),
(16, 'Ali Bilmem', '2021-10-15 01:25:58', 'Ali Bilmem', '2021-10-15 01:25:58', NULL, '', 'No', '15-10-2021 01:25:58', 'A5 Sportback', 'Geniş hava girişleri ve krom giydirmeler bu sportif beş kapılı aracın ön kısmına keskin görünümünü kazandırır. Yanlarda, orta ağırlıkta kapı eşikleri aracın sportifliğine vurgu yaparken ve göze çarpan tekerlek kemerleri de isteğe bağlı sunulan 20 inç alaşım jantları vurgular. Difüzördeki mat alüminyum gümüş renkte yatay şerit ve ikizkenar yamuk şeklindeki egzoz boruları, arka kısmın genişliğini etkileyici bir şekilde ön plana çıkarır. Buna ek olarak; opsiyonel, Advanced ve S line donanım paketleri sayesinde Audi A5 Sportback\'i özelleştirmek de mümkündür.', 'Audi', '4250358778', '750000.00', 'Available', ''),
(17, 'Ali Bilmem', '2021-10-15 01:26:49', 'Ali Bilmem', '2021-10-15 01:26:49', NULL, '', 'No', '15-10-2021 01:26:49', 'A4 Avant', 'A4 Avant şunları temsil ediyor: sportiflik, kalite, dijitalleşme ve çok yönlülük. Otomobilin özgür karakteri görsel olarak da güçlü bir şekilde ifade edilir. Yüksek kaliteli iç tasarımda, öncü teknolojiler dijital yaşamınızı araç içinde de sürdürmenizi sağlar. Ve geniş bagaj alanı, bir dizi esnek taşıma seçeneği sunar.', 'Audi', '4250409438', '988740.00', 'Available', ''),
(18, 'Ali Bilmem', '2021-10-15 01:28:32', 'Ali Bilmem', '2021-10-15 02:56:40', NULL, '', 'No', '15-10-2021 01:28:32', 'S400d ', 'Yeni Mercedes-Benz S-Serisi’nin net tasarımı önemli detaylara odaklanmıştır: Dış mekanda mükemmel orantılar ve iç mekanda zamana uygun lüks. Bu sayede Yeni S-Serisi’nin trend belirleyen yenilikleri, vazgeçmek istemeyeceğiniz eşsiz bir görünüme sahip olur.\nModern lüks ve birinci sınıf konfor Yeni S-Serisi’nde yeni bir seviyeye ulaşıyor. Gelecekte otomobil kullanmak ulaşımdan çok daha fazlasını ifade edecek. Yeni S-Serisi ideal inziva yeridir. Yenilikçi konfor özellikleri, sürücü koltuğundan arka bölüme kadar birinci sınıf bir sürüş deneyimi sağlar.', 'Mercedes-Benz', '4250512205', '4500000.00', 'Available', ''),
(19, 'Ali Bilmem', '2021-10-15 01:29:54', 'Ali Bilmem', '2021-10-15 01:29:54', NULL, '', 'No', '15-10-2021 01:29:54', 'C200 4-Matic', 'Yeni C-Serisi’nin, Edition 1 AMG adındaki ilk üretime özel paketinde kapsamlı bir donanım kombinasyonu sunuluyor. Maksimum ayrıcalık ve konfor için tasarlanan Yeni C-Serisi Edition 1 AMG, hayatınızı kolaylaştıracak özelliklerle donatıldı. Otomatik bagaj kapağı kapatma sistemi ve KEYLESS-GO, sürücü ve yolcular için maksimum konfor sunarken, 19 inç çok kollu jantlar ve AMG tasarımı gövde rengi bagaj üstü spoyleri sportif bileşenleri oluşturuyor. DIGITAL LIGHT ve Kör Nokta Yardımcısı ise yüksek güvenlik beklentilerini karşılıyor.\n', 'Mercedes-Benz', '4250594209', '898054.00', 'Available', ''),
(20, 'Ali Bilmem', '2021-10-15 23:32:07', 'Ali Bilmem', '2021-10-15 23:32:07', NULL, '', 'No', '15-10-2021 23:32:07', 'Deneme', 'deneme', 'Deneme', '4329927445', '1000.00', 'Available', '');

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
(5, 'c10e7931-5e68-43f1-a03f-8d80f7cf4e04i.jpg'),
(5, '65fb0617-eb56-4f61-963f-2c2109630334b.jpg'),
(5, '49d3d849-c713-4439-8dc8-741d9d8a57093.jpg');

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
(5, 3),
(6, 3),
(7, 3),
(8, 3),
(15, 3),
(16, 3),
(17, 3),
(18, 3),
(19, 3),
(20, 2);

--
-- Dökümü yapılmış tablolar için indeksler
--

--
-- Tablo için indeksler `contents`
--
ALTER TABLE `contents`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_en0utx3suqotcyyt9okr1mje2` (`title`),
  ADD UNIQUE KEY `UK_37kb7e6x43n8m7xu4yfsnfix6` (`no`);

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
-- Dökümü yapılmış tablolar için AUTO_INCREMENT değeri
--

--
-- Tablo için AUTO_INCREMENT değeri `contents`
--
ALTER TABLE `contents`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- Tablo için AUTO_INCREMENT değeri `product`
--
ALTER TABLE `product`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- Tablo için AUTO_INCREMENT değeri `product_category`
--
ALTER TABLE `product_category`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- Dökümü yapılmış tablolar için kısıtlamalar
--

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
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
