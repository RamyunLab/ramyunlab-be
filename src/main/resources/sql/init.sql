create database ramyun default character set utf8mb4 default collate utf8mb4_general_ci;
use ramyun;

CREATE USER 'sesac'@'%' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON *.* TO 'sesac'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;
select host, user, plugin, authentication_string from mysql.user;
ALTER USER 'sesac'@'%' IDENTIFIED WITH mysql_native_password BY '1234';

create table user (
	u_idx bigint primary key not null auto_increment,
	u_id varchar(20) not null,
	u_nickname varchar(30) not null,
    u_password varchar(255) not null,
    u_deleted_at TIMESTAMP null,
    u_is_admin tinyint(1) not null
);

