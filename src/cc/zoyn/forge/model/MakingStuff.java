package cc.zoyn.forge.model;

/**
 * 表示一个 制作材料, 包括数量
 */
public class MakingStuff {

    private String name;
    private int amount;

    public MakingStuff(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "MakingStuff{" +
                "name='" + name + '\'' +
                ", amount=" + amount +
                '}';
    }
}
