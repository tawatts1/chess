import java.util.ArrayList;
import java.lang.Math;
public class moves_methods
{
  public static void main(String[] args)
  {
    char[][][] board = new char[8][8][2];
    for (int i=0; i<8; i++) for (int j=0; j<8; j++)
    {
      board[i][j][0] = '0'; board[i][j][1] = '0';
    }
    board[4][5][0] = 'b'; board[4][5][1] = 'b';
    //board[0][4][0] = 'b'; board[0][4][1] = 'n';
    //board[6][4][0] = 'w'; board[6][4][1] = 'n';
    ArrayList<int[][]> mvs = get_moves(board, 'b');
    operations.print_board(board);
    for (int[][] mv : mvs)
      System.out.println(mv[1][0] + ", " + mv[1][1]);

  }

  public static ArrayList<int[][]> filter_illegal_moves(char[][][] board, ArrayList<int[][]> moves)
  {
    ArrayList<int[][]> out = new ArrayList<int[][]>();
    char color = board[moves.get(0)[0][0]][moves.get(0)[0][1]][0];
    for (int[][] mv0 : moves)
      if (false==in_check(operations.execute_move(board, mv0[0], mv0[1]), color))
        out.add(mv0);
    return out;
  }

  public static boolean in_check(char[][][] board, char color)
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
    ArrayList<int[][]> mvs = get_moves(board, other_color);
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

