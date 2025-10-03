package ru.nsu.vmarkidonov.exprparts;

import java.util.Objects;

public abstract class SymmetricOperator extends Operator {
    @Override
    public int hashCode() {
        return Objects.hash(exp1, exp2);
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
