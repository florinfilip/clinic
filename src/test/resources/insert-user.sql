-- Create pacients
INSERT INTO patients (cnp, DATE_OF_BIRTH, first_name, last_name, phone_number)
VALUES ('1900312419660', TO_DATE('1999-06-06', 'YYYY-MM-DD'), 'firstName', 'lastName', '0742341231');

INSERT INTO patients (cnp, DATE_OF_BIRTH, first_name, last_name, phone_number)
VALUES ('1990542080042', TO_DATE('1999-07-06', 'YYYY-MM-DD'), 'Popescu', 'Ion', '0712312323');

INSERT INTO patients (cnp, DATE_OF_BIRTH, first_name, last_name, phone_number)
VALUES ('1990412180042', TO_DATE('1999-02-06', 'YYYY-MM-DD'), 'John', 'Doe', '0123123231');

-- Create Users

INSERT INTO users (password, phone_number, email, enabled, patient_id)
VALUES ('$2a$12$Jx..SF4G6P2V9ML.8cDE3.BPHOrVvEx62i5lGxjyFJx6gfLn7kZaa', '0742341231', 'test@email.com', 1, 1);

INSERT INTO users (password, phone_number, email, enabled, patient_id)
VALUES ('password', '0712312323', 'test2@email.com', 1, 2);

INSERT INTO users (password, phone_number, email, enabled, patient_id)
VALUES ('password', '0123123231', 'test3@email.com', 1, 3);


-- Create user-role relations


INSERT INTO role (name)
VALUES ('USER');

INSERT INTO user_role (user_id, role_id)
VALUES (1, 1);

INSERT INTO user_role (user_id, role_id)
VALUES (2, 1);

INSERT INTO user_role (user_id, role_id)
VALUES (3, 1);
