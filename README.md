# voltorb-flip-solver
Game-Solving Tool used to predict best next move in Voltorb Flip Game (minigame from Pokemon HGSS)

Voltorb Flip is a variation on Minesweeper from Pokemon HeartGold and SoulSilver. It is a blend of traditional Minesweeper and Picross. 

So what is the goal of the game, exactly? It looks something like this:

```
Board: 
     |PT: 4|PT: 7|PT: 5|PT: 5|PT: 4
     |VB: 1|VB: 1|VB: 0|VB: 2|VB: 2
     |#F: 4|#F: 4|#F: 0|#F: 3|#F: 3
-----+-----+-----+-----+-----+-----
PT: 6|     |     |     |     |     
VB: 1|     |     |     |     |     
#F: 3|     |     |     |     |     
-----+-----+-----+-----+-----+-----
PT: 4|     |     |     |     |     
VB: 1|     |     |     |     |     
#F: 3|     |     |     |     |     
-----+-----+-----+-----+-----+-----
PT: 7|     |     |     |     |     
VB: 1|     |     |     |     |     
#F: 3|     |     |     |     |     
-----+-----+-----+-----+-----+-----
PT: 5|     |     |     |     |     
VB: 1|     |     |     |     |     
#F: 3|     |     |     |     |     
-----+-----+-----+-----+-----+-----
PT: 3|     |     |     |     |     
VB: 2|     |     |     |     |     
#F: 2|     |     |     |     |     
```

Each tile contains a value of either 0, 1, 2, or 3. The goal of the game is to flip over all the 2s and 3s. Score is calculated by multiplying each revealed tile's face value together. Notice that each row and column is labeled with a PT, VB, and \#F value. It follows that flipping over even a single 0 will cause a game over, so this program aims to minimize the chance of losing by suggesting tiles which have the lowest probability of having a 0 face value.

