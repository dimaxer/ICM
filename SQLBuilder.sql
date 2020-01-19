-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Jan 17, 2020 at 07:54 AM
-- Server version: 8.0.13-4
-- PHP Version: 7.2.24-0ubuntu0.18.04.2

CREATE SCHEMA request_db;
USE request_db;
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ZZUehgwcK6`
--

-- --------------------------------------------------------

--
-- Table structure for table `AdditionalInfo`
--

CREATE TABLE `AdditionalInfo` (
  `requestID` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `additionalInfo` varchar(255) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `AdditionalInfo`
--

INSERT INTO `AdditionalInfo` (`requestID`, `additionalInfo`) VALUES
('1', 'How much money it will cost to make an agreement with the company that holds the rights to this software'),
('2', 'Why visual studio is not enough is the expanded and more expensive version of visual studio code'),
('3', 'How much will it cost to replace computers and how many computers with this problem there are'),
('4', 'Why does the student actually need these courses if he or she has already completed them'),
('5', 'What is the cost of adaptation of a mobile site and whether such an adjustment would be useful to students');

-- --------------------------------------------------------

--
-- Table structure for table `EvaluationReport`
--

CREATE TABLE `EvaluationReport` (
  `RequestID` varchar(6) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `EvaluatorID` varchar(6) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `Description` varchar(2000) COLLATE utf8_unicode_ci NOT NULL,
  `Constraints` varchar(2000) COLLATE utf8_unicode_ci NOT NULL,
  `Result` varchar(2000) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `EvaluationReport`
--

INSERT INTO `EvaluationReport` (`RequestID`, `EvaluatorID`, `Description`, `Constraints`, `Result`) VALUES
('1', '006', 'The request was to install an notepad++ in the Class PCs ', 'need to check if we have an agreement with the suplier of this software and if not condider to buy one', 'I think that it benfit to the student so I recoment make the change'),
('2', '006', 'The request was to install an visual studio code in the Class PCs ', 'need to check if we have an agreement with the suplier of this software and if not condider to buy one', 'I think that it can benfit to the student so I recoment to make the change'),
('3', '007', 'problems with the internet in the Farm', 'old computers that can	t make internet connection', 'need to replece all the computers that make problemes'),
('4', '010', 'some of the older courses of the student not appear because the lecturer decision', 'it is up to the lecturer decision', 'I think we need to make this courses visible to the students'),
('5', '011', 'The site is not optimized for mobile', 'This requires widespread change with great financial cost ', 'The cost is worth the money and the change will be very useful');

-- --------------------------------------------------------
CREATE TABLE `IssuedGraph` (
  `rowIndex` int(11) NOT NULL,
  `graphName` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `issueDate` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `IssuedGraph`
--

INSERT INTO `IssuedGraph` (`rowIndex`, `graphName`, `issueDate`) VALUES
(1, 'Active Report', '2020-01-17'),
(2, 'Active Report', '2020-01-17'),
(3, 'Extensions Report', '2020-01-17'),
(4, 'Active Report', '2020-01-17'),
(5, 'Delays Report', '2020-01-17'),
(6, 'Delays Report', '2020-01-17'),
(7, 'Extensions Report', '2020-01-17');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `IssuedGraph`
--
ALTER TABLE `IssuedGraph`
  ADD PRIMARY KEY (`rowIndex`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `IssuedGraph`
--
ALTER TABLE `IssuedGraph`
  MODIFY `rowIndex` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

CREATE TABLE `IssuedGraphLog` (
  `rowIndex` int(11) NOT NULL,
  `issuedIndex` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `category` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `value` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `IssuedGraphLog`
--

INSERT INTO `IssuedGraphLog` (`rowIndex`, `issuedIndex`, `category`, `value`) VALUES
(1, '1', 'Week 0', 0),
(2, '1', 'Week 1', 2),
(3, '1', 'Week 2', 8),
(4, '2', 'Week 0', 4),
(5, '2', 'Week 1', 7),
(6, '2', 'Week 2', 5),
(7, '3', '0-10', 2),
(8, '3', '11-21', 1),
(9, '3', '22-32', 1),
(10, '4', 'Week 0', 5),
(11, '4', 'Week 1', 5),
(12, '7', '0-10', 2),
(13, '7', '11-21', 1),
(14, '7', '22-32', 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `IssuedGraphLog`
--
ALTER TABLE `IssuedGraphLog`
  ADD PRIMARY KEY (`rowIndex`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `IssuedGraphLog`
--
ALTER TABLE `IssuedGraphLog`
  MODIFY `rowIndex` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;
--
-- Table structure for table `EvaluatorAppointment`
--

CREATE TABLE `EvaluatorAppointment` (
  `RequestID` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `EvaluatorID` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `EvaluatorName` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ExecutionLeaderAppointment`
