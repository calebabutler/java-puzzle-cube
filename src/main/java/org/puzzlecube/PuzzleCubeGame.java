
package org.puzzlecube;

import static org.lwjgl.glfw.GLFW.*;

public class PuzzleCubeGame implements Game {

    private boolean cameraMode, isLeftShiftHeld, isLeftControlHeld;
    private double oldX, oldY;

    private enum MoveType {
        NO_MOVE,
        WHITE,
        GREEN,
        BLUE,
        ORANGE,
        RED,
        YELLOW;
    }

    private MoveType move;
    private boolean isFirstMove;

    private Cubie[] cube;

    public PuzzleCubeGame() {
        cameraMode = false;
        move = MoveType.NO_MOVE;
        isFirstMove = false;
        oldX = 0.f;
        oldY = 0.f;
        isLeftShiftHeld = false;
        isLeftControlHeld = false;

        cube = new Cubie[] {
            // Top
            new Cubie(-0.1f, 0.1f, -0.1f, Renderer.FaceColor.WHITE, Renderer.FaceColor.BLACK,
                      Renderer.FaceColor.BLUE, Renderer.FaceColor.ORANGE,
                      Renderer.FaceColor.BLACK, Renderer.FaceColor.BLACK),
            new Cubie(0.f, 0.1f, -0.1f, Renderer.FaceColor.WHITE, Renderer.FaceColor.BLACK,
                      Renderer.FaceColor.BLUE, Renderer.FaceColor.BLACK,
                      Renderer.FaceColor.BLACK, Renderer.FaceColor.BLACK),
            new Cubie(0.1f, 0.1f, -0.1f, Renderer.FaceColor.WHITE, Renderer.FaceColor.BLACK,
                      Renderer.FaceColor.BLUE, Renderer.FaceColor.BLACK,
                      Renderer.FaceColor.RED, Renderer.FaceColor.BLACK),
            new Cubie(-0.1f, 0.1f, 0.f, Renderer.FaceColor.WHITE, Renderer.FaceColor.BLACK,
                      Renderer.FaceColor.BLACK, Renderer.FaceColor.ORANGE,
                      Renderer.FaceColor.BLACK, Renderer.FaceColor.BLACK),
            new Cubie(0.f, 0.1f, 0.f, Renderer.FaceColor.WHITE, Renderer.FaceColor.BLACK,
                      Renderer.FaceColor.BLACK, Renderer.FaceColor.BLACK,
                      Renderer.FaceColor.BLACK, Renderer.FaceColor.BLACK),
            new Cubie(0.1f, 0.1f, 0.f, Renderer.FaceColor.WHITE, Renderer.FaceColor.BLACK,
                      Renderer.FaceColor.BLACK, Renderer.FaceColor.BLACK,
                      Renderer.FaceColor.RED, Renderer.FaceColor.BLACK),
            new Cubie(-0.1f, 0.1f, 0.1f, Renderer.FaceColor.WHITE, Renderer.FaceColor.GREEN,
                      Renderer.FaceColor.BLACK, Renderer.FaceColor.ORANGE,
                      Renderer.FaceColor.BLACK, Renderer.FaceColor.BLACK),
            new Cubie(0.f, 0.1f, 0.1f, Renderer.FaceColor.WHITE, Renderer.FaceColor.GREEN,
                      Renderer.FaceColor.BLACK, Renderer.FaceColor.BLACK,
                      Renderer.FaceColor.BLACK, Renderer.FaceColor.BLACK),
            new Cubie(0.1f, 0.1f, 0.1f, Renderer.FaceColor.WHITE, Renderer.FaceColor.GREEN,
                      Renderer.FaceColor.BLACK, Renderer.FaceColor.BLACK,
                      Renderer.FaceColor.RED, Renderer.FaceColor.BLACK),

            // Middle
            new Cubie(-0.1f, 0.f, -0.1f, Renderer.FaceColor.BLACK, Renderer.FaceColor.BLACK,
                      Renderer.FaceColor.BLUE, Renderer.FaceColor.ORANGE,
                      Renderer.FaceColor.BLACK, Renderer.FaceColor.BLACK),
            new Cubie(0.f, 0.f, -0.1f, Renderer.FaceColor.BLACK, Renderer.FaceColor.BLACK,
                      Renderer.FaceColor.BLUE, Renderer.FaceColor.BLACK,
                      Renderer.FaceColor.BLACK, Renderer.FaceColor.BLACK),
            new Cubie(0.1f, 0.f, -0.1f, Renderer.FaceColor.BLACK, Renderer.FaceColor.BLACK,
                      Renderer.FaceColor.BLUE, Renderer.FaceColor.BLACK,
                      Renderer.FaceColor.RED, Renderer.FaceColor.BLACK),
            new Cubie(-0.1f, 0.f, 0.f, Renderer.FaceColor.BLACK, Renderer.FaceColor.BLACK,
                      Renderer.FaceColor.BLACK, Renderer.FaceColor.ORANGE,
                      Renderer.FaceColor.BLACK, Renderer.FaceColor.BLACK),
            new Cubie(0.f, 0.f, 0.f, Renderer.FaceColor.BLACK, Renderer.FaceColor.BLACK,
                      Renderer.FaceColor.BLACK, Renderer.FaceColor.BLACK,
                      Renderer.FaceColor.BLACK, Renderer.FaceColor.BLACK),
            new Cubie(0.1f, 0.f, 0.f, Renderer.FaceColor.BLACK, Renderer.FaceColor.BLACK,
                      Renderer.FaceColor.BLACK, Renderer.FaceColor.BLACK,
                      Renderer.FaceColor.RED, Renderer.FaceColor.BLACK),
            new Cubie(-0.1f, 0.f, 0.1f, Renderer.FaceColor.BLACK, Renderer.FaceColor.GREEN,
                      Renderer.FaceColor.BLACK, Renderer.FaceColor.ORANGE,
                      Renderer.FaceColor.BLACK, Renderer.FaceColor.BLACK),
            new Cubie(0.f, 0.f, 0.1f, Renderer.FaceColor.BLACK, Renderer.FaceColor.GREEN,
                      Renderer.FaceColor.BLACK, Renderer.FaceColor.BLACK,
                      Renderer.FaceColor.BLACK, Renderer.FaceColor.BLACK),
            new Cubie(0.1f, 0.f, 0.1f, Renderer.FaceColor.BLACK, Renderer.FaceColor.GREEN,
                      Renderer.FaceColor.BLACK, Renderer.FaceColor.BLACK,
                      Renderer.FaceColor.RED, Renderer.FaceColor.BLACK),

            // Bottom
            new Cubie(-0.1f, -0.1f, -0.1f, Renderer.FaceColor.BLACK, Renderer.FaceColor.BLACK,
                      Renderer.FaceColor.BLUE, Renderer.FaceColor.ORANGE,
                      Renderer.FaceColor.BLACK, Renderer.FaceColor.YELLOW),
            new Cubie(0.f, -0.1f, -0.1f, Renderer.FaceColor.BLACK, Renderer.FaceColor.BLACK,
                      Renderer.FaceColor.BLUE, Renderer.FaceColor.BLACK,
                      Renderer.FaceColor.BLACK, Renderer.FaceColor.YELLOW),
            new Cubie(0.1f, -0.1f, -0.1f, Renderer.FaceColor.BLACK, Renderer.FaceColor.BLACK,
                      Renderer.FaceColor.BLUE, Renderer.FaceColor.BLACK,
                      Renderer.FaceColor.RED, Renderer.FaceColor.YELLOW),
            new Cubie(-0.1f, -0.1f, 0.f, Renderer.FaceColor.BLACK, Renderer.FaceColor.BLACK,
                      Renderer.FaceColor.BLACK, Renderer.FaceColor.ORANGE,
                      Renderer.FaceColor.BLACK, Renderer.FaceColor.YELLOW),
            new Cubie(0.f, -0.1f, 0.f, Renderer.FaceColor.BLACK, Renderer.FaceColor.BLACK,
                      Renderer.FaceColor.BLACK, Renderer.FaceColor.BLACK,
                      Renderer.FaceColor.BLACK, Renderer.FaceColor.YELLOW),
            new Cubie(0.1f, -0.1f, 0.f, Renderer.FaceColor.BLACK, Renderer.FaceColor.BLACK,
                      Renderer.FaceColor.BLACK, Renderer.FaceColor.BLACK,
                      Renderer.FaceColor.RED, Renderer.FaceColor.YELLOW),
            new Cubie(-0.1f, -0.1f, 0.1f, Renderer.FaceColor.BLACK, Renderer.FaceColor.GREEN,
                      Renderer.FaceColor.BLACK, Renderer.FaceColor.ORANGE,
                      Renderer.FaceColor.BLACK, Renderer.FaceColor.YELLOW),
            new Cubie(0.f, -0.1f, 0.1f, Renderer.FaceColor.BLACK, Renderer.FaceColor.GREEN,
                      Renderer.FaceColor.BLACK, Renderer.FaceColor.BLACK,
                      Renderer.FaceColor.BLACK, Renderer.FaceColor.YELLOW),
            new Cubie(0.1f, -0.1f, 0.1f, Renderer.FaceColor.BLACK, Renderer.FaceColor.GREEN,
                      Renderer.FaceColor.BLACK, Renderer.FaceColor.BLACK,
                      Renderer.FaceColor.RED, Renderer.FaceColor.YELLOW),
        };
    }

