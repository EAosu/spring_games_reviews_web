## Authors
• Name: Ely Asaf, Email:  elyas@edu.hac.ac.il <br>
• Name: Nadav Malul, Email: nadavma@edu.hac.ac.il <br>

## Description
<p>The game ranking website we developed is designed to provide users with a platform to search for and discover games based on their preferences.</p>
<p>The general idea of the website revolves around the following features:</p>
<ul>
  <li>
    <h3>Users:</h3>
    <ul>
      <li>Each user has a personal "Games List".</li>
      <li>Users may edit their existing reviews, add new reviews, or delete their reviews.</li>
      <li>Adding reviews to a game will be performed in a way that the user may select the game to review from an existing global list.</li>
      <li>Users may login, but not register. Users are hardcoded.</li>
    </ul>
  </li>
  <li>
    <h3>Admin:</h3>
    <ul>
      <li>The admin has control panels, in which he may delete reviews and edit/delete games of other users.</li>
      <li>Admin is hardcoded (born with the website).</li>
    </ul>
  </li>
</ul>

### General information
<ul>
  <li>Game Reviews website.</li>
  <li>Global list of existing games (added by users).</li>
  <li>Top ranked & reviewed games featured on the homepage.</li>
  <li>Good search form with advanced search options.</li>
  <li>The website includes user accounts and an admin account.</li>
</ul>
<p>Overall, the game ranking website aims to provide users with an interactive and informative platform for discovering and discussing games while catering to both user and administrator needs.</p>

### Functionality
<h3>How does our games search work.</h3>
You may leave the "Title" and "Genre" fields open, but you have to pick at least one of the following Multiplayer/Singleplayer.
The function checks if the user has provided at least one selection for multiplayer or singleplayer. 
If not, an error message is added to the model, and the "errorpage" view is returned.
Based on the provided inputs, the function performs a search in the database for games that match the specified criteria (title, genre, multiplayer, singleplayer).
<hr/>
<h2>User:</h2>
<ul>
  <li>Add a game</li>
  <li>Add a review on a game</li>
  <li>Edit owned existing reviews</li>
  <li>Delete owned reviews</li>
  <li>Search the database for games</li>
  <li>See all reviews on all games</li>
</ul>

<h2>Admin:</h2>
<ul>
  <li>Add game</li>
  <li>Delete reviews (any review)</li>
  <li>Delete games</li>
  <li>Edit games</li>
</ul>
<h2>Not authenticated</h2>
<ul>
  <li>See the homepage</li>
  <li>See the login page</li>
</ul>

<h2>Installation</h2>
Download Xampp and start "Apache" and "MySQL".<br>
Create a table called "ex5" in phpmyadmin (click "Admin" in MySQL row).<br>
Run the application.

<h2>Useful information</h2> 

Credentials of User role:
1. Mario / password
2. Luigi / password<br>
Credentials of Admin role:
1. Admin / password
(Username/Password format)
