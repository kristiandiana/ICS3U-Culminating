// The "Snake" class.
import java.awt.*;
import hsa.Console;

/*
ICS3U Culminating - Snake Game
Kristian Diana
Mon, Nov 8, 2021

This program will play the iconic retro snake game:
Eat apples to try and get your snake as long as you can
Watch out! If you touch the outside black border, or another segment of your snake, you lose!
*/


public class Snake
{
    static Console c;           // The output console

    public static void main (String[] args)
    {
	c = new Console (26, 79);

	// declaration
	Font f = new Font ("Algerian", Font.BOLD, 40);
	c.setFont (f);

	Color snakeGreen = new Color (37, 194, 84);

	int[] oldX = new int [265]; // 265 is the maximum length for this grid
	int[] oldY = new int [265];
	int[] tempX = {210};
	int[] tempY = {210};
	int[] fruitCoordinates = new int [2];
	char direction;
	int iterations, play = 32, score, highscore = 0, bounds, fruit, size, collision, selection;


	// title screen
	do
	{
	    selection = mainMenu (); 
	    if (selection == 1)
	    {
		instructions (); // opens instructions page if user wants it
		c.setFont (f);
	    }
	}
	while (selection != 0); // will keep looping until user selects play

	c.clear ();
	c.setFont (f);

	// game
	while (play == 32)
	{
	    iterations = 0;
	    score = 0;
	    bounds = 0;
	    collision = 0;
	    size = 0;
	    oldX [0] = 210; // oldX and oldY will hold the initial values of the snake
	    oldY [0] = 210;
	    spawnFruit (oldX, oldY, fruitCoordinates); // create an initial fruit
	    direction = 'n';

	    while (1 == 1)
	    {

		if (c.isCharAvail ())  // if user presses a key, it will check the direction
		{
		    direction = directionCheck (direction);
		}

		// creates temperary coordinates of the snakes head with the user inputted direction
		if (eatenFruit (tempX, tempY, fruitCoordinates) == 1)
		{
		    size = 1; // if the temperary coordinates collide with a fruit, the snake will increase in size
		}
		else
		{
		    size = 0;
		}

		int[] x = new int [score + size + 1]; // create new arrays of x and y coordinates
		int[] y = new int [score + size + 1]; // if needed, the size will be one larger than the previous time

		copy (oldX, x, size, 1); // copies coordinates from previous iteration to new arrays
		copy (oldY, y, size, 1);


		if (size == 0) // if the snake is not getting larger, all the segments of the snake must move up one place
		{
		    snakeShift (x, y);
		}

		updateSnake (x, y, direction);

		fruit = eatenFruit (x, y, fruitCoordinates);

		if (fruit == 1) // if the fruit was eaten, new fruit coordinates will be created
		{
		    score++; // update score for each fruit eaten
		    spawnFruit (x, y, fruitCoordinates);
		}

		// now that all the coordinates have been updated, we can draw everything so the user can see the new screen
		drawFruit (fruitCoordinates);
		drawSnake (x, y);

		// checks if snake has gone outside of the black border or has intercepted one of its own segments
		bounds = outOfBounds (x, y);
		collision = collisionCheck (x, y);

		if (collision == 1 || bounds == 1) // if a collision has occured, you lose
		{
		    for (int i = 0 ; i < 5 ; i++) // flashes red to the user to let them know where they hit
		    {
			c.setColor (Color.red);
			c.fillRect (x [0], y [0], 30, 30);
			delay (200);
			c.setColor (snakeGreen);
			c.fillRect (x [0], y [0], 30, 30);
			delay (100);
		    }

		    break; // exits game
		}

		// prints score
		c.setColor (Color.black);
		c.drawString ("Score: " + score, 50, 520);
		c.drawString ("Highscore: " + highscore, 300, 520);

		// the coordinates will be copied over to the old arrays to save the data once the loop reiterates
		copy (x, oldX, 0, 2);
		copy (y, oldY, 0, 2);

		// reset the temperary variables to the coordinates of the head of the snake
		tempX [0] = oldX [0];
		tempY [0] = oldY [0];

		// if the player has not yet moved, a hint on what the controls are will be displayed
		if (direction == 'n' && iterations >= 10)
		{
		    if (fruitCoordinates [1] >= 180 && fruitCoordinates [1] <= 240) // to make sure text doesn't cover fruit
		    {
			c.drawString ("Use W, A, S, or D to move!", 50, 100);
		    }
		    else
		    {
			c.drawString ("Use W, A, S, or D to move!", 50, 200);
		    }
		}

		iterations++;
		delay (150); // wait 0.15 seconds before starting again
		c.clear (); // clear the old screen


	    }

	    if (score > highscore) // updates highscore
		highscore = score;

	    c.clear ();
	    playAgain (score, highscore);


	    do // user input error checking
	    {
		play = (int) (c.getChar ());
	    }
	    while (!(play == 32 || play == 113));
	    c.clear ();

	}

	c.drawString ("Thank you for playing!", 60, 250);



    } // main method

