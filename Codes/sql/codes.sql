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
-- Creates the tables supporting the codes framework.
--

--
-- Category table.
--
CREATE TABLE bws_category
(
   category_id	INTEGER			NOT NULL,
   name					VARCHAR(20)	NOT NULL,
   is_deleted		CHAR(1)			NOT NULL,
   domain_id    INTEGER			NOT NULL
)
;

ALTER TABLE bws_category ADD CONSTRAINT pk_category
   PRIMARY KEY (category_id)
;

CREATE INDEX idx_category_1 ON bws_category (domain_id);

--
-- Code table.
--
CREATE TABLE bws_code
(
   code_id			INTEGER			NOT NULL,
   name 				VARCHAR(50)	NOT NULL,
   is_default		CHAR(1)			NOT NULL,
   category_id	INTEGER			NOT NULL,
   version			INTEGER			NOT NULL,
   is_deleted		CHAR(1)			NOT NULL,
   domain_id    INTEGER			NOT NULL
)
;

ALTER TABLE bws_code ADD CONSTRAINT pk_code
   PRIMARY KEY (code_id)
;

ALTER TABLE bws_code ADD CONSTRAINT fk_code_1
   FOREIGN KEY (category_id) REFERENCES bws_category (category_id)
;

CREATE INDEX idx_code_1 ON bws_code (domain_id);
