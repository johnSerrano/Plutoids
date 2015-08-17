package johnserrano.tank.sprites;

public interface Touchable {
    void handleActionDown(int x, int y);

    /**
      * method to be called when pointer is  placed
      */


    void handleActionMove(int x, int y);
        /**
         *  method to be called when pointer is moved
         */

    void handleActionUp();
        /**
         *  method to be called when pointer is removed
         */
}
