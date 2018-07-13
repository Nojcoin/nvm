package org.aion.avm.arraywrapper;

import org.aion.avm.internal.IDeserializer;
import org.aion.avm.internal.IObject;

import java.util.Arrays;

public class FloatArray extends Array {

    private float[] underlying;

    public static FloatArray initArray(int c){
        //IHelper.currentContractHelper.get().externalChargeEnergy(c * 32);
        return new FloatArray(c);
    }

    public FloatArray(int c) {
        this.underlying = new float[c];
    }

    // Deserializer support.
    public FloatArray(IDeserializer deserializer, long instanceId) {
        super(deserializer, instanceId);
    }

    public int length() {
        return this.underlying.length;
    }

    public float get(int idx) {
        return this.underlying[idx];
    }

    public void set(int idx, float val) {
        this.underlying[idx] = val;
    }

    public IObject avm_clone() {
        return new FloatArray(Arrays.copyOf(underlying, underlying.length));
    }

    public IObject clone() {
        return new FloatArray(Arrays.copyOf(underlying, underlying.length));
    }

    //========================================================
    // Methods below are used by runtime and test code only!
    //========================================================

    public FloatArray(float[] underlying) {
        this.underlying = underlying;
    }

    public float[] getUnderlying() {
        return underlying;
    }

    public void setUnderlyingAsObject(java.lang.Object u){
        this.underlying = (float[]) u;
    }

    public java.lang.Object getUnderlyingAsObject(){
        return underlying;
    }
}
