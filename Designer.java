//Programmed By Wasiqul Islam (completed at)1:25 AM 5/6/2004 last updated 7:54 PM 9/30/2004
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.io.*;

public class Designer extends JWindow implements KeyListener
{
   public static int  tmpE2Dcount;
   public static int tmpRR2Dcount;
   public static int tmpR2Dcount;
   public static int countLimit;
   private InstanceData objectArray[];
   private Random randomClassObject;
   private static Thread designerThread;
   private BufferedImage bi;
   private boolean busy = false;
   private boolean old = false;   //Used for help - not to build same design more than once in buffered image
   private static Designer app;
   private static final int SLEEP_TIME = 5000;
   private String extraMessage = null;
   private boolean firstTime = true;
   public long seed = 0;
   public Designer()
   {
      this( 0L );
   }
   public Designer( long s )
   {
      try
      {
         if( s != 0 )
            seed = s;
         else
             seed = 0L;
         Container container = getContentPane();
         container.setLayout( new BorderLayout() );
         addKeyListener( this );
         setSize(800, 600);
         container.setCursor( Cursor.getPredefinedCursor( Cursor.CROSSHAIR_CURSOR ) );
         bi = new BufferedImage( 800, 600, BufferedImage.TYPE_INT_RGB );
         initRandom();
         File file = new File( "LastSeedUsed.info" );
         DataOutputStream filePointer = new DataOutputStream( new FileOutputStream( file ) );
         filePointer.writeBytes( "" + seed );
         filePointer.close();
         setVisible( true );
         requestFocus();
      }
      catch( Throwable t )
      {
         t.printStackTrace();
         System.exit( 1 );
      }
   }
   public void keyPressed( KeyEvent event )
   {

   }
   public void keyReleased( KeyEvent event )
   {
      if( event.getKeyChar() == KeyEvent.VK_ESCAPE )
      {
         if( Designer.designerThread != null )
         {
            Designer.designerThread.stop();
            Designer.designerThread = null;
         }
         System.exit( 0 );
      }
      else if( event.getKeyChar() == KeyEvent.VK_SPACE )
      {
         if( !busy )
         {
            busy = true;
            click();
            busy = false;
         }
      }
      else if( event.getKeyChar() == KeyEvent.VK_ENTER )
      {
         if( !busy )
         {
            busy = true;
            changeColor();
            busy = false;
         }
      }
      else if( event.getKeyChar() == 'S' || event.getKeyChar() == 's' )
      {
         extraMessage = " Autoplay Stopped ";
         if( Designer.designerThread != null )
         {
            Designer.designerThread.stop();
            Designer.designerThread = null;
         }
         repaint();
      }
      else if( event.getKeyChar() == 'P' || event.getKeyChar() == 'p' )
      {
         extraMessage = " Autoplay Started ";
         if( Designer.designerThread != null )
         {
         extraMessage = " Autoplaying... ";
            return;
         }
         Designer.designerThread = new Thread()
         {
            public void run()
            {
               try
               {
                  while( true )
                  {
                     Designer.app.click();
                     Thread.sleep( Designer.SLEEP_TIME );
                     Designer.app.changeColor();                  
                     Thread.sleep( Designer.SLEEP_TIME );
                  }
               }
               catch( InterruptedException ie )
               {
                  System.err.println( ie.toString() );
               }
            }
         };
         Designer.designerThread.start();
      }
   }
   public void keyTyped( KeyEvent event )
   {

   }
   public void paint( Graphics g )
   {
      g.setColor( Color.black );
      if( firstTime )
      {
         g.fillRect( 0, 0, 800, 600 );
         int x, y, z;
         x = 75;
         y = 25;
         z = 100;
         g.setColor( Color.red );
         g.drawString( "Designer v1.4(7:54 PM 9/30/2004). Programmed by Wasiqul Islam" , z , x += y );
         g.drawString( "(E-mail address: wasiqul_islam@yahoo.com)" , z , x += y );
         g.drawString( "Please set resolution to 800X600 pixels (if not set)" , z , x += y );
         g.drawString( "You can use \'PrintScreenSysReq\' key( located near \'Delete\' key ) to copy image" , z , x += y );
         g.drawString( "and can paste it in any photo editor" , z , x += y );
         g.drawString( "Type the following keys for:" , z , x += y );
         g.setColor( Color.blue );
         g.drawString( "SPACE = to re-design" , z , x += y );
         g.drawString( "ENTER = to change color" , z , x += y );
         g.drawString( "ESC = to Exit" , z , x += y);
         g.drawString( "S = to stop Autoplay" , z , x += y);
         g.drawString( "P = to Autoplay" , z , x += y);
         return;
      }
      else
      {
         g.fillRect( 0, 0, 100, 100 );
      }
      if( old )
      {
         ( (Graphics2D)g ).drawImage( bi, 0, 0, this );
      }
      if( extraMessage != null )
      {
         g.setColor( Color.red );
         g.drawString( extraMessage , 0, 50 );
         extraMessage = null;
      }
      if( old )
      {
         return;
      }
      g.setColor( Color.red );
      g.drawString( "Please Wait" , 0, 10 );
      g.setColor( Color.blue );
      g.drawString( "processing..." , 0, 25 );
      Graphics2D g2d = ( Graphics2D )bi.getGraphics();
      g2d.setColor( Color.black );
      g2d.fillRect( 0, 0, 800, 600 );
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
      g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
      g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
      g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
      g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
      g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
      g2d.translate(400, 300);
      for( int i = 0; i < objectArray.length; i++ )
      {
         AffineTransform at1, at2;
         double dbl;
         if( objectArray[ i ].moveDirection )
            dbl =   ( (double)Math.PI * 2 ) / objectArray[ i ].moveUnit ;
         else
            dbl = -( (double)Math.PI * 2 ) / objectArray[ i ].moveUnit ;
         if( objectArray[ i ].moveDirection )
            at2 = AffineTransform.getRotateInstance( ( (double)Math.PI * 2 ) / objectArray[ i ].moveUnit );
         else
            at2 = AffineTransform.getRotateInstance( - ( (double)Math.PI * 2 ) / objectArray[ i ].moveUnit );
         Area a1, a2 = new Area( (Shape) objectArray[ i ].s );
         Area a3 = new Area( (Shape) objectArray[ i ].s );
         for( int say = 0; say < objectArray[ i ].moveUnit / 4; say ++ )
         {
            a2.transform( at2 );
            a3.add( a2 );
         }
         for( int j = 0; j < ( objectArray[i].moveUnit ); j++ )
         {
            g2d.setStroke( new BasicStroke( objectArray[i].stroke ) );
            g2d.setPaint( new GradientPaint( 50.0f, 37.0f, objectArray[ i ].colorArray[ j ] , 100.0f, 75.0f, objectArray[ i ].colorArray[ ( ( j + 1 ) % objectArray[ i ].colorArray.length ) ] ) );
            if( false ) //objectArray[ i ].drawType //fill and stroke is omitted in v1.2
            {
               g2d.draw( ( Shape ) objectArray[i].s );
            }
            else
            {
               if( ( objectArray[ i ].moveUnit - j ) < ( objectArray[ i ].moveUnit / 4 ) )
               {
                  a1 = new Area( (Shape) objectArray[ i ].s );
                  at1 = AffineTransform.getRotateInstance( dbl * ( ( objectArray[ i ].moveUnit - j ) ) );
                  a2 = (Area) a3.clone();
                  a2.transform( at1 );
                  a1.subtract( a2 );
                  g2d.fill( a1 );
               }
               else
                  g2d.fill( ( Shape )objectArray[i].s );
            }
            if( objectArray[i].moveDirection )
            {
               g2d.rotate( ( ( float )Math.PI * 2 ) / objectArray[i].moveUnit );
            }
            else
            {
                g2d.rotate( -( ( float )Math.PI * 2 ) / objectArray[i].moveUnit );
            }
         }
      }
      ( (Graphics2D)g ).drawImage( bi, 0, 0, this );
      old = true;
   }
   private void click()
   {
      old = false;
      firstTime = false;
      tmpE2Dcount = 0;
      tmpRR2Dcount = 0;
      tmpR2Dcount = 0;
      //initRandom();
      if( ( ( int )( random() * 200 ) ) == 0 )
      {
      countLimit = 20;
      objectArray = new InstanceData[ 1 + ( int )( random() * 12 ) ] ;
      }
      else if( ( ( int )( random() * 70 ) ) == 0 )
      {
      countLimit = 20;
      objectArray = new InstanceData[ 1 + ( int )( random() * 3 ) ] ;
      }
      else
      {
      countLimit = 2;
      objectArray = new InstanceData[ 1 + ( int )( random() * 4 ) ] ;
      }
      for( int i =0; i<objectArray.length; i++ )
      {
         objectArray[i] = new InstanceData();
      }
         repaint();
   }
   private void changeColor()
   {
      old = false;
      if( firstTime )
      {
         click();
         return;
      }
      firstTime = false;
      //initRandom();
      for( int i=0; i< objectArray.length; i++ )
      {
         for( int j=0; j< objectArray[i].colorArray.length; j++ )
         {
            objectArray[i].colorArray[j] = new Color( ( int )( random() * 256 ), ( int )( random() * 256 ), ( int )( random() * 256 ) );
         }
      }
     repaint();
   }
   public static void main( String args[] )
   {
      long tmp = 0;
      if( args.length >= 2 )
      {
         try
         {
            tmp = Long.parseLong( args[ 1 ] );
         }
         catch( Throwable t )
         {
            t.printStackTrace();
         }
      }
      if( tmp == 0 )
      {
         Designer.app = new Designer();
      }
      else
      {
         System.out.println( "seed: " + tmp );
         Designer.app = new Designer( tmp );
      }
      if( args.length >= 1 && args[ 0 ].equals( "autoplay" ) )
      {
         Designer.designerThread = new Thread()
         {
            public void run()
            {
               try
               {
                  Thread.sleep( Designer.SLEEP_TIME );
                  while( true )
                  {
                     Designer.app.click();
                     Thread.sleep( Designer.SLEEP_TIME );
                     Designer.app.changeColor();
                     Thread.sleep( Designer.SLEEP_TIME );
                  }
               }
               catch( InterruptedException ie )
               {
                  System.err.println( ie.toString() );
               }
            }
         };
         Designer.designerThread.start();
      }
   }
   private class InstanceData
   {
      private Object s;
      private Color colorArray[];
      private boolean moveDirection, drawType;
      private int moveUnit;
      private float stroke;
      public InstanceData()
     {
          stroke = (float)( 2 + ( random() * 13 ) );
          if(( (int)( random() * 19 ) ) == 0 && Designer.tmpE2Dcount <= Designer.countLimit )
         {
            int x, y;
            s = new Ellipse2D.Double( x = ( (int)( random() * 200 ) ), y = ( (int)( random() * 200 ) ), ( (int)( random() * ( 200 - x + 3) ) ), ( (int)( random() * ( 200 - y + 3) ) ) );
            Designer.tmpE2Dcount++;        
         }
         else if( ( (int)( random() * 13 ) ) == 0 && Designer.tmpRR2Dcount <= Designer.countLimit)
         { 
            int x, y;
            s = new RoundRectangle2D.Double( x = ( (int)( random() * 200 ) ) , y = ( (int)( random() * 200 ) ) , ( (int)( random() * ( 200 - x ) ) ) , ( (int)( random() * ( 200 - y ) ) ) , ( (int)( random() * ( 200 - x ) ) ) , ( (int)( random() * ( 200 - y ) ) )  );
            Designer.tmpRR2Dcount++;
         }
         else if( ( (int)( random() * 7 ) ) == 0 && Designer.tmpR2Dcount <= Designer.countLimit )
         {
            int x, y;
            s = new Rectangle2D.Double( x = ( (int)( random() * 199 ) ), y = ( (int)( random() * 199 ) ), ( (int)( random() * (200-x + 3) ) ), ( (int)( random() * (200-y + 3) ) ));
            Designer.tmpR2Dcount++;
         }
         else
         {
           s = new GeneralPath();
            ( ( GeneralPath )s ).moveTo(( (int)( random() * 200 ) ), ( (int)( random() * 200 ) ));
            int x = 3 + ( (int)( random() * 3 ) );
            int i;
            for(i=0; i < x; i++)
            {
               if(( (int)( random() * 2 ) ) == 0)
               {
                  ( ( GeneralPath ) s ).lineTo(( (int)( random() * 200 ) ), ( (int)( random() * 200 ) ));
               }
               else
               {
                  ( ( GeneralPath ) s ).curveTo( ( (int)( random() * 200 ) ), ( (int)( random() * 200 ) ), ( (int)( random() * 200 ) ), ( (int)( random() * 200 ) ), ( (int)( random() * 200 ) ), ( (int)( random() * 200 ) ) );
               }
            }
             ( ( GeneralPath ) s ).closePath();
         }
         if( ( (int)( random() * 2 ) ) == 0 )
         {
            moveDirection = true ;
         }
         else
         {
            moveDirection = false ;
         }
         if( ( (int)( random() * 2 ) ) == 0 )
         {
            drawType = true;
         }
         else
         {
            drawType = false;
         }
         moveUnit = 2 + ( (int)( random() * 16 ) ) ;
         colorArray = new Color[ moveUnit];
         for( int i = 0; i < colorArray.length; i++ )
         {
            colorArray[i] = new Color( (int)( random() * 256 )  ,  (int)( random() * 256 ) ,  (int)( random() * 256 ) );
         }
      }
   }
   private double random()
   {
      return randomClassObject.nextDouble();
   }
   private void initRandom()
   {
      if( seed == 0 )
         seed = System.currentTimeMillis();
      randomClassObject = new Random( seed );
   }
   public void update( Graphics g )
   {
      paint( g );
   }
}