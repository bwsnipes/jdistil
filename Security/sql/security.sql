--
-- Copyright (C) 2015 Bryan W. Snipes
-- 
-- This file is part of the JDistil web application framework.
-- 
-- JDistil is free software: you can redistribute it and/or modify
-- it under the terms of the GNU Lesser General Public License as published by
-- the Free Software Foundation, either version 3 of the License, or
-- (at your option) any later version.
-- 
-- JDistil is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
-- GNU Lesser General Public License for more details.
-- 
-- You should have received a copy of the GNU Lesser General Public License
-- along with JDistil.  If not, see <http://www.gnu.org/licenses/>.
-- 

--
-- Creates the tables supporting the security framework.
--

--
-- Domain table.
--
create table bws_domain 
(
	domain_id                INTEGER     NOT NULL,
	name                     VARCHAR(30) NOT NULL,
	is_default_datasource    CHAR(1)     NOT NULL,
	datasource_name          VARCHAR(30) NULL,
	is_deleted               CHAR(1)     NOT NULL,
	version                  INTEGER     NOT NULL
)
;

ALTER TABLE bws_domain ADD CONSTRAINT pk_domain
   PRIMARY KEY (domain_id)
;

insert into bws_id_lookup (table_name, column_name, max_value) values ('bws_domain', 'domain_id', 1);

--
-- User table.
--
CREATE TABLE bws_user
(
   user_id				  INTEGER			NOT NULL,
   logon_id				  VARCHAR(20)	NOT NULL,
   salt						  VARCHAR(30)	NOT NULL,
   password				  VARCHAR(30)	NOT NULL,
   first_name			  VARCHAR(20)	NOT NULL,
   middle_initial	  CHAR(1)			NULL,
   last_name			  VARCHAR(20)	NOT NULL,
   is_domain_admin  CHAR(1)			NOT NULL,
   is_deleted			  CHAR(1)			NOT NULL,
   version				  INTEGER			NOT NULL,
   domain_id        INTEGER			NOT NULL
)
;

ALTER TABLE bws_user ADD CONSTRAINT pk_user
   PRIMARY KEY (user_id)
;

CREATE INDEX idx_user_1 ON bws_user (domain_id);

insert into bws_id_lookup (table_name, column_name, max_value) values ('bws_user', 'user_id', 1);

--
-- Role table.
--
CREATE TABLE bws_role
(
   role_id	    INTEGER		  NOT NULL,
   name		      VARCHAR(20)	NOT NULL,
   is_deleted   CHAR(1)     NOT NULL,
   version	    INTEGER		  NOT NULL,
   domain_id    INTEGER			NOT NULL
)
;

ALTER TABLE bws_role ADD CONSTRAINT pk_role
   PRIMARY KEY (role_id)
;

CREATE INDEX idx_role_1 ON bws_role (domain_id);

insert into bws_id_lookup (table_name, column_name, max_value) values ('bws_role', 'role_id', 1);

--
-- User role assignment table.
--
CREATE TABLE bws_user_role
(
   user_id	INTEGER		NOT NULL,
   role_id	INTEGER		NOT NULL
)
;

ALTER TABLE bws_user_role ADD CONSTRAINT pk_user_role 
   PRIMARY KEY (user_id, role_id)
;

ALTER TABLE bws_user_role ADD CONSTRAINT fk_user_role_1
   FOREIGN KEY (user_id) REFERENCES bws_user (user_id)
;

ALTER TABLE bws_user_role ADD CONSTRAINT fk_user_role_2
   FOREIGN KEY (role_id) REFERENCES bws_role (role_id)
;

--
-- Task table.
--
CREATE TABLE bws_task
(
   task_id	    INTEGER			NOT NULL,
   name			    VARCHAR(40)	NOT NULL
)
;

ALTER TABLE bws_task ADD CONSTRAINT pk_task
   PRIMARY KEY (task_id)
;

--
-- Action table.
--
CREATE TABLE bws_action
(
   action_id    INTEGER			NOT NULL,
   secure_id    VARCHAR(15)	NOT NULL,
   task_id		  INTEGER			NOT NULL
)
;

ALTER TABLE bws_action ADD CONSTRAINT pk_action
   PRIMARY KEY (action_id)
;

ALTER TABLE bws_action ADD CONSTRAINT fk_action_1
   FOREIGN KEY (task_id) REFERENCES bws_task (task_id)
;

--
-- Restricted task table.
--
CREATE TABLE bws_restricted_task
(
   role_id	INTEGER		NOT NULL,
   task_id	INTEGER		NOT NULL
)
;

ALTER TABLE bws_restricted_task ADD CONSTRAINT pk_restricted_task
   PRIMARY KEY (role_id, task_id)
;

ALTER TABLE bws_restricted_task ADD CONSTRAINT fk_restricted_task_1
   FOREIGN KEY (role_id) REFERENCES bws_role (role_id)
;

ALTER TABLE bws_restricted_task ADD CONSTRAINT fk_restricted_task_2
   FOREIGN KEY (task_id) REFERENCES bws_task (task_id)
;

--
-- Field group table.
--
CREATE TABLE bws_field_group
(
   group_id	    INTEGER			NOT NULL,
   name			    VARCHAR(30)	NOT NULL
)
;

ALTER TABLE bws_field_group ADD CONSTRAINT pk_field_group
   PRIMARY KEY (group_id)
;

--
-- Field table.
--
CREATE TABLE bws_field
(
   field_id	    INTEGER			NOT NULL,
   name			    VARCHAR(30)	NOT NULL,
   secure_id    VARCHAR(15) NOT NULL,
   group_id	    INTEGER			NOT NULL
)
;

ALTER TABLE bws_field ADD CONSTRAINT pk_field
   PRIMARY KEY (field_id)
;

ALTER TABLE bws_field ADD CONSTRAINT fk_field_1
   FOREIGN KEY (group_id) REFERENCES bws_field_group (group_id)
;

--
-- Read only field table.
--
CREATE TABLE bws_read_only_field
(
   role_id	INTEGER		NOT NULL,
   field_id	INTEGER		NOT NULL
)
;

ALTER TABLE bws_read_only_field ADD CONSTRAINT pk_read_only_field
   PRIMARY KEY (role_id, field_id)
;

ALTER TABLE bws_read_only_field ADD CONSTRAINT fk_read_only_field_1
   FOREIGN KEY (role_id) REFERENCES bws_role (role_id)
;

ALTER TABLE bws_read_only_field ADD CONSTRAINT fk_read_only_field_2
   FOREIGN KEY (field_id) REFERENCES bws_field (field_id)
;

--
-- Restricted field table.
--
CREATE TABLE bws_restricted_field
(
   role_id	INTEGER		NOT NULL,
   field_id	INTEGER		NOT NULL
)
;

ALTER TABLE bws_restricted_field ADD CONSTRAINT pk_restricted_field
   PRIMARY KEY (role_id, field_id)
;

ALTER TABLE bws_restricted_field ADD CONSTRAINT fk_restricted_field_1
   FOREIGN KEY (role_id) REFERENCES bws_role (role_id)
;

ALTER TABLE bws_restricted_field ADD CONSTRAINT fk_restricted_field_2
   FOREIGN KEY (field_id) REFERENCES bws_field (field_id)
;
