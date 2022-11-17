# Arenal-Bot

The bot listens to a GitHub Event that sends a JSON file to our API REST endpoint whenever there's a change in that branch. If there is a change in the file in the branch we are expecting, the API REST downloads the file and generates a list of the teams that are winning. Then it creates a message stating what teams are in that list and sends it to the Telegram Bot. 

## Bot Configuration

To set up the bot, add in bot.info file the bot's username and token as bot.username and bot.token

## Database



We are using PostgreSQL 15.1 to achieve data persistence of chat ids.



### Database installation



To install the database, first you need to download the installer from https://www.postgresql.org/download/. Then you install it with the default properties taking into consideration the password for user postgres you set up.



Then you type the following command ```psql -U postgres``` to access the database of user postgres. After entering the password, you need to create a new user with ```create user telegrambot with password 'telegrambot'``` and create a new database with ```create database telegrambot owner 'telegrambot'```. Finally, you leave the terminal entering ```\q```.