PT: The point total (sum of all cards' face values) of the row/column

VB: The number of 0s in the row/column

\#F: The number of remaining safely flippable tiles (nonzero tiles)

This simple program determines which rows are unlikely to contain 1s or 0s, suggesting to the user which tile has the least 0 probability at each step of the game. Let's run through a sample of how we can use this program.

Start the program by running MainSolver.

The program will prompt for the point totals and number of 0s in each row/column like so:

```
Enter the stats for row 1: 6 1
Enter the stats for row 2: 4 1
Enter the stats for row 3: 7 1
Enter the stats for row 4: 5 1
Enter the stats for row 5: 3 2
Enter the stats for column 1: 4 1
Enter the stats for column 2: 7 1
Enter the stats for column 3: 5 0
Enter the stats for column 4: 5 2
Enter the stats for column 5: 4 2
```

You'll notice I entered the point total, followed by the number of 0s, in the format `6 1`. This means in row 1, there is a point total of `6` and exactly `1` 0 in that row.

This program will then run the Ignorance Check:

```
Running Ignorance Check...
Marking cards of guarenteed value 1...
Marking additionally found bombs...
Marking Complete!
Marking cards of guarenteed value 1...
Marking additionally found bombs...
Marking Complete!
Ignorance Check Complete!
```

Essentially, if the number of flippable cards is equal to the point total of a row or column, then we can conclude that these cards must have either a value of `1` or `0`. With this in mind, we can start eliminating cards we don't even want to flip. Finally, the updated board is displayed and a prompt is given:

```
==================================================
Board: 
     |PT: 4|PT: 7|PT: 5|PT: 5|PT: 4
     |VB: 1|VB: 1|VB: 0|VB: 2|VB: 2
     |#F: 4|#F: 4|#F: 0|#F: 3|#F: 3
-----+-----+-----+-----+-----+-----
PT: 6|     |B   1|     |B   1|B   1
VB: 1|  X  |     |  1  |     |     
#F: 3|     |2   3|     |2   3|2   3
-----+-----+-----+-----+-----+-----
PT: 4|     |     |     |     |     
VB: 1|  X  |  X  |  1  |  X  |  X  
#F: 3|     |     |     |     |     
-----+-----+-----+-----+-----+-----
PT: 7|     |B   1|     |B   1|B   1
VB: 1|  X  |     |  1  |     |     
#F: 3|     |2   3|     |2   3|2   3
-----+-----+-----+-----+-----+-----
PT: 5|     |B   1|     |B   1|B   1
VB: 1|  X  |     |  1  |     |     
#F: 3|     |2   3|     |2   3|2   3
-----+-----+-----+-----+-----+-----
PT: 3|     |     |     |     |     
VB: 2|  X  |  X  |  1  |  X  |  X  
#F: 2|     |     |     |     |     
==================================================
Turn 1:
==================================================
Evaluating next best move...
Evaluation Complete! The best choice is:
Row: 1
Col: 2
Bomb Probability: 5
Enter flip: 121
```

Keep in mind, an X in the middle of the card denotes the card is "dead" (either it has a value of 1 or 0, both of which are useless and potentially game-ending.) Cards with potential value are still marked with their corners as B, 1, 2, or 3 (B = 0 is the bomb, avoid these!). A turn evaluation is made based on which tile has the least chance of being a 0, and a request is sent to the player to update this board with information.

The syntax for this part is: `121` denotes that in Row 1, column 2, we report when we flipped over the card, we got a value of 1.

Keep in mind, also, that just because the program recommends you to flip in one spot, you need not always do so! If you wanted to instead manually flip over a card in row 4, column 5, you can do so, and the program will accept it and update the board anyway!

So we entered our input of `121`. Here's what the program does:

```
Running Ignorance Check...
Marking cards of guarenteed value 1...
Marking additionally found bombs...
Marking Complete!
Ignorance Check Complete!
==================================================
Board: 
     |PT: 4|PT: 7|PT: 5|PT: 5|PT: 4
     |VB: 1|VB: 1|VB: 0|VB: 2|VB: 2
     |#F: 4|#F: 3|#F: 0|#F: 3|#F: 3
-----+-----+-----+-----+-----+-----
PT: 6|     |     |     |B   1|B   1
VB: 1|  X  |  1  |  1  |     |     
#F: 2|     |     |     |2   3|2   3
-----+-----+-----+-----+-----+-----
PT: 4|     |     |     |     |     
VB: 1|  X  |  X  |  1  |  X  |  X  
#F: 3|     |     |     |     |     
-----+-----+-----+-----+-----+-----
PT: 7|     |B   1|     |B   1|B   1
VB: 1|  X  |     |  1  |     |     
#F: 3|     |2   3|     |2   3|2   3
-----+-----+-----+-----+-----+-----
PT: 5|     |B   1|     |B   1|B   1
VB: 1|  X  |     |  1  |     |     
#F: 3|     |2   3|     |2   3|2   3
-----+-----+-----+-----+-----+-----
PT: 3|     |     |     |     |     
VB: 2|  X  |  X  |  1  |  X  |  X  
#F: 2|     |     |     |     |     
==================================================
Turn 2:
==================================================
Evaluating next best move...
Evaluation Complete! The best choice is:
Row: 3
Col: 2
Bomb Probability: 6
Enter flip: 323
```

This tool continues until you have revealed the board entirely, like so:

```
Board: 
     |PT: 4|PT: 7|PT: 5|PT: 5|PT: 4
     |VB: 1|VB: 1|VB: 0|VB: 2|VB: 2
     |#F: 4|#F: 1|#F: 0|#F: 2|#F: 2
-----+-----+-----+-----+-----+-----
PT: 6|     |     |     |     |     
VB: 1|  X  |  1  |  1  |  3  |  X  
#F: 1|     |     |     |     |     
-----+-----+-----+-----+-----+-----
PT: 4|     |     |     |     |     
VB: 1|  X  |  X  |  1  |  X  |  X  
#F: 3|     |     |     |     |     
-----+-----+-----+-----+-----+-----
PT: 7|     |     |     |     |     
VB: 1|  X  |  3  |  1  |  X  |  2  
#F: 1|     |     |     |     |     
-----+-----+-----+-----+-----+-----
PT: 5|     |     |     |     |     
VB: 1|  X  |  2  |  1  |  X  |  X  
#F: 2|     |     |     |     |     
-----+-----+-----+-----+-----+-----
PT: 3|     |     |     |     |     
VB: 2|  X  |  X  |  1  |  X  |  X  
#F: 2|     |     |     |     |     
==================================================
Turn 6:
==================================================
Evaluating next best move...
No more viable moves exist. Exiting...
```

Congratulations, you just completed a game of Voltorb Flip using this program.
