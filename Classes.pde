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
      effects = int(split(characteristics[number-1],","));
   }
   
   // Card display function
   void display_card(int i)
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
   void shuffle()
   {  ArrayList shuffled_deck = new ArrayList();
      while (card.size() != 0)
      {   int randomvariable = int(random(card.size()));
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
    void update()
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
    void info_setup()
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
    void tower_info_window()
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
    void render_tower()
    {   int tower_height = int(225*(float(tower)/float(victor[0])));
        if (player1) image(tower_picture,20,437-tower_height,150,tower_height+1); 
        else image(tower_picture,width-175,437-tower_height,150,tower_height+1);
    }

    // Renders the graphics for the player's wall    
    void render_wall()
    {   int wall_height = int(225*(float(wall)/float(victor[1])));
        if (player1) image(wall_picture,160,435-wall_height,30,wall_height+1);
        else image(wall_picture,width-200, 435-wall_height,30,wall_height+1);
    }
    
    // Displays the player's currently active hand (Top 6 cards)
    void display_hand()
    {   for  (int i = 0; i < 6; i++)
        {   Card card = (Card)cards.card.get(i);
            card.display_card(i);    }
    }
    
    // Function that checks whether a move is valid
    boolean valid_move(int i)
    {   Card active_card = (Card)cards.card.get(i);
        if ((resources.bricks >= active_card.effects[7]) && (resources.gems >= active_card.effects[8]) && (resources.beasts >= active_card.effects[9])) return true;
        else return false;
    }
    
    // Function hat checks whether the player has won or not
    boolean victory(Player enemy)
    {   if (tower >= victor[0]) return true;
        if (resources.bricks >= victor[5]) return true;
        if (resources.gems >= victor[6]) return true;
        if (resources.beasts >= victor[7]) return true;
        if (enemy.tower <= 0) return true;
        return false;
    }
    
    // Function that plays a card from the current player's hand
    int play_card(int i, Player enemy)
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
