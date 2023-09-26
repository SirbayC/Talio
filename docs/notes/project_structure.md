# Project Structure

## Directory structure:

### Entities

- Board : name, (optional) password, Lists
- List : title, Cards
- Card : title, description, date, Tasks, Tags
- Task : title, (boolean) completed
- Tag : name, color

### Repositories

One per entity, located under the server sub-project, in a folder called 'repositories'

### Controllers

- Located under the server sub-project, in a folder called 'controllers'
- Board controller for the board entity (in the main folder)
- List, Card, Task, Tag controller for their respective entities (in a "boards" folder, under main)

### Services

- Located under the server sub-project, in a respective folder
- Websockets (part of live communication)
