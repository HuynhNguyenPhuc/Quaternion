package com.example.quaternion;

public class Point {
    public final float x;
    public final float y;
    public final float z;

    public Point(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point(float[] coordinates){
        this.x = coordinates[0];
        this.y = coordinates[1];
        this.z = coordinates[2];
    }

    public Point add(Vector vector){
        return new Point(
            x + vector.x,
            y + vector.y,
            z + vector.z
        );
    }

    public Point subtract(Point point){
        return new Point(
            x - point.x,
            y - point.y,
            z - point.z
        );
    }

    public Point subtract(Vector vector){
        return new Point(
            x - vector.x,
            y - vector.y,
            z - vector.z
        );
    }

    public float[] getCoordinates(){ return new float[]{x, y, z}; }

    public Point getCenter(Point point){
        return new Point(
            (x + point.x) / 2,
            (y + point.y) / 2,
            (z + point.z) / 2
        );
    }

    public float[] getMesh(){
        return new float[]{x, y, z};
    }

    @Override
    public String toString() {
        return "Point coordinates: (" + x + ", " + y + ", " + z + ")";
    }
}
