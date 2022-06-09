
public class MyThread extends Thread
{

   private boolean shouldStop = false;
   public static final int SLEEP_TIME = 5000;   


   public void stopIt()
   {
      shouldStop = true;  
   }

   public void run()
   {
      try
      {
         while( true )
         {
            Designer.app.click();
            Thread.sleep( SLEEP_TIME );
            Designer.app.changeColor();                  
            Thread.sleep( SLEEP_TIME );
            if( shouldStop )
            {
               break;
            }
         }
      }
      catch( InterruptedException ie )
      {
         System.err.println( ie.toString() );
      }
   }

}