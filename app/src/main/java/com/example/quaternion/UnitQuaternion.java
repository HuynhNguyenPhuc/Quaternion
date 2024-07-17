package com.example.quaternion;

public class UnitQuaternion extends Quaternion {
    public float theta;
    
    public UnitQuaternion(float w, float x, float y, float z){
        super(w, x, y, z);
        float magnitude = (float) Math.sqrt(w * w + x * x + y * y + z * z);
        this.w /= magnitude;
        this.x /= magnitude;
        this.y /= magnitude;
        this.z /= magnitude;
        this.v = new Vector(x, y, z);
        this.theta = (float) Math.acos(w);
    }

    public UnitQuaternion(Vector v, float theta){
        super((float) Math.cos(theta), v.normalize().multiply((float) Math.sin(theta)));
        this.theta = theta;
    }

    public UnitQuaternion negate(){
        return new UnitQuaternion(-this.w, -this.x, -this.y, -this.z);
    }

    public UnitQuaternion conjugate() {
        return new UnitQuaternion(this.w, -this.x, -this.y, -this.z);
    }

    public Vector rotate(Vector vector) {
        Quaternion v = new Quaternion(vector);
        Quaternion result = this.multiply(v).multiply(this.conjugate());
        return result.getVector();
    }

    public static UnitQuaternion slerp(UnitQuaternion q1, UnitQuaternion q2, float t) {
        float dotProduct = q1.w * q2.w + q1.x * q2.x + q1.y * q2.y + q1.z * q2.z;
        if (dotProduct < 0) {
            q2 = q2.negate();
            dotProduct = -dotProduct;
        }

        float theta = (float) Math.acos(dotProduct);
        float alpha = (float) Math.sin((1.0f - t) * theta) / (float) Math.sin(theta);
        float beta = (float) Math.sin(t * theta) / (float) Math.sin(theta);
        UnitQuaternion q = new UnitQuaternion(
                q1.w * alpha + q2.w * beta,
                q1.x * alpha + q2.x * beta,
                q1.y * alpha + q2.y * beta,
                q1.z * alpha + q2.z * beta
        );
        return q;
    }

    public float[] getEulerAngles(){
        float epsilon = 1e-6f;
        float alpha = (float) Math.atan2(2 * (this.w * this.x + this.y * this.z) + epsilon, 1 - 2 * (this.x * this.x + this.y * this.y) + epsilon);
        float beta = (float) Math.asin(2 * (this.w * this.y - this.z * this.x) + epsilon);
        float gamma = (float) Math.atan2(2 * (this.w * this.z + this.x * this.y) + epsilon, 1 - 2 * (this.y * this.y + this.z * this.z) + epsilon);
        alpha *= 180.0f / (float) Math.PI;
        beta *= 180.0f / (float) Math.PI;
        gamma *= 180.0f / (float) Math.PI;
        return new float[]{alpha, beta, gamma};
    }
}
