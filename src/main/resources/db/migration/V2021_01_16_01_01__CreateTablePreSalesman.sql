CREATE TABLE `tfile` (
  `fid` bigint(20) NOT NULL,
  `fparent` bigint(20),
  `fname` varchar(2000),
  `ftype` int,
  `fsize` bigint,
  `fstatus` int,
  PRIMARY KEY (`fid`)
);
CREATE INDEX tfile_fparent ON tfile (fparent);