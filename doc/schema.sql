/*
createdb -U postgres clog
psql -U postgres -c "create user clog with password 'clog';"
psql -U postgres -c "alter database clog owner to clog;"
psql -U postgres -d clog -c 'create extension "uuid-ossp";'

psql -U clog -d clog -f doc/schema.sql
*/

drop table if exists users cascade;
drop table if exists blog cascade;
drop table if exists ring_session cascade;


create table ring_session (
       id varchar(25) not null primary key,
       session_data varchar(1024),
       session_timestamp timestamp not null
);

/*
   Any user of the system.
   user_type = Client, Contractor
   parent_id - If null, this is the boss.  If not null, then points to the boss.
   customer_id - always points to the paying customer account.  The billing email is on crm_customer.
*/
create table users (
       id varchar(25) not null primary key,
       fname varchar(50) not null,
       lname varchar(50) not null,
       email varchar(50) not null,
       password varchar(50) not null,
       create_dt timestamp without time zone default now(),
       delete_dt timestamp without time zone
);
alter table users add constraint user_email unique (email);

create table blog (
       id varchar(25) not null primary key,
       title varchar(100) not null,
       description varchar(200) not null,
       keywords varchar(200) not null,
       content text not null,
       users_id varchar(25) not null,
       create_dt timestamp without time zone default now(),
       delete_dt timestamp without time zone
);

alter table blog add foreign key (users_id) references users;
