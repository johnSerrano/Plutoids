package johnserrano.tank;

//wrapper class for touchable

import johnserrano.tank.sprites.Touchable;

class Pointer {
    private Touchable touched;

    public Pointer(Touchable t)
    {
        touched = t;
    }

    public Touchable getTouchable()
    {
        if (touched == null)
            return new Touchable() {
                @Override
                public void handleActionDown(int x, int y) { }

                @Override
                public void handleActionMove(int x, int y) {}

                @Override
                public void handleActionUp() {}
            };

        return touched;
    }
}
