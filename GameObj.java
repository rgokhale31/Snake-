
import java.awt.Graphics;

/** An object in the game. 
 *
 *  Game objects exist in the game court. They have a position, 
 *  velocity, size and bounds. Their velocity controls how they 
 *  move; their position should always be within their bounds.
 */ 
public class GameObj {

 /** Current position of the object (in terms of graphics coordinates)
  *  
  * Coordinates are given by the upper-left hand corner of the object.
  * This position should always be within bounds.
  *  0 <= pos_x <= max_x 
  *  0 <= pos_y <= max_y 
  */
 public int pos_x; 
 public int pos_y;

 /** Size of object, in pixels */
 public int width;
 public int height;
 
 /** Velocity: number of pixels to move every time move() is called */
 public int v_x;
 public int v_y;

 /** Upper bounds of the area in which the object can be positioned.  
  *    Maximum permissible x, y positions for the upper-left 
  *    hand corner of the object
  */
 public int max_x;
 public int max_y;
 
 
 public final int COURT_WIDTH = 300;
 public final int COURT_HEIGHT = 300;
 
 /**
  * Constructor 
  */
 public GameObj(int pos_x, int pos_y, int v_x, int v_y, 
  int width, int height){
  this.v_x = v_x;
  this.v_y = v_y;
  this.pos_x = pos_x;
  this.pos_y = pos_y;
  this.width = width;
  this.height = height;
    
  // take the width and height into account when setting the 
  // bounds for the upper left corner of the object.
  this.max_x = COURT_WIDTH - width;
  this.max_y = COURT_HEIGHT - height;
 
 } 
 
 
 /**
  * Moves the object by its velocity.  Ensures that the object does
  * not go outside its bounds by clipping.
  */
 public void move(){
  pos_x += v_x;
  pos_y += v_y;
 
  clip();
 }

 /**
  * Prevents the object from going outside of the bounds of the area 
  * designated for the object. (i.e. Object cannot go outside of the 
  * active area the user defines for it).
  */       
 public void clip(){
  if (pos_x < 0) pos_x = 0;
  else if (pos_x > max_x) pos_x = max_x;

  if (pos_y < 0) pos_y = 0;
  else if (pos_y > max_y) pos_y = max_y;
 } 

 /**
  * Determine whether this game object is currently intersecting
  * another object.
  * 
  * Intersection is determined by comparing bounding boxes. If the 
  * bounding boxes overlap, then an intersection is considered to occur.
  * 
  * @param obj : other object
  * @return whether this object intersects the other object.
  */
 public boolean intersects(GameObj obj){
  return (pos_x + width >= obj.pos_x
    && pos_y + height >= obj.pos_y 
    && obj.pos_x + obj.width >= pos_x 
    && obj.pos_y + obj.height >= pos_y);
 }

 
 /**
  * Determine whether this game object will intersect another in the
  * next time step, assuming that both objects continue with their 
  * current velocity.
  * 
  * @param obj : other object
  * @return whether an intersection will occur.
  */  
 public boolean willIntersect(GameObj obj){
  int next_x = pos_x + v_x;
  int next_y = pos_y + v_y;
  int next_obj_x = obj.pos_x + obj.v_x;
  int next_obj_y = obj.pos_y + obj.v_y;
  return (next_x + width >= next_obj_x
    && next_y + height >= next_obj_y
    && next_obj_x + obj.width >= next_x 
    && next_obj_y + obj.height >= next_y);
 }

 
 /** Update the direction of the object in response to the user turning 
  *  in a certain direction. If the direction is
  *  null, this method has no effect on the object. */
 public void turn(Direction d) {
  if (d == null) return;
  switch (d) {
  case UP: 
   v_x = 0;
   v_y = -v_y;  
   break;  
  case DOWN: 
   v_x = 0;
   v_y = v_y; 
   break;
  case LEFT:  
   v_x = -v_x;    
   v_y = 0;
      break;
  case RIGHT: 
   v_x = 5; 
   v_y = 0;
   break;
  }
 } 
 
 /** Determine whether the game object will hit a 
  *  wall in the next time step. If so, return the direction
  *  of the wall in relation to this game object.
  *  
  * @return direction of impending wall, null if all clear.
  */
 public Direction hitWall() {
  if (pos_x + v_x < 0)
   return Direction.LEFT;
  else if (pos_x + v_x > max_x)
   return Direction.RIGHT;
  if (pos_y + v_y < 0)
   return Direction.UP;
  else if (pos_y + v_y > max_y)
   return Direction.DOWN;
  else return null;
 }

 /** Determine whether the game object will hit another 
  *  object in the next time step. If so, return the direction
  *  of the other object in relation to this game object.
  *  
  * @return direction of impending object, null if all clear.
  */
 public Direction hitObj(GameObj other) {

  if (this.willIntersect(other)) {
   double dx = other.pos_x + other.width /2 - (pos_x + width /2);
   double dy = other.pos_y + other.height/2 - (pos_y + height/2);

   double theta = Math.acos(dx / (Math.sqrt(dx * dx + dy *dy)));
   double diagTheta = Math.atan2(height / 2, width / 2);

   if (theta <= diagTheta ) {
     return Direction.RIGHT;
   } else if ( theta > diagTheta && theta <= Math.PI - diagTheta ) {
     if ( dy > 0 ) {
       // Coordinate system for GUIs is switched
       return Direction.DOWN;
     } else {
       return Direction.UP;
     }
   } else {
     return Direction.LEFT;
   }
  } else {
   return null;
  }

 }
 
 /**
  * Default draw method that provides how the object should be drawn 
  * in the GUI. This method does not draw anything. Subclass should 
  * override this method based on how their object should appear.
  *  
  */
 public void draw(Graphics g) {
 }
 
}