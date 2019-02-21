# LogicPuzzleAI
Solve logic grid puzzles. Import from XML using JAXB unmarshaller and solve using JDBC and SQL.

See <a href = "https://www.logic-puzzles.org/how-to-solve-a-logic-puzzle.php">this</a> if you are unfamiliar with logic puzzles.

Example:
```
snakes: ash python, banded keel, horned viper, tawny cobra
countries: Australia, Cambodia, Nicaragua, Panama
lengths: 12 inches, 18 inches, 24 inches, 30 inches
colors: black & red, black & white, green & red, yellow & blue

The black & white serpent is 6 inches longer than the ash python.
The banded keel is 6 inches longer than the serpent from Australia.
The black & white snake isn't from Panama.
The green & red serpent is 12 inches shorter than the horned viper.
The animal from Cambodia, the reptile that is 12 inches long, and the black & white serpent are three 
 different animals.
The serpent from Panama is either the horned viper or the banded keel.
The snake that is 12 inches long, the black & red snake, and the black & white snake are three different 
 animals.
The animal from Australia is 6 inches shorter than the green & red animal.
```

## Process
This project solves logic puzzles of any size. Test puzzles are included in [logicpuzzles](src/main/resources/logicpuzzles.xml). [LoadPuzzles](src/main/java/puzzle/LoadPuzzles.java) imports the puzzles, [Puzzle](src/main/java/puzzle/Puzzle.java) processes raw language clues into [Clue Objects](src/main/java/puzzle/Clue.java). Then [Gameplay](src/main/java/puzzle/Gameplay.java) operates with [GridMananger](src/main/java/puzzle/GridManager.java) to prefrom CRUD operations on the database and solve the puzzle. 

## Console
Processed clues and solutions are printed to the console. For the earlier example, the console would read: 

```
[black & white] 6.0>[ash python]
[banded keel] 6.0>[Australia]
[black & white]!=[Panama]
[green & red] 12.0<[horned viper]
[Cambodia]=[12 inches][black & white]
[Panama]=[banded keel][horned viper]
[12 inches][black & red][black & white]
[Australia] 6.0<[green & red]
```
```
Connected to database
```
```
ash python = Nicaragua = 24 inches = black & red
banded keel = Panama = 18 inches = green & red
horned viper = Cambodia = 30 inches = black & white
tawny cobra = Australia = 12 inches = yellow & blue
```

## Install
Change the password in [Test](src/main/java/puzzle/Test.java) and the JDBC link in [GridMananger](src/main/java/puzzle/GridManager.java)

## Thoughts
This was a great experiement in scaling. I built the system around the first three puzzles, which each had three categories of four items, but planned ahead to scale to larger puzzles. To do this I used an enum class for words that indicated the equation in the clue - so it was easy to add longer and shorter as comparisons for the snake puzzle. I also have a somewhat bulky method Gameplay#comparison that would be a lot easier hard coded for four items but uses calculated minimums and maximums to traverse the items 

``` java
if(step > 1) {
	for(int j=0; j < step; j++) {
		grid.updateIntersection(subject, stepCategory.item(j), 1);
		grid.updateIntersection(object, stepCategory.item(max - j), 1);
	}
}
```
When I later introduced bigger puzzle - three categories of five items or four categories of four items - I was pleased I didn't have to change any of the methods.

## To Do
Finish testing and javadoc. GUI?
