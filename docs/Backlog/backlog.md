## Stakeholders

- **User**: any person that interacts with Talio
- **Admin**: a user with access to edit all the boards

## Terminology

- **Tag**: Small descriptive entries, used for categorizing cards
- **Task**: A piece of work in a checklist corresponding to each card
- **Card**: A goal or purpose with a title (a description, a list of tags, and a checklist of tasks)
- **List**: An ordered collection of cards with a title (e.g. "Todo", "In Progress", "Done")
- **Board**: An ordered collection of lists with a title (e.g. "OOP Project")

## Mocks
![connect to server](/docs/Backlog/mocks/1.png "1")
![single board](/docs/Backlog/mocks/2.png "2")
![multiboard start](/docs/Backlog/mocks/3.png "3")
![multiboard start2](/docs/Backlog/mocks/3.1.png "3.1")
![multiboard start3](/docs/Backlog/mocks/3.2.png "3.2")
![multiboard](/docs/Backlog/mocks/4.png "4")


## Epics

1. **Minimal functioning app**
   - The ability to open the default static board on multiple servers
2. **Card interaction**
   - The ability to modify cards and rearrange their order within a list
3. **List interaction**
   - The ability to modify lists and rearrange their order within a board
4. **Multi-User**
   - The ability to work with others in a synchronised and collaborative manner
5. **Multi-Board**
   - The ability to modify boards and having an overview of all available boards
6. **Card Details**
   - The ability to further clarify a card with a description, date and tags
7. **Tag Support**
   - The ability to add a checklist of tasks to a card and have it show the progression of the checklist
8. **Customisation**
   - The ability to change the appearance of the board
9. **Keyboard Shortcuts**
   - The ability to use certain keyboard shortcuts to improve usability
10. **Security**
    - The ability to make boards password protected for editing, while remaining visible to all users

## User stories

### Minimal functioning app

1. As a user, I want to be able to explicitly specify which server to connect to without having to log in, so that nobody has to register, as all with access can be trusted
   - The client application directly connects to the server running on the same device
   - At startup, there is a prompt for the URL to be used for the connection to the server
   - There is a connect button next to the input field
   - Once clicked, the user gets connected to the specified server
2. As a user, I want to be provided with a default board, so that I have a starting point for the interaction with the
   application
   - Once connected to a server, the user is presented with an overview of all the lists present on that board
   - If the server's database contains any lists / cards, they are automatically displayed
   - Otherwise, a default hard-coded board is initialized and saved to the server

### Card interaction

1. As a user, I want to be able to consistently see all my saved cards, so that the app itself is reliable
   - The database maintains the instance of a card, even if the server is restarted
2. As a user, I want to be able to add cards to a specific list in the board, so that I can use the basic organizational functionality of the app
   - There exists an "add" button corresponding to each list
   - Once pressed, an input field is created within the list and becomes in focus (the cards, at the most basic form,
     consist of a simple title)
   - The card is saved to the database whenever the input field gets out of focus (the user clicks somewhere else)
3. As a user, I want to be able to update cards, so I can mark my progress or change an existing card
   - The text area is shaded on hover (also to offer ease with drag and drop feature)
   - A user starts editing a card by clicking on the text of the card
   - Changes are saved whenever the input field gets out of focus
4. As a user, I want to be able to remove cards, so I can remove things that are no longer needed or relevant
   - There is a "delete" button that appears next to each card when hovering above it with the mouse
   - Once pressed, the card is removed from the list
   - The database is updated to reflect the change
5. As a user, I want to be able to drag and drop cards, to organise my cards more easily
   - The operation is started by clicking within the area of a card, but not on the text
   - If a card is moved within its list, the ordering of the list changes, and thus its position
   - If a card is moved to another list, it is deleted from the original and added to the other list, in the
     specified position
   - The final position/rank of a card is indicated by a horizontal line, creating a padded "space" between the other
     two
     cards, marking its final position
   - Until the card is dropped, its last location is indicated by a shaded area
   - Once a card is dropped, it stays in that list/location, saving changes in the database
   - If the place where the card was dropped is non-specific/invalid (thus, with no line indicator shown), it should be
     moved back
     to where it was taken from (no saved changes)

### List Interaction

