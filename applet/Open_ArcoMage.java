import processing.core.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class Open_ArcoMage extends PApplet {

// Code Author: Nishant Mohanchandra
// Instructor: Prof. Daniel Shiffman
// Project Name: Open ArcoMage

// What is it? An attempt at recreating a multiplayer fantasy card game, "ArcoMage", using Processing, with several modifications and extras.
 
// Idea Description - One of my favourite childhood games was something a now long-bankrupt games company called 3DO developed: "Might and Magic 7: For Blood and Honour". 
// One of the quests within that game unlocked access to a small minigame called "ArcoMage" that you could play at taverns across the game world. 
// ArcoMage, in essence, is a relatively simple card game akin to "Magic: The Gathering". 
// It is played between 2 players, both of whom control a tower and a wall, along with 3 resource production facilities - a quarry, magic, and a dungeon. 
// These produce resources - bricks, gems and beasts - respectively. Resources are important because they are required to "play" the cards in your hand. 
// The object of the game is to win by (1) building your tower up to a certain amount, (2) destroying your enemy's tower or (3) collecting resources beyond a certain threshhold.
 
// I want to redesign the game a little, by adding more cards, a multiplayer mode, and making the code open-source so fellow players can help with the evolution of the game.

// NOTE: The game's initial & victory conditions can be edited in conditions.txt. Edit the 1st, 6th, 7th, and 8th numbers of the second line for tower/brick/gem/beast threshholds.

/* 
@pjs preload = "sprites/tower.png,sprites/wall.png,sprites/1turn.png,sprites/2turn.png,sprites/dragon.jpg,sprites/OpenArcoMage.png,sprites/rules.png,sprites/background.jpg,cards/1/1.png,cards/1/2.png,cards/1/3.png,cards/1/4.png,cards/1/5.png,cards/1/6.png,cards/1/7.png,cards/1/8.png,cards/1/9.png,cards/1/10.png,cards/1/11.png,cards/1/12.png,cards/1/13.png,cards/1/14.png,cards/1/15.png,cards/1/16.png,cards/1/17.png,cards/1/18.png,cards/1/19.png,cards/1/20.png,cards/1/21.png,cards/1/22.png,cards/1/23.png,cards/1/24.png,cards/1/25.png,cards/1/26.png,cards/1/27.png,cards/1/28.png,cards/1/29.png,cards/1/30.png,cards/1/31.png,cards/1/32.png,cards/1/33.png,cards/1/34.png,cards/2/1.png,cards/2/2.png,cards/2/3.png,cards/2/4.png,cards/2/5.png,cards/2/6.png,cards/2/7.png,cards/2/8.png,cards/2/9.png,cards/2/10.png,cards/2/11.png,cards/2/12.png,cards/2/13.png,cards/2/14.png,cards/2/15.png,cards/2/16.png,cards/2/17.png,cards/2/18.png,cards/2/19.png,cards/2/20.png,cards/2/21.png,cards/2/22.png,cards/2/23.png,cards/2/24.png,cards/2/25.png,cards/2/26.png,cards/2/27.png,cards/2/28.png,cards/2/29.png,cards/2/30.png,cards/2/31.png,cards/2/32.png,cards/2/33.png,cards/2/34.png,cards/3/1.png,cards/3/2.png,cards/3/3.png,cards/3/4.png,cards/3/5.png,cards/3/6.png,cards/3/7.png,cards/3/8.png,cards/3/9.png,cards/3/10.png,cards/3/11.png,cards/3/12.png,cards/3/13.png,cards/3/14.png,cards/3/15.png,cards/3/16.png,cards/3/17.png,cards/3/18.png,cards/3/19.png,cards/3/20.png,cards/3/21.png,cards/3/22.png,cards/3/23.png,cards/3/24.png,cards/3/25.png,cards/3/26.png,cards/3/27.png,cards/3/28.png,cards/3/29.png,cards/3/30.png,cards/3/31.png,cards/3/32.png,cards/3/33.png,cards/3/34.png";
*/

// Player initialization
Player[] player = new Player[2];

// Player, turn and resource tracking tracking variables
int x = 0;              // Player tracking variable
int y = 0;              // Turn tracking variable
int z = 1;              // Resource update tracking variable

// Other necessities
int keys = 0;           // Tracks number of key presses
int victorious = 0;     // Tracks the victorious player

// Starting and victory conditions
int[] start;            // Array that stores starting conditions
int[] victor;           // Array that stores victory conditions

// Global image variables
PImage dragon;          // Picture of a dragon
PImage openAM;          // Open ArcoMage text logo
PImage rules;           // Screenshot of ArcoMage's rules
PImage back;            // Background picture
PImage tower_picture;   // Tower picture
PImage wall_picture;    // Wall picture

public void setup()
{  // Window setup
   size(1224,700);

   // Initial and victory condition initialization
   start = new int[8];
   victor = new int[8];   
   String input_file = "conditions.txt";
   String[] conditions = loadStrings(input_file);
   start = PApplet.parseInt(split(conditions[0],","));
   victor = PApplet.parseInt(split(conditions[1],","));
   
   // Player setup
   player[0] = new Player(start[0],start[1],start[2],start[3],start[4],start[5],start[6],start[7],true,false,0); // TF0 = Human player, player one & FF0 = Human player, player two 
   player[1] = new Player(start[0],start[1],start[2],start[3],start[4],start[5],start[6],start[7],false,true,0); // FT# = Computer player. # indicates strategy.
  
   // Player deck shuffling
   player[0].cards.shuffle();
   player[1].cards.shuffle();
   
   // Image pre-loading
   dragon = loadImage("sprites/dragon.jpg");
   openAM = loadImage("sprites/OpenArcoMage.png");
   rules = loadImage("sprites/rules.png");
   back = loadImage("sprites/background.jpg");
   tower_picture = loadImage("sprites/tower.png");
   wall_picture = loadImage("sprites/wall.png"); 
}

public void draw()
{  // Welcome screen setup
   if (keys == 0)
   {  image(dragon,0,0,width/2,height);
      image(openAM,width/2,0,width/2,height/2);
      image(rules,width/2,height/2,width/2,height/2);  }
      
   // Starting conditions and victory threshhold information
   else if (keys == 1)
   {  fill(0); rect(0,0,1224,700); fill(0,255,0);
      text("STARTING CONDITIONS: Tower = " + start[0] + ", Wall = " + start[1] + ", Quarry/Magic/Dungeon = " + start[2] + "/" + start[3] + "/" + start[4] + ", Bricks/Gems/Beasts = " + start[5] + "/" + start[6] + "/" + start[7],300,250);
      fill(255,255,0); text("VICTORY CONDITIONS: Tower = " + victor[0] + ", Bricks/Gems/Beasts = " + victor[5] + "/" + victor[6] + "/" + victor[7] + ", or Enemy Tower destruction!",340,300);
      fill(255); text("Press any key to start a game!",525,350);
      fill(127); text("You can edit the starting conditions and victory threshholds in conditions.txt",400,400);
   }
   
   // Gameplay screen setup
   else 
   {  // Background setup
      image(back,0,0,width,height);
    
      // Card display
      player[x].display_hand();
       
      // Resource update (if required)
      if (z != 0)
      {  z = opposite(z);
         player[x].resources.update();  }
      
      // Turn console setup
      fill(0,125); rect(300,460,600,30); fill(255);
      text("It is currently player " + (x+1) + "'s turn.",520,480);

      // Played card display
      image(player[x].pcard,300,50,100,130);
      
      // If the current player is in the middle of a turn
      if (player[x].played_cards.size() != 0) 
      {  // Display the cards played in this turn so far
         display_array(player[x].played_cards);    
         for (int k = 0; k < player[x].played_cards.size() ; k++)
         {  fill(127,127,127,127);
            rect(400+(k*100),50,100,130);  }
         // Discards display
         for (int n = 0; n < 5 ; n++)
         {  fill(255,125); rect(player[x].discard_pile[n],115,100,20);
            fill(255,0,0); text("DISCARDED",(player[x].discard_pile[n]+15),130);  }
      }
      
      // If the current player's turn has just begun
      else
      {  // Display the cards played in the opponent's last turn
         display_array(player[opposite(x)].played_cards);
         for (int k = 0; k < player[opposite(x)].played_cards.size() ; k++)
         {  fill(127,127,127,127);
            rect(400+(k*100),50,100,130);  }
         // Discards display
         for (int n = 0; n < 5 ; n++)
         {  fill(255,125); rect(player[opposite(x)].discard_pile[n],115,100,20);
            fill(255,0,0); text("DISCARDED",(player[opposite(x)].discard_pile[n]+15),130);  }
      }  
      
      // Game display window setup 
      player[0].info_setup(); player[0].tower_info_window(); 
      player[1].info_setup(); player[1].tower_info_window();
    
      // Tower and wall graphics rendering
      player[0].render_tower(); player[0].render_wall();
      player[1].render_tower(); player[1].render_wall();
      
      // What to do incase someone wins
      if ((player[x].victory(player[opposite(x)])) || (player[opposite(x)].victory(player[x])))              
      {  fill(0,125); rect((width/2)-135,(height/2)-70,300,55); fill(255,255,0);
         textSize(35);
         if (victorious != 0) 
         {  text("PLAYER " + victorious + " WINS!",(width/2)-120,(height/2)-30); noLoop();  }
         else if (player[x].victory(player[opposite(x)]))  text("PLAYER " + (x+1) + " WINS!",(width/2)-120,(height/2)-30);
         else text("PLAYER " + (opposite(x)+1) + " WINS!",(width/2)-120,(height/2)-30);
         noLoop();
      }
      
      // Greying out invalid moves
      for (int j = 0; j < 6; j++)
      {  if (!player[x].valid_move(j)) 
         {  fill(127,127,127,127);
            rect(select_card_2(j),500,100,130);
         }
      }
      
      // Decides the move in case of a computer player
      if (player[x].AI) 
      {  int comp_move = decide_move(player[x]);
         
         // If there is no valid move, discard a card randomly
         if (comp_move == -1)
         {  player[opposite(x)].played_cards = clear_array();                // Clearing the opposite player's played cards array
            player[opposite(x)].discard_pile = clear_array_2(true);          // Clearing the opposite player's discard pile array
            float randvar = random(6); int n;
            player[x].played_cards.add(player[x].cards.card.get(select_card_3(randvar)));
            for (n = 0; n < 5; n++)
            {  if (player[x].discard_pile[n] != -101) {}
               else break;  }
            player[x].discard_pile[n] = (400+((player[x].played_cards.size()-1)*100));
            player[x].cards.card.remove(select_card_3(randvar));
            x = opposite(x);        // Change active player
            z = opposite(z);        // Reset resource tracker
         }
         
         // Else do a valid move
         else {
         player[opposite(x)].played_cards = clear_array();             // Clearing the opposite player's played cards array
         player[opposite(x)].discard_pile = clear_array_2(true);       // Clearing the opposite player's discard pile array
         y = player[x].play_card(comp_move,player[opposite(x)]);       // Play the card
         if (player[x].victory(player[opposite(x)]))                   // What to do incase someone wins
         {  victorious = x+1;  }
         if (y == 0)            // Normal card
         {  x = opposite(x);    // Change active player
            z = opposite(z);  } // Reset resource tracker
         else if (y == 1)       // "Play again" card
         {  }                   // Do nothing
         else if (y == 2)       // Special "if/then" card
         {  x = opposite(x);    // Change active player
            z = opposite(z);  } // Reset resource tracker
         else if (y == 3)       // Elven Scout / Prism card
         {  float randvar = random(6); int n;
            player[x].played_cards.add(player[x].cards.card.get(select_card_3(randvar)));
            for (n = 0; n < 5; n++)
            {  if (player[x].discard_pile[n] != -101) {}
               else break;  }
            player[x].discard_pile[n] = (400+((player[x].played_cards.size()-1)*100));
            player[x].cards.card.remove(select_card_3(randvar));
         }
         }
      }
      
      // Mouse hover action
      if ((mouse_over_hand()) && (player[x].valid_move(select_card()))) 
      {  fill(0,255,0,75);
         rect(mouseX_rounder(),500,100,130);
      }
     
   }
}

public void mouseClicked()
{  // If the mouse is left clicked 
   if ((mouse_over_hand()) && (mouseButton == LEFT))                   // If the mouse is hovering over the hand
   {  if (player[x].valid_move(select_card()))                         // If the move is valid
      {  player[opposite(x)].played_cards = clear_array();             // Clearing the opposite player's played cards array
         player[opposite(x)].discard_pile = clear_array_2(true);       // Clearing the opposite player's discard pile array
         y = player[x].play_card(select_card(),player[opposite(x)]);   // Play the card
         if (player[x].victory(player[opposite(x)]))                   // What to do incase someone wins
         {  victorious = x+1;  }
         if (y == 0)            // Normal card
         {  x = opposite(x);    // Change active player
            z = opposite(z);  } // Reset resource tracker
         else if (y == 1)       // "Play again" card
         {  }                   // Do nothing
         else if (y == 2)       // Special "if/then" card
         {  x = opposite(x);    // Change active player
            z = opposite(z);  } // Reset resource tracker
         else if (y == 3)       // Elven Scout / Prism card
         {  float randvar = random(6); int n;
            player[x].played_cards.add(player[x].cards.card.get(select_card_3(randvar)));
            for (n = 0; n < 5; n++)
            {  if (player[x].discard_pile[n] != -101) {}
               else break;  }
            player[x].discard_pile[n] = (400+((player[x].played_cards.size()-1)*100));
            player[x].cards.card.remove(select_card_3(randvar));         // Discards a random card from your hand
         }
      }
   }
   
   // If the mouse is right clicked
   if ((mouse_over_hand()) && (mouseButton == RIGHT))                  // If the mouse is hovering over the hand
   {  player[opposite(x)].played_cards = clear_array();                // Clearing the opposite player's played cards array
      player[opposite(x)].discard_pile = clear_array_2(true);          // Clearing the opposite player's discard pile array
      player[x].played_cards.add(player[x].cards.card.get(select_card()));
      int n;
      for (n = 0; n < 5; n++)
      {  if (player[x].discard_pile[n] != -101) {}
         else break;  }
      player[x].discard_pile[n] = (400+((player[x].played_cards.size()-1)*100));
      player[x].cards.card.remove(select_card());
      x = opposite(x);        // Change active player
      z = opposite(z);        // Reset resource tracker
   }
}

public void keyTyped()
{   keys++;  }
public int decide_move(Player computer_player)
{  boolean[] possible_moves = new boolean[6];
   for (int i = 301 ; i < 901 ; i += 100)  if (computer_player.valid_move(select_card_AI(i)))  possible_moves[select_card_AI(i)] = true;
   
   // The "Random valid move" strategy
   if (computer_player.AI_strategy == 0)
   {  if ((possible_moves[0] == false) && (possible_moves[1] == false) && (possible_moves[2] == false) && (possible_moves[3] == false) && (possible_moves[4] == false) && (possible_moves[5] == false))
      return -1;
      while ((possible_moves[0] != false) || (possible_moves[1] != false) || (possible_moves[2] != false) || (possible_moves[3] != false) || (possible_moves[4] != false) || (possible_moves[5] != false))
      {  int randominteger = PApplet.parseInt(random(6));
         if (possible_moves[randominteger] == true) return randominteger;
      }
   }
   
   return -1;
   
}
class Card
{  int type;          // Brick (1), Gem (2), or Beast (3)  
   int number;        // Card numbers range from 1-34 so far
   String imgpath;    // Location of image path on disk
   PImage card_img;   // Image object
   int[] effects;     // Stores the characteristics of the card
   
   // Card initializer - Constructor. Uses card type & number to load to appropriate sprite and card effects
   Card(int t, int n)
   {  // Card data initialization
      type = t; number = n;
      imgpath = "cards/" + type + "/" + number + ".png";
      card_img = loadImage(imgpath);
      // Reading card effects from file
      effects = new int[19];      
      String input_file = "" + type + ".txt";
      String[] characteristics = loadStrings(input_file);
      effects = PApplet.parseInt(split(characteristics[number-1],","));
   }
   
   // Card display function
   public void display_card(int i)
   {  image(card_img,300+(i*100),500);  }
}

class Deck
{  ArrayList card;                          // Stores a collection of cards   
   
   // Deck Assembly - Constructor
   Deck()
   {  card = new ArrayList();               // ArrayList initialization
      for (int i = 1; i < 4; i++)           // Tier One cards
      {  for (int j = 1; j < 11; j++)
         {  for (int k = 0; k < 4; k++)
            {  card.add(new Card(i,j)); }
         }
      }
      for (int i = 1; i < 4; i++)           // Tier Two cards
      {  for (int j = 11; j < 21; j++)
         {  for (int k = 0; k < 3; k++)
            {  card.add(new Card(i,j)); }
         }
      }
      for (int i = 1; i < 4; i++)           // Tier Three cards
      {  for (int j = 21; j < 36; j++)
         {  for (int k = 0; k < 2; k++)
            {  card.add(new Card(i,j)); }
         }
      }  
   }
   
   // Shuffles the cards into a deck, produces a random ordering of cards
   public void shuffle()
   {  ArrayList shuffled_deck = new ArrayList();
      while (card.size() != 0)
      {   int randomvariable = PApplet.parseInt(random(card.size()));
          shuffled_deck.add(card.get(randomvariable));
          card.remove(randomvariable);
      }
      card = shuffled_deck;
   }
} 
      
class Inventory
{   int quarry; int magic; int dungeon;    // Resource production facilities
    int bricks; int gems;  int beasts;     // Counters for resource amounts
    
    // Inventory creation - Constructor
    Inventory(int q, int m, int d, int b, int g, int be)
    {  quarry = q; magic = m; dungeon = d;
       bricks = b; gems = g;  beasts = be;
    }
    
    // Inventory update function
    public void update()
    {  bricks += quarry;
       gems += magic;
       beasts += dungeon;
    }
}

class Player
{   int tower;               // Tower Health
    int wall;                // Wall Health
    Deck cards;              // Cards available to a player
    boolean player1;         // Stores whether the player is player one or not
    Inventory resources;     // Resource production and inventory
    ArrayList played_cards;  // Tracks all the cards played in the player's current turn
    int[] discard_pile;      // Tracks all player discards
    PImage pcard;            // Player graphic
    boolean AI;              // Stores whether the player is an AI player or not
    int AI_strategy;         // Int that represents what kind of AI is playing, if any

    // Player initialization - Constructor
    Player(int t, int w, int prod1, int prod2, int prod3, int res1, int res2, int res3, boolean player, boolean isAI, int AItype)
    {  cards = new Deck();
       resources = new Inventory(prod1,prod2,prod3,res1,res2,res3);
       tower = t; wall = w;
       player1 = player;
       AI = isAI; AI_strategy = AItype;
       played_cards = new ArrayList();
       
       discard_pile = new int[5];
       for (int n = 0; n < 5; n++) discard_pile[n] = -101;
       
       if (player1) pcard = loadImage("sprites/1turn.png");
       else pcard = loadImage("sprites/2turn.png");
    }
    
    // Creates the information tracking window for a player
    public void info_setup()
    {  if (player1)
       {  fill(0,125); rect(50,50,150,170); fill(255);
          fill(255,255,0);
          text("Player One",70,70);
          fill(255,0,0);
          text("Quarry: " + resources.quarry,70,95);
          text("Bricks: " + resources.bricks,70,115);
          fill(0,0,255);
          text("Magic: " + resources.magic,70,140);
          text("Gems: " + resources.gems,70,160);
          fill(0,255,0);
          text("Dungeon: " + resources.dungeon,70,185);     
          text("Beasts: " + resources.beasts,70,205);
       }
       else
       {  fill(0,125); rect(width-200,50,150,170); fill(255);
          fill(255,255,0);
          text("Player Two",width-180,70);
          fill(255,0,0);
          text("Quarry: " + resources.quarry,width-180,95);
          text("Bricks: " + resources.bricks,width-180,115);
          fill(0,0,255);
          text("Magic: " + resources.magic,width-180,140);
          text("Gems: " + resources.gems,width-180,160);
          fill(0,255,0);
          text("Dungeon: " + resources.dungeon,width-180,185);
          text("Beasts: " + resources.beasts,width-180,205);
       }
    }
    
    // Creates the tower and wall tracking window for a player   
    public void tower_info_window()
    {   fill(0,125);
        if (player1) rect(75,450,100,50);
        else rect(width-190,450,100,50);
        fill(255);
        if (player1)
        {  text("Tower: " + tower,95,470);
           text("Wall: " + wall,95,490);    }
        else
        {  text("Tower: " + tower,width-170,470);
           text("Wall: " + wall,width-170,490);    }
    }
    
    // Renders the graphics for the player's tower
    public void render_tower()
    {   int tower_height = PApplet.parseInt(225*(PApplet.parseFloat(tower)/PApplet.parseFloat(victor[0])));
        if (player1) image(tower_picture,20,437-tower_height,150,tower_height+1); 
        else image(tower_picture,width-175,437-tower_height,150,tower_height+1);
    }

    // Renders the graphics for the player's wall    
    public void render_wall()
    {   int wall_height = PApplet.parseInt(225*(PApplet.parseFloat(wall)/PApplet.parseFloat(victor[1])));
        if (player1) image(wall_picture,160,435-wall_height,30,wall_height+1);
        else image(wall_picture,width-200, 435-wall_height,30,wall_height+1);
    }
    
    // Displays the player's currently active hand (Top 6 cards)
    public void display_hand()
    {   for  (int i = 0; i < 6; i++)
        {   Card card = (Card)cards.card.get(i);
            card.display_card(i);    }
    }
    
    // Function that checks whether a move is valid
    public boolean valid_move(int i)
    {   Card active_card = (Card)cards.card.get(i);
        if ((resources.bricks >= active_card.effects[7]) && (resources.gems >= active_card.effects[8]) && (resources.beasts >= active_card.effects[9])) return true;
        else return false;
    }
    
    // Function hat checks whether the player has won or not
    public boolean victory(Player enemy)
    {   if (tower >= victor[0]) return true;
        if (resources.bricks >= victor[5]) return true;
        if (resources.gems >= victor[6]) return true;
        if (resources.beasts >= victor[7]) return true;
        if (enemy.tower <= 0) return true;
        return false;
    }
    
    // Function that plays a card from the current player's hand
    public int play_card(int i, Player enemy)
    {   Card active_card = (Card)cards.card.get(i);
    
        // Check and deal with special cards on a case-by-case basis
        if (active_card.effects[0] == 2)    // Special cards
        {  if (active_card.type == 1)       // Special brick cards
           {  if (active_card.number == 5)  // Mother Lode
              {  if (resources.quarry >= enemy.resources.quarry)
                 {  active_card.effects[4] = -1;  }
              }
              if (active_card.number == 8)  // Copping The Tech
              {  if (resources.quarry < enemy.resources.quarry)
                 {  active_card.effects[4] = resources.quarry - enemy.resources.quarry;  }
              }
              if (active_card.number == 12)  // Foundations
              {  if (wall != 0)
                 {  active_card.effects[2] = -3;  }
              }
              if (active_card.number == 31)  // Flood Water
              {  if (wall <= enemy.wall)
                 {  active_card.effects[1] = 2;
                    active_card.effects[6] = 1;  }
                 if (wall < enemy.wall)
                 {  active_card.effects[10] = 0;
                    active_card.effects[15] = 0;
                 }
              }
              if (active_card.number == 32)  // Barracks
              {  if (resources.dungeon >= enemy.resources.dungeon)
                 {  active_card.effects[6] = 0;  }
              }
              if (active_card.number == 34)  // Shift
              {  active_card.effects[2] = wall - enemy.wall;
                 active_card.effects[11] = enemy.wall - wall;
              }
           }
           if (active_card.type == 2)  // Special magic cards
           {  if (active_card.number == 14)  // Parity
              {  if (resources.magic <= enemy.resources.magic)
                 {  active_card.effects[5] = resources.magic - enemy.resources.magic;  }
                 else
                 {  active_card.effects[14] = enemy.resources.magic - resources.magic; }
              }
              if (active_card.number == 30)  // Bag of Baubles
              {  if (tower >= enemy.tower)
                 {  active_card.effects[1] = -1;  }
              }
              if (active_card.number == 33)  // Lightning Shard
              {  if (tower <= enemy.wall)
                 {  active_card.effects[10] = 0;
                    active_card.effects[12] = 8;  }
              }
           }
           if (active_card.type == 3)  // Special dungeon cards
           {  if (active_card.number == 19)  // Spizzer
              {  if (enemy.wall > 0)
                 {  active_card.effects[12] = 6;  }
              }
              if (active_card.number == 21)  // Corrosion Cloud
              {  if (enemy.wall == 0)
                 {  active_card.effects[12] = 7;  }
              }
              if (active_card.number == 22)  // Unicorn
              {  if (resources.magic <= enemy.resources.magic)
                 {  active_card.effects[12] = 8;  }
              }
              if (active_card.number == 30)  // Spearman
              {  if (wall <= enemy.wall)
                 {  active_card.effects[12] = 2;  }
              }
           }
        }      
    
        // Playing any card self-effects
        tower -= active_card.effects[1];
        wall -= active_card.effects[2];
        if (wall < 0) wall = 0;        
        if (wall >= active_card.effects[3]) wall -= active_card.effects[3];
        else {  active_card.effects[3] -= wall; wall = 0; tower -= active_card.effects[3];  }
        resources.quarry -= active_card.effects[4];
        resources.magic -= active_card.effects[5];
        resources.dungeon -= active_card.effects[6];
        resources.bricks -= active_card.effects[7];
        resources.gems -= active_card.effects[8];
        resources.beasts -= active_card.effects[9];
        
        // Playing card enemy effects
        enemy.tower -= active_card.effects[10];
        enemy.wall -= active_card.effects[11];
        if (enemy.wall >= active_card.effects[12]) enemy.wall -= active_card.effects[12];
        else {  active_card.effects[12] -= enemy.wall; enemy.wall = 0; enemy.tower -= active_card.effects[12];  }
        enemy.resources.quarry -= active_card.effects[13];
        enemy.resources.magic -= active_card.effects[14];
        enemy.resources.dungeon -= active_card.effects[15];
        enemy.resources.bricks -= active_card.effects[16];
        enemy.resources.gems -= active_card.effects[17];
        enemy.resources.beasts -= active_card.effects[18];
        
        // Rounding back any negative resources or production facilities to zero
        if (enemy.resources.quarry < 0) enemy.resources.quarry = 0;
        if (enemy.resources.magic < 0) enemy.resources.magic = 0;
        if (enemy.resources.dungeon < 0) enemy.resources.dungeon = 0;
        if (enemy.resources.bricks < 0) enemy.resources.bricks = 0;
        if (enemy.resources.gems < 0) enemy.resources.gems = 0;
        if (enemy.resources.beasts < 0) enemy.resources.beasts = 0;
        
        played_cards.add(active_card);     // Add the active card to the played cards pile
        cards.card.remove(i);              // Deck cleanup / Garbage removal
        return active_card.effects[0];     
    }
}    
// Checks whether the house is hovering over the hand
public boolean mouse_over_hand()
{  if ((mouseX >= 300) && (mouseX <= 900) && (mouseY >=500) && (mouseY <= 630)) return true;
   else return false;
}

// Rounds mouseX values in the active zone to the nearest hundred
public int mouseX_rounder()
{  if ((mouseX >= 300) && (mouseX <=400)) return 300;
   else if ((mouseX > 400) && (mouseX <=500)) return 400;
   else if ((mouseX > 500) && (mouseX <=600)) return 500;
   else if ((mouseX > 600) && (mouseX <=700)) return 600;
   else if ((mouseX > 700) && (mouseX <=800)) return 700;
   else return 800;
}

// Returns the card index of the card the mouse is hovering over when in the active zone
public int select_card()
{  if ((mouseX >= 300) && (mouseX <=400)) return 0;
   else if ((mouseX > 400) && (mouseX <=500)) return 1;
   else if ((mouseX > 500) && (mouseX <=600)) return 2;
   else if ((mouseX > 600) && (mouseX <=700)) return 3;
   else if ((mouseX > 700) && (mouseX <=800)) return 4;
   else return 5;
}

// Returns the card index of the card for a computer AI
public int select_card_AI(int posX)
{  if ((posX >= 300) && (posX <=400)) return 0;
   else if ((posX > 400) && (posX <=500)) return 1;
   else if ((posX > 500) && (posX <=600)) return 2;
   else if ((posX > 600) && (posX <=700)) return 3;
   else if ((posX > 700) && (posX <=800)) return 4;
   else return 5;
}

// Returns the mouseX region of a given card index 
public int select_card_2(int j)
{  if (j==0) return 300;
   else if (j==1) return 400;
   else if (j==2) return 500;
   else if (j==3) return 600;
   else if (j==4) return 700;
   else return 800;
}

// Actually just converts a float to an int. This exists because select_card() is overloaded and gets float inputs from Prism and Elven Scout which need to be dealt with differently
public int select_card_3(float k)
{  return PApplet.parseInt(k);
}

// Returns 1 if given 0; returns 0 if given 1 or any other number
public int opposite(int some_number)
{  if (some_number == 0) return 1;
   else return 0;
}

// Clears an ArrayList
public ArrayList clear_array()
{  ArrayList empty_array = new ArrayList();
   return empty_array;
}

// Resets the values of the discard_pile array
public int[] clear_array_2(boolean dummy_variable)
{  int[] empty_array = new int[5];
   for (int n = 0; n < 5; n++) empty_array[n] = -101;
   return empty_array;
}

// Displays a card at the top of the screen, given the card and its index in the played_cards ArrayList
public void display_card(Card card, int i)
{  image(card.card_img,400+(i*100),50);
}

// Displays the elements of a card ArrayList
public void display_array(ArrayList card_array)
{  for (int i = 0; i < card_array.size(); i++)
   {  display_card((Card)card_array.get(i),i);  }
}
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#F0F0F0", "Open_ArcoMage" });
  }
}
