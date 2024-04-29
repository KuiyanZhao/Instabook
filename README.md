### README

[TOC]

#### 1. Instructions on how to compile and run this project.

> **How to compile and run the backend project**: Please ensure that you have Maven installed. You can verify this by entering 'mvn -version' in the command line. If there are still errors in the pom.xml, you can enter 'mvn clean' in the terminal, followed by 'mvn install'. Also, make sure you have configured the corresponding jdk version for the project, which can be done in the 'project structure' menu item.
>
> **How to compile and run the client:** it's also Need jdk 17 and run maven build.Simply run `MainApplication.main`. For ease of testing, you can start another client by running `MainApplication2.main` again. (Note: `MainApplication2` is solely for debugging purposes; this means that the system supports multiple logins.).The instructions for operating the `client operation.docx` 
>
> tips:All the images mentioned in the readme can be found in the file `pngs` and If the GUI is up and running, you should be able to use the entire system directly since we have already deployed the backend on the cloud.
>
> **Currently, the system's functionality list is as follows:**
>
> - Register and login
>
> - Upload avatar
>
> + View own profile
>
> + View others' profiles
>
> + Search users
>
> + Add friends
>
> + Block users
>
> + Unblock users
>
> - Remove friends
>
> + View friends list
>
> - Send friend requests
>
> - Accept or reject friend requests
>
> - Query recent chats
>
> - Query message records
>
> + Upload and send images
>
> + Receive image messages
>
> + Send text messages
>
> + Receive text messages
>
> + Supporting simultaneous login from multiple devices
>
> Phase 3 further refined the $backend$ code and test cases based on Phase 1 and Phase2, and completed the design and coding of the GUI.
>
> So far, we have completed the following tasks in Phase 3:
>
> 1. Database Tables
>
>    We have designed the following tables to implement various functionalities:
>
>    - Message Table: Send and receive messages
>   - User Table: Register, login, and search for friends
>    - User_application Table: Apply to add friends and reply to friend requests
>   - User_relationship Table: Record friend relationships and set friend status and block relationships. 
>    - The specific $SQL$ files can be found in $instabook.sql$. You can run this file using database visualization software such as $Navicat$ to view and execute the operations.
>   - To implement the functions required for phase 2, we have made some modifications to the database. For specific details, please refer to the document `sql update.docx`.
>   - The final database code is available in `instabook.sql`.
>
>    **The database code written is as follows:**
>
>    + **All $.java$ files in the directory Instabook\src\main\java\com\instabook\mapper.**
>    + **All .xml files in the directory Instabook\src\main\resources\mappers.**
>
> 2. Interface Design and Coding
>
>   Specific design diagram or file as follows $instabook.drawio$
>
>    ![image-20240327224549401](https://chat-bucket-demo.oss-cn-shanghai.aliyuncs.com/3.png)
>
>    The functional flowchart is as follows.
>
> ![Flowchart](https://chat-bucket-demo.oss-cn-shanghai.aliyuncs.com/2.png)
>
> All server code is provided below:
>
> + **Instabook\src\main\java\\***
>
> 3. Test Cases
>
>      In Phase 1, we wrote the following test cases and conducted corresponding code testing using $Junit$ unit testing
>
>      | Test Case ID | Functionality | Case Name       | Case Step Description                                        | Expected Result                 |
>     | ------------ | ------------- | --------------- | ------------------------------------------------------------ | ------------------------------- |
>      | 1            | register      | register_1      | The user entered "$username1$" as the $username$ and "password" as the password. Click on the register button | successful                      |
>     | 2            | register      | register_2      | The user entered an existing $username$, "$username1$," and "password" as the password. Click on the register button. | return user exist               |
>      | 3            | login         | login_1         | The user is not registered. Please enter a non-existent $username$: $username2$ and password and click on the login button. | return  user not exist          |
>     | 4            | login         | login_2         | The user has registered and entered "$username1$" as the $username$ and "password" as the password used during registration. Click on the login button to access the system. | return a token                  |
>      | 5            | login         | login_3         | The user entered an existing account $username1$, but the password  as the pass provided does not match | return token error              |
>      | 6            | login         | login_4         | The user entered an incorrect $username$, "$username2$," and entered correct "password" as the password. | return user not exist           |
>      | 7            | message       | message_1       | User sends a message after logging out                       | return token error              |
>      | 8            | message       | message_2       | The user entered the correct $username$, "$username1$," and the correct password, "password," but selected a user who is not their friend to send a message to. | return he isn't your friend yet |
>      | 9            | message       | message_3       | The user enters the correct $username$: "$username1$" and the correct password: "password", and requests to add the user with the $username$ "$username2$" as a friend | successful                      |
>      | 10           | message       | message_4       | The user enters the correct $username$: "$username1$" and the correct password: "password". They request to add the user with the $username$ "$username2$" as a friend. Once the friend with the $username$ "$username2$" successfully logs in, they agree to the friend request. | successful                      |
>      | 11           | message       | message_5       | The user enters the correct $username$: "$username1$" and the correct password: "password". They request to add the user with the $username$ "$username2$" as a friend. Once the friend with the $username$ "$username2$" successfully logs in and agrees to the friend request, the user "$username1$" sends a message "hello world" to the user with the $username$ "$username2$". | successful                      |
>      | 12           | message       | message_6       | The user enters the correct $username$: "$username1$" and the correct password: "password". They request to add the user with the $username$ "$username2$" as a friend. Once the friend with the $username$ "$username2$" successfully logs in and agrees to the friend request, the user "$username1$" sends a message "hello world" to the user with the $username$ "$username2$". After that, user "$username2$" checks the message. | successful                      |
>      | 13           | message       | message_7       | The user enters the correct username: "username1" and the correct password: "password". They choose to block the user with the username "username2" as a friend. After that, the user with the username "username2" sends a message to the user with the username "username1". | return you are blocked          |
>      | 14           | image         | image_1         | The user enters the correct username: "username1" and the correct password: "password". They click on the option to upload their profile picture. | return a image link             |
>      | 15           | news          | news_1          | The user enters the correct username: "username1" and the correct password: "password". They proceed to publish a post in their social media feed with the content "hello world". | successful                      |
>      | 16           | news          | news_2          | After the user enters the correct $username$: "$username1$" and the correct password: "password", and publishes a post in their social media feed with the content "hello world", the user "$username2$" who has been blocked will not be able to see the content posted by "$username1$" in their Moments or $Timeline$. The blocking action restricts the visibility of posts and interactions between the two users. | successful                      |
>      | 17           | news          | news_3          | After the user enters the correct $username$: "$username1$" and the correct password: "password", and publishes a post in their social media feed with the content "hello world", users who are not friends with "$username1$" (such as "$username3$") will not be able to see the content posted in "$username1's$" Moments or $Timeline$. The visibility of posts in a user's social media feed is typically limited to their approved friends or contacts, ensuring privacy and control over who can view the content. | successful                      |
>      | 18           | news          | news_4          | After the user enters the correct $username$: "$username1$" and the correct password: "password", and publishes a post in their social media feed with the content "hello world" in their Moments or $Timeline$, if they subsequently unblock the user "$username2$", then "$username2$" will be able to see the content posted by "$username1$" in their own Moments or $Timeline$. | successful                      |
>      | 19           | $newsComment$ | $newsComment$_1 | After the user enters the correct $username$:"$username1$" and the correct password:"password"and publishes a post in their social media feed with the content "hello world" in their Moments or $Timeline$, one who is his and friend is not blocked can comment the moment under it. | successful                      |
>      | 20           | $newsComment$ | $newsComment$_2 | For $username3$, who initially couldn't see this moment, he needs to add $username1$ and $username2$ as friends first. Then, he can see this moment and the comment by $username2$ under this moment. After that, he can reply to $username2$'s comment under $username1$'s moment, saying "hello world." | successful                      |
>
>      All $JUnit$ tests are written in the following file:
>
>      + **Instabook\src\main\test\\*.java**
>
>      In Phase 2, we conducted testing on the $backend$ interfaces by initiating network I/O requests (HTTP) using Postman. Since the $backend$ interfaces involve corresponding read and write operations to the database, each class interface and database interface will be tested once again in this round of testing.you can find the detailed screenshots of all the Postman tests in the attached file `PostmanTest.zip` and The Postman test data can be found in the file named Instabook.postman_collection.
>
>      + $ConnectWebSocket$
>
>        ![image-20240414010735268](https://chat-bucket-demo.oss-cn-shanghai.aliyuncs.com/PostmanTest/ConnectWebSocket.png)
>
>      + $ReceiveAMessage$
>
>        ![image-20240414020953362](https://chat-bucket-demo.oss-cn-shanghai.aliyuncs.com/PostmanTest/ReceiveAMessage.png)
>
>      + $SendAImageMessage$
>
>        ![image-20240414020953362](https://chat-bucket-demo.oss-cn-shanghai.aliyuncs.com/PostmanTest/SendAImageMessage.png)
>
>      + $SendAMessage$
>
>        ![image-20240414020953362](https://chat-bucket-demo.oss-cn-shanghai.aliyuncs.com/PostmanTest/SendAMessage.png)
>
>      + $SendMessageToSomeoneNotFriend$
>
>        ![image-20240414020953362](https://chat-bucket-demo.oss-cn-shanghai.aliyuncs.com/PostmanTest/SendMessageToSomeoneNotFriend.png)
>
>      + $RegisterSuccess$![image-20240414020953362](https://chat-bucket-demo.oss-cn-shanghai.aliyuncs.com/PostmanTest/RegisterSuccess.png)
>
>      + UserExist
>
>        ![image-20240414020953362](https://chat-bucket-demo.oss-cn-shanghai.aliyuncs.com/PostmanTest/UserExist.png)
>
>      + $LoginSuccess$
>
>        ![image-20240414020953362](https://chat-bucket-demo.oss-cn-shanghai.aliyuncs.com/PostmanTest/LoginSuccess.png)
>
>      + UserNotExist
>
>        ![image-20240414020953362](https://chat-bucket-demo.oss-cn-shanghai.aliyuncs.com/PostmanTest/UserNotExist.png)
>
>      + $PasswordWrong$
>
>        ![image-20240414133028354](https://chat-bucket-demo.oss-cn-shanghai.aliyuncs.com/PostmanTest/PasswordWrong.png)
>
>      + Profile
>
>        ![image-20240414133028354](https://chat-bucket-demo.oss-cn-shanghai.aliyuncs.com/PostmanTest/Profile.png)
>
>      + UserSearch
>
>        ![image-20240414133028354](https://chat-bucket-demo.oss-cn-shanghai.aliyuncs.com/PostmanTest/UserSearch.png)
>
>      + $ApplyForAFriend$
>
>        ![image-20240414133028354](https://chat-bucket-demo.oss-cn-shanghai.aliyuncs.com/PostmanTest/ApplyForAFriend.png)
>
>      + $AlreadyFriends$
>
>        ![image-20240414133028354](https://chat-bucket-demo.oss-cn-shanghai.aliyuncs.com/PostmanTest/AlreadyFriends.png)
>
>      + $UserApplicationPage$
>
>        ![image-20240414133028354](https://chat-bucket-demo.oss-cn-shanghai.aliyuncs.com/PostmanTest/UserApplicationPage.png)
>
>      + $ApplicationNotExist$
>
>        ![image-20240414133028354](https://chat-bucket-demo.oss-cn-shanghai.aliyuncs.com/PostmanTest/ApplicationNotExist.png)
>
>      + $PassAUserApplication$
>
>        ![image-20240414133028354](https://chat-bucket-demo.oss-cn-shanghai.aliyuncs.com/PostmanTest/PassAUserApplication.png)
>
>      + $AlreadyReplied$
>
>        ![image-20240414133028354](https://chat-bucket-demo.oss-cn-shanghai.aliyuncs.com/PostmanTest/AlreadyReplied.png)
>
>      + $RefuseAUserApplication$
>
>        ![image-20240414133028354](https://chat-bucket-demo.oss-cn-shanghai.aliyuncs.com/PostmanTest/RefuseAUserApplication.png)
>
>      + $GetFriendList$
>
>        ![image-20240414133028354](https://chat-bucket-demo.oss-cn-shanghai.aliyuncs.com/PostmanTest/GetFriendList.png)
>
>      + UnblockUser
>
>        ![image-20240414133028354](https://chat-bucket-demo.oss-cn-shanghai.aliyuncs.com/PostmanTest/UnblockUser.png)
>
>      + $BlockUser$
>
>        ![image-20240414133028354](https://chat-bucket-demo.oss-cn-shanghai.aliyuncs.com/PostmanTest/BlockUser.png)
>
>      + $ChatPage$
>
>        ![image-20240414133028354](https://chat-bucket-demo.oss-cn-shanghai.aliyuncs.com/PostmanTest/ChatPage.png)
>
>      + $MessageRecord$
>
>        ![image-20240414133028354](https://chat-bucket-demo.oss-cn-shanghai.aliyuncs.com/PostmanTest/MessageRecord.png)
>
>      + UploadAnImageForSendingAMessage
>
>        ![image-20240414133028354](https://chat-bucket-demo.oss-cn-shanghai.aliyuncs.com/PostmanTest/UploadAnImageForSendingAMessage.png)
>

#### 2. the list of who submitted which parts of the assignment on $Brightspace$ and $Vocareum$.
> $Kuiyan Zhao$ - .Completed the refinement of the backend code and database and participated in writing the report..
>
> $Aarnav Bomma$ -.Completed writing the [readme.md ](http://readme.md/)and participated in writing the report.
>
>  Gavin Lindsay - The client-side code was correspondingly refined and participated in writing the report.

#### 3. A detailed description of each class. 

> The function of each class is as follows: 
>
> Each database object corresponds to a controller, service, and mapper respectively. Their relationships and functions are shown in the following diagram or file `relationUML.png`.
>
> ![image-20240327225827087](https://chat-bucket-demo.oss-cn-shanghai.aliyuncs.com/1.png)
>
> The frontend GUI's relationship diagram is shown below or can be found in the file `instabook-client-uml.png`
>
> ![image-20240327225827087](https://chat-bucket-demo.oss-cn-shanghai.aliyuncs.com/4d1eb5596f7f04f619042a491392b3a.png)
>
> 