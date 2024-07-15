package com.example.quaternion;

public class Test {
    public static void main(String[] args) {
        float theta = (float) Math.toRadians(45);
        Vector u = new Vector(1, 0, 0);
        Vector v = new Vector(0, 1, 0);
        new Test().test1(u, theta, v);
        // float w1 = 3;
        // float w2 = 2;
        // Vector v1 = new Vector(1, -2, 1);
        // Vector v2 = new Vector(-1, 2, 3);
        // new Test().test2(w1, v1, w2, v2);
    }

    public void test1(Vector u, float theta, Vector v){
        UnitQuaternion q = new UnitQuaternion(u, theta);
        Vector v1 = q.rotate(v);
        System.out.println("w = " + q.w);
        System.out.println("x = " + q.x);
        System.out.println("y = " + q.y);
        System.out.println("z = " + q.z);
        System.out.println(v);
        System.out.println(v1);
    }

    public void test2(float w1, Vector v1, float w2, Vector v2){
        Quaternion q1 = new Quaternion(w1, v1);
        Quaternion q2 = new Quaternion(w2, v2);
        Quaternion q = q1.multiply(q2);
        System.out.println(q);
    }
}
