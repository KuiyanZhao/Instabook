### README

[TOC]

#### 1. Instructions on how to compile and run this project.

> For phase 1, we have designed database tables, interfaces, and test cases.
>
> 1. Database Tables
>
>    We have designed the following tables to implement various functionalities:
>
>    - Message Table: Send and receive messages
>    - User Table: Register, login, and search for friends
>    - User_application Table: Apply to add friends and reply to friend requests
>    - User_relationship Table: Record friend relationships and set friend status and block relationships. 
>    - The specific $SQL$ files can be found in $instabook.sql$. You can run this file using database visualization software such as $Navicat$ to view and execute the operations.
>
> 2. Interface Design
>
>    Specific design diagram or file as follows $instabook.drawio$
>
>    ![image-20240327224549401](C:\Users\86183\AppData\Roaming\Typora\typora-user-images\image-20240327224549401.png)
>
>    The functional flowchart is as follows.
>
> ![Flowchart](C:\Users\86183\Desktop\类似wechat\Flowchart.png)
>
> 3. Test Cases
>
>      | Test Case ID | Functionality | Case Name  | Case Step Description                                        | Expected Result                 |
>      | ------------ | ------------- | ---------- | ------------------------------------------------------------ | ------------------------------- |
>      | 1            | register      | register_1 | The user entered "$username1$" as the $username$ and "password" as the password. Click on the register button | successful                      |
>      |              | register      | register_2 | The user entered an existing $username$, "$username1$," and "password" as the password. Click on the register button. | return user exist               |
>      | 1            | login         | login_1    | The user is not registered. Please enter a non-existent $username$: $username2$ and password and click on the login button. | return  user not exist          |
>      | 2            | login         | login_2    | The user has registered and entered "$username1$" as the $username$ and "password" as the password used during registration. Click on the login button to access the system. | return a token                  |
>      | 3            | login         | login_3    | The user entered an existing account $username1$, but the password  as the pass provided does not match | return token error              |
>      |              | login         | login_4    | The user entered an incorrect $username$, "$username2$," and entered correct "password" as the password. | return user not exist           |
>      |              | message       | message_1  | User sends a message after logging out                       | return token error              |
>      |              | message       | message_2  | The user entered the correct $username$, "$username1$," and the correct password, "password," but selected a user who is not their friend to send a message to. | return he isn't your friend yet |
>      |              | message       | message_3  | The user enters the correct $username$: "$username1$" and the correct password: "password", and requests to add the user with the $username$ "$username2$" as a friend | successful                      |
>      |              | message       | message_4  | The user enters the correct $username$: "$username1$" and the correct password: "password". They request to add the user with the $username$ "$username2$" as a friend. Once the friend with the $username$ "$username2$" successfully logs in, they agree to the friend request. | successful                      |
>      |              | message       | message_5  | The user enters the correct $username$: "$username1$" and the correct password: "password". They request to add the user with the $username$ "$username2$" as a friend. Once the friend with the $username$ "$username2$" successfully logs in and agrees to the friend request, the user "$username1$" sends a message "hello world" to the user with the $username$ "$username2$". | successful                      |
>      |              | message       | message_6  | The user enters the correct $username$: "$username1$" and the correct password: "password". They request to add the user with the $username$ "$username2$" as a friend. Once the friend with the $username$ "$username2$" successfully logs in and agrees to the friend request, the user "$username1$" sends a message "hello world" to the user with the $username$ "$username2$". After that, user "$username2$" checks the message. | successful                      |
>      |              |               |            |                                                              |                                 |
>      |              |               |            |                                                              |                                 |
>      
>      
>
> 

#### 2. the list of who submitted which parts of the assignment on $Brightspace$ and $Vocareum$.
> Student 1 - Submitted Report on Brightspace. Student 2 - Submitted Vocareum workspace.（待提交时修改）

#### 3. A detailed description of each class. 

> The function of each class is as follows: 
>
> Each database object corresponds to a controller, service, and mapper respectively. Their relationships and functions are shown in the following diagram or file $relationUML.drawio$.
>
> ![image-20240327225827087](C:\Users\86183\AppData\Roaming\Typora\typora-user-images\image-20240327225827087.png)