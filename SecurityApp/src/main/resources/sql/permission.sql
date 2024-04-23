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
-- Security configuration for Codes.
--
insert into bws_task (task_id, name)
  select coalesce(max(task_id), 0) + 1, 'Manage Codes' from bws_task
;

insert into bws_action (action_id, secure_id, task_id) 
  select 
    a.action_id, 
    'CDA1',
    b.task_id
  from
    (select coalesce(max(action_id), 0) + 1 as action_id from bws_action) a, 
    (select task_id from bws_task where name = 'Manage Codes') b
;

insert into bws_action (action_id, secure_id, task_id) 
  select 
    a.action_id, 
    'CDA2',
    b.task_id
  from
    (select coalesce(max(action_id), 0) + 1 as action_id from bws_action) a, 
    (select task_id from bws_task where name = 'Manage Codes') b
;

insert into bws_action (action_id, secure_id, task_id) 
  select 
    a.action_id, 
    'CDA3',
    b.task_id
  from
    (select coalesce(max(action_id), 0) + 1 as action_id from bws_action) a, 
    (select task_id from bws_task where name = 'Manage Codes') b
;

insert into bws_action (action_id, secure_id, task_id) 
  select 
    a.action_id, 
    'CDA4',
    b.task_id
  from
    (select coalesce(max(action_id), 0) + 1 as action_id from bws_action) a, 
    (select task_id from bws_task where name = 'Manage Codes') b
;

insert into bws_action (action_id, secure_id, task_id) 
  select 
    a.action_id, 
    'CDA5',
    b.task_id
  from
    (select coalesce(max(action_id), 0) + 1 as action_id from bws_action) a, 
    (select task_id from bws_task where name = 'Manage Codes') b
;

insert into bws_action (action_id, secure_id, task_id) 
  select 
    a.action_id, 
    'CDA6',
    b.task_id
  from
    (select coalesce(max(action_id), 0) + 1 as action_id from bws_action) a, 
    (select task_id from bws_task where name = 'Manage Codes') b
;

insert into bws_field_group (group_id, name)
  select coalesce(max(group_id), 0) + 1, 'Code' from bws_field_group
;

insert into bws_field (field_id, name, secure_id, group_id)
  select 
    a.field_id,
    'Name',
    'CDF4',
    b.group_id
  from
    (select coalesce(max(field_id), 0) + 1 as field_id from bws_field) a,
    (select group_id from bws_field_group where name = 'Code') b
;

insert into bws_field (field_id, name, secure_id, group_id)
  select 
    a.field_id,
    'Is System',
    'CDF5',
    b.group_id
  from
    (select coalesce(max(field_id), 0) + 1 as field_id from bws_field) a,
    (select group_id from bws_field_group where name = 'Code') b
;

insert into bws_field (field_id, name, secure_id, group_id)
  select 
    a.field_id,
    'Is Default',
    'CDF6',
    b.group_id
  from
    (select coalesce(max(field_id), 0) + 1 as field_id from bws_field) a,
    (select group_id from bws_field_group where name = 'Code') b
;

insert into bws_field (field_id, name, secure_id, group_id)
  select 
    a.field_id,
    'Sequence',
    'CDF7',
    b.group_id
  from
    (select coalesce(max(field_id), 0) + 1 as field_id from bws_field) a,
    (select group_id from bws_field_group where name = 'Code') b
;


--
-- Security configuration for Roles.
--
insert into bws_task (task_id, name)
  select coalesce(max(task_id), 0) + 1, 'Manage Roles' from bws_task
;

insert into bws_action (action_id, secure_id, task_id) 
  select 
    a.action_id, 
    'SCA6',
    b.task_id
  from
    (select coalesce(max(action_id), 0) + 1 as action_id from bws_action) a, 
    (select task_id from bws_task where name = 'Manage Roles') b
;

