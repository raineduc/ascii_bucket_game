# ascii_bucket_game
Simple ascii 2d game


## Game control
- <kbd>Enter</kbd> - to continue
- <kbd>←</kbd> - to decrease ball velocity
- <kbd>→</kbd> - to increase ball velocity
- <kbd>↓</kbd> - to rotate ball directions clockwise (0 degree - direction to the right)
- <kbd>↓</kbd> - to rotate ball directions counterclockwise
- <kbd>CTRL</kbd>+<kbd>c</kbd> or <kbd>ESC</kbd> to quit the game


## Requirements and caveats
- On Windows only <kbd>ESC</kbd> works to exit and during the round it's inactive
- Better to play in full screen mode

## Build
```
git clone 'https://github.com/raineduc/ascii_bucket_game'
cd ascii_bucket_game
sbt package
```
**Run**:

```scala target/scala-{version}/ascii_bucket_game_{version}.jar```

## Demo

![demo](https://raw.githubusercontent.com/raineduc/ascii_bucket_game/main/demo/gif1.gif)


