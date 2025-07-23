# Project Backend 04 — Java_Bootcamp

**Summary:** In this project, you will learn how to add databases to Java web applications using Spring and work with authorization.

💡 *[Click here](https://new.oprosso.net/p/4cb31ec3f47a4596bc758ea1861fb624) to share your feedback on this project.* It’s anonymous and will help our team improve the training. We recommend filling out the survey right after completing the project.

## Contents**

1. **Chapter I**
   1. Instructions
1. **Chapter II**
   1. General Information
   1. Authorization
      1. Identification, Authentication, Authorization
      1. Authorization via Login and Password
1. **Chapter III**
   1. Task 1. Adding a Database
   1. Task 2. Adding Authorization
   1. Task 3. Adding Game Logic Between Two Players

## Chapter I

### Instructions

1. Throughout the course, you will experience uncertainty and a severe lack of information — this is normal. Remember that the repository and Google are always available to you, as are your peers and Rocket.Chat. Communicate. Search. Rely on common sense. Do not be afraid of making mistakes.
1. Pay attention to sources of information. Verify, think, analyze, compare.
1. Read the assignments carefully. Reread them several times.
1. It’s best to read the examples carefully as well. They may contain something not explicitly stated in the assignment itself.
1. You might encounter inconsistencies when something new in the task or example contradicts what you already know. If that happens, try to figure it out. If you fail, make a note under “open questions” and resolve it during your work. Do not leave open questions unresolved.
1. If a task seems unclear or unachievable, it only seems that way. Try decomposing it. Most likely, individual parts will become clearer.
1. Along the way, you’ll encounter many different tasks. Those marked with an asterisk (\*) are for more meticulous learners. They are of higher complexity and are not mandatory, but if you do them, you’ll gain additional experience and knowledge.
1. Do not try to fool the system or those around you. You’ll only be fooling yourself.
1. Have a question? Ask the neighbor on your right. If that doesn’t help, ask the neighbor on your left.
1. When using someone’s help, always make sure you understand why, how, and what for. Otherwise, that help is meaningless.
1. Always push only to the **develop** branch! The **master** branch will be ignored. Work in the **src** directory.
1. Your directory should not contain any files other than those specified in the tasks.

## Chapter II

### General Information

#### Authorization

Authorization controls the access of legitimate users to the system's resources, granting each user exactly those privileges assigned by the administrator.

#### Identification, Authentication, Authorization

- **Identification** is a process by which a subject's unique identifier is established, uniquely defining the subject in the information system.
- **Authentication** is the process of verifying authenticity. For example, verifying the user by comparing the password entered by the user with the password stored in the system.
- **Authorization** is the granting of rights to a specific person or group to perform a specific set of actions.

#### Authorization via Login and Password**

This method is based on the user providing a login and password for successful identification and authentication in the system. The login/password pair is specified by the user during registration. Upon successful authorization on the server, the user is granted the rights to perform the available requests.

The client sends a request to the server and receives an "Unauthorized" response with information about the authorization procedure. After successful authorization, each subsequent client request automatically includes an "Authorization" header (forming the authorization header), which carries the client's credentials for server authentication.

There are also other authorization methods.

**Topics to study**:

- Web application;
- Login-password (basic auth) authorization;
- PostgreSQL;
- ASP.NET.

## Chapter III

**Project: Tic-Tac-Toe**

Use the server-side project from the previous week (T03).

### Task 1. Adding a Database

- Describe the PostgreSQL database connection in application.properties.
- Eliminate the storage-class approach (i.e., get rid of the in-memory storage class).
- Add special annotations to the classes that need to be stored in the database.
- In your repositories, use CrudRepository as the parent interface.

### Task 2. Adding Authorization

- Add users, each with a UUID, login, and password.
- Provide user support across all layers.
- Create a SignUpRequest model that contains a login and password.
- Create an authorization service that uses UserService:
  - a registration method that takes a SignUpRequest and returns a registration success status;
  - an authorization method that takes the login and password in the header as base64(login:password) and returns the user's UUID.
- Create an authorization controller with the following endpoints:
  - for user registration,
  - for user authorization.
- Create a class AuthFilter extending GenericFilterBean and implement the doFilter method:
  - Validate the login and password.
  - If the validation is successful, proceed with the request.
  - If the validation fails, add the 401 status code to the response and do not proceed with the request.
- Create a Spring Configuration class where:
  - You define a Bean for obtaining SecurityFilterChain.
  - Allow access without authorization to the registration and authorization endpoints.
  - All other endpoints require authorization.
  - Use AuthFilter as a filter.

### Task 3. Adding Game Logic Between Two Players

- Add states for the current game:
  - Waiting for players;
  - Turn of the player with the UUID;
  - Draw;
  - Win by the player with the UUID.
- Add information about the tokens (X/O) that users will use in the current game.
- Improve the game-ending logic using these states.
- Add an endpoint to create a new game with a user or with a computer.
- Add an endpoint to get the available current games.
- Add an endpoint for a user to join a game.
- Enhance the endpoint for updating the current game to account for playing against another user or against the computer.
- Add an endpoint to get the current game.
- Add an endpoint to get user information by UUID.