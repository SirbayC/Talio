## Stakeholders:
-   User: any person that interacts with Talio

## Terminology:
-   Tag: Small descriptive entries, used for categorizing cards
-   Task: Subentry in a checklist corresponding to each card
-   Card: An entry in a list with a title (description, a checklist of tasks, a list of tags)
-   List: An ordered collection of cards with a name (e.g. To do, Doing, Done)
-   Board: An ordered collection of lists with a name

## Mocks:
Note: We checked the several mocks included in the appendix, but are they meant to be a rough design or can it be more defined?
- TODO

## MUST Requirements:
As a user, I want...
-   To work together on the same board, to improve collaboration
-   Every change made to a card to appear on the board immediately, so I can see what others are doing
-   To drag & drop cards, to make it easier to maintain order
-   The ability to create, read, update and delete cards and lists, so I can keep the board up to date
-   Cards and lists to have a title, so I know what they are about
-   To be able to choose a different server when I start the application, so I can work with other companies/teams
-   To choose a server and be presented with an overview of all cards (in the default board), so I am flexible in choosing with whom I’m working
-   The content to be persistent, so I don’t lose my work

## SHOULD Requirements:
As a user, I want...
-   To be able to add descriptions (and dates) to my cards alongside titles to further specify my entries
-   To add tags to each card, so I can keep track of certain cards that show up in multiple lists.
-   To be presented with an overview of all boards after choosing a server, with the ability to choose the one to work on, by id or name, so that I have access to multiple boards that serve different purposes.
-   To create and change the name of a board, so I can quickly see what the purpose of that board is.

## COULD Requirements:
As a user, I want...
-   To be able to add extra protection with a password to a board for edit permissions for personal tasks
-   To use keyboard shortcuts to interact with different parts of the UI to make interacting with the program easier:
	- on enter create next card 
	- hover + T change text
	- backspace/delete remove card
	- escape to close card
-   The application to remember what I worked on for each server, so I don’t have to repeatedly look up boards
-   To be able to add a checklist of tasks to a card, to add another level of depth to each card
-   To filter by tags, in order to be able to locate specific cards belonging to a certain category
-   To be able to customise different elements (e.g. change background colour), for aesthetic reasons

## WON'T Requirements:
As a user, I don't want...
- To use any user management system