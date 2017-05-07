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

void setup()
{  // Window setup
   size(1224,700);

   // Initial and victory condition initialization
   start = new int[8];
   victor = new int[8];   
   String input_file = "conditions.txt";
   String[] conditions = loadStrings(input_file);
   start = int(split(conditions[0],","));
   victor = int(split(conditions[1],","));
   
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

void draw()
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

void mouseClicked()
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

void keyTyped()
{   keys++;  }
