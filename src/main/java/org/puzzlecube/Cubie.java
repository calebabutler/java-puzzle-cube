
/* Copyright (c) 2023 Caleb Butler
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.puzzlecube;

public class Cubie {

    // Private fields

    private final float cubieSize = 0.1f;
    private final float[] x, y, z;
    private final Renderer.FaceColor top, front, back, left, right, bottom;

    // Public methods

    public Cubie(float xPos, float yPos, float zPos, Renderer.FaceColor t, Renderer.FaceColor f,
                 Renderer.FaceColor b, Renderer.FaceColor l, Renderer.FaceColor r,
                 Renderer.FaceColor bo) {
        final float x1 = xPos - cubieSize / 2.f;
        final float y1 = yPos - cubieSize / 2.f;
        final float z1 = zPos + cubieSize / 2.f;
        final float x2 = xPos + cubieSize / 2.f;
        final float y2 = yPos + cubieSize / 2.f;
        final float z2 = zPos - cubieSize / 2.f;

        x = new float[] {x1, x2, x2, x1, x1, x2, x2, x1};
        y = new float[] {y2, y2, y2, y2, y1, y1, y1, y1};
        z = new float[] {z2, z2, z1, z1, z2, z2, z1, z1};

        top = t;
        front = f;
        back = b;
        left = l;
        right = r;
        bottom = bo;
    }

    public void rotateX(double degrees) {
        final float cos = (float) Math.cos(degrees * Math.PI / 180.0);
        final float sin = (float) Math.sin(degrees * Math.PI / 180.0);
        for (int i = 0; i < 8; i++) {
            y[i] = y[i] * cos - z[i] * sin;
            z[i] = y[i] * sin + z[i] * cos;
        }
    }

    public void rotateY(double degrees) {
        final float cos = (float) Math.cos(degrees * Math.PI / 180.0);
        final float sin = (float) Math.sin(degrees * Math.PI / 180.0);
        for (int i = 0; i < 8; i++) {
            x[i] = x[i] * cos + z[i] * sin;
            z[i] = x[i] * -sin + z[i] * cos;
        }
    }

    public void rotateZ(double degrees) {
        final float cos = (float) Math.cos(degrees * Math.PI / 180.0);
        final float sin = (float) Math.sin(degrees * Math.PI / 180.0);
        for (int i = 0; i < 8; i++) {
            x[i] = x[i] * cos - y[i] * sin;
            y[i] = x[i] * sin + y[i] * cos;
        }
    }

    public float getX() {
        float sum = 0.f;
        for (float xValue : x) {
            sum += xValue;
        }
        return sum / 8.f;
    }

    public float getY() {
        float sum = 0.f;
        for (float yValue : y) {
            sum += yValue;
        }
        return sum / 8.f;
    }

    public float getZ() {
        float sum = 0.f;
        for (float zValue : z) {
            sum += zValue;
        }
        return sum / 8.f;
    }

    public boolean isAboutLocked(float tolerance) {
        final float ideal1 = 0.1f - cubieSize / 2.f;
        final float ideal2 = 0.1f + cubieSize / 2.f;
        final float ideal3 = -0.1f - cubieSize / 2.f;
        final float ideal4 = -0.1f + cubieSize / 2.f;

        for (float xValue : x) {
            if (Math.abs(xValue - ideal1) > tolerance && Math.abs(xValue - ideal2) > tolerance
             && Math.abs(xValue - ideal3) > tolerance && Math.abs(xValue - ideal4) > tolerance) {
                return false;
            }
        }
        for (float yValue : y) {
            if (Math.abs(yValue - ideal1) > tolerance && Math.abs(yValue - ideal2) > tolerance
             && Math.abs(yValue - ideal3) > tolerance && Math.abs(yValue - ideal4) > tolerance) {
                return false;
            }
        }
        for (float zValue : z) {
            if (Math.abs(zValue - ideal1) > tolerance && Math.abs(zValue - ideal2) > tolerance
             && Math.abs(zValue - ideal3) > tolerance && Math.abs(zValue - ideal4) > tolerance) {
                return false;
            }
        }
        return true;
    }

    public void lock(float tolerance) {
        final float ideal1 = 0.1f - cubieSize / 2.f;
        final float ideal2 = 0.1f + cubieSize / 2.f;
        final float ideal3 = -0.1f - cubieSize / 2.f;
        final float ideal4 = -0.1f + cubieSize / 2.f;

        for (int i = 0; i < 8; i++) {
            if (Math.abs(x[i] - ideal1) <= tolerance) {
                x[i] = ideal1;
            } else if (Math.abs(x[i] - ideal2) <= tolerance) {
                x[i] = ideal2;
            } else if (Math.abs(x[i] - ideal3) <= tolerance) {
                x[i] = ideal3;
            } else if (Math.abs(x[i] - ideal4) <= tolerance) {
                x[i] = ideal4;
            }

            if (Math.abs(y[i] - ideal1) <= tolerance) {
                y[i] = ideal1;
            } else if (Math.abs(y[i] - ideal2) <= tolerance) {
                y[i] = ideal2;
            } else if (Math.abs(y[i] - ideal3) <= tolerance) {
                y[i] = ideal3;
            } else if (Math.abs(y[i] - ideal4) <= tolerance) {
                y[i] = ideal4;
            }

            if (Math.abs(z[i] - ideal1) <= tolerance) {
                z[i] = ideal1;
            } else if (Math.abs(z[i] - ideal2) <= tolerance) {
                z[i] = ideal2;
            } else if (Math.abs(z[i] - ideal3) <= tolerance) {
                z[i] = ideal3;
            } else if (Math.abs(z[i] - ideal4) <= tolerance) {
                z[i] = ideal4;
            }
        }
    }

    public void draw(Renderer renderer) {
        renderer.drawCube(x, y, z, top, front, back, left, right, bottom);
    }

}