    public void load(Renderer renderer) {
        renderer.setBackgroundColor(0.25f, 0.25f, 0.25f, 1.f);
    }

    public void update(Renderer renderer, double deltaTime) {
        final float tolerance = 0.01f;
        double startSpeed = 4.0;
        double speed = 2.0;
        int lockCount = 0;

        if (isLeftShiftHeld) {
            startSpeed *= -1;
            speed *= -1;
        }

        switch (move) {
        case WHITE:
            for (Cubie cubie : cube) {
                if (Math.abs(cubie.getY() - 0.1f) <= tolerance) {
                    if (isFirstMove) {
                        cubie.rotateY(-startSpeed);
                    } else {
                        cubie.rotateY(-speed);
                        if (cubie.isAboutLocked(tolerance)) {
                            lockCount++;
                        }
                    }
                }
            }
            if (isFirstMove) {
                isFirstMove = false;
            }
            if (lockCount >= 9) {
                for (Cubie cubie : cube) {
                    if (Math.abs(cubie.getY() - 0.1f) <= tolerance) {
                        cubie.lock(tolerance);
                    }
                }
                move = MoveType.NO_MOVE;
            }
            break;
        case GREEN:
            for (Cubie cubie : cube) {
                if (Math.abs(cubie.getZ() - 0.1f) <= tolerance) {
                    if (isFirstMove) {
                        cubie.rotateZ(-startSpeed);
                    } else {
                        cubie.rotateZ(-speed);
                        if (cubie.isAboutLocked(tolerance)) {
                            lockCount++;
                        }
                    }
                }
            }
            if (isFirstMove) {
                isFirstMove = false;
            }
            if (lockCount >= 9) {
                for (Cubie cubie : cube) {
                    if (Math.abs(cubie.getZ() - 0.1f) <= tolerance) {
                        cubie.lock(tolerance);
                    }
                }
                move = MoveType.NO_MOVE;
            }
            break;
        case BLUE:
            for (Cubie cubie : cube) {
                if (Math.abs(cubie.getZ() + 0.1f) <= tolerance) {
                    if (isFirstMove) {
                        cubie.rotateZ(startSpeed);
                    } else {
                        cubie.rotateZ(speed);
                        if (cubie.isAboutLocked(tolerance)) {
                            lockCount++;
                        }
                    }
                }
            }
            if (isFirstMove) {
                isFirstMove = false;
            }
            if (lockCount >= 9) {
                for (Cubie cubie : cube) {
                    if (Math.abs(cubie.getZ() + 0.1f) <= tolerance) {
                        cubie.lock(tolerance);
                    }
                }
                move = MoveType.NO_MOVE;
            }
            break;
        case ORANGE:
            for (Cubie cubie : cube) {
                if (Math.abs(cubie.getX() + 0.1f) <= tolerance) {
                    if (isFirstMove) {
                        cubie.rotateX(startSpeed);
                    } else {
                        cubie.rotateX(speed);
                        if (cubie.isAboutLocked(tolerance)) {
                            lockCount++;
                        }
                    }
                }
            }
            if (isFirstMove) {
                isFirstMove = false;
            }
            if (lockCount >= 9) {
                for (Cubie cubie : cube) {
                    if (Math.abs(cubie.getX() + 0.1f) <= tolerance) {
                        cubie.lock(tolerance);
                    }
                }
                move = MoveType.NO_MOVE;
            }
            break;
        case RED:
            for (Cubie cubie : cube) {
                if (Math.abs(cubie.getX() - 0.1f) <= tolerance) {
                    if (isFirstMove) {
                        cubie.rotateX(-startSpeed);
                    } else {
                        cubie.rotateX(-speed);
                        if (cubie.isAboutLocked(tolerance)) {
                            lockCount++;
                        }
                    }
                }
            }
            if (isFirstMove) {
                isFirstMove = false;
            }
            if (lockCount >= 9) {
                for (Cubie cubie : cube) {
                    if (Math.abs(cubie.getX() - 0.1f) <= tolerance) {
                        cubie.lock(tolerance);
                    }
                }
                move = MoveType.NO_MOVE;
            }
            break;
        case YELLOW:
            for (Cubie cubie : cube) {
                if (Math.abs(cubie.getY() + 0.1f) <= tolerance) {
                    if (isFirstMove) {
                        cubie.rotateY(startSpeed);
                    } else {
                        cubie.rotateY(speed);
                        if (cubie.isAboutLocked(tolerance)) {
                            lockCount++;
                        }
                    }
                }
            }
            if (isFirstMove) {
                isFirstMove = false;
            }
            if (lockCount >= 9) {
                for (Cubie cubie : cube) {
                    if (Math.abs(cubie.getY() + 0.1f) <= tolerance) {
                        cubie.lock(tolerance);
                    }
                }
                move = MoveType.NO_MOVE;
            }
            break;
        case NO_MOVE:
            break;
        }
    }

