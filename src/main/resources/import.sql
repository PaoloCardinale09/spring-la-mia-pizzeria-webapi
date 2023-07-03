-- Pizzas
INSERT INTO `pizzas` (`id`, `price`, `description`, `name`, `pic_url`, `created_at`) VALUES (NULL, '6.00', 'Is a typical Neapolitan pizza, made with San Marzano tomatoes, mozzarella cheese, fresh basil, salt, and extra-virgin olive oil.', 'Pizza Margherita', 'https://www.finedininglovers.it/sites/g/files/xknfdk1106/files/styles/recipes_1200_800_fallback/public/fdl_content_import_it/margherita-50kalo.jpg?itok=v9nHxNMS', '2023-06-23 15:28:00'), (NULL, '7.00', 'Traditional Pizza From Italy, Western ...\r\nDiavola is a variety of Italian pizza that is traditionally topped with tomato sauce, mozzarella cheese, spicy salami, and hot chili peppers. Black olives are optional and can be added for extra flavor.', 'Pizza Diavola', 'https://i1.wp.com/www.piccolericette.net/piccolericette/wp-content/uploads/2017/06/3236_Pizza.jpg?resize=895%2C616&ssl=1', '2023-06-23 15:28:00'), (NULL, '8.00', 'Pepperoni pizza is an American pizza variety which includes one of the country\'s most beloved toppings.', 'Pepperoni pizza', 'https://media-assets.lacucinaitaliana.it/photos/620fbf8588f5a214a3ad3e4b/3:2/w_1500,h_1000,c_limit/pizza-con-peperoni-olive-e-capperi.jpg', '2023-06-23 15:28:00')
--Special offers
INSERT INTO `special_offers` (`expiry_date`, `id`, `pizza_id`, `start_date`, `title`) VALUES ('2023-07-06', NULL, '1', '2023-06-29', 'The Margherita Weekly'), ('2023-07-03', NULL, '2', '2023-06-30', 'The Devil Week End'), ('2023-06-30', NULL, '3', '2023-06-29', 'The day of pepperoni')
-- Ingredients
INSERT INTO `ingredients` (`id`, `name`, `description`) VALUES (NULL, 'Mozzarella', 'A southern Italian cheese traditionally made from Italian buffalo\'s milk by the pasta filata method.'), (NULL, 'Sauce', 'Is traditionally made from tomato, and can be additionally seasoned with wide variety of spices and herbs. '), (NULL, 'Basil', 'Is a culinary herb of the family Lamiaceae (mints). ')

INSERT INTO `ingredient_pizza` (`ingredient_id`, `pizza_id`) VALUES ('1', '1'), ('2','1'),('3','1');


-- user
INSERT INTO `roles` (`id`, `name`) VALUES (NULL, 'ADMIN'), (NULL, 'USER');

INSERT INTO `users` (`id`, `email`, `first_name`, `last_name`, `password`) VALUES (NULL, 'mariorossi@hotmail.it', 'mario', 'rossi', '{noop}ok'), (NULL, 'claudianeri@gmail.com', 'claudia', 'neri', '{noop}ok');