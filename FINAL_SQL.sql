CREATE SCHEMA `request_db`;
SELECT DATABASE();
USE request_db;

CREATE TABLE `Faculty` (
  `faculty_num` int(11) NOT NULL,
  `faculty_name` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Positions`
--

CREATE TABLE `Positions` (
  `worker_position` int(11) NOT NULL,
  `position_name` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Requirement`
--

CREATE TABLE `RequestInformation` (
  `InitiatorName` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `RequestID` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `CurrentStageDetails` varchar(1000) COLLATE utf8_unicode_ci NOT NULL,
  `Description` varchar(1000) COLLATE utf8_unicode_ci NOT NULL,
  `Status` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `HandlerName` varchar(255) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `Requirement`
--

INSERT INTO `RequestInformation` (`InitiatorName`, `RequestID`, `CurrentStageDetails`, `Description`, `Status`, `HandlerName`) VALUES
('noam', '111', 'evaluation', 'Class is boring', 'Suspended', 'yosi'),
('raz', '222', 'first', 'shoam', 'ongoing', 'shmuel'),
('dima', '333', 'first', 'yosi', 'ongoing', 'kobi');

-- --------------------------------------------------------

--
-- Table structure for table `sample_login`
--

CREATE TABLE `sample_login` (
  `uid` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `upassword` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `sample_login`
--

INSERT INTO `sample_login` (`uid`, `upassword`) VALUES
('123456789', '0'),
('987654321', '1'),
('123', '123'),
('123', '123');

-- --------------------------------------------------------

--
-- Table structure for table `Students`
--

CREATE TABLE `Students` (
  `student_num` int(11) NOT NULL,
  `uid` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `faculty_num` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Users`
--

CREATE TABLE `Users` (
  `uid` varchar(256) COLLATE utf8_unicode_ci NOT NULL,
  `upassword` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `first_name` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_name` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `email` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `isWorker` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Workers`
--

CREATE TABLE `Workers` (
  `worker_num` int(11) NOT NULL,
  `uid` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `faculty_num` int(11) NOT NULL,
  `worker_position` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Faculty`
--
ALTER TABLE `Faculty`
  ADD PRIMARY KEY (`faculty_num`);

--
-- Indexes for table `Positions`
--
ALTER TABLE `Positions`
  ADD PRIMARY KEY (`worker_position`);

--
-- Indexes for table `Students`
--
ALTER TABLE `Students`
  ADD PRIMARY KEY (`student_num`),
  ADD KEY `uid` (`uid`),
  ADD KEY `faculty_num` (`faculty_num`);

--
-- Indexes for table `Users`
--
ALTER TABLE `Users`
  ADD PRIMARY KEY (`uid`);

--
-- Indexes for table `Workers`
--
ALTER TABLE `Workers`
  ADD PRIMARY KEY (`worker_num`),
  ADD KEY `uid` (`uid`),
  ADD KEY `faculty_num` (`faculty_num`),
  ADD KEY `worker_position` (`worker_position`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Faculty`
--
ALTER TABLE `Faculty`
  MODIFY `faculty_num` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Positions`
--
ALTER TABLE `Positions`
  MODIFY `worker_position` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Students`
--
ALTER TABLE `Students`
  MODIFY `student_num` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Workers`
--
ALTER TABLE `Workers`
  MODIFY `worker_num` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `Students`
--
ALTER TABLE `Students`
  ADD CONSTRAINT `Students_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `Users` (`uid`),
  ADD CONSTRAINT `Students_ibfk_2` FOREIGN KEY (`faculty_num`) REFERENCES `Faculty` (`faculty_num`);

--
-- Constraints for table `Workers`
--
ALTER TABLE `Workers`
  ADD CONSTRAINT `Workers_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `Users` (`uid`),
  ADD CONSTRAINT `Workers_ibfk_2` FOREIGN KEY (`faculty_num`) REFERENCES `Faculty` (`faculty_num`),
  ADD CONSTRAINT `Workers_ibfk_3` FOREIGN KEY (`worker_position`) REFERENCES `Positions` (`worker_position`);
COMMIT;