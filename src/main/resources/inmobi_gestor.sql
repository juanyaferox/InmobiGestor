-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 17-10-2024 a las 17:16:21
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `inmobi_gestor`
--
CREATE DATABASE IF NOT EXISTS `inmobi_gestor` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `inmobi_gestor`;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `branchs`
--

CREATE TABLE `branchs` (
  `idBranch` int(10) UNSIGNED NOT NULL,
  `idTown` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clients`
--

CREATE TABLE `clients` (
  `idClient` int(10) UNSIGNED NOT NULL,
  `idUser` int(10) UNSIGNED DEFAULT NULL,
  `idBranch` int(10) UNSIGNED NOT NULL,
  `idEstateRented` int(10) UNSIGNED DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `lastname1` varchar(50) NOT NULL,
  `lastname2` varchar(50) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `dni` varchar(9) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `type` int(10) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `estates`
--

CREATE TABLE `estates` (
  `idEstate` int(10) UNSIGNED NOT NULL,
  `idClient` int(10) UNSIGNED DEFAULT NULL,
  `idBranch` int(10) UNSIGNED NOT NULL,
  `reference` int(11) NOT NULL,
  `fullAddress` varchar(500) NOT NULL,
  `imagePath` varchar(500) DEFAULT NULL,
  `state` int(10) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `historyrent`
--

CREATE TABLE `historyrent` (
  `idHistoryRent` int(10) UNSIGNED NOT NULL,
  `idEstate` int(10) UNSIGNED NOT NULL,
  `idClient` int(10) UNSIGNED DEFAULT NULL,
  `idClientRented` int(10) UNSIGNED DEFAULT NULL,
  `startDate` date NOT NULL,
  `endDate` date DEFAULT NULL,
  `exitDate` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `historysale`
--

CREATE TABLE `historysale` (
  `idHistorySale` int(10) UNSIGNED NOT NULL,
  `idEstate` int(10) UNSIGNED NOT NULL,
  `idClientPrevious` int(10) UNSIGNED DEFAULT NULL,
  `idClientActual` int(10) UNSIGNED DEFAULT NULL,
  `salePrice` decimal(10,2) UNSIGNED DEFAULT NULL,
  `saleDate` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `users`
--

CREATE TABLE `users` (
  `idUser` int(10) UNSIGNED NOT NULL,
  `user` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `lastname1` varchar(50) NOT NULL,
  `lastname2` varchar(50) NOT NULL,
  `dni` varchar(9) NOT NULL,
  `idBranch` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `branchs`
--
ALTER TABLE `branchs`
  ADD PRIMARY KEY (`idBranch`);

--
-- Indices de la tabla `clients`
--
ALTER TABLE `clients`
  ADD PRIMARY KEY (`idClient`),
  ADD KEY `idUser` (`idUser`,`idBranch`),
  ADD KEY `idBranch` (`idBranch`);

--
-- Indices de la tabla `estates`
--
ALTER TABLE `estates`
  ADD PRIMARY KEY (`idEstate`),
  ADD KEY `idClient` (`idClient`,`idBranch`),
  ADD KEY `idBranch` (`idBranch`);

--
-- Indices de la tabla `historyrent`
--
ALTER TABLE `historyrent`
  ADD PRIMARY KEY (`idHistoryRent`),
  ADD KEY `idEstate` (`idEstate`,`idClient`,`idClientRented`),
  ADD KEY `historyrent_ibfk_2` (`idClient`),
  ADD KEY `historyrent_ibfk_3` (`idClientRented`);

--
-- Indices de la tabla `historysale`
--
ALTER TABLE `historysale`
  ADD PRIMARY KEY (`idHistorySale`),
  ADD KEY `idEstate` (`idEstate`,`idClientPrevious`,`idClientActual`),
  ADD KEY `historysale_ibfk_2` (`idClientPrevious`),
  ADD KEY `historysale_ibfk_3` (`idClientActual`);

--
-- Indices de la tabla `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`idUser`),
  ADD KEY `idBranch` (`idBranch`);

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `clients`
--
ALTER TABLE `clients`
  ADD CONSTRAINT `clients_ibfk_1` FOREIGN KEY (`idUser`) REFERENCES `users` (`idUser`),
  ADD CONSTRAINT `clients_ibfk_2` FOREIGN KEY (`idBranch`) REFERENCES `branchs` (`idBranch`);

--
-- Filtros para la tabla `estates`
--
ALTER TABLE `estates`
  ADD CONSTRAINT `estates_ibfk_1` FOREIGN KEY (`idBranch`) REFERENCES `branchs` (`idBranch`),
  ADD CONSTRAINT `estates_ibfk_2` FOREIGN KEY (`idClient`) REFERENCES `clients` (`idClient`);

--
-- Filtros para la tabla `historyrent`
--
ALTER TABLE `historyrent`
  ADD CONSTRAINT `historyrent_ibfk_1` FOREIGN KEY (`idEstate`) REFERENCES `estates` (`idEstate`),
  ADD CONSTRAINT `historyrent_ibfk_2` FOREIGN KEY (`idClient`) REFERENCES `clients` (`idClient`),
  ADD CONSTRAINT `historyrent_ibfk_3` FOREIGN KEY (`idClientRented`) REFERENCES `clients` (`idClient`) ;

--
-- Filtros para la tabla `historysale`
--
ALTER TABLE `historysale`
  ADD CONSTRAINT `historysale_ibfk_1` FOREIGN KEY (`idEstate`) REFERENCES `estates` (`idEstate`),
  ADD CONSTRAINT `historysale_ibfk_2` FOREIGN KEY (`idClientPrevious`) REFERENCES `clients` (`idClient`),
  ADD CONSTRAINT `historysale_ibfk_3` FOREIGN KEY (`idClientActual`) REFERENCES `clients` (`idClient`);

--
-- Filtros para la tabla `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `users_ibfk_2` FOREIGN KEY (`idBranch`) REFERENCES `branchs` (`idBranch`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
