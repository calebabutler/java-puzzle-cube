
package org.puzzlecube;

import java.util.Queue;
import java.util.LinkedList;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;

public class PuzzleCubeGame implements Game {

    private boolean cameraMode, isLeftShiftHeld, isLeftControlHeld;
    private double oldX, oldY;
    private double saveAngle;

    private enum MoveType {
        NO_MOVE,
        UP_C,
        UP_CC,
        DOWN_C,
        DOWN_CC,
        RIGHT_C,
        RIGHT_CC,
        LEFT_C,
        LEFT_CC,
        FRONT_C,
        FRONT_CC,
        BACK_C,
        BACK_CC,
        MIDDLE_C,
        MIDDLE_CC,
        SLICE_C,
        SLICE_CC,
        EQUATOR_C,
        EQUATOR_CC,
        X_ROTATION_C,
        X_ROTATION_CC,
        Z_ROTATION_C,
        Z_ROTATION_CC;
    }

    private enum Axis {
        X,
        Y,
        Z;
    }

    private MoveType currentMove;
    private Queue<MoveType> moveQueue;
    private boolean isFirstMove;

    private Cubie[] cube;

    private Random random;

    public PuzzleCubeGame() {
        cameraMode = false;
        currentMove = MoveType.NO_MOVE;
        moveQueue = new LinkedList<MoveType>();
        random = new Random();
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

    private void continueTurn(Axis axis, float globalDesired, double direction) {
        final float tolerance = 0.02f;
        final double startSpeed = 8.0;
        final double speed = 4.0;

        int lockCount = 0;

        for (Cubie cubie : cube) {
            float actual;
            switch (axis) {
            case X:
                actual = cubie.getX();
                break;
            case Y:
                actual = cubie.getY();
                break;
            case Z:
            default:
                actual = cubie.getZ();
                break;
            }

            float desired;
            if (Float.isNaN(globalDesired)) {
                desired = actual;
            } else {
                desired = globalDesired;
            }

            if (Math.abs(actual - desired) <= tolerance) {
                if (isFirstMove) {
                    switch (axis) {
                    case X:
                        cubie.rotateX(startSpeed * direction);
                        break;
                    case Y:
                        cubie.rotateY(startSpeed * direction);
                        break;
                    case Z:
                        cubie.rotateZ(startSpeed * direction);
                        break;
                    }
                } else {
                    switch (axis) {
                    case X:
                        cubie.rotateX(speed * direction);
                        break;
                    case Y:
                        cubie.rotateY(speed * direction);
                        break;
                    case Z:
                        cubie.rotateZ(speed * direction);
                        break;
                    }
                    if (cubie.isAboutLocked(tolerance)) {
                        lockCount++;
                    }
                }
            }
        }
        if (isFirstMove) {
            isFirstMove = false;
        }
        if ((Float.isNaN(globalDesired) && lockCount >= 27)
         || (!Float.isNaN(globalDesired) && lockCount >= 9)) {
            for (Cubie cubie : cube) {
                float actual;
                switch (axis) {
                case X:
                    actual = cubie.getX();
                    break;
                case Y:
                    actual = cubie.getY();
                    break;
                case Z:
                default:
                    actual = cubie.getZ();
                    break;
                }

                float desired;
                if (Float.isNaN(globalDesired)) {
                    desired = actual;
                } else {
                    desired = globalDesired;
                }

                if (Math.abs(actual - desired) <= tolerance) {
                    cubie.lock(tolerance);
                }
            }
            currentMove = MoveType.NO_MOVE;
        }
    }

    private void continueMove(Renderer renderer, MoveType move) {
        if (move != MoveType.NO_MOVE && isFirstMove) {
            saveAngle = renderer.getCameraAngleY();
        }
        double angleSin = Math.sin(saveAngle * Math.PI / 180.0);
        double angleCos = Math.cos(saveAngle * Math.PI / 180.0);
        double inverseSquareRoot2 = Math.sqrt(2.0) / 2.0;

        switch (move) {
        case UP_C:
            continueTurn(Axis.Y, 0.1f, -1.f);
            break;
        case UP_CC:
            continueTurn(Axis.Y, 0.1f, 1.f);
            break;
        case DOWN_C:
            continueTurn(Axis.Y, -0.1f, 1.f);
            break;
        case DOWN_CC:
            continueTurn(Axis.Y, -0.1f, -1.f);
            break;
        case RIGHT_C:
            if (angleSin > inverseSquareRoot2) {
                continueTurn(Axis.Z, 0.1f, -1.f);
            } else if (angleCos > inverseSquareRoot2) {
                continueTurn(Axis.X, 0.1f, -1.f);
            } else if (angleSin < -inverseSquareRoot2) {
                continueTurn(Axis.Z, -0.1f, 1.f);
            } else {
                continueTurn(Axis.X, -0.1f, 1.f);
            }
            break;
        case RIGHT_CC:
            if (angleSin > inverseSquareRoot2) {
                continueTurn(Axis.Z, 0.1f, 1.f);
            } else if (angleCos > inverseSquareRoot2) {
                continueTurn(Axis.X, 0.1f, 1.f);
            } else if (angleSin < -inverseSquareRoot2) {
                continueTurn(Axis.Z, -0.1f, -1.f);
            } else {
                continueTurn(Axis.X, -0.1f, -1.f);
            }
            break;
        case LEFT_C:
            if (angleSin > inverseSquareRoot2) {
                continueTurn(Axis.Z, -0.1f, 1.f);
            } else if (angleCos > inverseSquareRoot2) {
                continueTurn(Axis.X, -0.1f, 1.f);
            } else if (angleSin < -inverseSquareRoot2) {
                continueTurn(Axis.Z, 0.1f, -1.f);
            } else {
                continueTurn(Axis.X, 0.1f, -1.f);
            }
            break;
        case LEFT_CC:
            if (angleSin > inverseSquareRoot2) {
                continueTurn(Axis.Z, -0.1f, -1.f);
            } else if (angleCos > inverseSquareRoot2) {
                continueTurn(Axis.X, -0.1f, -1.f);
            } else if (angleSin < -inverseSquareRoot2) {
                continueTurn(Axis.Z, 0.1f, 1.f);
            } else {
                continueTurn(Axis.X, 0.1f, 1.f);
            }
            break;
        case FRONT_C:
            if (angleSin > inverseSquareRoot2) {
                continueTurn(Axis.X, -0.1f, 1.f);
            } else if (angleCos > inverseSquareRoot2) {
                continueTurn(Axis.Z, 0.1f, -1.f);
            } else if (angleSin < -inverseSquareRoot2) {
                continueTurn(Axis.X, 0.1f, -1.f);
            } else {
                continueTurn(Axis.Z, -0.1f, 1.f);
            }
            break;
        case FRONT_CC:
            if (angleSin > inverseSquareRoot2) {
                continueTurn(Axis.X, -0.1f, -1.f);
            } else if (angleCos > inverseSquareRoot2) {
                continueTurn(Axis.Z, 0.1f, 1.f);
            } else if (angleSin < -inverseSquareRoot2) {
                continueTurn(Axis.X, 0.1f, 1.f);
            } else {
                continueTurn(Axis.Z, -0.1f, -1.f);
            }
            break;
        case BACK_C:
            if (angleSin > inverseSquareRoot2) {
                continueTurn(Axis.X, 0.1f, -1.f);
            } else if (angleCos > inverseSquareRoot2) {
                continueTurn(Axis.Z, -0.1f, 1.f);
            } else if (angleSin < -inverseSquareRoot2) {
                continueTurn(Axis.X, -0.1f, 1.f);
            } else {
                continueTurn(Axis.Z, 0.1f, -1.f);
            }
            break;
        case BACK_CC:
            if (angleSin > inverseSquareRoot2) {
                continueTurn(Axis.X, 0.1f, 1.f);
            } else if (angleCos > inverseSquareRoot2) {
                continueTurn(Axis.Z, -0.1f, -1.f);
            } else if (angleSin < -inverseSquareRoot2) {
                continueTurn(Axis.X, -0.1f, -1.f);
            } else {
                continueTurn(Axis.Z, 0.1f, 1.f);
            }
            break;
        case MIDDLE_C:
            if (angleSin > inverseSquareRoot2) {
                continueTurn(Axis.Z, 0.f, 1.f);
            } else if (angleCos > inverseSquareRoot2) {
                continueTurn(Axis.X, 0.f, 1.f);
            } else if (angleSin < -inverseSquareRoot2) {
                continueTurn(Axis.Z, 0.f, -1.f);
            } else {
                continueTurn(Axis.X, 0.f, -1.f);
            }
            break;
        case MIDDLE_CC:
            if (angleSin > inverseSquareRoot2) {
                continueTurn(Axis.Z, 0.f, -1.f);
            } else if (angleCos > inverseSquareRoot2) {
                continueTurn(Axis.X, 0.f, -1.f);
            } else if (angleSin < -inverseSquareRoot2) {
                continueTurn(Axis.Z, 0.f, 1.f);
            } else {
                continueTurn(Axis.X, 0.f, 1.f);
            }
            break;
        case SLICE_C:
            if (angleSin > inverseSquareRoot2) {
                continueTurn(Axis.X, 0.f, 1.f);
            } else if (angleCos > inverseSquareRoot2) {
                continueTurn(Axis.Z, 0.f, -1.f);
            } else if (angleSin < -inverseSquareRoot2) {
                continueTurn(Axis.X, 0.f, -1.f);
            } else {
                continueTurn(Axis.Z, 0.f, 1.f);
            }
            break;
        case SLICE_CC:
            if (angleSin > inverseSquareRoot2) {
                continueTurn(Axis.X, 0.f, -1.f);
            } else if (angleCos > inverseSquareRoot2) {
                continueTurn(Axis.Z, 0.f, 1.f);
            } else if (angleSin < -inverseSquareRoot2) {
                continueTurn(Axis.X, 0.f, 1.f);
            } else {
                continueTurn(Axis.Z, 0.f, -1.f);
            }
            break;
        case EQUATOR_C:
            continueTurn(Axis.Y, 0.f, 1.f);
            break;
        case EQUATOR_CC:
            continueTurn(Axis.Y, 0.f, -1.f);
            break;
        case X_ROTATION_C:
            if (angleSin > inverseSquareRoot2) {
                continueTurn(Axis.Z, Float.NaN, -1.f);
            } else if (angleCos > inverseSquareRoot2) {
                continueTurn(Axis.X, Float.NaN, -1.f);
            } else if (angleSin < -inverseSquareRoot2) {
                continueTurn(Axis.Z, Float.NaN, 1.f);
            } else {
                continueTurn(Axis.X, Float.NaN, 1.f);
            }
            break;
        case X_ROTATION_CC:
            if (angleSin > inverseSquareRoot2) {
                continueTurn(Axis.Z, Float.NaN, 1.f);
            } else if (angleCos > inverseSquareRoot2) {
                continueTurn(Axis.X, Float.NaN, 1.f);
            } else if (angleSin < -inverseSquareRoot2) {
                continueTurn(Axis.Z, Float.NaN, -1.f);
            } else {
                continueTurn(Axis.X, Float.NaN, -1.f);
            }
            break;
        case Z_ROTATION_C:
            if (angleSin > inverseSquareRoot2) {
                continueTurn(Axis.X, Float.NaN, 1.f);
            } else if (angleCos > inverseSquareRoot2) {
                continueTurn(Axis.Z, Float.NaN, -1.f);
            } else if (angleSin < -inverseSquareRoot2) {
                continueTurn(Axis.X, Float.NaN, -1.f);
            } else {
                continueTurn(Axis.Z, Float.NaN, 1.f);
            }
            break;
        case Z_ROTATION_CC:
            if (angleSin > inverseSquareRoot2) {
                continueTurn(Axis.X, Float.NaN, -1.f);
            } else if (angleCos > inverseSquareRoot2) {
                continueTurn(Axis.Z, Float.NaN, 1.f);
            } else if (angleSin < -inverseSquareRoot2) {
                continueTurn(Axis.X, Float.NaN, 1.f);
            } else {
                continueTurn(Axis.Z, Float.NaN, -1.f);
            }
            break;
        default:
            break;
        }
    }

    private void scramble() {
        for (int i = 0; i < 30; i++) {
            moveQueue.add(MoveType.values()[random.nextInt(1, 12)]);
        }
    }

    public void update(Renderer renderer, double deltaTime) {
        if (currentMove == MoveType.NO_MOVE && !moveQueue.isEmpty()) {
            currentMove = moveQueue.remove();
            isFirstMove = true;
        }
        continueMove(renderer, currentMove);
    }

    public void draw(Renderer renderer) {
        for (Cubie cubie : cube) {
            cubie.draw(renderer);
        }
        renderer.setColor(1.f, 1.f, 1.f, 1.f);
        if (Math.pow(Math.sin(renderer.getCameraAngleY() * Math.PI / 180.0), 2.0) < 0.5) {
            renderer.drawLine(0.f, 0.f, 1.f, 0.f, 0.f, -1.f);
        } else {
            renderer.drawLine(1.f, 0.f, 0.f, -1.f, 0.f, 0.f);
        }
    }

    public void keyPressed(Renderer renderer, int key) {
        switch (key) {
        case GLFW_KEY_U:
            if (isLeftControlHeld) {
                moveQueue.add(MoveType.UP_C);
                moveQueue.add(MoveType.UP_C);
            } else if (isLeftShiftHeld) {
                moveQueue.add(MoveType.UP_CC);
            } else {
                moveQueue.add(MoveType.UP_C);
            }
            break;
        case GLFW_KEY_D:
            if (isLeftControlHeld) {
                moveQueue.add(MoveType.DOWN_C);
                moveQueue.add(MoveType.DOWN_C);
            } else if (isLeftShiftHeld) {
                moveQueue.add(MoveType.DOWN_CC);
            } else {
                moveQueue.add(MoveType.DOWN_C);
            }
            break;
        case GLFW_KEY_R:
            if (isLeftControlHeld) {
                moveQueue.add(MoveType.RIGHT_C);
                moveQueue.add(MoveType.RIGHT_C);
            } else if (isLeftShiftHeld) {
                moveQueue.add(MoveType.RIGHT_CC);
            } else {
                moveQueue.add(MoveType.RIGHT_C);
            }
            break;
        case GLFW_KEY_L:
            if (isLeftControlHeld) {
                moveQueue.add(MoveType.LEFT_C);
                moveQueue.add(MoveType.LEFT_C);
            } else if (isLeftShiftHeld) {
                moveQueue.add(MoveType.LEFT_CC);
            } else {
                moveQueue.add(MoveType.LEFT_C);
            }
            break;
        case GLFW_KEY_F:
            if (isLeftControlHeld) {
                moveQueue.add(MoveType.FRONT_C);
                moveQueue.add(MoveType.FRONT_C);
            } else if (isLeftShiftHeld) {
                moveQueue.add(MoveType.FRONT_CC);
            } else {
                moveQueue.add(MoveType.FRONT_C);
            }
            break;
        case GLFW_KEY_B:
            if (isLeftControlHeld) {
                moveQueue.add(MoveType.BACK_C);
                moveQueue.add(MoveType.BACK_C);
            } else if (isLeftShiftHeld) {
                moveQueue.add(MoveType.BACK_CC);
            } else {
                moveQueue.add(MoveType.BACK_C);
            }
            break;
        case GLFW_KEY_M:
            if (isLeftControlHeld) {
                moveQueue.add(MoveType.MIDDLE_C);
                moveQueue.add(MoveType.MIDDLE_C);
            } else if (isLeftShiftHeld) {
                moveQueue.add(MoveType.MIDDLE_CC);
            } else {
                moveQueue.add(MoveType.MIDDLE_C);
            }
            break;
        case GLFW_KEY_S:
            if (isLeftControlHeld) {
                moveQueue.add(MoveType.SLICE_C);
                moveQueue.add(MoveType.SLICE_C);
            } else if (isLeftShiftHeld) {
                moveQueue.add(MoveType.SLICE_CC);
            } else {
                moveQueue.add(MoveType.SLICE_C);
            }
            break;
        case GLFW_KEY_E:
            if (isLeftControlHeld) {
                moveQueue.add(MoveType.EQUATOR_C);
                moveQueue.add(MoveType.EQUATOR_C);
            } else if (isLeftShiftHeld) {
                moveQueue.add(MoveType.EQUATOR_CC);
            } else {
                moveQueue.add(MoveType.EQUATOR_C);
            }
            break;
        case GLFW_KEY_X:
            if (isLeftControlHeld) {
                moveQueue.add(MoveType.X_ROTATION_C);
                moveQueue.add(MoveType.X_ROTATION_C);
            } else if (isLeftShiftHeld) {
                moveQueue.add(MoveType.X_ROTATION_CC);
            } else {
                moveQueue.add(MoveType.X_ROTATION_C);
            }
            break;
        case GLFW_KEY_Z:
            if (isLeftControlHeld) {
                moveQueue.add(MoveType.Z_ROTATION_C);
                moveQueue.add(MoveType.Z_ROTATION_C);
            } else if (isLeftShiftHeld) {
                moveQueue.add(MoveType.Z_ROTATION_CC);
            } else {
                moveQueue.add(MoveType.Z_ROTATION_C);
            }
            break;
        case GLFW_KEY_ENTER:
            scramble();
            break;
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
