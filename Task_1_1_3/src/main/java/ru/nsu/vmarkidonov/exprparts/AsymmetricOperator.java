package ru.nsu.vmarkidonov.exprparts;

import java.util.Objects;

abstract class AsymmetricOperator extends Operator {
    @Override
    public int hashCode() {
        return Objects.hash(getClass(), exp1, exp2);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        AsymmetricOperator other = (AsymmetricOperator) obj;

        return exp1.equals(other.exp1) && exp2.equals(other.exp2);
    }
}
