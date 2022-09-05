insert into team(team_name) values ('team1');
insert into employee(name, team_no) select concat('user',1), team_no from team where team_name = 'team1';
insert into employee(name, team_no) select concat('user',2), team_no from team where team_name = 'team1';
insert into employee(name, team_no) select concat('user',3), team_no from team where team_name = 'team1';
insert into employee(name, team_no) select concat('user',4), team_no from team where team_name = 'team1';
insert into employee(name, team_no) select concat('user',5), team_no from team where team_name = 'team1';

insert into team(team_name) values ('team2');
insert into employee(name, team_no) select concat('user',6), team_no from team where team_name = 'team2';
insert into employee(name, team_no) select concat('user',7), team_no from team where team_name = 'team2';
insert into employee(name, team_no) select concat('user',8), team_no from team where team_name = 'team2';
insert into employee(name, team_no) select concat('user',9), team_no from team where team_name = 'team2';
insert into employee(name, team_no) select concat('user',10), team_no from team where team_name = 'team2';

insert into team(team_name) values ('team3');
insert into employee(name, team_no) select concat('user',11), team_no from team where team_name = 'team3';
insert into employee(name, team_no) select concat('user',12), team_no from team where team_name = 'team3';
insert into employee(name, team_no) select concat('user',13), team_no from team where team_name = 'team3';
insert into employee(name, team_no) select concat('user',14), team_no from team where team_name = 'team3';
insert into employee(name, team_no) select concat('user',15), team_no from team where team_name = 'team3';