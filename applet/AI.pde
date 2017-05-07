int decide_move(Player computer_player)
{  boolean[] possible_moves = new boolean[6];
   for (int i = 301 ; i < 901 ; i += 100)  if (computer_player.valid_move(select_card_AI(i)))  possible_moves[select_card_AI(i)] = true;
   
   // The "Random valid move" strategy
   if (computer_player.AI_strategy == 0)
   {  if ((possible_moves[0] == false) && (possible_moves[1] == false) && (possible_moves[2] == false) && (possible_moves[3] == false) && (possible_moves[4] == false) && (possible_moves[5] == false))
      return -1;
      while ((possible_moves[0] != false) || (possible_moves[1] != false) || (possible_moves[2] != false) || (possible_moves[3] != false) || (possible_moves[4] != false) || (possible_moves[5] != false))
      {  int randominteger = int(random(6));
         if (possible_moves[randominteger] == true) return randominteger;
      }
   }
   
   return -1;
   
}
