import java.io.Serializable;

public class Expression implements Serializable {
    private double first;
    private double second;
    private double solution;
    private boolean solved = false;

    private String operation;

    public Expression(double first, double second, String operation) {
        this.first = first;
        this.second = second;
        this.operation = operation;
    }

    public void solve(Applicable op) {
        this.solution = op.apply(first,second);
        solved = true;
    }

    public double getSolution() {
        return solution;
    }

    public String getOperation() {
        return operation;
    }

    public String toString() {
        if (!solved) {
            return first+operation+second;
        } else {
            return first+operation+second+"="+solution;
        }
    }

    public boolean isSolved() {
        return solved;
    }
}
