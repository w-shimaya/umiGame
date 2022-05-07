set foreign_key_checks = 0;
drop table if exists participant;
drop table if exists message;
drop table if exists guess;
drop table if exists clarification;
drop table if exists question;
set foreign_key_checks = 1;

create table question
(
    question_id int auto_increment,
    message_id varchar(32) not null,
    statement varchar(256) not null,
    answered boolean,
    answer varchar(256),
    constraint question_pk primary key(question_id)
);

create table clarification
(
  clarification_id int auto_increment,
  question_id int not null,
  message_id varchar(32) not null,
  state varchar(8) default "AWAIT",
  content varchar(256) not null,
  constraint clarification_pk primary key(clarification_id),
  constraint cl_q_fk foreign key(question_id) references question(question_id)
);

create table guess
(
  guess_id int auto_increment,
  question_id int not null,
  message_id varchar(32) not null,
  content varchar(256) not null,
  is_answered boolean not null default false,
  is_correct boolean not null default false,
  constraint guess_pk primary key(guess_id),
  constraint g_q_fk foreign key(question_id) references question(question_id)
);

create table participant
(
  user_id varchar(32),
  user_name varchar(64),
  constraint participant_pk primary key(user_id)
);

create table message
(
  message_id varchar(32),
  author_id varchar(32),
  channel_id varchar(32),
  created_at datetime not null,
  constraint message_pk primary key(message_id),
  constraint m_a_fk foreign key(author_id) references participant(user_id)
);
