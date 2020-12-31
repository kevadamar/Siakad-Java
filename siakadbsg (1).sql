-- phpMyAdmin SQL Dump
-- version 4.2.11
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: 27 Des 2020 pada 14.20
-- Versi Server: 5.6.21
-- PHP Version: 5.6.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `siakadbsg`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `kelas`
--

CREATE TABLE IF NOT EXISTS `kelas` (
  `id_kelas` int(4) NOT NULL,
  `nama_kelas` varchar(5) NOT NULL,
  `id_prodi` int(3) NOT NULL,
  `id_konsentrasi` int(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `kelas`
--

INSERT INTO `kelas` (`id_kelas`, `nama_kelas`, `id_prodi`, `id_konsentrasi`) VALUES
(1, 'SE 18', 1, 1),
(2, 'CN 18', 1, 2),
(3, 'MM 18', 1, 3);

-- --------------------------------------------------------

--
-- Struktur dari tabel `konsentrasi`
--

CREATE TABLE IF NOT EXISTS `konsentrasi` (
  `id_konsentrasi` int(3) NOT NULL,
  `nama_konsen` varchar(40) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `konsentrasi`
--

INSERT INTO `konsentrasi` (`id_konsentrasi`, `nama_konsen`) VALUES
(1, 'Software Engineering'),
(2, 'Computer Networking'),
(3, 'Multimedia'),
(4, 'E-Commerce'),
(5, 'Bussiness Intelligence'),
(6, 'Computerized Accounting');

-- --------------------------------------------------------

--
-- Struktur dari tabel `mahasiswa`
--

CREATE TABLE IF NOT EXISTS `mahasiswa` (
  `nim` char(8) NOT NULL,
  `nama` varchar(30) NOT NULL,
  `alamat` text NOT NULL,
  `no_telp` varchar(15) NOT NULL,
  `id_prodi` int(3) NOT NULL,
  `id_konsentrasi` int(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `mahasiswa`
--

INSERT INTO `mahasiswa` (`nim`, `nama`, `alamat`, `no_telp`, `id_prodi`, `id_konsentrasi`) VALUES
('1111', 'Yusro Iza Maulana', 'Kp.Serdang', '081291466043', 1, 1),
('1112', 'Khoerun Nisa', 'Curug Kulon', '083875567710', 2, 5);

-- --------------------------------------------------------

--
-- Struktur dari tabel `matkul`
--

CREATE TABLE IF NOT EXISTS `matkul` (
  `id_matkul` varchar(4) NOT NULL,
  `nama_matkul` varchar(50) NOT NULL,
  `sks` int(2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `matkul`
--

INSERT INTO `matkul` (`id_matkul`, `nama_matkul`, `sks`) VALUES
('1', 'PBO', 6),
('2', 'Kalkulus', 7);

-- --------------------------------------------------------

--
-- Struktur dari tabel `prodi`
--

CREATE TABLE IF NOT EXISTS `prodi` (
  `id_prodi` int(3) NOT NULL,
  `nama_prodi` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `prodi`
--

INSERT INTO `prodi` (`id_prodi`, `nama_prodi`) VALUES
(1, 'TI'),
(2, 'SI');

-- --------------------------------------------------------

--
-- Struktur dari tabel `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `username` varchar(16) NOT NULL,
  `password` varchar(10) NOT NULL,
  `level` enum('mahasiswa','petugas','admin') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `user`
--

INSERT INTO `user` (`username`, `password`, `level`) VALUES
('admin', '123', 'admin'),
('mahasiswa', 'mahasiswa', 'mahasiswa'),
('petugas', 'petugas', 'petugas');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `kelas`
--
ALTER TABLE `kelas`
 ADD PRIMARY KEY (`id_kelas`);

--
-- Indexes for table `konsentrasi`
--
ALTER TABLE `konsentrasi`
 ADD PRIMARY KEY (`id_konsentrasi`);

--
-- Indexes for table `mahasiswa`
--
ALTER TABLE `mahasiswa`
 ADD PRIMARY KEY (`nim`);

--
-- Indexes for table `matkul`
--
ALTER TABLE `matkul`
 ADD PRIMARY KEY (`id_matkul`);

--
-- Indexes for table `prodi`
--
ALTER TABLE `prodi`
 ADD PRIMARY KEY (`id_prodi`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
 ADD PRIMARY KEY (`username`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
