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
-- Creates the tables supporting the core framework.
--
CREATE TABLE bws_id_lookup (
   table_name		VARCHAR(30)	NOT NULL,
   column_name	VARCHAR(20)	NOT NULL,
   max_value		INTEGER			NOT NULL
)
;

ALTER TABLE bws_id_lookup ADD CONSTRAINT pk_id_lookup
   PRIMARY KEY (table_name, column_name)
;