  public static ArrayList<int[][]> get_moves(char[][][] board, ArrayList<int[]> coords)
  {
    ArrayList<int[][]> out = new ArrayList<int[][]>();
    char piece;// = board[coord[0]][coord[1]][1];
    char piece_color;// = board[coord[0]][coord[1]][0];
    int[] out0 = new int[2];
    int ff; // friendly fire
    for (int[] coord : coords)
    {
      piece = board[coord[0]][coord[1]][1];
      piece_color = board[coord[0]][coord[1]][0];
      switch (piece)
      {
        case 'n': 
          int[][] moves = {{2,1}, {2,-1}, 
                            {1,2}, {1,-2}, 
                            {-1,2},{-1,-2}, 
                            {-2,1},{-2,-1} };
          for (int[] move : moves)//(int i=0; i<8; i++)
          {
            out0 = operations.add_lists(coord, move);
            if (in_board_space(out0))
            {
              ff = friendly_fire(piece_color, board[out0[0]][out0[1]][0]);
              if (ff != 1)
              {
                out.add(new int[][] {coord, out0});
              }
            }
          }
          break;
        case 'q': // just use bishop and rook type moves
        case 'b':
          int M = Math.min(8-coord[0], 8-coord[1]);
          for (int i=1; i<M; i++)
          {
            out0 = operations.add_lists(coord, new int[] {i, i});
            ff = friendly_fire(piece_color, board[out0[0]][out0[1]][0]);
            if (ff==0){out.add(new int[][] {coord, out0});}
            else if (ff==1){break;}
            else 
            {
              out.add(new int[][] {coord, out0});
              break;
            }
          }
          
          M = Math.min(coord[0]+1,coord[1]+1);
          for (int i=1; i<M; i++)
          {
            out0 = operations.subtract_lists(coord, new int[] {i, i});
            ff = friendly_fire(piece_color, board[out0[0]][out0[1]][0]);
            if (ff==0){out.add(new int[][] {coord, out0});}
            else if (ff==1){break;}
            else 
            {
              out.add(new int[][] {coord, out0});
              break;
            }
          }
          M = Math.min(coord[0]+1,8-coord[1]);
          for (int i=1; i<M; i++)
          {
            out0 = operations.subtract_lists(coord, new int[] {i, (int)(-i)});
            ff = friendly_fire(piece_color, board[out0[0]][out0[1]][0]);
            if (ff==0){out.add(new int[][] {coord, out0});}
            else if (ff==1){break;}
            else 
            {
              out.add(new int[][] {coord, out0});
              break;
            }
          }
          M = Math.min(coord[1]+1,8-coord[0]);
          for (int i=1; i<M; i++)
          {
            out0 = operations.subtract_lists(coord, new int[] {(int)(-i),i});
            ff = friendly_fire(piece_color, board[out0[0]][out0[1]][0]);
            if (ff==0){out.add(new int[][] {coord, out0});}
            else if (ff==1){break;}
            else 
            {
              out.add(new int[][] {coord, out0});
              break;
            }
          }

          if (piece == 'b')
            break;
        case 'r':
          for (int i=1; i<8-coord[0]; i++)
          {
            out0 = operations.add_lists(coord, new int[] {i, 0});
            ff = friendly_fire(piece_color, board[out0[0]][out0[1]][0]);
            if (ff==0){out.add(new int[][] {coord, out0});}
            else if (ff==1){break;}
            else 
            {
              out.add(new int[][] {coord, out0});
              break;
            }
          }
          for (int i=1; i<coord[0]+1; i++)
          {
            out0 = operations.subtract_lists(coord, new int[] {i, 0});
            ff = friendly_fire(piece_color, board[out0[0]][out0[1]][0]);
            if (ff==0){out.add(new int[][] {coord, out0});}
            else if (ff==1){break;}
            else 
            {
              out.add(new int[][] {coord, out0});
              break;
            }
          }
          for (int i=1; i<coord[1]+1; i++)
          {
            out0 = operations.subtract_lists(coord, new int[] {0, i});
            ff = friendly_fire(piece_color, board[out0[0]][out0[1]][0]);
            if (ff==0){out.add(new int[][] {coord, out0});}
            else if (ff==1){break;}
            else 
            {
              out.add(new int[][] {coord, out0});
              break;
            }
          }
          for (int i=1; i<8-coord[1]; i++)
          {
            out0 = operations.add_lists(coord, new int[] {0, i});
            ff = friendly_fire(piece_color, board[out0[0]][out0[1]][0]);
            if (ff==0){out.add(new int[][] {coord, out0});}
            else if (ff==1){break;}
            else 
            {
              out.add(new int[][] {coord, out0});
              break;
            }
          }
          break;           

        case 'k':
          int[][] basis3 = {{1,0},{0,1}, {-1,1},{1,1}};
          int[] sign3 = {-1,1};
          for (int sgn : sign3)
          {
            for (int[] base : basis3)
            {
              int[] move = operations.multiply_list(base, sgn);
              out0 = operations.add_lists(coord, move);
              if (in_board_space(out0))
              {
                ff = friendly_fire(piece_color, board[out0[0]][out0[1]][0]);
                if (ff != 1)
                {
                  out.add(new int[][] {coord, out0});
                }
              }
            
            }
          }
          break;
        case 'p':
          int sgn = get_pawn_sign(piece_color);
          int[] mv0 = {sgn, 0};
          int[][] attacks = {{sgn, 1}, {sgn, -1}};
          out0 = operations.add_lists(coord, mv0);
          if (in_board_space(out0))
          {
            ff = friendly_fire(piece_color, board[out0[0]][out0[1]][0]);
            if (ff == 0)
            {
              out.add(new int[][] {coord, out0});
            }
          }
          for (int[] mva : attacks)
          {
            out0 = operations.add_lists(coord, mva);
            if (in_board_space(out0))
            {
              ff = friendly_fire(piece_color, board[out0[0]][out0[1]][0]);
              if (ff == -1)
              {
                out.add(new int[][] {coord, out0});
              }
            }
          }
          break;
        
        }// end switch statement
      //}
      }
    return out;
  }
  public static ArrayList<int[][]> get_moves(char[][][] board1, char color)
  { // returns all moves for a certain color
    ArrayList<int[]> my_coords = new ArrayList<int[]>();
    for (int i=0; i<8; i++)
    {
      for (int j=0; j<8; j++)
      {
        int[] coord = {i, j};
        if (color == board1[i][j][0])
          my_coords.add(coord);
      }
    }
    return get_moves(board1, my_coords);
  }
 


  private static boolean in_board_space(int[] out0)
  {
    if (out0[0]<8 && out0[0]>-1 && out0[1] < 8 && out0[1] >-1){return true;}
    else {return false;}
  }
  
  
  private static int get_pawn_sign(char color)
  {
    int out = 0;
    if (color=='b'){out = 1;}
    else if (color=='w'){out = -1;}
    return out;
  }
  private static int friendly_fire(char color0, char color1)
  {
    int out;
    if      (color1 == '0')   
      {out = 0;}
    else if (color0 == color1)
      {out = 1;}
    else                      
      {out = -1;}
    return out;
  }

}