insert into bws_action (action_id, secure_id, task_id) 
  select 
    a.action_id, 
    'SCA7',
    b.task_id
  from
    (select coalesce(max(action_id), 0) + 1 as action_id from bws_action) a, 
    (select task_id from bws_task where name = 'Manage Roles') b
;

insert into bws_action (action_id, secure_id, task_id) 
  select 
    a.action_id, 
    'SCA8',
    b.task_id
  from
    (select coalesce(max(action_id), 0) + 1 as action_id from bws_action) a, 
    (select task_id from bws_task where name = 'Manage Roles') b
;

insert into bws_action (action_id, secure_id, task_id) 
  select 
    a.action_id, 
    'SCA9',
    b.task_id
  from
    (select coalesce(max(action_id), 0) + 1 as action_id from bws_action) a, 
    (select task_id from bws_task where name = 'Manage Roles') b
;

insert into bws_action (action_id, secure_id, task_id) 
  select 
    a.action_id, 
    'SCA10',
    b.task_id
  from
    (select coalesce(max(action_id), 0) + 1 as action_id from bws_action) a, 
    (select task_id from bws_task where name = 'Manage Roles') b
;

insert into bws_action (action_id, secure_id, task_id) 
  select 
    a.action_id, 
    'SCA11',
    b.task_id
  from
    (select coalesce(max(action_id), 0) + 1 as action_id from bws_action) a, 
    (select task_id from bws_task where name = 'Manage Roles') b
;

insert into bws_action (action_id, secure_id, task_id) 
  select 
    a.action_id, 
    'SCA12',
    b.task_id
  from
    (select coalesce(max(action_id), 0) + 1 as action_id from bws_action) a, 
    (select task_id from bws_task where name = 'Manage Roles') b
;

insert into bws_field_group (group_id, name)
  select coalesce(max(group_id), 0) + 1, 'Role' from bws_field_group
;

insert into bws_field (field_id, name, secure_id, group_id)
  select 
    a.field_id,
    'Name',
    'SCF9',
    b.group_id
  from
    (select coalesce(max(field_id), 0) + 1 as field_id from bws_field) a,
    (select group_id from bws_field_group where name = 'Role') b
;

insert into bws_field (field_id, name, secure_id, group_id)
  select 
    a.field_id,
    'Restricted Tasks',
    'SCF10',
    b.group_id
  from
    (select coalesce(max(field_id), 0) + 1 as field_id from bws_field) a,
    (select group_id from bws_field_group where name = 'Role') b
;

insert into bws_field (field_id, name, secure_id, group_id)
  select 
    a.field_id,
    'Restricted Fields',
    'SCF11',
    b.group_id
  from
    (select coalesce(max(field_id), 0) + 1 as field_id from bws_field) a,
    (select group_id from bws_field_group where name = 'Role') b
;

insert into bws_field (field_id, name, secure_id, group_id)
  select 
    a.field_id,
    'Read Only Fields',
    'SCF12',
    b.group_id
  from
    (select coalesce(max(field_id), 0) + 1 as field_id from bws_field) a,
    (select group_id from bws_field_group where name = 'Role') b
;


--
-- Security configuration for Users.
--
insert into bws_task (task_id, name)
  select coalesce(max(task_id), 0) + 1, 'Manage Users' from bws_task
;

insert into bws_action (action_id, secure_id, task_id) 
  select 
    a.action_id, 
    'SCA13',
    b.task_id
  from
    (select coalesce(max(action_id), 0) + 1 as action_id from bws_action) a, 
    (select task_id from bws_task where name = 'Manage Users') b
;

insert into bws_action (action_id, secure_id, task_id) 
  select 
    a.action_id, 
    'SCA14',
    b.task_id
  from
    (select coalesce(max(action_id), 0) + 1 as action_id from bws_action) a, 
    (select task_id from bws_task where name = 'Manage Users') b
;

insert into bws_action (action_id, secure_id, task_id) 
  select 
    a.action_id, 
    'SCA15',
    b.task_id
  from
    (select coalesce(max(action_id), 0) + 1 as action_id from bws_action) a, 
    (select task_id from bws_task where name = 'Manage Users') b
