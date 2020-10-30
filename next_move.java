import java.util.ArrayList;
import java.util.List;

public class next_move
{
  public static void main (String[] args)
  {
    //int M = args.size();
    ArrayList<ArrayList<String>> board0 = construct_board(args[0]);
    System.out.print(board0);
    //System.out.println(args[0]);
    //str_to_array("hshs ldld ldld ll");
  }
  private static ArrayList<ArrayList<String>> construct_board(String lst)
  {
    //String[] wlist = lst.split(" ");
    ArrayList<ArrayList<String>> out = new ArrayList<ArrayList<String>>();
    //List<List<String>> out = new List<List<String>>();
    for (String row: lst.split("="))
    {
      //System.out.println(row);
      ArrayList<String> row_l = new ArrayList<String>();
      for (String piece: row.split("_"))
      {
        row_l.add(piece);
        //System.out.print(piece+"\n");
      }
      out.add(row_l);
    }
    return out;
  }
  /** 
  private static String[][] str_to_array(String lst)
  {
    String[][] out = {{"s", "x", "p"}, {"1","2","3"}};
    //String in = "bk bq wq wq";
    String[] lst0 = lst.split(" ");
    int S = lst0.size();
    if (S == 4)
    {
      System.out.println("4");
    }


    return out;
  }
  **/
}
