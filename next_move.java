
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


//import operations;


public class next_move {


public static void main(String[] args) throws Exception {
  //     board_string, color, N, legal, specialty, specialty_num, post_strategy
    char[][][] board = operations.construct_board(args[0]);
    //operations.print_board(board);
    char clr = args[1].charAt(0);
    int N = Integer.parseInt(args[2]);
    boolean check = false;
    boolean legal=false;
    char specialty_piece = 'k';

    if (args[3].equals("list_all"))
      check=true;
    else if (args[3].equals("legal"))
      legal=true;
    
    if (args[4].equals("pawn"))
      specialty_piece = 'p';
    else if (args[4].equals("knight"))
      specialty_piece = 'n';
    else if (args[4].equals("queen"))
      specialty_piece = 'q';
    
    int extra_moves_ = Integer.parseInt(args[5]);
    
      
    

    
    ArrayList<int[][]> mvs = recursive_ai_enhanced(board, clr, N, extra_moves_ ,specialty_piece);
    if (legal) // don't allow illegal moves: 
      mvs = filter_illegal_moves(board, mvs); 
    
    if (mvs.size()==0) // if none of those moves were legal, find a group that are
    {
      mvs = moves_methods.get_moves(board, clr);
      mvs = filter_illegal_moves(board, moves_methods.get_moves(board, clr));
    }
    
    if (check)
    {
      for (int[][] mv0 : mvs)
      {
        System.out.println(mv0[0][0] + "," + mv0[0][1] + "," + mv0[1][0] + "," + mv0[1][1]);
      }
    }
    else
    {
      if (mvs.size()>0)
      {
        //System.out.println(args[6]);
        switch (args[6])
        {
        case "kill,pawns" : 
          mvs = post_strategy.kill(board, mvs);
        case "pawns" : 
          mvs = post_strategy.pawns_first(board, mvs);
          break;
        case "kill" : 
          mvs = post_strategy.kill(board, mvs);
          break;
        case "None" : 
          break;
        default : throw new Exception("Not implemented");
        }
        Random rando = new Random();
        int rand_i = rando.nextInt(mvs.size());
        int[][] mv0 = mvs.get(rand_i);
        System.out.print(mv0[0][0] + "," + mv0[0][1] + "," + mv0[1][0] + "," + mv0[1][1]);
      }
      else System.out.println("STALEMATE");
    }
  }

private static ArrayList<int[][]> filter_illegal_moves(char[][][] board, ArrayList<int[][]> moves)
{
  ArrayList<int[][]> out = new ArrayList<int[][]>();
  char color = board[moves.get(0)[0][0]][moves.get(0)[0][1]][0];
  for (int[][] mv0 : moves)
    if (false==in_check(operations.execute_move(board, mv0[0], mv0[1]), color))
      out.add(mv0);
  return out;
}
private static boolean in_check(char[][][] board, char color)
{
  boolean out = false;
  char other_color;
  if (color=='b') other_color = 'w';
  else if (color == 'w') other_color = 'b';
  else return true;
  int[] king_coords = new int[2];
  for (int i=0; i<8; i++)
  {
    for (int j=0; j<8; j++)
    {
      if (board[i][j][0] == color && board[i][j][1] == 'k')
      {
        king_coords[0] = i; king_coords[1] = j;
      }
    }
  }
  ArrayList<int[][]> mvs = moves_methods.get_moves(board, other_color);
  for (int[][] mv : mvs)
  {
    if (mv[1][0] == king_coords[0] && mv[1][1] == king_coords[1])
    {
      out = true;
      break;
    }
  }
  return out;
}

private static void prioritize_moves(char[][][] board, ArrayList<int[][]> mvs0, char enemy_color)
{// attacks, direction?
  // attack +10, 
  //for bishop prioritize number of moves (13,11,9,7), 
  //for knight, prioritize number of moves as well(8,...), 
  //queen, number of moves 
  //ArrayList<int[][]> out = new ArrayList<int[][]>(mvs0);

  
  int[][] mv = new int[2][2];
  //int scalar;
  //if (enemy_color == 'b') scalar = -1;
  //else scalar = 1;

  int j;
  
  j = 0;
  for (int i=0; i< mvs0.size(); i++)
  {
    mv = mvs0.get(i);
    if (board[mv[1][0]][mv[1][1]][0] == enemy_color)
    {
      Collections.swap(mvs0, i, j++); // swap elements and increment j. 
    }
  }
  //return out;
}

private static ArrayList<int[][]> recursive_ai_enhanced(
  char[][][] board1, 
  char color, 
  int N,
  int extra_moves,
  char special_piece)
{
  // get enemy color
  char new_move_color;
  if (color=='w'){ new_move_color = 'b'; }
  else { new_move_color = 'w'; }

  // get initial black and white lists:
  ArrayList<int[]> my_coords = new ArrayList<int[]>();
  ArrayList<int[]> enemy_coords = new ArrayList<int[]>();
  for (int i=0; i<8; i++)
  {
    for (int j=0; j<8; j++)
    {
      int[] coord = {i, j};
      if (color == board1[i][j][0])
        my_coords.add(coord);
      else if (new_move_color == board1[i][j][0])
        enemy_coords.add(coord);
    }
  }

  ArrayList<int[][]> mvs = moves_methods.get_moves(board1, my_coords);
  prioritize_moves(board1, mvs, new_move_color);
  int[] scores = new int[mvs.size()];


  int wcs = -127; // start as the worst possible
  int updated_extra_moves;
  int updated_N;
  for (int i=0; i<mvs.size(); i++)
  {
    int[][] move = mvs.get(i);
    ArrayList<ArrayList<int[]>> updated_coords = operations.update_coords(my_coords, enemy_coords, move);
    ArrayList<int[]> upd_my_coords = updated_coords.get(0); 
    ArrayList<int[]> upd_enemy_coords = updated_coords.get(1); 
    updated_extra_moves = extra_moves; // reset for every move
    updated_N = N; // reset for every move
    char attacking_piece = board1[move[0][0]][move[0][1]][1];
    if (N>0)
    { 
      if (attacking_piece == special_piece && extra_moves>0)
      { // if the piece you are using to attack is your specialty, look further
        updated_N += 1;
        updated_extra_moves -= 1;
      }
      //ArrayList<int[]> upd_my_coords = new ArrayList<int[]>(my_coords);
      //ArrayList<int[]> upd_enemy_coords = new ArrayList<int[]>(enemy_coords);
      char[][][] board2 = operations.execute_move(board1, move[0], move[1]);        
      scores[i] = get_min_or_max_enhanced(
        board2, 
        new_move_color, 
        updated_N, 
        wcs, 
        //negative_next_board_score,
        special_piece,
        updated_extra_moves,
        upd_enemy_coords,
        upd_my_coords);
    
      if (filter_illegal_moves(board2, moves_methods.get_moves(board2, new_move_color)).size()==0 && 
      false==in_check(board2, new_move_color))
        scores[i]=0;
  
    }
    else // if N = 0; almost never used, unless you want a super dumb ai. 
      scores[i] = board_score(operations.execute_move(board1, move[0],move[1]), color);
    if (scores[i] > wcs)
    {
      wcs = scores[i];
    }
  }
  int max_score = scores[operations.max_index(scores)];
  ArrayList<int[][]> out = new ArrayList<int[][]>();
  for (int i=0; i<mvs.size(); i++)
  {
    if (max_score == scores[i])
    {
      out.add(mvs.get(i));
    }
  }
  return out;
}

