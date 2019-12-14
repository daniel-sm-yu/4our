# 4our

Imagine the look on your friend's face as you rotate the Connect4 board only to have four of your discs fall in a line. Or when they think they've won, only to for you to rotate the board and block both of their ways to win. In 4our, you can! Tap on a column to drop a disc. The background colour will change to indicate whose turn it is. Tap and hold the background while rotating the phone to rotate the board in the desired direction.

4our uses the gyroscope sensor to determine when the angular acceleration exceeds a certain value before rotating the board. OnTouchListeners are used to avoid accidental rotations, as a rotation only occurs when there is a finger on the screen. 

The code was structured with the goal of an elegant rotation algorithm in mind. For example, Columns take discs and drops them to the lowest empty slot, while the Board is just an array of Columns. In order to rotate the board, simply iterate through the current board (starting with the column that will become the bottom row after rotating) while dropping each disc into the appropriate Column of the new board. Afterwards, you'll be left with the result of the rotation in the new board, almost as if you rotated a Connect4 board in real life!

![4our demo](connect4demo.gif)

*It might take a few seconds for the demo video to load but I promise it's worth it!*