1. As a user, I want to be able to add lists to the board, so that I can categorize cards (e.g., "Todo", "In
   Progress", "Done")
   - There exists an "add" button on the list overview page
   - Once it is pressed, the user is prompted to give the list a name
   - The prompt consists of an input field and an "enter" button
   - After confirming, the new list is added next to the other present ones
   - Any added list is saved on the server
2. As a user, I want to be able to edit lists by changing their titles, so that I can do changes when I want/need to
   i.e., correcting a mistake
   - The text area is shaded on hover (also to offer ease with drag and drop feature)
   - A user starts editing a list's title by clicking on the text of the list
   - Once pressed, the list's title is brought in focus, open to changes
   - The title is saved to the database whenever the input field gets out of focus (the user clicks somewhere else)
3. As a user, I want to be able to drag and drop lists, to organise them more easily
   - The operation is started by clicking within the area of a card, but not on the text
   - When the user presses and holds the intended area, he is able to drag and drop the entire list within the board,
   - When the user presses and holds the intended area, the list disappears from the original place
   - The final position of a list will be indicated by a vertical line, creating a "space" between the other two lists,
     where it would fit in between
   - Once a list is dropped, it stays in that location, saving changes in the database
   - If the place the list was dropped was non-specific/invalid (with no line indicator shown), it should be moved back
     to where it was taken from (no saved changes)
4. As a user, I want to be able to delete lists, so that it will not take place on the board when no longer necessary
   - There is a "delete" button to the left of the lists title area, which becomes visible when the user hovers above
     it
   - Once pressed, the user is prompted to confirm his action
   - If the user agrees, the entire list is removed from the overview
   - If the user agrees, the entire list is removed from the database

### Multi-User

1. As a user, I want to be able to work with others on the same boards to improve collaboration
   - Multiple clients are able to connect simultaneously to the same server
   - Given one board per server, once two users connect to the same server, they will be working on the same board
   - Everyone on the server can interact with the cards and make permanent changes
   - Everyone on the server can interact with the lists and make permanent changes
   - Once saved, the changes are immediately synchronized, and reflected in the others' overview
   - In case of conflicts, the most recent change will be the one to prevail


### Multi-board

1. As a user, I don't want to directly be taken to a board when connecting to a server, so that I have more flexibility
   by using multiple boards:
   - A server supports multiple boards
   - Once a user connects to a server, he is presented with a start page
   - The start page is divided in two, indicating the two possible choices of a user
   - On the left, there is an area for creating a new board, with descriptive text
   - On the right, there is an area for accessing an existing board, with descriptive text
2. As a user, I want to be able to access any boards after choosing a server, with the ability to choose the
   one to work on, by id or name, so that I have access to multiple boards that serve different purposes
   - The right area of the start page contains an input area and a "connect" button
   - By filling in a boards name or id, a user is able to access an already existing board
3. As a user I want to be able to create a new board, so that I have separate boards corresponding to their purpose (
   e.g. one for personal use and one for work)
   - The left area contains a "create board" button
   - Once pressed, the user is prompted to name his board
   - Once confirmed, a new empty board is initialized and an automatic id is allocated to the board
   - Newly created boards are permanent and other users may already connect to them
4. As a user, I want to be able to manage a board, so I can keep them on track with my needs
   - A "settings" button is displayed to the left of the banner containing the board's name
   - Once pressed, a pop-up window appears
   - The board's allocated id is displayed (read-only)
   - The board's name is now editable in an input field, with changes saved when it gets out of focus
   - Changes on boards are permanent and visible to all users
5. As a user, I want to be able to delete a board, if it serves no purpose to me anymore, so I do not clutter the server
   - The "settings" pop-up contains a delete button
   - Once pressed, the user is prompted to confirm his action
   - If confirmed, the user is brought back to the start page of the application
   - A deleted board gets removed from the database, and other users that may be working to it are brought to the start
     page

### Card features

1. As a user, I want to be able to add extra features, so that I can make more use of the app
   - Each card now has an additional "edit" button, shown next to the already existing "delete" button
   - Once pressed, a pop-up window is created, populated with all the card details
   - All the possible fields with corresponding input areas are shown, either populated or not
   - The drag and drop feature now works by clicking anywhere on the entire area of the card
   - Changes are saved when the field gets out of focus
2. As a user, I want to be able to add a description to my cards, to further specify my entries
   - There is an optional input text area
   - Content is added by simply typing in the area
   - The description is saved, whenever it gets out of focus
3. As a user, I want to be able to add a due date to my cards, to be able to prioritize certain activities
   - There is an optional date area
   - Content is added by simply filling in the day, month, year, optionally hour and minutes
   - The date is saved, whenever it gets out of focus
4. As a user, I want to be able to add a checklist of tasks to a card, to better organise work
   - There is an optional checklist feature, with a corresponding "add" button
   - Once pressed, a new task is added, and the task's title input area is brought in focus
   - The task is saved, whenever it gets out of focus
   - Changes are made directly by clicking on the task itself
   - A small "delete" button exists next to each task, which appears when hovering above it
   - When pressed, the task is deleted
   - There is a checkbox to the left of each task
   - If checked, a task becomes "crossed" and is moved to the button of the checklist

### Tag Support
1. As a user, I want to be able to add tags to each card, so I can keep track of certain categories of cards that show
   up in different
   lists
   - There is an optional tag list feature, with a corresponding "add" button
   - Once pressed, a new tag is added, and the tags' text input area is brought in focus
   - The tag is saved, whenever it gets out of focus
   - Tags are added to this list in the order of their creation
   - Editing a tag is done by clicking on it
   - Deleting a tag is done using a small cross button
2. As a user, I want to be able to filter by tags, to be able to locate specific cards belonging to a certain category
   - There is a search bar in the top banner area, with the ability to search either by card or tag
   - Searching with a tag correctly displays the cards that have the tag, hiding cards that are not tagged with the specified tag

### Customization
1. As a user, I want to be able to customise different elements (e.g., change background colour), for aesthetic reasons
   - The settings page of the board allows to change the theme of the board
   - Any changes made to the appearance are visible in the overview once changes are saved

### Keyboard shortcuts

1. As a user, I want to be given the ability to create a new card every time I press enter after typing the title of a
   card, so that I can quickly add multiple cards
2. As a user, I want to also be able to press enter to save changes to a text field, so that I do not have to click
   somewhere else so that it becomes out of focus each time
3. As a user, I want to be able to change text by hovering over it and pressing T (as an alternative to cliking on it)
4. As a user, I want to be able to remove different elements by pressing delete (as an alternative to using the "delete" buttons)
5. As a user, I want to be able to close a card (open for editing) be pressing the escape button

### Security

1. As a user, I want to be able to make editing a board password protected, but it should still be visible to everyone,
   so I can prevent unwanted changes
   - Upon creating a new board, besides naming it, a user can also, optionally protect it, by filling in a password field (leaving it blank means no protection)
   - There is a "Viewer mode" indicator, that allows to switch to the "Editor mode", being prompted for the password
   - A user connecting to a board that is not password protected enter directly in editor mode
   - The settings page of the board now displays the current password, in the form of an editable input field, thus
     allowing its change (or removal, the board becoming unprotected)
2. As an admin, I want to be able to have a master key
   - The master password can be used to unlock all password-protected boards
