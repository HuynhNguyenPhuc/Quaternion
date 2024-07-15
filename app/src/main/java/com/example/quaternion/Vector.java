package com.example.quaternion;

public class Vector {
    public final float x;
    public final float y;
    public final float z;

    public Vector(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector(float[] coordinates){
        this.x = coordinates[0];
        this.y = coordinates[1];
        this.z = coordinates[2];
    }

    public float magnitude(){
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public float[] getCoordinates(){ return new float[]{x, y, z}; }

    public Vector normalize(){
        float magnitude = this.magnitude();
        return new Vector(
            x / magnitude,
            y / magnitude,
            z / magnitude
        );
    }

    public Vector getNormal(){
        return new Vector(
                2.0f / (x + (float) 1e-6),
                -1.0f / (y + (float) 1e-6),
                -1.0f / (z + (float) 1e-6)
        );
    }

    public Vector negate(){
        return new Vector(
            -x,
            -y,
            -z
        );
    }

    public Vector add(Vector vector){
        return new Vector(
            x + vector.x,
            y + vector.y,
            z + vector.z
        );
    }

    public Vector subtract(Vector vector){
        return new Vector(
            x - vector.x,
            y - vector.y,
            z - vector.z
        );
    }

    public Vector multiply(float scalar){
        return new Vector(
            x * scalar,
            y * scalar,
            z * scalar
        );
    }

    public Vector crossProduct(Vector vector){
        return new Vector(
            y * vector.z - z * vector.y,
            z * vector.x - x * vector.z,
            x * vector.y - y * vector.x
        );
    }

    public float dotProduct(Vector vector){
        return x * vector.x + y * vector.y + z * vector.z;
    }

    @Override
    public String toString() {
        return "Vector coordinates: (" + x + ", " + y + ", " + z + ")";
    }
}
