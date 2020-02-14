import java.awt.Color;
import java.util.*;

class MinMax extends GomokuPlayer {
    
    Color human;
    Color ai;
 
    public Move chooseMove(Color[][] board, Color maximizer) {
        
        ai = maximizer;
        if(ai == Color.black){
            human = Color.white;
        }else{
            human = Color.black;
        }
        int[] move = (int []) minimax(board,5,-100,100,ai).get("move");
        return new Move(move[0],move[1]);
        
    } // chooseMove()
    
    public List<int []> getAvailableMoves(Color [][] board){
        List<int []> moves = new ArrayList<>();
          for (int row = 0; row < GomokuBoard.ROWS; row++){
                  for (int col = 0; col < GomokuBoard.COLS; col++){
                      if (board[row][col] == null){
                        moves.add(new int[]{row,col});
                    }
                }
            }
          return moves;
    }
    
    public Map<String,Object> minimax(Color [][] board, int depth, int alpha, int beta, Color player){
          
        List<int []> availableMoves = getAvailableMoves(board);
        
        if(depth == 0){
            
            if(checkWin(board,ai)){return new LinkedHashMap<String, Object>(){{put("move", null);put("score", 2);}};} 
            
            else if(checkFourAndTwoOpens(board,ai)){return new LinkedHashMap<String, Object>(){{put("move", null);put("score", 2);}};}
            
            else if(checkWin(board,human)){return new LinkedHashMap<String, Object>(){{put("move", null);put("score", 0);}};} 
            
            else if(checkFourAndTwoOpens(board,human)){ return new LinkedHashMap<String, Object>(){{put("move", null);put("score", 0);}};}
            
            else{ return new LinkedHashMap<String, Object>(){{put("move", null);put("score", 1);}};}
        }
        
        //Depth Not Zero
        if(checkWin(board,ai)){return new LinkedHashMap<String, Object>(){{put("move", null);put("score", 2);}};}
        
        else if(checkWin(board,human)){ return new LinkedHashMap<String, Object>(){{put("move", null);put("score", 0);}};}
        
        else if(availableMoves.isEmpty()){ return new LinkedHashMap<String, Object>(){{put("move", null);put("score", 1);}};}

        if(player == ai){
            
            int score = -100;
            int[] played = new int[1];        
            
            for(int[] move:availableMoves){
                
                Color [][] boardCopy = board.clone(); 
                boardCopy[move[0]][move[1]] = player;
                int newScore = (int) minimax(boardCopy,depth-1,alpha,beta,human).get("score");
                
                if(newScore > score){
                    score = newScore;
                    played = move;
                }
                
                boardCopy[move[0]][move[1]] = null;//Reset square
                alpha = Math.max(alpha,score);
                
                if(alpha >= beta){
                    break;
                }
            }          
            
            final int playerScore = score;
            final int[] playerMove = played;
            return new LinkedHashMap<String, Object>(){{put("score", playerScore);put("move", playerMove);}};
            
        }else{
            
            int score = 100;
            int[] played = new int[1];
            
            for(int[] move:availableMoves){
                
                Color [][] boardCopy = board.clone(); 
                boardCopy[move[0]][move[1]] = player;
                int newScore = (int) minimax(boardCopy,depth-1,alpha,beta,ai).get("score");
                
                if(newScore < score){
                    score = newScore;
                    played = move;

                }
                
                boardCopy[move[0]][move[1]] = null;//Reset square
                beta = Math.min(beta,score);
                
                if(alpha >= beta){
                    break;
                }
            }
            
            final int playerScore = score;
            final int[] playerMove = played;
            return new LinkedHashMap<String, Object>(){{put("score", playerScore);put("move", playerMove);}};
            
        }
    }
    
    //Check For Win
    public boolean checkWin(Color [][] board, Color player){
    
          // Check Rows
          for (int row = 0; row < GomokuBoard.ROWS; row++ ){
              for (int col = 0; col < GomokuBoard.COLS - 4 ; col++){
                  if (board[row][col] == player 
                  && board[row][col+1] == player 
                  && board[row][col+2] == player 
                  && board[row][col+3] == player
                  && board[row][col+4] == player
                  ){
                      return true;
                  }           
              }
          }
          
          // Check Columns
          for (int col = 0; col < GomokuBoard.COLS; col++ ){
              for (int row = 0; row < GomokuBoard.ROWS - 4 ; row++){
                  if (board[row][col] == player 
                  && board[row+1][col] == player 
                  && board[row+2][col] == player 
                  && board[row+3][col] == player
                  && board[row+4][col] == player
                  ){
                      return true;
                  }           
              }
          }
          
          // Check Ascending Diagonals
          for (int row = 4 ; row<GomokuBoard.ROWS ; row++){
              for (int col = 0; col<GomokuBoard.COLS-4 ; col++){
                  if (board[row][col] == player 
                  && board[row-1][col+1] == player 
                  && board[row-2][col+2] == player 
                  && board[row-3][col+3] == player
                  && board[row-4][col+4] == player
                  ){
                      return true;
                  }
              }
          }
          
          // Check Descending Diagonals
          for (int row = 4; row < GomokuBoard.ROWS; row++){
              for (int col=4; col<GomokuBoard.COLS; col++){
                  if (board[row][col] == player 
                  && board[row-1][col-1] == player 
                  && board[row-2][col-2] == player 
                  && board[row-3][col-3] == player
                  && board[row-4][col-4] == player
                  ){
                      return true;
                  }
              }
          }
          return false;
    }
    
    //Check For Two Open ends with Middle Four in Sucession
    public boolean checkFourAndTwoOpens(Color [][] board, Color player){
    
           // Check Rows
          for (int row = 0; row < GomokuBoard.ROWS; row++ ){
              for (int col = 0; col < GomokuBoard.COLS - 5 ; col++){
                  if (board[row][col] == null 
                  && board[row][col+1] == player 
                  && board[row][col+2] == player 
                  && board[row][col+3] == player
                  && board[row][col+4] == player
                  && board[row][col+5] == null
                  ){
                      return true;
                  }           
              }
          }
          
          // Check Columns
          for (int col = 0; col < GomokuBoard.COLS; col++ ){
              for (int row = 0; row < GomokuBoard.ROWS - 5 ; row++){
                  if (board[row][col] == null 
                  && board[row+1][col] == player 
                  && board[row+2][col] == player 
                  && board[row+3][col] == player
                  && board[row+4][col] == player
                  && board[row+5][col] == null
                  ){
                      return true;
                  }           
              }
          }
          
          // Check Ascending Diagonals
          for (int row = 5 ; row<GomokuBoard.ROWS ; row++){
              for (int col = 0; col<GomokuBoard.COLS - 5 ; col++){
                  if (board[row][col] == null 
                  && board[row-1][col+1] == player 
                  && board[row-2][col+2] == player 
                  && board[row-3][col+3] == player
                  && board[row-4][col+4] == player
                  && board[row-5][col+5] == null
                  ){
                      return true;
                  }
              }
          }
          
          // Check Descending Diagonals
          for (int row = 5; row < GomokuBoard.ROWS; row++){
              for (int col = 5; col<GomokuBoard.COLS; col++){
                  if (board[row][col] == null 
                  && board[row-1][col-1] == player 
                  && board[row-2][col-2] == player 
                  && board[row-3][col-3] == player
                  && board[row-4][col-4] == player
                  && board[row-5][col-5] == null
                  ){
                      return true;
                  }
              }
          }
          return false;
    }
} // class RandomPlayer
