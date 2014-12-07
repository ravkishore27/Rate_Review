
DROP PROCEDURE IF EXISTS yelp.categories_insert;
DELIMITER //
CREATE PROCEDURE yelp.`categories_insert`(
IN category_name  CHAR(20),
IN type int,
IN address CHAR(20),
IN contact_number INT
)
BEGIN
	IF type = 1 THEN
		INSERT INTO CATEGORIES(Category_Name, Category_Type, Address, Contact_Number)
		VALUES (category_name , "RESTAURANT" , address, contact_number);
	END IF;
	IF type = 2 THEN
		INSERT INTO CATEGORIES(Category_Name, Category_Type, Address, Contact_Number)
		VALUES (category_name , "SHOPPING" , address, contact_number);
	END IF;
	IF type = 3 THEN
		INSERT INTO CATEGORIES(Category_Name, Category_Type, Address, Contact_Number)
		VALUES (category_name , "HOTEL_TRAVEL" , address, contact_number);
	END IF;
END;//
DELIMITER ;

DROP PROCEDURE IF EXISTS yelp.subcategories_insert;
DELIMITER //
CREATE PROCEDURE yelp.`subcategories_insert`(
IN subcategory  CHAR(200),
IN category_id INT,
IN special_attribute CHAR(200)
)
begin
IF subcategory="restaurant_insert" THEN
	INSERT INTO RESTAURANT VALUES(category_id, special_attribute);
END IF;
IF subcategory="shopping_insert" THEN
	INSERT INTO SHOPPING VALUES(category_id, special_attribute);
END IF;
IF subcategory="hotel_travel_insert" THEN
	INSERT INTO HOTEL_TRAVEL VALUES(category_id, special_attribute);
END IF;

END;//
DELIMITER ;

DROP PROCEDURE IF EXISTS yelp.categories_delete;
DELIMITER //
CREATE PROCEDURE yelp.`categories_delete`(
IN id INT
)
BEGIN
	DELETE FROM CATEGORIES WHERE Category_Id = id;
END;//
DELIMITER ;

DROP PROCEDURE IF EXISTS yelp.categories_update;
DELIMITER //
CREATE PROCEDURE yelp.`categories_update`(
IN id INT,
IN category_name  CHAR(200),
IN address CHAR(200),
IN contact_number INT,
IN subcategory char(20),
IN special_attribute char(50)
)
BEGIN
	UPDATE CATEGORIES
    SET Category_Name = category_name, Address = address, Contact_Number = contact_number
	WHERE Category_Id = id;

	IF subcategory="restaurant_insert" THEN
		UPDATE RESTAURANT SET Cuisine = special_attribute where Restaurant_Id = id;
	END IF;
	IF subcategory="shopping_insert" THEN
		UPDATE SHOPPING SET Working_Hrs = special_attribute where Shopping_Id = id;
	END IF;
	IF subcategory="hotel_travel_insert" THEN
		UPDATE HOTEL_TRAVEL SET Star = special_attribute where Hotel_Travel_Id = id;
	END IF;
END;//
DELIMITER ;

