SET FOREIGN_KEY_CHECKS = 0; 

TRUNCATE TABLE customers;
INSERT INTO customers (Company,adress)
VALUES('Bjudkakor AB','Ystad' ),('Finkakor AB','Helsingborg' ),('Gästkakor AB','Hässleholm' ),('Kaffebröd AB','Landskrona' )
,('Kalaskakor AB','Trelleborg' ),('Partykakor AB','Kristianstad' ),('Skånekakor AB','Perstorp' ),('Småbröd AB','Malmö' );


TRUNCATE TABLE cookies;
INSERT INTO cookies (cookieName)
VALUES('Almond delight' ),('Amneris' ),('Berliner' ),('Nut cookie' ),('Nut ring' ),('Tango');


TRUNCATE TABLE inventory;
INSERT INTO inventory (ingredient,quantity,unit)
VALUES('Bread crumbs', 500000,'g'),('Butter', 500000,'g'),('Chocolate', 500000,'g'),('Chopped almonds', 500000,'g'),
('Cinnamon', 500000,'g'),('Egg whites', 500000,'ml'),('Eggs', 500000,'g')
,('Fine-ground nuts', 500000,'g'),('Flour', 500000,'g'),('Ground, roasted nuts', 500000,'g'),('Icing sugar', 500000,'g'),('Marzipan', 500000,'g')
,('Potato starch', 500000,'g'),('Roasted, chopped nuts', 500000,'g')
,('Sodium bicarbonate', 500000,'g'),('Sugar', 500000,'g'),('Vanilla sugar', 500000,'g'),('Vanilla', 500000,'g'),('Wheat flour', 500000,'g');


TRUNCATE TABLE recipes;
INSERT INTO recipes (cookieName,ingredient,quantity,unit)
VALUES((SELECT * from cookies where cookieName = 'Almond delight'),(SELECT ingredient from inventory where ingredient = 'Butter'),400,'g' ),
((SELECT * from cookies where cookieName = 'Almond delight'),(SELECT ingredient from inventory where ingredient = 'Chopped almonds'),279,'g' ),
((SELECT * from cookies where cookieName = 'Almond delight'),(SELECT ingredient from inventory where ingredient = 'Cinnamon'),10,'g' ),
((SELECT * from cookies where cookieName = 'Almond delight'),(SELECT ingredient from inventory where ingredient = 'Flour'),400,'g' ),
((SELECT * from cookies where cookieName = 'Almond delight'),(SELECT ingredient from inventory where ingredient = 'Sugar'),400,'g' ),

((SELECT * from cookies where cookieName = 'Amneris'),(SELECT ingredient from inventory where ingredient = 'Butter'),250,'g' ),
((SELECT * from cookies where cookieName = 'Amneris'),(SELECT ingredient from inventory where ingredient = 'Eggs'),50,'g' ),
((SELECT * from cookies where cookieName = 'Amneris'),(SELECT ingredient from inventory where ingredient = 'Marzipan'),750,'g' ),
((SELECT * from cookies where cookieName = 'Amneris'),(SELECT ingredient from inventory where ingredient = 'Potato starch'),25,'g' ),
((SELECT * from cookies where cookieName = 'Amneris'),(SELECT ingredient from inventory where ingredient = 'Wheat flour'),100,'g' ),

((SELECT * from cookies where cookieName = 'Berliner'),(SELECT ingredient from inventory where ingredient = 'Butter'),250,'g' ),
((SELECT * from cookies where cookieName = 'Berliner'),(SELECT ingredient from inventory where ingredient = 'Chocolate'),50,'g' ), 
((SELECT * from cookies where cookieName = 'Berliner'),(SELECT ingredient from inventory where ingredient = 'Eggs'),50,'g' ), 
((SELECT * from cookies where cookieName = 'Berliner'),(SELECT ingredient from inventory where ingredient = 'Flour'),350,'g' ), 
((SELECT * from cookies where cookieName = 'Berliner'),(SELECT ingredient from inventory where ingredient = 'Icing sugar'),100,'g' ), 
((SELECT * from cookies where cookieName = 'Berliner'),(SELECT ingredient from inventory where ingredient = 'Vanilla sugar'),5,'g' ),

((SELECT * from cookies where cookieName = 'Nut cookie'),(SELECT ingredient from inventory where ingredient = 'Bread crumbs'),125,'g' ), 
((SELECT * from cookies where cookieName = 'Nut cookie'),(SELECT ingredient from inventory where ingredient = 'Chocolate'),50,'g' ), 
((SELECT * from cookies where cookieName = 'Nut cookie'),(SELECT ingredient from inventory where ingredient = 'Egg whites'),350,'ml' ), 
((SELECT * from cookies where cookieName = 'Nut cookie'),(SELECT ingredient from inventory where ingredient = 'Fine-ground nuts'),750,'g' ), 
((SELECT * from cookies where cookieName = 'Nut cookie'),(SELECT ingredient from inventory where ingredient = 'Ground, roasted nuts'),625,'g' ), 
((SELECT * from cookies where cookieName = 'Nut cookie'),(SELECT ingredient from inventory where ingredient = 'Sugar'),375,'g' ), 

((SELECT * from cookies where cookieName = 'Nut ring'),(SELECT ingredient from inventory where ingredient = 'Butter'),450,'g' ),
((SELECT * from cookies where cookieName = 'Nut ring'),(SELECT ingredient from inventory where ingredient = 'Flour'),450,'g' ), 
((SELECT * from cookies where cookieName = 'Nut ring'),(SELECT ingredient from inventory where ingredient = 'Icing sugar'),190,'g' ), 
((SELECT * from cookies where cookieName = 'Nut ring'),(SELECT ingredient from inventory where ingredient = 'Roasted, chopped nuts'),225,'g' ), 

((SELECT * from cookies where cookieName = 'Tango'),(SELECT ingredient from inventory where ingredient = 'Butter'),200,'g' ), 
((SELECT * from cookies where cookieName = 'Tango'),(SELECT ingredient from inventory where ingredient = 'Flour'),300,'g' ), 
((SELECT * from cookies where cookieName = 'Tango'),(SELECT ingredient from inventory where ingredient = 'Sodium bicarbonate'),4,'g' ), 
((SELECT * from cookies where cookieName = 'Tango'),(SELECT ingredient from inventory where ingredient = 'Sugar'),250,'g' ), 
((SELECT * from cookies where cookieName = 'Tango'),(SELECT ingredient from inventory where ingredient = 'Vanilla'),2,'g' );


TRUNCATE TABLE cookieOrders;
TRUNCATE TABLE pallets;

SET FOREIGN_KEY_CHECKS = 1;