  private static int get_min_or_max_enhanced(
    char[][][] board1, 
    char move_color,
    int n_left,
    int wcs,
    char special_piece,
    int extra_moves,
    ArrayList<int[]> my_coords,
    ArrayList<int[]> enemy_coords)
/** The idea is as follows: suppose white is thinking ahead and comes up with a
* move in which he can force a gain of 5 points. He is now considering his next
* possible move but discovers that if he does that black can then force that white
*  only gets 4 instead of 5 points. White should then abandon that move 
* and not even calculate the rest of the possibilities
* In each layer this is implemented it should save about half of the computations. 
*/  
{
int out=0;
char new_move_color;
if (move_color=='w'){ new_move_color = 'b'; }
else { new_move_color = 'w'; }
//calculate moves using known coordinates to increase speed
ArrayList<int[][]> mvs = moves_methods.get_moves(board1, my_coords);
prioritize_moves(board1, mvs, new_move_color);

int[] scores = new int[mvs.size()];
int new_wcs = -127; // start as the worst possible


if (n_left > 1)
{
  int updated_extra_moves;
  int updated_N;
  for (int i=0; i<mvs.size(); i++)
  { 
    updated_extra_moves = extra_moves; // reset for every move
    updated_N = n_left; // reset for every move
    int[][] move = mvs.get(i);
    // get updated coordinates for next recursive move call
    ArrayList<ArrayList<int[]>> updated_coords = operations.update_coords(my_coords, enemy_coords, move);
    ArrayList<int[]> upd_my_coords = updated_coords.get(0); 
    ArrayList<int[]> upd_enemy_coords = updated_coords.get(1); 
    char attacking_piece = board1[move[0][0]][move[0][1]][1];
    char attacked_piece = board1[move[1][0]][move[1][1]][1];
    if (attacking_piece == special_piece && extra_moves>0)
    { // if the piece you are attacking is your specialty, look further
      updated_N += 1;
      updated_extra_moves -= 1;
    }
    if (attacked_piece == 'k')
      scores[i] = 100 + n_left;//127; // and don't go deeper
    else 
      scores[i] = get_min_or_max_enhanced(
      operations.execute_move(board1, move[0], move[1]), 
      new_move_color, 
      updated_N-1, 
      new_wcs, 
      special_piece,
      updated_extra_moves,
      upd_enemy_coords,
      upd_my_coords);
    if (scores[i] > new_wcs) // if there's a better path,
    {// there is a new worst case scenario
      new_wcs = scores[i];
    }
    if (scores[i] > -wcs)
    {
      break;
    }
  }
}
else // if n_left <= 1:
{
  for (int i=0; i<mvs.size(); i++)
  {
    int[][] move = mvs.get(i);
    if (board1[move[1][0]][move[1][1]][1] == 'k')
      scores[i] = 100 + n_left; // and don't go deeper
    else 
      scores[i] = board_score(operations.execute_move(board1, move[0], move[1]),
       move_color);//current_board_score + piece_value(board1[move[1][0]][move[1][1]][1]);
    if (scores[i] > -wcs)
      {
        break;
      }
      
  }
  
}
out = scores[operations.max_index(scores)];
return -out;
}

  private static int board_score(char[][][] board, char color)
  {
    int out = 0;
    int val = 0;
    char pp;
    for (int i=0; i<8; i++)
    {
      for (int j=0; j<8; j++)
      {
        pp = board[i][j][1];
        if (pp!='0')
        {
          val = piece_value(pp);
          if (color==board[i][j][0]){out+=val;}
          else {out-=val;}
        }
      }
    }
    return out;
  }
  private static int piece_value(char piece)
  {
    int out=0;
    switch (piece)
    {
      case 'p' : out=1; break;
      case 'b' : 
      case 'n' : out = 3; break;
      case 'r' : out = 5; break;
      case 'q' : out = 9; break;
      //case 'k' : out =0; break;
      default  : break;
    }
    return out;
  }

}
