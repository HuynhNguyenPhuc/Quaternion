package com.example.quaternion;

public class UnitQuaternion extends Quaternion {
    public float theta;

    public UnitQuaternion(Vector v, float theta){
        super((float) Math.cos(theta/2.0f), v.normalize().multiply((float) Math.sin(theta/2.0f)));
        this.theta = theta;
    }

    public Vector rotate(Vector vector){
        Quaternion v = new Quaternion(vector);
        Quaternion result = this.multiply(v).multiply(this.conjugate());
        return result.getVector();
    }

    public float[] getEulerAngles(){
        float alpha = (float) Math.atan2(2 * (this.w * this.x + this.y * this.z), 1 - 2 * (this.x * this.x + this.y * this.y));
        float beta = (float) Math.asin(2 * (this.w * this.y - this.z * this.x));
        float gamma = (float) Math.atan2(2 * (this.w * this.z + this.x * this.y), 1 - 2 * (this.y * this.y + this.z * this.z));
        alpha *= 180 / (float) Math.PI;
        beta *= 180 / (float) Math.PI;
        gamma *= 180 / (float) Math.PI;
        return new float[]{alpha, beta, gamma};
    }
}
