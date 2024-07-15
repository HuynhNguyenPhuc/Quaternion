package com.example.quaternion;

public class Quaternion {
    public final float w;
    public final float x;
    public final float y;
    public final float z;
    public final Vector v;

    public Quaternion(float w, float x, float y, float z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
        this.v = new Vector(x, y, z);
    }

    public Quaternion (Vector v){
        this.w = 0;
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.v = v;
    }

    public Quaternion(float w, Vector v) {
        this.w = w;
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.v = v;
    }

    public float magnitude() {
        return (float) Math.sqrt(w * w + x * x + y * y + z * z);
    }

    public Quaternion normalize() {
        float mag = magnitude();
        return new Quaternion(w / mag, x / mag, y / mag, z / mag);
    }

    public Quaternion conjugate() {
        return new Quaternion(w, -x, -y, -z);
    }

    public Quaternion inverse() {
        float mag = magnitude();
        if (mag == 0) {
            throw new ArithmeticException("Cannot invert a zero quaternion");
        }
        Quaternion conjugate = conjugate();
        float magSquared = mag * mag;
        return new Quaternion(conjugate.w / magSquared, conjugate.x / magSquared, conjugate.y / magSquared, conjugate.z / magSquared);
    }

    public Quaternion add(Quaternion other){
        return new Quaternion(w + other.w, x + other.x, y + other.y, z + other.z);
    }

    public Quaternion subtract(Quaternion other){
        return new Quaternion(w - other.w, x - other.x, y - other.y, z - other.z);
    }

    public Quaternion multiply(Quaternion other){
        Vector v1 = this.v.multiply(other.w);
        Vector v2 = other.v.multiply(this.w);
        Vector v3 = this.v.crossProduct(other.v);
        Vector v = v1.add(v2).add(v3);
        float newW = this.w * other.w - this.v.dotProduct(other.v);
        return new Quaternion(newW, v.x, v.y, v.z);
    }

    public Vector getVector(){
        if (w != 0){
            throw new ArithmeticException("Cannot get vector from a non-zero quaternion");
        }
        return v;
    }

    @Override
    public String toString() {
        return "Quaternion{" +
                "w = " + w +
                ", x = " + x +
                ", y = " + y +
                ", z = " + z
                + "}";
    }
}