import java.util.ArrayList;

public class moves_methods
{
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

          case 'b':
            byte[][] basis = {{1,-1},{1,1}};
            byte[] sign = {-1,1};
            byte[] multiplier = {1,2,3,4,5,6,7,8};
            for (byte sgn : sign)
            {
              for (byte[] base : basis)
              {
                for (byte i=0; i<8; i++)
                {
                  byte alpha = (byte) (multiplier[i]*sgn);
                  byte[] move = operations.multiply_list(base, alpha);
                  out0 = operations.add_lists(coord, move);
                  if (in_board_space(out0))
                  {
                    ff = friendly_fire(piece_color, board[out0[0]][out0[1]][0]);
                    if (ff==0){out.add(new byte[][] {coord, out0});}
                    else if (ff==1){break;}
                    else 
                    {
                      out.add(new byte[][] {coord, out0});
                      break;
                    }
                  }
                }
              }
            }
            break;
          case 'r':
            byte[][] basis1 = {{1,0},{0,1}};
            byte[] sign1 = {-1,1};
            byte[] multiplier1 = {1,2,3,4,5,6,7,8};
            for (byte sgn : sign1)
            {
              for (byte[] base : basis1)
              {
                for (byte i=0; i<8; i++)
                {
                  byte alpha = (byte) (multiplier1[i]*sgn);
                  byte[] move = operations.multiply_list(base, alpha);
                  out0 = operations.add_lists(coord, move);
                  if (in_board_space(out0))
                  {
                    ff = friendly_fire(piece_color, board[out0[0]][out0[1]][0]);
                    if (ff==0){out.add(new byte[][] {coord, out0});}
                    else if (ff==1){break;}
                    else 
                    {
                      out.add(new byte[][] {coord, out0});
                      break;
                    }
                  }
                }
              }
            }
            break;
          case 'q':
            byte[][] basis2 = {{1,0},{0,1}, {-1,1},{1,1}};
              byte[] sign2 = {-1,1};
              byte[] multiplier2 = {1,2,3,4,5,6,7,8};
              for (byte sgn : sign2)
              {
                for (byte[] base : basis2)
                {
                  for (byte i=0; i<8; i++)
                  {
                    byte alpha = (byte) (multiplier2[i]*sgn);
                    byte[] move = operations.multiply_list(base, alpha);
                    out0 = operations.add_lists(coord, move);
                    if (in_board_space(out0))
                    {
                      ff = friendly_fire(piece_color, board[out0[0]][out0[1]][0]);
                      if (ff==0){out.add(new byte[][] {coord, out0});}
                      else if (ff==1){break;}
                      else 
                      {
                        out.add(new byte[][] {coord, out0});
                        break;
                      }
                    }
                  }
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
  public static ArrayList<byte[][]> get_moves(char[][][] board, char color)
  { // returns all moves for a certain color
    ArrayList<byte[][]> out = new ArrayList<byte[][]>();
    //byte[][] mv = new byte[2][2];
    byte[] c1 = new byte[2];
    byte[] c2 = new byte[2];
    //byte[][] mv = new byte[2][2];
    ArrayList<byte[]> piece_moves = new ArrayList<byte[]>();
    
    for (byte i=0; i<8; i++)
    {
      for (byte j=0; j<8; j++)
      {
        if (board[i][j][0] == color)
        {
          c1[0] = i; c1[1] = j;
          piece_moves = moves(board, c1, color);
          for (int k=0; k<piece_moves.size(); k++)
          {
            c2 = piece_moves.get(k);
            byte[][] mv = {c1,c2};
            out.add(mv);//make_copy(c1,c2));
          }
          
        }
      }
    }
    return out;
  }
  private static ArrayList<byte[]> moves(char[][][] board, byte[] coords, char color)
  {
    ArrayList<byte[]> out = new ArrayList<byte[]>();
    char piece = board[coords[0]][coords[1]][1];
    char piece_color = board[coords[0]][coords[1]][0];
    byte[] out0 = new byte[2];
    //if (piece_color==color)
    //{
      byte ff; // friendly fire
      switch (piece)
      {
        case 'n': 
          byte[][] moves = {{2,1}, {2,-1}, 
                            {1,2}, {1,-2}, 
                            {-1,2},{-1,-2}, 
                            {-2,1},{-2,-1} };
          for (byte[] move : moves)//(byte i=0; i<8; i++)
          {
            out0 = operations.add_lists(coords, move);
            if (in_board_space(out0))
            {
              ff = friendly_fire(piece_color, board[out0[0]][out0[1]][0]);
              if (ff != 1)
              {
                out.add(out0);
              }
            }
          }
          break;

        case 'b':
          byte[][] basis = {{1,-1},{1,1}};
          byte[] sign = {-1,1};
          byte[] multiplier = {1,2,3,4,5,6,7,8};
          for (byte sgn : sign)
          {
            for (byte[] base : basis)
            {
              for (byte i=0; i<8; i++)
              {
                byte alpha = (byte) (multiplier[i]*sgn);
                byte[] move = operations.multiply_list(base, alpha);
                out0 = operations.add_lists(coords, move);
                if (in_board_space(out0))
                {
                  ff = friendly_fire(piece_color, board[out0[0]][out0[1]][0]);
                  if (ff==0){out.add(out0);}
                  else if (ff==1){break;}
                  else 
                  {
                    out.add(out0);
                    break;
                  }
                }
              }
            }
          }
          break;
        case 'r':
          byte[][] basis1 = {{1,0},{0,1}};
          byte[] sign1 = {-1,1};
          byte[] multiplier1 = {1,2,3,4,5,6,7,8};
          for (byte sgn : sign1)
          {
            for (byte[] base : basis1)
            {
              for (byte i=0; i<8; i++)
              {
                byte alpha = (byte) (multiplier1[i]*sgn);
                byte[] move = operations.multiply_list(base, alpha);
                out0 = operations.add_lists(coords, move);
                if (in_board_space(out0))
                {
                  ff = friendly_fire(piece_color, board[out0[0]][out0[1]][0]);
                  if (ff==0){out.add(out0);}
                  else if (ff==1){break;}
                  else 
                  {
                    out.add(out0);
                    break;
                  }
                }
              }
            }
          }
          break;
        case 'q':
          byte[][] basis2 = {{1,0},{0,1}, {-1,1},{1,1}};
            byte[] sign2 = {-1,1};
            byte[] multiplier2 = {1,2,3,4,5,6,7,8};
            for (byte sgn : sign2)
            {
              for (byte[] base : basis2)
              {
                for (byte i=0; i<8; i++)
                {
                  byte alpha = (byte) (multiplier2[i]*sgn);
                  byte[] move = operations.multiply_list(base, alpha);
                  out0 = operations.add_lists(coords, move);
                  if (in_board_space(out0))
                  {
                    ff = friendly_fire(piece_color, board[out0[0]][out0[1]][0]);
                    if (ff==0){out.add(out0);}
                    else if (ff==1){break;}
                    else 
                    {
                      out.add(out0);
                      break;
                    }
                  }
                }
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
              out0 = operations.add_lists(coords, move);
              if (in_board_space(out0))
              {
                ff = friendly_fire(piece_color, board[out0[0]][out0[1]][0]);
                if (ff != 1)
                {
                  out.add(out0);
                }
              }
            
            }
          }
          break;
        case 'p':
          byte sgn = get_pawn_sign(piece_color);
          byte[] mv0 = {sgn, 0};
          byte[][] attacks = {{sgn, 1}, {sgn, -1}};
          out0 = operations.add_lists(coords, mv0);
          if (in_board_space(out0))
          {
            ff = friendly_fire(piece_color, board[out0[0]][out0[1]][0]);
            if (ff == 0)
            {
              out.add(out0);
            }
          }
          for (byte[] mva : attacks)
          {
            out0 = operations.add_lists(coords, mva);
            if (in_board_space(out0))
            {
              ff = friendly_fire(piece_color, board[out0[0]][out0[1]][0]);
              if (ff == -1)
              {
                out.add(out0);
              }
            }
          }
          break;
        
      }// end switch statement
    //}

    return out;
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
