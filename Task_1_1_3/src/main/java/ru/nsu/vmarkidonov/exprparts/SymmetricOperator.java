package ru.nsu.vmarkidonov.exprparts;

import java.util.Objects;

import static java.lang.Math.max;
import static java.lang.Math.min;

abstract class SymmetricOperator extends Operator {
    @Override
    public int hashCode() {
        int hash1 = exp1.hashCode();
        int hash2 = exp2.hashCode();

        return Objects.hash(getClass(), min(hash1, hash2), max(hash1, hash2));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        SymmetricOperator other = (SymmetricOperator) obj;

        return (exp1.equals(other.exp1) && exp2.equals(other.exp2))
                || (exp1.equals(other.exp2) && exp2.equals(other.exp1));
    }
}
