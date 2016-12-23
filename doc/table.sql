create table t_unit(fid int, fname varchar(256), fremark varchar(256), primary key(fid));
create table t_type_res(fid int, fname varchar(256), fremark varchar(256), fbean varchar(256), primary key(fid));
create table t_res(fid int, fname varchar(256), ftype int, fremark varchar(256), famount int, funit int, fstart timestamp null default null, fend timestamp null default null, fcontent varchar(256),
	primary key(fid),
	foreign key (ftype) references t_type_res(fid),
	foreign key (funit) references t_unit(fid));
create table t_resItem(fid int, fname varchar(256),fparent int, fremark varchar(256), famount int, funit int, fstart timestamp null default null, fend timestamp null default null, fcontent varchar(256),
	primary key(fid),
	foreign key (fparent) references t_res(fid),
	foreign key (funit) references t_unit(fid));

create table tfile(fid BIGINT, fparent BIGINT, fname varchar(2000), ftype int, fsize BIGINT, fstatus int,
	primary key(fid));

create table t_type_task(fid int, fname varchar(256), fremark varchar(256), fbean varchar(256), primary key(fid));
create table t_task(fid int, fparentId int, fname varchar(256), fremark varchar(256), ftype int,fcontent varchar(256), fimportant int,
	furgency int, fstart timestamp null default null, fend timestamp null default null,fstatus int, fprogress FLOAT,
	primary key(fid));
create table t_taskRes(ftaskid int,fresid int, fremark varchar(256),
	primary key(ftaskid, fresid));
create table t_plan(fid int, ftaskid int, fstart timestamp not null, fend timestamp not null,
	primary key (fid));
create table t_planRes(fplanId int, fresItemId int,
	primary key (fplanId,fresItemId));

create table t_history();
create table t_system(fid int, fname varchar(256), fvalue varchar(256));

create table t_guest();

-----------------
insert into t_system values(0, 'version', 'v0.1');
