
package org.puzzlecube;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33C.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Renderer {

    public enum FaceColor {
        BLACK,
        WHITE,
        RED,
        ORANGE,
        BLUE,
        GREEN,
        YELLOW;
    }

    public final int width, height;

    private final String windowTitle;
    private final Game game;

    private long window;
    private int shaderProgram;
    private int positionLocation;
    private int colorLocation;
    private int projectionLocation;
    private int viewLocation;
    private boolean isRunning;

    private double cameraAngleY;
    private double cameraAngleX;

    private float backgroundR, backgroundG, backgroundB, backgroundA;

    public Renderer(Game game, String windowTitle, int width, int height) {
        this.game = game;
        this.windowTitle = windowTitle;
        this.width = width;
        this.height = height;
        isRunning = false;
    }

    public void run() {
        isRunning = true;
        init();
        loop();
        glDeleteProgram(shaderProgram);
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
        isRunning = false;
    }

    public void drawLine(float x1, float y1, float z1, float x2, float y2, float z2) {
        if (isRunning) {
            float[] vertices = {
                x1, y1, z1,
                x2, y2, z2
            };

            glUseProgram(shaderProgram);

            int vao = glGenVertexArrays();
            int vbo = glGenBuffers();

            glBindVertexArray(vao);
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, vertices, GL_STREAM_DRAW);

            glVertexAttribPointer(positionLocation, 3, GL_FLOAT, false, 0, 0);
            glEnableVertexAttribArray(positionLocation);

            glDrawArrays(GL_LINES, 0, 2);
            glBindVertexArray(0);

            glDeleteBuffers(vbo);
            glDeleteVertexArrays(vao);
        } else {
            System.err.println("Cannot draw when the renderer is not running.");
        }
    }

    public void drawTriangle(float x1, float y1, float z1, float x2, float y2, float z2, float x3,
                             float y3, float z3) {
        if (isRunning) {
            float[] vertices = {
                x1, y1, z1,
                x2, y2, z2,
                x3, y3, z3
            };

            glUseProgram(shaderProgram);

            int vao = glGenVertexArrays();
            int vbo = glGenBuffers();

            glBindVertexArray(vao);
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, vertices, GL_STREAM_DRAW);
    
            glVertexAttribPointer(positionLocation, 3, GL_FLOAT, false, 0, 0);
            glEnableVertexAttribArray(positionLocation);

            glDrawArrays(GL_TRIANGLES, 0, 3);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);

            glDeleteBuffers(vbo);
            glDeleteVertexArrays(vao);
        } else {
            System.err.println("Cannot draw when the renderer is not running.");
        }
    }

    public void setColor(float r, float g, float b, float a) {
        if (isRunning) {
            glUseProgram(shaderProgram);
            glUniform4f(colorLocation, r, g, b, a);
        } else {
            System.err.println("Cannot draw when the renderer is not running.");
        }
    }

    public void setFaceColor(FaceColor color) {
        switch (color) {
        case BLACK:
            setColor(0.f, 0.f, 0.f, 1.f);
            break;
        case WHITE:
            setColor(1.f, 1.f, 1.f, 1.f);
            break;
        case RED:
            setColor(0.5f, 0.f, 0.f, 1.f);
            break;
        case ORANGE:
            setColor(0.75f, 0.5f, 0.f, 1.f);
            break;
        case BLUE:
            setColor(0.f, 0.f, 0.5f, 1.f);
            break;
        case GREEN:
            setColor(0.f, 0.75f, 0.f, 1.f);
            break;
        case YELLOW:
            setColor(0.75f, 0.75f, 0.f, 1.f);
            break;
        }
    }

    public void drawCube(float[] x, float[] y, float[] z, FaceColor top, FaceColor front,
                         FaceColor back, FaceColor left, FaceColor right, FaceColor bottom) {
        setFaceColor(top);
        drawTriangle(x[3], y[3], z[3], x[1], y[1], z[1], x[0], y[0], z[0]);
        drawTriangle(x[3], y[3], z[3], x[2], y[2], z[2], x[1], y[1], z[1]);

        setFaceColor(front);
        drawTriangle(x[7], y[7], z[7], x[2], y[2], z[2], x[3], y[3], z[3]);
        drawTriangle(x[7], y[7], z[7], x[6], y[6], z[6], x[2], y[2], z[2]);

        setFaceColor(back);
        drawTriangle(x[4], y[4], z[4], x[0], y[0], z[0], x[1], y[1], z[1]);
        drawTriangle(x[4], y[4], z[4], x[1], y[1], z[1], x[5], y[5], z[5]);

        setFaceColor(left);
        drawTriangle(x[7], y[7], z[7], x[3], y[3], z[3], x[0], y[0], z[0]);
        drawTriangle(x[7], y[7], z[7], x[0], y[0], z[0], x[4], y[4], z[4]);

        setFaceColor(right);
        drawTriangle(x[6], y[6], z[6], x[1], y[1], z[1], x[2], y[2], z[2]);
        drawTriangle(x[6], y[6], z[6], x[5], y[5], z[5], x[1], y[1], z[1]);

        setFaceColor(bottom);
        drawTriangle(x[7], y[7], z[7], x[4], y[4], z[4], x[5], y[5], z[5]);
        drawTriangle(x[7], y[7], z[7], x[5], y[5], z[5], x[6], y[6], z[6]);
    }

    public void setBackgroundColor(float r, float g, float b, float a) {
        backgroundR = r;
        backgroundG = g;
        backgroundB = b;
        backgroundA = a;
    }

    private void updateCamera() {
        if (isRunning) {
            double cameraAngleXRadians = cameraAngleX * Math.PI / 180.0;
            double cameraAngleYRadians = cameraAngleY * Math.PI / 180.0;
            float xsin = (float) Math.sin(cameraAngleXRadians);
            float xcos = (float) Math.cos(cameraAngleXRadians);
            float ysin = (float) Math.sin(cameraAngleYRadians);
            float ycos = (float) Math.cos(cameraAngleYRadians);
            glUseProgram(shaderProgram);
            glUniformMatrix4fv(viewLocation, true, new float [] {
                ycos, 0, ysin, 0,
                xsin * ysin, xcos, -xsin * ycos, 0,
                -xcos * ysin, xsin, xcos * ycos, 0,
                0, 0, 0, 1
            });
        } else {
            System.err.println("Cannot draw when the renderer is not running.");
        }
    }

    public void rotateCameraX(double degrees) {
        if (cameraAngleX + degrees <= 90 && cameraAngleX + degrees >= -90) {
            cameraAngleX += degrees;
        }
        updateCamera();
    }

    public void rotateCameraY(double degrees) {
        cameraAngleY += degrees;
        updateCamera();
    }

    public double getCameraAngleX() {
        return cameraAngleX;
    }

    public double getCameraAngleY() {
        return cameraAngleY;
    }

    public void turnOffCursor() {
        if (isRunning) {
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        } else {
            System.err.println("Cannot draw when the renderer is not running.");
        }
    }

    public void turnOnCursor() {
        if (isRunning) {
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        } else {
            System.err.println("Cannot draw when the renderer is not running.");
        }
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        window = glfwCreateWindow(width, height, windowTitle, NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create GLFW window");
        }

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (action == GLFW_PRESS) {
                game.keyPressed(this, key);
            }
            if (action == GLFW_RELEASE) {
                game.keyReleased(this, key);
            }
        });

        glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
            if (action == GLFW_PRESS) {
                game.mouseButtonPressed(this, button);
            } else if (action == GLFW_RELEASE) {
                game.mouseButtonReleased(this, button);
            }
        });

        glfwSetCursorPosCallback(window, (window, xpos, ypos) -> {
            game.cursorMoved(this, xpos, ypos);
        });

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);

        GL.createCapabilities();
        System.err.println("GL_VENDOR: " + glGetString(GL_VENDOR));
        System.err.println("GL_RENDERER: " + glGetString(GL_RENDERER));
        System.err.println("GL_VERSION: " + glGetString(GL_VERSION));

        glViewport(0, 0, width, height);

        final String vertexShaderSource
            = "#version 330 core\n"
            + "in vec3 position;"
            + "uniform mat4 view;"
            + "uniform mat4 projection;"
            + "void main() {"
            + "gl_Position = projection * view * vec4(position, 1.0);"
            + "}";

        final int vertexShader = glCreateShader(GL_VERTEX_SHADER);

        glShaderSource(vertexShader, vertexShaderSource);
        glCompileShader(vertexShader);

        final String fragmentShaderSource
            = "#version 330 core\n"
            + "uniform vec4 color;"
            + "out vec4 FragColor;"
            + "void main() {"
            + "FragColor = color;"
            + "}";

        final int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);

        glShaderSource(fragmentShader, fragmentShaderSource);
        glCompileShader(fragmentShader);

        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);

        positionLocation = glGetAttribLocation(shaderProgram, "position");
        colorLocation = glGetUniformLocation(shaderProgram, "color");
        projectionLocation = glGetUniformLocation(shaderProgram, "projection");
        viewLocation = glGetUniformLocation(shaderProgram, "view");

        glUseProgram(shaderProgram);
        glUniform4f(colorLocation, 1.0f, 1.0f, 1.0f, 1.0f);

        // Default view matrix
        glUniformMatrix4fv(viewLocation, true, new float [] {
            1.f, 0.f, 0.f, 0.f,
            0.f, 1.f, 0.f, 0.f,
            0.f, 0.f, 1.f, 0.f,
            0.f, 0.f, 0.f, 1.f
        });

        // Calculate projection matrix
        final float fov = 60.f;
        final float aspect = width / height;
        final float zNear = 0.1f;
        final float zFar = 2.f;

        final float tanHalfFov = (float) Math.tan(fov / 2.f * Math.PI / 180.f);
        glUniformMatrix4fv(projectionLocation, true, new float [] {
            1.f / (aspect * tanHalfFov), 0.f, 0.f, 0.f,
            0.f, 1.f / tanHalfFov, 0.f, 0.f,
            0.f, 0.f, -(zFar + zNear) / (zFar - zNear), (zFar + zNear - 2 * zFar * zNear) / (zFar - zNear),
            0.f, 0.f, -1.f, 1.f
        });

        cameraAngleX = 0.f;
        cameraAngleY = 0.f;

        backgroundR = 0.5f;
        backgroundG = 0.5f;
        backgroundB = 0.5f;
        backgroundA = 1.0f;

        glEnable(GL_DEPTH_TEST);
    }

    private void loop() {
        double currentTime = 0.0;
        double lastTime = 0.0;
        double deltaTime = 0.0;
        game.load(this);
        while (!glfwWindowShouldClose(window)) {
            currentTime = glfwGetTime();
            deltaTime = currentTime - lastTime;
            lastTime = currentTime;
            game.update(this, deltaTime);
            glClearColor(backgroundR, backgroundG, backgroundB, backgroundA);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            game.draw(this);
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

}
