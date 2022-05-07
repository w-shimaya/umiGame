ALTER DATABASE umigamedb CHARACTER SET utf8mb4;

SET foreign_key_checks = 0;
DROP TABLE IF EXISTS participant;
DROP TABLE IF EXISTS message;
DROP TABLE IF EXISTS guess;
DROP TABLE IF EXISTS clarification;
DROP TABLE IF EXISTS question;
SET foreign_key_checks = 1;

CREATE TABLE question
(
    question_id INT AUTO_INCREMENT,
    message_id VARCHAR(32) NOT NULL,
    statement VARCHAR(256) NOT NULL,
    answered BOOLEAN,
    answer VARCHAR(256),
    CONSTRAINT question_pk PRIMARY KEY(question_id)
);

CREATE TABLE clarification
(
  clarification_id INT AUTO_INCREMENT,
  question_id INT NOT NULL,
  message_id VARCHAR(32) NOT NULL,
  state VARCHAR(8) default 'AWAIT',
  content VARCHAR(256) NOT NULL,
  CONSTRAINT clarification_pk PRIMARY KEY(clarification_id),
  CONSTRAINT cl_q_fk FOREIGN KEY(question_id) REFERENCES question(question_id)
);

CREATE TABLE guess
(
  guess_id INT AUTO_INCREMENT,
  question_id INT NOT NULL,
  message_id VARCHAR(32) NOT NULL,
  content VARCHAR(256) NOT NULL,
  is_answered BOOLEAN NOT NULL DEFAULT FALSE,
  is_correct BOOLEAN NOT NULL DEFAULT FALSE,
  CONSTRAINT guess_pk PRIMARY KEY(guess_id),
  CONSTRAINT g_q_fk FOREIGN KEY(question_id) REFERENCES question(question_id)
);

CREATE TABLE participant
(
  user_id VARCHAR(32),
  user_name VARCHAR(64),
  CONSTRAINT participant_pk PRIMARY KEY(user_id)
);

CREATE TABLE message
(
  message_id VARCHAR(32),
  author_id VARCHAR(32),
  channel_id VARCHAR(32),
  created_at DATETIME NOT NULL,
  CONSTRAINT message_pk PRIMARY KEY(message_id),
  CONSTRAINT m_a_fk FOREIGN KEY(author_id) REFERENCES participant(user_id)
);
