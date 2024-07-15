package com.example.quaternion;

public class Ray {
    public final Point origin;
    public final Vector direction;

    public Ray(Point origin, Vector direction){
        this.origin = origin;
        this.direction = direction;
    }

    public Point at(float t){
        return new Point(
            origin.x + direction.x * t,
            origin.y + direction.y * t,
            origin.z + direction.z * t
        );
    }

    public float[] getMesh(){
        return new float[]{origin.x, origin.y, origin.z, origin.x + direction.x * 1000.0f, origin.y + direction.y * 1000.0f, origin.z + direction.z * 1000.0f};
    }

    @Override
    public String toString() {
        return "Ray origin: " + origin + ", direction: " + direction + "\n" + "Ray direction: (" + direction.x + ", " + direction.y + ", " + direction.z + ")";
    }
}