    // methods
    
    public static int mainMenu ()  // creates main menu page
    {
	Font snakeTitle = new Font ("Algerian", Font.BOLD, 100);
	Font title = new Font ("Algerian", Font.BOLD, 30);
	Color snakeGreen = new Color (37, 194, 84);

	c.setColor (Color.green);
	c.fillRect (0, 430, 620, 40);
	c.fillRect (580, 200, 40, 230);
	c.fillRect (30, 200, 550, 40);
	c.fillRect (30, 30, 40, 170);
	c.fillRect (70, 30, 400, 40);

	c.setColor (Color.red);
	c.fillOval (550, 30, 40, 40);

	c.setColor (snakeGreen);
	c.fillRect (470, 30, 40, 40);

	c.setFont (snakeTitle);
	c.drawString ("S", 140, 170);
	c.setColor (Color.green);
	c.drawString ("nake", 210, 170);
	c.setColor (Color.black);
	c.setFont (title);
	c.drawString ("Play: Press the space bar", 20, 300);
	c.drawString ("Instructions: Press the Q key", 20, 380);

	int input;

	do // user input error checking
	{
	    input = (int) (c.getChar ());
	}
	while (!(input == 32 || input == 113 || input == 81));

	if (input == 32) // what it returns will determine whether the program will play or enter instructions page
	{
	    return 0;
	}
	else
	{
	    return 1;
	}

    }


    public static void instructions ()  // creates instructions page
    {
	Font smallF = new Font ("Aharoni", Font.BOLD, 17);

	c.clear ();
	int space;
	c.drawString ("W = UP", 235, 280);
	c.drawString ("A = LEFT", 40, 350);
	c.drawString ("S = DOWN", 215, 420);
	c.drawString ("D = RIGHT", 400, 350);
	c.drawString ("Instructions:", 180, 100);
	c.drawString ("Good luck and have fun!", 90, 500);
	c.setFont (smallF);
	c.drawString ("Collect as many apples as you can without hitting the black", 70, 140);
	c.drawString (" border or intersecting with another segment of the snake.", 70, 160);
	c.drawString ("Each time you eat an apple, your snake will grow by one segment.", 55, 180);
	c.drawString ("Use the following keys to move the snake:", 130, 200);
	c.drawString ("Press the space bar to exit", 5, 20);

	do
	{
	    space = (int) (c.getChar ());
	}
	while (space != 32);
	c.clear ();
    }


    public static void playAgain (int score, int highscore)  // creates a basic "play again" display
    {
	c.setColor (Color.black);
	c.drawString ("Score: " + score, 220, 120);
	c.drawString ("Highscore: " + highscore, 170, 160);
	c.drawString ("Press the space bar", 110, 250);
	c.drawString ("to play again", 160, 300);
	c.drawString ("Press q to exit", 150, 400);
    }


    public static void copy (int[] oldArr, int[] newArr, int start, int type)  // copies all values from an array to another array
    {
	int length;
	if (type == 1) // depending on which array is longer, to avoid IndexOutOfBounds Exception
	{
	    length = newArr.length;
	}
	else
	{
	    length = oldArr.length;
	}
	for (int i = start ; i < length ; i++)
	    newArr [i] = oldArr [i - start];

    }



    public static int eatenFruit (int[] x, int[] y, int[] coordinates)  // determines whether the snake has intercepted the fruit
    {
	if (x [0] == coordinates [0] && y [0] == coordinates [1])
	{
	    return 1;
	}
	else
	{
	    return 0;
	}
    }


