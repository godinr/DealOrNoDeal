package dealornodeal;

public class Case {

    private int number;
    private double value;
    private boolean isOpen;
    private boolean isChosen;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public boolean isChosen() {
        return isChosen;
    }

    public void setChosen(boolean chosen) {
        isChosen = chosen;
    }

    public Case(int number, double value, boolean isOpen, boolean isChosen){
        this.number = number;
        this.value = value;
        this.isOpen = isOpen;
        this.isChosen = isChosen;
    }
}