;

insert into bws_action (action_id, secure_id, task_id) 
  select 
    a.action_id, 
    'SCA16',
    b.task_id
  from
    (select coalesce(max(action_id), 0) + 1 as action_id from bws_action) a, 
    (select task_id from bws_task where name = 'Manage Users') b
;

insert into bws_action (action_id, secure_id, task_id) 
  select 
    a.action_id, 
    'SCA17',
    b.task_id
  from
    (select coalesce(max(action_id), 0) + 1 as action_id from bws_action) a, 
    (select task_id from bws_task where name = 'Manage Users') b
;

insert into bws_action (action_id, secure_id, task_id) 
  select 
    a.action_id, 
    'SCA18',
    b.task_id
  from
    (select coalesce(max(action_id), 0) + 1 as action_id from bws_action) a, 
    (select task_id from bws_task where name = 'Manage Users') b
;

insert into bws_action (action_id, secure_id, task_id) 
  select 
    a.action_id, 
    'SCA19',
    b.task_id
  from
    (select coalesce(max(action_id), 0) + 1 as action_id from bws_action) a, 
    (select task_id from bws_task where name = 'Manage Users') b
;

insert into bws_action (action_id, secure_id, task_id) 
  select 
    a.action_id, 
    'SCA20',
    b.task_id
  from
    (select coalesce(max(action_id), 0) + 1 as action_id from bws_action) a, 
    (select task_id from bws_task where name = 'Manage Users') b
;

insert into bws_action (action_id, secure_id, task_id) 
  select 
    a.action_id, 
    'SCA21',
    b.task_id
  from
    (select coalesce(max(action_id), 0) + 1 as action_id from bws_action) a, 
    (select task_id from bws_task where name = 'Manage Users') b
;

insert into bws_field_group (group_id, name)
  select coalesce(max(group_id), 0) + 1, 'User' from bws_field_group
;

insert into bws_field (field_id, name, secure_id, group_id)
  select 
    a.field_id,
    'First Name',
    'SCF17',
    b.group_id
  from
    (select coalesce(max(field_id), 0) + 1 as field_id from bws_field) a,
    (select group_id from bws_field_group where name = 'User') b
;

insert into bws_field (field_id, name, secure_id, group_id)
  select 
    a.field_id,
    'Middle Initial',
    'SCF18',
    b.group_id
  from
    (select coalesce(max(field_id), 0) + 1 as field_id from bws_field) a,
    (select group_id from bws_field_group where name = 'User') b
;

insert into bws_field (field_id, name, secure_id, group_id)
  select 
    a.field_id,
    'Last Name',
    'SCF19',
    b.group_id
  from
    (select coalesce(max(field_id), 0) + 1 as field_id from bws_field) a,
    (select group_id from bws_field_group where name = 'User') b
;

insert into bws_field (field_id, name, secure_id, group_id)
  select 
    a.field_id,
    'Logon ID',
    'SCF21',
    b.group_id
  from
    (select coalesce(max(field_id), 0) + 1 as field_id from bws_field) a,
    (select group_id from bws_field_group where name = 'User') b
;

insert into bws_field (field_id, name, secure_id, group_id)
  select 
    a.field_id,
    'Roles',
    'SCF22',
    b.group_id
  from
    (select coalesce(max(field_id), 0) + 1 as field_id from bws_field) a,
    (select group_id from bws_field_group where name = 'User') b
;


--
-- Administrative user and associated role
--
insert into bws_id_lookup values ('bws_user', 'user_id', 1);

insert into bws_id_lookup values ('bws_role', 'role_id', 1);

insert into bws_user values (1, 'admin', 'yt9n1+eaIeTwJeqwWTFaXQ==', 'ochRLLNNPQvr484xcXEhew==', 'admin', null, 'admin', '1', '0', 1, 0);

insert into bws_role values (1, 'admin', '0', 1, 0);

insert into bws_user_role values (1, 1);

