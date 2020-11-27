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
    ArrayList<byte[][]> mvs = get_moves(board, 'b');
    operations.print_board(board);
    for (byte[][] mv : mvs)
      System.out.println(mv[1][0] + ", " + mv[1][1]);

  }
    public static ArrayList<byte[][]> get_moves(char[][][] board, ArrayList<byte[]> coords)
  {
    ArrayList<byte[][]> out = new ArrayList<byte[][]>();
    char piece;// = board[coord[0]][coord[1]][1];
    char piece_color;// = board[coord[0]][coord[1]][0];
    byte[] out0 = new byte[2];
    byte ff; // friendly fire
    for (byte[] coord : coords)
    {
      piece = board[coord[0]][coord[1]][1];
      piece_color = board[coord[0]][coord[1]][0];
      //byte[] out0 = new byte[2];
      //if (piece_color==color)
      //{
        switch (piece)
        {
          case 'n': 
            byte[][] moves = {{2,1}, {2,-1}, 
                              {1,2}, {1,-2}, 
                              {-1,2},{-1,-2}, 
                              {-2,1},{-2,-1} };
            for (byte[] move : moves)//(byte i=0; i<8; i++)
            {
              out0 = operations.add_lists(coord, move);
              if (in_board_space(out0))
              {
                ff = friendly_fire(piece_color, board[out0[0]][out0[1]][0]);
                if (ff != 1)
                {
                  out.add(new byte[][] {coord, out0});
                }
              }
            }
            break;
          case 'q': // just use bishop and rook type moves
          case 'b':
            int M = Math.min(8-coord[0], 8-coord[1]);
            for (byte i=1; i<M; i++)
            {
              out0 = operations.add_lists(coord, new byte[] {i, i});
              ff = friendly_fire(piece_color, board[out0[0]][out0[1]][0]);
              if (ff==0){out.add(new byte[][] {coord, out0});}
              else if (ff==1){break;}
              else 
              {
                out.add(new byte[][] {coord, out0});
                break;
              }
            }
            
            M = Math.min(coord[0]+1,coord[1]+1);
            for (byte i=1; i<M; i++)
            {
              out0 = operations.subtract_lists(coord, new byte[] {i, i});
              ff = friendly_fire(piece_color, board[out0[0]][out0[1]][0]);
              if (ff==0){out.add(new byte[][] {coord, out0});}
              else if (ff==1){break;}
              else 
              {
                out.add(new byte[][] {coord, out0});
                break;
              }
            }
            M = Math.min(coord[0]+1,8-coord[1]);
            for (byte i=1; i<M; i++)
            {
              out0 = operations.subtract_lists(coord, new byte[] {i, (byte)(-i)});
              ff = friendly_fire(piece_color, board[out0[0]][out0[1]][0]);
              if (ff==0){out.add(new byte[][] {coord, out0});}
              else if (ff==1){break;}
              else 
              {
                out.add(new byte[][] {coord, out0});
                break;
              }
            }
            M = Math.min(coord[1]+1,8-coord[0]);
            for (byte i=1; i<M; i++)
            {
              out0 = operations.subtract_lists(coord, new byte[] {(byte)(-i),i});
              ff = friendly_fire(piece_color, board[out0[0]][out0[1]][0]);
              if (ff==0){out.add(new byte[][] {coord, out0});}
              else if (ff==1){break;}
              else 
              {
                out.add(new byte[][] {coord, out0});
                break;
              }
            }

            if (piece == 'b')
              break;
          case 'r':
            for (byte i=1; i<8-coord[0]; i++)
            {
              out0 = operations.add_lists(coord, new byte[] {i, 0});
              ff = friendly_fire(piece_color, board[out0[0]][out0[1]][0]);
              if (ff==0){out.add(new byte[][] {coord, out0});}
              else if (ff==1){break;}
              else 
              {
                out.add(new byte[][] {coord, out0});
                break;
              }
            }
            for (byte i=1; i<coord[0]+1; i++)
            {
              out0 = operations.subtract_lists(coord, new byte[] {i, 0});
              ff = friendly_fire(piece_color, board[out0[0]][out0[1]][0]);
              if (ff==0){out.add(new byte[][] {coord, out0});}
              else if (ff==1){break;}
              else 
              {
                out.add(new byte[][] {coord, out0});
                break;
              }
            }
            for (byte i=1; i<coord[1]+1; i++)
            {
              out0 = operations.subtract_lists(coord, new byte[] {0, i});
              ff = friendly_fire(piece_color, board[out0[0]][out0[1]][0]);
              if (ff==0){out.add(new byte[][] {coord, out0});}
              else if (ff==1){break;}
              else 
              {
                out.add(new byte[][] {coord, out0});
                break;
              }
            }
            for (byte i=1; i<8-coord[1]; i++)
            {
              out0 = operations.add_lists(coord, new byte[] {0, i});
              ff = friendly_fire(piece_color, board[out0[0]][out0[1]][0]);
              if (ff==0){out.add(new byte[][] {coord, out0});}
              else if (ff==1){break;}
              else 
              {
                out.add(new byte[][] {coord, out0});
                break;
              }
            }
            break;           

          case 'k':
            byte[][] basis3 = {{1,0},{0,1}, {-1,1},{1,1}};
            byte[] sign3 = {-1,1};
            //byte[] multiplier3 = {1};
            for (byte sgn : sign3)
            {
              for (byte[] base : basis3)
              {
              
                //byte alpha = (byte) (multiplier3[i]*sgn);
                byte[] move = operations.multiply_list(base, sgn);
                out0 = operations.add_lists(coord, move);
                if (in_board_space(out0))
                {
                  ff = friendly_fire(piece_color, board[out0[0]][out0[1]][0]);
                  if (ff != 1)
                  {
                    out.add(new byte[][] {coord, out0});
                  }
                }
              
              }
            }
            break;
          case 'p':
            byte sgn = get_pawn_sign(piece_color);
            byte[] mv0 = {sgn, 0};
            byte[][] attacks = {{sgn, 1}, {sgn, -1}};
            out0 = operations.add_lists(coord, mv0);
            if (in_board_space(out0))
            {
              ff = friendly_fire(piece_color, board[out0[0]][out0[1]][0]);
              if (ff == 0)
              {
                out.add(new byte[][] {coord, out0});
              }
            }
            for (byte[] mva : attacks)
            {
              out0 = operations.add_lists(coord, mva);
              if (in_board_space(out0))
              {
                ff = friendly_fire(piece_color, board[out0[0]][out0[1]][0]);
                if (ff == -1)
                {
                  out.add(new byte[][] {coord, out0});
                }
              }
            }
            break;
          
        }// end switch statement
      //}
      }
    return out;
  }
  public static ArrayList<byte[][]> get_moves(char[][][] board1, char color)
  { // returns all moves for a certain color
    //char new_move_color;
    //if (color=='w'){ new_move_color = 'b'; }
    //else { new_move_color = 'w'; }
    ArrayList<byte[]> my_coords = new ArrayList<byte[]>();
    //ArrayList<byte[]> enemy_coords = new ArrayList<byte[]>();
    for (byte i=0; i<8; i++)
    {
      for (byte j=0; j<8; j++)
      {
        byte[] coord = {i, j};
        if (color == board1[i][j][0])
          my_coords.add(coord);
        //else if (new_move_color == board1[i][j][0])
        //  enemy_coords.add(coord);
      }
    }
    return get_moves(board1, my_coords);
  }
 


  private static boolean in_board_space(byte[] out0)
  {
    if (out0[0]<8 && out0[0]>-1 && out0[1] < 8 && out0[1] >-1){return true;}
    else {return false;}
  }
  
  
  private static byte get_pawn_sign(char color)
  {
    byte out = 0;
    if (color=='b'){out = 1;}
    else if (color=='w'){out = -1;}
    return out;
  }
  private static byte friendly_fire(char color0, char color1)
  {
    byte out;
    if      (color1 == '0')   
      {out = 0;}
    else if (color0 == color1)
      {out = 1;}
    else                      
      {out = -1;}
    return out;
  }

}
