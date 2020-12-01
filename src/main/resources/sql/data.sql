-- psql -U dbadmin -d playme

INSERT INTO roles VALUES('c6495d8c-0dcf-44a1-a2a6-a27494dcc454', 'user');
INSERT INTO roles VALUES('cfdafb34-5c90-49bf-a3db-bd909ac6e692', 'administrator');

INSERT INTO statuses VALUES('e2d487d3-7e4a-4024-8f0d-40af28a929be', 'active');
INSERT INTO statuses VALUES('4c7d6691-5675-4eef-9f7a-1d944c968489', 'pending');
INSERT INTO statuses VALUES('54379e58-1ad4-4b0f-a66a-aeb8e8108e55', 'disabled');
INSERT INTO statuses VALUES('85d50ad9-2fd0-4bef-88f4-2385e00953c1', 'deleted');

INSERT INTO skills VALUES('42bbb050-d1f2-4e89-884a-1febb3eeec18', 'classical guitar');
INSERT INTO skills VALUES('7e8e571a-c95e-4ef4-b83d-08f96ae52b85', 'acoustic guitar');
INSERT INTO skills VALUES('dfe681ce-3a5e-4337-9e59-0a4c8fdc0771', 'bass guitar');
INSERT INTO skills VALUES('560ef018-a2d8-4628-b594-f39e77907d59', 'vocal');
INSERT INTO skills VALUES('2d4012c8-fd17-447b-ae0d-a03bebc16ce0', 'piano');

INSERT INTO users VALUES('bea6b180-d4f9-4bc9-8c42-dcf1832ea731', 'MainDeveloper', 'faf552345a40bc30f11ef0874e0e92db3fb9c6f701cc287017d846e64f35d2d1', 'anthony.bulhakov@gmail.com', TO_DATE('2000-01-05', 'YYYY-MM-DD'), 'Anton', 'Bulhakov', 'cfdafb34-5c90-49bf-a3db-bd909ac6e692', 'e2d487d3-7e4a-4024-8f0d-40af28a929be', NOW(), null);
INSERT INTO users VALUES('0cdf4caf-a70a-4654-a942-bb19f9805a6e', 'Lindemann', 'b1da7d186594bfc9466c629df0059b6eaf02ebb0ebe72ae9109530aa568438c6', 'till.lindemann@gmail.com', TO_DATE('1963-01-04', 'YYYY-MM-DD'), 'Till', 'Lindemann', 'c6495d8c-0dcf-44a1-a2a6-a27494dcc454', 'e2d487d3-7e4a-4024-8f0d-40af28a929be', NOW(), null);