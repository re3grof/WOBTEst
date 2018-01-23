
CREATE DATABASE IF NOT EXISTS `wob_data` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `wob_data`;

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `order`
--

CREATE TABLE `order` (
  `OrderId` int(11) NOT NULL,
  `BuyerName` varchar(50) NOT NULL,
  `BuyerEmail` varchar(50) NOT NULL,
  `OrderDate` date NOT NULL,
  `OrderTotalValue` double NOT NULL,
  `Address` varchar(100) NOT NULL,
  `PostCode` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `order_item`
--

CREATE TABLE `order_item` (
  `OrderItemId` int(11) NOT NULL,
  `OrderId` int(11) NOT NULL,
  `SalePrice` float NOT NULL,
  `ShippingPrice` float NOT NULL,
  `TotalItemPrice` float NOT NULL,
  `SKU` varchar(100) NOT NULL,
  `Status` enum('IN_STOCK','OUT_OF_STOCK') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Eseményindítók `order_item`
--
DELIMITER $$
CREATE TRIGGER `trg_order_item_bd` BEFORE DELETE ON `order_item` FOR EACH ROW BEGIN

UPDATE `order` SET `order`.`OrderTotalValue` =  TRUNCATE((SELECT COALESCE(SUM(`order_item`.`TotalItemPrice`),0.0) FROM `order_item` WHERE `OrderId` = old.OrderId and `OrderItemId` <> old.OrderItemId) ,2)
WHERE `order`.`OrderId` = old.OrderId;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `trg_order_item_bi` BEFORE INSERT ON `order_item` FOR EACH ROW BEGIN
	SET new.TotalItemPrice = new.SalePrice + new.ShippingPrice;

UPDATE `order` SET `order`.`OrderTotalValue` =  TRUNCATE((SELECT COALESCE(SUM(`order_item`.`TotalItemPrice`),0.0) FROM `order_item` WHERE `OrderId` = new.OrderId) + new.TotalItemPrice,2)
WHERE `order`.`OrderId` = new.OrderId;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `trg_order_item_bu` BEFORE UPDATE ON `order_item` FOR EACH ROW BEGIN
	SET new.TotalItemPrice = new.SalePrice + new.ShippingPrice;

UPDATE `order` SET `order`.`OrderTotalValue` =  TRUNCATE((SELECT COALESCE(SUM(`order_item`.`TotalItemPrice`),0.0) FROM `order_item` WHERE `OrderId` = new.OrderId and `OrderItemId` <> new.OrderItemId) + new.TotalItemPrice,2)
WHERE `order`.`OrderId` = new.OrderId;
END
$$
DELIMITER ;

--
-- Indexek a kiírt táblákhoz
--

--
-- A tábla indexei `order`
--
ALTER TABLE `order`
  ADD PRIMARY KEY (`OrderId`);

--
-- A tábla indexei `order_item`
--
ALTER TABLE `order_item`
  ADD PRIMARY KEY (`OrderItemId`),
  ADD KEY `OrderId` (`OrderId`);

--
-- Megkötések a kiírt táblákhoz
--

--
-- Megkötések a táblához `order_item`
--
ALTER TABLE `order_item`
  ADD CONSTRAINT `fk_order` FOREIGN KEY (`OrderId`) REFERENCES `order` (`OrderId`) ON DELETE CASCADE ON UPDATE CASCADE;
