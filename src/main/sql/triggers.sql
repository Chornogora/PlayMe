CREATE OR REPLACE FUNCTION CHANGE_MODIFICATION_DATE_FUNCTION()
  RETURNS trigger AS
$$
BEGIN
    new.modification_date = NOW();
    RETURN NEW;
END;
$$
LANGUAGE 'plpgsql';

CREATE TRIGGER CHANGE_MODIFICATION_DATE_TRIGGER
BEFORE UPDATE OR INSERT ON users
FOR EACH ROW
EXECUTE PROCEDURE CHANGE_MODIFICATION_DATE_FUNCTION();
