
package org.puzzlecube;

public interface Game {

    public void load(Renderer renderer);
    public void update(Renderer renderer, double deltaTime);
    public void draw(Renderer renderer);
    public void keyPressed(Renderer renderer, int key);
    public void keyReleased(Renderer renderer, int key);
    public void mouseButtonPressed(Renderer renderer, int button);
    public void mouseButtonReleased(Renderer renderer, int button);
    public void cursorMoved(Renderer renderer, double x, double y);

}
