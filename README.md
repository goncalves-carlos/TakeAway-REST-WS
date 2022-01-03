# Exercise 4

## Video Recording
Video is stored in my Google Drive
link: https://drive.google.com/file/d/1ocxX3rVBwEAe8j52WUJz0dSA9s90oVh2/view?usp=sharing

## MongoDB
username: root
password: example
database: TakeAway
host: localhost
port: 27017

Collection: Dishes, Order, Users

## Application

This application first start with the login. we have to add the credentials that was manually created in mongodb. We have
one user which is the client: username: carlos07 password: qwertz. And a second user which is the owner: username: owner1
password: asdfgh.

If we are an owner than we will be sent to the html "/dishes" which where the owner can create, delete or update a dish.

If we are a client than we will be sent to the html "/dishesClient" which is where the client sees the dishes and can add
dishes to a cart and place an order. to place an order we have to choose a dish and then click on checkout. we will be
sent to a new page where we will have the order's id, customer's id, the total price and number of products bought. To
check out we need to fill the boxes which are used for the address. We have country, city, street and number. If we submit
we will be sent back to the "/dishesClient" page and can place a new order.

## Technical Information

This project has three packages. Controller, Model and Repository. In the directory templates I have all the html files
that are used for this project.

In the login we check if the credentials are correct. If so then we look at the userType of the user to see if it is a client
or an owner. Depending on the type the user will be sent to its correct page.

When we log in as a client, we first check if there is an order that is still not finished. If there is one then we will
use it and reset all the information that were previously stored such as dishes_id, price or the customer_id. If there is
no order then we create a new one. After that we will be sent to the next page.

When an order has been finalized then we will be sent back to the clients main page. If we send a dish to the cart then
a new order we will be created.

The order does not only as a list of the dishes' id but a dish_id which is the last dish that has been added.

For the Java classes I used lombok, so I can use the annotations for the getter and setter.

## Use

To use the program we first need to start the docker-compose with "docker-compose up -d" and the use the command
"mvn spring-boot:run".
Then we have to go the browser and type: "localhost:8080/login".
To use as a client we have: username: carlos07 password: qwertz
To use as an owner we have: username: owner1 password: asdfgh

If there are no users, then we have to manually add them.
For the client we type the following query in the mongodb shell:
db.Users.insertOne({userName: "carlos07", password: "qwertz", userType: "client"})

For the owner we type the following query in the mongodb shell:
db.Users.insertOne({userName: "owner1", password: "asdfgh", userType: "owner"})