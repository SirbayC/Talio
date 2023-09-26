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