--

CREATE TABLE `ExecutionLeaderAppointment` (
  `RequestID` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `StartTime` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `InformationSystem`
--

CREATE TABLE `InformationSystem` (
  `infoSysName` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `evaluatorID` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `InformationSystem`
--

INSERT INTO `InformationSystem` (`infoSysName`, `evaluatorID`) VALUES
('Class PCs', '006'),
('Farm', '007'),
('Labs', '008'),
('Library', '009'),
('Moodle', '010'),
('Website', '011'),
('Yedion', '012');

-- --------------------------------------------------------

--
-- Table structure for table `PermanentEmployee`
--

CREATE TABLE `PermanentEmployee` (
  `rowIndex` int(11) NOT NULL,
  `userID` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `jobDescription` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `roleCount` int(2) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `PermanentEmployee`
--

INSERT INTO `PermanentEmployee` (`rowIndex`, `userID`, `jobDescription`, `roleCount`) VALUES
(1, '001', 'ISD Chief', 1),
(2, '002', 'Supervisor', 1),
(3, '004', 'Committee Member', 1),
(4, '003', 'Committee Chairman', 1),
(5, '005', 'Committee Member', 2);

-- --------------------------------------------------------

--
-- Table structure for table `Requests`
--

CREATE TABLE `Requests` (
  `RequestID` int(10) NOT NULL,
  `Date` varchar(10) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `InformationSystem` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `RequestedChange` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `CurrentSituation` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `RequestReason` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `Note` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `AttachFiles` tinyint(1) NOT NULL,
  `Stage` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `Status` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `InitiatorID` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `TesterID` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `ExecutionLeaderID` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `EvaluatorID` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `adhoc` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `Requests`
--

INSERT INTO `Requests` (`RequestID`, `Date`, `InformationSystem`, `RequestedChange`, `CurrentSituation`, `RequestReason`, `Note`, `AttachFiles`, `Stage`, `Status`, `InitiatorID`, `TesterID`, `ExecutionLeaderID`, `EvaluatorID`, `adhoc`) VALUES
(1, '2020-01-15', 'Class PCs', 'notepad++ in the PCs', 'there is no notepad ++ in the computers', 'I need notepad++ for Lab tasks', 'notepad++ is free software', 0, 'Closing', 'Closed', '006', '004', '006', '006', 1),
(2, '2020-01-16', 'Class PCs', 'visual studio code in the PCs', 'there is no visual studio code in the computers', 'I need visual studio code for Lab tasks', 'visual studio code is free software', 0, 'Closing', 'Active', '006', NULL, NULL, '006', 0),
(3, '2020-01-16', 'Farm', 'problems with the internet', 'not all computers connected to the internet', 'I don’t have intenet in the computer', 'some of the computers has internet', 0, 'Execution', 'Active', '007', '003', '008', '007', 1),
(4, '2020-01-16', 'Moodle', 'fix the moodle view courses', 'some of my courses not appear', 'cant get access to my courses', 'some of the courses I can access to', 0, 'Closing', 'Active', '010', '003', '010', '010', 1),
(5, '2020-01-17', 'Website', 'make the site compatiable with phones', 'I cant view the website in my samsung galaxy 10', 'want to get access to website information from my phone', 'Im my mothers phone (Iphone 10) it worked just fine ', 0, 'Closing', 'Closed', '001', '002', '002', '011', 0);

-- --------------------------------------------------------

--
-- Table structure for table `StageTable`
--

CREATE TABLE `StageTable` (
  `requestId` int(255) NOT NULL,
  `stage` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `startTime` date DEFAULT NULL,
  `endTime` date DEFAULT NULL,
  `deadlineTime` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `StageTable`
--

INSERT INTO `StageTable` (`requestId`, `stage`, `startTime`, `endTime`, `deadlineTime`) VALUES
(1, 'Closing', '2020-01-15', '2020-01-15', '2020-01-15'),
(1, 'Decision', '2020-01-15', '2020-01-15', '2020-01-22'),
(1, 'Evaluation', '2020-01-15', '2020-01-01', '2020-01-17'),
(1, 'Execution', '2020-01-16', '2020-01-27', '2020-01-18'),
(1, 'Testing', '2020-01-15', '2020-01-15', '2020-01-22'),
(2, 'Closing', '2020-01-17', NULL, '2020-01-18'),
(2, 'Decision', '2020-01-17', '2020-01-17', '2020-01-23'),
(2, 'Evaluation', '2020-01-16', '2020-01-17', '2020-01-18'),
(2, 'Execution', '2020-01-16', '2020-01-16', '2020-01-18'),
(3, 'Closing', '2020-01-10', NULL, '2020-01-14'),
(3, 'Decision', '2020-01-16', '2020-01-16', '2020-01-23'),
(3, 'Evaluation', '2020-01-16', '2020-01-16', '2020-01-19'),
(3, 'Execution', '2020-01-16', '2020-01-24', '2020-01-19'),
(3, 'Testing', '2020-01-16', '2020-01-16', '2020-01-23'),
(4, 'Closing', '2020-01-17', NULL, '2020-01-22'),
(4, 'Decision', '2020-01-16', '2020-01-16', '2020-01-23'),
(4, 'Evaluation', '2020-01-16', '2020-01-16', '2020-01-19'),
(4, 'Execution', '2020-01-16', '2020-01-16', '2020-01-19'),
(4, 'Testing', '2020-01-16', '2020-01-17', '2020-01-18'),
(5, 'Closing', '2020-01-17', NULL, '2020-01-21'),
(5, 'Decision', '2020-01-17', '2020-01-17', '2020-01-24'),
(5, 'Evaluation', '2020-01-17', '2020-01-17', '2020-01-21');

-- --------------------------------------------------------

--
-- Table structure for table `StageTableLog`
--

CREATE TABLE `StageTableLog` (
  `rowIndex` int(11) NOT NULL,
  `requestId` int(255) NOT NULL,
  `stage` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `startTime` date DEFAULT NULL,
  `endTime` date DEFAULT NULL,
  `deadlineTime` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `StageTableLog`
--

INSERT INTO `StageTableLog` (`rowIndex`, `requestId`, `stage`, `startTime`, `endTime`, `deadlineTime`) VALUES
(1, 1, 'Evaluation', '2020-01-15', '2020-01-15', '2020-01-17'),
(2, 1, 'Decision', '2020-01-15', '2020-01-15', '2020-01-22'),
(3, 1, 'Evaluation', '2020-01-15', '2020-01-15', '2020-01-17'),
(4, 1, 'Decision', '2020-01-15', '2020-01-15', '2020-01-22'),
(7, 1, 'Execution', '2020-01-15', '2020-01-15', '2020-01-19'),
(8, 1, 'Testing', '2020-01-15', '2020-01-15', '2020-01-22'),
(9, 1, 'Execution', '2020-01-15', '2020-01-15', '2020-01-19'),
(10, 1, 'Execution', '2020-01-15', '2020-01-15', '2020-01-17'),
(11, 1, 'Testing', '2020-01-15', '2020-01-15', '2020-01-22'),
(12, 1, 'Execution', '2020-01-15', '2020-01-15', '2020-01-17'),
(13, 1, 'Testing', '2020-01-15', '2020-01-15', '2020-01-22'),
(14, 1, 'Execution', '2020-01-15', '2020-01-15', '2020-01-17'),
(15, 1, 'Testing', '2020-01-15', '2020-01-15', '2020-01-22'),
(16, 1, 'Closing', '2020-01-15', '2020-01-15', '2020-01-15'),
(17, 2, 'Evaluation', '2020-01-16', '2020-01-16', '2020-01-18'),
(18, 2, 'Decision', '2020-01-16', '2020-01-16', '2020-01-23'),
(19, 2, 'Evaluation', '2020-01-16', '2020-01-16', '2020-01-18'),
(20, 2, 'Decision', '2020-01-16', '2020-01-16', '2020-01-23'),
(21, 3, 'Evaluation', '2020-01-16', '2020-01-16', '2020-01-18'),
(22, 3, 'Decision', '2020-01-16', '2020-01-16', '2020-01-23'),
(23, 3, 'Evaluation', '2020-01-16', '2020-01-16', '2020-01-19'),
(24, 3, 'Decision', '2020-01-16', '2020-01-16', '2020-01-23'),
(25, 3, 'Execution', '2020-01-16', '2020-01-16', '2020-01-18'),
(26, 3, 'Testing', '2020-01-16', '2020-01-16', '2020-01-23'),
(27, 3, 'Testing', '2020-01-16', '2020-01-16', '2020-01-23'),
(28, 4, 'Evaluation', '2020-01-16', '2020-01-16', '2020-01-20'),
(29, 4, 'Decision', '2020-01-16', '2020-01-16', '2020-01-23'),
(30, 4, 'Evaluation', '2020-01-16', '2020-01-16', '2020-01-19'),
(31, 4, 'Decision', '2020-01-16', '2020-01-16', '2020-01-23'),
(32, 4, 'Execution', '2020-01-16', '2020-01-16', '2020-01-19'),
(33, 4, 'Testing', '2020-01-16', '2020-01-16', '2020-01-23'),
(34, 4, 'Execution', '2020-01-16', '2020-01-16', '2020-01-19'),
(35, 4, 'Testing', '2020-01-16', '2020-01-16', '2020-01-23'),
(36, 4, 'Closing', '2020-01-16', '2020-01-16', '2020-01-16'),
(37, 5, 'Evaluation', '2020-01-17', '2020-01-17', '2020-01-19'),
(38, 5, 'Decision', '2020-01-17', '2020-01-17', '2020-01-24'),
(39, 5, 'Evaluation', '2020-01-17', '2020-01-17', '2020-01-21'),
(40, 5, 'Decision', '2020-01-17', '2020-01-17', '2020-01-24'),
(41, 5, 'Closing', '2020-01-17', '2020-01-17', '2020-01-17'),
(42, 4, 'Testing', '2020-01-16', '2020-01-16', '2020-01-23'),
(43, 4, 'Testing', '2020-01-16', '2020-01-17', '2020-01-23'),
(44, 2, 'Evaluation', '2020-01-16', '2020-01-18', '2020-01-18'),
(45, 2, 'Decision', '2020-01-17', '2020-01-17', '2020-01-23');

-- --------------------------------------------------------

--
-- Table structure for table `StatusTable`
--

CREATE TABLE `StatusTable` (
  `requestId` int(255) NOT NULL,
  `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `startTime` date DEFAULT NULL,
  `endTime` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `StatusTable`
--

INSERT INTO `StatusTable` (`requestId`, `status`, `startTime`, `endTime`) VALUES
(1, 'Active', '2020-01-15', '2020-01-16'),
(1, 'Frozen', '2020-01-15', NULL),
(2, 'Active', '2020-01-17', NULL),
(2, 'Frozen', '2020-01-17', NULL),
(3, 'Active', '2020-01-16', NULL),
(4, 'Active', '2020-01-16', NULL),
(4, 'Closed', '2020-01-16', '2020-01-16'),
(5, 'Active', '2020-01-17', NULL),
(5, 'Closed', '2020-01-17', '2020-01-17'),
(7, 'Active', '2020-01-08', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `StatusTableLog`
--

CREATE TABLE `StatusTableLog` (
  `rowIndex` int(11) NOT NULL,
  `requestId` int(255) NOT NULL,
  `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `startTime` date DEFAULT NULL,
  `endTime` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `StatusTableLog`
--

INSERT INTO `StatusTableLog` (`rowIndex`, `requestId`, `status`, `startTime`, `endTime`) VALUES
(1, 1, 'Active', '2020-01-12', '2020-01-13'),
(2, 1, 'Frozen', '2020-01-13', '2020-01-15'),
(4, 1, 'Closed', '2020-01-19', '2020-01-19'),
(5, 4, 'Closed', '2020-01-12', '2020-01-13'),
(6, 3, 'Active', '2020-01-12', '2020-01-13'),
(7, 4, 'Active', '2020-01-16', '2020-01-16'),
(8, 4, 'Closed', '2020-01-16', '2020-01-16'),
(9, 2, 'Active', '2020-01-16', '2020-01-17'),
(10, 2, 'Frozen', '2020-01-17', '2020-01-17'),
(11, 5, 'Active', '2020-01-17', '2020-01-17'),
(12, 5, 'Closed', '2020-01-17', '2020-01-17');

-- --------------------------------------------------------

--
-- Table structure for table `TestRejectionInfo`
--

CREATE TABLE `TestRejectionInfo` (
  `requestID` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `additionalInfo` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `TestRejectionInfo`
--

INSERT INTO `TestRejectionInfo` (`requestID`, `additionalInfo`) VALUES
('1', 'AK'),
('3', 'A'),
('4', 'F');

-- --------------------------------------------------------

--
-- Table structure for table `TimeAssessment`
--

CREATE TABLE `TimeAssessment` (
  `RequestID` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `UserID` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `JobDescription` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `StartTime` date NOT NULL,
  `EndTime` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `TimeAssessmentLog`
--

CREATE TABLE `TimeAssessmentLog` (
  `rowIndex` int(11) NOT NULL,
  `RequestID` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `UserID` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `JobDescription` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `StartTime` date NOT NULL,
  `EndTime` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `TimeAssessmentLog`
--

INSERT INTO `TimeAssessmentLog` (`rowIndex`, `RequestID`, `UserID`, `JobDescription`, `StartTime`, `EndTime`) VALUES
(1, '1', '006', 'Evaluator', '2020-01-15', '2020-01-09'),
(2, '1', '006', 'Execution Leader', '2020-01-15', '2020-01-17'),
(3, '1', '006', 'Execution Leader', '2020-01-15', '2020-01-17'),
(4, '1', '006', 'Execution Leader', '2020-01-15', '2020-01-17'),
(5, '2', '006', 'Evaluator', '2020-01-15', '2020-01-01'),
(7, '3', '007', 'Evaluator', '2020-01-16', '2020-01-18'),
(8, '3', '007', 'Evaluator', '2020-01-16', '2020-01-19'),
(9, '3', '008', 'Execution Leader', '2020-01-16', '2020-01-18'),
(10, '4', '010', 'Evaluator', '2020-01-16', '2020-01-20'),
(11, '4', '010', 'Evaluator', '2020-01-16', '2020-01-19'),
(12, '4', '010', 'Execution Leader', '2020-01-16', '2020-01-19'),
(13, '4', '010', 'Execution Leader', '2020-01-16', '2020-01-19'),
(14, '3', '008', 'Execution Leader', '2020-01-16', '2020-01-19'),
(15, '2', '010', 'Execution Leader', '2020-01-16', '2020-01-20'),
(16, '5', '011', 'Evaluator', '2020-01-17', '2020-01-19'),
(17, '5', '011', 'Evaluator', '2020-01-17', '2020-01-21');

-- --------------------------------------------------------

--
-- Table structure for table `TimeAssessmentRejectionInfo`
--

CREATE TABLE `TimeAssessmentRejectionInfo` (
  `RequestID` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `UserID` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `RejectionInfo` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `TimeExtension`
--

CREATE TABLE `TimeExtension` (
  `RequestID` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `UserID` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `JobDescription` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `StartTime` date NOT NULL,
  `EndTime` date NOT NULL,
  `Stage` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `TimeExtension`
--

INSERT INTO `TimeExtension` (`RequestID`, `UserID`, `JobDescription`, `StartTime`, `EndTime`, `Stage`) VALUES
('2', '006', 'Evaluator', '2020-01-16', '2020-01-21', 'Evaluation');

-- --------------------------------------------------------

--
-- Table structure for table `TimeExtensionLog`
--

CREATE TABLE `TimeExtensionLog` (
  `rowIndex` int(11) NOT NULL,
  `RequestID` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `UserID` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `JobDescription` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `StartTime` date NOT NULL,
  `EndTime` date NOT NULL,
  `Stage` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `TimeExtensionLog`
--

INSERT INTO `TimeExtensionLog` (`rowIndex`, `RequestID`, `UserID`, `JobDescription`, `StartTime`, `EndTime`, `Stage`) VALUES
(1, '1', '006', 'Execution Leader', '2020-01-15', '2020-01-19', 'Execution'),
(2, '2', '006', 'Execution Leader', '2020-01-15', '2020-01-19', 'Execution'),
(3, '3', '003', 'Dog', '2020-01-20', '2020-02-19', NULL),
(4, '4', '004', 'Cow', '2020-01-10', '2020-01-26', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `TimeExtensionRejectionInfo`
--

CREATE TABLE `TimeExtensionRejectionInfo` (
  `RequestID` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `UserID` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `RejectionInfo` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `UserMessage`
--

CREATE TABLE `UserMessage` (
  `rowIndex` int(11) NOT NULL,
  `UserID` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `RequestID` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `Message` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `Status` varchar(255) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Users`
--

CREATE TABLE `Users` (
  `UserID` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `Password` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `Name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `Email` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `Department` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `userType` varchar(255) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `Users`
--

INSERT INTO `Users` (`UserID`, `Password`, `Name`, `Email`, `Department`, `userType`) VALUES
('001', '001', 'Malka', 'shoham39@gmail.com', 'Software', 'ISE'),
('002', '002', 'Dima', 'razmalkau@gmail.com', 'Software', 'ISE'),
('003', '003', 'Noam', 'shoham39@gmail.com', 'Software', 'ISE'),
('004', '004', 'Shoham', 'razmalkau@gmail.com', 'Software', 'ISE'),
('005', '005', 'Afriat', 'shoham39@gmail.com', 'Software', 'ISE'),
('006', '006', 'David', 'shoham39@gmail.com', 'Mechanic', 'ISE'),
('007', '007', 'Joshua', 'shoham39@gmail.com', 'Mechanic', 'ISE'),
('008', '008', 'Silvester', 'shoham39@gmail.com', 'Biotech', 'ISE'),
('009', '009', 'Vladimir', 'shoham39@gmail.com', 'Biotech', 'ISE'),
('010', '010', 'Benjamin', 'shoham39@gmail.com', 'Biotech', 'ISE'),
('011', '011', 'Klein', 'shoham39@gmail.com', 'Medicine', 'ISE'),
('012', '012', 'Kevin', 'shoham39@gmail.com', 'Medicine', 'ISE'),
('013', '013', 'Cody', 'shoham39@gmail.com', 'Medicine', 'ISE'),
('014', '014', 'Pavel', 'shoham39@gmail.com', 'Medicine', 'ISE'),
('015', '015', 'Kyle', 'shoham39@gmail.com', 'Software', 'Student'),
('016', '016', 'Walter', 'shoham39@gmail.com', 'Software', 'Student'),
('017', '017', 'Claude', 'shoham39@gmail.com', 'Software', 'Lecturer'),
('018', '018', 'Catelyn', 'shoham39@gmail.com', 'Software', 'Lecturer'),
('123', '123', 'dima', 'shoham39@gmail.com', 'Software Engineering', 'Student');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `AdditionalInfo`
--
ALTER TABLE `AdditionalInfo`
  ADD PRIMARY KEY (`requestID`);

--
-- Indexes for table `EvaluationReport`
--
ALTER TABLE `EvaluationReport`
  ADD PRIMARY KEY (`RequestID`);

--
-- Indexes for table `EvaluatorAppointment`
--
ALTER TABLE `EvaluatorAppointment`
  ADD PRIMARY KEY (`RequestID`);

--
-- Indexes for table `ExecutionLeaderAppointment`
--
ALTER TABLE `ExecutionLeaderAppointment`
  ADD PRIMARY KEY (`RequestID`);

--
-- Indexes for table `InformationSystem`
--
ALTER TABLE `InformationSystem`
  ADD PRIMARY KEY (`infoSysName`),
  ADD UNIQUE KEY `evaluatorID` (`evaluatorID`);

--
-- Indexes for table `PermanentEmployee`
--
ALTER TABLE `PermanentEmployee`
  ADD PRIMARY KEY (`rowIndex`);

--
-- Indexes for table `Requests`
--
ALTER TABLE `Requests`
  ADD PRIMARY KEY (`RequestID`);

--
-- Indexes for table `StageTable`
--
ALTER TABLE `StageTable`
  ADD PRIMARY KEY (`requestId`,`stage`);

--
-- Indexes for table `StageTableLog`
--
ALTER TABLE `StageTableLog`
  ADD PRIMARY KEY (`rowIndex`);

--
-- Indexes for table `StatusTable`
--
ALTER TABLE `StatusTable`
  ADD PRIMARY KEY (`requestId`,`status`);

--
-- Indexes for table `StatusTableLog`
--
ALTER TABLE `StatusTableLog`
  ADD PRIMARY KEY (`rowIndex`);

--
-- Indexes for table `TestRejectionInfo`
--
ALTER TABLE `TestRejectionInfo`
  ADD PRIMARY KEY (`requestID`);

--
-- Indexes for table `TimeAssessment`
--
ALTER TABLE `TimeAssessment`
  ADD PRIMARY KEY (`RequestID`);

--
-- Indexes for table `TimeAssessmentLog`
--
ALTER TABLE `TimeAssessmentLog`
  ADD PRIMARY KEY (`rowIndex`);

--
-- Indexes for table `TimeAssessmentRejectionInfo`
--
ALTER TABLE `TimeAssessmentRejectionInfo`
  ADD PRIMARY KEY (`RequestID`);

--
-- Indexes for table `TimeExtension`
--
ALTER TABLE `TimeExtension`
  ADD PRIMARY KEY (`RequestID`);

--
-- Indexes for table `TimeExtensionLog`
--
ALTER TABLE `TimeExtensionLog`
  ADD PRIMARY KEY (`rowIndex`);

--
-- Indexes for table `TimeExtensionRejectionInfo`
--
ALTER TABLE `TimeExtensionRejectionInfo`
  ADD PRIMARY KEY (`RequestID`);

--
-- Indexes for table `UserMessage`
--
ALTER TABLE `UserMessage`
  ADD PRIMARY KEY (`rowIndex`);

--
-- Indexes for table `Users`
--
ALTER TABLE `Users`
  ADD PRIMARY KEY (`UserID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `PermanentEmployee`
--
ALTER TABLE `PermanentEmployee`
  MODIFY `rowIndex` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `Requests`
--
ALTER TABLE `Requests`
  MODIFY `RequestID` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `StageTableLog`
--
ALTER TABLE `StageTableLog`
  MODIFY `rowIndex` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=46;

--
-- AUTO_INCREMENT for table `StatusTableLog`
--
ALTER TABLE `StatusTableLog`
  MODIFY `rowIndex` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `TimeAssessmentLog`
--
ALTER TABLE `TimeAssessmentLog`
  MODIFY `rowIndex` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT for table `TimeExtensionLog`
--
ALTER TABLE `TimeExtensionLog`
  MODIFY `rowIndex` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `UserMessage`
--
ALTER TABLE `UserMessage`
  MODIFY `rowIndex` int(11) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
