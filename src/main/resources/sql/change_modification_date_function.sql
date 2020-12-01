CREATE OR REPLACE FUNCTION CHANGE_MODIFICATION_DATE_FUNCTION()
    RETURNS trigger AS
$$
BEGIN
    new.modification_date = NOW();
    RETURN NEW;
END;
$$
    LANGUAGE 'plpgsql';