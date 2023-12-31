# Talio

## Description of project

Talio is a collaborative goal setting application which allows multiple people to work on the same
board. These boards work with lists of tasks that can be rearranger in any desired order and
customized for a more personalised look.

## Group members

| Profile Picture                                                                                                                                      | Name                    | Email                             |
|------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------|-----------------------------------|
| ![](https://secure.gravatar.com/avatar/50dd60a86257bcbd0e763219e8ca7ac5?s=800&d=identicon&length=4&size=50&color=DDD&background=777&font-size=0.325) | Cosmin Andrei Vasilescu | c.vasilescu-1@student.tudelft.nl  |
| ![](https://eu.ui-avatars.com/api/?name=OOPP&length=4&size=50&color=DDD&background=777&font-size=0.325)                                              | Thomas van Weert        | t.c.o.vanWeert@student.tudelft.nl |
| ![](https://secure.gravatar.com/avatar/cdd9dd4409bb96f775ebc39d172fc754?s=800&d=identicon&length=4&size=50&color=DDD&background=777&font-size=0.325) | Lia Petrova             | L.P.Petrova-1@student.tudelft.nl  |
| ![](https://eu.ui-avatars.com/api/?name=OOPP&length=4&size=50&color=DDD&background=777&font-size=0.325)                                              | Andreas Shiamishis      | a.shiamishis@student.tudelft.nl   |
| ![](https://eu.ui-avatars.com/api/?name=OOPP&length=4&size=50&color=DDD&background=777&font-size=0.325)                                              | Sare Öztürk             | z.s.ozturk-1@student.tudelft.nl   |

## How to run it

First, in order to set up the JavaFX dependencies, make sure to download the JavaFX SDK (19.0.2.1) and store it in an accessible location. Then, go to `File >> Project Structure >> Modules >> Dependency >> + (on left-side of window)`, referencing the directory where the unpacked SDK's `lib` folder is located. Don't change the default settings.

Then, in `Run >> Edit Configurations`, add the following line to VM Options: `--module-path /path/to/JavaFX/lib --add-modules=javafx.controls,javafx.fxml`.

To run the Talio application the client and server need to be started, this can be done in an
arbitrary order. When connecting to the server make sure this has finished its start-up procedure.

If you want to connect to the application as an administrator use the following steps:

- add `/admin` at the end of the server address
- press `ok` and enter the admin password, this has been set to `adminPassword`

## Changing the admin password

The hashcode of the admin password of the server resides
in `server/src/main/resources/passwordhash.txt`. Here we
have to place the hash-code of our new password. After this we can build the server and run it to
start using our new admin password.

The hashcode of a password can be calculated using the following steps:

- `hash = 7`
- For each character `c` in the password we multiply `hash` by `31` and add the integer
  representation in `UTF16` of the character `c` to this.

## Disclaimer

The original repository is located on GitLab, this being a simple copy of it, with demonstrative purposes. The code, as mentioned earlier, was created by me and the mentioned team members, based on a template provided by the university. The code and content in this repository is provided for reference purposes only. One may not copy, reproduce, or distribute this code without explicit permission from the owner. Unauthorized use of this code may violate applicable copyright laws.
