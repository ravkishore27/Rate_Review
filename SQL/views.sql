SELECT * FROM Restaurant_Search;
DROP VIEW IF exists Restaurant_Search;
CREATE VIEW Restaurant_Search AS
SELECT C.*, R.Cuisine AS Special, P.Promo_Message, P.Expiration_Date  FROM CATEGORIES AS C
INNER JOIN RESTAURANT AS R ON C.Category_Id = R.Restaurant_Id 
INNER JOIN PROMOTION AS P ON C.Category_Id = P.Category_Id ;

SELECT * FROM Shopping_Search;
DROP VIEW IF exists Shopping_Search;
CREATE VIEW Shopping_Search AS
SELECT C.*, S.Working_Hrs AS Special, P.Promo_Message, P.Expiration_Date  FROM CATEGORIES AS C
INNER JOIN SHOPPING AS S ON C.Category_Id = S.Shopping_Id
INNER JOIN PROMOTION AS P ON C.Category_Id = P.Category_Id;


SELECT * FROM Hotel_Travel_Search ;
DROP VIEW IF exists Hotel_Travel_Search;
CREATE VIEW Hotel_Travel_Search AS
SELECT C.*, HT.Star AS Special, P.Promo_Message, P.Expiration_Date FROM CATEGORIES AS C
INNER JOIN HOTEL_TRAVEL AS HT ON C.Category_Id = HT.Hotel_Travel_Id
INNER JOIN PROMOTION AS P ON C.Category_Id = P.Category_Id;



CREATE VIEW  Users_Rate_And_Review AS
SELECT registered_user.Username, rate.Rate, review.Review, rate.Date_Time FROM rate INNER JOIN review 
ON rate.Review_Id = review.Review_Id INNER JOIN 
	registered_user ON registered_user.User_Id = rate.Reg_Id;


SELECT RU.Username, R.Rate, RE.Review, R.Date_Time FROM RATE AS R
INNER JOIN REVIEW AS RE ON R.Review_Id = RE.Review_Id
INNER JOIN REGISTERED_USER AS RU ON RU.User_Id = R.Reg_Id;

