DROP trigger user_signup;
delimiter //
CREATE TRIGGER user_signup BEFORE INSERT ON REGISTERED_USER
FOR EACH ROW
BEGIN
	IF NEW.Name is null THEN
		SET NEW.Name = NEW.Username;
	END IF;
    IF NEW.Email is null THEN
		SET NEW.Email = CONCAT(New.Username,"@gmail.com");
	END IF;
	IF NEW.Address is null THEN
		SET NEW.Address = "United States";
    END IF;
END;//
delimiter ;