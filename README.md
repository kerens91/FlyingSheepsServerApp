This file contains a concise explanation about the application, along with a short review of the main components of the application.

![imageedit_158_7735667754](https://user-images.githubusercontent.com/23153754/144446587-5b455cb1-7615-47ad-8865-6e7b89565cb6.png)


Please note that the code in this Git is the server side implementation of the application.

In addition to this file, you may also find other reference documents in the "Addition" folder:
1. About the application - description and instruction of the game.
2. Class Diagram - application class diagram.
3. Cards Diagram - a class diagram of the cards component.
4. Attack States - visual representation of the game attack states.
5. Database - visual representation of the tables in the database.





A short demo video of the app can be found in the link:
ADD


#############################################################
# FlyingSheepsServerApp 
Java code implementing the server side of the 'Flying Sheeps' application.
#############################################################

This application is a Maven project written in Java using Eclipse IDE. 
This code constitutes the server side of a multi-client server card game application.

The communication is done through Sockets. 
The socket handling is implemented in the 'serverConnections' package.
The SocketHandler class is implementing the Runnable interface, when run() is called, a new socket is created, running in a while loop listening to incoming requests. 
Once a request is accepted, a new Thread is created to handle the client connection. 
The messages sent between the server and the client are defined in the ‘message’ package, as a Message object, consisting of a message type and a list of parameters.
The ‘MsgTypeEnum’ defines the messages types, while the ‘MessageConvertor’ and ‘MessageCreator’ suggest APIs for the messages creation.
The messages are sent over the socket using the google Gson library. 

The game logic is implemented in the ‘game’ package.
The ‘game’ class is the core of the game, responsible for the game operations, it also makes use of other classes services,
each of which is responsible for a different part of the game:
playersManager - manages the players in game
cardsManager - manages the cards in the game
attackGenerator, attackHandler, attackResolver - manages the attacks in the game
gameNotifier - defines the game events sent to the game manager

The attacks are implemented using a FSM model, as defined in the ‘attackState’ package  (for more details see the ‘attack states’’ attached in the ‘additions’ folder).
The turns are handled by an implementation of a DoublyLinkedList, enabling circular turns mechanism, and skipping players when no longer active.

The cards implementation is OOP based, meaning an abstract card class is defined, and all other cards are inheriting from it, categorized by card type.
In order to understand the card types division, you can see the ‘cards class diagram’ attached to the project (in the ‘additions’ folder).
The cards definition and implementation is under the ‘card’ package. 
The cards data is held in a database, implemented under the ‘database’ package.
I am using MySQL database along with JDBC connector handled in the ‘DriverSQL’ class.
The information needed to access the database is defined in the ‘persistence’ XML file. 
I used Hibernate for the object-relational mapping, the tables are mapped to Entities, defined in the ‘entity’ sub-package,
there you can see three tables - cards, cardsStrings and cardsDecorations.
The cards table is using card id as foreign key to the other tables.

Finally, the GameManager class is a singleton class created once, when the application starts running, 
and is responsible for getting events from all other app components as mentioned above.
The game manager is implementing event interfaces, as defined in the ‘eventnotifications’ package. 



#############################################################
# FlyingSheepsClientApp 
Java code implementing the client side of the 'Flying Sheeps' application.
#############################################################

This application is an Android project written in Java using Android Studio. 
This code constitutes the client side of a multi-client server card game application.

The clients will run on an android device, displaying the visual representation of the game, while the logic of the game is handled on the server.
All clients will get real time updates from the server during the game.

The application activities are defined in the activities package.
The first activity presented when starting the app is the MainActivity, that represents the main screen with the user information (name and image).
The buttons in this screen depend on whether the user is registered.
The RegisterActivity handles user registration and profile editing, and the Game activity is handling the game representation.
Once the game is over the FinishGameActivity is presented with an image indicating the result.

The user authentication is done by using Firebase authentication provider.
Once registering for the first time, the user details are saved in Firebase SharedPreferences,
thus the user is not required to insert his details the next time he starts the app.
The user can also edit his profile, which allows him to upload an image to the FirebaseStorage,
thus the image can be shared with other users of the application. 

The communication with the server is done through Sockets. 
The socket handling is implemented in the 'connectionservice' package.
The ConnectionService class is a Bound Service that runs in the background when bound to the main activity,
when run() is called, a new socket is created, running in a while loop listening to incoming requests.

The UI and the service communicate using the MVVM architecture.
The UI observes the server messages, which updates for each incoming request by the service.
The ConnectionServiceViewModel is the view-model holding the server message as a string mutable data, and also uses PriorityBlockingQueue to manage the multiple requests.
The messages sent between the server and the client (as mentioned in the server description part) are defined in the ‘message’ package,
as a Message object, consisting of a message type and a list of parameters.
The ‘MsgTypeEnum’ defines the messages types, while the ‘MessageConvertor’ and ‘MessageCreator’ suggest APIs for the messages creation. 
The messages are sent over the socket using the google Gson library.

The cards and the players in the game are handled in the ‘activityadapters’ package,
defined by a ViewModel, and represented to the screen using RecyclerView adapters.
The CardViewModel represents the hand of cards list, extends the ViewModel
and enables the constant modification of the cards list by calling the adapters getValue and postValue methods.
The images in the application are created by Sarai Yahel, using Adobe Illustrator.


#############################################################

The code is still in work, but in a constant improvement :) 
Hope you will enjoy reading my code. 

#############################################################

