package patterns.decorator;

public class BaseReceipt implements Receipt {
    private final String content;

    public BaseReceipt(String content) {
        this.content = content;
    }

    @Override
    public String render() {
        return content;
    }
}
