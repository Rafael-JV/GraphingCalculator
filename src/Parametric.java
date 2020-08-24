package src;

public class Parametric extends Function {

    private SingleVariate x;
    private SingleVariate y;
    
    public Parametric(String xExpr, String yExpr, double minRange, double maxRange) {
        super(minRange, maxRange);
       this.x = new SingleVariate(xExpr, minRange, maxRange);
       this.y = new SingleVariate(yExpr, minRange, maxRange);
    }

    @Override
    public Point calc(double t) {
        return new Point(this.x.calc(t).getY(), this.y.calc(t).getY());
    }
    
    @Override
    public String toString() {
    	return x.toString() + ";" + y.toString();
    }

}