    public void draw(Renderer renderer) {
        for (Cubie cubie : cube) {
            cubie.draw(renderer);
        }
    }

    public void keyPressed(Renderer renderer, int key) {
        if (move == MoveType.NO_MOVE) {
            switch (key) {
            case GLFW_KEY_W:
                move = MoveType.WHITE;
                isFirstMove = true;
                break;
            case GLFW_KEY_G:
                move = MoveType.GREEN;
                isFirstMove = true;
                break;
            case GLFW_KEY_B:
                move = MoveType.BLUE;
                isFirstMove = true;
                break;
            case GLFW_KEY_O:
                move = MoveType.ORANGE;
                isFirstMove = true;
                break;
            case GLFW_KEY_R:
                move = MoveType.RED;
                isFirstMove = true;
                break;
            case GLFW_KEY_Y:
                move = MoveType.YELLOW;
                isFirstMove = true;
                break;
            }
        }
        switch (key) {
        case GLFW_KEY_LEFT_SHIFT:
            isLeftShiftHeld = true;
            break;
        case GLFW_KEY_LEFT_CONTROL:
            isLeftControlHeld = true;
            break;
        }
    }

    public void keyReleased(Renderer renderer, int key) {
        switch (key) {
        case GLFW_KEY_LEFT_SHIFT:
            isLeftShiftHeld = false;
            break;
        case GLFW_KEY_LEFT_CONTROL:
            isLeftControlHeld = false;
            break;
        }
    }

    public void mouseButtonPressed(Renderer renderer, int button) {
        if (button == GLFW_MOUSE_BUTTON_LEFT) {
            cameraMode = true;
            renderer.turnOffCursor();
        }
    }

    public void mouseButtonReleased(Renderer renderer, int button) {
        if (button == GLFW_MOUSE_BUTTON_LEFT) {
            cameraMode = false;
            renderer.turnOnCursor();
        }
    }

    public void cursorMoved(Renderer renderer, double x, double y) {
        final float speed = 0.75f;

        double deltaX = x - oldX;
        double deltaY = y - oldY;
        if (cameraMode) {
            renderer.rotateCameraX((float) deltaY * speed);
            renderer.rotateCameraY((float) deltaX * speed);
        }
        oldX = x;
        oldY = y;
    }

}
