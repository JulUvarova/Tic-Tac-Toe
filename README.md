# Project Backend 05 — Java_Bootcamp

**Summary:** In this project, you will learn how to work with JWT authorization and expand the capabilities of Java-based web applications using Spring.

💡 *[Click here](https://new.oprosso.net/p/4cb31ec3f47a4596bc758ea1861fb624) to share your feedback on this project.* It’s anonymous and will help our team improve the training. We recommend completing the survey right after finishing the project.

## Contents

  - [Chapter I](#chapter-i)
    - [Instructions](#instructions)
  - [Chapter II](#chapter-ii)
    - [General Information](#general-information)
      - [Token, Session Token, Refresh Token](#token-session-token-refresh-token)
  - [Chapter III](#chapter-iii)
    - [Project: Tic-Tac-Toe](#project-tic-tac-toe)
    - [Task 1. Switching Basic Authorization to JWT](#task-1-switching-basic-authorization-to-jwt)
    - [Task 2. Adding Support for Game History](#task-2-adding-support-for-game-history)
    - [Task 3. Adding Leaderboard Support](#task-3-adding-leaderboard-support)

## Chapter I
### Instructions

1. Throughout the course, you will experience uncertainty and a severe lack of information — this is normal. Remember that the repository and Google are always available to you, as are your peers and Rocket.Chat. Communicate. Search. Rely on common sense. Do not be afraid of making mistakes.
2. Pay attention to sources of information. Verify, think, analyze, compare.
3. Read the assignments carefully. Reread them several times.
4. It’s best to read the examples carefully as well. They may contain something not explicitly stated in the assignment itself.
5. You might encounter inconsistencies when something new in the task or example contradicts what you already know. If that happens, try to figure it out. If you fail, make a note under “open questions” and resolve it during your work. Do not leave open questions unresolved.
6. If a task seems unclear or unachievable, it only seems that way. Try decomposing it. Most likely, individual parts will become clearer.
7. Along the way, you’ll encounter many different tasks. Those marked with an asterisk (\*) are for more meticulous learners. They are of higher complexity and are not mandatory, but if you do them, you’ll gain additional experience and knowledge.
8. Do not try to fool the system or those around you. You’ll only be fooling yourself.
9. Have a question? Ask the neighbor on your right. If that doesn’t help, ask the neighbor on your left.
10. When using someone’s help, always make sure you understand why, how, and what for. Otherwise, that help is meaningless.
11. Always push only to the **develop** branch! The **master** branch will be ignored. Work in the **src** directory.
12. Your directory should not contain any files other than those specified in the tasks.

## Chapter II
### General Information

#### Token, Session Token, Refresh Token

A **token** is a unique string of characters that replaces the user's login and password, preventing leaks of confidential information. Tokens have a specific lifespan and stop working once they expire.

A **session token** gives users the right to perform available actions during their session. It is reusable and has a short lifespan.

A **refresh token** extends the validity of the session token. It is single-use and has a long lifespan.

**Topics to study:**

- Web application;
- JWT authorization;
- PostgreSQL;
- Spring.

## Chapter III

### Project: Tic-Tac-Toe

Use the backend project from the previous week (T04).

### Task 1. Switching Basic Authorization to JWT

- Add a role enumeration with a single **USER** role.
- Implement role support at all levels.
- Extend GrantedAuthority for the domain model of the role.
- Give users a list of roles.
- Create a JwtRequest model that includes a login and password.
- Create a JwtResponse model that includes a type, accessToken, and refreshToken.
- Create a RefreshJwtRequest model that includes refreshToken.
- Implement a JwtProvider class with the following methods:
  - use io.jsonwebtoken.Jwts.builder() to generate tokens;
  - a method to generate an **accessToken** from a User, storing information about the UUID and role in the token's claims (information about the object is stored in claims for future use);
  - a method of generating a **refreshToken** from a User, storing information about the UUID in the token's claims;
  - a method to validate the accessToken;
  - a method to validate the refreshToken;
  - a method to retrieve claims.
- Create a JwtAuthentication model that extends Authentication:
  - for getAuthorities(), return the roles;
  - for getPrincipal(), return the UUID;
  - for getName(), return the UUID;
  - for authentication status, add a separate field.
- Implement a JwtUtil class with a method to create JwtAuthentication from claims.
- Update the authorization service that uses the UserService, JwtProvider, and SecurityContextHolder to implement the following methods:
  - Modify the authorization method so that it now takes a JwtRequest and returns a JwtResponse.
  - Create a method to refresh the **accessToken** that takes a refreshToken and returns a JwtResponse.
  - Create a method to refresh the **refreshToken**, which also takes a refreshToken and returns a JwtResponse.
  - Create a method to get JwtAuthentication.
- Update the authorization controller by adding or modifying endpoints:
  - for user authorization;
  - for updating the **accessToken**;
  - for updating the **refreshToken**.
- Modify the behavior of the doFilter method in the AuthFilter class:
  - Retrieve the token from the Authorization header, which contains "Bearer {accessToken}".
  - Validate the token using the JwtProvider.
  - Get claims using JwtProvider.
  - Use JwtUtil to create JwtAuthentication from the claims.
  - Set up authentication using SecurityContextHolder.
- In the Spring Configuration, allow unauthenticated access to the endpoint to update the **accessToken**.
- Add an endpoint to retrieve user information from the **accessToken**.

### Task 2. Adding Support for Game History

- Add a creation date to the game model.
- Describe a database query to get all completed games by user's UUID.
- A game is considered completed when it has one of the following states:
  - The player with the UUID won;
  - A draw.
- Add a method to the game service to get all completed games by the user's UUID.
- Add an endpoint to get all completed games by **accessToken**, accessible only to authenticated users.

### Task 3. Adding Leaderboard Support

- Create a model for information about games won that includes the user's UUID and win ratio.
- Describe a database query in which:
  - Retrieve the ratio of the number of games won to the number of losses and draws for each user.
  - Sort by the win ratio in descending order.
  - Select the top N records, each of which contains the user's UUID and their win ratio.
- In the game service, add a method to get the top N best players.
- Add an endpoint to get the top N best players that takes N (the number of top players) and returns a list of the best players (UUID and login) with their win ratios.
- The endpoint to get the best players must be accessible only to authenticated users.