    public static void spawnFruit (int[] x, int[] y, int[] coordinates)  // creates new coordinates for the fruit
    {
	int valid, randomX, randomY;
	do
	{
	    valid = 0;
	    randomX = ((int) ((Math.random () * 57) + 3)) * 10; // randomized x and y coordinates
	    randomY = ((int) ((Math.random () * 42) + 3)) * 10;
	    randomX = randomX - (randomX % 30);
	    randomY = randomY - (randomY % 30);

	    for (int i = 0 ; i < x.length ; i++) // makes sure the fruit does not intercept any of the snake's segments
	    {
		if (x [i] == randomX && y [i] == randomY)
		{
		    valid = 1;
		}
	    }
	}
	while (valid == 1); // will continue looping until the fruit coordinates do not touch the snake

	// set fruit coordinates to random values
	coordinates [0] = randomX;
	coordinates [1] = randomY;
    }


    public static char directionCheck (char direction)  // makes it so that snake can not turn inwards on itself
    {
	char input = c.getChar ();
	// all possible input options
	if (direction == 'd' && input == 'a')
	{
	    input = 'd';
	}
	else if (direction == 'a' && input == 'd')
	{
	    input = 'a';
	}
	else if (direction == 'w' && input == 's')
	{
	    input = 'w';
	}
	else if (direction == 's' && input == 'w')
	{
	    input = 's';
	}
	else if (!(input == 'a' || input == 's' || input == 'w' || input == 'd'))
	{
	    input = direction; // if input is not valid, direction will not change
	}

	return input;
    }


    public static int collisionCheck (int[] x, int[] y)  // checks if snake head has intercepted with any other snake segments
    {
	int result = 0;
	for (int i = 4 ; i < x.length ; i++)
	{
	    // if the coordinates of the head match any of coordinates of any other segments, that is a collision
	    if (x [0] == x [i] && y [0] == y [i])
	    {
		result = 1;
	    }
	}
	return result;
    }


    public static int outOfBounds (int[] x, int[] y)  // checks if snake has gone outside of black border
    {
	int result = 0;
	if (x [0] < 30 || x [0] >= 600) // checks if x is out of bounds
	{
	    result = 1;
	}
	else if (y [0] < 30 || y [0] >= 450) // checks if y is out of bounds
	{
	    result = 1;
	}
	return result;
    }


    public static void snakeShift (int[] x, int[] y)  // moves all segments of the snake up one place
    {
	for (int i = x.length - 1 ; i > 0 ; i--)
	{
	    // starts at end of the loop and sets each value to the value of the cell before it
	    x [i] = x [i - 1];
	    y [i] = y [i - 1];
	}
    }


    public static void updateSnake (int[] x, int[] y, char direction)
    {
	// depending on the direction, the snake head will move
	if (direction == 'd')
	{
	    x [0] += 30;
	}
	else if (direction == 'a')
	{
	    x [0] -= 30;
	}
	else if (direction == 'w')
	{
	    y [0] -= 30;
	}
	else if (direction == 's')
	{
	    y [0] += 30;
	}
    }


    public static void drawSnake (int[] x, int[] y)
    {
	Color snakeGreen = new Color (37, 194, 84);

	// black border
	c.setColor (Color.black);
	c.fillRect (0, 0, 630, 30); // top border
	c.fillRect (0, 30, 30, 450); // left border
	c.fillRect (30, 450, 600, 30); // bottom border
	c.fillRect (600, 30, 30, 420); // right border

	// snake
	c.setColor (snakeGreen); // draws snake head in different colour
	c.fillRect (x [0], y [0], 30, 30);

	for (int i = 1 ; i < x.length ; i++)
	{
	    c.setColor (Color.green); // draws a square at each x and y coordinate
	    c.fillRect (x [i], y [i], 30, 30);
	}
    }


    public static void drawFruit (int[] coordinates)  // takes the coordinates of the updated fruit and draws a circle
    {
	c.setColor (Color.red);
	c.fillOval (coordinates [0], coordinates [1], 30, 30);
	c.setColor (Color.black);
    }


    public static void delay (int millisecs)  // Delay Method
    {
	try
	{
	    Thread.currentThread ().sleep (millisecs);
	}


	catch (InterruptedException e)
	{
	}
    }
} // Snake class
