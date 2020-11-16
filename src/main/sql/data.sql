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