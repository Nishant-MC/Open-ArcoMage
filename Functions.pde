// Checks whether the house is hovering over the hand
boolean mouse_over_hand()
{  if ((mouseX >= 300) && (mouseX <= 900) && (mouseY >=500) && (mouseY <= 630)) return true;
   else return false;
}

// Rounds mouseX values in the active zone to the nearest hundred
int mouseX_rounder()
{  if ((mouseX >= 300) && (mouseX <=400)) return 300;
   else if ((mouseX > 400) && (mouseX <=500)) return 400;
   else if ((mouseX > 500) && (mouseX <=600)) return 500;
   else if ((mouseX > 600) && (mouseX <=700)) return 600;
   else if ((mouseX > 700) && (mouseX <=800)) return 700;
   else return 800;
}

// Returns the card index of the card the mouse is hovering over when in the active zone
int select_card()
{  if ((mouseX >= 300) && (mouseX <=400)) return 0;
   else if ((mouseX > 400) && (mouseX <=500)) return 1;
   else if ((mouseX > 500) && (mouseX <=600)) return 2;
   else if ((mouseX > 600) && (mouseX <=700)) return 3;
   else if ((mouseX > 700) && (mouseX <=800)) return 4;
   else return 5;
}

// Returns the card index of the card for a computer AI
int select_card_AI(int posX)
{  if ((posX >= 300) && (posX <=400)) return 0;
   else if ((posX > 400) && (posX <=500)) return 1;
   else if ((posX > 500) && (posX <=600)) return 2;
   else if ((posX > 600) && (posX <=700)) return 3;
   else if ((posX > 700) && (posX <=800)) return 4;
   else return 5;
}

// Returns the mouseX region of a given card index 
int select_card_2(int j)
{  if (j==0) return 300;
   else if (j==1) return 400;
   else if (j==2) return 500;
   else if (j==3) return 600;
   else if (j==4) return 700;
   else return 800;
}

// Actually just converts a float to an int. This exists because select_card() is overloaded and gets float inputs from Prism and Elven Scout which need to be dealt with differently
int select_card_3(float k)
{  return int(k);
}

// Returns 1 if given 0; returns 0 if given 1 or any other number
int opposite(int some_number)
{  if (some_number == 0) return 1;
   else return 0;
}

// Clears an ArrayList
ArrayList clear_array()
{  ArrayList empty_array = new ArrayList();
   return empty_array;
}

// Resets the values of the discard_pile array
int[] clear_array_2(boolean dummy_variable)
{  int[] empty_array = new int[5];
   for (int n = 0; n < 5; n++) empty_array[n] = -101;
   return empty_array;
}

// Displays a card at the top of the screen, given the card and its index in the played_cards ArrayList
void display_card(Card card, int i)
{  image(card.card_img,400+(i*100),50);
}

// Displays the elements of a card ArrayList
void display_array(ArrayList card_array)
{  for (int i = 0; i < card_array.size(); i++)
   {  display_card((Card)card_array.get(i),i);  }
}
