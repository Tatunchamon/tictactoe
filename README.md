# TicTacToe Game

## How to run it in demo mode

It requires `docker-compose` and access to internet.

This command will build the application with help of [multi-stage docker build](Dockerfile) and run 2 instances of application. 

```bash
docker-compose up
```

After startup each instance will try to start a game with a random jitter delay, and after that they will play games with each other.

You can check the state of games with help of a special REST API.


You can control the delay of each turn by setting `APP_TURN_DELAY_MS` (defaults to 100 ms) environment variable in `compose.yaml`.

## Observability REST API

The application has a special API for obtaining its internal state.

You can get the state of game by calling this command:
```bash
# instance of player-1
curl -vv -XPUT http://localhost:8081/game/$UUID
# instance of player-2
curl -vv -XPUT http://localhost:8082/game/$UUID
```

Expected output

```json
{
  "id": "006b225f-33f6-4049-b04a-bcb26213149f",
  "board": [
    "O",
    "X",
    "O",
    "EMPTY",
    "X",
    "O",
    "EMPTY",
    "X",
    "X"
  ],
  "status": "X_WON",
  "myPlayer": "X"
}
```

For all played games
```bash
# instance of player-1
curl -vv -XPUT http://localhost:8081/games
# instance of player-2
curl -vv -XPUT http://localhost:8082/games
```

Expected output

```json
{
  "006b225f-33f6-4049-b04a-bcb26213149f": {
    "id": "006b225f-33f6-4049-b04a-bcb26213149f",
    "board": [
      "O",
      "X",
      "O",
      "EMPTY",
      "X",
      "O",
      "EMPTY",
      "X",
      "X"
    ],
    "status": "X_WON",
    "myPlayer": "X"
  },
  "efae377e-36ed-4541-b386-2fea6af68752": {
    "id": "efae377e-36ed-4541-b386-2fea6af68752",
    "board": [
      "O",
      "X",
      "EMPTY",
      "X",
      "O",
      "O",
      "X",
      "X",
      "O"
    ],
    "status": "O_WON",
    "myPlayer": "X"
  }
}

```