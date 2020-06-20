# 4our

Imagine the look on your friend's face as you rotate the Connect4 board and four of your discs fall in a line. Or when they think they've won, only for you to rotate the board and block both of their win conditions. In 4our, you can! Tap on a column to drop a disc. Press the background and rotate the phone to rotate the board in that direction. The background color will change to indicate whose turn it is.

4our uses your phone's built-in gyroscope sensor to measure its angular acceleration, rotating the game board when it exceeds a certain threshold. OnTouchListeners are used to avoid accidental rotations, as a rotation can only occur if there is a finger touching the screen. 

The code was structured with the goal of an elegant rotation algorithm. For example, Columns take discs and drops them to the lowest empty slot, while the Board is just an array of Columns. In order to rotate the board, simply iterate through the current board (starting with the column that will become the bottom row after rotating) while dropping each disc into the appropriate Column of the new board. Afterwards, you'll be left with a board displaying the new game state, almost as if you rotated a Connect4 board in real life!

![4our demo](4our_demo.gif)
